package com.lvb.baseApi.restful.order.web;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lvb.baseApi.common.result.AjaxResult;
import com.lvb.baseApi.common.result.ResultPage;
import com.lvb.baseApi.common.result.ResultPageData;
import com.lvb.baseApi.common.util.CurrentUser;
import com.lvb.baseApi.common.util.IdWorker;
import com.lvb.baseApi.common.util.UserBean;
import com.lvb.baseApi.restful.doctor.entity.DoctorInfoEntity;
import com.lvb.baseApi.restful.doctor.service.DoctorService;
import com.lvb.baseApi.restful.order.entity.OrderEntity;
import com.lvb.baseApi.restful.order.service.OrderService;
import com.lvb.baseApi.restful.order.vo.OrderInfoVo;
import com.lvb.baseApi.restful.user.entity.AppUserEntity;
import com.lvb.baseApi.restful.user.service.AppUserService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 邓小顺
 */
@RestController
@RequestMapping(value="/order")
public class RestOrder {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AppUserService appUserService;

    @Autowired
    private DoctorService doctorService;

    //医生订单列表
    @RequestMapping(value = "/getDoctorOrderList", method = RequestMethod.GET)
    public ResultPage getDoctorOrderList(Integer page, Integer pageSize, Integer stateCode,@CurrentUser UserBean user) throws Exception {
        Map<String,Object> map = new HashMap<>();
        if(user==null){
            return new ResultPage(203, "请先登录");
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity==null){
            return new ResultPage(203, "用户信息不存在!");
        };
        map.put("stateCode",stateCode==-1?null:stateCode);
        DoctorInfoEntity doctorInfoEntity = doctorService.getUserId(appUserEntity.getId());
        map.put("doctorId",doctorInfoEntity.getId());
        List<OrderInfoVo> oderInfoVoList = new ArrayList<>();
        IPage<OrderEntity> listPage = orderService.getDoctorOrderList(new Page<>(page, pageSize),map);
        for (OrderEntity orderEntity :listPage.getRecords()) {
            //判断图文是否超过30分钟
            if(orderEntity.getReply_time()!=null) {
                if (orderEntity.getStatus() == 20 && orderEntity.getInterrogation_type() == 10) {
                    if(orderEntity.getPrice_type()!=null){

                    if(orderEntity.getPrice_type().equals("11")){
                        Long min = RestOrder.TimeDiffMin(orderEntity.getReply_time());
                        if (min >= 30) {
                            orderEntity.setStatus(50);//已过期
                            orderService.saveOrUpdate(orderEntity);
                        }
                    }else if(orderEntity.getPrice_type().equals("12")){
                        Long min = RestOrder.TimeDiffMin(orderEntity.getReply_time());
                        if (min >= 1440) {
                            orderEntity.setStatus(50);//已过期
                            orderService.saveOrUpdate(orderEntity);
                        }
                    }

                    }
                }
            }
            //判断视频是否超过10分钟（600s）
            if(orderEntity.getStatus()==20&&orderEntity.getInterrogation_type()==20){
                if(orderEntity.getTalk_time()>=600){
                    orderEntity.setStatus(50);//已过期
                    orderService.saveOrUpdate(orderEntity);
                }
            };
            OrderInfoVo oderInfoVo = new OrderInfoVo();
            AppUserEntity doctorAppUserEntity = appUserService.getById(orderEntity.getUser_id());
            //doctorAppUserEntity.setNick_name(doctorAppUserEntity.getNick_name());
            oderInfoVo.setDoctorInfoEntity(doctorInfoEntity);
            oderInfoVo.setOrderEntity(orderEntity);
            oderInfoVo.setAppUserEntity(doctorAppUserEntity);
            oderInfoVoList.add(oderInfoVo);
        };
        ResultPageData resultPageData = new ResultPageData(listPage.getCurrent(),listPage.getTotal(),listPage.getPages(),oderInfoVoList);
        ResultPage resultPage = new ResultPage(200, "返回医生订单列表",resultPageData);
        return resultPage;
    }

