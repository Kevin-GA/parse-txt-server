package com.newtranx.cloud.edit.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDto {
    private String title;
    private Integer page;
    private List<ContentDto> child;
}
