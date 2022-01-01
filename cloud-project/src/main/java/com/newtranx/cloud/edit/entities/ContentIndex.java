package com.newtranx.cloud.edit.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author niujiaxin
 * @since 2022-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("content_index")
@ApiModel(value="ContentIndex对象", description="")
public class ContentIndex implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String pre;

    private String bian;

    private String zhang;

    private String jie;

    private String contentIndex;

    private String contentName;

    private Integer pageNum;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String isDel;


}
