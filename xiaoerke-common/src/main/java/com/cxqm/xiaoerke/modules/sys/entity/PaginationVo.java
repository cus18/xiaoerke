package com.cxqm.xiaoerke.modules.sys.entity;

import java.util.List;


public class PaginationVo<T> {


	private int pageSize = 20;

	private int pageNo;

	private int upPage;

	private int nextPage;

	private long totalCount;

	private int totalPage;

	private List<T> datas;


	private String pageUrl;


	public int getFirstResult() {
		return (this.getPageNo() - 1) * this.getPageSize();
	}


	public int getLastResult() {
		return this.getPageNo() * this.getPageSize();
	}


	public void setTotalPage() {
		this.totalPage = (int) ((this.totalCount % this.pageSize > 0) ? (this.totalCount / this.pageSize + 1)
				: this.totalCount / this.pageSize);
	}


	public void setUpPage() {
		this.upPage = (this.pageNo > 1) ? this.pageNo - 1 : this.pageNo;
	}


	public void setNextPage() {
		this.nextPage = (this.pageNo == this.totalPage) ? this.pageNo : this.pageNo + 1;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public int getUpPage() {
		return upPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount2) {
		this.totalCount = totalCount2;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public PaginationVo(int pageNo, int pageSize, long totalCount2) {
		this.setPageNo(pageNo);
		this.setPageSize(pageSize);
		this.setTotalCount(totalCount2);
		this.init();
	}


	private void init() {
		this.setTotalPage();
		this.setUpPage();
		this.setNextPage();
	}
}
