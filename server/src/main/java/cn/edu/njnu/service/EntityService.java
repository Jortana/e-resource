package cn.edu.njnu.service;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.cypherdsl.core.Limit;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EntityService {
    private final ResourceMapper resourceMapper;

    public EntityService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    private Driver createDrive(){
        return GraphDatabase.driver( "bolt://223.2.50.241:7687", AuthTokens.basic( "neo4j", "123456" ) );
    }
    public JSONArray getRelatedEntity(String entityName, Session session){
        StatementResult result = session.run( "MATCH (a:concept) -[]-> (m:concept) where a.name = { name }" +
                        "RETURN m.name AS name limit 5",
                parameters( "name", entityName ) );
        JSONArray relatedEntity = new JSONArray();
        while ( result.hasNext() )
        {
            Record record = result.next();
            relatedEntity.add(record.get( "name" ).asString());
        }
        return relatedEntity;
    }

    public Result getRelatedEntity(Map<String, Object> keywordMap){
        String[] keyword = ((String) keywordMap.get("keyword")).split("#");
        Driver driver = createDrive();
        Session session = driver.session();
        JSONArray resArray = new JSONArray();
        if (keyword.length > 1) {
            for (String entityName : keyword){
                System.out.println(entityName);
                JSONObject similarEntity = new JSONObject();
                similarEntity.put("relatedEntity", getRelatedEntity(entityName, session));
                similarEntity.put("entityName", entityName);
                resArray.add(similarEntity);
            }
        }
        else {
            StatementResult result = session.run( "MATCH (a:concept) where a.name =~ {name} " +
                            "RETURN a.name AS name order by a.name limit 5",
                    parameters( "name", ".*" + keyword[0] + ".*" ) );
            if (!result.hasNext()){
                return ResultFactory.buildFailResult("未查询到相关知识点");
            }
            while ( result.hasNext() )
            {
                Record record = result.next();
                String entityName = record.get( "name" ).asString();
                JSONObject similarEntity = new JSONObject();
                similarEntity.put("relatedEntity", getRelatedEntity(entityName, session));
                similarEntity.put("entityName", entityName);
                resArray.add(similarEntity);
            }
        }

        return ResultFactory.buildSuccessResult("查询成功", resArray);
    }
    public Result getEntity(Map<String, Object> keywordMap) {
        String keyword = (String) keywordMap.get("keyword");
        int page = Integer.parseInt( (String) keywordMap.get("page") );
        int perPage = Integer.parseInt( (String) keywordMap.get("perPage") );
        int skip = (page - 1) * perPage;
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult result = session.run( "MATCH (a:concept) where a.name =~ {name} " +
                        "RETURN properties(a) AS props order by a.name skip {skip} limit {limit}",
                parameters( "name", ".*" + keyword + ".*", "skip", skip, "limit", perPage ) );
        JSONObject resObject = new JSONObject();
        JSONArray resArray = new JSONArray();
        if (!result.hasNext()){
            return ResultFactory.buildFailResult("未查询到相关知识点");
        }
        int totalEntity = 0;
        while ( result.hasNext() )
        {
            totalEntity ++;
            Record record = result.next();
            String entityName = record.get( "props" ).get( "name" ).asString();
            JSONObject similarEntity = new JSONObject();
            similarEntity.put("entityName", entityName);
            similarEntity.put("properties", record.get( "props" ).asMap());
            similarEntity.put("resources", queryResource(entityName));
            similarEntity.put("goalAndKey", goalAndKey(entityName));
            resArray.add(similarEntity);
        }
//        System.out.println("test!");
        resObject.put("resources", resArray);
        resObject.put("total", totalEntity);
        resObject.put("pages", (int)Math.ceil(totalEntity * 1.0 / perPage));
        session.close();
//        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resObject);
    }
    //根据entity查资源
    public JSONArray queryResource(String entityName) {
        String entity = entityName + '#';
        ArrayList<Resource> queryResource = resourceMapper.queryResourceByEntity(entity);
        JSONArray resourceList = new JSONArray();
        for (Resource perResource : queryResource){
            resourceList.add(perResource);
        }
        return resourceList;
    }
    //根据entity查找重难点
    public JSONArray goalAndKey(String entityName){
        JSONArray resArray = new JSONArray();
        String entity = entityName + '#';
        List<Map> goalAndKey = resourceMapper.queryGoalAndKey(entity);
        for (Map perGK : goalAndKey){
            JSONObject singleGK = new JSONObject();
            singleGK.put("objectives", perGK.get("t_goal"));
            singleGK.put("resourceID", perGK.get("resource_id"));
            singleGK.put("keyPoint", perGK.get("t_key"));
            resArray.add(singleGK);
        }
        return resArray;
    }

    public Result getProperties(Map<String, Object> nameMap){
        String entityName = (String) nameMap.get("entityName");
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult result = session.run( "MATCH (a:concept) where a.name = {name} " +
                        "RETURN properties(a) AS props ",
                parameters( "name",  entityName ) );
        if (!result.hasNext()) {
            return ResultFactory.buildFailResult("未查询到相关知识点");
        }
        JSONObject resObject = new JSONObject();
        Record record = result.next();
        resObject.put("entityName", entityName);
        resObject.put("properties", record.get( "props" ).asMap());
        session.close();
//        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resObject);
    }
}
