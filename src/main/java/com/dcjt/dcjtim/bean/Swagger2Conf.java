package com.dcjt.dcjtim.bean;

import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author 滔哥
 * @since 2020/7/28
 */
@Configuration
@EnableSwagger2
public class Swagger2Conf {

    public static final String SWAGGER_SCAN_BASE_PACKAGE = "com.dcjt.dcjtim";
    public static final String VERSION = "1.0.0";

    /**
     * 配置swagger2核心配置 docket
     * @return
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)// 指定api类型为swagger2
                .apiInfo(apiInfo())// 接口文档的基本信息
                .select()
                // 方法需要有ApiOperation注解才能生存接口文档
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .apis(RequestHandlerSelectors.basePackage(SWAGGER_SCAN_BASE_PACKAGE))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                // 如何保护我们的Api，有三种验证（ApiKey, BasicAuth, OAuth）
                //.securitySchemes(security())
                .build();
    }

    /**
     * 接口文档详细信息
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API接口文档") //设置文档的标题
                .description("API 接口文档") // 设置文档的描述
                .contact(new Contact("zxw","https://www.zxw.com","abc@imooc.com"))// 联系信息
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                //.termsOfServiceUrl("http://www.baidu.com") // 网站地址
                .build();
    }

    private List<ApiKey> security() {
        ArrayList<ApiKey> apiKeys = new ArrayList<>();
        apiKeys.add(new ApiKey("token", "token", "header"));
        return apiKeys;
    }
}
