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
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

@Slf4j
@WebFilter(filterName = "querydevice", urlPatterns = {"/querydevice/*"})
public class QueryDeviceFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("用户查看过滤器-->>初始化");

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info("用户查看过滤器-->>开始过滤");

        ResponseData responseData = new ResponseData();

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        BodyReaderHttpServletRequestWrapper myRequestWrapper;
        // 封装请求参数
        myRequestWrapper = new BodyReaderHttpServletRequestWrapper(httpServletRequest);

        String valueStr = "";
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = new BufferedReader(myRequestWrapper.getReader());
            String s = "";
            while ((s = br.readLine()) != null) {
                sb.append(s);
            }
            valueStr = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("valusStr:" + valueStr);
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;



//        String url = httpServletRequest.getRequestURI();
//        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String url = myRequestWrapper.getRequestURI();
        User user = (User) myRequestWrapper.getSession().getAttribute("user");
        log.info("用户查看过滤器-->>正在访问{}", url);

        if (null != user) {
            Map<Integer, Integer> indexPrivilegeMap = user.getIndexPrivilegeMap();
            log.info("用户查看过滤器-->>用户信息：{}", user.toString());

            // 是否查看该用户所有权限
            if (!url.equals("/querydevice/queryindex")) {
                int index = new Gson().fromJson(myRequestWrapper.getReader(), InteractionData.class).getIndex();
                if (indexPrivilegeMap.containsKey(index)) {
                    filterChain.doFilter(myRequestWrapper, servletResponse);
                    return;
                } else {
                    log.info("用户权限不足,权限值为:{}", indexPrivilegeMap.values());
                    responseData.setStatus(StatusEnum.USER_HASNOPRIVILEGE.getStatus());
                }
            } else {
                filterChain.doFilter(myRequestWrapper, servletResponse);
                return;
            }

        } else {
            log.info("用户未登陆");
            responseData.setStatus(StatusEnum.USER_ISNOLOGIN.getStatus());
        }
        httpServletResponse.getWriter().write(new Gson().toJson(responseData));
    }

    @Override
    public void destroy() {

    }
}
