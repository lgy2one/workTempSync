package com.qg.exclusiveplug.filter;

import com.google.gson.Gson;
import com.qg.exclusiveplug.constant.StatusEnum;
import com.qg.exclusiveplug.dtos.InteractionData;
import com.qg.exclusiveplug.dtos.ResponseData;
import com.qg.exclusiveplug.model.User;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@WebFilter(filterName = "actiondevice", urlPatterns = {"/actiondevice/*"})
public class ActionDeviceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        log.info("用户操作过滤器-->>初始化");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("用户操作过滤器");
        BodyReaderHttpServletRequestWrapper myRequestWrapper;

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;


        // 封装请求参数
        myRequestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);
        if (chain(myRequestWrapper)) {
            filterChain.doFilter(myRequestWrapper, servletResponse);
            return;
        }
        int index = new Gson().fromJson(myRequestWrapper.getReader(), InteractionData.class).getIndex();
        User user = (User) myRequestWrapper.getSession().getAttribute("user");

        ResponseData responseData = new ResponseData();
        String url = myRequestWrapper.getRequestURI();
        log.info("用户正在访问-->{}", url);

//        User user = (User) httpServletRequest.getSession().getAttribute("user");
//        int index = new Gson().fromJson(httpServletRequest.getReader(), InteractionData.class).getIndex();

        if (null != user) {
            Map<Integer, Integer> indexPrivilegeMap = user.getIndexPrivilegeMap();
            if (indexPrivilegeMap.containsKey(index) && indexPrivilegeMap.get(index) == 1) {
                filterChain.doFilter(myRequestWrapper, servletResponse);
                return;
            } else {
                if (url.equals("/actiondevice/adddevice")) {
                    filterChain.doFilter(myRequestWrapper, servletResponse);
                    return;
                } else {
                    log.info("操作用电器过滤器 -->> 用户无权限");
                    responseData.setStatus(StatusEnum.USER_HASNOPRIVILEGE.getStatus());
                }
            }
        } else {
            log.info("操作用电器拦截器 -->> 用户未登陆");
            responseData.setStatus(StatusEnum.USER_ISNOLOGIN.getStatus());
        }
        httpServletResponse.getWriter().write(new Gson().toJson(responseData));
    }

    @Override
    public void destroy() {

    }

    private boolean chain(BodyReaderHttpServletRequestWrapper requestWrapper) {
        String url = requestWrapper.getRequestURI();
        // 代码隔太久忘记架构了，赶时间产物，轻喷
        if (url.endsWith("listtiming") || url.endsWith("deltiming") || url.endsWith("feedback")) {
            return true;
        }
        return false;
    }
}
