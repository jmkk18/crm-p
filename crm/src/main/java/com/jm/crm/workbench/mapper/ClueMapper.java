package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.Activity;
import com.jm.crm.workbench.pojo.Clue;
import com.jm.crm.workbench.pojo.ClueExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface ClueMapper {
    long countByExample(ClueExample example);

    int deleteByExample(ClueExample example);

    int deleteByPrimaryKey(String id);

    int insert(Clue row);

    int insertSelective(Clue row);

    List<Clue> selectByExample(ClueExample example);

    Clue selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") Clue row, @Param("example") ClueExample example);

    int updateByExample(@Param("row") Clue row, @Param("example") ClueExample example);

    int updateByPrimaryKeySelective(Clue row);

    int updateByPrimaryKey(Clue row);

    /**
     * 根据条件分页查询线索的列表
     * @param map
     * @return
     */
    List<Clue> selectClueByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询线索的总条数
     * @param map
     * @return
     */
    int selectCountOfClueByCondition(Map<String,Object> map);

    /**
     * 添加线索
     * @param clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * 根据id查询详细信息
     * @param id
     * @return
     */
    Clue selectClueForDetailById(String id);

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    Clue selectClueById(String id);

    /**
     * 根据id删除线索
     * @param id
     * @return
     */
    int deleteClueById(String id);
}