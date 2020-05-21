package kl.springboot.demo.component;

import kl.springboot.demo.utils.Internationalization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;

//可以扩展spring mvc
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    //添加视图映射
    //浏览器发送请求来到lello页面
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/").setViewName("login");
    }

    //注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration r = registry.addInterceptor(new LoginHandlerInterceptor());
        r.addPathPatterns("/**");
        r.excludePathPatterns("/login.html");//放开首页
        r.excludePathPatterns("/Login.do");//放开登录
        r.excludePathPatterns("/static/**");//放开静态资源
        //spring 2.x版本要加下边这句话，不然静态资源会被拦截
        r.excludePathPatterns("/webjars/**", "/css/**", "/js/**", "/img/**");
    }


    /**
     * 国际化
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new Internationalization();
    }


}
