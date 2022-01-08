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
 * @since 2021-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("doc_job")
@ApiModel(value="DocJob对象", description="")
public class DocJob implements Serializable {

    private static final long serialVersionUID=1L;

      @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "任务")
    private String task;

    @ApiModelProperty(value = "发起人")
    private String promoter;

    @ApiModelProperty(value = "执行人")
    private String executor;

    @ApiModelProperty(value = "完成日期")
    private LocalDateTime completeDate;

    @ApiModelProperty(value = "任务状态")
    private String taskStatus;

    @ApiModelProperty(value = "文件位置")
    private String filePath;

    private String isDel;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
