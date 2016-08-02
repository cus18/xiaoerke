package com.cxqm.xiaoerke.common.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;

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

            Image logo_src = getRoundImage(headUrl);

            Image code_src = cutImage(codeUrl, 25, 25, 380, 380);

            int bg_width = bg_src.getWidth(null);
            int bg_height = bg_src.getHeight(null);

            BufferedImage tag = new BufferedImage(bg_width, bg_height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = tag.createGraphics();
            g2d.drawImage(bg_src, 0, 0, bg_width, bg_height, null);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,1.0f)); //透明度设置开始 
            g2d.drawImage(logo_src, width, height, 126, 126, null);
            g2d.drawImage(code_src, width1, height1, 215, 215, null);
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
     * 剪裁图片
     * @param src 原图片路径
     * @param x 开始位置
     * @param y 开始位置
     * @param w 剪裁宽
     * @param h 剪裁高
     * @return
     * @throws IOException
     */
    public static Image cutImage(String src,int x,int y,int w,int h) throws IOException{
        Iterator iterator = ImageIO.getImageReadersByFormatName("jpg");
        ImageReader reader = (ImageReader)iterator.next();

        URL url = new URL(src);
        HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();

        InputStream in=httpUrl.getInputStream();
        ImageInputStream iis = ImageIO.createImageInputStream(in);
        reader.setInput(iis, true);

        ImageReadParam param = reader.getDefaultReadParam();

        Rectangle rect = new Rectangle(x, y, w,h);
        param.setSourceRegion(rect);
        Image bi = reader.read(0,param);

        return bi;
    }

    /**
     * 获取圆图
     * @return
     */
    public static Image getRoundImage(String headUrl) throws Exception{
        URL url1 = new URL(headUrl);
        HttpURLConnection httpUrl1 = (HttpURLConnection) url1.openConnection();
        Image logo_src = javax.imageio.ImageIO.read(httpUrl1.getInputStream());

        BufferedImage bi1 = (BufferedImage) logo_src;

        // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB
        BufferedImage bi2 = new BufferedImage(bi1.getWidth(), bi1.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = bi2.createGraphics();

        // ---------- 增加下面的代码使得背景透明 -----------------
        bi2 = g2.getDeviceConfiguration().createCompatibleImage(bi1.getWidth(), bi1.getHeight(), Transparency.TRANSLUCENT);

        g2.dispose();

        g2 = bi2.createGraphics();
        //

        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, bi1.getWidth(), bi1
                .getHeight());

        g2.setBackground(Color.red);
        g2.draw(new Rectangle(bi2.getWidth(), bi2.getHeight()));
        g2.setClip(shape);
        //使用 setRenderingHint 设置抗锯齿
        g2.drawImage(bi1, 0, 0, null);

        g2.dispose();

        return bi2;
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
        img1 = "http://wx.qlogo.cn/mmopen/tqRiaNianNl1kJWsfxu2EwSCbuViaXB5NSpKS7YBHDdVBeRD64LiamibjVKvtvBBNaNn2KfVbAicG91oJL7nK0t48CU952Rr4Z9lpY/0";
        String img2 = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/%25E6%2580%258E%25E4%25B9%2588%25E8%25A1%25A5%25E9%2593%2581%25E6%259C%2580%25E5%25AE%2589%25E5%2585%25A8%281%29.jpg";//二维码
        img2="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGS8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL3ZrVTZmWXJsMm0yalNaUDhFV3MyAAIEOk2gVwMEgDoJAA==";
        String outPath = System.getProperty("user.dir").replace("bin", "uploadImg")+"\\image\\xxx.png";
        ImgUtils.composePic(img1,img2, outPath, 71, 231,185,500);

//        try {
//            ImgUtils.uploadImage("olympicBaby_inviteBaseImg.png", "C:\\Users\\Administrator\\Desktop\\showqrcode.jpg");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        String img1="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGS8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL3ZrVTZmWXJsMm0yalNaUDhFV3MyAAIEOk2gVwMEgDoJAA==";
//        String img2 = "C:\\Users\\Administrator\\Desktop\\showqrcode1.jpg";
//        try {
//            cutImage(img1,25,25,380,380);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        Long end =System.currentTimeMillis();
        System.out.print("time====:"+(end-star));
    }

}