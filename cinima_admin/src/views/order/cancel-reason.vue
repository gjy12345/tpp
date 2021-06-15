<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
      <el-input v-model="listQuery.reason" placeholder="理由" style="width: 200px;" class="filter-item" @keyup.enter.native="getList" />
      <el-select v-model="listQuery.status" placeholder="是否启用" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option label="未选择" value="" />
        <el-option label="启用" value="1" />
        <el-option label="禁用" value="0" />
      </el-select>
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
      <el-table-column :label="'序号'" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="'理由'" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.reason }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="'创建时间'" width="170px">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="'更新时间'" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="'创建人'" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createUser }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="'是否可用'" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status===1?'可用':'禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="'操作'" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
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
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="left" label-width="80px" style="width: 400px; margin-left:50px;">
        <el-form-item label="理由" prop="reason">
          <el-input v-model="temp.reason" />
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="Please select">
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
  </div>
</template>

<script>
import { getCancelReasonList, updateReason, deleteReason, createReason } from '@/api/order-reason'
import Pagination from '@/components/Pagination'
// import Pagination from '@/components/Pagination' // secondary package based on el-pagination
export default {
  name: 'CancelReason',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        pageSize: 10,
        reason: '',
        status: null
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
        reason: [{ required: true, message: '请填入退款理由', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      }
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
      getCancelReasonList(this.listQuery).then(res => {
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
          deleteReason(row.id).then(_ => {
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
    handleCreate() {
      this.resetTemp()
      this.dialogType = 'create'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      console.log('createData')
      this.$refs['dataForm'].validate((valid) => {
        console.log(valid)
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          createReason(tempData).then(() => {
            this.dialogVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          updateReason(tempData).then(() => {
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
        reason: '',
        status: null
      }
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        reason: '',
        type: '',
        status: ''
      }
    }
  }
}
</script>

<style scoped>

</style>
