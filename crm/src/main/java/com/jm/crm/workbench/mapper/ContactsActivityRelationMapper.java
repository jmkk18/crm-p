package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.ContactsActivityRelation;
import com.jm.crm.workbench.pojo.ContactsActivityRelationExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContactsActivityRelationMapper {
    long countByExample(ContactsActivityRelationExample example);

    int deleteByExample(ContactsActivityRelationExample example);

    int deleteByPrimaryKey(String id);

    int insert(ContactsActivityRelation row);

    int insertSelective(ContactsActivityRelation row);

    List<ContactsActivityRelation> selectByExample(ContactsActivityRelationExample example);

    ContactsActivityRelation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") ContactsActivityRelation row, @Param("example") ContactsActivityRelationExample example);

    int updateByExample(@Param("row") ContactsActivityRelation row, @Param("example") ContactsActivityRelationExample example);

    int updateByPrimaryKeySelective(ContactsActivityRelation row);

    int updateByPrimaryKey(ContactsActivityRelation row);

    /**
     * 批量保存新建的联系人和市场活动的关系
     * @param contactsActivityRelationList
     * @return
     */
    int insertContactsActivityRelationByList(List<ContactsActivityRelation> contactsActivityRelationList);
}