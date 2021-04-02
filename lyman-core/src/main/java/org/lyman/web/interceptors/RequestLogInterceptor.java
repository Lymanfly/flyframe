package org.lyman.web.interceptors;

import lombok.extern.slf4j.Slf4j;
import org.lyman.utils.JsonUtils;
import org.lyman.utils.WebUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Slf4j(topic = "REQUEST LOG")
public class RequestLogInterceptor implements HandlerInterceptor {

    private static final long BAD_REQUEST_TIME = 1000L;

    private static final ThreadLocal<Long> TIMER = new ThreadLocal<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        long timestamp = System.currentTimeMillis();
        TIMER.set(timestamp);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        long cost = System.currentTimeMillis() - TIMER.get();
        String url = WebUtils.getRelativeUrl(request);
        Map<String, String> args = WebUtils.getRequestArgs(request);
        String ip = WebUtils.getClientAddress(request);
        String browser = WebUtils.getUserBrowser(request);
        String content = String.format("%s | %s | %s | %s | %dms", url, JsonUtils.toJsonString(args), ip, browser, cost);
        if (cost > BAD_REQUEST_TIME)
            log.warn(content);
        else
            log.info(content);
    }
}
