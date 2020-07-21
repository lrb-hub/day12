package com.xiaoshu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.xiaoshu.config.util.ConfigUtil;
import com.xiaoshu.entity.EmpVo;
import com.xiaoshu.entity.Log;
import com.xiaoshu.entity.Operation;
import com.xiaoshu.entity.Role;
import com.xiaoshu.entity.Tbemp;
import com.xiaoshu.entity.User;
import com.xiaoshu.service.DeptService;
import com.xiaoshu.service.EmpService;
import com.xiaoshu.service.OperationService;
import com.xiaoshu.service.RoleService;
import com.xiaoshu.service.UserService;
import com.xiaoshu.util.StringUtil;
import com.xiaoshu.util.TimeUtil;
import com.xiaoshu.util.WriterUtil;

@Controller
@RequestMapping("emp")
public class EmpController extends LogController {
	static Logger logger = Logger.getLogger(EmpController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private EmpService empService;
	@Autowired
	private DeptService deptService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private OperationService operationService;

	@RequestMapping("empindex")
	public String index(HttpServletRequest request, Integer menuid) throws Exception {
		List<Role> roleList = roleService.findRole(new Role());
		List<Operation> operationList = operationService.findOperationIdsByMenuid(menuid);

		request.setAttribute("operationList", operationList);
		request.setAttribute("deptList", deptService.findAll());
		return "emp";
	}

	@RequestMapping(value = "empList", method = RequestMethod.POST)
	public void empList(EmpVo empVo, HttpServletRequest request, HttpServletResponse response, String offset,
			String limit) throws Exception {
		try {
			String order = request.getParameter("order");
			String ordername = request.getParameter("ordername");

			Integer pageSize = StringUtil.isEmpty(limit) ? ConfigUtil.getPageSize() : Integer.parseInt(limit);
			Integer pageNum = (Integer.parseInt(offset) / pageSize) + 1;

			PageInfo<EmpVo> page = empService.findPage(empVo, pageNum, pageSize);

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("total", page.getTotal());
			jsonObj.put("rows", page.getList());
			WriterUtil.write(response, jsonObj.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("用户展示错误", e);
			throw e;
		}
	}

	// 新增或修改
	@RequestMapping("reserveUser")
	public void reserveUser(MultipartFile empFile,HttpServletRequest request,Tbemp emp, HttpServletResponse response) throws IllegalStateException, IOException {
		
		if (empFile.getSize()>0&& empFile!=null) {
			//获取图片名
			String filename = empFile.getOriginalFilename();
			//获取后缀名
			String end = filename.substring(filename.lastIndexOf("."));
			//重新命名
			String tbEmpImg = UUID.randomUUID()+end;
			//xunilujing
			File file = new File("d:/img/"+tbEmpImg);
			
			empFile.transferTo(file);
			emp.setTbEmpImg(tbEmpImg);
			
		}
		
		/*//文件上传
		String basepath = request.getServletContext().getRealPath("/");
		
		String path = basepath+"/img";
		File file = new File(path);
		
		if (!file.exists()) {
			file.mkdir();
		}
		String oldname = empFile.getOriginalFilename(); 
		
		
		String img = UUID.randomUUID().toString()+oldname.substring(oldname.lastIndexOf("."));
		
		empFile.transferTo(new File(path+"/"+img));
		
		emp.setTbEmpImg("../img/"+img);
		*/
		
		Integer tbEmpId = emp.getTbEmpId();
		JSONObject result = new JSONObject();
		try {
			Tbemp emp2 = empService.findByName(emp.getTbEmpName());

			if (tbEmpId != null) { // userId不为空 说明是修改
				if ( emp2 == null || (emp2 != null && emp2.getTbEmpId().equals(tbEmpId))) {

					empService.updateEmp(emp);

					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该人员名被使用");
				}

			} else { // 添加
				if (emp2 == null) { // 没有重复可以添加
					empService.addEmp(emp);
					result.put("success", true);
				} else {
					result.put("success", true);
					result.put("errorMsg", "该用户名被使用");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("保存人员信息错误", e);
			result.put("success", true);
			result.put("errorMsg", "对不起，操作失败");
		}
		WriterUtil.write(response, result.toString());
	}

	@RequestMapping("deleteUser")
	public void delUser(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			String[] ids = request.getParameter("ids").split(",");
			for (String id : ids) {
				empService.delById(Integer.parseInt(id));
			}
			result.put("success", true);
			result.put("delNums", ids.length);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除用户信息错误", e);
			result.put("errorMsg", "对不起，删除失败");
		}
		WriterUtil.write(response, result.toString());
	}
	//导入
	@RequestMapping("importEmp")
	public void importEmp(MultipartFile importFile,HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			
			//获取文件
			Workbook wb = WorkbookFactory.create(importFile.getInputStream());
			//获取文件中的sheet页
			Sheet sheet = wb.getSheetAt(0);
			//获取最后一行行数
			int lastRow = sheet.getLastRowNum();
			//循环行数 获取每一行对象
			for (int i = 1; i <= lastRow; i++) {
				Row row = sheet.getRow(i);
				//获取每一行的单元格
				String name = row.getCell(1).getStringCellValue();
				String sex = row.getCell(2).getStringCellValue();
				Double age1 = row.getCell(3).getNumericCellValue();
				int age = age1.intValue();
				String addr = row.getCell(4).getStringCellValue();
				String img = row.getCell(5).getStringCellValue();
				Date time = row.getCell(6).getDateCellValue();
				String dname = row.getCell(7).getStringCellValue();
				
				//查询did
				Integer did = deptService.findIdByDname(dname);
				
				//封装 emp对象 
				Tbemp tbemp = new Tbemp();
				tbemp.setTbEmpName(name);
				tbemp.setTbEmpSex(sex.equals("男")?"1":"0");
				tbemp.setTbEmpAge(age);
				tbemp.setTbEmpAddress(addr);
				tbemp.setTbEmpImg(img);
				tbemp.setTbEmpBirthday(time);
				tbemp.setTbEmpDid(did);
				//调用service保存方法保存数据
				empService.addEmp(tbemp);
				
				
			}
			
			
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导入错误", e);
			result.put("errorMsg", "对不起，导入失败");
		}
		WriterUtil.write(response, result.toString());
	}
	
	@RequestMapping("exportEmp2")
	public void exportEmp2(EmpVo empvo,HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			String time = TimeUtil.formatTime(new Date(), "yyyyMMddHHmmss");
		    String excelName = "人员信息"+time;
		    
		    
			Log log = new Log();
			List<EmpVo> list = empService.findAllList(empvo);
			
			String[] handers = {"编号","人员名","性别","年龄","住址","头像","生日","部门"};
			// 1导入硬盘
			ExportExcelToDisk(request,handers,list, excelName);
			
			
			
			result.put("success", true);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出信息错误", e);
			result.put("errorMsg", "对不起，导出失败");
		}
		WriterUtil.write(response, result.toString());
	}
	// 导出到硬盘
		@SuppressWarnings("resource")
		private void ExportExcelToDisk(HttpServletRequest request,
				String[] handers, List<EmpVo> list, String excleName) throws Exception {
			
			try {
				HSSFWorkbook wb = new HSSFWorkbook();//创建工作簿
				HSSFSheet sheet = wb.createSheet("操作记录备份");//第一个sheet
				HSSFRow rowFirst = sheet.createRow(0);//第一个sheet第一行为标题
				rowFirst.setHeight((short) 500);
				for (int i = 0; i < handers.length; i++) {
					sheet.setColumnWidth((short) i, (short) 4000);// 设置列宽
				}
				//写标题了
				for (int i = 0; i < handers.length; i++) {
				    //获取第一行的每一个单元格
				    HSSFCell cell = rowFirst.createCell(i);
				    //往单元格里面写入值
				    cell.setCellValue(handers[i]);
				}
				for (int i = 0;i < list.size(); i++) {
				    //获取list里面存在是数据集对象
					
				    EmpVo vo = list.get(i);
				    //创建数据行
				    HSSFRow row = sheet.createRow(i+1);
				    //设置对应单元格的值
				    row.setHeight((short)400);   // 设置每行的高度
				    //"序号","操作人","IP地址","操作时间","操作模块","操作类型","详情"
				    row.createCell(0).setCellValue(vo.getTbEmpId());
				    row.createCell(1).setCellValue(vo.getTbEmpName());
				    row.createCell(2).setCellValue(vo.getTbEmpSex().equals("1")?'男':'女');
				    row.createCell(3).setCellValue(vo.getTbEmpAge());
				    row.createCell(4).setCellValue("dizhi");
				    row.createCell(5).setCellValue(vo.getTbEmpImg());
				    row.createCell(6).setCellValue(TimeUtil.formatTime(vo.getTbEmpBirthday(), "yyyy-MM-dd"));
				    row.createCell(7).setCellValue(vo.getDname());
				}
				//写出文件（path为文件路径含文件名）
					OutputStream os;
					File file = new File("D:/"+File.separator+excleName+".xls");
					
					if (!file.exists()){//若此目录不存在，则创建之  
						file.createNewFile();  
						logger.debug("创建文件夹路径为："+ file.getPath());  
		            } 
					os = new FileOutputStream(file);
					wb.write(os);
					os.close();
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
		}
	
	@RequestMapping("exportEmp")
	public void exportEmp(EmpVo empVo,HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			
			//导出
			//查询导出的数据
			
			List<EmpVo> list = empService.findAllList(empVo);
			
			//查到的数据 导出
			Workbook wb = new HSSFWorkbook();
			
			//创建一个工作对象sheet
			Sheet sheet = wb.createSheet();
			//第一行
			Row firstrow = sheet.createRow(0);
			
			//创建第一行单元格  并赋值
 			String[] headers = {"编号","人员名","性别","年龄","住址","头像","生日","部门"};
			for (int i = 0; i < headers.length; i++) {
				Cell cell = firstrow.createCell(i);
				cell.setCellValue(headers[i]);
			}
			
			
			/*firstrow.createCell(0).setCellValue();
			firstrow.createCell(1)
			firstrow.createCell(2)
			firstrow.createCell(3)
			firstrow.createCell(4)
			firstrow.createCell(5)
			firstrow.createCell(6)*/
			
			
			for (int i = 0; i < list.size(); i++) {
				
				//创建行对象                                                 第二行开始
				Row row = sheet.createRow(i+1);
				//创建单元格对象
				/*for (int j = 0; j <list.size(); j++) {
					row.createCell(i).setCellValue(list.get(j).getTbEmpId());
				}*/
				row.createCell(0).setCellValue(list.get(i).getTbEmpId());
				row.createCell(1).setCellValue(list.get(i).getTbEmpName());
				row.createCell(2).setCellValue(list.get(i).getTbEmpSex().equals("1")?"男":"女");
				row.createCell(3).setCellValue(list.get(i).getTbEmpAge());
				row.createCell(4).setCellValue("地址");
				row.createCell(5).setCellValue(list.get(i).getTbEmpImg());
				row.createCell(6).setCellValue(TimeUtil.formatTime(list.get(i).getTbEmpBirthday(), "yyyy-MM-dd"));
				row.createCell(7).setCellValue(list.get(i).getDname());
				
				
			}
			
			//把工作溥 写到内从
			//写出文件（path为文件路径含文件名）
			OutputStream os;
			File file = new File("C:\\Users\\mi\\Desktop\\人员信息.xls");
			
			if (!file.exists()){//若此目录不存在，则创建之  
				file.createNewFile();  
				logger.debug("创建文件夹路径为："+ file.getPath());  
            } 
			os = new FileOutputStream(file);
			wb.write(os);
			os.close();
			
			
			
			
			
			
			
			
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出用户信息错误", e);
			result.put("errorMsg", "对不起，导出失败");
		}
		WriterUtil.write(response, result.toString());
	}

	@RequestMapping("editPassword")
	public void editPassword(HttpServletRequest request, HttpServletResponse response) {
		JSONObject result = new JSONObject();
		String oldpassword = request.getParameter("oldpassword");
		String newpassword = request.getParameter("newpassword");
		HttpSession session = request.getSession();
		User currentUser = (User) session.getAttribute("currentUser");
		if (currentUser.getPassword().equals(oldpassword)) {
			User user = new User();
			user.setUserid(currentUser.getUserid());
			user.setPassword(newpassword);
			try {
				userService.updateUser(user);
				currentUser.setPassword(newpassword);
				session.removeAttribute("currentUser");
				session.setAttribute("currentUser", currentUser);
				result.put("success", true);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("修改密码错误", e);
				result.put("errorMsg", "对不起，修改密码失败");
			}
		} else {
			logger.error(currentUser.getUsername() + "修改密码时原密码输入错误！");
			result.put("errorMsg", "对不起，原密码输入错误！");
		}
		WriterUtil.write(response, result.toString());
	}
}
