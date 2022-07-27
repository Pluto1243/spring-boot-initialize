package com.wj.boot.config;

import com.wj.boot.entity.User;
import com.wj.boot.exception.CommonException;
import com.wj.boot.exception.EmError;
import com.wj.boot.service.IUserService;
import com.wj.boot.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户权限拦截器
 *
 * @author wangjie
 * @date 10:49 2022年07月26日
 **/
@CrossOrigin
@Component
public class SignInterceptor implements HandlerInterceptor {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 预处理回调方法，实现处理器的预处理
     * 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或处理器
     */
    @Override
    @ResponseBody
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 验证用户是否登录
        final String token = request.getHeader("token");
        if (token == null){
            throw new CommonException(EmError.LOGIN_EXPIRED);
        }
        final String account = jwtTokenUtil.getUsernameFromToken(token);
        if (account == null){
            throw new CommonException(EmError.LOGIN_EXPIRED);
        }
        final User user = userService.getUserByAccount(account);
        if (user == null){
            throw new CommonException(EmError.LOGIN_EXPIRED);
        }
        return true;
    }

    /**
     * 后处理回调方法，实现处理器（controller）的后处理，但在渲染视图之前
     * 此时我们可以通过modelAndView对模型数据进行处理或对视图进行处理
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

    }

    /**
     * 整个请求处理完毕回调方法，即在视图渲染完毕时回调，
     * 如性能监控中我们可以在此记录结束时间并输出消耗时间，
     * 还可以进行一些资源清理，类似于try-catch-finally中的finally，
     * 但仅调用处理器执行链中
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

    }
}
