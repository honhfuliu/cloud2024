package com.ziheng.cloud.resp;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultData<T> {
    private String code;
    private String massage;
    private T data;
    private Long timestmap;

    public ResultData() {
        this.timestmap = System.currentTimeMillis();
    }

    // 成功
    public static <T> ResultData<T> success(T data) {
        ResultData resultData = new ResultData<>();
        resultData.setCode(ReturnCodeEnum.RC200.getCode());
        resultData.setMassage(ReturnCodeEnum.RC200.getMessage());
        resultData.setData(data);
        return resultData;
    }

    // 失败
    public static <T> ResultData<T> fali(String code, String massage) {
        ResultData resultData = new ResultData<>();
        resultData.setCode(code);
        resultData.setMassage(massage);
        resultData.setData(null);
        return resultData;
    }

}
