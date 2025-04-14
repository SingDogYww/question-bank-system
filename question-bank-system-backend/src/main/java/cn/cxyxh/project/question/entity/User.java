package cn.cxyxh.project.question.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author awei
 * @since 2025-04-09
 */
@Getter
@Setter
@ToString
public class User implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID，UUID
     */
    private String id;

    /**
     * 用户名，不能登录
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱，可参与登录
     */
    private String email;

    /**
     * 手机号，可参与登录
     */
    private String mobilePhone;

    /**
     * 用户状态（禁用，激活，删除）
     */
    private Integer status;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
