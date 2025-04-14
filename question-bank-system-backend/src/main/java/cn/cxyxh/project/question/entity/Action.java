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
 * 操作表
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Getter
@Setter
@ToString
public class Action implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 操作名称
     */
    private String name;

    /**
     * 操作编码
     */
    private String code;

    /**
     * 操作类型(API、操作按钮)
     */
    private String type;


    private String path;


    private String method;


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
