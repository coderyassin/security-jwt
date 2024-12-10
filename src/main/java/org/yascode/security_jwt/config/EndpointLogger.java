package org.yascode.security_jwt.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Component
@Slf4j
public class EndpointLogger implements ApplicationRunner {
    private final WebApplicationContext webApplicationContext;

    public EndpointLogger(WebApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        RequestMappingHandlerMapping requestMappingHandlerMapping =
                webApplicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();

        log.info("\n==== Available REST Endpoints ====");
        map.forEach((key, value) -> {
            log.info("Endpoint: {} | Method: {} | Handler: {}.{}",
                    key.getPathPatternsCondition().getPatterns(),
                    key.getMethodsCondition().getMethods(),
                    value.getMethod().getDeclaringClass().getSimpleName(),
                    value.getMethod().getName());
        });
        log.info("==================================\n");
    }
}
