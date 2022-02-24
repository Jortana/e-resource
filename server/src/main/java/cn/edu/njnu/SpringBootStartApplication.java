package cn.edu.njnu;

import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.E;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class SpringBootStartApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){


        return builder.sources(EResourceApplication.class);


    }
}
