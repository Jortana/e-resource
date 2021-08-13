package cn.edu.njnu.mapper;

import cn.edu.njnu.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;

@Mapper
@Repository
public interface RecordMapper {
    void addEntityRecord(int userID, String browseDate, String entityName);
    void addResourceRecord(int userID, String browseDate, int resourceID);

    ArrayList<Map<String, Object>> record(int userID);
}