    //订单列表
    @RequestMapping(value = "/getOrderList", method = RequestMethod.GET)
    public ResultPage getOrderList(Integer page, Integer pageSize, Integer stateCode,@CurrentUser UserBean user) throws Exception {
        Map<String,Object> map = new HashMap<>();
        if(user==null){
            return new ResultPage(203, "请先登录");
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity==null){
            return new ResultPage(203, "用户信息不存在!");
        };
        map.put("stateCode",stateCode==-1?null:stateCode);
        map.put("userId",appUserEntity.getId());
        List<OrderInfoVo> oderInfoVoList = new ArrayList<>();
        IPage<OrderEntity> listPage = orderService.getOrderList(new Page<>(page, pageSize),map);
        for (OrderEntity orderEntity :listPage.getRecords()) {
            if(orderEntity.getReply_time()!=null) {
                if (orderEntity.getStatus() == 20 && orderEntity.getInterrogation_type() == 10) {
                    if(orderEntity.getPrice_type()!=null) {
                        if (orderEntity.getPrice_type().equals("11")) {
                            Long min = RestOrder.TimeDiffMin(orderEntity.getReply_time());
                            if (min >= 30) {//30分钟
                                orderEntity.setStatus(50);//已过期
                                orderService.saveOrUpdate(orderEntity);
                            }
                        } else if (orderEntity.getPrice_type().equals("12")) {
                            Long min = RestOrder.TimeDiffMin(orderEntity.getReply_time());
                            if (min >= 1440) {//24小时
                                orderEntity.setStatus(50);//已过期
                                orderService.saveOrUpdate(orderEntity);
                            }
                        }
                    }
                }
            }
            //判断视频是否超过10 分钟（600s）
            if(orderEntity.getStatus()==20&&orderEntity.getInterrogation_type()==20){
                if(orderEntity.getTalk_time()>=600){
                    orderEntity.setStatus(50);//已过期
                    orderService.saveOrUpdate(orderEntity);
                }
            };
            OrderInfoVo oderInfoVo = new OrderInfoVo();
            DoctorInfoEntity doctorInfoEntity = doctorService.getById(orderEntity.getDoctor_id());
            AppUserEntity appUserEntity1 = appUserService.getUserId(doctorInfoEntity.getUser_id());
            oderInfoVo.setDoctorInfoEntity(doctorInfoEntity);
            oderInfoVo.setOrderEntity(orderEntity);
            oderInfoVo.setAppUserEntity(appUserEntity1);
            oderInfoVo.setAppDoctorUserEntity(appUserEntity);
            oderInfoVoList.add(oderInfoVo);
        };
        ResultPageData resultPageData = new ResultPageData(listPage.getCurrent(),listPage.getTotal(),listPage.getPages(),oderInfoVoList);
        ResultPage resultPage = new ResultPage(200, "返回用户订单列表",resultPageData);
        return resultPage;
    }

