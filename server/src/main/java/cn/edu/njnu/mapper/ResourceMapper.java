package cn.edu.njnu.mapper;

import cn.edu.njnu.pojo.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ResourceMapper {
    //根据条件查询资源
    ArrayList<Resource> queryResourceByKeywords(String keyword, int resourceType, int period, int grade, int subject, String updateTime, int limit, int perPage);
    int queryResourceNumByKeywords(String keyword, int resourceType, int period, int grade, int subject, String updateTime);
    //通过ID获取资源信息
    Resource queryResourceByID(int resource_id);
    List<Map> queryType();
    //根据ID查相关资源
    String queryRelated(int resource_id);
    ArrayList<Resource> queryResourceByEntity(String entity,int start,int end);
    List<Map> queryGoalAndKey(String entity);
    //更新资源浏览次数
    boolean updateBrowse(int browse, int resourceID);
    //更新资源浏览次数
    boolean updateDownload(int download, int resourceID);
    //根据条件查询资源
    ArrayList<Resource> queryHot();
    //根据条件查询资源
    ArrayList<Resource> queryTime();
}
