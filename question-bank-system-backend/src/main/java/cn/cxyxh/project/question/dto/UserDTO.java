package cn.cxyxh.project.question.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private String id;
    private String username;
    private String password;
    private String email;
    private String mobilePhone;
    private Integer status;

    // 关联信息
    private List<RoleDTO> roles;
    private List<UserGroupDTO> userGroups;
}