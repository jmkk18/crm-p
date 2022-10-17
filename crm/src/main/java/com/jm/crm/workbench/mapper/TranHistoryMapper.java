package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.TranHistory;
import com.jm.crm.workbench.pojo.TranHistoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TranHistoryMapper {
    long countByExample(TranHistoryExample example);

    int deleteByExample(TranHistoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(TranHistory row);

    int insertSelective(TranHistory row);

    List<TranHistory> selectByExample(TranHistoryExample example);

    TranHistory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") TranHistory row, @Param("example") TranHistoryExample example);

    int updateByExample(@Param("row") TranHistory row, @Param("example") TranHistoryExample example);

    int updateByPrimaryKeySelective(TranHistory row);

    int updateByPrimaryKey(TranHistory row);

    /**
     * 保存交易的历史记录
     * @param tranHistory
     * @return
     */
    int insertTranHistory(TranHistory tranHistory);

    /**
     * 根据tranId查询交易历史
     * @param tranId
     * @return
     */
    List<TranHistory> selectTranHistoryForDetailByTranId(String tranId);
}