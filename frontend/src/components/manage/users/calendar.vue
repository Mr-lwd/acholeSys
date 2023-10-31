<template>
  <div>
    <v-row class="fill-height ma-1" style="height: 100%">
      <v-col style="height: 100%">
        <v-sheet height="10%">
          <v-toolbar
              flat
          >
            <v-btn
                class="mr-4"
                color="grey darken-2"
                outlined
                @click="setToday"
            >
              今 日
            </v-btn>
            <v-btn
                color="grey darken-2"
                fab
                small
                text
                @click="prev"
            >
              <v-icon small>
                mdi-chevron-left
              </v-icon>
            </v-btn>
            <v-btn
                color="grey darken-2"
                fab
                small
                text
                @click="next"
            >
              <v-icon small>
                mdi-chevron-right
              </v-icon>
            </v-btn>
            <v-toolbar-title v-if="$refs.calendar">
              {{ $refs.calendar.title }}
            </v-toolbar-title>
            <v-spacer></v-spacer>
            <v-menu
                bottom
                right
            >
              <template v-slot:activator="{ on, attrs }">
                <v-btn
                    color="grey darken-2"
                    outlined
                    v-bind="attrs"
                    v-on="on"
                >
                  <span>{{ typeToLabel[type] }}</span>
                  <v-icon right>
                    mdi-menu-down
                  </v-icon>
                </v-btn>
              </template>
              <v-list>
                <v-list-item @click="type = 'day'">
                  <v-list-item-title>Day</v-list-item-title>
                </v-list-item>
                <v-list-item @click="type = 'week'">
                  <v-list-item-title>Week</v-list-item-title>
                </v-list-item>
                <v-list-item @click="type = 'month'">
                  <v-list-item-title>Month</v-list-item-title>
                </v-list-item>
                <v-list-item @click="type = '4day'">
                  <v-list-item-title>4 days</v-list-item-title>
                </v-list-item>
              </v-list>
            </v-menu>
          </v-toolbar>
        </v-sheet>
        <v-sheet height="90%">
          <v-calendar
              ref="calendar"
              v-model="focus"
              :event-color="getEventColor"
              :events="events"
              :type="type"
              color="primary"
              @click:event="showEvent"
              @click:more="viewDay"
              @click:date="viewDay"
          ></v-calendar>
          <v-menu
              v-model="selectedOpen"
              :activator="selectedElement"
              :close-on-content-click="false"
              offset-x
          >
            <v-card
                color="grey lighten-4"
                flat
                min-width="300px"
            >
              <v-toolbar
                  :color="selectedEvent.color"
                  dark
              >
                <v-btn icon @click="openEditDialog">
                  <v-icon>mdi-pencil</v-icon>
                </v-btn>
                <v-toolbar-title v-html="selectedEvent.name"></v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <span v-html="selectedEvent.details"></span>
              </v-card-text>
              <v-card-actions>
