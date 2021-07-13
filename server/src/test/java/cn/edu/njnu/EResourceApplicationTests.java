package cn.edu.njnu;

import cn.edu.njnu.pojo.Result;
import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONObject;
import org.junit.jupiter.api.Test;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;


@SpringBootTest
class EResourceApplicationTests {
	@Autowired
	DataSource dataSource;

	@Test
	void testDate(){
		System.out.println(System.currentTimeMillis());
	}
	@Test
	void contextLoads() throws SQLException {
		System.out.println(dataSource.getClass());
		System.out.println(dataSource.getConnection());
	}

	@Test
	void neo4jTest(){
		Driver driver = createDrive();
		Session session = driver.session();
        StatementResult result = session.run( "MATCH (a:user) where a.id = {id} " +
                        "RETURN a.id",
                parameters( "id", 18) );
        if (result.hasNext()){
			System.out.println("yes");
		}
        else {
			System.out.println("no");
			session.run( "CREATE (a:user { id: {id} })",
					parameters( "id", 18) );
		}
		session.run("MATCH (a:user), (b:user) " +
				"WHERE a.id = " + 17 + " AND b.id = " + 18
				+ " CREATE (a)-[:" + 1 + "]->(b)");

	}
	private static Driver createDrive(){
		return GraphDatabase.driver( "bolt://223.2.50.241:7687", AuthTokens.basic( "neo4j", "123456" ) );
	}

	@Test
	public static void main(String[] args) {
		Driver driver = createDrive();
		Session session = driver.session();
		StatementResult result = session.run( "MATCH (n:resource)-[r]->(m:concept) where n.subject='化学' " +
						"RETURN  n.id as id,m.name as entityName,r.tf as tf",
				parameters() );
		while ( result.hasNext() )
		{
			Record record = result.next();
			int id = record.get( "id" ).asInt();
			HashMap<Integer, HashMap<String, Integer>> map = new HashMap<Integer, HashMap<String, Integer>>();
			HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
//			hm1.put("我", 1);
//			hm1.put("喜欢", 1);
//			hm1.put("小说", 1);
//			map.put(id,hm1);
//			StatementResult result_1 = session.run( "MATCH (n:resource)-[r]->(m:concept) where n.subject='化学' and n.id<>{id} " +
//							"RETURN  n.id as id,m.name as entityName,r.tf as tf",
//					parameters("id", id) );
			System.out.println(id);
		}
//		HashMap<Integer, HashMap<String, Integer>> map = new HashMap<Integer, HashMap<String, Integer>>();
//		HashMap<Integer, HashMap<String, Integer>> map1= new HashMap<Integer, HashMap<String, Integer>>();
//		HashMap<String, Integer> hm1 = new HashMap<String, Integer>();
//		HashMap<String, Integer> hm2 = new HashMap<String, Integer>();
//		HashMap<String, Integer> hm3 = new HashMap<String, Integer>();
//		hm1.put("我", 1);
//		hm1.put("喜欢", 1);
//		hm1.put("小说", 1);
//		hm2.put("我", 1);
//		hm2.put("喜欢",2);
//		hm2.put("电视", 1);
//		hm2.put("电影",1);
//		hm2.put("不",2);
//		map.put(1,hm1);
//		map1.put(2,hm2);
//		resxsd( map, map1);
	}
	@Test
	public void relatedResource(){
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
	}
	public static void resxsd(HashMap<Integer, HashMap<String, Integer>> keywords, HashMap<Integer, HashMap<String, Integer>> keywords1) {  //读取与Resid在同一个知识点下面的资源以及与该知识点直接相连的知识下的资源

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
		}


	}
}
