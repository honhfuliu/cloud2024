package com.ziheng.cloud.controller;

import cn.hutool.core.date.DateUtil;
import com.ziheng.cloud.apis.PayFeignApi;
import com.ziheng.cloud.entities.PayDTO;
import com.ziheng.cloud.resp.ResultData;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("feign")
public class OrderController {
    @Resource
    private PayFeignApi payFeignApi;

    @PostMapping("add")
    public ResultData add(@RequestBody PayDTO payDTO){
        System.out.println("第一步：模拟本地addOrder新增订单成功(省略sql操作)，第二步：再开启addPay支付微服务远程调用");
        ResultData resultData = payFeignApi.addPay(payDTO);
        return resultData;
    }

    @GetMapping("/pay/{id}")
    public ResultData getByIdInfo(@PathVariable("id") Integer id){
        System.out.println("-------支付微服务远程调用，按照id查询订单支付流水信息");
        ResultData<Object> resultData = new ResultData<>();
        try{
            System.out.println("调用开始：" +  DateUtil.now());
            resultData = payFeignApi.getByIdInfo(id);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("调用结束：" +  DateUtil.now());
            ResultData.fali("500", e.getMessage());
        }
        return resultData;
    }

    // openfeign天然支持负载均衡演示
    @GetMapping("/get/info")
    public String mylb(){
        return payFeignApi.myLoadB();
    }

}
