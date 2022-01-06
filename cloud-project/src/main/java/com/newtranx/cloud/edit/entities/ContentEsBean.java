package com.newtranx.cloud.edit.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;


import java.io.Serializable;
import java.util.List;

/**
 * 存储每个文章的目录结构
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
//通过这个注解可以声明一个文档，指定其所在的索引库和type
//@Document(indexName = "contentesbean")
public class ContentEsBean implements Serializable {

    @Id
    private String id;
//    @Field(analyzer = "ik_smart", type = FieldType.Text)
    private String totalTitle;
    private String fileId;
    private String muluJson;
}
