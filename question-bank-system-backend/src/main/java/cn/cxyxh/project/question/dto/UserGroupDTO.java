package cn.cxyxh.project.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserGroupDTO {
    private Integer id;
    private String name;
    private String type;
    private String description;

    // 关联信息
    private List<String> userIds;
    private List<UserGroupDataDTO> dataPermissions;
}