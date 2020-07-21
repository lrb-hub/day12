package com.xiaoshu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.xiaoshu.dao.CourseMapper;
import com.xiaoshu.dao.StudentMapper;
import com.xiaoshu.entity.Course;
import com.xiaoshu.entity.Student;
import com.xiaoshu.entity.StudentVo;
import com.xiaoshu.entity.User;
import com.xiaoshu.entity.UserExample;
import com.xiaoshu.entity.UserExample.Criteria;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Service
public class StudentService {

	@Autowired
	StudentMapper StudentMapper;

	/*// 查询所有
	public List<User> findUser(User t) throws Exception {
		return userMapper.select(t);
	};

	// 数量
	public int countUser(User t) throws Exception {
		return userMapper.selectCount(t);
	};

	// 通过ID查询
	public User findOneUser(Integer id) throws Exception {
		return userMapper.selectByPrimaryKey(id);
	};

	// 新增
	public void addUser(User t) throws Exception {
		userMapper.insert(t);
	};

	// 修改
	public void updateUser(User t) throws Exception {
		userMapper.updateByPrimaryKeySelective(t);
	};

	// 删除
	public void deleteUser(Integer id) throws Exception {
		userMapper.deleteByPrimaryKey(id);
	};

	// 登录
	public User loginUser(User user) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andPasswordEqualTo(user.getPassword()).andUsernameEqualTo(user.getUsername());
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过用户名判断是否存在，（新增时不能重名）
	public User existUserWithUserName(String userName) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(userName);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	};

	// 通过角色判断是否存在
	public User existUserWithRoleId(Integer roleId) throws Exception {
		UserExample example = new UserExample();
		Criteria criteria = example.createCriteria();
		criteria.andRoleidEqualTo(roleId);
		List<User> userList = userMapper.selectByExample(example);
		return userList.isEmpty()?null:userList.get(0);
	}*/

	public PageInfo<StudentVo> findUserPage(StudentVo studentVo, int pageNum, int pageSize, String ordername, String order) {
		PageHelper.startPage(pageNum, pageSize);
		ordername = StringUtil.isNotEmpty(ordername)?ordername:"userid";
		order = StringUtil.isNotEmpty(order)?order:"desc";
		UserExample example = new UserExample();
		example.setOrderByClause(ordername+" "+order);
		Criteria criteria = example.createCriteria();
		
		List<StudentVo> userList = StudentMapper.findPageAll(studentVo);
		
		PageInfo<StudentVo> pageInfo = new PageInfo<StudentVo>(userList);
		return pageInfo;
	}
	@Autowired
	CourseMapper courseMapper;
	
	@Autowired
	JedisPool jedisPool;
	
	public Course findByCode(String co) {
		// TODO Auto-generated method stub
		
		
		Course course = new Course();
		course.setCode(co);
		return courseMapper.selectOne(course);
	}

	public void addcourse(Course course) {
		// TODO Auto-generated method stub
		
		courseMapper.insert(course);
		Jedis jedis = jedisPool.getResource();
		
	Course 	cc =  courseMapper.selectOne(course);
		
	jedis.hset("人员信息",cc.getId().toString(),cc.getName());
	
		
	}

	public List<Course> findAll() {
		// TODO Auto-generated method stub
		return courseMapper.selectAll();
	}

	public Student findByName(String name) {
		Student arg0 = new Student();
		arg0.setName(name);
		// TODO Auto-generated method stub
		return StudentMapper.selectOne(arg0);
	}

	public void updateUser(Student student) {
		// TODO Auto-generated method stub
		StudentMapper.updateByPrimaryKeySelective(student);
	}

	public void addUser(Student student) {
		// TODO Auto-generated method stub
		StudentMapper.insert(student);
	}

	public List<Course> findCourse() {
		// TODO Auto-generated method stub
		return courseMapper.selectAll();
	}


}
