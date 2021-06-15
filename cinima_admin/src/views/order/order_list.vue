<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.phone" placeholder="用户账号" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getList" />
      <el-input v-model="listQuery.filmName" placeholder="电影名" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getList" />
      <el-select v-model="listQuery.status" placeholder="订单状态" clearable class="filter-item" style="width: 120px;margin-right: 10px">
        <el-option label="未选择" value="" />
        <el-option label="未支付" value="0" />
        <el-option label="已支付" value="1" />
        <el-option label="已退款" value="-1" />
        <el-option label="已过期" value="-2" />
      </el-select>
      <el-date-picker
        v-model="listQuery.begin"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 250px;margin-right: 10px"
        class="filter-item"
        type="datetime"
        placeholder="下单时间"
      />
      <el-date-picker
        v-model="listQuery.end"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 250px;margin-right: 10px"
        class="filter-item"
        type="datetime"
        placeholder="下单时间尾"
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
      <el-table-column label="订单编号" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.orderNum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电影名" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.filmName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单金额" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.price/100 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户账号" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户昵称" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.customerName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" min-width="150px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusTagFilter">
            <span>{{ row.status | statusFilter }}</span>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下单时间" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="支付时间" width="170px">
        <template slot-scope="{row}">
          <span>{{ row.payTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button v-if="row.status===1||row.status===2" type="danger" size="mini" @click="handleReturn(row,$index)">
            退款
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.pageSize" @pagination="getList" />
  </div>
</template>

<script>
import { getResourceUrl } from '@/api/static'
import Pagination from '@/components/Pagination'
import { getToken } from '@/utils/auth'
import { getOrderList, returnMoney } from '@/api/order'
// import Pagination from '@/components/Pagination' // secondary package based on el-pagination
export default {
  name: 'OrderManage',
  components: { Pagination },
  filters: {
    statusTagFilter(status) {
      const lockedFilter = {
        '-2': 'danger',
        '-1': 'info',
        '0': 'warning',
        '1': 'success',
        '2': ''
      }
      return lockedFilter[status]
    },
    concatUrl(url) {
      return getResourceUrl(url)
    },
    statusFilter(status) {
      const statusFilter = {
        '-2': '超时自动取消',
        '-1': '已退单',
        '0': '未支付',
        '1': '已支付',
        '2': '已使用'
      }
      return statusFilter[status + '']
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
      getOrderList(this.listQuery).then(res => {
        this.list = res.data.list
        this.total = res.data.total
        this.listLoading = false
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
    handleReturn(row, index) {
      this.$confirm('确认对此订单进行退款处理？')
        .then(_ => {
          returnMoney(row.id).then(_ => {
            this.$notify({
              title: '成功',
              message: '退款成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        })
        .catch(_ => {})
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
