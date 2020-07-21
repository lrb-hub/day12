package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.DeptMapper;
import com.xiaoshu.entity.Dept;
import com.xiaoshu.entity.DeptVo;

@Service
public class DeptService {

	@Autowired
	DeptMapper deptMapper;

	public PageInfo<DeptVo> findPage(DeptVo dept,Integer pageSize,Integer pageNum, String order, String ordername) {
			PageHelper.startPage(pageNum, pageSize);
			
			ordername = StringUtil.isNotEmpty(ordername)?ordername:"userid";
			order = StringUtil.isNotEmpty(order)?order:"desc";
			
			dept.setOrderByClause(ordername+" "+order);
			
			List<DeptVo> list = deptMapper.findList(dept);
		
		return new PageInfo<>(list);
	}
	
	public Dept findByName(String name) {
		Dept dept = new Dept();
		dept.setName(name);
		Dept dept2 = deptMapper.selectOne(dept);
		
		
		return dept2;
	}
	public void addDept(Dept dept) {
		
		deptMapper.insert(dept);
		
	}
	public void updateDept(Dept dept) {
		
		deptMapper.updateByPrimaryKeySelective(dept);
		
	}

	public void deleteDept(int id) {
		deptMapper.deleteByPrimaryKey(id);
	}

	public List<Dept> findAll() {
		// TODO Auto-generated method stub
		return deptMapper.selectAll();
	}

	public Integer findIdByDname(String dname) {
		Dept arg0 = new Dept();
		arg0.setName(dname);
		// TODO Auto-generated method stub
		Dept dept = deptMapper.selectOne(arg0);
		if (dept==null) {
			deptMapper.addDept(arg0);
			dept=arg0;
			
		}
		
		return dept.getId(); 
	}

}
