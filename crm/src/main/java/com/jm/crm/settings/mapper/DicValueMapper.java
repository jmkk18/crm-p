package com.jm.crm.settings.mapper;

import com.jm.crm.settings.pojo.DicValue;
import com.jm.crm.settings.pojo.DicValueExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface DicValueMapper {
    long countByExample(DicValueExample example);

    int deleteByExample(DicValueExample example);

    int deleteByPrimaryKey(String id);

    int insert(DicValue row);

    int insertSelective(DicValue row);

    List<DicValue> selectByExample(DicValueExample example);

    DicValue selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("row") DicValue row, @Param("example") DicValueExample example);

    int updateByExample(@Param("row") DicValue row, @Param("example") DicValueExample example);

    int updateByPrimaryKeySelective(DicValue row);

    int updateByPrimaryKey(DicValue row);

    /**
     * 根据typeCode查询字典值
     * @param typeCode
     * @return
     */
    List<DicValue> selectDicValueByTypeCode(String typeCode);
}