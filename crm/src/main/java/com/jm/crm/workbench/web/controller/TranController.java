package com.jm.crm.workbench.web.controller;

import com.jm.crm.commons.constants.Constants;
import com.jm.crm.commons.pojo.ReturnObject;
import com.jm.crm.settings.pojo.DicValue;
import com.jm.crm.settings.pojo.User;
import com.jm.crm.settings.service.DicValueService;
import com.jm.crm.settings.service.UserService;
import com.jm.crm.workbench.pojo.*;
import com.jm.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private TranService tranService;
    @Autowired
    private TranRemarkService tranRemarkService;
    @Autowired
    private TranHistoryService tranHistoryService;

    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法，查询动态数据
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        //把数据保存在request中
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/queryTranByConditionForPage.do")
    @ResponseBody
    public Object queryTranByConditionForPage(String owner, String name, String customer, String stage, String type,
                                              String source, String contacts, int pageNo, int pageSize){
        // 封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("owner", owner);
        map.put("name", name);
        map.put("customer", customer);
        map.put("source", source);
        map.put("stage", stage);
        map.put("type", type);
        map.put("contacts", contacts);
        map.put("beginNo", (pageNo - 1) * pageSize);
        map.put("pageSize", pageSize);
        //调用service层方法，查询分页信息
        List<Tran> tranList = tranService.queryTransactionByConditionForPage(map);
        int totalRows = tranService.queryCountOfTransactionByCondition(map);
        //封装查询参数，响应前端
        Map<String,Object> retMap=new HashMap<>();
        retMap.put("tranList",tranList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        //调用service层方法，查询动态数据
        List<User> userList=userService.queryAllUsers();
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        List<DicValue> transactionTypeList = dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        //把数据保存在request中
        request.setAttribute("userList",userList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        //请求转发
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        //解析properties文件，获取阶段对应的可能性
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        //根据阶段获取对应的可能性
        String possibility=bundle.getString(stageValue);
        //响应
        return possibility;
    }

    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryCustomerNameByName(String name){
        //调用service层方法，查询动态数据
        List<String> customerNameList = customerService.queryCustomerNameByName(name);
        //响应
        return customerNameList;
    }

    @RequestMapping("/workbench/transaction/queryActivityByFuzzyName.do")
    @ResponseBody
    public Object queryActivityByFuzzyName(String activityName){
        List<Activity> activityList = activityService.queryActivityByFuzzyName(activityName);
        return activityList;
    }

    @RequestMapping("/workbench/transaction/queryContactsByFuzzyName.do")
    @ResponseBody
    public Object queryContactsByFuzzyName(String contactsName){
        List<Contacts> contactsList = contactsService.queryContactsByFuzzyName(contactsName);
        return contactsList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        //封装参数
        map.put(Constants.SESSION_USER,session.getAttribute(Constants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法
            tranService.saveCreateTran(map);
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试...");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,HttpServletRequest request){
        //调用service层方法，查询数据
        Tran tran = tranService.queryTranForDetailById(id);
        List<TranRemark> tranRemarkList = tranRemarkService.queryTranRemarkForDetailByTranId(tran.getId());
        List<TranHistory> tranHistoryList = tranHistoryService.queryTranHistoryForDetailByTranId(tran.getId());
        List<DicValue> stageList = dicValueService.queryDicValueByTypeCode("stage");
        //根据交易所处阶段的名称去查询名字
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String possibility = bundle.getString(tran.getStage());
        //把数据存到request中
        request.setAttribute("tran",tran);
        request.setAttribute("tranRemarkList",tranRemarkList);
        request.setAttribute("tranHistoryList",tranHistoryList);
        request.setAttribute("possibility",possibility);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/transaction/detail";
    }


}
