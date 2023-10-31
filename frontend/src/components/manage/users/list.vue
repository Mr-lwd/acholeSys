<template>
  <v-container>
    <div v-if="loadingTable">
      <v-skeleton-loader :loading="loadingTable" class="mx-auto" type="table-thead,table-tbody">
      </v-skeleton-loader>
    </div>
    <div v-else>
      <v-card>
        <!--        title-->
        <v-card-title>
          <h2>Records</h2>
          <v-spacer></v-spacer>
          <v-select
              v-model="itemsPerPage"
              :items="pageSizes"
              hide-details
              label="分页大小"
          ></v-select>
          <v-spacer></v-spacer>
          <v-text-field
              v-model="search"
              append-icon="mdi-magnify"
              hide-details
              label="查找信息（输入任意关键词）"
              single-line
          ></v-text-field>
        </v-card-title>

        <div class="text-center pt-2">
          <v-pagination
              v-model="page"
              :length="totalPages"
              :total-visible="7"
              circle></v-pagination>
        </div>

        <v-data-table
            :headers="header"
            :items="tableData"
            :items-per-page="itemsPerPage"
            :loading="dataLoading"
            :page.sync="page"
            :search="search"
            class="text-md"
            hide-default-footer>
          <template v-slot:item.timed="{ item }">
            <div v-if="ifAllday(item.timed)">
              <v-icon  color="black">mdi-check-all</v-icon>
              全天
            </div>
            <div v-else>
              <v-icon color="black">mdi-clock-time-four</v-icon>
              时间段
            </div>
          </template>
          <template v-slot:item.drinking="{ item }">
            <v-chip
                :color="stateColors[item.drinking]"
                dark
            >
              {{ stateTexts[item.drinking] }}
            </v-chip>
          </template>
          <template v-slot:item.details="{item}">
            <v-edit-dialog
                :return-value.sync="item.details"
                cancel-text="取消"
                large
                save-text="修改"
                @save="saveDetails(item)"
            >
              {{ item.details }}
              <template v-slot:input>
                <v-textarea
                    v-model="item.details"
                    :rules="[]"
                    counter
                    label="编辑"
                    single-line
                    type="text"
                ></v-textarea>
              </template>
            </v-edit-dialog>
          </template>
        </v-data-table>
      </v-card>
    </div>
  </v-container>
</template>

<script>
import {mapActions, mapGetters, mapState} from "vuex";
import request from "@/utils/request";
import Vue from "vue";

export default {
  name: "comments",
  data() {
    return {
      tableData: [],
      search: '',
      header: [
        {
          text: 'ID',
          align: 'start',
          sortable: false,
          value: 'id',
        },
        {text: '名称', value: 'name'},
        {text: '类型', value: 'timed'},
        {text: '起始', value: 'start'},
        {text: '截止', value: 'end'},
        {text: '详细信息', value: 'details'},
        {text: '健康状态', value: 'drinking'},
      ],
      stateTexts: ['未沾一滴', '少量酒精', '接近醉酒', '深度醉酒'],
      stateColors: ['green', 'blue', 'warning', 'error'],
      pagination: {},
      pageNum: 1,
      pageSize: 5,
      loadingTable: true,
      nextState: 1,
      dataLoading: false,
      page: 1,
      totalPages: 0,
      pageSizes: [5, 10, 20, 30],
      itemsPerPage: 10,
    }
  },
  computed: {
    ...mapState('User', ['user',]),
  },
  created() {
    this.load()
    setTimeout(() => {
      this.loadingTable = false
    }, 900)
  },
  methods: {
    ...mapActions('User', ['refreshDetails']),
    load() {
      this.dataLoading = true
      this.$request.get(`/calendar/page/${this.user.id}`, {
        params: {
          pageNum: this.page,
          pageSize: this.itemsPerPage,
        }
      }).then(res => {
        // console.log(res)
        this.tableData = res.data.records
        this.totalPages = res.data.pages
        setTimeout(() => {
          this.dataLoading = false
        }, 200)
      }).catch(err => {
        console.log(err)
      })
    },

    ifAllday(allDay) {
      return allDay == 0 ? true : false;
    },
    saveDetails(item) {
      this.$request.get(`/calendar/savedetails?id=${item.id}&details=${item.details}`).then(
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
  },
  watch: {
    page: {
      handler(newVal, oldVal) {
        this.page = newVal
        this.load()
      }
    },
    itemsPerPage: {
      handler(newVal, oldVal) {
        this.itemsPerPage = newVal
        this.load()
      }
    },
    search:{
      handler(newVal, oldVal) {
        this.search = newVal
        this.load()
      }
    }
  }
}
</script>

<style scoped>
</style>
