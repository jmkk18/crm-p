package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.ContactsRemark;
import com.jm.crm.workbench.pojo.ContactsRemarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ContactsRemarkMapper {
    long countByExample(ContactsRemarkExample example);

    int deleteByExample(ContactsRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(ContactsRemark row);

    int insertSelective(ContactsRemark row);

    List<ContactsRemark> selectByExample(ContactsRemarkExample example);

    ContactsRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") ContactsRemark row, @Param("example") ContactsRemarkExample example);

    int updateByExample(@Param("row") ContactsRemark row, @Param("example") ContactsRemarkExample example);

    int updateByPrimaryKeySelective(ContactsRemark row);

    int updateByPrimaryKey(ContactsRemark row);

    /**
     * 批量保存联系人备注
     * @param list
     * @return
     */
    int insertContactsRemarkByList(List<ContactsRemark> list);
}