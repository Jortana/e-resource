package cn.edu.njnu.service;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultCode;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResourceService {
    private final ResourceMapper resourceMapper;

    public ResourceService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }
    //获取资源类型
    public Result getResourceType(){
        List<Map> typeMap = resourceMapper.queryType();
        System.out.println(typeMap);
        JSONArray typeArray = new JSONArray();
        JSONObject typeAll = new JSONObject();
        typeAll.put("type", "全部");
        typeAll.put("code", 0);
        typeArray.add(typeAll);
        for (Map map : typeMap){
            JSONObject typeCode = new JSONObject();
            typeCode.put("type", map.get("resource_type"));
            typeCode.put("code", map.get("type_id"));
            typeArray.add(typeCode);
        }
        return ResultFactory.buildSuccessResult("成功获取资源类型", typeArray);
    }
    //关键词查找资源
    public Result conditionalQueryResource(Map<String, Object> conditionalMap){
        System.out.println(conditionalMap);
        String keyword = conditionalMap.get("keyword") != null ? (String) conditionalMap.get("keyword") : null;
        int resourceType = conditionalMap.get("resourceType") != null ? Integer.parseInt ((String) conditionalMap.get("resourceType")) : 0;
        int period = conditionalMap.get("period") != null ? Integer.parseInt ((String)conditionalMap.get("period")) : 0;
        int grade = conditionalMap.get("grade") != null?Integer.parseInt ((String)conditionalMap.get("grade")) : 0;
        int subject = conditionalMap.get("subject") != null ? Integer.parseInt ((String)conditionalMap.get("subject")) : 0;
        String updateTime = conditionalMap.get("updateTime") != null ? (String) conditionalMap.get("updateTime") : null;

        //int relevantType = Integer.parseInt ((String) conditionalMap.get("relevantType"));
        //int relevantID = Integer.parseInt ((String) conditionalMap.get("relevantID"));
        int page = Integer.parseInt ((String)conditionalMap.get("page"));
        int perPage = Integer.parseInt ((String)conditionalMap.get("perPage"));
        int limit = perPage * (page - 1);
        int total = resourceMapper.queryResourceNumByKeywords(keyword, resourceType, period, grade, subject, updateTime);
        if (total == 0){
            return ResultFactory.buildFailResult("未查询到相关资源");
        }
        ArrayList<Resource> resourceList = resourceMapper.queryResourceByKeywords(keyword, resourceType, period, grade, subject, updateTime, limit, perPage);
        JSONObject resultData = new JSONObject();
        resultData.put("page", page);
        resultData.put("perPage", perPage);
        resultData.put("pages", (int)Math.ceil(total * 1.0 / resourceList.size()));
        resultData.put("total", total);
        JSONArray resources = new JSONArray();
        for (Resource perResource : resourceList){
            perResource.setEntityList(perResource.getEntity().split("#"));
            resources.add(perResource);
        }

        resultData.put("resources", resources);
        return ResultFactory.buildSuccessResult("查询成功", resultData);
    }

//    public Result queryResourceByID(Map<String, Object> ResourceIDMap){
//        int resourceID = (int) ResourceIDMap.get("resourceID");
//    }
}
