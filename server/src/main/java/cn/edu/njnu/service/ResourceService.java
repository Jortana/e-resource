package cn.edu.njnu.service;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultCode;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.catalina.webresources.AbstractSingleArchiveResource;
import org.neo4j.kernel.api.impl.index.storage.DirectoryFactory;
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
        //关键词分词提取
        String keywordList = new String();
        if (keyword != null){
            org.ansj.domain.Result result = ToAnalysis.parse(keyword); //封装的分词结果对象，包含一个terms列表
            List<Term> terms = result.getTerms(); //term列表，元素就是拆分出来的词以及词性
            for(Term term:terms){
                System.out.println(term.getName());		//分词的内容
            }
        }
        int resourceType = conditionalMap.get("resourceType") != null ? Integer.parseInt ((String) conditionalMap.get("resourceType")) : 0;
        int period = conditionalMap.get("period") != null ? Integer.parseInt ((String)conditionalMap.get("period")) : 0;
        int grade = conditionalMap.get("grade") != null ? Integer.parseInt ((String)conditionalMap.get("grade")) : 0;
        int subject = conditionalMap.get("subject") != null ? Integer.parseInt ((String)conditionalMap.get("subject")) : 0;
        String updateTime = conditionalMap.get("updateTime") != null ? (String) conditionalMap.get("updateTime") : null;
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
    //查相关资源
    public Result queryRelated(Map<String, Object> resourceIDMap){
        int resourceID = Integer.parseInt((String)resourceIDMap.get("resourceID"));
        String related_10 = resourceMapper.queryRelated(resourceID);
        if (related_10 == null){
            return ResultFactory.buildFailResult("未查询到相关资源");
        }
        JSONObject resObject = new JSONObject();
        JSONArray resourcesList = new JSONArray();
        String[] relatedID = related_10.split("#");
        for (String perResourceID : relatedID){
            int id = Integer.parseInt(perResourceID);
            Resource singleResource = resourceMapper.queryResourceByID(id);
            resourcesList.add(entityList(singleResource));
        }
        return ResultFactory.buildSuccessResult("查询成功", resourcesList);
    }
    //根据ID查资源属性
    public Result queryResource(Map<String, Object> ResourceIDMap){
        int resourceID = Integer.parseInt ((String) ResourceIDMap.get("resourceID"));
        Resource queryResource = resourceMapper.queryResourceByID(resourceID);
        return ResultFactory.buildSuccessResult("查询成功",entityList(queryResource));
    }
    //将数据库entity转换为List类型
    public Resource entityList(Resource resource){
        resource.setEntityList(resource.getEntity().split("#"));
        return resource;
    }
}
