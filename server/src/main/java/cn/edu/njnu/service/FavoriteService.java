package cn.edu.njnu.service;

import cn.edu.njnu.mapper.FavoriteMapper;
import cn.edu.njnu.pojo.Folder;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FavoriteService {
    private final FavoriteMapper favoriteMapper;

    public FavoriteService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }
    //生成8位id
    public static  String getUUID()
    {
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
                "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
                "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9"};
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 0x24]);
        }
        return shortBuffer.toString();
    }

    //根据username获取收藏夹
    public Result favorite(){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        JSONArray resObject = new JSONArray();
        List<Map> folderList = favoriteMapper.folder(username);
        for (Map folder:folderList){
            JSONObject singleFolder = new JSONObject();
            String folderID = (String) folder.get("id");
            String folderName = (String) folder.get("name");
            String introduction = (String) folder.get("introduction");
            singleFolder.put("folderID", folderID);
            singleFolder.put("folderName", folderName);
            singleFolder.put("introduction", introduction);
            singleFolder.put("resourceNum", favoriteMapper.number(folderID));
            resObject.add(singleFolder);
        }
        return ResultFactory.buildSuccessResult("收藏夹获取成功", resObject);
    }

    //根据收藏夹ID获取资源
    public Result folderResource(String folderID){
        JSONObject folder = new JSONObject();
        folder.put("resources", favoriteMapper.collection(folderID));
        ArrayList<Map> contentMap = favoriteMapper.collectionStr(folderID);
        ArrayList<String> content = new ArrayList<>();
        if (contentMap.size()>0){
            System.out.println(contentMap);
            for (Map singleContent:contentMap){
                if (singleContent!=null){
                    content.add((String) singleContent.get("content"));
                }
            }
        }
        folder.put("content", content);

        ArrayList<Map> goalMap = favoriteMapper.goal(folderID);
        ArrayList<String> goal = new ArrayList<>();
        System.out.println(goalMap);
        if (goalMap.size()>0){
            for (Map singleGoal:goalMap){
                if (singleGoal!=null){
                    goal.add((String) singleGoal.get("goal"));
                }

            }
        }
        folder.put("goal", goal);

        ArrayList<Map> keyMap = favoriteMapper.key(folderID);
        ArrayList<String> key = new ArrayList<>();
        if (goalMap.size()>0){
            for (Map singleKey:keyMap){
                if (singleKey!=null){
                    key.add((String) singleKey.get("key"));
                }

            }
        }
        folder.put("key", key);
        return ResultFactory.buildSuccessResult("获取资源成功", folder);
    }
    //用户创建收藏夹
    public Result createFolder(Map<String, Object> infoMap){
        String id = getUUID();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        String name = (String) infoMap.get("name");
        String introduction = (String) infoMap.get("introduction");
        int date = (int) System.currentTimeMillis();
        if (favoriteMapper.createFolder(id, name, introduction, username, date)){
            Folder folder = favoriteMapper.queryFolder(id);
            return ResultFactory.buildSuccessResult("创建成功",folder);
        }
        return ResultFactory.buildFailResult("创建失败");
    }
    //用户修改收藏夹
    public Result updateFolder(Map<String, Object> infoMap){
        String id = (String) infoMap.get("folderID");
        String name = (String) infoMap.get("name");
        String introduction = (String) infoMap.get("introduction");
        if (favoriteMapper.updateFolder(id, name, introduction)){
            Folder folder = favoriteMapper.queryFolder(id);
            return ResultFactory.buildSuccessResult("创建成功",folder);
        }
        return ResultFactory.buildFailResult("创建失败");
    }
    //根据收藏夹ID删除收藏夹
    public Result deleteFolder(String folderID){
        favoriteMapper.deleteResource(folderID);
        favoriteMapper.deleteFolder(folderID);
        return ResultFactory.buildSuccessResult("删除成功", null);
    }
    //资源加入资源包
    public Result putInFolder(Map<String, Object> IDMap){
        int date = (int) System.currentTimeMillis();
        ArrayList<String> folderIDList = (ArrayList<String>) IDMap.get("addFolderID");
        ArrayList<String> delFolderIDList = (ArrayList<String>) IDMap.get("deleteFolderID");
        if (IDMap.containsKey("resourceID")){
            int resourceID = (int) IDMap.get("resourceID");
            for (String folderID:delFolderIDList){
                favoriteMapper.delFolderResource(resourceID, folderID);
            }
            for (String folderID:folderIDList){
                favoriteMapper.putInFolder(resourceID, folderID, date);
            }
        }
        else if (IDMap.containsKey("goal")){
            String goal = (String) IDMap.get("goal");
            for (String folderID:delFolderIDList){
                favoriteMapper.delFolderGoal(goal, folderID);
            }
            for (String folderID:folderIDList){
                favoriteMapper.putGoal(goal, folderID, date);
            }
        }
        else if (IDMap.containsKey("key")){
            String key = (String) IDMap.get("key");
            for (String folderID:delFolderIDList){
                favoriteMapper.delFolderKey(key, folderID);
            }
            for (String folderID:folderIDList){
                favoriteMapper.putKey(key, folderID, date);
            }
        }
        else {
            String content = (String) IDMap.get("content");
            for (String folderID:delFolderIDList){
                favoriteMapper.delFolderContent(content, folderID);
            }
            for (String folderID:folderIDList){
                favoriteMapper.putInFolderStr(content, folderID, date);
            }
        }
        return ResultFactory.buildSuccessResult("添加成功", null);
    }
}
