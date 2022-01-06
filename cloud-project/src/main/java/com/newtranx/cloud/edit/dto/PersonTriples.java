package com.newtranx.cloud.edit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonTriples {
//    3.	person_triples 关于人物的三元组，表示提取的关于人的信息，对应关系为：
    @Id
    private String    id;
    private String    name; //人名
    private String    ethnic; // 民族
    private String    gender; //性别
    private String    origin; //籍贯
    private String    nickname; //别称
    private String    birthdate; //出生年月
    private String    deathdate; //死亡年月
    private String    career; //职业

    private Integer taskid;

}
