package com.newtranx.cloud.edit.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@NoArgsConstructor
@AllArgsConstructor
//通过这个注解可以声明一个文档，指定其所在的索引库和type
@Document(indexName = "demoesbean")
public class DemoEsBean {

    @Id
    private long id;
    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String name;
    private String fileId;
    private String desc;
}
