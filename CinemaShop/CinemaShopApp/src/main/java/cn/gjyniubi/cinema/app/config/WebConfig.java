package cn.gjyniubi.cinema.app.config;

import cn.gjyniubi.cinema.app.interceptor.AppAuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author gujianyang
 * @Date 2021/5/14
 * @Class WebConfig
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AppAuthInterceptor appAuthInterceptor;

    @Value("${project.file.save-path}")
    private String rootDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(appAuthInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/webjars/**","/swagger-ui.html",
                        "/v2/api-docs","/swagger-resources/**")
                .excludePathPatterns("/res/file/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/user/checkToken")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/util/**")
                .excludePathPatterns("/home/**")
                .excludePathPatterns("/cinema/position/**")
                .excludePathPatterns("/common/position/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/res/file/**")
                .addResourceLocations("file:"+rootDir+"/");
    }
}
