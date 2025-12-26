<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="20" :xs="24">
        <el-form
          ref="queryForm"
          :model="queryParams"
          inline
          label-width="68px"
        >
          <!-- 用户名称 -->
          <el-form-item label="用户名称" prop="userName">
            <el-input
              v-model="queryParams.userName"
              placeholder="请输入用户名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>

          <!-- 手机号码 -->
          <el-form-item label="手机号码" prop="phonenumber">
            <el-input
              v-model="queryParams.phonenumber"
              placeholder="请输入手机号码"
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
              placeholder="用户状态"
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

          <!-- 搜索/重置按钮 -->
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

        <!-- 新增/修改/删除按钮 -->
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
    <!-- 用户表格 -->
    <el-row :gutter="20">
      <el-col :span="24">
        <!-- 多选框 -->
        <el-table
          :data="userList"
          style="width: 100%"
          :loading="listLoading"
          @selection-change="handleSelectionChange"
        >
          <!-- 多选列 -->
          <el-table-column type="selection" width="40" align="center" />

          <!-- 用户编号 -->
          <el-table-column prop="userId" label="用户编号" align="center" />

          <!-- 用户名称 -->
          <el-table-column
            prop="userName"
            label="用户名称"
            align="center"
            show-overflow-tooltip
          />

          <!-- 用户昵称 -->
          <el-table-column
            prop="nickName"
            label="用户昵称"
            align="center"
            show-overflow-tooltip
          />

          <!-- 手机号码 -->
          <el-table-column prop="phonenumber" label="手机号码" align="center" />

          <!-- 状态 -->
          <el-table-column label="状态" align="center">
            <template #default ="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>

          <!-- 创建日期 -->
          <el-table-column label="创建日期" align="center">
            <template slot-scope = "scope">
              <i class="el-icon-time" />
              <span>{{ scope.row.createTime }}</span>
            </template>
          </el-table-column>

          <!-- 操作 -->
          <el-table-column label="操作" align="center" width="220">
            <template #default="scope">
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
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页组件 -->
        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="queryParams.page"
          :limit.sync="queryParams.size"
          @pagination="getList"
        />
      </el-col>
    </el-row>
    <el-dialog
  :title="title"
  :visible.sync="open"
  width="600px"
