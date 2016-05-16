package com.cxqm.xiaoerke.modules.marketing.serviceimpl;

import com.cxqm.xiaoerke.modules.marketing.dao.LoveActivityCommentDao;
import com.cxqm.xiaoerke.modules.marketing.dao.LoveMarketingDao;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveActivityComment;
import com.cxqm.xiaoerke.modules.marketing.entity.LoveMarketing;
import com.cxqm.xiaoerke.modules.marketing.service.LoveMarketingService;
import com.cxqm.xiaoerke.modules.sys.service.SystemService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by feibendechayedan on 16/5/11.
 */
@Service
@Transactional(readOnly = false)
public class LoveMarketingServiceImpl implements LoveMarketingService {

    @Autowired
    private SystemService systemService;

    @Autowired
    private LoveMarketingDao loveMarketingDao;

    @Autowired
    private LoveActivityCommentDao loveActivityCommentDao;

    String token="tt74ZEASBvfPjnX90LAo47qXuRppIhk8pZRiWzTdAiOjxgOj1zKYw17rfCPdztov5id9Q1DKB_iSrF1Xc2DyI_z0A2VyrPQFf6FKwtgVOKUPVn7TKIYMBdBwAwhLFubZRITfAHANEP";

    @Override
    public Map<String, Object> getNicknameAndHeadImageByOpenid(String openid) {
        Map<String,Object> userAlone=new HashMap<String, Object>();
//        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+systemService.getWechatParameter()+"&openid="+openid+"&lang=zh_CN";
        String strURL="https://api.weixin.qq.com/cgi-bin/user/info?access_token="+token+"&openid="+openid+"&lang=zh_CN";
        String param="";
        String json=this.post(strURL, param, "GET");
        JSONObject jo=JSONObject.fromObject(json);
        if(jo.get("subscribe").toString().equals("0")){
            userAlone.put("subscribe","0");
            return userAlone;
        }
        userAlone.put("headImage", jo.get("headimgurl"));
        userAlone.put("name", jo.get("nickname"));
        return userAlone;
    }

    @Override
    public void saveLoveActivityComment(LoveActivityComment loveActivityComment) {
         loveActivityCommentDao.saveLoveActivityComment(loveActivityComment);
    }

    @Override
    public LoveActivityComment findLoveActivityComment() {
        List<LoveActivityComment> loveActivityCommentlist= loveActivityCommentDao.findLoveActivityComment();
        if(loveActivityCommentlist != null && loveActivityCommentlist.size() >0){
            LoveActivityComment loveActivityComment = loveActivityCommentlist.get(0);
            return loveActivityComment;
        }else{
            return null;
        }
    }

