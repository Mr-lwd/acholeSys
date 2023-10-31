<template>
  <div style="height: 100%">
    <v-row style="height: 90%;" >
      <v-col align-self="center"  cols="6">
        <v-card class="mx-auto flat" outlined width="400" style="border-radius: 10px">
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
        </v-card>
      </v-col>
      <v-col align-self="center" cols="6">
        <div  id="main1" style="height: 500px"></div>
      </v-col>
    </v-row>
<!--    <v-row style="height: 50%;">-->
<!--      <v-col style="width: 50%">-->
<!--        <div id="main2" style="height: 100%;"></div>-->
<!--      </v-col>-->
<!--      <v-col style="width: 50%">-->
<!--        <div id="main3" style="height: 100%;"></div>-->
<!--      </v-col>-->
<!--    </v-row>-->

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
  </div>
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
        userDetail: '',
        formData: '',
        chartDom: "",
        myChart1: "",
        // myChart2: "",
        // myChart3: "",
        option: "",
        chartData1: [],
        // chartData2: [],
        // chartData3: [],
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
      }
  ),
  computed: {
    ...mapState('User', ['user']),
    // userDetail() {
    //   return this.user
    // },
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
    this.userDetail = this.user
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
    crypoget() {
      this.$request.get(`/calendar/charts/${this.user.id}`).then(response => {
        console.log(response)
        var chartDom = document.getElementById('main1');
        var myChart1 = echarts.init(chartDom);
        var option;

        const data = JSON.parse(JSON.stringify(response.data))

        this.chartData1 = [
          {value: data["drinking0"], name: '未沾一滴'},
          {value: data["drinking1"], name: '少量酒精'},
          {value: data["drinking2"], name: '接近醉酒'},
          {value: data["drinking3"], name: '深度醉酒'}
        ]

        // console.log(this.chartData1)
        option = {
          title: {
            text: '总体图表',
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
              color: [
                '#4CAF50',
                '#2196F3',
                '#EB5E28',
                '#E71D36',
              ],
              name: '情况',
              type: 'pie',
              radius: ['30%', '70%'],
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
              if (res.code == '200') {
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
    clearHander() {
      // 清空当前实例，会移除实例中所有的组件和图表。
      let myChart1 = echarts.init(document.getElementById('main1'));
      // let myChart2 = echarts.init(document.getElementById('main2'));
      // let myChart3 = echarts.init(document.getElementById('main3'));
      myChart1.clear()
      myChart1.dispose()
      // myChart2.clear()
      // myChart2.dispose()
      // myChart3.clear()
      // myChart3.dispose()
      this.chartData1 = []
      // this.chartData2 = []
      // this.chartData3 = []
    },
    reInitCharts() {
      this.clearHander()
      // console.log(sort)
      this.crypoget()
    },
  }
  ,
  watch: {
    // tabChart: {
    //   handler(newVal, oldVal) {
    //     this.$refs.indexChart.reInitCharts(newVal)
    //   },
    // }
  }
  ,
}
</script>
