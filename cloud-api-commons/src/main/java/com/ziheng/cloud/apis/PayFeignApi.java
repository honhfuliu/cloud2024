package com.ziheng.cloud.apis;

import com.ziheng.cloud.entities.PayDTO;
import com.ziheng.cloud.resp.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//@FeignClient(value =  "cloud-payment-service")
@FeignClient(value =  "cloud-gateway")
public interface PayFeignApi {
    // 消费者80通过这里直接调用8001里面的pay/add

    // 新增一条支付相关流水记录
    @PostMapping("/pay")
    public ResultData addPay(@RequestBody PayDTO payDTO);

    // 按照主键记录查询支付流水信息
    @GetMapping("/pay/{id}")
    public ResultData getByIdInfo(@PathVariable("id") Integer id);

    // 查询所有
//    @GetMapping("/pay/all")
//    public ResultData getAll();

    //openfeign天然支持负载均衡演示
    @GetMapping("/pay/get/info")
    public String myLoadB();

    /**
     * Resilience4j CircuitBreaker 的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/circuit/{id}")
    public String myCircuit(@PathVariable("id") Integer id);

    /**
     * Resilience4j Bulkhead 的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/bulkhead/{id}")
    public String myBulkhead(@PathVariable("id") Integer id);

    /**
     * Resilience4j Ratelimit 的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/ratelimit/{id}")
    public String myRatelimit(@PathVariable("id") Integer id);

    /**
     * Micrometer(Sleuth)进行链路监控的例子
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/micrometer/{id}")
    public String myMicrometer(@PathVariable("id") Integer id);

    /**
     * GateWay进行网关测试案例01
     * @param id
     * @return
     */
    @GetMapping(value = "/pay/gateway/get/{id}")
    public ResultData getById(@PathVariable("id") Integer id);

    /**
     * GateWay进行网关测试案例02
     * @return
     */
    @GetMapping(value = "/pay/gateway/info")
    public ResultData<String> getGatewayInfo();
}
