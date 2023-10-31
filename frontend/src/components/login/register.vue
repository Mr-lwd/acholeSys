<template>
  <v-card-text class="my-4 ">
    <v-form ref="form">
      <v-text-field v-model="email" :rules="emailRules" color="primary" label="邮箱" prepend-icon="mdi-email"
                    required></v-text-field>
      <v-text-field v-model="name" :counter="10" :rules="nameRules" color="primary" label="用户名"
                    prepend-icon="mdi-account" required></v-text-field>
      <v-text-field v-model="phone" :rules="phonenumRules" color="primary" label="手机号" prepend-icon="mdi-phone"
                    required></v-text-field>
      <v-text-field v-model="password" :rules="passwordRules" color="primary" label="密码"
                    prepend-icon="mdi-lock-outline" required type="password"></v-text-field>
      <v-select v-model="select" :hint="`${select.power}`" :items="items" :rules="[v => !!v || '选择用户']"
                item-text="state"
                item-value="val" label="选择角色" persistent-hint prepend-icon="mdi-cog-outline"
                required
                return-object></v-select>
      <v-checkbox v-model="checkbox" :rules="[v => !!v || '同意必要协议']" label="是否同意必要的使用协议？"
                  required></v-checkbox>
      <v-fade-transition appear>
        <v-card-actions>
          <v-row justify="center">
            <v-btn color="primary" depressed large style="color: white;" tile @click="userRegister()">
              快 速 注 册
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-fade-transition>
    </v-form>
  </v-card-text>
</template>
<script>


export default {
  name: 'registerComponent',
  components: {},
  data() {
    return {
      email: '',
      emailRules: [
        v => !!v || '邮箱是必须项',
        v => /.+@.+\..+/.test(v) || '邮箱格式不正确',
      ],
      name: '',
      nameRules: [
        v => !!v || '用户名是必须项',
        v => (v && v.length <= 10) || '用户名无法超过10个字符',
      ],
      phone: '',
      phonenumRules: [
        v => !!v || '手机号是必须项',
        v => (v && v.length > 0 && v.length <= 20) || '请输入正确手机',
      ],
      password: '',
      passwordRules: [
        v => !!v || '密码是必须项',
        v => (v && v.length > 0) || '请输入密码',
      ],
      select: {state: '平台使用者', val: 'user', power: "每日记录&信息列表"},
      items: [
        {state: '平台使用者', val: 'user', power: "每日记录&信息列表"},
      ],
      checkbox: false,
    }
  },
  methods: {
    async userRegister() {
      this.$refs.form.validate()
      // if (this.checkbox && this.email && this.name && this.password && this.select) {
      if (this.checkbox && this.email && this.name && this.password) {
        let registerData = new FormData()
        const data = {
          email: this.email,
          name: this.name,
          password: this.password,
          phone: this.phone,
          role: this.select.val,
        }
        for (const key in data) {
          registerData.append(key, data[key]);
        }
        await this.$store.dispatch("User/register", registerData)
      }
    }
  },
  mounted() {
  }
}
</script>
<style></style>