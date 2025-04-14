package cn.cxyxh.project.question.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Getter
@Setter
@ToString
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 菜单编码
     */
    private String code;

    /**
     * 父级菜单编码
     */
    private String parentCode;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由路径
     */
    private String route;

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
