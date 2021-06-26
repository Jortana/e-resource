package cn.edu.njnu.service;

import cn.edu.njnu.mapper.FavoriteMapper;
import cn.edu.njnu.pojo.Folder;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.pojo.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.neo4j.kernel.api.impl.index.storage.layout.FolderLayout;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FavoriteService {
    private final FavoriteMapper favoriteMapper;

    public FavoriteService(FavoriteMapper favoriteMapper) {
        this.favoriteMapper = favoriteMapper;
    }
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
            shortBuffer.append(chars[x % 0x25]);
        }
        return shortBuffer.toString();
    }


    public Result favorite(){
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        JSONArray resObject = new JSONArray();
        List<Map> folderList = favoriteMapper.folder(username);
        for (Map folder:folderList){
            JSONObject singleFolder = new JSONObject();
            System.out.println(folder);
            String folderID = (String) folder.get("id");
            String folderName = (String) folder.get("name");
            String introduction = (String) folder.get("introduction");
            singleFolder.put("folderID", folderID);
            singleFolder.put("folderName", folderName);
            singleFolder.put("introduction", introduction);
            singleFolder.put("resources", favoriteMapper.collection(folderID));
            
            resObject.add(singleFolder);
        }
        return ResultFactory.buildSuccessResult("收藏夹获取成功", resObject);
    }

    public Result createFolder(Map<String, Object> infoMap){
        String id = getUUID();
        String username = (String) SecurityUtils.getSubject().getPrincipal();
        String name = (String) infoMap.get("name");
        String introduction = (String) infoMap.get("introduction");
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        if (favoriteMapper.createFolder(id, name, introduction, username, dateStr)){
            Folder folder = favoriteMapper.queryFolder(id);
            return ResultFactory.buildSuccessResult("创建成功",folder);
        }
        return ResultFactory.buildFailResult("创建失败");
    }
}
