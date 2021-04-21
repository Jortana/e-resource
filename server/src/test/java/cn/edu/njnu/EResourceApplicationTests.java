package cn.edu.njnu;

import org.junit.jupiter.api.Test;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.neo4j.driver.v1.Values.parameters;


@SpringBootTest
class EResourceApplicationTests {
	@Autowired
	DataSource dataSource;

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
	private Driver createDrive(){
		return GraphDatabase.driver( "bolt://223.2.50.241:7687", AuthTokens.basic( "neo4j", "123456" ) );
	}
}
