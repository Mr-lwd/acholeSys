<template>
  <v-container>
    <div id="mycharts" style="width: 100%;height: 620px;"></div>
  </v-container>
</template>

<script>
import * as echarts from 'echarts/core';
import {
  DataZoomComponent,
  GridComponent,
  LegendComponent,
  MarkLineComponent,
  MarkPointComponent,
  TitleComponent,
  TooltipComponent
} from 'echarts/components';
import {CandlestickChart, LineChart} from 'echarts/charts';
import {UniversalTransition} from 'echarts/features';
import {CanvasRenderer} from 'echarts/renderers';
import axios from "axios";

echarts.use([
  TitleComponent,
  TooltipComponent,
  GridComponent,
  LegendComponent,
  DataZoomComponent,
  MarkLineComponent,
  MarkPointComponent,
  CandlestickChart,
  LineChart,
  CanvasRenderer,
  UniversalTransition
]);

export default {
  name: "indexcharts",
  data() {
    return {
      cry_data: [],
      data0: "",
      sorts: [
        "BTC",
        "ETH",
        "DASH"
      ]
    }
  },
  methods: {
    async crypoget(sort) {
      await axios.get('https://min-api.cryptocompare.com/data/v2/histoday', {
        params: {
          fsym: sort,
          tsym: 'USD',
          limit: '100',
          // api_key: '086c7bbfe76ba2a27be3820b3e59ca74d79392757cb1be99139c3d83ac9b05b7',
        }
      }).then(response => {
        // console.log("raw data", response.data.Data.Data)
        for (var i = 0; i < response.data.Data.Data.length; i++) {
          this.cry_data.push([this.getDmh(response.data.Data.Data[i].time), response.data.Data.Data[i].open, response.data.Data.Data[i].close, response.data.Data.Data[i].low, response.data.Data.Data[i].high])
        }
        var chartDom = document.getElementById('mycharts');
        var myChart = echarts.init(chartDom);
        var option;

        const upColor = '#ef233c';
        const upBorderColor = '#d90429';
        const downColor = '#70e000';
        const downBorderColor = '#008F28';

        // Each item: open，close，lowest，highest
        this.data0 = this.splitData(this.cry_data);
        option = {
          // title: {
          //   text: '上证指数',
          //   left: 0
          // },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'cross'
            }
          },
          legend: {
            data: ['日K', 'MA5', 'MA10', 'MA20', 'MA30']
          },
          grid: {
            left: '10%',
            right: '10%',
            bottom: '15%'
          },
          xAxis: {
            type: 'category',
            data: this.data0.categoryData,
            boundaryGap: false,
            axisLine: {onZero: false},
            splitLine: {show: false},
            min: 'dataMin',
            max: 'dataMax'
          },
          yAxis: {
            scale: true,
            splitArea: {
              show: true
            }
          },
          dataZoom: [
            {
              type: 'inside',
              start: 50,
              end: 100
            },
            {
              show: true,
              type: 'slider',
              top: '90%',
              start: 50,
              end: 100
            }
          ],
          series: [
            {
              name: '日K',
              type: 'candlestick',
              data: this.data0.values,
              itemStyle: {
                color: upColor,
                color0: downColor,
                borderColor: upBorderColor,
                borderColor0: downBorderColor
              },
            },
            {
              name: 'MA5',
              type: 'line',
              data: this.calculateMA(5),
              smooth: true,
              lineStyle: {
                opacity: 0.5
              }
            },
            {
              name: 'MA10',
              type: 'line',
              data: this.calculateMA(10),
              smooth: true,
              lineStyle: {
                opacity: 0.5
              }
            },
            {
              name: 'MA20',
              type: 'line',
              data: this.calculateMA(20),
              smooth: true,
              lineStyle: {
                opacity: 0.5
              }
            },
            {
              name: 'MA30',
              type: 'line',
              data: this.calculateMA(30),
              smooth: true,
              lineStyle: {
                opacity: 0.5
              }
            }
          ]
        };
        option && myChart.setOption(option);
        // console.log("array data",cry_data)
      })
          .catch(error => {
            console.log(error)
          });
    },
    getDmh(seconds) {
      seconds = seconds * 1000
      var date1 = new Date(seconds).toLocaleDateString();//获取当前系统时间
      // console.log(date1)
      return date1
    },
    splitData(rawData) {
      const categoryData = [];
      const values = [];
      for (var i = 0; i < rawData.length; i++) {
        categoryData.push(rawData[i].splice(0, 1)[0]);
        values.push(rawData[i]);
      }
      return {
        categoryData: categoryData,
        values: values
      };
    },
    calculateMA(dayCount) {
      var result = [];
      for (var i = 0, len = this.data0.values.length; i < len; i++) {
        if (i < dayCount) {
          result.push('-');
          continue;
        }
        var sum = 0;
        for (var j = 0; j < dayCount; j++) {
          sum += +this.data0.values[i - j][1];
        }
        result.push((sum / dayCount).toFixed(2));
      }
      return result;
    },
    clearHander(){
      // 清空当前实例，会移除实例中所有的组件和图表。
      let myChart = echarts.init(document.getElementById('mycharts'));
      this.cry_data=[],
      this.data0="",
      myChart.clear()
      myChart.dispose()
    },
    reInitCharts(newVal) {
      this.clearHander()
      const sort = this.sorts[newVal]
      // console.log(sort)
      this.crypoget(sort)
    }
  },
  created() {
    this.crypoget(this.sorts[0])

  },
  mounted() {
    // Scrollbar.init(document.querySelector('.my-scrollbar'))
  },
}
</script>

<style scoped>

</style>
