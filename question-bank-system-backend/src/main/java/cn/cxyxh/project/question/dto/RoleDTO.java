package cn.cxyxh.project.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {
    private Integer id;
    private String name;
    private String code;
    private Integer deleted;

    // 关联信息
    private List<Integer> menuIds;
    private List<Integer> actionIds;
}