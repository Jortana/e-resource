package cn.edu.njnu.mapper;

import cn.edu.njnu.pojo.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Mapper
@Repository
public interface ResourceMapper {
    //根据条件查询资源
    ArrayList<Resource> queryResourceByKeywords(String keyword, int resourceType, int period, int grade, int subject, String updateTime, int limit, int perPage);
    int queryResourceNumByKeywords(String keyword, int resourceType, int period, int grade, int subject, String updateTime);
    //通过ID获取资源信息
    Resource queryResourceByID(int resource_id);
}
