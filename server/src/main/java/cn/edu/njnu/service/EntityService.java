package cn.edu.njnu.service;

import cn.edu.njnu.mapper.RecordMapper;
import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.neo4j.cypherdsl.core.Case;
import org.neo4j.driver.v1.*;
import static org.neo4j.driver.v1.Values.parameters;

import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EntityService {
    private final ResourceMapper resourceMapper;
    private final RecordMapper recordMapper;
    public EntityService(ResourceMapper resourceMapper, RecordMapper recordMapper) {
        this.resourceMapper = resourceMapper;
        this.recordMapper = recordMapper;
    }
    String resourceRoot = "http://223.2.50.241:8082";
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
//    public Result getEntity(Map<String, Object> keywordMap) {
//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        String browseDate = formatter.format(date);
//        String keyword = (String) keywordMap.get("keyword");
//        if (keywordMap.containsKey("userId")){
//            int userId = Integer.parseInt((String) keywordMap.get("userId"));
//            recordMapper.addEntityRecord(userId,browseDate,keyword);
//        }
//        int sort = 0;  //0默认，1最热，2最新
//        int type = 0;  //0全部
//        if (keywordMap.containsKey("sort")){
//            sort = Integer.parseInt((String) keywordMap.get("sort"));
//        }
//        if (keywordMap.containsKey("type")){
//            type = Integer.parseInt((String) keywordMap.get("type"));
//        }
//        int page = Integer.parseInt( (String) keywordMap.get("page") );
//        int perPage = Integer.parseInt( (String) keywordMap.get("perPage") );
//        Driver driver = createDrive();
//        Session session = driver.session();
//
//        StatementResult result = session.run( "MATCH (a:concept) where a.name = {name} " +
//                        "RETURN properties(a) AS props order by a.name",
//                parameters( "name", keyword) );
//        JSONObject resObject = new JSONObject();
//        JSONArray resArray = new JSONArray();
//        if (!result.hasNext()){
//            return ResultFactory.buildFailResult("未查询到相关知识点");
//        }
//        int totalEntity = 0;
//        Record record = result.next();
//        String entityName = record.get( "props" ).get( "name" ).asString();
//        JSONObject similarEntity = new JSONObject();
//        similarEntity.put("entityName", entityName);
//        similarEntity.put("properties", record.get( "props" ).asMap());
//        if (sort != 0){
//            similarEntity.put("resources", queryResource(entityName,perPage,page,sort,type));
//            totalEntity = queryResource(entityName,100000,1, sort, type).size();
//        }
//        else {
//            int skip = (page-1)*perPage;
//            StatementResult resourceNode = null;
//            if (type == 0){
//                resourceNode = session.run( "MATCH (p:resource)-[r]->(a:concept) where a.name = {name}" +
//                                "RETURN p.id AS id order by r.tfidf skip {skip} limit {limit}",
//                        parameters( "name", keyword,"skip",skip,"limit",perPage) );
//                StatementResult count = session.run( "MATCH (p:resource)-[r]->(a:concept) where p.id>0 and a.name = {name} " +
//                                "RETURN count(*) as num",
//                        parameters( "name", keyword) );
//                if ( count.hasNext() )
//                {
//                    Record countRecord = count.next();
//                    totalEntity = countRecord.get( "num" ).asInt();
//                }
//            }
//            else {
//                resourceNode = session.run( "MATCH (p:resource)-[r]->(a:concept) where p.id>0 and a.name = {name} and p.type = {type} " +
//                                "RETURN p.id AS id order by r.tfidf skip {skip} limit {limit}",
//                        parameters( "name", keyword,"skip",skip,"limit",perPage,"type", type) );
//                StatementResult count = session.run( "MATCH (p:resource)-[r]->(a:concept) where p.id>0 and a.name = {name} and p.type = {type} " +
//                                "RETURN count(*) as num",
//                        parameters( "name", keyword, "type", type) );
//                if ( count.hasNext() )
//                {
//                    Record countRecord = count.next();
//                    totalEntity = countRecord.get( "num" ).asInt();
//                }
//
//            }
//
//            JSONArray resourceArray = new JSONArray();
//            while ( resourceNode.hasNext() )
//            {
//                Record ResourceRecord = resourceNode.next();
//                int resourceID = ResourceRecord.get( "id" ).asInt();
//                Resource resource = resourceMapper.queryResourceByID(resourceID);
//                resourceArray.add(resource);
//            }
//            similarEntity.put("resources", resourceArray);
//
//        }
//        similarEntity.put("goalAndKey", goalAndKey(entityName));
//        resArray.add(similarEntity);
//        resObject.put("resources", resArray);
//        resObject.put("total", totalEntity);
//        resObject.put("pages", (int)Math.ceil(totalEntity * 1.0 / perPage));
//        session.close();
////        driver.close();
//        return ResultFactory.buildSuccessResult("查询成功", resObject);
//    }
    public Result getEntity(Map<String, Object> keywordMap) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String browseDate = formatter.format(date);
        String keyword = (String) keywordMap.get("keyword");
        if (keywordMap.containsKey("userId")){
            int userId = Integer.parseInt((String) keywordMap.get("userId"));
            recordMapper.addEntityRecord(userId,browseDate,keyword);
        }
        int sort = 0;  //0默认，1最热，2最新
        int type = 0;  //0全部
        if (keywordMap.containsKey("sort")){
            sort = Integer.parseInt((String) keywordMap.get("sort"));
        }
        if (keywordMap.containsKey("type")){
            type = Integer.parseInt((String) keywordMap.get("type"));
        }
        int page = Integer.parseInt( (String) keywordMap.get("page") );
        int perPage = Integer.parseInt( (String) keywordMap.get("perPage") );
        Driver driver = createDrive();
        Session session = driver.session();

        StatementResult result = session.run( "MATCH (a:concept) where a.name = {name} " +
                        "RETURN properties(a) AS props order by a.name",
                parameters( "name", keyword) );
        JSONObject resObject = new JSONObject();
        JSONArray resArray = new JSONArray();
        if (!result.hasNext()){
            return ResultFactory.buildFailResult("未查询到相关知识点");
        }
        int totalEntity = 0;
        Record record = result.next();
        String entityName = record.get( "props" ).get( "name" ).asString();
        JSONObject similarEntity = new JSONObject();
        similarEntity.put("entityName", entityName);
        similarEntity.put("properties", record.get( "props" ).asMap());
        int skip = (page-1)*perPage;
        if (sort == 0){
            StatementResult resourceNode = session.run( "MATCH (m:resource)-[r]->(a:concept) where a.name = {name} " +
                            "RETURN m.id AS id order by r.tfidf",
                    parameters( "name", keyword) );
            JSONArray resourceArray = new JSONArray();
            while ( resourceNode.hasNext() )
            {
                Record ResourceRecord = resourceNode.next();
                int resourceID = ResourceRecord.get( "id" ).asInt();
                Resource resource = resourceMapper.queryResourceByID(resourceID);
                int extendID = resource.getTableResourceID();
                int tableID = resource.getTable();
                switch (tableID) {
                    case 1:
                        Map bvideoInfo = resourceMapper.queryBvideo(extendID);
                        resource.setAid((String) bvideoInfo.get("aid"));
                        resource.setBvid((String) bvideoInfo.get("bvid"));
                        resource.setCid((String) bvideoInfo.get("cid"));
                        resource.setPage((int)bvideoInfo.get("page"));
                        break;
                    case 2:
                        Map documentInfo = resourceMapper.queryDocument(extendID);
                        resource.setUrl((String) documentInfo.get("url"));
                        resource.setViewUrl((String) documentInfo.get("view_url"));
                        break;
                }
                if (type == 0 || resource.getResourceType() == type){
                    resourceArray.add(resource);
                    totalEntity++;
                }
            }
            JSONArray resourceTotal = new JSONArray();
            for (int i = skip;i<skip+perPage && i<totalEntity;i++){
                resourceTotal.add(resourceArray.get(i));
            }
            similarEntity.put("resources", resourceTotal);
        }
        else if(sort == 1){
            StatementResult resourceNode = session.run( "MATCH (m:resource)-[r]->(a:concept) where a.name = {name} " +
                            "RETURN m.id AS id order by r.tfidf",
                    parameters( "name", keyword) );
            JSONArray resourceArray = new JSONArray();
            ArrayList<Integer> idList = new ArrayList<>();
            while ( resourceNode.hasNext() )
            {
                Record ResourceRecord = resourceNode.next();
                int resourceID = ResourceRecord.get( "id" ).asInt();
                idList.add(resourceID);
            }
            ArrayList<Resource> resourceArrayList = resourceMapper.queryResourceByIDList(idList);
            System.out.println(resourceArrayList);
        }
        else{
        }
        similarEntity.put("goalAndKey", goalAndKey(entityName));
        resArray.add(similarEntity);
        resObject.put("resources", resArray);
        resObject.put("total", totalEntity);
        resObject.put("pages", (int)Math.ceil(totalEntity * 1.0 / perPage));
        session.close();
//        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resObject);
    }
    //根据entity查资源
    public JSONArray queryResource(String entityName,int perPage,int page,int sort,int type) {
        String entity = entityName + '#';
        int start = (page-1) * perPage;
        int end = perPage;
        ArrayList<Resource> queryResource = resourceMapper.queryResource(entity, start, end, sort, type);
        JSONArray resourceList = new JSONArray();
        for (Resource perResource : queryResource){
//            perResource.setUrl(resourceRoot + perResource.getUrl());
//            perResource.setViewUrl(resourceRoot + perResource.getViewUrl());
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
//    public Result getEntity_neo4j(Map<String, Object> keywordMap) {
//        String keyword = (String) keywordMap.get("keyword");
//        int page = Integer.parseInt( (String) keywordMap.get("page") );
//        int perPage = Integer.parseInt( (String) keywordMap.get("perPage") );
//        int skip = (page - 1) * perPage;
//        Driver driver = createDrive();
//        Session session = driver.session();
//        StatementResult result = session.run( "MATCH (a:concept) where a.name =~ {name} " +
//                        "RETURN properties(a) AS props order by a.name skip {skip} limit {limit}",
//                parameters( "name", ".*" + keyword + ".*", "skip", skip, "limit", perPage ) );
//        JSONObject resObject = new JSONObject();
//        JSONArray resArray = new JSONArray();
//        if (!result.hasNext()){
//            return ResultFactory.buildFailResult("未查询到相关知识点");
//        }
//        int totalEntity = 0;
//        while ( result.hasNext() )
//        {
//            totalEntity ++;
//            Record record = result.next();
//            String entityName = record.get( "props" ).get( "name" ).asString();
//            JSONObject similarEntity = new JSONObject();
//            similarEntity.put("entityName", entityName);
//            similarEntity.put("properties", record.get( "props" ).asMap());
//            similarEntity.put("resources", queryResource(entityName));
//            similarEntity.put("goalAndKey", goalAndKey_neo4j(entityName));
//            resArray.add(similarEntity);
//        }
//        resObject.put("resources", resArray);
//        resObject.put("total", totalEntity);
//        resObject.put("pages", (int)Math.ceil(totalEntity * 1.0 / perPage));
//        session.close();
////        driver.close();
//        return ResultFactory.buildSuccessResult("查询成功", resObject);
//    }
//    //根据entity查找重难点,从neo4j里面查
//    public JSONObject goalAndKey_neo4j(String entityName){
//        JSONObject resArray = new JSONObject();
//        Driver driver = createDrive();
//        Session session = driver.session();
//        StatementResult keyResult = session.run( "MATCH (a:concept)-[k:key]->(m) where a.name = {name} " +
//                        "RETURN m.content AS content",
//                parameters( "name", entityName) );
//        JSONArray keyArray = new JSONArray();
//        while ( keyResult.hasNext() )
//        {
//            Record record = keyResult.next();
//            String key = record.get("content").asString();
//            keyArray.add(key);
//        }
//        resArray.put("key", keyArray);
//        StatementResult goalResult = session.run( "MATCH (a:concept)-[k:goal]->(m) where a.name = {name} " +
//                        "RETURN m.content AS content",
//                parameters( "name", entityName) );
//        JSONArray goalArray = new JSONArray();
//        while ( goalResult.hasNext() )
//        {
//            Record record = goalResult.next();
//            String goal = record.get("content").asString();
//            goalArray.add(goal);
//        }
//        resArray.put("goal", goalArray);
//        return resArray;
//    }
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