<!--                <v-btn-->
<!--                    color="success"-->
<!--                    text-->
<!--                    @click="saveTime"-->
<!--                >-->
<!--                  保 存-->
<!--                </v-btn>-->
                <v-btn
                    color="secondary"
                    text
                    @click="selectedOpen = false"
                >
                  关 闭
                </v-btn>
                <v-btn
                    color="warning"
                    text
                    @click="deleteItem"
                >
                  删 除
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-menu>
        </v-sheet>
      </v-col>
    </v-row>
    <v-btn class="white--text" color="primary" depressed fab
           large style="position: fixed;z-index: 4;bottom: 40px;right: 40px"
           @click="openTimeDialog"
    >
      <v-icon>
        mdi-plus
      </v-icon>
    </v-btn>

    <v-dialog v-model="addTimeDialog" width="600">
      <v-card>
        <v-card-title class="text-h5 lighten-2">
          记录添加
        </v-card-title>
        <v-form class="pl-5 pr-5">
          <v-row
          >
            <v-col
                align-self="center"
                cols="6"
            >
              <v-select
                  v-model="whichDrink"
                  :items="drinkLevels"
                  item-text="state"
                  item-value="value"
                  label="今日状态"
                  return-object
              ></v-select>
            </v-col>
            <v-col
                align-self="center"
                cols="6"
            >
              <v-select
                  v-model="timed"
                  :items="timeTypes"
                  item-text="state"
                  item-value="value"
                  label="时间跨度"
                  return-object
              ></v-select>
            </v-col>
          </v-row>
          <v-row>
            <v-col
                align-self="center"
                cols="12"
            >
              <v-dialog
                  ref="datedialog"
                  v-model="datePickDialog"
                  :return-value.sync="dateInit"
                  width="290px"
              >
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                      v-model="dateInit"
                      label="选择日期"
                      prepend-icon="mdi-calendar"
                      readonly
                      v-bind="attrs"
                      v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker
                    v-model="dateInit"
                    :max="nowTime"
                    locale="zh-cn"
                    scrollable
                >
                  <v-spacer></v-spacer>
                  <v-btn
                      color="primary"
                      text
                      @click="datePickDialog = false"
                  >
                    取 消
                  </v-btn>
                  <v-btn
                      color="primary"
                      text
                      @click="$refs.datedialog.save(dateInit)"
                  >
                    确 定
                  </v-btn>
                </v-date-picker>
              </v-dialog>
            </v-col>
          </v-row>
          <v-row v-if="this.timed.value" justify="space-around">
            <v-col style="width: 300px; flex: 0 1 auto;">
              <h2>Start:</h2>
              <v-time-picker
                  v-model="start"
                  :max="end"
              ></v-time-picker>
            </v-col>
            <v-col style="width: 300px; flex: 0 1 auto;">
              <h2>End:</h2>
              <v-time-picker
                  v-model="end"
                  :min="start"
              ></v-time-picker>
            </v-col>
          </v-row>
          <v-row>
            <v-col
                align-self="center"
                cols="12">
              <v-textarea v-model="textarea" label="详细描述"
                          type="number"></v-textarea>
            </v-col>
          </v-row>
        </v-form>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              text
              @click="addTimeDialog = false"
          >
            取 消
          </v-btn>
          <v-btn
              color="warning"
              depressed
              @click="resetTime"
          >
            重 置
          </v-btn>
          <v-btn
              color="success"
              depressed
              @click="addOnetip"
          >
            添 加
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="editDetailDialog" persistent width="500">
      <v-card>
        <v-card-title>修改信息</v-card-title>
        <v-form class="ma-4">
          <v-textarea v-model="editDetails"></v-textarea>
        </v-form>
        <v-card-actions>
          <v-btn
              text
              @click="editDetailDialog = false"
          >
            取 消
          </v-btn>
          <v-btn
              color="warning"
              depressed
              @click="resetDetails"
          >
            重 置
          </v-btn>
          <v-btn
              color="success"
              depressed
              @click="saveDetails"
          >
            保 存
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="deleteTipDialog" width="300">
      <v-card>
        <v-card-title>删除确认</v-card-title>
        <v-card-text>确定删除吗？将无法恢复</v-card-text>
        <v-card-actions>
          <v-btn
              text
              @click="deleteTipDialog = false"
          >
            取 消
          </v-btn>
          <v-btn
              color="warning"
              depressed
              @click="deleteTipConfirm()"
          >
            确 定
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

import Vue from "vue";
import {mapState} from "vuex";

