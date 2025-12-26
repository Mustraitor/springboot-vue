package edu.friday.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "sys_role", schema = "friday")
public class SysRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "role_name", nullable = false, length = 30)
    private String roleName;

    @Column(name = "role_key", nullable = false, length = 100)
    private String roleKey;

    @Column(name = "role_sort", nullable = false)
    private Integer roleSort;

    @ColumnDefault("'1'")
    @Column(name = "data_scope")
    private String dataScope;

    @Column(name = "status", nullable = false)
    private String status;

    @ColumnDefault("'0'")
    @Column(name = "del_flag")
    private String delFlag;

    @ColumnDefault("''")
    @Column(name = "create_by", length = 64)
    private String createBy;

    @Column(name = "create_time")
    private Instant createTime;

    @ColumnDefault("''")
    @Column(name = "update_by", length = 64)
    private String updateBy;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "remark", length = 500)
    private String remark;
}
