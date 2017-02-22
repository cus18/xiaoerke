package com.cxqm.xiaoerke.modules.sys.entity;

public class ReceiveXmlEntity {

	/** 消息id，64位整型  */
	private String MsgID;

	private String ToKfAccount;
	/** 开发者微信号  */
	private String ToUserName="";
	/** 发送方帐号OpenID */
	private String FromUserName="";
	/** 消息创建时间  */
	private String CreateTime="";
	private String MsgType="";
	private String MsgId="";
	private String Event="";
	private String EventKey="";
	private String Ticket="";
	private String Latitude="";
	private String Longitude="";
	private String Precision="";
	private String PicUrl="";
	private String MediaId="";
	private String Title="";
	private String Description="";
	private String Url="";
	private String Location_X="";
	private String Location_Y="";
	private String Scale="";
	private String Label="";
	private String Content="";
	private String Format="";
	private String Recognition="";
	private String KfAccount = "";
	private String FromKfAccount = "";
	private String MenuId = "";
	private String Status = "";
	private String ShopId = "";


	public String getRecognition() {
		return Recognition;
	}
	public void setRecognition(String recognition) {
		Recognition = recognition;
	}
	public String getFormat() {
		return Format;
	}
	public void setFormat(String format) {
		Format = format;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getLocation_X() {
		return Location_X;
	}
	public void setLocation_X(String locationX) {
		Location_X = locationX;
	}
	public String getLocation_Y() {
		return Location_Y;
	}
	public void setLocation_Y(String locationY) {
		Location_Y = locationY;
	}
	public String getScale() {
		return Scale;
	}
	public void setScale(String scale) {
		Scale = scale;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public String getMediaId() {
		return MediaId;
	}
	public void setMediaId(String mediaId) {
		MediaId = mediaId;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
	public String getLatitude() {
		return Latitude;
	}
	public void setLatitude(String latitude) {
		Latitude = latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public void setLongitude(String longitude) {
		Longitude = longitude;
	}
	public String getPrecision() {
		return Precision;
	}
	public void setPrecision(String precision) {
		Precision = precision;
	}
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getMsgId() {
		return MsgId;
	}
	public void setMsgId(String msgId) {
		MsgId = msgId;
	}
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getKfAccount() {
		return KfAccount;
	}

	public void setKfAccount(String kfAccount) {
		KfAccount = kfAccount;
	}

	public String getFromKfAccount() {
		return FromKfAccount;
	}

	public void setFromKfAccount(String fromKfAccount) {
		FromKfAccount = fromKfAccount;
	}

	public String getMsgID() {
		return MsgID;
	}

	public void setMsgID(String msgID) {
		MsgID = msgID;
	}

	public String getToKfAccount() {
		return ToKfAccount;
	}

	public void setToKfAccount(String toKfAccount) {
		ToKfAccount = toKfAccount;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMenuId() {
		return MenuId;
	}

	public void setMenuId(String menuId) {
		MenuId = menuId;
	}

	public String getShopId() {
		return ShopId;
	}

	public void setShopId(String shopId) {
		ShopId = shopId;
	}
}
