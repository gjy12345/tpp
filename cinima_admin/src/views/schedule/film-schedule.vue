<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-edit" @click="handleCreate">
        添加排片
      </el-button>
      <el-input v-model="listQuery.cinemaName" placeholder="影院名" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getList" />
      <el-input v-model="listQuery.filmName" placeholder="电影名" style="width: 200px;" class="filter-item" @keyup.enter.native="getList" />
      <el-date-picker
        v-model="listQuery.begin"
        type="datetime"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 200px;margin-left: 10px"
        class="filter-item"
        placeholder="放映日期起始时间"
      />
      <el-date-picker
        v-model="listQuery.end"
        type="datetime"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 200px;margin-left: 10px"
        class="filter-item"
        placeholder="放映日期结尾时间"
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
      <el-table-column label="电影名" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.filmName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="影厅" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.hallName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="影院" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.cinemaName }} <br> ({{ row.cinemaPosition }})</span>
        </template>
      </el-table-column>
      <el-table-column label="放映时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.beginTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.endTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="座位情况" width="170px" align="center">
        <template slot-scope="{row}">
          <span>共计:&nbsp;{{ row.totalSite }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="160px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建人" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createUser }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handlerShowSites(row)">
            查看座位
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { deleteScheduleFilm, getScheduleFilmList } from '@/api/schedule-film'
// import Pagination from '@/components/Pagination' // secondary package based on el-pagination
export default {
  name: 'FilmSchedule',
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
        filmName: null,
        cinemaName: null,
        begin: null,
        end: null
      },
      dialogVisible: false,
      dialogType: null,
      temp: {
        id: undefined,
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
    getList() {
      this.listLoading = true
      getScheduleFilmList(this.listQuery).then(res => {
        this.list = res.data.list
        this.total = res.data.total
        this.listLoading = false
      })
    },
    handlerShowSites(row) {
      this.$router.push({
        name: 'ShowSite',
        query: row
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除，删除将对所有已购买订单进行退款处理？')
        .then(_ => {
          deleteScheduleFilm(row.id).then(_ => {
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
      this.$router.push({
        name: 'Schedule'
      })
    },
    resetQueryParams() {
      this.listQuery = {
        page: 1,
        pageSize: 10,
        filmName: null,
        cinemaName: null,
        begin: null,
        end: null
      }
      this.getList()
    }
  }
}
</script>

<style scoped>

</style>
