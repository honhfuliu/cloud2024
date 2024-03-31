package com.ziheng.cloud.config;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

import java.util.*;

@Configuration
public class GatewayConfiguration {
    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayConfiguration(ObjectProvider<List<ViewResolver>> viewResolversProvider,
                                ServerCodecConfigurer serverCodecConfigurer) {
        this.viewResolvers = viewResolversProvider.getIfAvailable(Collections::emptyList);
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler() {
        // Register the block exception handler for Spring Cloud Gateway.
        return new SentinelGatewayBlockExceptionHandler(viewResolvers, serverCodecConfigurer);
    }

    @Bean
    @Order(-1)
    public GlobalFilter sentinelGatewayFilter() {
        return new SentinelGatewayFilter();
    }

    @PostConstruct
    public void doInit() {
        //如上代码都是copy官网的，下面的才是自己编写的
        initBlockHandler();
    }

    // 处理+自定义返回的例外信息内容，类似我们的调用触发了流控规则保护
    private void initBlockHandler() {
        // 创建限流规则集合
        Set<GatewayFlowRule> rules = new HashSet<>();
        // 创建限流规则
        rules.add(new GatewayFlowRule("pay_routh1") // 这里的resource 看你的application中的路由名称是什么
                // 设置限流规则的资源名称，这里是根据您的路由名称设置的
                .setCount(2) // 多少个
                .setIntervalSec(1) // 多少秒 ，这里是一秒钟两个
        );
        // 将限流规则加载到 Gateway 中
        // 加载规则
        GatewayRuleManager.loadRules(rules);

        // 创建自定义限流处理程序
        BlockRequestHandler handler= new BlockRequestHandler() {
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                // 创建错误信息
                HashMap<String, String> map = new HashMap<>();
                map.put("errorCode", HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase());
                map.put("errorMassage","请求太过频繁，系统忙不过来，触发限流（sentinel + gateway整合Case）");

                // 返回错误响应
                return ServerResponse.status(HttpStatus.TOO_MANY_REQUESTS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(map));
            }
        };
        // 将自定义限流处理程序设置到 Gateway 中
        GatewayCallbackManager.setBlockHandler(handler);
    }
}
