package cn.cxyxh.project.question.dto;

import lombok.Data;


@Data
public class ActionDTO {
    private Integer id;
    private String name;
    private String code;
    private String type; // API、操作按钮

}