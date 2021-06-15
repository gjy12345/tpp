<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
      <el-input v-model="listQuery.name" placeholder="电影名" style="width: 200px;" class="filter-item" @keyup.enter.native="getList" />
      <el-date-picker
        v-model="listQuery.begin"
        type="datetime"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 200px;margin-left: 10px"
        class="filter-item"
        placeholder="上映日期起始时间"
      />
      <el-date-picker
        v-model="listQuery.end"
        type="datetime"
        value-format="yyyy-MM-dd HH:mm:ss"
        style="width: 200px;margin-left: 10px"
        class="filter-item"
        placeholder="上映日期结尾时间"
      />
      <el-select v-model="listQuery.status" placeholder="是否上线" clearable class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option label="" value="" />
        <el-option label="上线" value="1" />
        <el-option label="下线" value="0" />
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
      <el-table-column label="电影名" min-width="150px" align="center">
        <template slot-scope="{row}">
          <img
            v-if="row.cover"
            :src="row.cover|concatUrl"
            style="width: 80%;display: block;margin: 0 auto"
          >
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="导演" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.director }}</span>
        </template>
      </el-table-column>
      <el-table-column label="国家/地区" width="100px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.region }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时长" width="80px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.duration }}</span>
        </template>
      </el-table-column>
      <el-table-column label="主演" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.star }}</span>
        </template>
      </el-table-column>
      <el-table-column label="语言" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lang }}</span>
        </template>
      </el-table-column>
      <el-table-column label="权重" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.weight }}</span>
        </template>
      </el-table-column>
      <el-table-column label="评分" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.score }}</span>
        </template>
      </el-table-column>
      <el-table-column label="类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag
            v-for="tag in (row.filmTypes==null?[]:row.filmTypes)"
            :key="tag.id"
            style="margin-top: 4px"
          >
            {{ tag.name }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="上映时间" width="120px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.showTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="简介" width="170px" align="center">
        <template slot-scope="{row}">
          <el-popover
            placement="top-start"
            title="简介"
            width="300"
            trigger="hover"
            :content="row.describe"
          >
            <span slot="reference" class="autoHidden">{{ row.describe }}</span>
          </el-popover>
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
      <el-table-column label="是否上线" class-name="status-col" width="80">
        <template slot-scope="{row}">
          <el-tag :type="row.status | statusFilter">
            {{ row.status===1?'上线':'下线' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column :label="'操作'" align="center" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" style="margin-top: 10px;margin-left: 5px" @click="handleUpdate(row)">
            编辑
          </el-button>
          <!--          <el-button size="mini" type="info" style="margin-top: 10px" @click="handleDelete(row,$index)">-->
          <!--            排片-->
          <!--          </el-button>-->
          <el-button size="mini" type="danger" style="margin-top: 10px" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.pageSize" @pagination="getList" />
    <!--  新增  修改-->
    <el-dialog :title="dialogType==='create'?'新增':'修改'" :visible.sync="dialogVisible">
      <el-form ref="dataForm" :rules="rules" :model="temp" label-position="right" label-width="85px" style="width: 80%; margin-left:10%;">
        <el-form-item label="电影名" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="电影封面">
          <el-upload
            class="avatar-uploader"
            :action="uploadResourceUrl"
            :headers="uploadHeader"
            :show-file-list="false"
            :on-success="handleCoverSuccess"
            :before-upload="beforeCoverUpload"
          >
            <img v-if="temp.cover!=null" :src="temp.cover|concatUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon el-upload" />
          </el-upload>
        </el-form-item>
        <el-form-item label="导演" prop="director">
          <el-input v-model="temp.director" />
        </el-form-item>
        <el-form-item label="国家/地区" prop="region">
          <el-input v-model="temp.region" />
        </el-form-item>
        <el-form-item label="语言" prop="lang">
          <el-input v-model="temp.lang" />
        </el-form-item>
        <el-form-item label="评分">
          <el-input v-model="temp.score" />
        </el-form-item>
        <el-form-item label="权重" prop="weight">
          <el-input v-model="temp.weight" />
        </el-form-item>
        <el-form-item label="时长" prop="duration">
          <el-input v-model="temp.duration" />
        </el-form-item>
        <el-form-item label="主演" prop="star">
          <el-input v-model="temp.star" type="textarea" autosize />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="temp.type" multiple placeholder="请选择" style="width: 100%">
            <el-option
              v-for="item in allFilmTypes"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="上映时间" prop="showTime">
          <el-date-picker
            v-model="temp.showTime"
            value-format="yyyy-MM-dd HH:mm:ss"
            type="datetime"
            placeholder="选择时间"
          />
        </el-form-item>
        <el-form-item label="简介" prop="describe">
          <el-input v-model="temp.describe" type="textarea" autosize />
        </el-form-item>
        <el-form-item label="是否上线" prop="status">
          <el-select v-model="temp.status" class="filter-item" placeholder="请选择">
            <el-option label="上线" :value="1" />
            <el-option label="下线" :value="0" />
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
import { getResourceUrl } from '@/api/static'
import { getAllSimpleTypes } from '@/api/doc-film-type'
import { createFilm, deleteFilm, getFilmList, updateFilm } from '@/api/doc-film'
import { getToken } from '@/utils/auth'
// import Pagination from '@/components/Pagination' // secondary package based on el-pagination
export default {
  name: 'FilmDoc',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'danger'
      }
      return statusMap[status]
    },
    concatUrl(u) {
      return getResourceUrl(u)
    }
  },
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,
      allFilmTypes: [],
      uploadResourceUrl: process.env.VUE_APP_BASE_API + '/common/file/uploadImages',
      uploadHeader: {
        'Token': getToken()
      },
      listQuery: {
        page: 1,
        pageSize: 10,
        begin: null,
        end: null,
        name: null,
        status: null
      },
      dialogVisible: false,
      dialogType: null,
      temp: {
        id: undefined,
        type: [],
        cover: null
      },
      rules: {
        name: [{ required: true, message: '请填入类型名', trigger: 'change' }],
        type: [{ required: true, message: '请选择类型', trigger: 'change', type: 'array' }],
        star: [{ required: true, message: '请选择地址', trigger: 'change' }],
        duration: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        region: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        director: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        showTime: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        describe: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        status: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        lang: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        weight: [{ required: true, message: '请填写必填项', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getAllTypes()
    this.getList()
  },
  methods: {
    getAllTypes() {
      getAllSimpleTypes().then(res => {
        this.allFilmTypes = res.data
      })
    },
    handleFilter() {
    },
    getList() {
      this.listLoading = true
      getFilmList(this.listQuery).then(res => {
        this.list = res.data.list
        this.total = res.data.total
        this.listLoading = false
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({
        type: []
      }, row) // copy obj
      this.temp.type = []
      this.dialogType = 'update'
      this.dialogVisible = true
      if (this.temp.filmTypes != null) {
        for (const index in this.temp.filmTypes) {
          this.temp.type.push(this.temp.filmTypes[index].id)
        }
      }
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除？')
        .then(_ => {
          deleteFilm(row.id).then(_ => {
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
      this.tempSCity = null
      this.tempSProvince = null
      this.tempSCountry = null
      this.dialogType = 'create'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          createFilm(tempData).then(() => {
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
          updateFilm(tempData).then(() => {
            const index = this.list.findIndex(v => v.id === this.temp.id)
            this.temp.filmTypes = this.allFilmTypes.filter(value => {
              return this.temp.type.find(v => {
                return v === value.id
              }) !== undefined
            })
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
    handleCoverSuccess(res, file) {
      if (res.code === 20000) {
        this.temp.cover = res.data
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
        this.$message.error('上传封面图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传封面图片大小不能超过 2MB!')
      }
      return isJPG && isLt2M
    },
    resetQueryParams() {
      this.listQuery = {
        page: 1,
        pageSize: 10,
        name: '',
        status: null
      }
      this.getList()
    },
    resetTemp() {
      this.temp.type = []
      this.temp = {
        id: undefined,
        cover: null
      }
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
.autoHidden{
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 8;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