    /**
     * 新用户生成海报
     * @param m
     * @return
     */
    @Override
    public String getNewPosterImage(Map<String, Object> m) throws Exception {
        String openid=m.get("openid").toString();
        String headImageURI=m.get("headImage").toString();
        String DheadImageURI=this.download(headImageURI,openid+"head.jpg","/Users/feibendechayedan/Downloads/");
        String SheadImageURI=scale(DheadImageURI,DheadImageURI,"head",true);
        List<Map<String,String>> ImageList=new ArrayList<Map<String,String>>();
            Map<String,String> headImage=new HashMap<String, String>();
            headImage.put("type", "image");
            headImage.put("image", SheadImageURI);
            headImage.put("x", "234");
            headImage.put("y", "155");
        ImageList.add(headImage);
//        String DqrcodeImageURI=this.download(this.getUserQRcode(openid),openid+"qrcode.jpg","/Users/feibendechayedan/Downloads/");
        String SqrcodeImageURI=scale(this.getUserQRcode(openid),this.getUserQRcode(openid),"qrcode",false);
            Map<String,String> qrcodeImage=new HashMap<String, String>();
            qrcodeImage.put("type", "image");
            qrcodeImage.put("image", SqrcodeImageURI);
            qrcodeImage.put("x", "45");
            qrcodeImage.put("y", "1020");
        ImageList.add(qrcodeImage);
            Map<String,String> LogoImage=new HashMap<String, String>();
            LogoImage.put("type", "image");
            LogoImage.put("image", "/Users/feibendechayedan/Downloads/xin_png_03.png");
            LogoImage.put("x", "142");
            LogoImage.put("y", "1120");
        ImageList.add(LogoImage);
            Map<String,String> nickname=new HashMap<String, String>();
            nickname.put("type", "nickname");
            nickname.put("text", "我是"+m.get("name").toString());
            nickname.put("x", "302");
            nickname.put("y", "515");
        ImageList.add(nickname);
            Map<String,String> nums=new HashMap<String, String>();
            nums.put("type", "text");
            nums.put("text", m.get("id").toString());
            nums.put("x", "370");
            nums.put("y", "590");
        ImageList.add(nums);
        SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM-dd");
        Date today=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 30);
            Map<String,String> date=new HashMap<String, String>();
            date.put("type", "textdate");
            date.put("text", sdf.format(cal.getTime()).toString());
            date.put("x", "245");
            date.put("y", "1288");
        ImageList.add(date);
        String posterImagePath=this.makePosterImage("/Users/feibendechayedan/Downloads/2.pic_hd.jpg", ImageList, "/Users/feibendechayedan/Downloads/"+openid+"posterImage.jpg");
        return posterImagePath;
    }

    /**
     * 老用户更新海报
     * @param m
     * @return
     */
    @Override
    public String UpdatePosterImage(Map<String, Object> m) throws Exception {
        String openid=m.get("openid").toString();
        String headImageURI=m.get("headImage").toString();
        String DheadImageURI=this.download(headImageURI,openid+"head.jpg","/Users/feibendechayedan/Downloads/");
        String SheadImageURI=scale(DheadImageURI,DheadImageURI,"head",true);
        List<Map<String,String>> ImageList=new ArrayList<Map<String,String>>();
        Map<String,String> headImage=new HashMap<String, String>();
        headImage.put("type", "image");
        headImage.put("image", SheadImageURI);
        headImage.put("x", "234");
        headImage.put("y", "155");
        ImageList.add(headImage);
//        String DqrcodeImageURI=this.download(this.getUserQRcode(openid),openid+"qrcode.jpg","/Users/feibendechayedan/Downloads/");
        String SqrcodeImageURI=scale(this.getUserQRcode(openid),this.getUserQRcode(openid),"qrcode",false);
        Map<String,String> qrcodeImage=new HashMap<String, String>();
        qrcodeImage.put("type", "image");
        qrcodeImage.put("image", SqrcodeImageURI);
        qrcodeImage.put("x", "45");
        qrcodeImage.put("y", "1020");
        ImageList.add(qrcodeImage);
        Map<String,String> LogoImage=new HashMap<String, String>();
        LogoImage.put("type", "image");
        LogoImage.put("image", "/Users/feibendechayedan/Downloads/xin_png_03.png");
        LogoImage.put("x", "142");
        LogoImage.put("y", "1120");
        ImageList.add(LogoImage);
        Map<String,String> nickname=new HashMap<String, String>();
        nickname.put("type", "nickname");
        nickname.put("text", "我是"+m.get("name").toString());
        nickname.put("x", "202");
        nickname.put("y", "520");
        ImageList.add(nickname);
        Map<String,String> nums=new HashMap<String, String>();
        nums.put("type", "text");
        nums.put("text", m.get("id").toString());
        nums.put("x", "370");
        nums.put("y", "580");
        ImageList.add(nums);
        SimpleDateFormat sdf=new  SimpleDateFormat("yyyy-MM-dd");
        Date today=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(today);
        cal.add(Calendar.DATE, 30);
        Map<String,String> date=new HashMap<String, String>();
        date.put("type", "textdate");
        date.put("text", sdf.format(cal.getTime()).toString());
        date.put("x", "245");
        date.put("y", "1288");
        ImageList.add(date);

        List<Map<String,Object>> fmap=(List<Map<String,Object>>)m.get("openidList");
        for (int i=0;i<fmap.size();i++) {
            String fopenid=fmap.get(i).get("openid").toString();
            Map<String, Object> fimage1 = this.getNicknameAndHeadImageByOpenid(fopenid);
            String DFheadImageURI = this.download(fimage1.get("headImage").toString(), fopenid + i +"head.jpg", "/Users/feibendechayedan/Downloads/");
            String SFheadImageURI = scale(DFheadImageURI, DFheadImageURI,"friendsHead", true);
            Map<String, String> fHeadImage = new HashMap<String, String>();
            fHeadImage.put("type", "image");
            fHeadImage.put("image", SFheadImageURI);
            fHeadImage.put("x", (220+i*70)+"");
            fHeadImage.put("y", "625");
            ImageList.add(fHeadImage);
        }
        Map<String,String> friendNums=new HashMap<String, String>();
        friendNums.put("type", "text");
        friendNums.put("text", fmap.size()+"");
        friendNums.put("x", "430");
        friendNums.put("y", "638");
        ImageList.add(friendNums);
        String posterImagePath=this.makePosterImage("/Users/feibendechayedan/Downloads/1.pic_hd.jpg", ImageList, "/Users/feibendechayedan/Downloads/"+openid+"posterImage.jpg");
        return posterImagePath;
    }

    @Override
    public List<Map<String, Object>> getOpenidByMarketer(String id) {
        return loveMarketingDao.getOpenidByMarketer(id);
    }

    @Override
    public void updateInviteMan(String  id,String openid) {
        Map<String,Object> map= loveMarketingDao.getOpenidById(id);
        if(!map.get("openid").toString().equals(openid)) {
            return;
        }
        double lovemoney = map.get("lovemoney") != null ? Integer.parseInt(map.get("lovemoney").toString()) : 0;
        LoveMarketing l = new LoveMarketing();
        l.setId(id);
        l.setLoveMoney(lovemoney + 1);
            //二级邀请人 +0.2
        Map<String, Object> fm = loveMarketingDao.getOpenidByTopMarketer(id);
        if(fm.get("marketer")!=null&&!fm.get("marketer").toString().equals("")){
            String fmarketer=fm.get("marketer").toString();
            Map<String,Object> ffmap= loveMarketingDao.getOpenidById(fmarketer);
            double flovemoney = ffmap.get("lovemoney") != null ? Integer.parseInt(ffmap.get("lovemoney").toString()) : 0;
            LoveMarketing fl = new LoveMarketing();
            fl.setId(fmarketer);
            fl.setLoveMoney(flovemoney + 0.2);
            loveMarketingDao.updateLoveMarketing(l);
            loveMarketingDao.updateLoveMarketing(fl);
        }else{
            loveMarketingDao.updateLoveMarketing(l);
        }

    }

    @Override
    public String getUserQRcode(String id) throws Exception{
        String url= "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+token;
        String jsonData="{\"expire_seconds\": 604800, \"action_name\": \"QR_SCENE\",\"action_info\": {\"scene\": {\"scene_id\": "+id+"}}}";
        String reJson=this.post(url, jsonData,"POST");
        System.out.println(reJson);
        JSONObject jb=JSONObject.fromObject(reJson);
        String qrTicket=jb.getString("ticket");
        String QRCodeURI="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+qrTicket;
        this.download(QRCodeURI,id+"qrcode.jpg","/Users/feibendechayedan/Downloads/");
        return "/Users/feibendechayedan/Downloads/"+id+"qrcode.jpg";
    }


    @Override
    public Map<String, Object> getLoveMarketingByOpenid(String openid) {
        return loveMarketingDao.getLoveMarketingByOpenid(openid);
    }

    @Override
    public int saveLoveMarketing(LoveMarketing loveMarketing) {
        return loveMarketingDao.saveLoveMarketing(loveMarketing);
    }

    @Override
    public int updateLoveMarketing(LoveMarketing loveMarketing) {
        return loveMarketingDao.updateLoveMarketing(loveMarketing);
    }

    /**
     * 在版图基础上添加用户信息
     * @param posterImagePath  版图地址
     * @param ImageList    集合
     * @param makeImagePath  生成图片的地址
     * @return
     */
    public String makePosterImage(String posterImagePath,List<Map<String,String>> ImageList,String makeImagePath){
        try {
            // 加载海报原图
            File file = new File(posterImagePath);
            Image image = ImageIO.read(file);
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            // 将目标图片加载到内存。
            BufferedImage bufferedImage = new BufferedImage(width, height,
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufferedImage.createGraphics();
            g.drawImage(image, 0, 0, width, height, null);
            // 设置水印图片的透明度。
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP,
                    1));
            for (int i = 0; i < ImageList.size(); i++) {
                Map<String,String> imageObject=ImageList.get(i);
                String type=imageObject.get("type");

                if(type.equals("image")){
                    String imageURI=imageObject.get("image");
                    // 加载水印图片。
                    Image waterImage = ImageIO.read(new File(imageURI));
                    int width_1 = waterImage.getWidth(null);
                    int height_1 = waterImage.getHeight(null);

                    // 设置水印图片的位置。
                    int x=Integer.parseInt(imageObject.get("x"));
                    int y=Integer.parseInt(imageObject.get("y"));
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
                }else if(type.equals("text")){
                    String pressText=imageObject.get("text");
                    g.setFont(new Font("微软雅黑", Font.BOLD, 40));
                    g.setColor(new Color(156,249,226));
                    // 设置水印图片的位置。
                    int width_1 = 30 * getLength(pressText);
                    int height_1 = 30;
                    int widthDiff = width - width_1;
                    int heightDiff = height - height_1;
                    int x=Integer.parseInt(imageObject.get("x"));
                    int y=Integer.parseInt(imageObject.get("y"));
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
                }else if(type.equals("textdate")){
                    String pressText=imageObject.get("text");
                    g.setFont(new Font("微软雅黑", Font.PLAIN, 22));
                    g.setColor(Color.white);
                    // 设置水印图片的位置。
                    int width_1 = 30 * getLength(pressText);
                    int height_1 = 30;
                    int widthDiff = width - width_1;
                    int heightDiff = height - height_1;
                    int x=Integer.parseInt(imageObject.get("x"));
                    int y=Integer.parseInt(imageObject.get("y"));
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
                }else if(type.equals("nickname")){
                    String pressText=imageObject.get("text");
                    g.setFont(new Font("微软雅黑", Font.PLAIN, 40));
                    g.setColor(Color.white);
                    // 设置水印图片的位置。
                    int width_1 = 30 * getLength(pressText);
                    int height_1 = 30;
                    int widthDiff = width - width_1;
                    int heightDiff = height - height_1;
                    int x=Integer.parseInt(imageObject.get("x"));
                    int y=Integer.parseInt(imageObject.get("y"));
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
                }
            }

            // 关闭画笔。
            g.dispose();

            // 保存目标图片。
            ImageIO.write(bufferedImage, "jpg", new File(makeImagePath));
            return makeImagePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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


    /**
     * 发送HttpPost请求
     *
     * @param strURL
     *            服务地址
     * @param params
     *            json字符串,例如: "{ \"id\":\"12345\" }" ;其中属性名必须带双引号<br/>
     *            type (请求方式：POST,GET)
     * @return 成功:返回json字符串<br/>
     */
    public  String post(String strURL, String params,String type) {
        System.out.println(strURL);
        System.out.println(params);
        try {
            URL url = new URL(strURL);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(type); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(params);
            out.flush();
            out.close();
            // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                System.out.println(result);
                return result;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null; // 自定义错误信息
    }

    /**
     * 下载图片
     * @param urlString
     * @param filename
     * @param savePath
     * @throws Exception
     */

    public  String download(String urlString, String filename,String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5*1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf=new File(savePath);
        if(!sf.exists()){
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath()+"/"+filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
        return sf.getPath()+"/"+filename;
    }

    /** *//**
     * 缩放图像
     * @param srcImageFile 源图像文件地址
     * @param result       缩放后的图像地址
     * @param flag         缩放选择:true 放大; false 缩小;
     */
    public String scale(String srcImageFile, String result, String flag,boolean head)
    {

        System.out.println("开始缩放");
        try
        {
            BufferedImage src = ImageIO.read(new File(srcImageFile)); // 读入文件
            int width = src.getWidth(); // 得到源图宽
            int height = src.getHeight(); // 得到源图长
            if (flag.equals("head"))
            {
                // 头像的尺寸
                width = 290;
                height = 290;
            }
            else if (flag.equals("qrcode"))
            {
                // 二维码尺寸
                width = 259;
                height = 259;
            }
            else if (flag.equals("friendsHead"))
            {
                // 被邀请人头像尺寸
                width = 50;
                height = 50;
            }

            Image image = src.getScaledInstance(width, height, Image.SCALE_DEFAULT);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
//            ImageIO.write(tag, "JPEG", new File(result));// 输出到文件流
            if(head){
                BufferedImage bi1 = tag;

                // 根据需要是否使用 BufferedImage.TYPE_INT_ARGB
                BufferedImage bi2 = new BufferedImage(bi1.getWidth(),bi1.getHeight(),BufferedImage.TYPE_INT_ARGB);

                Ellipse2D.Double shape = new Ellipse2D.Double(0,0,bi1.getWidth(),bi1.getHeight());

                Graphics2D g2 = bi2.createGraphics();
                g2.setClip(shape);
                // 使用 setRenderingHint 设置抗锯齿
                g2.drawImage(bi1,0,0,null);
                g2.dispose();

                ImageIO.write(bi2, "png", new File(result));
                return result;
            }else{
                ImageIO.write(tag, "png", new File(result));
                return result;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return result;
        }
    }

}


