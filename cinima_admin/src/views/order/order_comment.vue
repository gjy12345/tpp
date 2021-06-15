<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.phone" placeholder="用户账号" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getList" />
      <el-input v-model="listQuery.filmName" placeholder="电影名" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getList" />
      <el-date-picker
        v-model="listQuery.begin"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 250px;margin-right: 10px"
        class="filter-item"
        type="datetime"
        placeholder="评论时间"
      />
      <el-date-picker
        v-model="listQuery.end"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 250px;margin-right: 10px"
        class="filter-item"
        type="datetime"
        placeholder="评论时间尾"
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
      <el-table-column label="用户账号" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.phone }}</span>
        </template>
      </el-table-column>
      <el-table-column label="用户昵称" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.cusName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="评论" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.content }}</span>
        </template>
      </el-table-column>
      <el-table-column label="评分" min-width="150px" align="center">
        <template slot-scope="{row}">
          <el-rate
            :value="row.score"
            disabled
            show-score
            text-color="#ff9900">
          </el-rate>
        </template>
      </el-table-column>
      <el-table-column label="评论时间" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row}">
          <el-button type="danger" size="mini" @click="handleDelete(row,$index)">
            删除
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
import { getOrderCommentList,deleteComment } from '@/api/order_comment'
export default {
  name: 'OrderComment',
  components: { Pagination },
  filters: {
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
      getOrderCommentList(this.listQuery).then(res => {
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
    handleDelete(row, index) {
      this.$confirm('确认删除此条评论？')
        .then(_ => {
          deleteComment(row.id).then(_ => {
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
