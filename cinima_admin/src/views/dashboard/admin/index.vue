<template>
  <div class="dashboard-editor-container">

    <panel-group :day-in-come="dayIncome" :day-new-comment="dayCommon" :day-order="dayOrder" :day-new-customer="dayCustomer" @handleSetLineChartData="handleSetLineChartData" />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <line-chart :chart-data="lineChartData" :line-des="lineDes" />
    </el-row>

    <el-row style="text-align: center;font-size: medium">
      <span class="el-breadcrumb__inner">电影院选票系统v1.0 @copyright 2021</span>
    </el-row>
  </div>
</template>

<script>
import PanelGroup from './components/PanelGroup'
import LineChart from './components/LineChart'
import { getDashData } from '@/api/dashboard'

const lineChartData = {
  dayNewCustomer: {
    actualData: [1, 2, 2]
  },
  dayNewComment: {
    actualData: []
  },
  dayInCome: {
    actualData: []
  },
  dayOrder: {
    actualData: []
  }
}

const lineType = {
  dayNewCustomer: '新用户',
  dayNewComment: '新评论',
  dayInCome: '新增收入',
  dayOrder: '新增订单'
}

export default {
  name: 'DashboardAdmin',
  components: {
    PanelGroup,
    LineChart
  },
  data() {
    return {
      lineChartData: lineChartData.dayNewCustomer,
      lineDes: lineType.dayNewCustomer,
      dayCustomer: 0,
      dayCommon: 0,
      dayIncome: 0,
      dayOrder: 0
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type]
      this.lineDes = lineType[type]
      console.log((this.lineDes))
    },
    loadData() {
      getDashData().then(res => {
        const data = res.data
        lineChartData.dayNewCustomer.actualData = data[0].graphData
        this.dayCustomer = data[0].showCount
        lineChartData.dayNewComment.actualData = data[1].graphData
        this.dayCommon = data[1].showCount
        for (let i = 0; i < data[2].graphData.length; i++) {
          data[2].graphData[i] = data[2].graphData[i] / 100
        }
        lineChartData.dayInCome.actualData = data[2].graphData
        this.dayIncome = data[2].showCount / 100
        lineChartData.dayOrder.actualData = data[3].graphData
        this.dayOrder = data[3].showCount
        console.log(data)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .github-corner {
    position: absolute;
    top: 0px;
    border: 0;
    right: 0;
  }

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
