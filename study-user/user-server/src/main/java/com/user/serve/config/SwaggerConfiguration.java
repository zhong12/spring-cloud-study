package com.user.serve.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zj
 * @Date: 2021/4/14 17:30
 * @Description:  Swagger配置
 * @Version: 1.0
 */
@Component
public class SwaggerConfiguration {

    @Value("${swagger.enable}")
    private boolean enableSwagger;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.user.serve.controller"))
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
//                .securitySchemes(securitySchemes())
                .enable(enableSwagger)
                ;
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("swagger和springBoot整合")
                .description("swagger的API文档")
                .version("1.0")
                .termsOfServiceUrl("更多请关注http://www.baidu.com")
                .build();
    }

//    @Override
//    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // 解决静态资源无法访问
//        registry.addResourceHandler("/**")
//                .addResourceLocations("classpath:/static/");
//        // 解决swagger无法访问
//        registry.addResourceHandler("/swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        // 解决swagger的js文件无法访问
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }


    /**
     * SecurityScheme 子类 BasicAuth OAuth ApiKey
     *
     * @return
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> list = new ArrayList<>();
        // basicAuth SwaggerBootstrapUI支持的不好,使用swagger原生UI
        list.add(new BasicAuth("basicAuth"));
        // name 为参数名  keyname是页面传值显示的 keyname， name在swagger鉴权中使用
        list.add(new ApiKey("access_token", "access_token", "header"));
        list.add(new ApiKey("query_token鉴权值-参数名称", "query_token", "query"));
        return list;
    }
}