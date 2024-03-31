package com.ziheng.cloud.controller;

import com.ziheng.cloud.entities.Pay;
import com.ziheng.cloud.resp.ResultData;
import com.ziheng.cloud.service.IPayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("pay")
public class PayController {
    @Resource
    private IPayService payService;

    // 添加
    @PostMapping()
    public ResultData<String> addPay(@RequestBody Pay pay) {
        int i = payService.add(pay);
        return ResultData.success( "添加成功, 返回值：" + i);
    }

    //删除
    @DeleteMapping("{id}")
    public ResultData<String> deleteById(@PathVariable("id") Integer id){
        int i = payService.delete(id);
        return ResultData.success("删除成功，返回值是" + i);
    }


    //修改
    @PutMapping()
    public ResultData<String> updatePay(@RequestBody Pay pay){
        int i = payService.update(pay);
        return ResultData.success("修改成功，返回值是" + i);
    }

    // 查询单个
    @GetMapping("{id}")
    public ResultData<Pay> getById(@PathVariable("id") Integer id) {
        // 暂停62秒，测试处openfeign默认调用超时时间
        try {
            TimeUnit.SECONDS.sleep(62);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Pay dto = payService.getById(id);
        return ResultData.success(dto);
    }

    // 查询所有
    @GetMapping("all")
    public ResultData<List<Pay>> getAll(){
        List<Pay> all = payService.getAll();
        return  ResultData.success(all);
    }

    @Value("${server.port}")
    public String port;
    @GetMapping("/get/info")
    public String getInfoConsul(@Value("${ziheng.info}") String info){
        return "获取到的信息是" + info + ", port" + port;
    }






}
