package com.lvb.baseApi.restful.pay.sdk;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.util.*;

import static io.netty.util.internal.StringUtil.byteToHexString;


public class SignUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 签名
     *
     * @param reqParams
     *            reqParams
     * @return req json
     */
    public static String getSign(Map<String,Object> reqParams, String key) {

        // 过滤空值、sign参数
        Map<String, Object> sParaNew = paramFilter(reqParams);
        // 获取待签名字符串
        String preSignStr = getParamString(sParaNew);
        System.out.println("待签名字符串："+preSignStr+",key:"+key);
        return getSign(preSignStr, key);
    }

    /**
     * 参数签名
     *
     * @param preSignStr
     *            待签名字符串
     * @param key
     *            签名Key
     *
     * @return 签名
     */
    public static String getSign(String preSignStr, String key) {
        String temp = preSignStr + "&key=" + key;
        LOG.info("待签名字符串：{}", temp);
        return MD5Encode(temp, "UTF-8").toUpperCase();
    }

    /**
     * 将参数按ASCII码排序
     *
     * @param params
     * @return
     */
    public static String getParamString(Map<String, Object> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key).toString();
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    /**
     * Description:MD5工具生成token
     */
    public static String MD5Encode(String target, String charset) {
        String md5Str = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] bytes = md5.digest(charset==null?target.getBytes():target.getBytes(charset));
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bytes) {
                int bt = b & 0xff;
                if (bt < 16) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(bt));
            }
            md5Str = stringBuffer.toString();
        } catch (Exception ex) {
            LOG.error("encrypt error,target:" + target, ex);
        }
        return md5Str.toUpperCase();
     }

    private static String byteArrayToHexString(byte b[]) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            resultSb.append(byteToHexString(b[i]));

        return resultSb.toString();
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray
     *            签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    private static Map<String, Object> paramFilter(Map<String, Object> sArray) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (MapUtils.isEmpty(sArray)) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key).toString();
            if (StringUtils.isEmpty(value) || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }


}
