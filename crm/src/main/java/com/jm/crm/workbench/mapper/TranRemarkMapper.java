package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.TranRemark;
import com.jm.crm.workbench.pojo.TranRemarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TranRemarkMapper {
    long countByExample(TranRemarkExample example);

    int deleteByExample(TranRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(TranRemark row);

    int insertSelective(TranRemark row);

    List<TranRemark> selectByExample(TranRemarkExample example);

    TranRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") TranRemark row, @Param("example") TranRemarkExample example);

    int updateByExample(@Param("row") TranRemark row, @Param("example") TranRemarkExample example);

    int updateByPrimaryKeySelective(TranRemark row);

    int updateByPrimaryKey(TranRemark row);

    int insertTranRemarkByList(List<TranRemark> list);

    /**
     * 根据tranId查询交易备注
     * @param tranId
     * @return
     */
    List<TranRemark> selectTranRemarkForDetailByTranId(String tranId);
}