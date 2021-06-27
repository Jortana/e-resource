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
    ArrayList<Map> collection(String folderID);
    ArrayList<Map> collectionStr(String folderID);
    boolean createFolder(String id, String name, String introduction, String username, String date);
    Folder queryFolder(String id);
    boolean putInFolder(int resourceID, String folderID, String date);
    Map queryCollection(int resourceID, String folderID);
    boolean putInFolderStr(String content, String folderID, String date);
    Map queryCollectionStr(String content, String folderID);
    int number(String folderID);
}
