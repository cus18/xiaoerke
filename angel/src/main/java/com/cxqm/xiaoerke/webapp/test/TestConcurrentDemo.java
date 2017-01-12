package com.cxqm.xiaoerke.webapp.test;

import com.cxqm.xiaoerke.common.utils.DateUtils;

/**
 * Created by guozengguang on 2016/4/22.
 */
public class TestConcurrentDemo {

    //public  boolean isFinished = false;
    //private static String url = "http://localhost:8080/keeper/patient/wxChat";
    private static String url = "http://s201.xiaork.com/keeper/patient/wxChat";
    private static final int MAX_THREADS_COUNT = 100;//初始化开启最大线程数
    //private static final int MAX_THREADS_COUNT = 1;
    private static final int MESSAGE_COUNT = 3;//模拟一个用户向服务器发送的消息总数

    public static String getUrl(){
        return url;
    }
    public static int getMessageCount(){
        return MESSAGE_COUNT;
    }

    public void process(){
        //ExecutorService executorService =  Executors.newFixedThreadPool(5000);
        //executorService.execute(new TestSimulator(1));
        for (int i = 0; i < MAX_THREADS_COUNT; i++) {

            new Thread(new TestSimulator(i,"测试线程" + i)).start();
            //System.out.println("线程" +i);
        }


        //isFinished = true;
    }

    public static void main(String[] args) {
        TestConcurrentDemo testConcurrentDemo = new TestConcurrentDemo();
        testConcurrentDemo.process();
        //System.out.println();
        //Map<String,Object> map1 =new HashMap<String, Object>();
        //Map<String,Object> map2 =new HashMap<String, Object>();
        //map1.put("a",1);
        //map2.put("b",2);
        //map1.putAll(map2);
        //System.out.println(map1);
        //String id = "99" + "120006306".substring(1);
        //System.out.println("begin:"+id);
        //System.out.println("还原:1"+id.substring(2));
        //String str = "payment_type=1&subject=ceshi&trade_no=2016071421001004310232035444&buyer_email=13552482692" + "&gmt_create=2016-07-14 11:39:46&notify_type=trade_status_sync&quantity=1&out_trade_no=120000003"
        //    + "&seller_id=2088911946127712&notify_time=2016-07-14 12:03:05&body=ceshi&trade_status=TRADE_SUCCESS" + "&is_total_fee_adjust=N&total_fee=0.01&gmt_payment=2016-07-14 11:39:47&seller_email=chenxingqiming@163.com&price=0.01"
        //    + "&buyer_id=2088022943838313&notify_id=b75b81779e44759007eb462c31e6283ie6&use_coupon=N&sign_type=MD5&sign=f196bd19ea85da314e55c82a87f265e7";
        //String[] strArr = str.split("&");
        //System.out.println(strArr.length);
        //Map<String,String> map = new HashMap<String, String>();
        //for (int i = 0; i <strArr.length ; i++) {
        //    String[] arr = strArr[i].split("=");
        //    map.put(arr[0],arr[1]);
        //}
        //System.out.println(map.size());
        //String id = "99" + "120006306".substring(1);
        //System.out.println("begin:"+id);
        //System.out.println("还原:1"+id.substring(2));
        String startDate = "2016-05-12";
        String endDate = DateUtils.formatDate(DateUtils.addDays(DateUtils.StrToDate(startDate, "date"), +5), "yyyy-MM-dd");
        System.out.println(endDate);
    }

}
