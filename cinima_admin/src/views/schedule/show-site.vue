<template>
  <div class="app-container">
    <div class="filter-container" style="margin: 0;padding: 0">
      <div class="el-form-item__content" style="display: block;margin: 0;padding: 0">
        <span class=" el-form-item__label">影院影院:</span>
        <span class=" el-form-item__label" style="color: deepskyblue">
          {{ info.cinemaPosition + ' - '+info.cinemaName + ' - '+ info.hallName }}
        </span>
      </div>
      <div class="filter-container">
        <span class="filter-item el-form-item__label" style="float: none">已售:</span><el-image :src="sale" alt="" height="40px" width="40px" class="filter-item" style="width: 40px;height: 40px;margin-right: 10px" />
        <span class="filter-item el-form-item__label" style="float: none">单人座:</span><el-image :src="normal" alt="" height="40px" width="40px" class="filter-item" style="width: 40px;height: 40px;margin-right: 10px" />
        <span class="filter-item el-form-item__label" style="float: none">情侣座:</span><el-image :src="lovers" alt="" height="40px" width="40px" class="filter-item" style="width: 40px;height: 40px" />
        <el-image :src="lovers_r" alt="" height="40px" width="40px" style="width: 40px;height: 40px;margin-right: 10px" class="filter-item" />
        <span class="filter-item el-form-item__label" style="float: none">维修:</span><el-image :src="bad" alt="" class="filter-item" height="40px" width="40px" style="width: 40px;height: 40px;margin-right: 10px" />
        <span class="filter-item el-form-item__label" style="float: none">不允许购买:</span><el-image :src="normal_disabled" alt="" class="filter-item" height="40px" width="40px" style="width: 40px;height: 40px;margin-right: 10px" />
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
              v-if="map[index-1][col_index-1].type>=1 && map[index-1][col_index-1].type<=3 && map[index-1][col_index-1].enable"
              placement="top-start"
              title="票价"
              width="100"
              trigger="hover"
              :content="map[index-1][col_index-1].price + '￥'"
            >
              <el-image slot="reference" v-model="map[index-1][col_index-1].src" :src="map[index-1][col_index-1].src" alt="" height="40px" width="40px" style="margin-top: 20px" />
            </el-popover>
            <el-image v-else slot="reference" v-model="map[index-1][col_index-1].src" :src="map[index-1][col_index-1].src" alt="" height="40px" width="40px" style="margin-top: 20px" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { loadSite } from '@/api/schedule-film'

export default {
  name: 'ShowSite',
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
      info: null,
      normal: '/wx.png',
      lovers: '/qlz_01.png',
      lovers_r: '/qlz_02.png',
      bad: '/gz.png',
      gd: '/gd.png',
      sale: '/ys.png',
      yx_lovers: '/yx_qlz_01.png',
      yx_lovers_r: '/yx_qlz_02.png',
      normal_disabled: '/normal_jz.png',
      lovers_disabled: '/qlz_jz_01.png',
      lovers_r_disabled: '/qlz_jz_02.png',
      tempRow: 0,
      tempCol: 0,
      map: [[]],
      loading: null,
      col: 0,
      row: 0
    }
  },
  created() {
    this.info = this.$route.query
    this.loadSiteData()
  },
  methods: {
    loadSiteData() {
      this.setLoading(true)
      loadSite(this.info.id).then(res => {
        if (res.data !== null && res.data.length > 0 && res.data[0].length > 0) {
          const row = res.data.length
          const col = res.data[0].length
          const map = []
          for (const i in res.data) {
            const item = []
            for (const j in res.data[i]) {
              let src, sale_image
              let disabled
              switch (res.data[i][j].type) {
                case 1:
                  src = this.normal
                  sale_image = this.sale
                  disabled = this.normal_disabled
                  break
                case -1:
                  src = this.bad
                  break
                case 0:
                  src = this.gd
                  break
                case 2:
                  src = this.lovers
                  sale_image = this.yx_lovers
                  disabled = this.lovers_disabled
                  break
                case 3:
                  src = this.lovers_r
                  sale_image = this.yx_lovers_r
                  disabled = this.lovers_r_disabled
                  break
              }
              if (res.data[i][j].sale) {
                src = sale_image
              } else if (!res.data[i][j].enable) {
                src = disabled
              }
              item.push({
                src: src,
                sale: res.data[i][j].sale,
                price: res.data[i][j].price,
                type: res.data[i][j].type,
                enable: res.data[i][j].enable
              })
            }
            map.push(item)
          }
          this.map = map
          this.row = row
          this.col = col
        } else {
          this.$message({
            message: '加载失败',
            type: 'warning'
          })
        }
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
    showPrice(row) {
      console.log(row)
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
