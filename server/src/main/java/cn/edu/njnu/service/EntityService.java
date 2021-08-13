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
    String resourceRoot = "http://222.192.6.62:8082";
    private Driver createDrive(){
        return GraphDatabase.driver( "bolt://222.192.6.62:7687", AuthTokens.basic( "neo4j", "123456" ) );
    }
    public JSONArray getRelatedEntity(String entityName, Session session, String mainEntityName){
        StatementResult result = session.run( "MATCH (a:concept) -[k:相关关系]-> (m:concept) where a.name = { name } and m.name<>{mainEntity}" +
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

    public Result queryEntity(Map<String, Object> keywordMap) {
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
                        "RETURN properties(a) AS props",
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
        StatementResult resourceNode = session.run( "MATCH (m:resource)-[r]->(a:concept) where a.name = {name} " +
                        "RETURN m.id AS id order by r.tfidf",
                parameters( "name", keyword) );
        ArrayList<Integer> idList = new ArrayList<>();
        while ( resourceNode.hasNext() )
        {
            Record ResourceRecord = resourceNode.next();
            int resourceID = ResourceRecord.get( "id" ).asInt();
            idList.add(resourceID);
        }
        ArrayList<Resource> resourceArrayList = new ArrayList<Resource>();
        if (sort == 0){
            for (int resourceID:idList){
                Resource resource = resourceMapper.queryResourceByID(resourceID);
                if (resource.getResourceType()==type || type==0){
                    resourceArrayList.add(resource);
                }

            }
        }
        else {
            resourceArrayList = resourceMapper.queryResourceByIDList(idList,sort,type);
        }

        for (Resource resource:resourceArrayList){
            int resourceID = resource.getId();
            StatementResult conceptNode = session.run( "MATCH (m:resource)-[r]->(a:concept) where m.id = {id} " +
                            "RETURN a.name AS name order by r.tfidf",
                    parameters( "id", resourceID) );
            ArrayList<String> entityList = new ArrayList<>();
            while ( conceptNode.hasNext() )
            {
                Record entityRecord = conceptNode.next();
                String name = entityRecord.get( "name" ).asString();
                entityList.add(name);
            }
            resource.setEntityList(entityList);
            totalEntity++;
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
        }
        JSONArray resourceTotal = new JSONArray();
        for (int i = skip;i<skip+perPage && i<totalEntity;i++){
            resourceTotal.add(resourceArrayList.get(i));
        }
        similarEntity.put("resources", resourceTotal);
        similarEntity.put("goalAndKey", goalAndKey(entityName));
        resArray.add(similarEntity);
        resObject.put("resources", resArray);
        resObject.put("total", totalEntity);
        resObject.put("pages", (int)Math.ceil(totalEntity * 1.0 / perPage));
        session.close();
//        driver.close();
        return ResultFactory.buildSuccessResult("查询成功", resObject);
    }
    //根据entity查找重难点,从mysql里面查
    public JSONArray goalAndKey(String entityName){
        JSONArray resArray = new JSONArray();
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult goalNode = session.run( "MATCH (m:GoalAndKey)-[r]->(a:concept) where a.name = {name} " +
                        "RETURN m.key, m.goal, m.id",
                parameters( "name", entityName) );
        while ( goalNode.hasNext() )
        {
            Record goalRecord = goalNode.next();
            String objectives = goalRecord.get( "m.goal" ).asString();
            String key = goalRecord.get( "m.key" ).asString();
            int id = goalRecord.get("m.id").asInt();
            JSONObject singleGK = new JSONObject();
            singleGK.put("objectives", objectives);
            singleGK.put("key", key);
            singleGK.put("resourceID", id);
            resArray.add(singleGK);
        }
        return resArray;
    }
    //根据用户浏览记录生成图谱
    public Result userGraph(int userID){
        Driver driver = createDrive();
        Session session = driver.session();
        HashMap<String, Integer> entityNumMap = new HashMap<>();
        HashMap<String, Integer> subjectMap = new HashMap<>();
        ArrayList<Map<String, Object>> recordMap = recordMapper.record(userID);
        String neo4JMatch = "MATCH (m:resource)-[]->(n:concept) where ";
        for (Map singleRecord:recordMap){
            if (singleRecord.containsKey("entity_name")){
                String entityName = (String) singleRecord.get("entity_name");
                if (entityNumMap.containsKey(entityName)){
                    int currentNum = entityNumMap.get(entityName);
                    entityNumMap.put(entityName, currentNum + 1);
                }
                else {
                    entityNumMap.put(entityName, 1);
                }
            }

            if (singleRecord.containsKey("resource_id")){
                int resourceID = (int)singleRecord.get("resource_id");
                neo4JMatch += "m.id =" + resourceID + " or ";
            }
        }
        neo4JMatch = neo4JMatch.substring(0, neo4JMatch.length()-4);
        neo4JMatch += " RETURN n.name as name, n.学科 as subject";
        StatementResult resEntity = session.run(neo4JMatch);
        while ( resEntity.hasNext() )
        {
            Record nodeRecord = resEntity.next();
            String entityName = nodeRecord.get( "name" ).asString();
            String subject = nodeRecord.get( "subject" ).asString();
            if (!subjectMap.containsKey(subject)){
                subjectMap.put(subject, 1);
            }
            if (entityNumMap.containsKey(entityName)){
                int currentNum = entityNumMap.get(entityName);
                entityNumMap.put(entityName, currentNum + 1);
            }
            else {
                entityNumMap.put(entityName, 1);
            }
        }
        //System.out.println(entityNumMap);
        //System.out.println(subjectMap);
        String sql =  "MATCH (n:concept) where (";
        for (String key : entityNumMap.keySet()) {
            sql += "n.name = \'" + key + "\'" + " or ";
        }
        sql = sql.substring(0, sql.length() - 4);
        JSONArray resArray = new JSONArray();
        for (String subjectKey : subjectMap.keySet()) {
            JSONObject subjectObject = new JSONObject();
            subjectObject.put("subject", subjectKey);
            String subjectSql = sql + ") and n.学科 = \'" + subjectKey + "\'" + " RETURN n.name as name";
            StatementResult subjectNode = session.run(subjectSql);
            JSONArray node = new JSONArray();
            while ( subjectNode.hasNext() )
            {
                JSONObject nodeInfo = new JSONObject();
                Record subjectNodeRecord = subjectNode.next();
                String entityName = subjectNodeRecord.get( "name" ).asString();
                nodeInfo.put("entityName", entityName);
                ArrayList<String> connectNode = new ArrayList<>();
                ArrayList<String> disconnect = new ArrayList<>();
                int disconnectNum = 0;
                StatementResult relatedNode = session.run("MATCH (n:concept)-[]->(m:concept) where n.name = {nodeName}" +
                        "RETURN m.name as name",
                        parameters("nodeName", entityName));
                while ( relatedNode.hasNext() )
                {
                    Record relatedNodeRecord = relatedNode.next();
                    String relatedEntityName = relatedNodeRecord.get("name").asString();
                    if (entityNumMap.containsKey(relatedEntityName)){
                        connectNode.add(relatedEntityName);
                    }
                    else {
                        if (disconnectNum < 4){ //防止disconnect太多了
                            disconnect.add(relatedEntityName);
                            disconnectNum++;
                        }
                    }
                }

                nodeInfo.put("connect", connectNode);
                nodeInfo.put("disconnect", disconnect);
                node.add(nodeInfo);
            }
            subjectObject.put("node", node);
            resArray.add(subjectObject);
        }
        return ResultFactory.buildSuccessResult("success", resArray);
    }
}
