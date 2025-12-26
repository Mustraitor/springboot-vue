<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true">
      <el-form-item label="菜单名称">
        <el-input
          v-model="queryParams.menuName"
          placeholder="请输入菜单名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="状态">
        <el-select
          v-model="queryParams.visible"
          placeholder="菜单状态"
          clearable
          size="small"
        >
          <el-option
            v-for="dict in visibleOptions"
            :key="dict.dictValue"
            :label="dict.dictLabel"
            :value="dict.dictValue"
          />
        </el-select>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">
          搜索
        </el-button>
        <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAdd">
          新增
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 树形表格 -->
    <el-table
      v-loading="loading"
      :data="menuList"
      row-key="menuId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column prop="menuName" label="菜单名称" show-overflow-tooltip width="160" />
      <el-table-column prop="icon" label="图标" align="center" width="100">
        <template slot-scope="scope">
          <svg-icon :icon-class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="orderNum" label="排序" width="60" />
      <el-table-column prop="perms" label="权限标识" show-overflow-tooltip />
      <el-table-column prop="component" label="组件路径" show-overflow-tooltip />
      <el-table-column
        prop="visible"
        label="可见"
        :formatter="visibleFormat"
        width="80"
      />
      <el-table-column prop="createTime" label="创建时间" align="center">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
          >
            修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >
            删除
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-plus"
            @click="handleAdd(scope.row)"
          >
            新增
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog :title="title" :visible.sync="open" width="600px">
  <el-form ref="form" :model="form" :rules="rules" label-width="80px">
    <el-row>
      <!-- 上级菜单 -->
      <el-col :span="24">
        <el-form-item label="上级菜单" >
          <treeselect
            v-model="form.parentId"
            :options="menuOptions"
            :normalizer="normalizer"
            :show-count="true"
            placeholder="选择上级菜单"
          />
        </el-form-item>
      </el-col>

      <!-- 菜单类型 -->
      <el-col :span="24">
        <el-form-item label="菜单类型" prop="menuType">
          <el-radio-group v-model="form.menuType">
            <el-radio label="M">目录</el-radio>
            <el-radio label="C">菜单</el-radio>
            <el-radio label="F">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <!-- 菜单图标 -->
      <el-col :span="24" v-if="form.menuType !== 'F'">
        <el-form-item label="菜单图标">
          <el-popover placement="bottom-start" width="460" trigger="click" @show="$refs['iconSelect'].reset()">
            <IconSelect ref="iconSelect" @selected="selected" />
            <el-input
              slot="reference"
              v-model="form.icon"
              placeholder="单击选择图标"
              readonly
            >
              <svg-icon
                v-if="form.icon"
                slot="prefix"
                :icon-class="form.icon"
                class="el-input_icon"
                style="height: 32px; width: 16px;"
              />
              <i v-else slot="prefix" class="el-icon-search el-input_icon" />
            </el-input>
          </el-popover>
        </el-form-item>
      </el-col>

      <!-- 菜单名称 -->
      <el-col :span="12">
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
      </el-col>

      <!-- 显示排序 -->
      <el-col :span="12">
        <el-form-item label="显示排序" prop="orderNum">
          <el-input-number v-model="form.orderNum" :min="0" controls-position="right" />
        </el-form-item>
      </el-col>

      <!-- 是否外链 -->
      <el-col :span="12" >
        <el-form-item v-if="form.menuType !== 'F'" label="是否外链">
          <el-radio-group v-model="form.isFrame">
            <el-radio label="0">是</el-radio>
            <el-radio label="1">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <!-- 路由地址 -->
      <el-col :span="12" >
        <el-form-item label="路由地址" prop="path" v-if="form.menuType !== 'F'">
          <el-input v-model="form.path" placeholder="请输入路由地址" />
        </el-form-item>
      </el-col>

      <!-- 组件路径 -->
      <el-col :span="12" v-if="form.menuType === 'C'">
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="form.component" placeholder="请输入组件路径" />
        </el-form-item>
      </el-col>

      <!-- 权限标识 -->
      <el-col :span="12">
        <el-form-item label="权限标识"  v-if="form.menuType !== 'M'" >
          <el-input v-model="form.perms" placeholder="请输入权限标识" maxlength="50" />
        </el-form-item>
      </el-col>

      <!-- 菜单状态 -->
      <el-col :span="24" v-if="form.menuType !== 'F'">
        <el-form-item label="菜单状态">
          <el-radio-group v-model="form.visible">
            <el-radio
              v-for="dict in visibleOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >
              {{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <!-- 底部按钮 -->
  <div slot="footer" class="dialog-footer">
    <el-button type="primary" @click="submitForm">确定</el-button>
    <el-button @click="cancel">取消</el-button>
  </div>
</el-dialog>

  </div>
</template>
<script>
import { listMenu, addMenu, updateMenu, getMenu, delMenu } from '@/api/system/menu'
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"
import IconSelect from "@/components/IconSelect"

export default {
  name: 'Menu',
  components: {
    Treeselect,
    IconSelect
  },
  data() {
    return {
      currentRoleId: null,
      // 遮罩层
      loading: true,
      // 菜单表格树数据
      menuList: [],
      // 菜单树选项（可用于表单选择父菜单）
      menuOptions: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 菜单状态数据字典
      visibleOptions: [
        {
          dictCode: '4',
          dictSort: '1',
          dictLabel: '显示',
          dictValue: '0',
          dictType: 'sys_show_hide',
          cssClass: '',
          listClass: 'primary',
          isDefault: 'Y',
          status: '0',
          createBy: '1',
          createTime: '0',
          updateBy: '0',
          updateTime: '0',
          remark: '显示菜单'
        },
        {
          dictCode: '5',
          dictSort: '2',
          dictLabel: '隐藏',
          dictValue: '1',
          dictType: 'sys_show_hide',
          cssClass: '',
          listClass: 'danger',
          isDefault: 'N',
          status: '0',
          createBy: '0',
          createTime: '0',
          updateBy: '0',
          updateTime: '0',
          remark: '隐藏菜单'
        }
      ],
      // 查询参数
      queryParams: {
        menuName: undefined,
        visible: undefined
      },
      // 表单参数
      form: {},
      // 表单校验规则
      rules: {
        menuName: [
          { required: true, message: '菜单名称不能为空', trigger: 'blur' }
        ],
        orderNum: [
          { required: true, message: '菜单顺序不能为空！', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
      // 选中图标
    selected(name) {
      this.form.icon = name;
    },
    // 处理树形结构，用于 el-tree-select
    normalizer(node) {
      return {
        id: node.menuId,
        label: node.menuName,
        children: node.children && node.children.length > 0 ? node.children : null
      };
    },
    /** 查询菜单列表 */
    getList() {
      this.loading = true
      listMenu(this.queryParams).then(response => {
        this.menuList = this.handleTree(response.data, 'menuId')
        this.loading = false
      })
    },
    visibleFormat(row, column) {
      if(row.menuType === 'F'){
        return ''
      }
      return this.selectDictLabel(this.visibleOptions, row.visible)
    },

    /** 将菜单列表处理成树形结构 */
    handleTree(data, idKey = 'menuId', parentKey = 'parentId', childrenKey = 'children') {
      const cloneData = JSON.parse(JSON.stringify(data))
      const tree = []
      const map = {}

      cloneData.forEach(item => {
        map[item[idKey]] = item
        item[childrenKey] = []
      })

      cloneData.forEach(item => {
        const parent = map[item[parentKey]]
        if (parent) {
          parent[childrenKey].push(item)
        } else {
          tree.push(item)
        }
      })

      return tree
    },

    /** 查询操作 */
    handleQuery() {
      this.getList()
    },

    /** 新增菜单 */
    handleAdd(row) {
      this.reset();          // 重置表单
      this.getTreeselect();  // 获取下拉树结构
      if (row != null) {
        this.form.parentId = row.menuId;
      }
      this.open = true;      // 弹出新增窗口
      this.title = '添加菜单';
    },

    /** 修改菜单 */
    handleUpdate(row) {
      this.reset()
      this.getTreeselect()
      getMenu(row.menuId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改菜单'
      })

    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$confirm(
        `是否确认删除名称为 "${row.menuName}" 的数据项？`,
        '警告',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      )
        .then(() => {
          // 调用删除接口
          return delMenu(row.menuId);
        })
        .then(() => {
          this.getList(); // 刷新列表
          this.msgSuccess('删除成功');
        })
        .catch(() => {
          // 用户取消或删除失败不做处理
        });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
  // 提交按钮
    submitForm() {
  this.$refs["form"].validate(valid => {
    if (valid) {
      if (this.form.menuId !== undefined) {
        // 修改菜单
        updateMenu(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          } else {
            this.msgError(response.msg);
          }
        });
      } else {
        // 新增菜单
        addMenu(this.form).then(response => {
          if (response.code === 200) {
            this.msgSuccess("新增成功");
            this.open = false;
            this.getList();

            // ✅ 新增菜单后给角色分配权限
            const newMenuId = response.data.menuId; // 后端返回的新菜单ID
            const roleId = this.currentRoleId;      // 当前编辑的角色ID，需要你在 data 或 props 中保存
            if (roleId) {
              this.addMenuPermission(roleId, newMenuId);
            }

          } else {
            this.msgError(response.msg);
          }
        });
      }
    }
  });
},

// 新增方法：给角色分配菜单权限
addMenuPermission(roleId, menuId) {
  this.$axios.post('/system/role/addMenuPermission', {
    roleId: roleId,
    menuId: menuId
  }).then(res => {
    if (res.code === 200) {
      this.msgSuccess('权限已自动添加给该角色');
      // 重新获取当前用户权限，让新增菜单立刻显示
      this.$store.dispatch('GetUserInfo').then(() => {
        this.getList(); // 刷新菜单
      });
    }
  });
},


    /** 表单重置 */
    reset() {
      this.form = {
        menuId: undefined,
        parentId: 0,
        menuName: '',
        icon: '',
        menuType: 'M',   // 默认目录
        orderNum: '',
        isFrame: '1',
        visible: '0',

        // 菜单 C 需要
        path: '',
        component: '',

        // 按钮 F 需要
        perms: ''
      };
      this.resetForm('form');
    },
      /** 查询菜单下拉树结构 */
    getTreeselect() {
      listMenu().then(response => {
        this.menuOptions = [];
        const menu = {
          menuId: 0,
          menuName: '主类目',
          children: []
        };
        menu.children = this.handleTree(response.data, 'menuId');
        this.menuOptions.push(menu);
      });
    },

  }
}
</script>
