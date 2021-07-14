package cn.edu.njnu.service;

import cn.edu.njnu.mapper.ResourceMapper;
import cn.edu.njnu.mapper.UserMapper;
import cn.edu.njnu.pojo.Resource;
import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import cn.edu.njnu.pojo.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.neo4j.driver.v1.*;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.neo4j.driver.v1.Values.parameters;

@Service
public class ResourceService {

    private final ResourceMapper resourceMapper;
    private final UserMapper userMapper;
    String resourceRoot = "http://222.192.6.62:8082";
    public ResourceService(ResourceMapper resourceMapper, UserMapper userMapper) {
        this.resourceMapper = resourceMapper;
        this.userMapper = userMapper;
    }
    private static Driver createDrive(){
        return GraphDatabase.driver( "bolt://222.192.6.62:7687", AuthTokens.basic( "neo4j", "123456" ) );
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

    //查相关资源
    public Result queryRelated(Map<String, Object> resourceIDMap){
        int resourceID = Integer.parseInt((String)resourceIDMap.get("resourceID"));
//        String related_10 = resourceMapper.queryRelated(resourceID);
//        if (related_10 == null){
//            return ResultFactory.buildFailResult("未查询到相关资源");
//        }
//        JSONObject resObject = new JSONObject();
//        JSONArray resourcesList = new JSONArray();
//        String[] relatedID = related_10.split("#");
//        for (String perResourceID : relatedID){
//            int id = Integer.parseInt(perResourceID);
//            Resource singleResource = resourceMapper.queryResourceByID(id);
//            resourcesList.add(singleResource);
//        }
//        return ResultFactory.buildSuccessResult("查询成功", resourcesList);
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 1;i<11;i++){
            idList.add(resourceID+i);
        }
        ArrayList<Resource> resList = resourceMapper.queryResourceByIDList(idList,0,0);
        return ResultFactory.buildSuccessResult("查询成功",resList);
    }
    //根据ID查资源属性
    public Result queryResource(Map<String, Object> ResourceIDMap){
        Driver driver = createDrive();
        Session session = driver.session();
        int resourceID = Integer.parseInt ((String) ResourceIDMap.get("resourceID"));
        Resource queryResource = resourceMapper.queryResourceByID(resourceID);
        StatementResult conceptNode = session.run( "MATCH (m:resource)-[r]->(a:concept) where m.id = {id} " +
                        "RETURN a.name as concept",
                parameters( "id", resourceID) );
        ArrayList<String> entityList = new ArrayList<>();
        while ( conceptNode.hasNext() ) {
            Record conceptRecord = conceptNode.next();
            String entityName = conceptRecord.get("concept").asString();
            entityList.add(entityName);
        }
        queryResource.setEntityList(entityList);
        int extendID = queryResource.getTableResourceID();
        int tableID = queryResource.getTable();
        switch (tableID) {
            case 1:
                Map bvideoInfo = resourceMapper.queryBvideo(extendID);
                queryResource.setAid((String) bvideoInfo.get("aid"));
                queryResource.setBvid((String) bvideoInfo.get("bvid"));
                queryResource.setCid((String) bvideoInfo.get("cid"));
                queryResource.setPage((int)bvideoInfo.get("page"));
                break;
            case 2:
                Map documentInfo = resourceMapper.queryDocument(extendID);
                queryResource.setUrl((String) documentInfo.get("url"));
                queryResource.setViewUrl((String) documentInfo.get("view_url"));
                break;
        }
//        resourceMapper.updateBrowse(queryResource.getBrowse() + 1,queryResource.getId());
        return ResultFactory.buildSuccessResult("查询成功",queryResource);
    }

    // private static Driver createDrive(){
    //     return GraphDatabase.driver( "bolt://222.192.6.62:7687", AuthTokens.basic( "neo4j", "123456" ) );
    // }

