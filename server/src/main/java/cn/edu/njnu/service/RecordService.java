package cn.edu.njnu.service;

import cn.edu.njnu.mapper.RecordMapper;
import cn.edu.njnu.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class RecordService {
    private final RecordMapper recordMapper;
    private final ResourceMapper resourceMapper;
    public RecordService(RecordMapper recordMapper, ResourceMapper resourceMapper) {
        this.recordMapper = recordMapper;
        this.resourceMapper = resourceMapper;
    }

    public void addRecord(Map recordMap){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String browseDate = formatter.format(date);
        if (recordMap.containsKey("resourceID")){
            int resourceID = (int) recordMap.get("resourceID");
            int browse = resourceMapper.queryResourceByID(resourceID).getBrowse() + 1;
            System.out.println(browse);
            resourceMapper.updateBrowse(browse, resourceID);
        }
        if (recordMap.containsKey("userId")){
            int userID = (int) recordMap.get("userId");
            if (recordMap.containsKey("entityName")){
                String entityName = (String) recordMap.get("entityName");
                recordMapper.addEntityRecord(userID, browseDate, entityName);
            }
            if (recordMap.containsKey("resourceID")){
                int resourceID = (int) recordMap.get("resourceID");
                recordMapper.addResourceRecord(userID, browseDate, resourceID);
            }
        }
    }
}
