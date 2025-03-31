package kitra.easyweibo.configuration;

import kitra.easyweibo.annotation.PostRestController;
import kitra.easyweibo.annotation.UserRestController;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfiguration implements WebMvcConfigurer {
    /**
     * 处理自定义RestController注解的路径前缀
     */
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/user", c -> c.isAnnotationPresent(UserRestController.class));
        configurer.addPathPrefix("/post", c -> c.isAnnotationPresent(PostRestController.class));
    }
}
