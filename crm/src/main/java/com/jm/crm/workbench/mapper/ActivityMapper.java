package com.jm.crm.workbench.mapper;

import com.jm.crm.workbench.pojo.Activity;
import com.jm.crm.workbench.pojo.ClueRemark;

import java.util.List;
import java.util.Map;


public interface ActivityMapper {
    int deleteByPrimaryKey(String id);

    int insertSelective(Activity row);

    Activity selectByPrimaryKey(String id);


    int updateByPrimaryKeySelective(Activity row);

    int updateByPrimaryKey(Activity row);
    /**
     * 保存创建的市场活动
     */
    int insertActivity(Activity activity);

    /**
     * 根据条件分页查询市场活动的列表
     * @param map
     * @return
     */
    List<Activity> selectActivityByConditionForPage(Map<String,Object> map);

    /**
     * 根据条件查询市场活动的总条数
     * @param map
     * @return
     */
    int selectCountOfActivityByCondition(Map<String,Object> map);

    /**
     * 根据ids批量删除市场活动
     * @param ids
     * @return
     */
    int deleteActivityByIds(String[] ids);

    /**
     * 根据id查询市场活动的信息
     * @param id
     * @return
     */
    Activity selectActivityById(String id);

    /**
     * 保存修改的市场活动
     * @param activity
     * @return
     */
    int updateActivity(Activity activity);

    /**
     * 查询所有的市场活动
     * @return
     */
    List<Activity> selectAllActivitys();

    /**
     * 根据id数组查询所有符合的市场活动
     * @param ids
     * @return
     */
    List<Activity> selectActivityByIds(String[] ids);

    /**
     * 批量保存创建的市场活动
     * @param activityList
     * @return
     */
    int insertActivityByList(List<Activity> activityList);

    /**
     * 根据id查询市场活动的明细信息
     * @param id
     * @return
     */
    Activity selectActivityForDetailById(String id);

    /**
     * 根据线索id查询市场活动列表
     * @param clueId
     * @return
     */
    List<Activity> selectActivityForDetailByClueId(String clueId);

    /**
     * 根据市场活动id和线索id模糊查询未绑定该线索的市场活动
     * @param map
     * @return
     */
    List<Activity> selectActivityForDetailByNameClueId(Map<String,Object> map);

    /**
     * 根据id数组查询所有市场活动
     * @param id
     * @return
     */
    List<Activity> selectActivityForDetailByIds(String[] id);

    /**
     * 根据市场活动id和线索id模糊查询已绑定该线索的市场活动
     * @param map
     * @return
     */
    List<Activity> selectActivityForConvertByNameClueId(Map<String,Object> map);

    /**
     * 根据市场活动名字模糊查询
     * @param activityName
     * @return
     */
    List<Activity> selectActivityByFuzzyName(String activityName);
}