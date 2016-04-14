package com.cxqm.xiaoerke.modules.wechat.service.impl;

import com.cxqm.xiaoerke.common.utils.StringUtils;
import com.cxqm.xiaoerke.modules.wechat.entity.SysWechatAppintInfoVo;
import com.cxqm.xiaoerke.modules.wechat.service.WechatAttentionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 该类用于咨询模拟测试 @Author deliang
 */
public class ConsultTest{
	@Autowired
	private WechatAttentionService wechatAttentionService;

	public void consultTest(int userNumber,String content){
		SysWechatAppintInfoVo sysWechatAppintInfoVo = new SysWechatAppintInfoVo();
		List<SysWechatAppintInfoVo> findAttentionInfoByOpenIdLists = wechatAttentionService.findAttentionInfo(sysWechatAppintInfoVo);
		//共有userNumber个用户,同时发送消息
		for(int i=0;i<=userNumber;i++){
			SysWechatAppintInfoVo vo = findAttentionInfoByOpenIdLists.get(i);

			String NickName = vo.getWechat_name();
			if(StringUtils.isBlank(vo.getWechat_name())){
				NickName = vo.getOpen_id().substring(0,10);
			}
			StringBuilder sb = new StringBuilder();
			sb.append("<xml>");
			sb.append("    <FromUserName>");
			sb.append(vo.getOpen_id());
			sb.append("    </FromUserName>");
			sb.append("    <MsgType>");
			sb.append("text");
			sb.append("    </MsgType>");
			sb.append("    <Content>");
			sb.append("我是");
			sb.append(NickName);
			sb.append("  ");
			sb.append(content);
			sb.append("    </Content>");
			sb.append("</xml>");
			Thread thread = new MyThread(sb.toString());
			thread.start();
		}
	}

	public class MyThread extends Thread
	{
		private String xmlInfo;
		public MyThread(String xmlInfo)
		{
			this.xmlInfo = xmlInfo;
		}
		public void run()
		{
			String url = "http://xiaoxiaoerke.cn/keeper/patient/wxChat";
			testPost(url,xmlInfo);
		}
	}
	/**
	 * 咨询测试
	 * 每个用户向该用户对应的医生，发送100条消息
	 * @param
	 */
	public void consultTestStart(){
		for(int i=0;i<100;i++){
			consultTest(500,"向你发送第"+i+"条消息");
		}
	}

	void testPost(String urlStr,String xmlInfo) {
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "text/xml");

			OutputStreamWriter out = new OutputStreamWriter(con
					.getOutputStream());

			out.write(new String(xmlInfo.getBytes("ISO-8859-1")));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(con
					.getInputStream()));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				System.out.println(line);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
