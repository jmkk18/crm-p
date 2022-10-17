package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.Customer;
import com.jm.crm.workbench.pojo.CustomerExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerMapper {
    long countByExample(CustomerExample example);

    int deleteByExample(CustomerExample example);

    int deleteByPrimaryKey(String id);

    int insert(Customer row);

    int insertSelective(Customer row);

    List<Customer> selectByExample(CustomerExample example);

    Customer selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") Customer row, @Param("example") CustomerExample example);

    int updateByExample(@Param("row") Customer row, @Param("example") CustomerExample example);

    int updateByPrimaryKeySelective(Customer row);

    int updateByPrimaryKey(Customer row);

    /**
     * 新增客户
     * @param customer
     * @return
     */
    int insertCustomer(Customer customer);

    /**
     * 根据name关键字查询CustomerName
     * @param name
     * @return
     */
    List<String> selectCustomerNameByName(String name);

    /**
     * 根据name查询信息
     * @param name
     * @return
     */
    Customer selectCustomerByName(String name);
}