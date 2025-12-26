package edu.friday.model;

import edu.friday.common.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "sys_user", schema = "friday")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Basic
    @Column(name = "user_name")
    private String userName;

    @Basic
    @Column(name = "nick_name")
    private String nickName;

    @Basic
    @Column(name = "user_type")
    private String userType;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "phonenumber")
    private String phonenumber;

    @Basic
    @Column(name = "sex")
    private String sex;

    @Basic
    @Column(name = "avatar")
    private String avatar;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "del_flag")
    private String delFlag;

    @Basic
    @Column(name = "login_ip")
    private String loginIp;

    @Basic
    @Column(name = "login_date")
    private Timestamp loginDate;

}
