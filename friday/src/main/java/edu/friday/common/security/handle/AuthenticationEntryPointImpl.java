package edu.friday.common.security.handle;

import com.alibaba.fastjson.JSON;
import edu.friday.common.constant.HttpStatus;
import edu.friday.common.result.RestResult;
import edu.friday.utils.StringUtils;
import edu.friday.utils.http.ServletUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源" , request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(RestResult.error(code, msg)));
    }
}
