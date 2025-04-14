package cn.cxyxh.project.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class MenuDTO {
    private Integer id;
    private String name;
    private String code;
    private String parentCode;
    private String icon;
    private String route;

    // 子菜单列表，用于构建树形结构
    private List<MenuDTO> children;
}