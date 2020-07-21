package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.TbempMapper;
import com.xiaoshu.dao.UserMapper;
import com.xiaoshu.entity.Dept;
import com.xiaoshu.entity.EmpVo;
import com.xiaoshu.entity.Tbemp;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

@Service
public class EmpService {

	@Autowired
	TbempMapper mapper;

	public PageInfo<EmpVo> findPage(EmpVo empVo, Integer pageNum, Integer pageSize) {
		
		
		PageHelper.startPage(pageNum, pageSize);
		List<EmpVo> list = mapper.findPage(empVo);
		
		return new PageInfo<>(list);
	}

	public Tbemp findByName(String name) {
		Tbemp emp = new Tbemp();
		emp.setTbEmpName(name);
				Tbemp one = mapper.selectOne(emp);
				return one;
	}


	public void addEmp(Tbemp emp) {
		mapper.insert(emp);
	}

	public void updateEmp(Tbemp emp) {
		mapper.updateByPrimaryKeySelective(emp);
	}

	public void delById(int id) {
			
		mapper.deleteByPrimaryKey(id);
		
	}

	public List<EmpVo> findAllList(EmpVo empVo) {
		// TODO Auto-generated method stub
		return mapper.findPage(empVo);
	}

	

}
