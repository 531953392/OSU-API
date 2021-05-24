package com.lvb.baseApi.restful.pay.web;


import com.alibaba.fastjson.JSONObject;
import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.IdWorker;
import com.lvb.baseApi.common.util.SendMessage;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import com.lvb.baseApi.restful.doctor.service.DoctorService;
import com.lvb.baseApi.restful.order.entity.OrderEntity;
import com.lvb.baseApi.restful.order.service.OrderService;
import com.lvb.baseApi.restful.pay.entity.PayOrderEntity;
import com.lvb.baseApi.restful.pay.sdk.*;
import com.lvb.baseApi.restful.pay.service.PayOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WeiXinPaymentController {

    @Value("${config.localhost}")
    private String serviceUrl;

    @Autowired
    private OrderService orderService;
    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private DoctorService doctorService;

    private static final Logger LOG = LoggerFactory.getLogger(WeiXinPaymentController.class);

    /**
     * 微信统一下单接口
     * @return
     */
    @RequestMapping(value = "api/payApply", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResult doUnifiedOrder(@RequestBody String payInfo, @CurrentUser UserBean user) {
        if (user == null) {
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        Map resultMap=new HashMap();
        JSONObject model = JSONObject.parseObject(payInfo);
        String amount = model.getString("payAmount");
        String orderId = model.getString("orderId");
        Integer payType = Integer.parseInt(model.getString("payType"));
        PayOrderEntity payOrderEntity = payOrderService.getByOrderId(orderId,payType);
        if (payOrderEntity != null&&payOrderEntity.getPay_result_code()!=null) {
            if (payOrderEntity.getPay_result_code().equals("SUCCESS")) {
                return AjaxResult.builder().code(202).msg("订单已支付，请勿重复操作!").build();
            }
        }
        String goodsContent = model.getString("goodsContent");
        BigDecimal txnAmt = new BigDecimal(amount);
        BigDecimal money = txnAmt.multiply(new BigDecimal(100));
        String openid = user.getOpenid();
        MyConfig config = null;
        WXPay wxpay =null;
        try {
            config = new MyConfig();
            wxpay= new WXPay(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //生成的随机字符串
        String nonce_str = WXPayUtil.generateNonceStr();
        //获取客户端的ip地址
        //获取本机的ip地址
        InetAddress addr = null;
        try {
            addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        String spbill_create_ip = addr.getHostAddress();
        //支付金额，需要转成字符串类型，否则后面的签名会失败
        BigDecimal  total_fee = money ;
        //商品描述
        String body = goodsContent;
        //商户订单号
        String out_trade_no= WXPayUtil.generateNonceStr();
        //统一下单接口参数
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("appid", config.getAppID());
        data.put("body", body);
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", nonce_str);
        data.put("notify_url", serviceUrl + "api/payNotify");
        data.put("out_trade_no",out_trade_no);
        data.put("spbill_create_ip", spbill_create_ip);
        data.put("total_fee", String.valueOf(total_fee));
        //data.put("total_fee", "1");
        data.put("trade_type", "JSAPI");
        data.put("openid", openid);
        data.put("sign_type", WXPayConstants.MD5);
        try {
            Map<String, String> rMap = wxpay.unifiedOrder(data);
            System.out.println("统一下单接口返回: " + rMap);
            String return_code = (String) rMap.get("return_code");
            String result_code = (String) rMap.get("result_code");
            String nonceStr = WXPayUtil.generateNonceStr();
            resultMap.put("appId",config.getAppID());
            resultMap.put("nonceStr", nonceStr);
            Long timeStamp = System.currentTimeMillis() / 1000;
            if ("SUCCESS".equals(return_code) && return_code.equals(result_code)) {
                String prepayid = rMap.get("prepay_id");
                resultMap.put("package", "prepay_id="+prepayid);
                resultMap.put("signType", "MD5");
                //这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                resultMap.put("timeStamp", timeStamp.toString());
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String sign = WXPayUtil.generateSignature(resultMap, config.getKey());
                resultMap.put("paySign", sign);
                System.out.println("支付参数: " + resultMap);
                System.out.println("生成的签名paySign : "+ sign);
                //生成支付订单
                OrderEntity order = orderService.getById(orderId);
                if (order != null) {
                    if (payOrderEntity == null) {
                        PayOrderEntity payOrder = new PayOrderEntity();
                        payOrder.setId(IdWorker.getFlowIdWorkerInstance().nextId() + "");
                        payOrder.setOrder_id(order.getId());
                        payOrder.setUser_id(order.getUser_id());
                        payOrder.setAmount(txnAmt.doubleValue());
                        payOrder.setPay_time(new Date());
                        payOrder.setPay_type(payType);
                        payOrder.setMerId(config.getMchID());
                        payOrder.setTraceNo(out_trade_no);
                        payOrder.setBody(goodsContent);
                        payOrder.setPay_result_code("");
                        payOrderService.save(payOrder);
                    } else {
                        payOrderEntity.setAmount(txnAmt.doubleValue());
                        payOrderEntity.setPay_time(new Date());
                        payOrderEntity.setTraceNo(out_trade_no);
                        payOrderEntity.setBody(goodsContent);
                        payOrderService.saveOrUpdate(payOrderEntity);
                    }
                }

                return AjaxResult.builder().code(200).msg("业务受理成功!").result(resultMap).build();
            }else{
                return AjaxResult.builder().code(202).msg("网络错误,请重试!").result(null).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.builder().code(202).msg("网络错误,请重试!").result(null).build();
        }
    }


    /**
     * 后台交易结果通知url
     */
    @RequestMapping(value = "api/payNotify", method = RequestMethod.POST)
    public String payNotify(@RequestBody String payInfo) throws Exception {
        Map<String, String> map = xmlToMap(payInfo);
        String out_trade_no = map.get("out_trade_no");
        String result_code = map.get("result_code");
        PayOrderEntity payOrder = payOrderService.getByMerOrderId(out_trade_no);
        if (payOrder != null) {
            payOrder.setPay_result_code(result_code);
            payOrderService.saveOrUpdate(payOrder);
        };

        return "SUCCESS";
    }
    public  Map<String, String> xmlToMap(String strXml) throws Exception {
        Map<String, String> data = new HashMap<String, String>();
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
        InputStream stream = new ByteArrayInputStream(strXml.getBytes("UTF-8"));
        org.w3c.dom.Document doc = documentBuilder.parse(stream);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        for (int idx=0; idx<nodeList.getLength(); ++idx) {
            Node node = nodeList.item(idx);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                data.put(element.getNodeName(), element.getTextContent());
            }
        }
        try {
            stream.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    /**
     * 支付成功回调
     */
    @RequestMapping(value = "api/paySuccess", method = RequestMethod.POST)
    public AjaxResult paySuccess(@RequestBody String payOrderInfo) {

        JSONObject model = JSONObject.parseObject(payOrderInfo);
        String id = model.getString("orderId");
        Integer payType =Integer.parseInt(model.getString("payType"));
        OrderEntity orderEntity = orderService.getById(id);
        if(payType ==10){
            orderEntity.setStatus(20);
            orderEntity.setPay_time(new Date());
            orderEntity.setPay(true);
        }else if (payType ==20){
            orderEntity.setTwopay(true);
        };
        orderService.saveOrUpdate(orderEntity);

        DoctorInfoEntity doctorInfoEntity = doctorService.getById(orderEntity.getDoctor_id());
        if(doctorInfoEntity!=null){
            //发送短信通知
            SendMessage.send("（优嗅云宠医）用户的订单已支付成功，请尽快进入小程序进行查看回复！",doctorInfoEntity.getUser_name());
        };
        return AjaxResult.builder().code(200).result(null).build();
    }


}
