package com.xiaoshu.dao;

import java.util.List;

import com.xiaoshu.base.dao.BaseMapper;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;

public interface UserMapper extends BaseMapper<User> {

	List<User> selectUserAndRoleByExample(UserExample example);

}