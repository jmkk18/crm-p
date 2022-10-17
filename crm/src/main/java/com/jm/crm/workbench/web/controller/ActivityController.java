package com.jm.crm.workbench.web.controller;

import com.jm.crm.commons.constants.Constants;
import com.jm.crm.commons.pojo.ReturnObject;
import com.jm.crm.commons.utils.DateUtils;
import com.jm.crm.commons.utils.HSSFUtils;
import com.jm.crm.commons.utils.UUIDUtils;
import com.jm.crm.settings.pojo.User;
import com.jm.crm.settings.service.UserService;
import com.jm.crm.workbench.pojo.Activity;
import com.jm.crm.workbench.pojo.ActivityRemark;
import com.jm.crm.workbench.service.ActivityRemarkService;
import com.jm.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用service层对象，查询所有的用户
        List<User> userList = userService.queryAllUsers();
        //将数据存到request中
        request.setAttribute("userList",userList);
        //请求转发到市场活动的主页面
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/saveCreateActivity")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        //封装参数(前端已经封装6个参数)
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateUtils.formatDateTime(new Date()));
        activity.setCreateBy(user.getId());

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层对象，保存创建的市场活动
            int count = activityService.saveCreateActivity(activity);

            if(count>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else{
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }

        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,
                                                  int pageNo,int pageSize){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service层方法，查询数据
        List<Activity> activityList = activityService.selectActivityByConditionForPage(map);
        int totalRows = activityService.queryCountOfActivityByCondition(map);
        //根据查询结果，生成响应信息
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/activity/deleteActivityByIds.do")
    @ResponseBody
    public Object deleteActivityByIds(String[] id){
        System.out.println(id);
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，删除市场活动
            int count = activityService.deleteActivityByIds(id);
            if(count > 0) {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        //调用service层方法，查询市场活动
        Activity activity = activityService.queryActivityById(id);
        //根据查询结果，相应信息
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        User user = (User) session.getAttribute(Constants.SESSION_USER);
        //封装参数
        activity.setEditBy(user.getId());
        activity.setEditTime(DateUtils.formatDateTime(new Date()));

        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法，保存修改的市场活动
            int count = activityService.saveEditActivity(activity);
            if(count>0) returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            else{
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/exportAllActivities.do")
    public void exportAllActivities(HttpServletResponse response) throws IOException {
        //调用service层方法，查询所有市场活动
        List<Activity> activityList = activityService.queryAllActivities();
        //创建excel文件，并且把activityList写入到excel文件中
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);//第一行
        HSSFCell cell = row.createCell(0);//第一列
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        //遍历activityList，创建HSSFRow对象，生成所有数据行
        if(activityList!=null&&activityList.size()>0){
            Activity activity=null;
            for(int i=0;i<activityList.size();i++){
                activity=activityList.get(i);
                //每遍历出一个activity，生成一行
                row=sheet.createRow(i+1);
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        //把生成的excel文件下载到客户端
        //设置响应的内容类型和编码方式
        response.setContentType("application/octet-stream;charset=UTF-8");
        //实现文件下载的时候，避免浏览器自动打开文件
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        //从response中获取输出流
        ServletOutputStream outputStream = response.getOutputStream();
        //将内存中文件写入到客户端
        wb.write(outputStream);
        //关闭刷新流
        wb.close();
        outputStream.flush();
    }

    @RequestMapping("/workbench/activity/exportActivityXzBtn.do")
    public void exportActivityXzBtn(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //获取参数
        String[] ids = request.getParameterValues("id");
        //调用service层方法，查询符合条件的市场活动
        List<Activity> activityList = activityService.queryActivityByIds(ids);
        //创建excel文件，将activityList内容写到excel中
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        HSSFRow row = sheet.createRow(0);//第一行
        HSSFCell cell = row.createCell(0);//第一列
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        //遍历activityList，创建HSSFRow对象，生成所有数据行
        if(activityList!=null&&activityList.size()>0){
            Activity activity=null;
            for(int i=0;i<activityList.size();i++){
                activity=activityList.get(i);
                //每遍历出一个activity，生成一行
                row=sheet.createRow(i+1);
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }
        //把生成的excel文件下载到客户端
        //设置响应内容和编码
        response.setContentType("application/octet-stream;charset=UTF-8");
        //实现下载的时候，防止浏览器打开文件
        response.addHeader("Content-Disposition","attachment;filename=activityList"+UUIDUtils.getUUID()+".xls");
        ServletOutputStream outputStream = response.getOutputStream();
        //写入客户端
        wb.write(outputStream);
        //关闭刷新流
        wb.close();
        outputStream.flush();
    }

    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile,HttpSession session){
        User user= (User) session.getAttribute(Constants.SESSION_USER);
        ReturnObject returnObject=new ReturnObject();
        try {
            //获取输入流
            InputStream inputStream = activityFile.getInputStream();
            //获取excel文本
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = wb.getSheetAt(0);//页的下标，从0开始
            HSSFRow row=null;
            HSSFCell cell=null;
            //解析excel文本，封装成Activity对象
            Activity activity=null;
            List<Activity> activityList=new ArrayList<>();
            for(int i=1;i<=sheet.getLastRowNum();i++){//sheet.getLastRowNum()：最后一行的下标
                row=sheet.getRow(i);//行的下标，下标从0开始
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formatDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for(int j=0;j<row.getLastCellNum();j++){//row.getLastCellNum():最后一列的下标+1
                    //根据HSSFRow对象，封装一列信息
                    cell=row.getCell(j);//列的下标，下标从0开始
                    //获取列中的数据
                    String cellValueForStr = HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(cellValueForStr);
                    }else if(j==1){
                        activity.setStartDate(cellValueForStr);
                    }else if(j==2){
                        activity.setEndDate(cellValueForStr);
                    }else if(j==3){
                        activity.setCost(cellValueForStr);
                    }else if(j==4) {
                        activity.setDescription(cellValueForStr);
                    }
                }
                //每一行封装完之后，添加到list集合
                activityList.add(activity);
            }
            //调用service层方法，保存导入的市场活动
            int count=activityService.saveCreateActivityByList(activityList);
            if(count>0){
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setMessage("导入"+count+"条市场活动");
            }else{
                returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试...");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/detail.do")
    public String detailActivity(String id,HttpServletRequest request){
        //调用service层方法，查询详细信息
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //把数据存到request中
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        //请求转发
        return "workbench/activity/detail";
    }

}
