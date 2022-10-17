package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.FunnelVO;
import com.jm.crm.workbench.pojo.Tran;
import com.jm.crm.workbench.pojo.TranExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface TranMapper {
    long countByExample(TranExample example);

    int deleteByExample(TranExample example);

    int deleteByPrimaryKey(String id);

    int insert(Tran row);

    int insertSelective(Tran row);

    List<Tran> selectByExample(TranExample example);

    Tran selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") Tran row, @Param("example") TranExample example);

    int updateByExample(@Param("row") Tran row, @Param("example") TranExample example);

    int updateByPrimaryKeySelective(Tran row);

    int updateByPrimaryKey(Tran row);

    /**
     * 保存新建的交易
     * @param tran
     * @return
     */
    int insertByTran(Tran tran);

    List<Tran> selectTransactionByConditionForPage(Map<String, Object> map);

    int selectCountOfTransactionByCondition(Map<String, Object> map);

    /**
     * 根据id查询详细信息
     * @param id
     * @return
     */
    Tran selectTranForDetailById(String id);

    /**
     * 分组查询各阶段的数量
     * @return
     */
    List<FunnelVO> selectCountOfTranGroupByStage();
}