export default {
  data: () => ({
    focus: '',
    type: 'month',
    typeToLabel: {
      month: '按月显示',
      week: '按周显示',
      day: '每日记录',
      '4day': '4天并列',
    },
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    events: [],
    stateColors: ['green', 'blue', 'warning', 'error'],
    names: ['Meeting', 'Holiday', 'PTO', 'Travel', 'Event', 'Birthday', 'Conference', 'Party'],
    addTimeDialog: false,
    textarea: '',
    datePickDialog: false,
    dateInit: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10),
    nowTime: (new Date(Date.now() - (new Date()).getTimezoneOffset() * 60000)).toISOString().substr(0, 10).toString(),
    drinkLevels: [
      {state: '未沾一滴', value: 0},
      {state: '少量酒精', value: 1},
      {state: '接近醉酒', value: 2},
      {state: '深度醉酒', value: 3},
    ],
    whichDrink: {state: '未沾一滴', value: 0},
    timeTypes: [
      {state: '时间段', value: true},
      {state: '全天', value: false},
    ],
    timed: {state: '全天', value: false},
    start: null,
    end: null,
    editDetailDialog: false,
    editDetails: '',
    deleteTipDialog: false,
  }),
  computed: {
    ...mapState('User', ['user',]),
  },
  mounted() {
    this.$refs.calendar.checkChange()
    this.load()
  },
  methods: {
    viewDay({date}) {
      this.focus = date
      this.type = 'day'
    },
    getEventColor(event) {
      return event.color
    },
    setToday() {
      this.focus = ''
    },
    prev() {
      this.$refs.calendar.prev()
    },
    next() {
      this.$refs.calendar.next()
    },
    load() {
      this.dataLoading = true
      this.$request.get(`/calendar/all/${this.user.id}`,).then(res => {
        // console.log(res)
        if (res.code == 200) {
          let data = res.data
          for (var i = 0; i < data.length; i++) {
            // 添加新属性到每个对象
            data[i].color = this.stateColors[data[i].drinking];
          }
          this.events = data
          setTimeout(() => {
            this.dataLoading = false
          }, 200)
        }
      }).catch(err => {
        console.log(err)
      })
    },
    showEvent({nativeEvent, event}) {
      const open = () => {
        this.selectedEvent = event
        this.selectedElement = nativeEvent.target
        requestAnimationFrame(() => requestAnimationFrame(() => this.selectedOpen = true))
      }

      if (this.selectedOpen) {
        this.selectedOpen = false
        requestAnimationFrame(() => requestAnimationFrame(() => open()))
      } else {
        open()
      }
      nativeEvent.stopPropagation()
    },
    openTimeDialog() {
      this.addTimeDialog = true;
    },
    deleteItem() {
      this.deleteTipDialog = true
    },
    addOnetip() {
      let dataSend = new FormData()
      const data = {
        name: `${this.drinkLevels[this.whichDrink.value].state} (${this.timed.state})`,
        start: this.timed.value ? `${this.dateInit} ${this.start}:00` : `${this.dateInit} 00:00:00`,
        end: this.timed.value ? `${this.dateInit} ${this.end}:00`: `${this.dateInit} 23:59:59`,
        // color: this.stateColors[this.whichDrink.value],
        details: this.textarea,
        drinking: parseInt(this.whichDrink.value),

        timed: parseInt(this.timed.value == true ? 1 : 0),//是否全天
        userid: parseInt(this.user.id)
      }
      // console.log(data)
      for (const key in data) {
        dataSend.append(key, data[key]);
      }
      console.log(data)
      // console.log(dataSend)
      this.$request.post("/calendar/save", dataSend).then(res => {
        if (res == true) {
          Vue.notify({
            title: '添加成功',
            text: '添加记录成功',
            type: 'success'
          })
          this.load()
          this.addTimeDialog = false
        } else {
          Vue.notify({
            title: '添加失败',
            text: '添加记录失败',
            type: 'warning'
          })
        }
      }).catch(err => {
        Vue.notify({
          title: '添加失败',
          text: '服务器错误',
          type: 'error'
        })
        console.log(err)
      })
      data["color"] = this.stateColors[this.whichDrink.value]
    },
    resetTime() {
      this.dateInit = this.nowTime
      this.whichDrink = {state: '未沾一滴', value: 0}
      this.timed = {state: '全天', value: false}
      this.textarea = ''
      this.start = ''
      this.end = ''
    },
    openEditDialog() {
      this.editDetails = this.selectedEvent.details
      this.editDetailDialog = true
    },
    resetDetails() {
      this.editDetails = this.selectedEvent.details
    },
    saveDetails() {
      const i = this.events.indexOf(this.selectedEvent)
      this.events[i].details = this.editDetails
      this.$request.get(`/calendar/savedetails?id=${this.events[i].id}&details=${this.events[i].details}`).then(
          res => {
            if (res == true) {
              Vue.notify({
                title: '修改成功',
                text: '修改记录成功',
                type: 'success'
              })
            } else {
              Vue.notify({
                title: '修改失败',
                text: '服务器错误',
                type: 'error'
              })
            }
          }
      ).catch(err => {
        console.log(err)
      })
      this.editDetailDialog = false
    },
    deleteTipConfirm() {
      const i = this.events.indexOf(this.selectedEvent)
      this.$request.get(`/calendar/delete/${this.selectedEvent.id}`,).then(res => {
        if (res == true) {
          Vue.notify({
            title: '删除成功',
            text: '删除记录成功',
            type: 'success'
          })
        } else {
          Vue.notify({
            title: '删除失败',
            text: '删除记录失败',
            type: 'warning'
          })
        }
      }).catch(err => {
        Vue.notify({
          title: '删除失败',
          text: '服务器错误',
          type: 'error'
        })
        console.log(err)
      })
      this.deleteTipDialog = false
      if (i !== -1) {
        this.events.splice(i, 1)
      }
    }
  },

}
</script>

<style lang="scss" scoped>

</style>