package com.nfet.icare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import com.nfet.icare.pojo.Company;
import com.nfet.icare.pojo.Device;
import com.nfet.icare.pojo.DeviceInfo;
import com.nfet.icare.pojo.ErrorInfo;
import com.nfet.icare.pojo.Ticket;
import com.nfet.icare.pojo.User;
import com.nfet.icare.service.CompanyServiceImpl;
import com.nfet.icare.service.DeviceServiceImpl;
import com.nfet.icare.service.TicketServiceImpl;
import com.nfet.icare.service.UserServiceImpl;
import com.nfet.icare.util.CacheMgr;
import com.nfet.icare.util.GlobalMethods;
import net.sf.json.JSONObject;

import static org.assertj.core.api.Assertions.in;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 上传Excel解析
 * 
 * @author zhoupx
 *
 */
@Controller
@RequestMapping("/mgr")
public class Upload {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private XSSFWorkbook workbook;
	private List<String> filesName = null;
	private String path = "";
	private ErrorInfo errorInfo = null;
	Map<Integer, String> map = new HashMap<Integer, String>();
	private CacheMgr cacheMgr = CacheMgr.getInstance();
	List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();

	@Autowired
	private UserServiceImpl userServiceImpl;
	@Autowired
	private DeviceServiceImpl deviceServiceImpl;
	@Autowired
	private TicketServiceImpl ticketServiceImpl;
	@Autowired
	private CompanyServiceImpl companyServiceImpl;
	@Autowired
	private RestTemplate restTemplate;
	@Value("${xxtUrl}")
	private String xxtUrl;
	private String fileType;
	// 导入设备核对用户
	private String curUser = "";

