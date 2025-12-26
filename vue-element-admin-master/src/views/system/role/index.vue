<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="20" :xs="24">
        <el-form
          ref="queryForm"
          :model="queryParams"
          inline
          label-width="80px"
        >
          <!-- 角色名称 -->
          <el-form-item label="角色名称" prop="roleName">
            <el-input
              v-model="queryParams.roleName"
              placeholder="请输入角色名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <!-- 权限字符 -->
          <el-form-item label="权限字符" prop="roleKey">
            <el-input
              v-model="queryParams.roleKey"
              placeholder="请输入角色权限字符"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <!-- 状态 -->
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="角色状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>

          <!-- 搜索 / 重置 -->
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
            >
              搜索
            </el-button>
            <el-button
              icon="el-icon-refresh"
              size="mini"
              @click="resetQuery"
            >
              重置
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 按钮区：新增、修改、删除 -->
        <el-row :gutter="10" class="mb8" style="margin-top: 10px;">
          <el-col :span="4">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >
              新增
            </el-button>
          </el-col>
          <el-col :span="4">
            <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
            >
              修改
            </el-button>
          </el-col>
          <el-col :span="4">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
            >
              删除
            </el-button>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="24">

        <el-table
          :data="roleList"
          style="width: 100%"
          :loading="listLoading"
          @selection-change="handleSelectionChange"
        >
          <!-- 多选 -->
          <el-table-column type="selection" width="40" align="center" />
          <!-- 角色编号 -->
          <el-table-column prop="roleId" label="角色编号" align="center" />
          <!-- 角色名称 -->
          <el-table-column prop="roleName" label="角色名称" align="center" :show-overflow-tooltip = true />
          <!-- 权限字符 -->
          <el-table-column prop="roleKey" label="权限字符" align="center" :show-overflow-tooltip = true />

          <!-- 状态 -->
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>

          <!-- 创建时间 -->
          <el-table-column label="创建日期" align="center">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span>{{ scope.row.createTime }}</span>
            </template>
          </el-table-column>

          <!-- 操作 -->
          <el-table-column label="操作" align="center" width="220">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改</el-button>

              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除</el-button>
            </template>
          </el-table-column>

        </el-table>

        <!-- 分页 -->
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.size"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <!-- <el-dialog :title="title" :visible.sync="open" width="600px">
  <el-form ref="form" :model="form" :rules="rules" label-width="90px">

    <el-row :gutter="20">
      <el-col :span="12">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName" placeholder="请输入角色名称" />
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item label="权限字符" prop="roleKey">
          <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="显示顺序" prop="roleSort">
          <el-input-number v-model.number="form.roleSort" :min="1" :controls-position="'right'" />
        </el-form-item>
      </el-col>

      <el-col :span="12">
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="'0'">正常</el-radio>
            <el-radio :label="'1'">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>
  <el-form-item label="菜单权限">
    <el-tree
      ref="menu"
      :data="menuOptions"
      show-checkbox
      node-key="id"
      default-expand-all
      :props="{ children: 'children', label: 'label' }"
    />
  </el-form-item>
      <el-col :span="24">
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-col>

    </el-row>
  </el-form>

  <template #footer>
    <el-button type="primary" @click="submitForm">确定</el-button>
    <el-button @click="cancel">取消</el-button>
  </template>
</el-dialog> -->
    <el-dialog :title="title" :visible.sync="open" width="600px">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px">

        <el-row :gutter="20">

          <!-- 左右两列 -->
          <el-col :span="12">
            <el-form-item label="角色名称" prop="roleName">
              <el-input v-model="form.roleName" placeholder="请输入角色名称" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="权限字符" prop="roleKey">
              <el-input v-model="form.roleKey" placeholder="请输入权限字符" />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="显示顺序" prop="roleSort">
              <el-input-number
                v-model.number="form.roleSort"
                :min="1"
                controls-position="right"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-radio-group v-model="form.status">
                <el-radio label="0">正常</el-radio>
                <el-radio label="1">停用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <!-- 菜单权限（单独一行） -->
          <el-col :span="24">
            <el-form-item label="菜单权限">
              <el-tree
                ref="menu"
                :data="menuOptions"
                show-checkbox
                node-key="id"
                :props="{ children: 'children', label: 'label' }"
              />
            </el-form-item>
          </el-col>

          <!-- 备注（单独一行） -->
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入备注"
              />
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="submitForm">确定</el-button>
        <el-button @click="cancel">取消</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { listRole, getRole, addRole, updateRole, delRole } from '@/api/system/role'
