<template>
  <div v-if="items.length != 0">
    <v-subheader>
      <b>资 质 证 明 列 表</b>
      <v-btn v-if="selectedFile.length != 0" class="ml-4" color="black" icon large @click="dialogImgs=true">
        <v-tooltip right>
          <template v-slot:activator="{ on, attrs }">
        <v-icon   v-bind="attrs" v-on="on">
          mdi-trash-can
        </v-icon>
          </template>
          <span>删除所选文件</span>
        </v-tooltip>
      </v-btn>
    </v-subheader>
    <v-treeview v-model="selectedFile" :item-key="itemTextname"
                :item-text="itemTextname" :items="items"
                :selection-type="selectionType"
                activatable hoverable open-on-click return-object rounded
                selectable transition>
      <template v-slot:prepend="{ item, open }">
        <b>{{ item.name }}</b>
        <v-icon>
          mdi-earth
        </v-icon>
      </template>
    </v-treeview>
    <v-dialog
        v-model="dialogImgs"
        max-width="290"
    >
      <v-card>
        <v-card-title class="text-h5">
          确定要批量删除吗？
        </v-card-title>
        <v-card-text>
          这将从远程存储内删除您选择的图片
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
              color="orange darken-1"
              text
              @click="dialogImgs = false"
          >
            取 消
          </v-btn>
          <v-btn
              color="green darken-1"
              text
              @click="DeleteList()"
          >
            确 认
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import {mapState} from "vuex";

export default {
  name: "imgsDelete",
  data() {
    return {
      items: [],
      itemTextname: 'link',
      selectionType: 'leaf',
      selectedFile: [],
      dialogImgs: false,
    }
  },
  computed: {
    ...mapState('User', ['user']),
    userid() {
      return this.user.id
    }
  },
  created() {
    this.initList()
  },
  methods: {
    deleteOpen() {
      this.dialog = true
    },
    DeleteList() {
      const selectIds = this.selectedFile.map(item => item.id);
      this.postDelete(selectIds)
    },
    postDelete(Ids) {
      this.$request.post('/imgs/deleteAll', Ids).then(
          res => {
            console.log(res)
            this.$notify({
              title: '删除成功',
              text: '即将刷新页面',
              type: 'success'
            })
            setTimeout(() => {
              window.location.reload()
            }, 500)
          }
      ).catch(err => {
        console.log(err)
      })
    },
    initList() {
      this.$request.get(`/imgs/getAll/${this.userid}`).then(
          res => {
            if (res.data.length != 0) {
              this.items = [
                {
                  name: '全部文件',
                  children:
                      res.data.map(item => {
                        return {
                          id: item.id,
                          name: item.id,
                          link: item.url
                        }
                      })
                }
              ]
            }
            setTimeout(() => {
              this.loadingImgs = false
            }, 500)
          }
      ).catch(err => {
        console.log(err)
      })
    }
  }
}
</script>

<style scoped>

</style>