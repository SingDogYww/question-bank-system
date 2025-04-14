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
 * 用户组数据映射表
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Getter
@Setter
@ToString
@TableName("user_group_data_map")
public class UserGroupDataMap implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userGroupId;

    /**
     * 数据id
     */
    private Integer dataId;

    /**
     * 数据类型
     */
    private Integer namespace;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;
}
