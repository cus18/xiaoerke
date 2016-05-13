package com.cxqm.xiaoerke.modules.marketing.web;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cxqm.xiaoerke.common.utils.WechatUtil;


@Controller
@RequestMapping(value = "loveMarketing")
public class LoveMarketingController {

    @Autowired
    LoveMarketingService loveMarketingService;

    /**
     * 生成海报
     * */
    @RequestMapping(value = "/MarkeImage", method = {RequestMethod.POST, RequestMethod.GET})
    public @ResponseBody
    Map<String, Object> MarkeImage(HttpSession session)throws  Exception {
        Map<String,Object> map=new HashMap<String, Object>();
        String openid=session.getAttribute("openId").toString();
        LoveMarketing modle=new LoveMarketing();
        modle.setOpenid(openid);
        loveMarketingService.saveLoveMarketing(modle);
        String id=modle.getId();
        if(id!=null){
           Map<String,Object> m= loveMarketingService.getNicknameAndHeadImageByOpenid(openid);
            if(m.get("headImage")!=null){
                loveMarketingService.download(m.get("headImage").toString(),modle.getId(),"/Users/feibendechayedan/Downloads/headImage/");
                String qrcodeurl=loveMarketingService.getUserQRcode(modle.getId());

            }
        }else{
            map.put("src","addFault");
            return  map;
        }
        return map;
    }


    /**
     * 添加图片水印
     *
     * @param targetImg
     *            目标图片路径，如：C:\\kutuku.jpg
     * @param waterImg
     *            水印图片路径，如：C:\\kutuku.png
     * @param x
     *            水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y
     *            水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha
     *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public final static void pressImage(String targetImg, String waterImg,
                                        int x, int y, float alpha) {
        try {
            // 加载目标图片
            File file = new File(targetImg);
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            // 将目标图片加载到内存。
            BufferedImage bufferedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);

            // 加载水印图片。
            Image waterImage = ImageIO.read(new File(waterImg));
            int width_1 = waterImage.getWidth(null);
            int height_1 = waterImage.getHeight(null);
            // 设置水印图片的透明度。
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 设置水印图片的位置。
            int widthDiff = width - width_1;
            int heightDiff = height - height_1;
            if (x < 0) {
                x = widthDiff / 2;
            } else if (x > widthDiff) {
                x = widthDiff;
            }
            if (y < 0) {
                y = heightDiff / 2;
            } else if (y > heightDiff) {
                y = heightDiff;
            }

            // 将水印图片“画”在原有的图片的制定位置。
            g.drawImage(waterImage, x, y, width_1, height_1, null);
            // 关闭画笔。
            g.dispose();

            // 保存目标图片。
            ImageIO.write(bufferedImage, "", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加文字水印
     *
     * @param targetImg
     *            目标图片路径，如：C:\\kutoku.jpg
     * @param pressText
     *            水印文字， 如：kutuku.com
     * @param fontName
     *            字体名称， 如：宋体
     * @param fontStyle
     *            字体样式，如：粗体和斜体(Font.BOLD|Font.ITALIC)
     * @param fontSize
     *            字体大小，单位为像素
     * @param color
     *            字体颜色
     * @param x
     *            水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
     * @param y
     *            水印文字距离目标图片上侧的偏移量，如果y<0, 则在正中间
     * @param alpha
     *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
     */
    public  void pressText(String targetImg, String pressText,
                                 String fontName, int fontStyle, int fontSize, Color color, int x,
                                 int y, float alpha) {
        try {
            // 加载目标图片
            File file = new File(targetImg);
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            // 将目标图片加载到内存。
            BufferedImage bufferedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            g.setFont(new Font(fontName, fontStyle, fontSize));
            g.setColor(color);
            // 设置水印图片的透明度。
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    alpha));

            // 设置水印图片的位置。
            int width_1 = fontSize * getLength(pressText);
            int height_1 = fontSize;
            int widthDiff = width - width_1;
            int heightDiff = height - height_1;
            if (x < 0) {
                x = widthDiff / 2;
            } else if (x > widthDiff) {
                x = widthDiff;
            }
            if (y < 0) {
                y = heightDiff / 2;
            } else if (y > heightDiff) {
                y = heightDiff;
            }

            // 将水印文字“写”在原有的图片的制定位置。
            g.drawString(pressText, x, y + height_1);
            // 关闭画笔。
            g.dispose();

            // 保存目标图片。
            ImageIO.write(bufferedImage,"/Users/feibendechayedan/Downloads/headImage/", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算字符串的长度.  一个字符长度为1，一个汉字的长度为2.
     * @param value
     * @return int
     */
    public  int getLength(String value) {
        if (value == null)
            return 0;
        StringBuffer buff = new StringBuffer(value);
        int length = 0;
        String stmp;
        for (int i = 0; i < buff.length(); i++) {
            stmp = buff.substring(i, i + 1);
            try {
                stmp = new String(stmp.getBytes("utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (stmp.getBytes().length > 1) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }




}
