package cn.edu.njnu;

import cn.edu.njnu.pojo.ResultFactory;
import com.alibaba.fastjson.JSONObject;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;


@SpringBootTest
class EResourceApplicationTests {
    @Test
    void ansj(){
        String str = "甲烷的化学式" ;
        Result result = ToAnalysis.parse(str); //封装的分词结果对象，包含一个terms列表
//		 System.out.println(result);
        List<Term> terms = result.getTerms(); //term列表，元素就是拆分出来的词以及词性
//		 System.out.println(terms);
        for(Term term:terms){
            System.out.println(term.getName());		//分词的内容
            System.out.println(term.getNatureStr());	//分词的词性
        }
    }
}
