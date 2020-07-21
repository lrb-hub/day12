package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.Dept;
import com.xiaoshu.entity.DeptVo;

public interface DeptMapper extends BaseMapper<Dept> {
	
	public List<DeptVo> findList(Dept dept);

	public void addDept(Dept arg0);
	
}