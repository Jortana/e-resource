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
import org.neo4j.driver.v1.*;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.neo4j.driver.v1.Values.isoDuration;
import static org.neo4j.driver.v1.Values.parameters;

@Service
public class ResourceService {

    private final ResourceMapper resourceMapper;
    String resourceRoot = "http://223.2.50.241:8082";
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
        resultData.put("pages", (int)Math.ceil(total*1.0/resourceList.size()));
        resultData.put("total", total);
        JSONArray resources = new JSONArray();
        for (Resource perResource : resourceList){
            perResource.setEntityList(perResource.getEntity().split("#"));
//            perResource.setUrl(resourceRoot + perResource.getUrl());
//            perResource.setViewUrl(resourceRoot + perResource.getViewUrl());
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
    private static Driver createDrive(){
        return GraphDatabase.driver( "bolt://223.2.50.241:7687", AuthTokens.basic( "neo4j", "123456" ) );
    }
    //更新资源相似度
    public Result relatedResource(){
        Driver driver = createDrive();
        Session session = driver.session();
        StatementResult result = session.run( "MATCH (n:resource) where n.subject='化学' " +
                        "RETURN n.id AS ID order by ID",
                parameters() );
        while ( result.hasNext() )
        {
            Record record = result.next();
            int resourceID = record.get( "ID" ).asInt();
            HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
            StatementResult tfidf = session.run( "MATCH (n:resource)-[r]->(m:concept) where n.id={id} " +
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
            StatementResult otherResource = session.run( "MATCH (n:resource) where n.subject='化学' and n.id<>{id}" +
                            "RETURN n.id AS ID order by ID",
                    parameters("id", resourceID) );
            while ( otherResource.hasNext() )
            {
                Record other = otherResource.next();
                int otherID = other.get( "ID" ).asInt();
                StatementResult otherTFIDF = session.run( "MATCH (n:resource)-[r]->(m:concept) where n.id={id} " +
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
                        "WHERE a.id = " + nu + " AND b.id = " + num
                        + " CREATE (a)-[:similarity{weight:" + xsd + "}]->(b)");
                session.run("MATCH (a:resource), (b:resource) " +
                        "WHERE a.id = " + nu + " AND b.id = " + num
                        + " CREATE (b)-[:similarity{weight:" + xsd + "}]->(a)");
            }
        }
        session.close();
        driver.close();
    }

    public Result recommendResource(Map<String, Object> IDMap){
        int userID = Integer.parseInt((String) IDMap.get("userId"));
        int resourceID = Integer.parseInt((String) IDMap.get("resourceID"));
        Driver driver = createDrive();
        Session session = driver.session();

        StatementResult resourceWeight = session.run( "MATCH (n:resource)-[r]->(m:resource) where n.id={id} " +
                        "RETURN m.id, r.weight order by r.weight desc limit 10",
                parameters("id", resourceID) );
        HashMap<Integer, Double> resourceMap = new HashMap<>();
        while ( resourceWeight.hasNext() ) {
            Record userRecord = resourceWeight.next();
            int resID = userRecord.get("m.id").asInt();
            double userResourceWeight = userRecord.get("r.weight").asDouble();
            resourceMap.put(resID, userResourceWeight);
        }
        StatementResult userWeight = session.run( "MATCH (n:user)-[r]->(m:resource) where n.id={id} " +
                        "RETURN m.id, r.weight order by r.weight desc limit 10",
                parameters("id", userID) );
        JSONArray recommendResource = new JSONArray();
        HashMap<Integer, Double> userResourceMap = new HashMap<>();
        while ( userWeight.hasNext() ) {
            Record userRecord = userWeight.next();
            int resID = userRecord.get("m.id").asInt();
            double userResourceWeight = userRecord.get("r,weight").asDouble();
            if (resourceMap.containsKey(resID)){
                JSONObject resource = new JSONObject();
                resource.put("resourceID", resID);

                resource.put("weight", resourceMap.get(resID));
                recommendResource.add(resource);
                resourceMap.remove(resID);
            }
            else {
                resourceMap.put(resID, userResourceWeight);
            }
        }
        for (Integer key : resourceMap.keySet()) {
            if (!userResourceMap.containsKey(key)){
                userResourceMap.put(key, resourceMap.get(key));
            }
        }
        List<Map.Entry<Integer, Double>> list = new ArrayList<Map.Entry<Integer, Double>>(userResourceMap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer, Double>>() {
            //升序排序
            public int compare(Map.Entry<Integer, Double> o1,
                               Map.Entry<Integer, Double> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        for(Map.Entry<Integer, Double> mapping:list){
            JSONObject resource = new JSONObject();
            resource.put("resourceID", mapping.getKey());
            resource.put("weight", mapping.getValue());
            recommendResource.add(resource);
        }

        session.close();
//        driver.close();
        for(int i=0;i<recommendResource.size();i++) {
            int id = (int) recommendResource.getJSONObject(i).get("resourceID");
            double weight = (double) recommendResource.getJSONObject(i).get("weight");
            System.out.println("资源ID:" + id + "  对用户ID:" + userID + " 的推荐度为: " + weight);
        }
        return ResultFactory.buildSuccessResult("Success", recommendResource);
    }
}