>
  <el-form
    ref="form"
    :model="form"
    :rules="rules"
    label-width="80px"
  >
    <el-row :gutter="20">
      <!-- 用户昵称 -->
      <el-col :span="12">
        <el-form-item label="用户昵称" prop="nickName">
          <el-input
            v-model="form.nickName"
            placeholder="请输入用户昵称"
          />
        </el-form-item>
      </el-col>

      <!-- 手机号码 -->
      <el-col :span="12">
        <el-form-item label="手机号码" prop="phonenumber">
          <el-input
            v-model="form.phonenumber"
            placeholder="请输入手机号码"
            maxlength="11"
          />
        </el-form-item>
      </el-col>

      <!-- 邮箱 -->
      <el-col :span="12">
        <el-form-item label="邮箱" prop="email">
          <el-input
            v-model="form.email"
            placeholder="请输入邮箱"
            maxlength="50"
          />
        </el-form-item>
      </el-col>

      <!-- 用户名称 -->
      <el-col :span="12">
        <el-form-item label="用户名称" prop="userName">
          <el-input
            v-model="form.userName"
            placeholder="请输入用户名称"
          />
        </el-form-item>
      </el-col>

      <!-- 用户密码（仅新增时显示） -->
      <el-col :span="12">
        <el-form-item v-if="!form.userId" label="用户密码" prop="password">
          <el-input
            v-model="form.password"
            placeholder="请输入用户密码"
            type="password"
          />
        </el-form-item>
      </el-col>

      <!-- 用户性别 -->
      <el-col :span="12">
        <el-form-item label="用户性别">
          <el-select v-model="form.sex" placeholder="请选择">
            <el-option
              v-for="dict in sexOptions"
              :key="dict.dictValue"
              :label="dict.dictLabel"
              :value="dict.dictValue"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <!-- 角色 -->
      <el-col :span="12">
        <el-form-item label="角色">
          <el-select
            v-model="form.roleIds"
            multiple
            placeholder="请选择"
          >
            <el-option
              v-for="item in roleOptions"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
              :disabled="item.status === 1"
            />
          </el-select>
        </el-form-item>
      </el-col>

      <!-- 状态 -->
      <el-col :span="12">
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in statusOptions"
              :key="dict.dictValue"
              :label="dict.dictValue"
            >
              {{ dict.dictLabel }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-col>

      <!-- 备注 -->
      <el-col :span="24">
        <el-form-item label="备注">
          <el-input
            v-model="form.remark"
            type="textarea"
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>

  <!-- 底部按钮 -->
  <template #footer>
    <el-button type="primary" @click="submitForm">确定</el-button>
    <el-button @click="cancel">取消</el-button>
  </template>
</el-dialog>

  </div>
</template>

<script>
import { listUser, getUser, addUser, updateUser, delUser } from '@/api/system/user'

export default {
  data() {
    return {
      userList: [],
      listLoading: true,
      total: 0,
      queryParams: {
        page: 1,
        size: 10,
        userName: undefined,
        phonenumber: undefined,
        status: undefined
      },
      statusOptions: [
        { dictLabel: '正常', dictValue: '0' },
        { dictLabel: '停用', dictValue: '1' }
      ],
      single: true,
      multiple: true,
      title: '',
      sexOptions: [
        { dictLabel: '男', dictValue: '0' },
        { dictLabel: '女', dictValue: '1' }
      ],
      roleOptions: [],
      initPassword: '123456',
      open: false,
      form: {},
      rules: {
        userName: [
          { required: true, message: '用户名称不能为空', trigger: 'blur' }
        ],
        nickName: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '用户密码不能为空', trigger: 'blur' }
        ],
        email: [
          {
            required: true,
            message: '邮箱地址不能为空',
            trigger: 'blur'
          },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ],
        phonenumber: [
          {
            required: true,
            message: '手机号码不能为空',
            trigger: 'blur'
          },
          {
            pattern: /^1[3-9]\d{9}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ]
      },

    }
  },
  created() {
    this.getList()
  },
  methods: {
    async getList() {
      this.listLoading = true
      const response = await listUser(this.queryParams)
      this.userList = response.rows
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
    handleAdd() {
      this.reset()
      getUser().then(response => {
        this.roleOptions = response.roles,
        this.open = true,
        this.title = '添加用户',
        this.form.password = this.initPassword
      })
      // console.log('新增用户')
    },
    submitForm: function() {
      this.$refs['form'].validate(valid => {
        if(valid){
          if(this.form.userId !== undefined){
            updateUser(this.form).then(response => {
              if(response.code === 200){
                this.msgSuccess('修改成功')
                this.open = false
                this.getList()
              }else {
                this.msgError(response.msg)
              }
            })
          }else {
            addUser(this.form).then(response => {
              if(response.code === 200){
                this.msgSuccess('新增成功')
                this.open = false
                this.getList()
              }else {
                this.msgError(response.msg)
              }
            })
          }
        }
      })
    },
    cancel() {
      this.open = false,
      this.reset()
    },
    handleUpdate(row) {
      this.reset()
      const userId = row.userId || this.ids
      getUser(userId).then(response => {
        console.log(response);

        this.form = { ...this.form, ...response.data }
        this.roleOptions = response.roles
        this.form.roleIds = response.roleIds.map(id => Number(id))
        this.open = true
        this.title = '修改用户'
      })
      console.log('修改用户', row)
    },
    handleDelete(row) {
      const userIds = row.userId || this.ids
      this.$confirm('是否确认删除用户编号为'+ userIds + '的数据项？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
       }).then(function() {
        return delUser(userIds)
       }).then(() => {
        this.getList()
        this.msgSuccess('删除成功')
       }).catch(function() {})
      // console.log('删除用户', row)
    },
    handleStatusChange(row) {
  //       const text = row.status === '0' ? '启用' : '停用'

  // this.$confirm(`确认要 ${text} 用户 "${row.userName}" 吗？`, '提示', {
  //   confirmButtonText: '确定',
  //   cancelButtonText: '取消',
  //   type: 'warning'
  // }).then(() => {

  //   // 只更新状态和用户ID即可
  //   const updateData = {
  //     userId: row.userId,
  //     status: row.status
  //   }

  //   updateUser(updateData).then(response => {
  //     if (response.code === 200) {
  //       this.msgSuccess(`${text}成功`)
  //     } else {
  //       this.msgError(response.msg)
  //     }
  //   })

  // }).catch(() => {
  //   // 如果取消，则还原之前的开关状态
  //   row.status = row.status === '0' ? '1' : '0'
  // })
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    reset() {
      this.form = {
        userId: undefined,
        nickName: undefined,
        userName: undefined,
        password: null,
        email: undefined,
        sex: undefined,
        status: undefined,
        phonenumber: undefined,
        remark: undefined,
        roleIds: []
      }
      this.resetForm('form')
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
