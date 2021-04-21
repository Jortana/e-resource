package cn.edu.njnu.service;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.cypherdsl.core.Limit;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EntityService {
    private final ResourceMapper resourceMapper;

    public EntityService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    private Driver createDrive(){
        return GraphDatabase.driver( "bolt://223.2.50.241:7687", AuthTokens.basic( "neo4j", "123456" ) );
    }
    public JSONArray getRelatedEntity(String entityName, Session session, String mainEntityName){
        StatementResult result = session.run( "MATCH (a:concept) -[k:Correlation]-> (m:concept) where a.name = { name } and m.name<>{mainEntity}" +
                        "RETURN m.name AS name limit 3",
                parameters( "name", entityName, "mainEntity", mainEntityName) );
        JSONArray relatedEntity = new JSONArray();
        while ( result.hasNext() )
        {
            Record record = result.next();
            relatedEntity.add(record.get( "name" ).asString());
        }
        relatedEntity.add(mainEntityName);
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
                similarEntity.put("relatedEntity", getRelatedEntity(entityName, session, entityName));
                similarEntity.put("entityName", entityName);
                resArray.add(similarEntity);
            }
        }
        else {
            StatementResult result = session.run( "MATCH (a:concept)-[]->(n:concept) where a.name = {name} " +
                            "RETURN n.name AS name limit 4",
                    parameters( "name",  keyword[0]  ) );
            if (!result.hasNext()){
                return ResultFactory.buildFailResult("未查询到相关知识点");
            }
            ArrayList<String> mainEntityRelated = new ArrayList<>();
            while ( result.hasNext() )
            {
                Record record = result.next();
                String entityName = record.get( "name" ).asString();
                mainEntityRelated.add(entityName);
                JSONObject similarEntity = new JSONObject();
                similarEntity.put("relatedEntity", getRelatedEntity(entityName, session, keyword[0]));
                similarEntity.put("entityName", entityName);
                resArray.add(similarEntity);
            }
            JSONObject mainEntity = new JSONObject();
            mainEntity.put("relatedEntity", mainEntityRelated);
            mainEntity.put("entityName", keyword[0]);
            resArray.add(mainEntity);
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
        StatementResult result = session.run( "MATCH (a:concept) where a.name = {name} " +
                        "RETURN properties(a) AS props order by a.name skip {skip} limit {limit}",
                parameters( "name", keyword, "skip", skip, "limit", perPage ) );
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
    //根据entity查找重难点,从mysql里面查
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
    public Result getEntity_neo4j(Map<String, Object> keywordMap) {
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
            similarEntity.put("goalAndKey", goalAndKey_neo4j(entityName));
            resArray.add(similarEntity);
        }
        resObject.put("resources", resArray);
        resObject.put("total", totalEntity);
        resObject.put("pages", (int)Math.ceil(totalEntity * 1.0 / perPage));
        session.close();
//        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resObject);
    }
    //根据entity查找重难点,从neo4j里面查
    public JSONObject goalAndKey_neo4j(String entityName){
        JSONObject resArray = new JSONObject();
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult keyResult = session.run( "MATCH (a:concept)-[k:key]->(m) where a.name = {name} " +
                        "RETURN m.content AS content",
                parameters( "name", entityName) );
        JSONArray keyArray = new JSONArray();
        while ( keyResult.hasNext() )
        {
            Record record = keyResult.next();
            String key = record.get("content").asString();
            keyArray.add(key);
        }
        resArray.put("key", keyArray);
        StatementResult goalResult = session.run( "MATCH (a:concept)-[k:goal]->(m) where a.name = {name} " +
                        "RETURN m.content AS content",
                parameters( "name", entityName) );
        JSONArray goalArray = new JSONArray();
        while ( goalResult.hasNext() )
        {
            Record record = goalResult.next();
            String goal = record.get("content").asString();
            goalArray.add(goal);
        }
        resArray.put("goal", goalArray);
        return resArray;
    }
    //获取知识点属性
    public Result getProperties(Map<String, Object> nameMap){
        String entityName = (String) nameMap.get("keyword");
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
        Map<String, Object> properties = record.get( "props" ).asMap();
        resObject.put("properties", properties);
        session.close();
//        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resObject);
    }
}