    //更新资源相似度
    public Result updateRelatedResource(){
        Driver driver = createDrive();
        Session session = driver.session();
//        StatementResult result = session.run( "MATCH (n:resource) where n.subject='化学' " +
//                        "RETURN n.id AS ID order by ID",
//                parameters() );
        StatementResult result = session.run( "MATCH (n:resource)" +
                        "RETURN id(n) AS ID order by ID",
                parameters() );
        while ( result.hasNext() )
        {
            Record record = result.next();
            int resourceID = record.get( "ID" ).asInt();
            HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
            StatementResult tfidf = session.run( "MATCH (n:resource)-[r]->(m:concept) where id(n)={id} " +
                        "RETURN m.name, r.tf",
                parameters("id", resourceID) );
            while ( tfidf.hasNext() )
            {
                Record tfidfRecord = tfidf.next();
                String word = tfidfRecord.get("m.name").asString();
                int tf = tfidfRecord.get("r.tf").asInt();
                hm1.put(word, tf);
            }
            HashMap<Integer, HashMap<String, Integer>> map = new HashMap<Integer, HashMap<String, Integer>>();
            map.put(resourceID,hm1);
//            StatementResult otherResource = session.run( "MATCH (n:resource) where n.subject='化学' and n.id<>{id}" +
//                            "RETURN n.id AS ID order by ID",
//                    parameters("id", resourceID) );
            StatementResult otherResource = session.run( "MATCH (n:resource) where id(n)<>{id}" +
                            "RETURN id(n) AS ID order by ID",
                    parameters("id", resourceID) );
            while ( otherResource.hasNext() )
            {
                Record other = otherResource.next();
                int otherID = other.get( "ID" ).asInt();
                StatementResult otherTFIDF = session.run( "MATCH (n:resource)-[r]->(m:concept) where id(n)={id} " +
                                "RETURN m.name, r.tf",
                        parameters("id", otherID) );
                HashMap<String, Integer> hm2 = new HashMap<String, Integer>();
                while ( otherTFIDF.hasNext() )
                {
                    Record tfidfRecord = otherTFIDF.next();
                    String word = tfidfRecord.get("m.name").asString();
                    int tf = tfidfRecord.get("r.tf").asInt();
                    hm2.put(word, tf);
                }
                HashMap<Integer, HashMap<String, Integer>> map1= new HashMap<Integer, HashMap<String, Integer>>();
                map1.put(otherID,hm2);
                resxsd( map, map1);
            }
        }
        return ResultFactory.buildSuccessResult("资源相似度更新成功", null);
    }

    public static void resxsd(HashMap<Integer, HashMap<String, Integer>> keywords, HashMap<Integer, HashMap<String, Integer>> keywords1) {  //读取与Resid在同一个知识点下面的资源以及与该知识点直接相连的知识下的资源
        Driver driver = createDrive();
        Session session = driver.session();
        for (Map.Entry<Integer, HashMap<String, Integer>> entrytemp : keywords.entrySet()) {
            // nu = entrytemp.getKey();
            System.out.println("待测资源"+keywords);}
        for (Map.Entry<Integer, HashMap<String, Integer>> entry : keywords1.entrySet()) {//遍历每一个相关资源
            System.out.println("相关资源"+entry);
            int num=entry.getKey();
            int nu=0;
            HashMap<String, Integer> s = entry.getValue();
            double fenmu1 = 0.0;
            double fenmu2 = 0.0;
            double fenzi = 0.0;
            int flag=1;
            for (HashMap.Entry<String, Integer> entry1 : s.entrySet()) {   //获取相关资源的关键词和tfidf值
                fenmu1 += Math.pow(entry1.getValue(), 2);
                for (Map.Entry<Integer, HashMap<String, Integer>> entrytemp : keywords.entrySet()) {
                    nu = entrytemp.getKey();
                    // System.out.println("待测资源"+keywords);
                    HashMap<String, Integer> st = entrytemp.getValue();
                    for (HashMap.Entry<String, Integer> entryt : st.entrySet()) {  //遍历待测资源
                        if (flag == 1) {
                            fenmu2 += Math.pow(entryt.getValue(), 2);
                        }
                        if (entryt.getKey().equals(entry1.getKey())) {
                            System.out.println("相同关键词："+entryt.getKey()+"---"+entry1.getKey());
                            fenzi += entryt.getValue() * entry1.getValue();
                        }
                    }
                    flag = 0;
                }
            }
            System.out.println(fenmu1 + "  " + fenmu2 + " " + fenzi);
            double result = fenzi / (Math.sqrt(fenmu1) * Math.sqrt(fenmu2));
            double xsd= 0.5*result+0.5;
            System.out.println(nu + "  " + num + "的相似度为" +xsd);
            if (num>nu){
                session.run("MATCH (a:resource), (b:resource) " +
                        "WHERE id(a) = " + nu + " AND id(b) = " + num
                        + " CREATE (a)-[:similarity{weight:" + xsd + "}]->(b)");
                session.run("MATCH (a:resource), (b:resource) " +
                        "WHERE id(a) = " + nu + " AND id(b) = " + num
                        + " CREATE (b)-[:similarity{weight:" + xsd + "}]->(a)");
            }
        }
        session.close();
        driver.close();
    }

