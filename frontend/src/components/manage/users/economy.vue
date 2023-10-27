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
          <h2>Invest</h2>
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
              label="查找投资信息（输入任意关键词）"
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
        </v-data-table>

      </v-card>
    </div>

  </v-container>
</template>

<script>

import {mapActions, mapState} from "vuex";

export default {
  name: "process",
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
        {text: '币种', value: 'moneyname'},
        {text: '货币数', value: 'coins'},
        {text: '金额（人民币）', value: 'amount'},
      ],
      pagination: {},
      //pageNum: 1,
      //pageSize: 5,
      loadingTable: true,
      nextState: 1,
      dataLoading: false,
      page: 1,
      totalPages: 0,
      pageSizes: [5,10,20,30],
      itemsPerPage: 10,
    }
  },
  created() {
    this.load()
    setTimeout(()=> {
      this.loadingTable = false
    },900)
  },
  computed:{
    ...mapState('User',['user'])
  },
  methods:{
    //...mapActions('comment', ['adminUserComment']),
    async load(){
      this.dataLoading = true
      await this.$request.get(`/invest/userGetInvest/${this.user.id}`,{
        params:{
          pageNum: this.page,
          pageSize: this.itemsPerPage,
        }
      }).then(res => {
        console.log(res)
        this.tableData = res.records
        this.totalPages = res.pages
        setTimeout(() => {
          this.dataLoading = false
        }, 200)
      }).catch(err => {
        console.log(err)
      })
    },
    // updateUserComment(item) {
    //   this.adminUserComment(item)
    // },
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
    }
  }
}
</script>

<style scoped>

</style>
