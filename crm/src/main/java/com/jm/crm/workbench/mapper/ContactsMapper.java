package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.Contacts;
import com.jm.crm.workbench.pojo.ContactsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContactsMapper {
    long countByExample(ContactsExample example);

    int deleteByExample(ContactsExample example);

    int deleteByPrimaryKey(String id);

    int insert(Contacts row);

    int insertSelective(Contacts row);

    List<Contacts> selectByExample(ContactsExample example);

    Contacts selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") Contacts row, @Param("example") ContactsExample example);

    int updateByExample(@Param("row") Contacts row, @Param("example") ContactsExample example);

    int updateByPrimaryKeySelective(Contacts row);

    int updateByPrimaryKey(Contacts row);

    int insertContacts(Contacts contacts);

    /**
     * 根据联系人名字模糊查询联系人信息
     * @param contactsName
     * @return
     */
    List<Contacts> selectContactsByFuzzyName(String contactsName);
}