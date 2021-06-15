<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-edit" @click="handleCreate">
        添加
      </el-button>
      <el-input v-model="listQuery.id" placeholder="电影院编号" style="width: 200px;margin-right: 10px" class="filter-item" @keyup.enter.native="getCinemaList" />
      <el-input v-model="listQuery.name" placeholder="电影院名" style="width: 200px;" class="filter-item" @keyup.enter.native="getList" />
      <el-select v-model="selectProvince" placeholder="省" clearable class="filter-item" style="width: 130px;margin-left: 10px" value-key="id" @change="onSelectChanged(0)">
        <el-option label="" value="" />
        <el-option v-for="item in provinces" :key="item.id" :label="item.name" :value="item" />
      </el-select>
      <el-select v-model="selectCity" placeholder="城市" clearable class="filter-item" style="width: 130px;margin-left: 10px" value-key="id" @change="onSelectChanged(1)">
        <el-option label="" value="" />
        <el-option v-for="item in cities" :key="item.id" :label="item.name" :value="item" />
      </el-select>
      <el-select v-model="selectCountry" placeholder="县" clearable class="filter-item" style="width: 130px;margin-left: 10px" value-key="id" @change="onSelectChanged(2)">
        <el-option label="" value="" />
        <el-option v-for="item in countries" :key="item.id" :label="item.name" :value="item" />
      </el-select>
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
      <el-table-column label="影院名" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="位置" width="170px">
        <template slot-scope="{row}">
          <span>{{ row.position }}</span>
        </template>
      </el-table-column>
      <el-table-column label="地址" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.address }}</span>
        </template>
      </el-table-column>
      <el-table-column label="电话" width="170px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.tel }}</span>
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
      <el-table-column label="服务" width="110px" align="center">
        <template slot-scope="{row}">
          <el-tag
            v-for="tag in (row.services==null?[]:row.services.split(','))"
            :key="tag"
            style="margin-top: 4px"
          >
            {{ tag }}
          </el-tag>
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
          <el-button size="mini" type="info" @click="handleToHall(row,$index)">
            放映厅
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
        <el-form-item label="影院名" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="地址" prop="position">
          <el-input v-if="false" v-model="temp.position" />
          <el-select v-model="tempSProvince" placeholder="省" clearable class="filter-item" style="width: 130px;margin-right: 10px" value-key="id" @change="onTempSelectChange(0)">
            <el-option label="" value="" />
            <el-option v-for="item in provinces" :key="item.id" :label="item.name" :value="item" />
          </el-select>
          <el-select v-model="tempSCity" placeholder="城市" clearable class="filter-item" style="width: 130px;margin-right: 10px" value-key="id" @change="onTempSelectChange(1)">
            <el-option label="" value="" />
            <el-option v-for="item in tempCities" :key="item.id" :label="item.name" :value="item" />
          </el-select>
          <el-select v-model="tempSCountry" placeholder="县" clearable class="filter-item" style="width: 130px;margin-right: 10px" value-key="id" @change="onTempSelectChange(2)">
            <el-option label="" value="" />
            <el-option v-for="item in tempCountries" :key="item.id" :label="item.name" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="temp.address" />
        </el-form-item>
        <el-form-item label="联系电话" prop="tel">
          <el-input v-model="temp.tel" />
        </el-form-item>
        <el-form-item label="服务">
          <el-input v-model="temp.services" placeholder="多个服务逗号分隔" />
        </el-form-item>
        <!--        <el-form-item label="服务" prop="tel">-->
        <!--          <el-input v-model="temp.tel" />-->
        <!--        </el-form-item>-->
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
import { getCities, getCountries, getProvinces } from '@/api/position'
import { createCinema, deleteCinema, getCinemaList, updateCinema } from '@/api/doc-cinema'
// import Pagination from '@/components/Pagination' // secondary package based on el-pagination
export default {
  name: 'CinemaDoc',
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
        status: null,
        province: null,
        name: '',
        city: null,
        country: null,
        id: null
      },
      selectProvince: null,
      selectCity: null,
      selectCountry: null,
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
        address: [{ required: true, message: '请选择地址', trigger: 'change' }],
        tel: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        service: [{ required: true, message: '请填写必填项', trigger: 'change' }],
        position: [{ required: true, message: '请选择位置', trigger: 'change' }]
      },
      provinces: [],
      cities: [],
      countries: [],
      allCities: [],
      allCountries: [],
      tempCities: [],
      tempCountries: [],
      tempSProvince: null,
      tempSCity: null,
      tempSCountry: null
    }
  },
  created() {
    this.getSelectData()
    this.getList()
  },
  methods: {
    onSelectChanged(type) {
      // console.log(type, this.selectProvince, this.selectCity, this.selectCountry)
      switch (type) {
        case 0:
          this.countries = []
          this.listQuery.city = null
          this.listQuery.country = null
          this.selectCountry = null
          this.selectCity = null
          this.listQuery.province = this.selectProvince.name
          this.cities = this.allCities.filter(value => {
            return value.pid === this.selectProvince.id
          })
          break
        case 1:
          this.listQuery.country = null
          this.selectCountry = null
          this.listQuery.city = this.selectCity.name
          this.countries = this.allCountries.filter(value => {
            return value.pid === this.selectCity.id
          })
          break
        case 2:
          this.listQuery.country = this.selectCountry.name
          break
      }
    },
    findPCCByName(name, level) {
      switch (level) {
        case 0:
          return this.provinces.find(value => { return value.name === name })
        case 1:
          console.log(this.tempSProvince)
          this.tempCities = this.allCities.filter(value => { return value.pid === this.tempSProvince.id })
          return this.tempCities
            .find(value => { return value.name === name })
        case 2:
          this.tempCountries = this.allCountries.filter(value => { return value.pid === this.tempSCity.id })
          return this.tempCountries
            .find(value => { return value.name === name })
      }
    },
    onTempSelectChange(type) {
      switch (type) {
        case 0:
          this.tempSCity = null
          this.tempSCountry = null
          this.temp.position = null
          this.tempCities = this.allCities.filter(value => { return value.pid === this.tempSProvince.id })
          break
        case 1:
          this.tempSCountry = null
          this.temp.position = null
          this.tempCountries = this.allCountries.filter(value => { return value.pid === this.tempSCity.id })
          break
        case 2:
          if (this.tempSCountry.name === undefined) {
            this.temp.position = null
            return
          }
          this.temp.position = this.tempSProvince.name + '-' + this.tempSCity.name + '-' + this.tempSCountry.name
          break
      }
    },
    getSelectData() {
      getProvinces().then(res => {
        this.provinces = res.data
      })
      getCountries().then(res => {
        this.allCountries = res.data
      })
      getCities().then(res => {
        this.allCities = res.data
      })
    },
    handleFilter() {
    },
    getList() {
      this.listLoading = true
      getCinemaList(this.listQuery).then(res => {
        this.list = res.data.list
        this.total = res.data.total
        this.listLoading = false
      })
    },
    handleToHall(row) {
      this.$router.push({ name: 'HallDoc', query: row })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      const arr = this.temp.position.split('-')
      if (arr.length === 3) {
        console.log(arr[0])
        this.tempSProvince = this.findPCCByName(arr[0], 0)
        this.tempSCity = this.findPCCByName(arr[1], 1)
        this.tempSCountry = this.findPCCByName(arr[2], 2)
      }
      this.dialogType = 'update'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row, index) {
      this.$confirm('确认删除？')
        .then(_ => {
          deleteCinema(row.id).then(_ => {
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
      console.log('createData')
      this.$refs['dataForm'].validate((valid) => {
        console.log(valid)
        if (valid) {
          const tempData = Object.assign({}, this.temp)
          createCinema(tempData).then(() => {
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
          console.log(tempData)
          updateCinema(tempData).then(() => {
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
        province: null,
        name: '',
        city: null,
        country: null,
        id: null
      }
      this.selectProvince = null
      this.selectCountry = null
      this.selectCity = null
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