    //提交订单
    @RequestMapping(value = "/postOrderInfo", method = RequestMethod.POST)
    public AjaxResult postOrderInfo(@RequestBody Map<String,Object> orderInfoVo,@CurrentUser UserBean user) {
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity == null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject( orderInfoVo.get("orderInfoVo"));
        String pet_type = model.getString("pet_type");
        String interrogation_type = model.getString("interrogation_type");
        String doctor_info_id = model.getString("doctor_info_id");
        String price_type = model.getString("price_type");
        String weChatNo = model.getString("weChatNo");
        BigDecimal amount = new BigDecimal(0);
        if(interrogation_type.equals("10")){
            if(price_type.equals("11")){
                amount = new BigDecimal(3);
            }else{
                amount = new BigDecimal(48);
            }
        } else if(interrogation_type.equals("20")){
            amount = new BigDecimal(20);
        } else if(interrogation_type.equals("30")){
            if(price_type.equals("31")){
                amount = new BigDecimal(268);
            }else{
                amount = new BigDecimal(998);
            }
        }else {
            return AjaxResult.builder().code(203).msg("问诊方式错误！").build();
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(IdWorker.getFlowIdWorkerInstance().nextId()+"");
        orderEntity.setCreate_time(new Date());
        orderEntity.setName(appUserEntity.getUser_name());
        orderEntity.setUser_id(appUserEntity.getId());
        orderEntity.setDoctor_id(doctor_info_id);
        orderEntity.setInterrogation_type(Integer.parseInt(interrogation_type));
        orderEntity.setPet_type(Integer.parseInt(pet_type));
        orderEntity.setTwopay(false);
        orderEntity.setTwo_pay_amount(new BigDecimal(0));
        orderEntity.setAmount(amount);
        orderEntity.setStatus(10);
        orderEntity.setTalk_time(0);
        orderEntity.setPay(false);
        orderEntity.setWeChatNo(weChatNo);
        orderEntity.setPrice_type(price_type);
        orderService.saveOrUpdate(orderEntity);
        DoctorInfoEntity doctorInfoEntity = doctorService.getById(doctor_info_id);
        doctorInfoEntity.setOrder_number(doctorInfoEntity.getOrder_number()+1);
        doctorService.saveOrUpdate(doctorInfoEntity);
        Map<String,Object> map = new HashMap<>();
        map.put("orderId",orderEntity.getId());
        map.put("payAmount",amount);
        return AjaxResult.builder().result(map).code(200).msg("创建订单成功!").build();
    }

    //取消订单
    @RequestMapping(value = "/cancelOrder", method = RequestMethod.POST)
    public AjaxResult cancelOrder(@RequestBody String orderId,@CurrentUser UserBean user) {
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject(orderId);
        String id = model.getString("orderId");
        OrderEntity orderEntity = orderService.getById(id);
        if(orderEntity == null ){
            return AjaxResult.builder().code(203).msg("订单不存在!").build();
        };
        orderEntity.setStatus(40);
        orderService.saveOrUpdate(orderEntity);
        return AjaxResult.builder().code(200).msg("订单取消成功!").build();
    }

    //结束订单
    @RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
    public AjaxResult completeOrder(@RequestBody String orderId,@CurrentUser UserBean user) {
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject(orderId);
        String id = model.getString("orderId");
        OrderEntity orderEntity = orderService.getById(id);
        if(orderEntity == null ){
            return AjaxResult.builder().code(203).msg("订单不存在!").build();
        };
        orderEntity.setStatus(30);
        orderService.saveOrUpdate(orderEntity);
        return AjaxResult.builder().code(200).msg("此订单已完成!").build();
    }

    //订单状态统计
    @RequestMapping(value = "/getOrderStatus", method = RequestMethod.GET)
    public AjaxResult getOrderStatus(@CurrentUser UserBean user){
        Map<String,Object> map = new HashMap<>();
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        AppUserEntity appUserEntity = appUserService.getUserById(user.getOpenid());
        if(appUserEntity==null){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        int waitPayCount =  orderService.getWaitPayCount(appUserEntity.getId());
        int waitReplyCount =  orderService.getWaitReplyCount(appUserEntity.getId());
        int waitRepairCount =  orderService.getWaitRepairCount(appUserEntity.getId());
        map.put("waitPayCount",waitPayCount);
        map.put("waitReplyCount",waitReplyCount);
        map.put("waitRepairCount",waitRepairCount);
        return AjaxResult.builder().result(map).code(200).build();
    }

    //视频通话时间（按秒算，最大600s）
    @RequestMapping(value = "/durationOrder", method = RequestMethod.POST)
    public AjaxResult durationOrder(@RequestBody String callOrderInfo,@CurrentUser UserBean user){
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject(callOrderInfo);
        OrderEntity orderEntity = null;
        if(model.containsKey("orderId")){
            String id = model.getString("orderId");
            orderEntity = orderService.getById(id);
        }else{
            String from = model.getString("from");
            orderEntity = orderService.getByUserName(from);
        }
        int duration = Integer.parseInt(model.getString("duration"));
        if(orderEntity == null ){
            return AjaxResult.builder().code(203).msg("订单不存在!").build();
        }
        orderEntity.setTalk_time(orderEntity.getTalk_time()+duration);
        orderService.saveOrUpdate(orderEntity);
        return AjaxResult.builder().code(200).msg("订单通话时长："+orderEntity.getTalk_time()+duration+"秒").build();
    }

    //医生回复时间
    @RequestMapping(value = "/saveReplyTime", method = RequestMethod.POST)
    public AjaxResult saveReplyTime(@RequestBody String callOrderInfo,@CurrentUser UserBean user){
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject(callOrderInfo);
        String id = model.getString("orderId");
        OrderEntity orderEntity = orderService.getById(id);
        if(orderEntity == null ){
            return AjaxResult.builder().code(203).msg("订单不存在!").build();
        };
        if(orderEntity.getReply_time()==null) {
            orderEntity.setReply_time(new Date());
            orderService.saveOrUpdate(orderEntity);
        }
        return AjaxResult.builder().code(200).msg("医生回复时间："+orderEntity.getReply_time()).build();
    }

    //判断订单是否过期
    @RequestMapping(value = "/judgeOrderExpired", method = RequestMethod.POST)
    public AjaxResult judgeOrderExpired(@RequestBody String orderInfo,@CurrentUser UserBean user){
        if(user ==null ){
            return AjaxResult.builder().code(203).msg("用户信息不存在!").build();
        };
        JSONObject model = JSONObject.fromObject(orderInfo);
        String id = model.getString("orderId");
        OrderEntity orderEntity = orderService.getById(id);
        if(orderEntity == null ){
            return AjaxResult.builder().code(203).msg("订单不存在!").build();
        };
        boolean orderExpired = false;
        if(orderEntity.getReply_time()!=null) {
            if (orderEntity.getInterrogation_type() == 10) {
                if(orderEntity.getPrice_type()!=null){
                    if(orderEntity.getPrice_type().equals("11")){
                        Long min = RestOrder.TimeDiffMin(orderEntity.getReply_time());
                        if (min >= 30) {
                            orderExpired=true;
                        }
                    }else if(orderEntity.getPrice_type().equals("12")){
                        Long min = RestOrder.TimeDiffMin(orderEntity.getReply_time());
                        if (min >= 1440) {
                            orderExpired=true;
                        }
                    }

                }
            }
        }
        //判断视频是否超过10分钟（600s）
        if(orderEntity.getInterrogation_type()==20){
            if(orderEntity.getTalk_time()>=600){
                orderExpired=true;
            }
        };
        return AjaxResult.builder().result(orderExpired).code(200).build();
    }

    /*
     * 是否超过30
     * */
    public static Long TimeDiffMin(Date pBeginTime) {
        Calendar dateOne=Calendar.getInstance();
        Calendar dateTwo=Calendar.getInstance();
        dateOne.setTime(new Date());    //设置为当前系统时间
        dateTwo.setTime(pBeginTime);    //获取数据库中的时间
        long timeOne=dateOne.getTimeInMillis();
        long timeTwo=dateTwo.getTimeInMillis();
        long minute=(timeOne-timeTwo)/(1000*60);//转化minute
        return minute;
    }
    /*
    * 是否超过一天
    * */
    public static Long TimeDiffDay(Date pBeginTime) {
        Calendar dateOne=Calendar.getInstance();
        Calendar dateTwo=Calendar.getInstance();
        dateOne.setTime(new Date());    //设置为当前系统时间
        dateTwo.setTime(pBeginTime);    //获取数据库中的时间
        long timeOne=dateOne.getTimeInMillis();
        long timeTwo=dateTwo.getTimeInMillis();
        long minute=(timeOne-timeTwo)/(1000*60);//转化minute
        return minute;
    }

}
