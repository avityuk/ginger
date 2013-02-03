package com.vityuk.ginger.spring;

import com.vityuk.ginger.LocaleResolver;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class SpringMvcLocaleResolver implements LocaleResolver {
    @Override
    public Locale getLocale() {
        HttpServletRequest request = getRequest();
        return RequestContextUtils.getLocale(request);
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes();
        return requestAttributes.getRequest();
    }
}
