package cn.cxyxh.project.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 用户组表
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Getter
@Setter
@ToString
@TableName("user_group")
public class UserGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户组名称
     */
    private String name;

    /**
     * 用户组类型
     */
    private String type;

    /**
     * 用户组描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 最后一次修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 最后一次修改人
     */
    private String updateBy;
}
