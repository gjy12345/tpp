<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="用户名或账号" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getList" />
      <el-input v-model="listQuery.phone" placeholder="电话" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getList" />
      <el-select v-model="listQuery.sex" placeholder="性别" clearable class="filter-item" style="width: 120px;margin-right: 10px">
        <el-option label="未选择" value="" />
        <el-option label="男" value="1" />
        <el-option label="女" value="0" />
      </el-select>
      <el-select v-model="listQuery.locked" placeholder="是否启用" clearable class="filter-item" style="width: 120px;margin-right: 10px">
        <el-option label="未选择" value="" />
        <el-option label="启用" value="1" />
        <el-option label="禁用" value="0" />
      </el-select>
      <el-date-picker
        v-model="listQuery.begin"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 250px;margin-right: 10px"
        class="filter-item"
        type="datetime"
        placeholder="注册时间"
      />
      <el-date-picker
        v-model="listQuery.end"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 250px;margin-right: 10px"
        class="filter-item"
        type="datetime"
        placeholder="注册时间尾"
      />
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="getList">
        搜索
      </el-button>
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-refresh-left" @click="resetQueryParams">
        重置
      </el-button>
    </div>
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="序号" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="头像" min-width="150px" align="center">
        <template slot-scope="{row}">
          <el-popover
            placement="top-start"
            title="详情"
            width="350"
            trigger="hover"
          >
            <el-image :src="row.avatar|concatUrl" width="100%" />
            <el-image slot="reference" :src="row.avatar|concatUrl" style="max-width: 60px" />
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="手机号" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="昵称" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.nickname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="性别" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.sex === 1?'男':'女' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生日" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.birthday }}</span>
        </template>
      </el-table-column>
      <el-table-column label="上一次登录" width="170px">
        <template slot-scope="{row}">
          <span>{{ row.lastLoginTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" width="170px">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="是否可用" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.locked | lockedFilter">
            {{ row.locked===0?'可用':'禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button type="waining" size="mini" @click="handleResetPwd(row)">
            重置密码
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.pageSize" @pagination="getList" />
    <!--  新增  修改-->
    <el-dialog :title="dialogType==='create'?'新增':'修改'" :visible.sync="dialogVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="头像">
          <el-upload
            class="avatar-uploader"
            :action="uploadResourceUrl"
            :headers="uploadHeader"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
          >
            <img v-if="temp.avatar!=null" :src="temp.avatar|concatUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon el-upload" />
          </el-upload>
        </el-form-item>
        <el-form-item label="昵称" prop="nickname">
          <el-input v-model="temp.nickname" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="temp.phone" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="temp.sex" class="filter-item" placeholder="Please select">
            <el-option label="男" :value="1" />
            <el-option label="女" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-select v-model="temp.locked" class="filter-item" placeholder="Please select">
            <el-option label="启用" :value="1" />
            <el-option label="禁用" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogType==='create'?createData():updateData()">
          确定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="重置密码" :visible.sync="resetPwdDialogVisible">
      <el-form ref="resetPwdForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="密码" prop="resetPwd">
          <el-input v-model="temp.resetPwd" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="resetPwdDialogVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="resetPwd">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getResourceUrl } from '@/api/static'
import Pagination from '@/components/Pagination'
import { deleteCustomer, getCustomerList, resetPassword, updateCustomer } from '@/api/customer'
import { getToken } from '@/utils/auth'
// import Pagination from '@/components/Pagination' // secondary package based on el-pagination
export default {
  name: 'UserManage',
  components: { Pagination },
  filters: {
    lockedFilter(status) {
      const lockedFilter = {
        0: 'success',
        1: 'danger'
      }
      return lockedFilter[status]
    },
    concatUrl(url) {
      return getResourceUrl(url)
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      uploadResourceUrl: process.env.VUE_APP_BASE_API + '/common/file/uploadImages',
      uploadHeader: {
        'Token': getToken()
      },
      listQuery: {
        page: 1,
        pageSize: 10,
        name: null,
        locked: null,
        sex: null,
        phone: null,
        begin: null,
        end: null
      },
      dialogVisible: false,
      dialogType: null,
      temp: {
        id: undefined,
        reason: '',
        type: '',
        status: ''
      },
      rules: {
        nickname: [{ required: true, message: '请填入昵称', trigger: 'change' }],
        phone: [{ required: true, message: '请填入手机号', trigger: 'change' }],
        resetPwd: [{ required: true, message: '请填入密码', trigger: 'change' }],
        locked: [{ required: true, message: '请选择状态', trigger: 'change' }],
        sex: [{ required: true, message: '请选择性别', trigger: 'change' }]
      },
      resetPwdDialogVisible: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleFilter() {
    },
    getList() {
      this.listLoading = true
      getCustomerList(this.listQuery).then(res => {
        this.list = res.data.list
        this.total = res.data.total
        this.listLoading = false
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogType = 'update'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除？')
        .then(_ => {
          deleteCustomer(row.id).then(_ => {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.list.splice(index, 1)
          })
        })
        .catch(_ => {})
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateCustomer(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.list.splice(index, 1, this.temp)
            this.dialogVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    resetQueryParams() {
      this.listQuery = {
        page: 1,
        pageSize: 10,
        name: null,
        locked: null,
        sex: null,
        phone: null,
        begin: null,
        end: null
      }
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: null,
        locked: null,
        sex: null,
        phone: null,
        begin: null,
        end: null
      }
    },
    handleResetPwd(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.resetPwdDialogVisible = true
      this.$nextTick(() => {
        this.$refs['resetPwdForm'].clearValidate()
      })
    },
    resetPwd() {
      this.$refs['resetPwdForm'].validate((valid) => {
        if (valid) {
          resetPassword(this.temp.id, this.temp.resetPwd).then(() => {
            this.resetPwdDialogVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleCoverSuccess(res, file) {
      if (res.code === 20000) {
        this.temp.avatar = res.data
      } else {
        this.$notify({
          title: '失败',
          message: res.msg | '上传失败',
          type: 'success',
          duration: 2000
        })
      }
    },
    beforeCoverUpload(file) {
      const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/gif'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    }
  }
}
</script>

<style scoped>
.avatar-uploader .el-upload {
  border: 1px dashed black;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  display: block;
}
</style>
