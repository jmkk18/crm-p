package com.jm.crm.workbench.service.impl;

import com.jm.crm.commons.constants.Constants;
import com.jm.crm.commons.utils.DateUtils;
import com.jm.crm.commons.utils.UUIDUtils;
import com.jm.crm.settings.pojo.User;
import com.jm.crm.workbench.mapper.*;
import com.jm.crm.workbench.pojo.*;
import com.jm.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;

    @Override
    public List<Clue> selectClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int queryCountOfClueByCondition(Map<String, Object> map) {
        return clueMapper.selectCountOfClueByCondition(map);
    }

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailById(String id) {
        return clueMapper.selectClueForDetailById(id);
    }

    @Override
    public void saveConvertClue(Map<String, Object> map) {
        //获取参数
        String clueId= (String) map.get("clueId");
        User user= (User) map.get(Constants.SESSION_USER);
        String isCreateTran= (String) map.get("isCreateTran");
        //根据ID查询线索信息
        Clue clue = clueMapper.selectClueById(clueId);
        //把线索中有关公司的信息转换到客户表中
        Customer customer=new Customer();
        customer.setAddress(clue.getAddress());
        customer.setCreateBy(user.getId());
        customer.setDescription(clue.getDescription());
        customer.setName(clue.getCompany());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(user.getId());
        customer.setPhone(clue.getPhone());
        customer.setWebsite(clue.getWebsite());
        customerMapper.insertCustomer(customer);
        //把线索中有关联系人的信息转换到客户表中
        Contacts contacts=new Contacts();
        contacts.setAddress(clue.getAddress());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setDescription(clue.getDescription());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setCreateBy(user.getId());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setOwner(user.getId());
        contacts.setSource(customer.getId());
        contacts.setJob(clue.getJob());
        contacts.setEmail(clue.getEmail());
        contacts.setCustomerId(clue.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setId(UUIDUtils.getUUID());
        contacts.setMphone(clue.getMphone());
        contactsMapper.insertContacts(contacts);

        //根据ID查询线索下的所有备注
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectClueRemarkByClueId(clueId);
        //如果该线索下有备注，就copy一份到客户备注表中,copy一份到联系人备注表
        if(clueRemarkList!=null&&clueRemarkList.size()>0){
            //封装实体类
            CustomerRemark customerRemark=null;
            ContactsRemark contactsRemark=null;
            List<CustomerRemark> customerRemarkList=new ArrayList<>();
            List<ContactsRemark> contactsRemarkList=new ArrayList<>();
            for(ClueRemark clueRemark:clueRemarkList) {
                customerRemark = new CustomerRemark();
                customerRemark.setCustomerId(customer.getId());
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemarkList.add(customerRemark);

                contactsRemark.setContactsId(contacts.getId());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemarkList.add(contactsRemark);
            }
            customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
            contactsRemarkMapper.insertContactsRemarkByList(contactsRemarkList);
        }

        //根据clueId查询线索和市场活动的关联关系
        List<ClueActivityRelation> carList = clueActivityRelationMapper.selectClueActivityRelationByClueId(clueId);
        //把该线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
        if(carList!=null&&carList.size()>0){
            List<ContactsActivityRelation> contactsActivityRelationList=new ArrayList<>();
            ContactsActivityRelation contactsActivityRelation=null;
            for(ClueActivityRelation car:carList){
                contactsActivityRelation=new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelation.setActivityId(car.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationByList(contactsActivityRelationList);
        }

        //如果需要创建交易，则往交易表中添加一条记录,还需要把该线索下的备注转换到交易备注表中一份
        if("true".equals(isCreateTran)){
            Tran tran=new Tran();
            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney((String) map.get("money"));
            tran.setName((String) map.get("name"));
            tran.setExpectedDate((String) map.get("expectedDate"));
            tran.setCustomerId(customer.getId());
            tran.setStage((String) map.get("stage"));
            tran.setActivityId((String) map.get("activityId"));
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateUtils.formatDateTime(new Date()));
            tranMapper.insertByTran(tran);

            if(clueRemarkList!=null&&clueRemarkList.size()>0){
                TranRemark tranRemark=null;
                List<TranRemark> tranRemarkList=new ArrayList<>();
                for(ClueRemark clueRemark:clueRemarkList){
                    tranRemark=new TranRemark();
                    //id, note_content, create_by, create_time, edit_by, edit_time, edit_flag, tran_id
                    tranRemark.setId(UUIDUtils.getUUID());
                    tranRemark.setNoteContent(clueRemark.getNoteContent());
                    tranRemark.setCreateBy(clueRemark.getCreateBy());
                    tranRemark.setCreateTime(clueRemark.getCreateTime());
                    tranRemark.setEditBy(clueRemark.getEditBy());
                    tranRemark.setEditTime(clueRemark.getEditTime());
                    tranRemark.setEditFlag(clueRemark.getEditFlag());
                    tranRemark.setTranId(tran.getId());
                    tranRemarkList.add(tranRemark);
                }
                tranRemarkMapper.insertTranRemarkByList(tranRemarkList);
            }
        }

        //删除该线索下的备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        //删除该线索和市场活动的关系
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        //删除该线索
        clueMapper.deleteClueById(clueId);
    }
}
