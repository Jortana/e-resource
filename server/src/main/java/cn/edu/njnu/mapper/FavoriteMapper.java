package cn.edu.njnu.mapper;

import cn.edu.njnu.pojo.Folder;
import cn.edu.njnu.pojo.Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface FavoriteMapper {
    List<Map> folder(String username);
    ArrayList<Resource> collection(String folderID);
    boolean createFolder(String id, String name, String introduction, String username, String date);
    Folder queryFolder(String id);
}
