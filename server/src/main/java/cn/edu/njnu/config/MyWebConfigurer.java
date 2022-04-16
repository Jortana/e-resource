package cn.edu.njnu.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class MyWebConfigurer implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有请求都允许跨域，使用这种配置方法就不能在 interceptor 中再配置 header 了
        registry.addMapping("/**")
                .allowCredentials(true)
//                .allowedOrigins("http://39.105.139.205")
                .allowedOrigins("http://127.0.0.1:8080")
//                .allowedOrigins("http://202.102.89.244:8010")

                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/e-resource/api/file/avatar/**").addResourceLocations("file:" + "d:/e-resource/avatar/");
        //registry.addResourceHandler("/e-resource/api/file/resource/**").addResourceLocations("file:" + "d:/e-resource/resource");
    }
}