import { treeselect, roleMenuTreeselect } from "@/api/system/menu";

export default {
  data() {
    return {
      roleList: [],           // 角色列表
      listLoading: true,      // 表格加载状态
      total: 0,   // 数据总数
      menuOptions: [],
      queryParams: {          // 查询参数
        page: 1,
        size: 10,
        roleName: undefined,  // 角色名称（模糊查询）
        status: undefined     // 角色状态
      },

      statusOptions: [        // 状态选择
        { dictLabel: '正常', dictValue: '0' },
        { dictLabel: '停用', dictValue: '1' }
      ],

      single: true,           // 是否单选（修改按钮可用性）
      multiple: true,         // 是否有选中（删除按钮可用性）
      title: '',              // 弹窗标题
      open: false,            // 弹窗显示状态

    form: {
      roleId: undefined,
      roleName: '',
      roleKey: '',
      roleSort: 1,   // Number
      status: '0',
      remark: '',
      dataScope: '1',
      menuIds: []    // 对应 VO 的 Long[]
    },

      rules: {                // 表单校验规则
        roleName: [
          { required: true, message: '角色名称不能为空', trigger: 'blur' }
        ],
        roleKey: [
          { required: true, message: '权限字符不能为空', trigger: 'blur' }
        ],
        roleSort: [
          { required: true, type: 'number', message: '显示顺序不能为空', trigger: 'blur' }
        ]
      }
    }
  },

  created() {
    this.getList()
  },

  methods: {
    /** 获取角色列表 */
    async getList() {
      this.listLoading = true
      const response = await listRole(this.queryParams)
      console.log(response);
      this.roleList = response.rows
      this.total = response.total
      this.listLoading = false
    },

    handleQuery() {
      this.queryParams.page = 1
      this.getList()
    },

    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },

    /** 新增角色 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加角色'
      // this.form.status = ''
      treeselect().then(res => {
        this.menuOptions = res.data;
      });

    },

    /** 修改角色 */
    handleUpdate(row) {
      this.reset()
      const roleId = row.roleId || this.ids
      getRole(roleId).then(response => {
        // console.log(this.form);
        this.form = response.data
        this.open = true
        this.title = '修改角色'
        roleMenuTreeselect(roleId).then(res => {
          this.menuOptions = res.menus;        // 树数据
          const checkedKeys = res.checkedKeys; // 已勾选菜单 ID
          this.$nextTick(() => {
            this.$refs.menu.setCheckedKeys(checkedKeys || []);
          });
        })

      })
    },



    /** 提交表单（新增/修改） */
    submitForm () {
      this.$refs['form'].validate(valid => {
        if (valid) {

          this.form.menuIds = [
            ...this.$refs.menu.getCheckedKeys(),
            ...this.$refs.menu.getHalfCheckedKeys()
          ]

          delete this.form.createTime;
          delete this.form.updateTime;
          console.log(this.form)
          if (this.form.roleId !== undefined) {
            updateRole(this.form).then(response => {
              console.log(this.form);
              console.log(response);

              if (response.code === 200) {
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          } else {
            addRole(this.form).then(response => {
              if (response.code === 200) {
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              } else {
                this.msgError(response.msg)
              }
            })
          }
        }
      })
    },

    /** 删除角色 */
    handleDelete(row) {
      const roleIds = row.roleId || this.ids
      this.$confirm('是否确认删除用户编号为'+ roleIds + '的数据项？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
       }).then(function() {
        return delRole(roleIds)
       }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
       }).catch(function() {})
    },

    /** 多选框 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.roleId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },

    /** 重置表单 */
    reset() {
      this.form = {
        roleId: undefined,
        roleName: '',
        roleKey: '',
        roleSort: 1,   // Number
        status: '0',
        remark: '',
        dataScope: '1',
        menuIds: []    // 对应 VO 的 Long[]
      }
      this.resetForm('form')
    },

    cancel() {
      this.open = false
      this.reset()
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}
.mb8 {
  margin-bottom: 8px;
}
</style>

