package scau.xwcommon.sentinal;


import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.springframework.stereotype.Component;
import scau.xwcommon.util.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//api接口的统一异常处理
@Component
public class GlobalDefaultFallback implements BlockExceptionHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse,
                       BlockException e) throws Exception {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        //统一处理降级 熔断操作
        if(e instanceof DegradeException){
            //降级异常
            httpServletResponse.getWriter().write("{\"code\":500,\"msg\":\"服务器繁忙降级，请稍后再试\"}");
            return;
        }
        //流控异常 限流
        if(e instanceof FlowException){
            httpServletResponse.getWriter().write("{\"code\":500,\"msg\":\"服务器繁忙限流，请稍后再试\"}");
            return;
        }
    }
}
