package com.cxqm.xiaoerke.modules.consult.entity;

import java.io.Serializable;
import java.util.Date;

public class ConsultSession  implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String STATUS_ONGOING = "ongoing";

	public final transient static String STATUS_COMPLETED = "completed";
	
	public final transient static String STATUS_INVALID = "invalid";
	
    private Integer id;

    private transient String title;

    private transient Date createTime;

    private String status;

    private transient Date updateTime;

    private String userId;
    
    private String csUserId;

    private transient String source;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCsUserId() {
		return csUserId;
	}

	public void setCsUserId(String csUserId) {
		this.csUserId = csUserId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}