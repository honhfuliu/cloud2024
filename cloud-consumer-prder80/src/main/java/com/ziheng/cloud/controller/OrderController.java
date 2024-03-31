package com.ziheng.cloud.controller;

import com.ziheng.cloud.entities.PayDTO;
import com.ziheng.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("")
public class OrderController {
//    public static final String PayURL = "http://localhost:8001"; //硬编码，地址写死
    public static final String PayURL = "http://cloud-payment-service";

    // 将RestTemplate注入
    @Resource
    private RestTemplate restTemplate;

    // 添加
    @PostMapping("/consumer/pay")
    public ResultData addOrder(@RequestBody PayDTO payDTO){
        return restTemplate.postForObject(PayURL + "/pay", payDTO, ResultData.class);
    }

    // 删除
    @DeleteMapping("consumer/pay/{id}")
    public ResultData getByIdDelete(@PathVariable("id") Integer id){
        // 这里的delete 和 put请求是不能像getForObject这些一样的直接返回ResultData，所以只能使用exchange通用请求来实现。
        ResponseEntity<ResultData> exchange = restTemplate.exchange(
                PayURL + "/pay/" + id,
                HttpMethod.DELETE,
                null,
                ResultData.class);
        ResultData resultData = exchange.getBody();
        return resultData;
    }

    // 修改
    @PutMapping("/consumer/pay")
    public ResultData update(@RequestBody PayDTO payDTO) {
        ResponseEntity<ResultData> exchange = restTemplate.exchange(
                PayURL + "/pay",
                HttpMethod.PUT,
                new HttpEntity<>(payDTO), // 请求体参数
                ResultData.class
        );
        ResultData resultData = exchange.getBody();
        return resultData;

    }

    // 根据id查询
    @GetMapping("/consumer/pay/{id}")
    public ResultData getByIdInfo(@PathVariable("id") Integer id){
        return restTemplate.getForObject(PayURL + "/pay/" + id, ResultData.class);
    }

    // 查询所有
    @GetMapping("/consumer/pay/all")
    public ResultData getAll(){
        return restTemplate.getForObject(PayURL + "/pay/all", ResultData.class);
    }

    @GetMapping("/consumer/pay/get/info")
    private String getInfoByConsul(){
        return restTemplate.getForObject(PayURL + "/pay/get/info", String.class);
    }

    @Resource
    private DiscoveryClient discoveryClient;
    @GetMapping("/consumer/discovery")
    public String discovery()
    {
        // 获取服务器上的所有的service
        List<String> services = discoveryClient.getServices();
        for (String element : services) {
            System.out.println(element);
        }

        System.out.println("===================================");
        // 找名字是cloud-payment-service，找到之后获取所有的service id, host   port  url
        List<ServiceInstance> instances = discoveryClient.getInstances("cloud-payment-service");
        for (ServiceInstance element : instances) {
            System.out.println(element.getServiceId()+"\t"+element.getHost()+"\t"+element.getPort()+"\t"+element.getUri());
        }

        return instances.get(0).getServiceId()+":"+instances.get(0).getPort();
    }

}
