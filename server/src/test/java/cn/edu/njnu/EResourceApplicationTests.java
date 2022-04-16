package cn.edu.njnu;


import cn.edu.njnu.mapper.Neo4jUtil;
import cn.edu.njnu.mapper.UserMapper;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.internal.value.MapValue;
import org.neo4j.driver.internal.value.StringValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;


@SpringBootTest
class EResourceApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Autowired
    private Neo4jUtil neo4jUtil;

    @Test
    void testNeo4j(){
        Map<String, Object> retMap = new HashMap<>();
        //cql语句
        String cql = "match (m:concept{name: \"欧洲\"}) return m";
        Set<Map<String, Object>> nodeList = new HashSet<>();
        neo4jUtil.getList(cql, nodeList);
        retMap.put("nodeList", nodeList);
        System.out.println(nodeList);
    }
}
