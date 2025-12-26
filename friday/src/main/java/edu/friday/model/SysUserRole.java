package edu.friday.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sys_user_role", schema = "friday")
public class SysUserRole {
    @EmbeddedId
    private SysUserRoleId id;

}