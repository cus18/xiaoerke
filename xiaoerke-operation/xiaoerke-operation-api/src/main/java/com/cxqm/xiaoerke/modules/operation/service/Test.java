package com.cxqm.xiaoerke.modules.operation.service;

import java.util.Date;

public class Test {
    public static void main(String[] args) {
//        String t = "ary||bfgdfg||cfdghdfgh||fghdhgd";
//
//        String[] temp = t.split("\\|\\|");
        Date date = new Date();
        date.setYear(2016);
        date.setMonth(1);
        date.setDate(1);

        Date date1 = new Date();
        date1.setYear(2016);
        date1.setMonth(10);
        date1.setDate(1);


        System.out.println("==========" + (date1.getTime() - date.getTime()));
        System.out.println("=========="+365l*24*60*60*1000);
    }
}
