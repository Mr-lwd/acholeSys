<template>
  <v-container style="height: 100%">
    <v-row style="height: 50%;border: 2px dashed grey">
      <v-col align-self="center" style="width: 50%">
        <v-card class="mx-auto flat" outlined width="400">
          <v-list-item three-line>
            <v-list-item-content>
              <div class="text-h3 mb-10">
                {{ userDetail.name }}
              </div>
              <v-list-item-title class="orange--text mb-3">
                <!--                Property: {{ userDetail.property }}(USD结算)-->
              </v-list-item-title>
              <v-list-item-title class="blue--text mb-3">
                Email: {{ userDetail.email }}
              </v-list-item-title>
              <v-list-item-subtitle>Phone: {{ userDetail.phone }}</v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
          <v-card-actions>
            <v-btn
                :large="dynamicWidth > 400" color="primary"
                depressed
                rounded
                size="10"
                @click="openDetails"
            >
              修改信息
            </v-btn>
          </v-card-actions>
          <v-divider></v-divider>
        </v-card>
      </v-col>
      <v-col v-if="dynamicWidth > 400" style="width: 50%">
        <div v-if="dynamicWidth > 400" id="main2" style="height: 100%"></div>
      </v-col>
    </v-row>
    <v-row style="height: 50%;">
      <v-col style="width: 50%">
        <div id="main1" style="height: 100%;"></div>
      </v-col>
      <v-col style="width: 50%">
        <div id="main3" style="height: 100%;"></div>
      </v-col>
    </v-row>

    <v-dialog v-model="changeDetail" width="400">
      <v-card>
        <v-tabs>
          <v-tab>用户信息</v-tab>
          <v-tab>密码修改</v-tab>
          <v-tab-item>
            <v-form
                v-if="formData"
                ref="detailForm"
                class="pa-5"
                lazy-validation
            >
              <v-text-field
                  v-model="formData.name"
                  :counter="10"
                  :rules="nameRules"
                  label="用户名"
                  required
              ></v-text-field>

              <v-text-field
                  v-model="formData.email"
                  :rules="emailRules"
                  disabled
                  label="邮箱"
                  required
              ></v-text-field>

              <v-text-field
                  v-model="formData.role"
                  :rules="roleRules"
                  disabled
                  label="角色"
                  required
              ></v-text-field>

              <v-text-field
                  v-model="formData.phone"
                  :counter="11"
                  :rules="phoneRules"
                  label="电话号码"
                  required
              ></v-text-field>
              <v-btn
                  class="mr-4"
                  color="info"
                  @click="reset"
              >
                重 置
              </v-btn>
              <v-btn
                  color="success"
                  @click.prevent="save"
              >
                保 存
              </v-btn>
            </v-form>
          </v-tab-item>
          <v-tab-item>
            <v-form
                v-if="formData"
                ref="passwordForm"
                class="pa-5"
                lazy-validation
            >
              <v-text-field
                  v-model="oldPasswd"
                  :rules="passwordRules"
                  label="旧密码"
                  required
                  type="password"
              ></v-text-field>
              <v-text-field
                  v-model="newPasswd"
                  :rules="passwordRules"
                  label="新密码"
                  required
                  type="password"
              ></v-text-field>
              <v-btn
                  color="success"
                  @click.prevent="savePass"
              >
                保 存
              </v-btn>
            </v-form>
          </v-tab-item>
        </v-tabs>
      </v-card>
    </v-dialog>
    <!--    <userChats></userChats>-->
  </v-container>
</template>

<script>
import * as echarts from "echarts/core";
import {GridComponent, LegendComponent, TitleComponent, TooltipComponent} from 'echarts/components';
import {BarChart, PieChart} from 'echarts/charts';
import {LabelLayout} from 'echarts/features';
import {CanvasRenderer} from 'echarts/renderers';
import {mapActions, mapState} from "vuex";
import indexcharts from "@/components/main/indexcharts";