	@RequestMapping("/upload")
	@ResponseBody
	public ErrorInfo uploadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "excel", required = false) List<MultipartFile> files,
			@RequestParam(value = "phone", required = false) String curPhone,
			@RequestParam(value = "fileType", required = true) String fileName) {

		logger.info("表格类型" + fileName);
		fileType = fileName;
		curUser = curPhone;
		errorInfo = new ErrorInfo();
		// 获取上传文件信息
		filesName = new ArrayList<String>();
		path = request.getServletContext().getRealPath("/");
		MultipartFile file = null;
		BufferedOutputStream stream = null;
		for (int i = 0; i < files.size(); ++i) {
			file = files.get(i);
			if (!file.isEmpty()) {
				try {
					System.out.println("存在文件");
					String filename = file.getOriginalFilename();
					System.out.println("原始名称" + filename);
					filename = filename.substring(filename.lastIndexOf("\\") + 1);
					System.out.println("处理后名称" + filename);
					filename = rename(filename);
					System.out.println("rename后名称" + filename);
					byte[] bytes = file.getBytes();
					stream = new BufferedOutputStream(new FileOutputStream(new File(path + filename)));
					stream.write(bytes);
					stream.close();

				} catch (Exception e) {
					stream = null;

				}
			}
		}

		System.out.println("上传文件名" + filesName.toString());
		readExcel(request, response);

		return errorInfo;
	}

	public void readExcel(HttpServletRequest request, HttpServletResponse response) {

		for (String filename : filesName) {
			File f = null;
			try {
				String str = path + filename;
				System.out.println("上传文件路径" + str);
				f = new File(str);
				// 文件大小判断
				if (f.length() > 2 * 1024 * 1024) {
					String errorType = "上传的文件大小超过2M，请重新上传";
					errorInfo.setErrorType(errorType);
					return;
				}
				FileInputStream excelFile = new FileInputStream(f);
				workbook = new XSSFWorkbook(excelFile);
				// 读入Excel文件的第一个表
				XSSFSheet sheet = workbook.getSheetAt(0);
				if ("customerInfo".equals(fileType)) {
					logger.info("处理上传的用户信息");
					ProcessUsers(sheet);
				} else if ("customer_device".equals(fileType))
					ProcessDevices(sheet);
				else {
					ProcessCompanys(sheet);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("请确认要解析的excel文件路径是否正确");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("文件io流出错");
			} catch (ParseException e) {
				e.printStackTrace();
				System.out.println("文件处理转换报错");
			} catch (Throwable t) {
				System.out.println("测试异常");
			} finally {
				System.out.println("开始删除文件");
				f.delete();
			}

		}

	}

	public String rename(String name) {

		Long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		Long random = (long) (Math.random() * now);
		String fileName = now + "" + random;

		if (name.indexOf(".") != -1) {
			fileName += name.substring(name.lastIndexOf("."));
		}
		filesName.add(fileName);
		System.out.println("上传文件名test" + filesName.toString());
		return fileName;
	}

	// 导入用户信息
	public void ProcessUsers(XSSFSheet sheet) {
		logger.info("Start processing user information ...");
		List<String> errorLines = new ArrayList<>();
		String errorType = "";
		List<User> userList = new ArrayList<User>();
		XSSFRow row = sheet.getRow(0);

		XSSFCell cellOne = row.getCell(0);
		XSSFCell cellTwo = row.getCell(1);
		XSSFCell cellThree = row.getCell(2);
		XSSFCell cellFour = row.getCell(3);
		XSSFCell cellFive = row.getCell(4);
		XSSFCell cellSix = row.getCell(5);
		XSSFCell cellSeven = row.getCell(6);

		String seq = getCellValue(cellOne);
		String name = getCellValue(cellTwo);
		String phone = getCellValue(cellThree);
		String companyName = getCellValue(cellFour);
		String industry = getCellValue(cellFive);
		String companyPhone = getCellValue(cellSix);
		String address = getCellValue(cellSeven);

		// 1.列数和列名一起校验
		if (!"序号".equals(seq) || !"会员姓名".equals(name) || !"会员电话".equals(phone) || !"公司全称".equals(companyName)
				|| !"所属行业".equals(industry) || !"公司电话".equals(companyPhone) || !"公司地址".equals(address)) {
			logger.info("表格列名不正确");
			errorType = "Excel格式不正确";
			errorInfo.setErrorType(errorType);
			return;
		}

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null || judgeEmptyRow(row, 7)) {
				logger.info("空行不处理");
				break;
			}

			cellOne = row.getCell(0);
			cellTwo = row.getCell(1);
			cellThree = row.getCell(2);
			cellFour = row.getCell(3);
			cellFive = row.getCell(4);
			cellSix = row.getCell(5);
			cellSeven = row.getCell(6);

			seq = getCellValue(cellOne);
			name = getCellValue(cellTwo);
			phone = getCellValue(cellThree);
			companyName = getCellValue(cellFour);
			industry = getCellValue(cellFive);
			companyPhone = getCellValue(cellSix);
			address = getCellValue(cellSeven);

			// 2.手机号格式校验
			Boolean validate = validatePhone(phone);
			if (!validate) {
				logger.info("手机号格式不正确");
				errorLines.add(seq);
				continue;
			}

			// 3. 姓名、手机号、公司名非空校验
			if ("".equals(phone) || "".equals(name) || "".equals(companyName)) {
				logger.info("用户缺少必填字段");
				errorLines.add(seq);
				continue;
			}

			// 4.查询手机号是否冲突
			String judge = cacheMgr.isExistPhone(phone);
			if (judge != null) {
				// 已经注册的用户提示错误还是不提示
				// errorLines.add(seq);
				continue;
			}

			// 5.姓名长度限制
			if (name.length() < 2 || name.length() > 6) {
				errorLines.add(seq);
				continue;
			}

			// 添加用户
			User user = new User();
			user.setName(name);
			user.setPhone(phone);

			Company company = new Company();
			company.setCompanyName(companyName);
			int companyId = importCompany(company);
			user.setCompanyId(companyId);
			userList.add(user);

		}

		importUsers(userList);
		errorInfo.setErrorType(errorType);
		errorInfo.setErrorLine(errorLines);

	}

	public void ProcessCompanys(XSSFSheet sheet) {
		logger.info("Start processing company information ...");
		String errorType = "";
		List<String> errorLines = new ArrayList<>();
		XSSFRow row = sheet.getRow(0);

		XSSFCell cellOne = row.getCell(0);
		XSSFCell cellTwo = row.getCell(1);
		XSSFCell cellThree = row.getCell(2);
		XSSFCell cellFour = row.getCell(3);
		XSSFCell cellFive = row.getCell(4);

		String seq = getCellValue(cellOne);
		String companyName = getCellValue(cellTwo);
		String industry = getCellValue(cellThree);
		String companyPhone = getCellValue(cellFour);
		String address = getCellValue(cellFive);

		// 1.列数和列名一起校验
		if (!"序号".equals(seq) || !"公司全称".equals(companyName) || !"所属行业".equals(industry)
				|| !"公司电话".equals(companyPhone) || !"公司地址".equals(address)) {
			errorType = "Excel格式不正确";
			errorInfo.setErrorType(errorType);
			errorInfo.setErrorType(errorType);
			return;
		}
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null || judgeEmptyRow(row, 5)) {
				logger.info("空行不处理");
				break;
			}

			cellOne = row.getCell(0);
			cellTwo = row.getCell(1);
			cellThree = row.getCell(2);
			cellFour = row.getCell(3);
			cellFive = row.getCell(4);

			seq = getCellValue(cellOne);
			companyName = getCellValue(cellTwo);
			industry = getCellValue(cellThree);
			companyPhone = getCellValue(cellFour);
			address = getCellValue(cellFive);

			if ("".equals(companyName)) {
				errorLines.add(seq);
				continue;
			}
			if (companyName.length() < 2 || companyName.length() > 50) {
				errorLines.add(seq);
				continue;
			}
			if (!"".equals(address)) {
				if (address.length() < 5 || address.length() > 60) {
					errorLines.add(seq);
					continue;
				}
			}
			if (!"".equals(industry)) {
				if (industry.length() < 2 || industry.length() > 10) {
					errorLines.add(seq);
					continue;
				}
			}

			Company company = new Company();
			company.setIndustry(industry);
			company.setIndustry(industry);
			company.setAddress(address);
			company.setPhone(companyPhone);
			company.setCompanyName(companyName);
			importCompany(company);

		}
		errorInfo.setErrorType(errorType);
		errorInfo.setErrorLine(errorLines);

	}

	public boolean judgeEmptyRow(XSSFRow row, int number) {
		boolean flag = true;
		for (int i = 1; i < number; i++) {
			XSSFCell cell = row.getCell(i);
			String value = getCellValue(cell);
			if (!"".equals(value))
				flag = false;
		}
		return flag;

	}

	public void ProcessDevices(XSSFSheet sheet) throws ParseException {
		logger.info("Start processing device information ...");
		List<String> errorLines = new ArrayList<>();
		List<String> deviceList = new ArrayList<>();
		String errorType = "";
		XSSFRow row = sheet.getRow(0);
		XSSFCell cellOne = row.getCell(0);
		XSSFCell cellTwo = row.getCell(1);
		XSSFCell cellThree = row.getCell(2);
		String seq = getCellValue(cellOne);
		String deviceNo = getCellValue(cellTwo);
		String phone = getCellValue(cellThree);

		logger.info("test header ==>" + seq + deviceNo + phone);
		// 1.列数和列名一起校验
		if (!"序号".equals(seq) || !"整机序列号".equals(deviceNo) || !"客户电话".equals(phone)) {
			errorType = "Excel格式不正确";
			errorInfo.setErrorType(errorType);
			return;
		}

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			row = sheet.getRow(i);
			if (row == null || judgeEmptyRow(row, 3)) {
				logger.info("遍历空行，结束");
				break;
			}
			cellOne = row.getCell(0);
			cellTwo = row.getCell(1);
			cellThree = row.getCell(2);
			seq = getCellValue(cellOne);
			deviceNo = getCellValue(cellTwo);
			phone = getCellValue(cellThree);
			// 2. 判断当前用户是否与表格一致,省去手机格式校验
			if (!curUser.equals(phone)) {
				errorLines.add(seq);
				continue;
			}
			// 3.添加校验非空校验
			if ("".equals(deviceNo) || "".equals(phone)) {
				errorLines.add(seq);
				continue;
			}
			// 4.设备是否存在及是否绑定
			boolean validate = cacheMgr.isExistDevice(deviceNo);
			if (validate) {
				errorLines.add(seq);
				continue;
			}
			// 根据手机号获取userNo
			String userNo = cacheMgr.isExistPhone(phone);
			logger.info("current user is " + userNo);
			// 添加绑定设备信息
			DeviceInfo deviceInfo = deviceServiceImpl.existDevice(deviceNo);
			// 未绑定
			logger.info("the device could be binded");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(deviceInfo.getMfgDate()));

			// 保修时间从出厂两个月后开始
			calendar.add(Calendar.MONTH, 2);
			Device device = new Device();
			device.setUserNo(userNo);
			device.setDeviceNo(deviceInfo);
			device.setBindTime(new Timestamp(System.currentTimeMillis()));
			device.setEcardNo("NFET" + sdf1.format(new Date()) + RandomStringUtils.randomNumeric(6));
			device.setWarrantPeriod(365 * 3);
			device.setWarrantFrom(sdf.format(calendar.getTime()));
			// 保修时间三年
			calendar.add(Calendar.YEAR, 3);
			device.setWarrantTo(sdf.format(calendar.getTime()));
			if (sdf.parse(sdf.format(new Date())).getTime() > calendar.getTime().getTime()) {// 保外
				device.setWarrantStatus(2);
			} else {// 保内
				device.setWarrantStatus(1);
			}
			device.setCreateTime(new Timestamp(System.currentTimeMillis()));
			device.setLastModifyTime(new Timestamp(System.currentTimeMillis()));

			// 赠送延保券
			Ticket ticket = new Ticket();
			ticket.setDeviceNo(deviceNo);
			ticket.setPeriod("3");
			ticket.setTicketNo("NFET" + RandomStringUtils.randomNumeric(8));
			ticket.setUserNo(userNo);
			ticket.setPhone(phone);
			ticket.setContent("延保有效期是指在延保券使用后，给所使用的设备在其延保期后延长X个月的保修时间；若已过保，则保修期即从使用当天开始算起，延长X个月保修时间。");
			ticket.setType(1);
			ticket.setCreateTime(new Timestamp(System.currentTimeMillis()));
			ticket.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			if (ticketServiceImpl.queryTicket(deviceNo) == null) {
				ticketServiceImpl.insertTicket(ticket);
			}

			// 未注册方给注册
			if (deviceServiceImpl.bindOrNot(deviceNo) == null) {
				deviceServiceImpl.bindSingleDevice(device);
			}

			deviceList.add(deviceNo);
			logger.info("insert single data into data_device");

		}
		// 拼接device字符串
		String deviceToString = StringUtils.join(deviceList.toArray(), ",");
		logger.info("打印device拼接字符串：" + deviceToString);

		// TODO 信讯通接口(注册设备)
		Map<String, Object> deviceMap = new HashMap<>();
		deviceMap.put("user_no", cacheMgr.isExistPhone(curUser));
		deviceMap.put("device_no", deviceToString);
		String json = GlobalMethods.parseString(deviceMap);
		String result = restTemplate.getForObject(xxtUrl +
				"queryWarranty/{json}",
				String.class, json);
		logger.info("XXT:" + result);

		errorInfo.setErrorType(errorType);
		errorInfo.setErrorLine(errorLines);
	}

	// 校验手机号格式
	public Boolean validatePhone(String phone) {
		String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(phone);
		return m.find();
	}

	/**
	 * 对Excel的各个单元格的格式进行判断并转换
	 */
	public String getCellValue(XSSFCell cell) {
		String cellValue = "";
		DecimalFormat df = new DecimalFormat("#");
		if (null == cell) {
			logger.info("单元格为null");
			return "";
		}
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_STRING:
			cellValue = cell.getRichStringCellValue().getString().trim();
			break;
		case XSSFCell.CELL_TYPE_NUMERIC:
			cellValue = df.format(cell.getNumericCellValue()).toString();
			break;
		case XSSFCell.CELL_TYPE_BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
			break;
		case XSSFCell.CELL_TYPE_FORMULA:
			cellValue = cell.getCellFormula();
			break;
		default:
			cellValue = "";
		}
		return cellValue;
	}

	// 导入公司信息
	public int importCompany(Company company) {
		String companyName = company.getCompanyName();
		int companyId = 0;
		companyId = cacheMgr.isExistCompany(company.getCompanyName());
		// 输入公司已存在
		if (companyId != 0) {
			logger.info("companyName:" + companyName + " already exists ");
			logger.info("companyId:" + companyId);
			return companyId;
		} else {// 公司不存在
			logger.info("companyName:" + companyName + " not exists ");
			if (companyServiceImpl.getMaxCompanyId() == null) {
				company.setCompanyId(1);
			} else {
				company.setCompanyId(companyServiceImpl.getMaxCompanyId() + 1);
			}
			company.setCompanyName(companyName);
			company.setCreateTime(new Timestamp(System.currentTimeMillis()));
			company.setLastModifyTime(new Timestamp(System.currentTimeMillis()));

			companyServiceImpl.importCompany(company);

			companyId = company.getCompanyId();
			// TODO 添加公司缓存信息,companyId可以通过缓存max++
			cacheMgr.putCompany(companyName, String.valueOf(companyId));
			logger.info("insert data into data_company");
			return company.getCompanyId();
		}

	}

	// excel导入用户信息
	public void importUsers(List<User> userList) {
		Map<String, Object> userMap = null;

		for (User user : userList) {
			String companyName = "";
			String userNo = "";
			String userName = user.getName();
			String phone = user.getPhone();
			int companyId = user.getCompanyId();
			logger.info("companyId ==>" + companyId);
			userMap = new HashMap<>();
			userMap.put("user_name", userName);
			userMap.put("phone", phone);
			companyName = companyServiceImpl.getCompanyName(companyId);
			userMap.put("company", companyName);
			// TODO userNo = getUserNo(userMap);
			userNo = "wxvip201708180009";
			User temp = new User();
			temp.setUserNo(userNo);
			temp.setName(userName);
			temp.setPhone(phone);
			temp.setCompanyId(companyId);
			temp.setCreateTime(new Timestamp(System.currentTimeMillis()));
			temp.setLastModifyTime(new Timestamp(System.currentTimeMillis()));
			userServiceImpl.initReg(temp);
			cacheMgr.putUser(phone, userNo);
			// 打印下缓存
			logger.info(cacheMgr.getPhoneCache().toString());
		}

	}

	// 调用信讯通接口获取userNO
	public String getUserNo(Map<String, Object> userMap) {
		logger.info(" 调用信讯通接口获取userNO");
		String json = GlobalMethods.parseString(userMap);

		logger.info("change userMap to json" + json);
		// TODO 暂时没法调用信讯通
		// String result = restTemplate.getForObject(xxtUrl + "register/{json}",
		// String.class, json);
		// JSONObject jsonObject = JSONObject.fromObject(result);
		// logger.info("3s return result:" + jsonObject);
		// String userNo = jsonObject.getString("user_no");

		String userNo = "wxvip20170818000";
		logger.info("得到的userNo：" + userNo);
		return userNo;

	}

	@RequestMapping("/start")
	public String start() {
		System.out.println(1);
		return "uploadpage";
		// return "downloadpage";
	}
}
