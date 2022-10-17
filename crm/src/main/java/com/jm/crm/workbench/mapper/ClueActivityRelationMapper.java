package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.ClueActivityRelation;
import com.jm.crm.workbench.pojo.ClueActivityRelationExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ClueActivityRelationMapper {
    long countByExample(ClueActivityRelationExample example);

    int deleteByExample(ClueActivityRelationExample example);

    int deleteByPrimaryKey(String id);

    int insert(ClueActivityRelation row);

    int insertSelective(ClueActivityRelation row);

    List<ClueActivityRelation> selectByExample(ClueActivityRelationExample example);

    ClueActivityRelation selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") ClueActivityRelation row, @Param("example") ClueActivityRelationExample example);

    int updateByExample(@Param("row") ClueActivityRelation row, @Param("example") ClueActivityRelationExample example);

    int updateByPrimaryKeySelective(ClueActivityRelation row);

    int updateByPrimaryKey(ClueActivityRelation row);

    /**
     * 关联线索与市场活动
     * @param list
     * @return
     */
    int insertClueActivityRelationByList(List<ClueActivityRelation> list);

    /**
     * 解除关联：根据ActivityId，ClueId
     * @param map
     * @return
     */
    int deleteClueActivityRelationByActivityIdClueId(Map<String,Object> map);

    /**
     *  根据clueId查询线索和市场活动的关联关系
     * @param clueId
     * @return
     */
    List<ClueActivityRelation> selectClueActivityRelationByClueId(String clueId);

    /**
     * 根据clueId删除线索和市场活动的关联关系
     * @param clueId
     * @return
     */
    int deleteClueActivityRelationByClueId(String clueId);
}