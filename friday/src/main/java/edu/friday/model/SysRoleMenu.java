package edu.friday.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sys_role_menu", schema = "friday")
public class SysRoleMenu {
    @EmbeddedId
    private SysRoleMenuId id;

}