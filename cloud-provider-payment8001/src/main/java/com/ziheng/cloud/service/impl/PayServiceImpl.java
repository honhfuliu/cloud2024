package com.ziheng.cloud.service.impl;

import com.ziheng.cloud.entities.Pay;
import com.ziheng.cloud.mapper.PayMapper;
import com.ziheng.cloud.service.IPayService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayServiceImpl implements IPayService {
    @Resource
    private PayMapper payMapper;

    @Override
    public int add(Pay pay) {
        return payMapper.insertSelective(pay);
    }

    @Override
    public int delete(Integer id) {
        return payMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int update(Pay pay) {
        return payMapper.updateByPrimaryKeySelective(pay);
    }

    @Override
    public Pay getById(Integer id) {
        return payMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Pay> getAll() {
        return payMapper.selectAll();
    }
}
