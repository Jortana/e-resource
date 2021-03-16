package cn.edu.njnu.service;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ResourceService {
    private final ResourceMapper resourceMapper;

    public ResourceService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    public Result conditionalQueryResource(Map<String, Object> conditionalMap){
        System.out.println(conditionalMap);
        String keyword = conditionalMap.get("keyword") != null ? (String) conditionalMap.get("keyword") : null;
        int resourceType = conditionalMap.get("resourceType") != null ? Integer.parseInt ((String) conditionalMap.get("resourceType")) : 0;
        int period = conditionalMap.get("period") != null ? Integer.parseInt ((String)conditionalMap.get("period")) : 0;
        int grade = conditionalMap.get("grade") != null?Integer.parseInt ((String)conditionalMap.get("grade")) : 0;
        int subject = conditionalMap.get("subject") != null ? Integer.parseInt ((String)conditionalMap.get("subject")) : 0;
        String updateTime = conditionalMap.get("updateTime") != null ? (String) conditionalMap.get("updateTime") : null;

//        int relevantType = Integer.parseInt ((String) conditionalMap.get("relevantType"));
//        int relevantID = Integer.parseInt ((String) conditionalMap.get("relevantID"));
        int page = Integer.parseInt ((String)conditionalMap.get("page"));
        int perPage = Integer.parseInt ((String)conditionalMap.get("perPage"));
        int limit = perPage*(page - 1);
//        System.out.println(limit);
        ArrayList<Resource> resourceList = resourceMapper.queryResourceByKeywords(keyword, resourceType, period, grade, subject, updateTime, limit, perPage);
        int total = resourceMapper.queryResourceNumByKeywords(keyword, resourceType, period, grade, subject, updateTime);
        System.out.println(resourceList);
        if (total == 0){
            return ResultFactory.buildSuccessResult("未查询到相关资源", null);
        }

        JSONObject resultData = new JSONObject();
        resultData.put("page", page);
        resultData.put("perPage", perPage);
        resultData.put("pages", (int)(total/Math.ceil(resourceList.size())));
        resultData.put("total", total);
        JSONArray resources = new JSONArray();
        for (Resource perResource : resourceList){
            JSONObject resultPerResource = new JSONObject();
            resultPerResource.put("resourceID", perResource.getId());
            resultPerResource.put("name", perResource.getResourceName());
            resultPerResource.put("download", perResource.getDownload());
            resultPerResource.put("collection", perResource.getCollection());
            resultPerResource.put("url", perResource.getUrl());
            String[] entityList = perResource.getEntity().split("#");
            resultPerResource.put("entity", entityList);
            resultPerResource.put("period", perResource.getPeriod());
            resultPerResource.put("grade", perResource.getGrade());
            resultPerResource.put("subject", perResource.getSubject());
            resources.add(resultPerResource);
        }

        resultData.put("resources", resources);
        return ResultFactory.buildSuccessResult("查询成功", resultData);
    }

//    public Result queryResourceByID(Map<String, Object> ResourceIDMap){
//        int resourceID = (int) ResourceIDMap.get("resourceID");
//    }
}
