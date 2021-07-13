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
    ArrayList<Map> key(String folderID);
    ArrayList<Map> goal(String folderID);
    boolean createFolder(String id, String name, String introduction, String username, int date);
    boolean updateFolder(String id, String name, String introduction);
    Folder queryFolder(String id);
    boolean putInFolder(int resourceID, String folderID, int date);
    boolean putGoal(String goal, String folderID, int date);
    boolean putKey(String key, String folderID, int date);
    Map queryCollection(int resourceID, String folderID);
    boolean putInFolderStr(String content, String folderID, int date);
    Map queryCollectionStr(String content, String folderID);
    int number(String folderID);
    boolean deleteFolder(String folderID);
    boolean deleteResource(String folderID);
    //delete
    boolean delFolderResource(int resourceID, String folderID);
    boolean delFolderContent(String content, String folderID);
    boolean delFolderGoal(String goal, String folderID);
    boolean delFolderKey(String key, String folderID);
}
