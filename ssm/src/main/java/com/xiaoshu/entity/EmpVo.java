package com.xiaoshu.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;


public class EmpVo extends Tbemp {

	private String dname;
	private Integer age1;
	private Integer age2;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tbEmpBirthday1;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date tbEmpBirthday2;
	
	public Date getTbEmpBirthday1() {
		return tbEmpBirthday1;
	}

	public void setTbEmpBirthday1(Date tbEmpBirthday1) {
		this.tbEmpBirthday1 = tbEmpBirthday1;
	}

	public Date getTbEmpBirthday2() {
		return tbEmpBirthday2;
	}

	public void setTbEmpBirthday2(Date tbEmpBirthday2) {
		this.tbEmpBirthday2 = tbEmpBirthday2;
	}

	public Integer getAge1() {
		return age1;
	}

	public void setAge1(Integer age1) {
		this.age1 = age1;
	}

	public Integer getAge2() {
		return age2;
	}

	public void setAge2(Integer age2) {
		this.age2 = age2;
	}


	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

}
