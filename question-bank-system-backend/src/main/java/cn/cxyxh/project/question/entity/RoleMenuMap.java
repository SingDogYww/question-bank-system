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
 * 角色菜单映射表
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Getter
@Setter
@ToString
@TableName("role_menu_map")
public class RoleMenuMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String menuId;

    private Integer roleId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;
}
