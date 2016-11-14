package com.cxqm.xiaoerke.common.utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 合成图片
 * Created by Administrator on 2016/8/1 0001.
 */
public class ImgUtils {

    private static String zhPattern = "[\u4E00-\u9FA5]";

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
//            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//            encoder.encode(tag);
            ImageIO.write(tag,"png",out);
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
     * 判断远程图片是否存在
     *
     * @param httpPath
     * @return
     */
    public static Boolean existHttpPath(String httpPath){
        URL httpurl = null;
        try {
            httpurl = new URL(httpPath);
            URLConnection rulConnection = httpurl.openConnection();
            rulConnection.getInputStream();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 上传图片至阿里云
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
//        Long star = System.currentTimeMillis();
//
//        String img1 = "http://xiaoerke-appoint.oss-cn-beijing.aliyuncs.com/other%2Fliangp.png";//头像、
//        img1 = "http://wx.qlogo.cn/mmopen/tqRiaNianNl1kJWsfxu2EwSCbuViaXB5NSpKS7YBHDdVBeRD64LiamibjVKvtvBBNaNn2KfVbAicG91oJL7nK0t48CU952Rr4Z9lpY/0";
//        String img2 = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/%25E6%2580%258E%25E4%25B9%2588%25E8%25A1%25A5%25E9%2593%2581%25E6%259C%2580%25E5%25AE%2589%25E5%2585%25A8%281%29.jpg";//二维码
//        img2="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQGS8DoAAAAAAAAAASxodHRwOi8vd2VpeGluLnFxLmNvbS9xL3ZrVTZmWXJsMm0yalNaUDhFV3MyAAIEOk2gVwMEgDoJAA==";
//        String outPath = System.getProperty("user.dir").replace("bin", "uploadImg")+"\\image\\xxx.png";
//        ImgUtils.composePic(img1,img2, outPath, 71, 231,185,500);

//        createStringMark("/Users/wangbaowei/Downloads/bao_master.png", "宝\r大\rg",Color.white, 100,"/Users/wangbaowei/Downloads/bao_1.png");
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


//        Long end =System.currentTimeMillis();
//        System.out.print("time====:"+(end-star));
    }

    public static String createStringMark(String babyName,String fileSrc,String picCoordinate){
//
        Integer fontSize = 0;
        if(babyName.length()<3){
            fontSize =  160;
        }else if(babyName.length()<4){
            fontSize =  146;
        }else{
            fontSize =  110;
        }
        String[] coordinateList = picCoordinate.split(";");
        for(String coordinate:coordinateList){
            String[] coordinateInfo = coordinate.split(",");
            Integer x =Integer.parseInt(coordinateInfo[0]);
            Integer y =Integer.parseInt(coordinateInfo[1]);

            String dataTime = DateUtils.DateToStr(new Date(),"yyyyMMddHHmmss");
            File file =new File(System.getProperty("user.dir").replace("bin", "uploadImg"));
            //如果文件夹不存在则创建
            if  (!file .exists()  && !file .isDirectory()){
                System.out.println("//不存在");
                file .mkdir();
            } else{
                System.out.println("//目录存在");
            }
            String outPath = System.getProperty("user.dir").replace("bin", "uploadImg")+"/"+dataTime+babyName+".png";
            URL url = null;
            try {
                url = new URL(encode(fileSrc, "UTF-8"));
                HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
                Image theImg = javax.imageio.ImageIO.read(httpUrl.getInputStream());
                int width=theImg.getWidth(null)==-1?200:theImg.getWidth(null);
                int height= theImg.getHeight(null)==-1?200:theImg.getHeight(null);
                System.out.println(width);
                System.out.println(height);
                System.out.println(theImg);
                BufferedImage bimage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g=bimage.createGraphics();
                g.setColor(Color.white);
                g.setBackground(Color.red);
                g.drawImage(theImg, 0, 0, null );
                g.setFont(new Font(null,Font.BOLD,fontSize)); //字体、字型、字号
                g.drawString(babyName,x,y); //画文字
                g.dispose();
                FileOutputStream out=new FileOutputStream(outPath); //先用一个特定的输出文件名
                JPEGImageEncoder encoder =JPEGCodec.createJPEGEncoder(out);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimage);
                param.setQuality(100, true);
                encoder.encode(bimage, param);
                out.close();
                uploadImage(dataTime+babyName+".png", outPath);

                File imgFile = new File(outPath);
                if(imgFile.exists()){
                    imgFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            fileSrc = "http://xiaoerke-article-pic.oss-cn-beijing.aliyuncs.com/"+dataTime+babyName+".png";

        }

        return fileSrc;
    }

    public static String encode(String str, String charset) throws UnsupportedEncodingException {
        Pattern p = Pattern.compile(zhPattern);
        Matcher m = p.matcher(str);
        StringBuffer b = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(b, URLEncoder.encode(m.group(0), charset));
        }
        m.appendTail(b);
        return b.toString();
    }
}