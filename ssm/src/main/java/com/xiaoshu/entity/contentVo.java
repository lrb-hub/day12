package com.xiaoshu.entity;

import java.util.Date;

public class contentVo extends Content {
	private String categoryname;
	 private String status1;
	 private Date createtime1;
	  
	public String getCategoryname() {
		return categoryname;
	}


	public void setCategoryname(String categoryname) {
		this.categoryname = categoryname;
	}


	public String getStatus1() {
		return status1;
	}


	public void setStatus1(String status1) {
		this.status1 = status1;
	}


	public Date getCreatetime1() {
		return createtime1;
	}


	public void setCreatetime1(Date createtime1) {
		this.createtime1 = createtime1;
	}
	
}
