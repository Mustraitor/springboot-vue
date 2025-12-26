package edu.friday.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "sys_config", schema = "friday")
public class SysConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id", nullable = false)
    private Integer id;

    @ColumnDefault("''")
    @Column(name = "config_name", length = 100)
    private String configName;

    @ColumnDefault("''")
    @Column(name = "config_key", length = 100)
    private String configKey;

    @ColumnDefault("''")
    @Column(name = "config_value", length = 500)
    private String configValue;

    @ColumnDefault("'N'")
    @Column(name = "config_type")
    private Character configType;

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