package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.CustomerRemark;
import com.jm.crm.workbench.pojo.CustomerRemarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerRemarkMapper {
    long countByExample(CustomerRemarkExample example);

    int deleteByExample(CustomerRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(CustomerRemark row);

    int insertSelective(CustomerRemark row);

    List<CustomerRemark> selectByExample(CustomerRemarkExample example);

    CustomerRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") CustomerRemark row, @Param("example") CustomerRemarkExample example);

    int updateByExample(@Param("row") CustomerRemark row, @Param("example") CustomerRemarkExample example);

    int updateByPrimaryKeySelective(CustomerRemark row);

    int updateByPrimaryKey(CustomerRemark row);

    /**
     * 批量保存客户备注
     * @param list
     * @return
     */
    int insertCustomerRemarkByList(List<CustomerRemark> list);
}