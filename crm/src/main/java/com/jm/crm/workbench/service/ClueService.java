package com.jm.crm.workbench.service;

import com.jm.crm.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {
    List<Clue> selectClueByConditionForPage(Map<String,Object> map);

    int queryCountOfClueByCondition(Map<String,Object> map);

    int saveCreateClue(Clue clue);

    Clue queryClueForDetailById(String id);

    void saveConvertClue(Map<String,Object> map);
}
