<template>
  <div class="app-container">
    <div class="filter-container" style="margin: 0;padding: 0">
      <div class="el-form-item__content" style="display: block;margin: 0;padding: 0">
        <span class=" el-form-item__label">当前选择影院:</span>
        <span class=" el-form-item__label" style="color: deepskyblue">
          {{ nowCinema.name }} ({{ nowCinema.position+nowCinema.address }})
        </span>
      </div>
      <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>

      <el-input v-model="listQuery.name" placeholder="影厅名" style="width: 200px;" class="filter-item" @keyup.enter.native="getList" />
      <el-select v-model="listQuery.status" placeholder="是否启用" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option label="" value="" />
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
      <el-table-column label="序号" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="影厅名" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="座位" width="170px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.nonNull | statusFilter">
            {{ row.nonNull===0?'未排位':'已排位' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="影厅类型" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.type }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createUser }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新时间" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.updateTime }}</span>
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
          <el-button size="mini" type="info" @click="handleEditSite(row)">
            排位
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
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="80px" style="width: 80%; margin-left:10%;">
        <el-form-item label="影厅名" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="影厅类型" prop="type">
          <el-input v-model="temp.type" placeholder="IMAX,激光..等类型" />
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
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
import Pagination from '@/components/Pagination'
import { getHallList, updateHall, createHall, deleteHall } from '@/api/doc_hall'
// import Pagination from '@/components/Pagination' // secondary package based on el-pagination
export default {
  name: 'HallDoc',
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
        cinemaId: null,
        page: 1,
        pageSize: 10,
        status: null,
        name: ''
      },
      dialogVisible: false,
      dialogType: null,
      temp: {
        id: undefined,
        name: '',
        type: '',
        status: '',
        position: ''
      },
      rules: {
        name: [{ required: true, message: '请填入影院名', trigger: 'change' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        type: [{ required: true, message: '请输入类型', trigger: 'change' }]
      },
      nowCinema: {}
    }
  },
  created() {
    this.nowCinema = this.$route.query
    this.listQuery.cinemaId = this.nowCinema.id
    this.getList()
  },
  methods: {
    handleFilter() {
    },
    getList() {
      this.listLoading = true
      getHallList(this.listQuery).then(res => {
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
          deleteHall(row.id).then(_ => {
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
    handleEditSite(row) {
      const tempData = Object.assign({}, row)
      tempData['cinema'] = this.nowCinema.name + this.nowCinema.position + this.nowCinema.address
      this.$router.push({ name: 'SiteDoc', query: tempData })
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
          createHall(tempData).then(() => {
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
          updateHall(tempData).then(() => {
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
        status: null,
        name: ''
      }
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: '',
        type: '',
        status: '',
        cinemaId: this.nowCinema.id
      }
    }
  }
}
</script>

<style scoped>

</style>
