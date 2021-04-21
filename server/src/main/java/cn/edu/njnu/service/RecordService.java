package cn.edu.njnu.service;

import cn.edu.njnu.mapper.RecordMapper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class RecordService {
    private final RecordMapper recordMapper;

    public RecordService(RecordMapper recordMapper) {
        this.recordMapper = recordMapper;
    }
    public void addRecord(Map recordMap){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String browseDate = formatter.format(date);
        System.out.println(browseDate);
        int userID = Integer.parseInt((String) recordMap.get("userID"));
        if (recordMap.containsKey("entityName")){
            String entityName = (String) recordMap.get("entityName");
            recordMapper.addEntityRecord(userID, browseDate, entityName);
        }
        if (recordMap.containsKey("resourceID")){
            int resourceID = Integer.parseInt((String) recordMap.get("resourceID"));
            recordMapper.addResourceRecord(userID, browseDate, resourceID);
        }
    }
}
