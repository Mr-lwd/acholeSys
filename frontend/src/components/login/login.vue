<template>
  <v-card-text class="my-4">
    <v-form ref="form">
      <v-text-field v-model="email" :rules="emailRules" color="primary" label="邮箱" prepend-icon="mdi-email"
                    required></v-text-field>
      <v-text-field v-model="password" :rules="passwordRules" color="primary" label="密码"
                    prepend-icon="mdi-lock-outline" required type="password"></v-text-field>
      <v-select v-model="select" :items="items" :rules="[v => !!v || '选择用户']" item-text="state" item-value="val"
                label="用 户 角 色" prepend-icon="mdi-cog-outline" required return-object></v-select>
      <v-fade-transition appear>
        <v-card-actions>
          <v-row justify="center">
            <v-btn :disabled="loading" color="primary" depressed large tile @click="userlogin()">
              立 即 登 入
            </v-btn>
            <v-btn color="primary" depressed large outlined style="margin-left: 2vw;" tile>
<!--              <router-link to="/diaries">前 往 论 坛</router-link>-->
              前 往 论 坛
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-fade-transition>
    </v-form>
    <!-- <v-btn @click="notify()" :disabled="loading">{{ loading ? 'loading...' : '测试' }}
    </v-btn> -->
    <!--    <v-btn @click="isLogin()">isLogin</v-btn>-->
    <!--    <v-btn @click="logout()">logout</v-btn>-->
  </v-card-text>
</template>
<script>
import {mapActions, mapGetters, mapState} from 'vuex'

export default {
  data() {
    return {
      email: '',
      emailRules: [
        v => !!v || '邮箱是必须项',
        v => /.+@.+\..+/.test(v) || '邮箱格式不正确',
      ],
      password: '',
      passwordRules: [
        v => !!v || '密码是必须项',
        v => (v && v.length > 0) || '请输入密码',
      ],
      select: {state: '使用者', val: 'user'},
      items: [
        {state: '使用者', val: 'user'},
        {state: '管理员', val: 'admin'},
      ],
    }
  },
  methods: {
    async userlogin() {
      this.$refs.form.validate()
      if (this.email && this.password && this.select) {
        let myloginData = new FormData()
        const data = {
          email: this.email,
          password: this.password,
          role: this.select.val,
        }
        for (const key in data) {
          myloginData.append(key, data[key]);
        }
        await this.login(myloginData)
      }
    }
    ,
    async handleLogout() {
      this.logout()
      //跳转到登陆页面
      // this.$route.push(/xxx)
    }
    ,
    notify() {
      this.$notify('Hello user!')
    }
    ,
    ...
        mapActions("User", ["login", "isLogin", "logout", "register"])
  },
  computed: {
    ...mapState({
      loading: (state) => state.User.loading
    }),
    ...mapGetters("User", ["status"]),
    ...mapState("User", ["user"]),
  },
  created() {
    // this()
  }
}
</script>
<style></style>