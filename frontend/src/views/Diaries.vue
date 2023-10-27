<template>
  <v-app>
    <v-app-bar>
      <v-btn icon @click="goBack">
        <v-icon>
          mdi-arrow-left
        </v-icon>
      </v-btn>
      <h3>BTC 金 融 论 坛</h3>
    </v-app-bar>
    <div id="main" class="onepage">
      <v-container>
        <Waterfall :gutter="16" :list="items">
          <template #item="{ item, index }">
            <v-card>
              <v-card-title style="padding-bottom: 0">
                <h3>
                  {{ item.username }}
                </h3>
                <v-subheader>{{ item.email }}</v-subheader>
              </v-card-title>
              <p class="pa-4" style="text-align: justify">
                {{ item.content }}
              </p>
              <v-card-actions>
                <v-chip
                    class="ma-2"
                    color="orange"
                    label
                    outlined
                >
                  <v-icon left>
                    mdi-thumb-up
                  </v-icon>
                  {{ item.thumb }}
                </v-chip>
                <v-spacer></v-spacer>
                <v-btn v-if="user" color="secondary" depressed @click="openTips(item.id)">
                  赞 赏
                </v-btn>
              </v-card-actions>
            </v-card>
          </template>
        </Waterfall>
      </v-container>
      <div class="adddiaryBtn">
        <v-slide-y-reverse-transition appear>
          <v-btn v-if="user" color="primary" depressed fab @click="diaryDialog=true">
            <v-icon size="24">mdi-pencil</v-icon>
          </v-btn>
        </v-slide-y-reverse-transition>
      </div>
      <!--    发布Diary-->
      <v-dialog
          v-model="diaryDialog"
          max-width="600"
          transition="dialog-top-transition"
      >
        <v-card>
          <v-toolbar
              color="primary"
              dark
          >发布模拟投资心得
          </v-toolbar>
          <v-card-text class="mt-6">
            <v-textarea v-model="diaryData" auto-grow clearable dense>
            </v-textarea>
          </v-card-text>
          <v-card-actions class="justify-end">
            <v-spacer></v-spacer>
            <v-btn
                text
                @click="diaryDialog = false"
            >关 闭
            </v-btn>
            <v-btn
                color="success"
                depressed
                @click="addDiary"
            >发 布
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <!--    打赏-->
      <v-dialog
          v-model="tipsDialog"
          max-width="300"
      >
        <v-card>
          <v-toolbar
              color="primary"
              dark
          >打 赏
          </v-toolbar>
          <v-card-text class="mt-6">
            一切打赏都是自愿行为，平台将收取20%的费用以防止非法行为
            <v-text-field v-model="tips" append-icon="mdi-currency-usd" required type="number">
            </v-text-field>
          </v-card-text>
          <v-card-actions class="justify-end">
            <v-spacer></v-spacer>
            <v-btn
                color="red"
                depressed
                icon
                @click="payTips"
            >
              <v-icon>
                mdi-thumb-up
              </v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </div>
  </v-app>
</template>

<script>
import {LazyImg, Waterfall} from 'vue-waterfall-plugin'
import 'vue-waterfall-plugin/dist/style.css'
import {mapActions, mapState} from "vuex";

export default {
  name: "Diaries",
  components: {
    LazyImg,
    Waterfall
  },
  data() {
    return {
      items: [
        {
          id: 1,
          content: '获取Diaries中',
          thumb: 0
        },
      ],
      diaryData: '',
      diaryDialog: false,
      tipsDialog: false,
      tips: 0,
      diaryId: null,
    }
  },
  computed: {
    ...mapState('User', ['user']),
  },
  methods: {
    ...mapActions('User', ['refreshDetails']),
    initDiaries() {
      this.$request.get('/diary').then(
          res => {
            // console.log(res)
            this.items = this.shuffleArray(res)
          }
      ).catch(err => {
        console.log(err)
      })
    },
    payTips() {
      this.$request.post(`/diary/thumb/${this.user.id}?diaryid=${this.diaryId}&money=${this.tips}`).then(
          res => {
            if (res.code == 200) {
              this.$notify({
                title: "赞助成功",
                text: this.tips,
                type: "success"
              })
              this.tipsDialog = false
              this.initDiaries()
              this.refreshDetails()
            } else if (res.code == 500) {
              this.$notify({
                title: "赞助失败",
                text: res.msg,
                type: "warning"
              })
            } else {
              this.$notify({
                title: "系统出错",
                type: "error"
              })
            }
            // console.log(res)
          }
      ).catch(err => {
        console.log(err)
      })
    },
    addDiary() {
      const form = new FormData()
      form.append('content', this.diaryData)
      form.append('userid', this.user.id)
      this.$request.post('/diary/save', form).then(
          res => {
            this.$router.go(0)
          }
      ).catch(err => {
        console.log(err)
      })
    },
    openTips(diaryid) {
      this.diaryId = diaryid
      this.tipsDialog = true
    },
    goBack() {
      this.$router.back()
    },
    shuffleArray(arr) {
      for (let i = arr.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [arr[i], arr[j]] = [arr[j], arr[i]];
      }
      return arr;
    }
  },
  created() {
    this.initDiaries()
  },
  mounted() {

  }
}
</script>

<style scoped>
#main {
  background-image: linear-gradient(to right top, #ffffff, #edecf7, #dad8f0, #c7c6e8, #b2b4e1, #a2a6df, #9298de, #818bdc, #737ddf, #666fe1, #5a61e3, #4e51e3);
}

.onepage {
  width: 100%;
  height: 100%;
}

.adddiaryBtn {
  position: fixed;
  bottom: 4%;
  right: 60px;
  z-index: 4;
}
</style>