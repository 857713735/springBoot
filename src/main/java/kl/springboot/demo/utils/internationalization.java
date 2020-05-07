package kl.springboot.demo.utils;

import org.springframework.web.servlet.LocaleResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 国际化
 */
public class internationalization implements LocaleResolver {
    /**
     * 解析
     *
     * @param httpServletRequest
     * @return
     */
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String yuyan = httpServletRequest.getParameter("l");//获取传递进来的参数
        Locale loca = Locale.getDefault();
        if (!StringUtils.isEmpty(yuyan)) {
            String[] spli = yuyan.split("_");
            loca = new Locale(spli[0], spli[1]);
        }
        return loca;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
