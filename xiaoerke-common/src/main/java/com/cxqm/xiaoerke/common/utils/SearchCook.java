package com.cxqm.xiaoerke.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchCook {

    //id是商品id，url是商品图片地址,name是商品名称，num是要记录的个数
    public static void writecookie(String id, String url, String name,
                                   HttpServletRequest request, HttpServletResponse response,
                                   Integer num)  {
        if (ishave(id, request) == true) {
            ArrayList<Cookiebean> arl = checkcookie(request);

            Integer abc = 0;
            if (arl != null) {
                Iterator<Cookiebean> it = arl.iterator();

                while (it.hasNext()) {
                    Cookiebean at = it.next();
                    if (at != null) {
                        abc = at.getXuhao() + 1;
                        if (abc > num) {
                            Cookiebean cb = arl.get(0);


                            Cookie ck = null;
                            try {
                                ck = new Cookie("myproductid" + abc, URLEncoder.encode(cb.getIdurlname(), "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            ck.setMaxAge(0);
                            ck.setPath("/");
                            response.addCookie(ck);
                            it.remove();
                        }
                        if (abc <= num) {
                            Cookie ck = null;
                            try {
                                ck = new Cookie("myproductid" + abc, URLEncoder.encode(at.getIdurlname(), "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            ck.setMaxAge(3600);
                            ck.setPath("/");
                            response.addCookie(ck);
                        }
                    }


                }
            }
// 序号,编号+"lai"+url+”huan“+name， lai和huan作为分隔符，其它字母也行
            try{
                Cookie ckk = new Cookie("myproductid1", URLEncoder.encode(id.toString()+ "lai" + url + "huan" + name, "UTF-8"));
                ckk.setMaxAge(3600);
                ckk.setPath("/");
                response.addCookie(ckk);
            }catch (Exception e){e.printStackTrace();}


        }


    }


    public static boolean ishave(String id, HttpServletRequest request) {
        boolean ha = true;
        String cb = null;
        String cc = null;
        Cookie[] cookie = request.getCookies();
        if (cookie != null && cookie.length > 0) {
            for (Cookie co : cookie) {
                if (co.getName().indexOf("myproductid") == 0) {
// 序号,编号+"lai"+url+”huan“+name， lai和huan作为分隔符，其它字母也行
                    try {
                        cc = //co.getValue() ;
                        URLDecoder.decode(co.getValue(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    cb = cc.substring(0, cc.indexOf("lai"));
                    if (cb.equals(id)) {
                        ha = false;
                        break;
                    }


                }
            }


        }
        return ha;
    }


    public static ArrayList<Cookiebean> checkcookie(HttpServletRequest request) {
        ArrayList<Cookiebean> al = null;
        Integer caa = null;
        String cbb = null;
        al = new ArrayList<Cookiebean>();
        Cookie[] cookie = request.getCookies();
        if (cookie != null && cookie.length > 0) {
            for (Cookie coo : cookie) {
                if (coo.getName().indexOf("myproductid") == 0) {
// 序号,编号
                    caa = Integer.parseInt(coo.getName().substring(11,
                            coo.getName().length()));
                    try {
                        cbb = //coo.getValue();
                        URLDecoder.decode(coo.getValue(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Cookiebean ck = new Cookiebean();
                    ck.setIdurlname(cbb);
                    ck.setXuhao(caa);
                    al.add(ck);
                }
            }
        }

        return al;


    }

    public static ArrayList<Cookiebean> getSearchPatientS(HttpServletRequest request) {
        ArrayList<Cookiebean> al = null;
        Integer caa = null;
        String cbb = null;
        al = new ArrayList<Cookiebean>();
        Cookie[] cookie = request.getCookies();
        if (cookie != null && cookie.length > 0) {
            for (Cookie coo : cookie) {
                if (coo.getName().indexOf("myproductid") == 0) {
// 序号,编号
                    caa = Integer.parseInt(coo.getName().substring(11,
                            coo.getName().length()));
                    try {
                        cbb = //coo.getValue();
                                URLDecoder.decode(coo.getValue(),"UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Cookiebean ck = new Cookiebean();
                    ck.setIdurlname(cbb.substring(cbb.indexOf("laihuan")+7));
                    ck.setXuhao(caa);
                    al.add(ck);
                }
            }
        }

        return al;


    }

    public static void removeAllSearchHistory(HttpServletRequest request,HttpServletResponse response){
        Cookie[] cookie = request.getCookies();
        if (cookie != null && cookie.length > 0) {
            for (Cookie coo : cookie) {
                coo.setMaxAge(0);
                coo.setPath("/");
                response.addCookie(coo);

            }
        }

    }

}