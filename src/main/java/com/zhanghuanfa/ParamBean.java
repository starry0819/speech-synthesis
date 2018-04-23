package com.zhanghuanfa;

import lombok.Data;

/**
 * @author zhanghuanfa
 * @date 2018-04-20 16:11
 */
@Data
public class ParamBean {

    private String auf;
    private String aue;
    private String voice_name;
    private String speed = "50";
    private String volume = "50";
    private String pitch = "50";
    private String engine_type = "intp65";
    private String text_type = "text";
}
