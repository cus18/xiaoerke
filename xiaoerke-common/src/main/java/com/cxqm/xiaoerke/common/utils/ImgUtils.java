package com.cxqm.xiaoerke.common.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * 合成图片
 * Created by Administrator on 2016/8/1 0001.
 */
public class ImgUtils {

    /**
     * 合成邀请卡图片
     *
     * @param headUrl 头像
     * @param codeUrl 二维码
     * @param outSrc 暂存图片路径
     * @param width 头像偏移量
     * @param height 头像偏移量
     * @param width1 二维码偏移量
     * @param height1 二维码偏移量
     */
    public static void composePic(String headUrl,String codeUrl,String outSrc,int width,int height,int width1,int height1) {
        String fileSrc = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/olympicBaby_inviteBaseImg.png";

        try {
            URL url = new URL(fileSrc);
            HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
            Image bg_src = javax.imageio.ImageIO.read(httpUrl.getInputStream());

            URL url1 = new URL(headUrl);
            HttpURLConnection httpUrl1 = (HttpURLConnection) url1.openConnection();
            Image logo_src = javax.imageio.ImageIO.read(httpUrl1.getInputStream());

            URL url2 = new URL(codeUrl);
            HttpURLConnection httpUrl2 = (HttpURLConnection) url2.openConnection();
            Image code_src = javax.imageio.ImageIO.read(httpUrl2.getInputStream());

            int bg_width = bg_src.getWidth(null);
            int bg_height = bg_src.getHeight(null);

            BufferedImage tag = new BufferedImage(bg_width, bg_height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = tag.createGraphics();
            g2d.drawImage(bg_src, 0, 0, bg_width, bg_height, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f)); //透明度设置开始 
            g2d.drawImage(logo_src, width, height, 126, 126, null);
            g2d.drawImage(code_src, width1, height1, 204, 204, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER)); //透明度设置 结束

            FileOutputStream out = new FileOutputStream(outSrc);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(tag);
            out.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传图片站至阿里云
     *
     * @param id
     * @param path
     * @throws Exception
     */
    public static void uploadImage(String id , String path) {
        try {
            File file = new File(URLDecoder.decode(path, "utf-8"));

            FileInputStream inputStream = new FileInputStream(file);
            long length = file.length();
            //上传图片至阿里云
            OSSObjectTool.uploadFileInputStream(id, length, inputStream, OSSObjectTool.BUCKET_ARTICLE_PIC);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        Long star = System.currentTimeMillis();

        String img1 = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/other%2Fliangp.png";//头像、
        String img2 = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/%25E6%2580%258E%25E4%25B9%2588%25E8%25A1%25A5%25E9%2593%2581%25E6%259C%2580%25E5%25AE%2589%25E5%2585%25A8%281%29.jpg";//二维码
        String outPath = System.getProperty("user.dir").replace("bin", "uploadImg")+"\\image\\xxx.png";
        ImgUtils.composePic(img1,img2, outPath, 71, 231,188,506);

//        try {
//            ImgUtils.uploadImage("olympicBaby_inviteBaseImg.png", "C:\\Users\\Administrator\\Desktop\\baseImg.png");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        Long end =System.currentTimeMillis();
        System.out.print("time====:"+(end-star));
    }

}