package com.boot.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

public class TransIdUtils {
    /**
     * @Title: buildRandom
     * @Description: 生成1000000-1999999随机数
     * @param @return
     * @return String
     * @throws
     */
    private static String buildRandom() {
        Long random = Math.round(Math.random() * 10000000L + 10000000L);
        return random + "";
    }

    public static String buildTransId() {
        String orderid = "";
        String curDate = FastDateFormat.getInstance("yyyyMMddHHmmssSSS").format(new Date());
        String random = buildRandom();
        orderid = orderid + curDate + random;
        return orderid;
    }

    public static String buildOrderNo(){
        return "CSYY"+buildTransId();
    }

    public static void main(String... args) {
        System.out.println("output:>>>" + buildOrderNo());
    }
}
