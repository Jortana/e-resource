package cn.edu.njnu.service;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        String keyword = (String) keywordMap.get("keyword");
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult result = session.run( "MATCH (a:concept) where a.name =~ {name} " +
                        "RETURN a.name AS name order by a.name limit 5",
                parameters( "name", ".*" + keyword + ".*" ) );

        JSONArray resArray = new JSONArray();
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
        session.close();
        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resArray);
    }
    public Result getEntity(Map<String, Object> keywordMap){
        String keyword = (String) keywordMap.get("keyword");
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult result = session.run( "MATCH (a:concept) where a.name =~ {name} " +
                        "RETURN a.name AS name order by a.name limit 10",
                parameters( "name", ".*" + keyword + ".*" ) );

        JSONArray resArray = new JSONArray();
        if (!result.hasNext()){
            return ResultFactory.buildFailResult("未查询到相关知识点");
        }

        while ( result.hasNext() )
        {
            Record record = result.next();
            String entityName = record.get( "name" ).asString();
            JSONObject similarEntity = new JSONObject();
            similarEntity.put("resource", queryResource(entityName));
            similarEntity.put("entityName", entityName);
            resArray.add(similarEntity);
        }
        session.close();
        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resArray);
    }
    //根据entity查资源

    public JSONArray queryResource(String entityName) {
        String entity = entityName + '#';
        ArrayList<Resource> queryResource = resourceMapper.queryResourceByEntity(entity);
//        System.out.println(entity);
        JSONArray resourceList = new JSONArray();
        for (Resource perResource : queryResource){
            resourceList.add(perResource);
        }
        return resourceList;
    }

}