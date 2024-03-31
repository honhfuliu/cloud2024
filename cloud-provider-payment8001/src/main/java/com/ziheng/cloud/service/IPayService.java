package com.ziheng.cloud.service;

import com.ziheng.cloud.entities.Pay;

import java.io.Serializable;
import java.util.List;

public interface IPayService extends Serializable {
    int add(Pay pay);

    int delete(Integer id);

    int update(Pay pay);

    Pay getById(Integer id);

    List<Pay> getAll();

}
