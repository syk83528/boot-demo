package com.ipaozha.demo1.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * http返回外层通用对象
 */
@Data
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;

}
