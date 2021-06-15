package cn.gjyniubi.cinema.common.config.swagger;

import cn.gjyniubi.cinema.common.contact.UserContact;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

/**
 * @Author gujianyang
 * @Date 2021/2/9
 * @Class SwaggerConfig
 * Swagger配置，从启动模块下的配置文件动态设定
 */
@ConditionalOnProperty("project.swagger.enabled")
@EnableSwagger2
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Value("${project.swagger.title}")
    private String title;

    @Value("${project.swagger.version}")
    private String version;

    @Value("${project.swagger.description}")
    private String description;

    @Value("${project.swagger.enabled}")
    private boolean enabled;

    @Value("${project.swagger.context-path}")
    private String apiRootPath;
    /**
     * 创建API
     */
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).pathMapping(apiRootPath)
                // 是否启用Swagger
                .enable(enabled)
                // 用来创建该API的基本信息，展示在文档的页面中（自定义展示的信息）
                .apiInfo(apiInfo())
                // 设置哪些接口暴露给Swagger展示
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList
                        (new ApiKey(UserContact.TOKEN_HEADER, UserContact.TOKEN_HEADER, UserContact.TOKEN_HEADER)))
                .securityContexts(Collections.singletonList(securityContexts()));
    }

    private SecurityContext securityContexts() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "描述信息");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(new SecurityReference(UserContact.TOKEN_HEADER, authorizationScopes));
    }


    private ApiInfo apiInfo() {
        // 用ApiInfoBuilder进行定制
        return new ApiInfoBuilder()
                // 设置标题
                .title(title)
                // 描述
                .description(description)
                .version(version)
                .build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(enabled){
            // 解决swagger无法访问
            registry.addResourceHandler("/swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            // 解决swagger的js文件无法访问
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }
}
