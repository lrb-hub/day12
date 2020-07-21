package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.EmpVo;
import com.xiaoshu.entity.Tbemp;

public interface TbempMapper extends BaseMapper<Tbemp> {
	
	List<EmpVo> findPage(EmpVo empVo);
	
}