    public Result recommendResource(Map<String, Object> IDMap){
        int resourceID = 29947;
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 1;i<9;i+=2){
            idList.add(resourceID+i);
        }
        ArrayList<Resource> resList = resourceMapper.queryResourceByIDList(idList,0,0);
        resourceID = 38211;
        ArrayList<Integer> idList1 = new ArrayList<>();
        for (int i = 1;i<9;i+=2){
            idList1.add(resourceID+i);
        }
        ArrayList<Resource> resList1 = resourceMapper.queryResourceByIDList(idList1,0,0);
        for (Resource resource:resList1){
            resList.add(resource);
        }
        return ResultFactory.buildSuccessResult("查询成功",resList);
//        Driver driver = createDrive();
//        Session session = driver.session();
//        int userID = Integer.parseInt((String) IDMap.get("userId"));
//        if (IDMap.containsKey("entity")){
//            String entity = (String) IDMap.get("entity");
//            StatementResult resourceID = session.run( "MATCH (n:user)-[r]->(m:resource) where n.id={id} " +
//                            "RETURN m.id as id order by r.weight desc",
//                    parameters("id", userID) );
//            int resNum = 0;
//            JSONArray resArray = new JSONArray();
//            while ( resourceID.hasNext() && resNum <=10) {
//                Record userRecord = resourceID.next();
//                int resID = userRecord.get("id").asInt();
//                Resource resource = resourceMapper.queryResourceByID(resID);
////                if (resource.getEntity().contains(entity)){
//                resArray.add(resource);
//                resNum++;
////                }
//            }
//            if (resNum!=0){
//                return ResultFactory.buildSuccessResult("查询成功",resArray);
//            }
//            else {
//                return ResultFactory.buildSuccessResult("未查询到任何推荐资源",resArray);
//            }
//        }
//        else if (!IDMap.containsKey("entity")&&!IDMap.containsKey("resourceID")){
//            StatementResult resourceID = session.run( "MATCH (n:user)-[r]->(m:resource) where n.id={id} and r.weight>0.5 " +
//                            "RETURN m.id as id order by r.weight desc",
//                    parameters("id", userID) );
//            int resNum = 0;
//            JSONArray resArray = new JSONArray();
//            while ( resourceID.hasNext() && resNum <=10) {
//                Record userRecord = resourceID.next();
//                int resID = userRecord.get("id").asInt();
//                Resource resource = resourceMapper.queryResourceByID(resID);
//                resArray.add(resource);
//                resNum++;
//            }
//            if (resNum!=0) {
//                return ResultFactory.buildSuccessResult("查询成功", resArray);
//            }
//            else {
//                return ResultFactory.buildSuccessResult("未查询到任何推荐资源",resArray);
//            }
//        }
//        else {
//            int resourceID = Integer.parseInt((String) IDMap.get("resourceID"));
//            StatementResult resourceWeight = session.run( "MATCH (n:resource)-[r]->(m:resource) where n.id={id} " +
//                            "RETURN m.id, r.weight order by r.weight desc limit 10",
//                    parameters("id", resourceID) );
//            HashMap<Integer, Double> resourceMap = new HashMap<>();
//            while ( resourceWeight.hasNext() ) {
//                Record userRecord = resourceWeight.next();
//                int resID = userRecord.get("m.id").asInt();
//                double userResourceWeight = userRecord.get("r.weight").asDouble();
//                resourceMap.put(resID, userResourceWeight);
//            }
//            StatementResult userWeight = session.run( "MATCH (n:user)-[r]->(m:resource) where n.id={id} " +
//                            "RETURN m.id, r.weight order by r.weight desc limit 10",
//                    parameters("id", userID) );
//            JSONArray recommendResource = new JSONArray();
//            HashMap<Integer, Double> userResourceMap = new HashMap<>();
//            while ( userWeight.hasNext() ) {
//                Record userRecord = userWeight.next();
//                int resID = userRecord.get("m.id").asInt();
////                double userResourceWeight = userRecord.get("r.weight").asDouble();
//                double userResourceWeight = 0.6; //临时搞的
//                if (resourceMap.containsKey(resID)){
//                    recommendResource.add(resourceMapper.queryResourceByID(resID));
//                    resourceMap.remove(resID);
//                }
//                else {
//                    resourceMap.put(resID, userResourceWeight);
//                }
//            }
//            for (Integer key : resourceMap.keySet()) {
//                if (!userResourceMap.containsKey(key)){
//                    userResourceMap.put(key, resourceMap.get(key));
//                }
//            }
//            List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(userResourceMap.entrySet());
//            Collections.sort(list,new Comparator<Map.Entry<Integer, Double>>() {
//                //升序排序
//                public int compare(Map.Entry<Integer, Double> o1,
//                                   Map.Entry<Integer, Double> o2) {
//                    return o2.getValue().compareTo(o1.getValue());
//                }
//            });
//            for(Map.Entry<Integer, Double> mapping:list){
//                recommendResource.add(resourceMapper.queryResourceByID(mapping.getKey()));
//            }
//
//            session.close();
////        driver.close();
//            return ResultFactory.buildSuccessResult("Success", recommendResource);
//        }
    }

    //获取相似资源
    public Result relatedResource(Map<String, Object> resourceIDMap){
        int resourceID = Integer.parseInt((String)resourceIDMap.get("resourceID"));
//        Driver driver = createDrive();
//        Session session = driver.session();
//        StatementResult resourceNode = session.run( "MATCH (n:resource)-[r]->(m:resource) where r.weight>0.5 and n.id = {resourceID} " +
//                        "RETURN m.id AS id order by r.weight",
//                parameters("resourceID", resourceID) );
//        int userID = 0;
//        if (resourceIDMap.containsKey("userId")){
//            userID = Integer.parseInt((String)resourceIDMap.get("userId"));
//        }
//        JSONArray resourceArray = new JSONArray();
//        int resourceNum = 0;
//        while ( resourceNode.hasNext() && resourceNum < 10)
//        {
//            Record resourceRecord = resourceNode.next();
//            int id = resourceRecord.get("id").asInt();
//            Resource resource = resourceMapper.queryResourceByID(id);
//            System.out.println(resource);
//            if (userID==0){
//                resourceArray.add(resource);
//                resourceNum++;
//            }
//            else {
//                User user = userMapper.queryUserByID(userID);
//                System.out.println(user);
//                if (Integer.parseInt(resource.getPeriod()) == user.getPeriod()){
//                    resourceArray.add(resource);
//                    resourceNum++;
//                }
//            }
//        }
//        return ResultFactory.buildSuccessResult("查询成功",resourceArray);
        ArrayList<Integer> idList = new ArrayList<>();
        for (int i = 1;i<11;i++){
            idList.add(resourceID+i);
        }
        ArrayList<Resource> resList = resourceMapper.queryResourceByIDList(idList,0,0);
        return ResultFactory.buildSuccessResult("查询成功",resList);
    }

    public Result queryHot(){
        ArrayList<Resource> resourceList = resourceMapper.queryHot();
        JSONArray resArray = new JSONArray();
        for (Resource resource:resourceList){
            resArray.add(resource);
        }
        return ResultFactory.buildSuccessResult("查询成功",resArray);
    }

    public Result queryTime(){
        ArrayList<Resource> resourceList = resourceMapper.queryTime();
        JSONArray resArray = new JSONArray();
        for (Resource resource:resourceList){
            resArray.add(resource);
        }
        return ResultFactory.buildSuccessResult("查询成功",resArray);
    }
}
