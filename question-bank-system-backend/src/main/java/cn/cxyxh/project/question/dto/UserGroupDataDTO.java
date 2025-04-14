// src/main/java/cn/cxyxh/project/question/dto/UserGroupDataDTO.java
package cn.cxyxh.project.question.dto;

import lombok.Data;


@Data
public class UserGroupDataDTO {
    private Integer id;
    private Integer userGroupId;
    private Integer dataId;
    private Integer namespace; // 数据类型
}