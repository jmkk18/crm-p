package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.ClueRemark;
import com.jm.crm.workbench.pojo.ClueRemarkExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClueRemarkMapper {
    long countByExample(ClueRemarkExample example);

    int deleteByExample(ClueRemarkExample example);

    int deleteByPrimaryKey(String id);

    int insert(ClueRemark row);

    int insertSelective(ClueRemark row);

    List<ClueRemark> selectByExample(ClueRemarkExample example);

    ClueRemark selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") ClueRemark row, @Param("example") ClueRemarkExample example);

    int updateByExample(@Param("row") ClueRemark row, @Param("example") ClueRemarkExample example);

    int updateByPrimaryKeySelective(ClueRemark row);

    int updateByPrimaryKey(ClueRemark row);

    /**
     * 根据clueId查询该线索下的详细备注
     * @param clueId
     * @return
     */
    List<ClueRemark> selectClueRemarkForDetailByClueId(String clueId);

    /**
     * 根据clueId下旬该线索下的备注
     * @param clueId
     * @return
     */
    List<ClueRemark> selectClueRemarkByClueId(String clueId);

    /**
     * 根据clueId删除该线索下的备注
     * @param clueId
     * @return
     */
    int deleteClueRemarkByClueId(String clueId);
}