<template>
  <div class="app-container">
    <div class="filter-container" style="margin: 0;padding: 0">
      <div class="el-form-item__content" style="display: block;margin: 0;padding: 0">
        <span class=" el-form-item__label">影院影院:</span>
        <span v-if="cinema" class=" el-form-item__label" style="color: deepskyblue">
          {{ cinema.name + ' ('+cinema.position + ' - '+ cinema.address + ' )' }}
        </span>
        <span v-else class=" el-form-item__label" style="color: deepskyblue">
          暂未选择影院
        </span>
        <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-edit" @click="choseCinema">
          选择影院
        </el-button>
        <span class="filter-item el-form-item__label" style="float: none">影片:</span>
        <span v-if="film" class="filter-item el-form-item__label" style="color: deepskyblue;float: none">
          {{ film.name }}
        </span>
        <span v-else class="filter-item el-form-item__label" style="color: deepskyblue;float: none">
          暂未选择影片
        </span>
        <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-edit" @click="choseFilm">
          选择影片
        </el-button>
        <span v-if="cinema" class="filter-item el-form-item__label" style="float: none">放映厅:</span>
        <el-select v-if="cinema" v-model="hall" placeholder="放映厅" style="width: 130px;margin-right: 10px" class="filter-item" @change="changeHall">
          <el-option label="" value="" />
          <el-option v-for="item in halls" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
        <span v-if="hall" class="filter-item el-form-item__label" style=";float: none">
          初始化票价
        </span>
        <el-input v-if="hall" v-model="initPrice" type="number" placeholder="价格" style="width: 200px; margin-right: 10px" class="filter-item" />
        <el-button v-if="hall" class="filter-item" type="primary" icon="el-icon-edit" @click="initAllPrice">
          一键初始化
        </el-button>
      </div>
      <div v-if="hall!==null && cinema !==null && film!==null && hall !==''" class="filter-container">
        <span class=" el-form-item__label">上映时间配置:</span>
        <el-date-picker
          v-model="showBegin"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 250px;margin-right: 10px"
          class="filter-item"
          type="datetime"
          placeholder="上映时间"
        />
        <el-date-picker
          v-model="showEnd"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 250px;margin-right: 10px"
          class="filter-item"
          type="datetime"
          placeholder="上映结束时间"
        />
        <el-button class="filter-item" type="primary" style="margin-right: 10px" icon="el-icon-document-checked" @click="submitSchedule">
          排片发布
        </el-button>
      </div>
      <div class="filter-container">
        <span class="filter-item el-form-item__label" style="float: none">单人座:</span><el-image :src="normal" alt="" height="40px" width="40px" class="filter-item" style="width: 40px;height: 40px;margin-right: 10px" />
        <span class="filter-item el-form-item__label" style="float: none">情侣座:</span><el-image :src="lovers" alt="" height="40px" width="40px" class="filter-item" style="width: 40px;height: 40px" />
        <el-image :src="lovers_r" alt="" height="40px" width="40px" style="width: 40px;height: 40px;margin-right: 10px" class="filter-item" />
        <span class="filter-item el-form-item__label" style="float: none">维修:</span><el-image :src="bad" alt="" class="filter-item" height="40px" width="40px" style="width: 40px;height: 40px;margin-right: 10px" />
      </div>
    </div>
    <div class="screen">
      <div class="screen-text">屏幕方向</div>
    </div>
    <div class="site_map_content">
      <div v-for="index in row" :key="index">
        <div class="site_layout">
          <div class="number" style="margin-right: 20px">{{ index }}</div>
          <div v-for="col_index in col" :key="col_index" style="width: 46px">
            <el-popover
              v-if="map[index-1][col_index-1].type>=1 && map[index-1][col_index-1].type<=3"
              placement="top-start"
              title="详情"
              width="100"
              trigger="click"
            >
              <el-checkbox v-if="map[index-1][col_index-1].type>=1 && map[index-1][col_index-1].type<=3" v-model="map[index-1][col_index-1].enable" @change="changeSiteStatus(index-1,col_index-1)">是否可用</el-checkbox>
              <el-input v-model="map[index-1][col_index-1].price" placeholder="请输入票价" type="number" />
              <el-image slot="reference" v-model="map[index-1][col_index-1].src" :src="map[index-1][col_index-1].src" alt="" height="40px" width="40px" style="margin-top: 20px" />
            </el-popover>
            <el-image v-else slot="reference" v-model="map[index-1][col_index-1].src" :src="map[index-1][col_index-1].src" alt="" height="40px" width="40px" style="margin-top: 20px" />
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="选择影院" :visible.sync="cinemaDialogVisible" width="80%">
      <div class="filter-container">
        <el-input v-model="cinemaListQuery.id" placeholder="电影院编号" style="width: 200px;" class="filter-item" @keyup.enter.native="getCinemaList" />
        <el-input v-model="cinemaListQuery.name" placeholder="电影院名" style="width: 200px;margin-left: 10px" class="filter-item" @keyup.enter.native="getCinemaList" />
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
        <el-select v-model="cinemaListQuery.status" placeholder="是否启用" clearable class="filter-item" style="width: 130px;margin-left: 10px">
          <el-option label="" value="" />
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="getCinemaList">
          搜索
        </el-button>
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-refresh-left" @click="resetCinemaQuery">
          重置
        </el-button>
      </div>
      <el-table
        :key="cinemaTableKey"
        v-loading="cinemaListLoading"
        :data="cinemaList"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        @current-change="onSelectCinema"
      >
        <el-table-column label="编号" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="影院名" align="center" width="150">
          <template slot-scope="{row}">
            <span>{{ row.name }}</span>
          </template>
        </el-table-column>
        <el-table-column label="地址" align="center">
          <template slot-scope="{row}">
            <span>{{ row.position + ' - ' + row.address }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="'是否可用'" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="row.status | statusFilter">
              {{ row.status===1?'可用':'禁用' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="cinemaListTotal>0" :total="cinemaListTotal" :page.sync="cinemaListQuery.page" :limit.sync="cinemaListQuery.pageSize" @pagination="getCinemaList" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="cinemaDialogVisible = false">
          取消
        </el-button>
        <el-button v-if="tempSelectCinema" type="primary" @click="dialogChoseCinema">
          确定
        </el-button>
      </div>
    </el-dialog>
    <el-dialog title="选择影片" :visible.sync="filmDialogVisible" width="80%">
      <div class="filter-container">
        <el-input v-model="filmListQuery.id" placeholder="电影编号" style="width: 200px;" class="filter-item" @keyup.enter.native="getCinemaList" />
        <el-input v-model="filmListQuery.name" placeholder="电影名" style="width: 200px;margin-left: 10px" class="filter-item" @keyup.enter.native="getCinemaList" />
        <el-select v-model="filmListQuery.status" placeholder="是否上线" clearable class="filter-item" style="width: 130px;margin-left: 10px">
          <el-option label="" value="" />
          <el-option label="启用" value="1" />
          <el-option label="禁用" value="0" />
        </el-select>
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-search" @click="getFilmList">
          搜索
        </el-button>
        <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-refresh-left" @click="resetFilmListQuery">
          重置
        </el-button>
      </div>
      <el-table
        :key="filmTableId"
        v-loading="filmListLoading"
        :data="filmList"
        border
        fit
        highlight-current-row
        style="width: 100%"
        max-height="400"
        @current-change="onSelectFilm"
      >
        <el-table-column label="编号" align="center" width="100">
          <template slot-scope="{row}">
            <span>{{ row.id }}</span>
          </template>
        </el-table-column>
        <el-table-column label="电影名" width="150px" align="center">
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
        <el-table-column label="简介" align="center">
          <template slot-scope="{row}">
            <span class="autoHidden">{{ row.describe }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="'是否上线'" class-name="status-col" width="100">
          <template slot-scope="{row}">
            <el-tag :type="row.status | statusFilter">
              {{ row.status===1?'上线':'下线' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <pagination v-show="filmListTotal>0" :total="filmListTotal" :page.sync="filmListQuery.page" :limit.sync="filmListQuery.pageSize" @pagination="getFilmList" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="filmDialogVisible = false">
          取消
        </el-button>
        <el-button v-if="tempSelectFilm" type="primary" @click="dialogChoseFilm">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { loadSite } from '@/api/doc_hall'
import { getCinemaList } from '@/api/doc-cinema'
import { getCities, getCountries, getProvinces } from '@/api/position'
import Pagination from '@/components/Pagination'
import { getAllSimpleHall } from '@/api/doc_hall'
import { getFilmList } from '@/api/doc-film'
import { getResourceUrl } from '@/api/static'
import { uploadScheduleData } from '@/api/schedule-film'

export default {
  name: 'Schedule',
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
      cinemaTableKey: 1,
      filmTableId: 2,
      normal: '/wx.png',
      lovers: '/qlz_01.png',
      lovers_r: '/qlz_02.png',
      bad: '/gz.png',
      gd: '/gd.png',
      normal_disabled: '/normal_jz.png',
      lovers_disabled: '/qlz_jz_01.png',
      lovers_r_disabled: '/qlz_jz_02.png',
      tempRow: 0,
      tempCol: 0,
      map: [[]],
      loading: null,
      col: 0,
      row: 0,
      cinema: null,
      halls: null,
      hall: null,
      film: null,
      cinemaDialogVisible: false,
      hallDialogVisible: false,
      cinemaListLoading: false,
      cinemaList: [],
      cinemaListQuery: {
        page: 1,
        pageSize: 10,
        status: null,
        province: null,
        name: null,
        city: null,
        country: null,
        id: null
      },
      provinces: [],
      cities: [],
      countries: [],
      allCities: [],
      allCountries: [],
      selectProvince: null,
      selectCity: null,
      selectCountry: null,
      cinemaListTotal: 0,
      tempSelectCinema: null,
      initPrice: null,
      filmDialogVisible: false,
      filmListQuery: {
        page: 1,
        pageSize: 10,
        status: null,
        name: null,
        id: null
      },
      filmListTotal: 0,
      filmList: [],
      filmListLoading: false,
      tempSelectFilm: null,
      showBegin: null,
      showEnd: null
    }
  },
  watch: {
    windowHeight(val) {
      console.log(val)
    }
  },
  created() {
    this.getSelectData()
  },
  methods: {
    loadSiteData() {
      this.setLoading(true)
      loadSite(this.hall).then(res => {
        if (res.data !== null && res.data.length > 0 && res.data[0].length > 0) {
          const row = res.data.length
          const col = res.data[0].length
          const map = []
          for (const i in res.data) {
            const item = []
            for (const j in res.data[i]) {
              let src
              switch (res.data[i][j].type) {
                case 1:
                  src = this.normal
                  break
                case -1:
                  src = this.bad
                  break
                case 0:
                  src = this.gd
                  break
                case 2:
                  src = this.lovers
                  break
                case 3:
                  src = this.lovers_r
                  break
              }
              item.push({
                src: src,
                sale: false,
                price: null,
                enable: true,
                type: res.data[i][j].type
              })
            }
            map.push(item)
          }
          this.map = map
          this.row = row
          this.col = col
        } else {
          this.$message({
            message: '加载失败,请检查此影厅是否已初始化',
            type: 'warning'
          })
          this.row = 0
          this.col = 0
          this.hall = null
        }
        this.setLoading(false)
      })
        .catch(e => {
          this.setLoading(false)
          this.col = 0
          this.row = 0
          throw e
        })
    },
    setLoading(show) {
      if (show) {
        this.loading = this.$loading({
          lock: true,
          text: 'Loading',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })
      } else {
        setTimeout(() => {
          this.loading.close()
        }, 500)
      }
    },
    dialogChoseCinema() {
      this.cinema = this.tempSelectCinema
      this.cinemaDialogVisible = false
      this.setLoading(true)
      getAllSimpleHall(this.cinema.id).then(res => {
        this.halls = res.data
        this.setLoading(false)
      }).catch(_ => {
        this.setLoading(false)
        throw _
      })
    },
    choseCinema() {
      this.resetCinemaQuery()
      this.tempSelectCinema = null
      this.cinemaDialogVisible = true
      this.hall = null
      this.changeHall()
      this.getCinemaList()
    },
    getCinemaList() {
      this.cinemaListLoading = true
      getCinemaList(this.cinemaListQuery).then(res => {
        this.cinemaList = res.data.list
        this.cinemaListTotal = res.data.total
        this.cinemaListLoading = false
      }).catch(_ => {
        this.cinemaListLoading = false
        throw _
      })
    },
    resetCinemaQuery() {
      this.cinlistQuery = {
        page: 1,
        pageSize: 10,
        status: null,
        province: null,
        name: null,
        city: null,
        country: null,
        id: null
      }
      this.selectProvince = null
      this.selectCountry = null
      this.selectCity = null
      // 获取列表
      this.getCinemaList()
    },
    onSelectChanged(type) {
      switch (type) {
        case 0:
          this.countries = []
          this.cinemaListQuery.city = null
          this.cinemaListQuery.country = null
          this.selectCountry = null
          this.selectCity = null
          this.cinemaListQuery.province = this.selectProvince.name
          this.cities = this.allCities.filter(value => {
            return value.pid === this.selectProvince.id
          })
          break
        case 1:
          this.cinemaListQuery.country = null
          this.selectCountry = null
          this.cinemaListQuery.city = this.selectCity.name
          this.countries = this.allCountries.filter(value => {
            return value.pid === this.selectCity.id
          })
          break
        case 2:
          this.cinemaListQuery.country = this.selectCountry.name
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
    onSelectCinema(currentRow, oldCurrentRow) {
      this.tempSelectCinema = currentRow
    },
    choseFilm() {
      this.resetFilmListQuery()
      this.tempSelectFilm = null
      this.filmDialogVisible = true
      this.getFilmList()
    },
    changeHall() {
      if (this.hall === null || this.hall === '') {
        this.col = 0
        this.row = 0
      } else {
        this.loadSiteData()
      }
    },
    changeSiteStatus(row_index, col_index) {
      const data = this.map[row_index][col_index]
      if (data.type === 2) {
        this.map[row_index][col_index + 1].enable = data.enable
        this.map[row_index][col_index + 1].src = this.getSrc(this.map[row_index][col_index + 1])
      } else if (data.type === 3) {
        this.map[row_index][col_index - 1].enable = data.enable
        this.map[row_index][col_index - 1].src = this.getSrc(this.map[row_index][col_index - 1])
      }
      data.src = this.getSrc(data)
    },
    getSrc(row) {
      let src
      switch (row.type) {
        case 1:
          src = row.enable ? this.normal : this.normal_disabled
          break
        case 2:
          src = row.enable ? this.lovers : this.lovers_disabled
          break
        case 3:
          src = row.enable ? this.lovers_r : this.lovers_r_disabled
          break
      }
      return src
    },
    initAllPrice() {
      if (this.initPrice === null || this.initPrice === '') {
        this.$message.warning('请输入价格')
        return
      }
      if (this.hall === null || this.map === null || this.map.length === 0) {
        this.$message.warning('当前放映厅错误!')
        return
      }
      for (const i in this.map) {
        for (const j in this.map[i]) {
          const item = this.map[i][j]
          if (item.enable && item.type >= 1 && item.type <= 3) {
            this.map[i][j].price = this.initPrice
          }
        }
      }
      this.$message.success('初始化成功!')
    },
    resetFilmListQuery() {
      this.filmListQuery = {
        page: 1,
        pageSize: 10,
        status: null,
        name: null,
        id: null
      }
    },
    getFilmList() {
      this.filmListLoading = true
      getFilmList(this.filmListQuery).then(res => {
        this.filmList = res.data.list
        this.filmListTotal = res.data.total
        this.filmListLoading = false
      }).catch(_ => {
        this.filmListLoading = false
        throw _
      })
    },
    dialogChoseFilm() {
      this.film = this.tempSelectFilm
      this.filmDialogVisible = false
    },
    onSelectFilm(currentRow, oldCurrentRow) {
      this.tempSelectFilm = currentRow
    },
    submitSchedule() {
      const map = this.map
      for (const i in map) {
        for (const j in map[i]) {
          const x = Number(i) + 1
          const y = Number(j) + 1
          const item = map[i][j]
          if (!item.enable || item.type < 1 || item.type > 3) { continue }
          if (item.price === undefined || item.price === null) {
            this.$message.error('请设置第' + x + '排,第' + y + '列座位的价格!')
            return
          }
          const price = item.price + ''
          if (price.match('^\\+?[1-9][0-9]*$ ')) {
            this.$message.error('请设置第' + x + '排,第' + y + '列价格有误!')
            return
          }
        }
      }
      const upData = {
        cinemaId: this.cinema.id,
        filmId: this.film.id,
        hallId: this.hall,
        sites: this.map,
        beginTime: this.showBegin,
        endTime: this.showEnd
      }
      this.setLoading(true)
      uploadScheduleData(upData).then(_ => {
        this.$message.success('发布成功')
        this.setLoading(false)
        this.resetAll()
      }).catch(_ => {
        this.setLoading(false)
      })
    },
    resetAll() {
      this.hall = null
      this.cinema = null
      this.film = null
      this.map = []
      this.initPrice = null
      this.beginTime = null
      this.endTime = null
      this.col = 0
      this.row = 0
    }
  }
}
</script>

<style scoped>
  .screen{
    width: 90%;
    height: 50px;
    background-color: #f3f4f6;
    margin: 0 auto;
  }
  .screen-text{
    color: #6BA2D6;
    line-height: 50px;
    text-align: center;
    white-space: nowrap;
    font-size: 20px;
  }
  .site_map_content{
    width: 90%;
    margin: 30px auto 0;
  }
  .number{
    text-align: center;
    color: #fff;
    border: 0;
    font-size: 100%;
    font-weight: 400;
    background-color: rgba(0,0,0,.3);
    vertical-align: baseline;
    padding: 0 .05rem;
    height: 40px;
    width: 40px;
    margin: 20px 0 0;
    line-height: 40px;
  }
  .site_layout{
    display: flex;
    flex: 1;
    margin: 0 auto;
    justify-content: center;
  }
  .number{
    width: 40px;
  }
  .mb{
    -webkit-filter: grayscale(1); /* Webkit */
    filter: grayscale(1); /* W3C */
  }
  .autoHidden{
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 5;
    overflow: hidden;
    text-overflow: ellipsis;
  }
</style>
