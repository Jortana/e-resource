package cn.edu.njnu.service;

import cn.edu.njnu.mapper.FavoriteMapper;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FavoriteService {
    private final FavoriteMapper favoriteMapper;

    public FavoriteService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }


    public Result favorite(Map<String, Object> userIDMap){
        JSONArray resObject = new JSONArray();
        int userID = Integer.parseInt((String) userIDMap.get("userID"));
        List<Map> folderList = favoriteMapper.folder(userID);
        for (Map folder:folderList){
            JSONObject singleFolder = new JSONObject();
            System.out.println(folder);
            int folderID = (int) folder.get("folder_id");
            String folderName = (String) folder.get("folder_name");
            singleFolder.put("folderName", folderName);
            singleFolder.put("resources", favoriteMapper.collection(folderID));
            resObject.add(singleFolder);
        }
        return ResultFactory.buildSuccessResult("收藏夹获取成功", resObject);
    }
}
