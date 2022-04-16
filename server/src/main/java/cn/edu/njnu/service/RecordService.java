package cn.edu.njnu.service;

import cn.edu.njnu.mapper.RecordMapper;
import cn.edu.njnu.mapper.ResourceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class RecordService {
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private ResourceMapper resourceMapper;


    public void addRecord(Map recordMap){
//        System.out.println("添加记录");
        String browser = (String) recordMap.get("browser");
        String OS = (String) recordMap.get("OS");
        String ipAddress = (String) recordMap.get("ipAddress");
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        long browseDate = System.currentTimeMillis();
//        String browseDate = formatter.format(date);
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
                recordMapper.addEntityRecord(userID, browseDate, entityName, browser, OS, ipAddress);
            }
            if (recordMap.containsKey("resourceID")){
                int resourceID = (int) recordMap.get("resourceID");
                recordMapper.addResourceRecord(userID, browseDate, resourceID, browser, OS, ipAddress);
            }
        }
    }
}
