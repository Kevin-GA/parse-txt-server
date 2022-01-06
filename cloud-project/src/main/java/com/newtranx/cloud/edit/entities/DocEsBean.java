package com.newtranx.cloud.edit.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;


import java.io.Serializable;


/**
 * 存储每个章节文章的es结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
//通过这个注解可以声明一个文档，指定其所在的索引库和type
//@Document(indexName = "DocEsBean", type = "DocEsBean")
//@Document(indexName = "docesbean")
public class DocEsBean implements Serializable {

    // 必须指定一个id，
    @Id
    private String id;
    // 这里配置了分词器，字段类型，可以不配置，默认也可
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String bianzhangjie;
    private String name;
    private Integer page;
    private Integer taksId;
    private Integer docId;
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String person;//人物
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String event;//事件
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String geographyInfo;// 地理
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String organization;//机构
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String desc;

}
