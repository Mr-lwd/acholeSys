<template>
  <v-container>
    <div v-if="loadingTable">
      <v-skeleton-loader :loading="loadingTable" class="mx-auto" type="table-thead,table-tbody,table-tbody">
      </v-skeleton-loader>
    </div>
    <div v-else>
      <v-card>
        <v-card-title>
          <h2>LoanControl</h2>
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
              label="查找贷款表项(任意关键词)"
              single-line
          ></v-text-field>
        </v-card-title>
        <div class="text-center pt-2">
          <v-pagination
              v-model="page"
              :length="totolPages"
              :total-visible="7"
              circle
          ></v-pagination>
        </div>
        <v-data-table
            :headers="headers"
            :items="tableData"
            :items-per-page="itemsPerPage"
            :loading="dataLoading"
            :page.sync="page"
            :search="search"
            class="text-md"
            hide-default-footer
        >
          <template v-slot:item.state="{ item }">
            <v-edit-dialog
                v-if="item.state == 2 || item.state == 4"
                cancel-text="取消"
                large
                save-text="确认还款"
                @save="repayMoney(item.id)"
            >
              <v-chip
                  :color="getStateColors(item.state)"
                  dark
              >
                {{ whichState(item.state) }}
              </v-chip>
              <template v-slot:input>
                确认还款？这将扣除利率对应的余额
              </template>
            </v-edit-dialog>
            <div v-else>
              <v-chip
                  :color="getStateColors(item.state)"
                  dark
              >
                {{ whichState(item.state) }}
              </v-chip>
            </div>
          </template>
        </v-data-table>
      </v-card>
    </div>
    <v-btn class="white--text" color="primary" depressed fab
           large style="position: fixed;z-index: 4;bottom: 40px;right: 40px"
           @click="addLoanDialog=true">
      <v-icon>
        mdi-piggy-bank-outline
      </v-icon>
    </v-btn>
    <v-dialog v-model="addLoanDialog" width="460">
      <v-card>
        <v-card-title class="text-h5 lighten-2">
          贷款申请
        </v-card-title>
        <v-card-subtitle>选择银行</v-card-subtitle>
        <v-form class="pl-15 pr-15">
          <v-text-field v-model="loanMoney" label="申请金额"
                        type="number"></v-text-field>
          <v-select
              v-model="selectBank"
              :items="banks"
              :label="`电话:${selectBank.phone}--地址:${selectBank.address}--可申请:${selectBank.property}`"
              item-text="name"
              return-object
          ></v-select>
          <v-select v-model="selectKind"
                    :items="rateKinds"
                    :label="`${rateKinds[selectKind].days}`"
                    item-text="days" item-value="id"
          ></v-select>
        </v-form>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="error"
              text
              @click="addLoanDialog = false"
          >
            取 消
          </v-btn>
          <v-btn
              color="success"
              depressed
              @click="addLoan"
          >
            确认申请
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>


<script>
import {mapActions, mapGetters, mapState} from "vuex";


export default {
  name: "processAll",
  data() {
    return {
      tableData: [],
      search: '',
      headers: [
        {
          text: 'ID',
          align: 'start',
          sortable: false,
          value: 'id',
        },
        {text: '状态', value: 'state'},
        {text: '贷款金额', value: 'amount'},
        {text: '已还款', value: 'repay'},
        {text: '利率(%)', value: 'rate'},
        {text: '银行', value: 'bankname'},
        {text: '申请时间', value: 'time'}
      ],
      snack: false,
      snackColor: '',
      snackText: '',
      max4chars: v => v.length <= 2 || '太黑心了 !',
      pagination: {},
      // pageNum: 1,
      // pageSize: 5,
      nextState: 1,
      loadingTable: true,
      dataLoading: false,
      page: 1,
      totolPages: 0,
      pageSizes: [5, 10, 20, 30],
      itemsPerPage: 10,
      textNum: 'text',
      addLoanDialog: false,
      banks: '',
      selectBank: {
        phone: '请选择',
        address: '请选择',
        property: '请选择',
        name:'下拉选择'
      },
      loanMoney: 0,
      rateKinds: [
        {
          id: 0,
          days: '7天还款',
          rate: 3.0,
        },
        {
          id: 1,
          days: '14天还款',
          rate: 6.5,
        },
        {
          id: 2,
          days: '30天还款',
          rate: 14.0,
        },
        {
          id: 3,
          days: '60天还款',
          rate: 30.0,
        }
      ],
      selectKind: 0,
    }
  },
  computed: {
    ...mapGetters('loan', ['getloanState', 'getStateColors']),
    ...mapState('User', ['user',]),
  },
  created() {
    this.load()
    this.loadBanks()
    setTimeout(() => {
      this.loadingTable = false
    }, 900)
  },

  methods: {
    ...mapActions('loan', ['adminUpdateLoan', 'updataloanRate', 'bankCancel']),
    ...mapActions('User', ['refreshDetails']),
    load() {
      this.dataLoading = true
      this.$request.get(`/loan/userGetLoan/${this.user.id}`, {
        params: {
          pageNum: this.page,
          pageSize: this.itemsPerPage,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.totolPages = res.data.pages
        setTimeout(() => {
          this.dataLoading = false
        }, 100)
      }).catch(err => {
        console.log(err)
      })
    },
    loadBanks() {
      this.$request.get('/bank/getAll').then(res => {
        this.banks = res
      }).catch(err => {
        console.log(err)
      })
    },
    whichState(status) {
      return this.getloanState(status)
    },
    updateRate(item) {
      if (item.rate == '' || item.rate <= 0) {
        this.$notify({
          title: '数据错误',
          text: '检查利率的数值',
          type: 'error'
        })
        this.load()
      } else {
        this.updataloanRate({item: item})
      }
    },
    repayMoney(itemId) {
      // console.log('itemID', itemId)
      this.$request.post(`/loan/repay?loanid=${itemId}`)
          .then(res => {
            if (res.code == 200) {
              this.$notify({
                title: '还款成功!',
                type: 'success'
              })
              this.refreshDetails()
              this.load()
            } else {
              this.$notify({
                title: '还款失败!',
                type: 'error'
              })
            }
          })
          .catch(error => {
            // 处理异步操作的错误
            console.log('数据更新失败：', error)
          })
    },
    addLoan() {
      if (this.loanMoney == 0 || this.loanMoney == undefined || this.loanMoney == null || this.selectBank.name == '下拉选择') {
        this.$notify({
          title: '申请信息有误',
          type: 'warning'
        })
      } else {
        if (this.loanMoney >= 0) {
          const loadForm = new FormData()
          const data = {
            state: 0,
            amount: this.loanMoney,
            userid: this.user.id,
            bankid: this.selectBank.id,
            ratekind: this.rateKinds[this.selectKind].id,
            rate: this.rateKinds[this.selectKind].rate,
          }
          for (let key in data) {
            loadForm.append(key, data[key])
          }
          this.$request.post(`/loan`, loadForm)
              .then(res => {
                if (res.code == 200) {
                  this.$notify({
                    title: '贷款成功!',
                    type: 'success'
                  })
                  this.addLoanDialog = false
                  this.refreshDetails()
                  // this.loadBanks()
                  this.load()
                } else {
                  this.$notify({
                    title: '贷款失败!',
                    text: res.msg,
                    type: 'error'
                  })
                }
              })
              .catch(error => {
                // 处理异步操作的错误
                console.log('错误异常：', error)
              })
        }
      }
    }
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
