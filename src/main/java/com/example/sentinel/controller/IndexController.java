package com.example.sentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: liuhuan
 * @Description: 跳转控制层
 * @date: 2019/10/30
 */
@Controller
public class IndexController {
    //  特别地，若 blockHandler 和 fallback 都进行了配置，则被限流降级而抛出 BlockException 时只会进入 blockHandler 处理逻辑。
    // 若未配置 blockHandler、fallback 和 defaultFallback，则被限流降级时会将 BlockException 直接抛出。
    @RequestMapping("/hello")
    @ResponseBody
    @SentinelResource(value = "hello", blockHandler = "exceptionHandler", fallback = "helloFallback")
    public String getInfo(Long s){
        return "hello 未被限流";
    }

    // 对应的 `handleException` 函数如果需要位于其他类中，在blockHandlerClass指定类，并且必须为 static 函数.
    @RequestMapping("/test")
    @ResponseBody
    @SentinelResource(value = "test", blockHandler = "exceptionHandler")
    public String test(Long s) {
        //initFlowRules();
        return "test 未被限流";
    }


    // Fallback 函数，函数签名与原函数一致或加一个 Throwable 类型的参数.
    @ResponseBody
    public String helloFallback(Long s) {
        return "进入 helloFallback 限流";
    }

    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    @ResponseBody
    public String exceptionHandler(Long s, BlockException ex) {
        // Do some log here.
        ex.printStackTrace();
        return "进入 exceptionHandler 限流";
    }

    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("test");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(3);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }
}
