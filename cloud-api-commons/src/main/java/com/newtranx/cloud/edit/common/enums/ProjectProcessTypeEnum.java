package com.newtranx.cloud.edit.common.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;

/**
 * 描述:项目流程枚举类
 *
 * @version V1.0
 */
public enum ProjectProcessTypeEnum implements IEnum {

    TRANSLATE(1, "翻译"),
    EDIT(2, "编辑"),
    CHECK(3, "校对"),
    ;

    ProjectProcessTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private String name;

    private Integer code;

    public static ProjectProcessTypeEnum getByCode(Integer code){
        for (ProjectProcessTypeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;

    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code){
        this.code = code;
    }

    @Override
    public Serializable getValue() {
        return code;
    }

    @JsonValue
    public String getName(){
        return this.name;
    }

}