echarts.use([
  TitleComponent,
  TooltipComponent,
  LegendComponent,
  PieChart,
  CanvasRenderer,
  LabelLayout,
  GridComponent,
  BarChart,
]);


export default {
  name: "userdetails",
  components: {
    indexcharts
  },
  data: () => ({
        changeDetail: false,
        // userDetail: '',
        formData: '',
        chartDom: "",
        myChart1: "",
        myChart2: "",
        myChart3: "",
        option: "",
        chartData1: [],
        chartData2: [],
        chartData3: [],
        nameRules: [
          v => !!v || '用户名必填',
          v => (v && v.length <= 10) || '用户名不能超过10个字符',
        ],
        emailRules: [
          v => !!v || '邮箱必填',
          v => /.+@.+\..+/.test(v) || '邮箱必须符合格式',
        ],
        roleRules: [
          v => !!v || '角色必填',
        ],
        phoneRules: [
          v => !!v || '电话号码必填',
          v => (v && v.length <= 11) || '电话无法超过11个字符',
        ],
        oldPasswd: '',
        newPasswd: '',
        passwordRules: [
          v => !!v || '密码是必须项',
          v => (v && v.length > 0) || '请输入密码',
        ],
        investDialog: false,
        tabChart: 0,
        addInvestDialog: false,
        amount: 0,
        sorts: [
          {
            id: 0,
            kind: "BTC"
          },
          {
            id: 1,
            kind: "ETH"
          }, {
            id: 2,
            kind: "DASH"
          },
        ],
        select: 0,
        coins: null,
        sellDialog: false,
        selectCoin: {
          name: '未选择',
          value: 0,
          moneyid: null,
        },
        sellCount: 0,//提现数量
      }
  ),
  computed: {
    ...mapState('User', ['user']),
    userDetail() {
      return this.user
    },
    dynamicWidth() {
      switch (this.$vuetify.breakpoint.name) {
        case 'xs':
          return 220
        case 'sm':
          return 360
        case 'md':
          return 500
        case 'lg':
          return 600
        case 'xl':
          return 800
      }
    },
  }
  ,
  created() {
    // this.userDetail = this.user
  }
  ,
  mounted() {
    this.crypoget()
  }
  ,
  methods: {
    ...
        mapActions('User', ['saveDetails', 'logout', 'savePasswd', 'refreshDetails']),
    openDetails() {
      this.formData = JSON.parse(JSON.stringify((this.userDetail)))
      this.changeDetail = true
    }
    ,
    async crypoget() {
      await this.$request.get(`/loan/userGetLoanCounts/${this.user.id}`).then(response => {
        // console.log(response)
        var chartDom = document.getElementById('main1');
        var myChart1 = echarts.init(chartDom);
        var option;

        const data = JSON.parse(JSON.stringify(response.data))

        this.chartData1 = [
          {value: data["loaned"], name: '已借款'},
          {value: data["loaning"], name: '借款中'},
          {value: data["loanrepay"], name: '已还款'},
          {value: data["loanexpire"], name: '已逾期'}
        ]

        // console.log(this.chartData1)
        option = {
          title: {
            text: '贷款分布',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              data: this.chartData1,
              name: '订单数',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 30
                }
              },
              labelLine: {
                show: false
              },
            }
          ]
        };
        option && myChart1.setOption(option);
      }).catch(err => {
        console.log(err)
      })
      await this.$request.get(`/loan/userGetAllLoan/${this.user.id}`).then(response => {
        // console.log(response)
        var chartDom = document.getElementById('main2');
        var myChart2 = echarts.init(chartDom);
        var option;

        const data = JSON.parse(JSON.stringify(response.data))

        this.chartData2 = [
          {value: data["loaning"], name: '借款中'},
          {value: data["loaned"], name: '已借款'},
          {value: data["repay"], name: '已还款'},
        ]
        // console.log(this.chartData2)
        option = {
          title: {
            text: '贷款金额',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: ['借款中', '已借款', '已还款'],
            axisTick: {
              alignWithLabel: true
            }
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              data: this.chartData2,
              //name: 'ALLMONEY',
              type: 'bar',
              barWidth: '40',
              //radius: '50%',
              itemStyle: {
                barBorderRadius: [0, 50, 50, 0],// 统一设置四个角的圆角大小
                normal: {
                  //这里是重点
                  color: function (params) {
                    //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
                    //var colorList = ['#00F4FF', '#0092FF', '#0061FE', '#002099', '#000682'];
                    var colorList = ['rgba(30,144,255,1)', 'rgb(76,168,51)', 'rgb(255,191,66)'];
                    return colorList[params.dataIndex]
                  }
                }
              },
            }
          ]
        };
        option && myChart2.setOption(option);
      }).catch(err => {
        console.log(err)
      })
      await this.$request.get(`/virtualSaving/userGetCoins/${this.user.id}`).then(response => {
        // console.log(response)
        var chartDom = document.getElementById('main3');
        var myChart3 = echarts.init(chartDom);
        var option;

        const data = JSON.parse(JSON.stringify(response.data))


        for (let i = 0; i < data.length; i++) {
          this.chartData3.push(
              {value: data[i].amount, name: data[i].moneyname, moneyid: data[i].moneyid}
          )
        }

        // this.chartData3 = [
        //   {value: data["BTC"], name: 'BTC'},
        //   {value: data["ETH"], name: 'ETH'},
        //   {value: data["DASH"], name: 'DASH'}
        // ]

        // console.log(this.chartData1)
        option = {
          title: {
            text: '虚拟货币持有',
            left: 'center'
          },
          tooltip: {
            trigger: 'item'
          },
          legend: {
            top: '5%',
            left: 'center'
          },
          series: [
            {
              data: this.chartData3,
              name: '持有总数',
              type: 'pie',
              radius: ['40%', '70%'],
              avoidLabelOverlap: false,
              itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
              },
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: 30,
                  //fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
            }
          ]
        };
        option && myChart3.setOption(option);
      }).catch(err => {
        console.log(err)
      })
    }
    ,
    save() {
      if (this.$refs.detailForm.validate()) {
        if (JSON.stringify(this.formData) == JSON.stringify(this.userDetail)) {
          this.changeDetail = false
        } else {
          let form = new FormData()
          const data = {
            id: this.formData.id,
            name: this.formData.name,
            email: this.formData.email,
            phone: this.formData.phone,
          }
          for (const key in data) {
            form.append(key, data[key]);
          }
          this.saveDetails(form).then(
              res => {
                if (res) {
                  this.changeDetail = false
                  this.$notify({
                    title: '请重新登录！',
                    type: 'info'
                  })
                  setTimeout(() => {
                    this.logout()
                  }, 500)
                } else {
                  this.$notify({
                    title: '更新失败！',
                    type: 'error'
                  })
                }
              }
          ).catch(error => {
            // 处理异步操作的错误
            console.log('数据更新失败：', error)
          });
        }
      } else {
        this.$notify({
          title: '表项错误',
          type: 'error'
        });
      }
    }
    ,
    savePass() {
      if (this.$refs.passwordForm.validate()) {
        this.savePasswd({
          id: this.userDetail.id,
          oldPasswd: this.oldPasswd,
          newPasswd: this.newPasswd
        }).then(
            res => {
              if (res) {
                this.changeDetail = false
                this.$notify({
                  title: '请重新登录！',
                  type: 'info'
                })
                setTimeout(() => {
                  this.logout()
                }, 500)
              } else {
                this.$notify({
                  title: '更新失败！',
                  type: 'error'
                })
              }
            }
        ).catch(error => {
          // 处理异步操作的错误
          console.log('数据更新失败：', error)
        });
      } else {
        this.$notify({
          title: '表格错误',
          type: 'error'
        });
      }
    }
    ,
    reset() {
      this.formData = JSON.parse(JSON.stringify((this.userDetail)))
    },
    sendInvest() {
      if (this.amount == 0 || this.amount == undefined) {
        this.$notify(
            {
              title: '投资金额错误',
              type: 'error'
            }
        )
      } else {
        this.addInvestDialog = false
        setTimeout(() => {
          this.investDialog = false
        }, 500)
        const form = new FormData()
        this.$request.get('https://min-api.cryptocompare.com/data/price', {
          params: {
            fsym: this.sorts[this.select].kind,
            tsyms: 'USD',
            api_key: '086c7bbfe76ba2a27be3820b3e59ca74d79392757cb1be99139c3d83ac9b05b7',
          }
        }).then(res => {
          // console.log(res)
          if (res['USD']) {
            const data = {
              userid: this.user.id,
              moneyid: this.select,
              amount: this.amount,
              rate: res['USD']
            }
            // console.log(data)
            for (let key in data) {
              form.append(key, data[key])
            }
            this.$request.post('/invest/add', form).then(
                res => {
                  // console.log(res)
                  if (res.code == 200) {
                    this.refreshDetails()
                    this.reInitCharts()
                    this.$notify({
                      title: '投资成功',
                      type: 'success'
                    })
                  } else {
                    this.$notify({
                      title: res.msg,
                      type: 'error'
                    })
                  }
                }
            ).catch(err => {
              console.log(err)
            })
          }
        })
      }
    },
    clearHander() {
      // 清空当前实例，会移除实例中所有的组件和图表。
      let myChart1 = echarts.init(document.getElementById('main1'));
      let myChart2 = echarts.init(document.getElementById('main2'));
      let myChart3 = echarts.init(document.getElementById('main3'));
      myChart1.clear()
      myChart1.dispose()
      myChart2.clear()
      myChart2.dispose()
      myChart3.clear()
      myChart3.dispose()
      this.chartData1 = []
      this.chartData2 = []
      this.chartData3 = []
    },
    reInitCharts() {
      this.clearHander()
      // console.log(sort)
      this.crypoget()
    },
    sellCoins() {
      if (this.selectCoin.moneyid == null || this.sellCount <= 0 ||
          this.sellCount == undefined || this.sellCount == null) {
        this.$notify({
          title: '提现信息错误!',
          type: 'error'
        })
      } else {
        this.$request.get('https://min-api.cryptocompare.com/data/price', {
          params: {
            fsym: this.selectCoin.name,
            tsyms: 'USD',
            api_key: '086c7bbfe76ba2a27be3820b3e59ca74d79392757cb1be99139c3d83ac9b05b7',
          }
        }).then(res => {
              if (res['USD']) {
                const form = new FormData()
                const temp = {
                  userid: this.user.id,
                  amount: this.sellCount,
                  rate: res['USD'],
                  moneyid: this.selectCoin.moneyid
                }
                for (let key in temp) {
                  form.append(key, temp[key])
                }
                this.$request.post('/sold', form)
                    .then(res => {
                      if (res.code == 200) {
                        // console.log(res)
                        this.$notify({
                          title: '提现成功!',
                          type: 'success'
                        })
                      } else {
                        this.$notify({
                          title: '提现失败!',
                          type: 'error'
                        })
                      }
                      this.sellDialog = false
                      this.refreshDetails()
                      this.reInitCharts()
                    })
                    .catch(err => {
                      console.log(err)
                    })
              } else {
                this.$notify({
                  title: '调用接口!',
                  type: 'error'
                })
              }
            }
        ).catch(err => {
          console.log(err)
        })
      }
    }
  }
  ,
  watch: {
    tabChart: {
      handler(newVal, oldVal) {
        this.$refs.indexChart.reInitCharts(newVal)
      },
    }
  }
  ,
}
</script>
