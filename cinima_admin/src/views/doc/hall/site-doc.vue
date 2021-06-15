<template>
  <div class="app-container">
    <div class="filter-container" style="margin: 0;padding: 0">
      <div class="el-form-item__content" style="display: block;margin: 0;padding: 0">
        <span class=" el-form-item__label">当前选择影院:</span>
        <span class=" el-form-item__label" style="color: deepskyblue">
          {{ cinema }}
        </span>
      </div>
      <span class=" el-form-item__label">行:</span>
      <el-input v-model="tempRow" placeholder="行" label="行" style="width: 200px; margin-right: 10px" class="filter-item" @keyup.enter.native="changeSiteNumber" />
      <span class="filter-item el-form-item__label" style="float: none">列:</span>
      <el-input v-model="tempCol" placeholder="列" label="列" style="width: 200px;" class="filter-item" @keyup.enter.native="changeSiteNumber" />
      <el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-refresh-left" @click="rebuildSiteMap">
        重新生成座位
      </el-button>
      <span class="filter-item el-form-item__label" style="float: none;margin-left: 10px">当前选择设置:</span>
      <el-select v-model="selectType" placeholder="座位类型" class="filter-item" style="width: 130px;margin-left: 10px">
        <el-option label="普通单人座" :value="normal" />
        <el-option label="情侣座" :value="lovers" />
        <el-option label="过道" :value="gd" />
        <el-option label="维修" :value="bad" />
      </el-select>
      <el-button class="filter-item" style="margin-left: 10px;" type="success" icon="el-icon-document-checked" @click="uploadSiteData">
        保存
      </el-button>
    </div>
    <div class="screen">
      <div class="screen-text">屏幕方向</div>
    </div>
    <div class="site_map_content">
      <div v-for="index in row" :key="index">
        <div class="site_layout">
          <div class="number" style="margin-right: 20px">{{ index }}</div>
          <div v-for="col_index in col" :key="col_index" style="width: 46px">
            <el-image v-model="map[index-1][col_index-1].src" :src="map[index-1][col_index-1].src" alt="" height="46px" width="46px" style="margin-top: 20px" @click="resetSite(index-1,col_index-1)" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { loadSite, uploadSite } from '@/api/doc_hall'

export default {
  name: 'SiteDoc',
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
      cinema: null,
      hall: null,
      row: 0, // 行
      col: 0, // 列
      normal: '/wx.png',
      lovers: '/qlz_01.png',
      lovers_r: '/qlz_02.png',
      bad: '/gz.png',
      gd: '/gd.png',
      tempRow: 0,
      tempCol: 0,
      map: [[]],
      selectType: null,
      loading: null
    }
  },
  created() {
    this.cinema = this.$route.query.cinema
    this.hall = this.$route.query
    this.loadSiteData()
  },
  methods: {
    loadSiteData() {
      this.setLoading(true)
      loadSite(this.hall.id).then(res => {
        if (res.data !== null && res.data.length > 0 && res.data[0].length > 0) {
          // 非首次加载
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
                src: src
              })
            }
            map.push(item)
          }
          this.map = map
          this.row = row
          this.col = col
          this.tempCol = col
          this.tempRow = row
        } else {
          this.$message({
            message: '此放映厅未设置位置',
            type: 'warning'
          })
          this.row = 0
          this.col = 0
          this.tempCol = 0
          this.tempRow = 0
          this.initMap(this.row, this.col)
        }
        this.selectType = this.normal
        this.setLoading(false)
      }).catch(e => {
        this.setLoading(false)
        throw e
      })
    },
    initMap(row, col) {
      this.map = []
      for (let i = 0; i < row; i++) {
        const arr = []
        for (let j = 0; j < col; j++) {
          arr.push({
            src: this.normal
          })
        }
        this.map.push(arr)
      }
    },
    changeSiteNumber() {

    },
    rebuildSiteMap() {
      this.initMap(this.tempRow, this.tempCol)
      this.col = Number(this.tempCol)
      this.row = Number(this.tempRow)
    },
    resetSite(row, col) {
      if (this.map[row][col].src === this.selectType) { return }
      if (this.selectType === this.lovers) {
        if (col === this.col - 1) {
          this.$message.error('情侣座需要右边有空余位置')
          return
        } else if (this.map[row][col + 1].src !== this.normal) {
          this.$message.error('情侣座需要两个位置，但此座位右边不为普通座')
          return
        } else if (this.map[row][col].src !== this.normal) {
          this.$message.error('当前选择位置不为普通座')
          return
        }
        this.map[row][col + 1].src = this.lovers_r
      } else {
        if (this.map[row][col].src === this.lovers) {
          this.map[row][col + 1].src = this.selectType
        } else if (this.map[row][col].src === this.lovers_r) {
          this.map[row][col - 1].src = this.selectType
        }
      }
      this.map[row][col].src = this.selectType
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
        this.loading.close()
      }
    },
    uploadSiteData() {
      this.setLoading(true)
      const upData = []
      for (const i in this.map) {
        const dataItem = []
        for (const j in this.map[i]) {
          const src = this.map[i][j].src
          let type
          switch (src) {
            case this.normal:
              type = 1
              break
            case this.bad:
              type = -1
              break
            case this.gd:
              type = 0
              break
            case this.lovers:
              type = 2
              break
            case this.lovers_r:
              type = 3
              break
          }
          dataItem.push({
            type: type
          })
        }
        upData.push(dataItem)
      }
      uploadSite({
        sites: upData,
        hallId: this.hall.id
      }).then(res => {
        this.setLoading(false)
        this.$notify({
          title: '成功',
          message: '更新成功',
          type: 'success',
          duration: 2000
        })
      }).catch(e => {
        this.setLoading(false)
        throw e
      })
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
    margin: 0;
    border: 0;
    font-size: 100%;
    font-weight: 400;
    background-color: rgba(0,0,0,.3);
    vertical-align: baseline;
    padding: 0 .05rem;
    height: 40px;
    width: 40px;
    margin-top: 20px;
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
</style>
