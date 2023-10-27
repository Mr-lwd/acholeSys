<template>
  <div>
    <form id="upload-form" class="dropzone" enctype="multipart/form-data">
      <v-btn ref="submitBtn" color="primary" depressed type="submit" x-large @click.prevent="uploadImgs">上 传 图 片
      </v-btn>
    </form>
  </div>
</template>
<script>
import Dropzone from 'dropzone'
import 'dropzone/dist/dropzone.css'
import {mapState} from "vuex";
import Vue from "vue";


export default {
  name: 'imgsupload',
  data: function () {
    return {}
  },
  computed: {
    ...mapState('User', ['user']),
    userid() {
      return this.user.id
    }
  },
  methods: {
    uploadImgs() {
      this.myDropzone.processQueue();
    }
  },
  mounted() {
    let myDropzone = new Dropzone('#upload-form', { // The camelized version of the ID of the form element
          // The configuration we've talked about above
          autoProcessQueue: false,
          uploadMultiple: true,
          parallelUploads: 8,
          acceptedFiles: 'image/*',
          // paramName: "siteFile", // 传到后台的参数名称
          maxFiles: 8,
          // url: `/api/imgs/upload?userid=${this.userid}`,
          url: `http://116.205.234.27:9090/imgs/upload?userid=${this.userid}`,
          // headers: {
          //   'content-type': "multipart/form-data",
          // },
          paramName: "file",
          method: "post",
          dictDefaultMessage: '拖动文件至此或者点击上传', // 设置默认的提示语句
          dictFallbackMessage: '您的浏览器不支持！', //如果浏览器不支持,默认消息将被替换为这个文本。默认为 “Your browser does not support drag'n'drop file uploads.”。
          dictInvalidFileType: '该文件不允许上传', //如果文件类型不匹配时显示的错误消息。
          dictResponseError: '上传失败，请稍后重试', //如果服务器响应是无效的时候显示的错误消息。
          init: function () {
            this.on("successmultiple", function (files, response) {
              Vue.notify({
                title: '上传成功',
                text: '即将刷新页面',
                type: 'success'
              })
              setTimeout(() => {
                window.location.reload()
              }, 500)
            });
          },
        }
    )
    this.myDropzone = myDropzone
  }
}
</script>