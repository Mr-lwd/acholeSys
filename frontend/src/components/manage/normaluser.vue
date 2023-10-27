<template>
  <v-navigation-drawer disable-resize-watcher v-model="drawer" :mini-variant.sync="mini" app color="secondary" permanent>
    <v-list-item class="px-2" style="color: white;">
      <v-list-item-avatar style="background-color: white;">
        <v-icon color="primary"> mdi-account</v-icon>
      </v-list-item-avatar>

      <v-list-item-title>
        <h2>{{ userName }}</h2>
      </v-list-item-title>

      <v-btn icon @click.stop="mini = !mini">
        <v-icon style="color: white;">mdi-chevron-left</v-icon>
      </v-btn>
    </v-list-item>

    <v-divider></v-divider>

    <v-list-item-group v-model="selectedItem" color="success">
      <v-list shaped>
        <v-list-item @click="routerPushpath(item.linker)" v-for="item in items" :key="item.title" link>
          <v-list-item-icon >
            <v-icon style="color: white;">{{ item.icon }}</v-icon>
          </v-list-item-icon>
          <!-- <router-link :to="item.linker"> -->
          <v-list-item-content>
            <v-list-item-title style="color: white;font-size: 1.1rem;">{{ item.title }}</v-list-item-title>
          </v-list-item-content>
          <!-- </router-link> -->
        </v-list-item>
        <v-divider></v-divider>
        <v-list-item style="margin-top: 30px" @click="userLogout">
          <v-list-item-icon>
            <v-icon style="color: white;">
              mdi-logout
            </v-icon>
          </v-list-item-icon>
          <v-list-item-content style="color: white;font-size: 1.1rem;">
            退 出 登 录
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-list-item-group>
  </v-navigation-drawer>
</template>
<script>
import GlobalMethods from '@/helpers/global-methods'
import {mapActions, mapGetters} from "vuex";

export default {
  name: 'normalUser',
  data: () => ({
    drawer: true,
    items: [
      {title: '信息管理', icon: 'mdi-information-variant-box', linker: '/user/userdetails'},
      {title: '每日记录', icon: 'mdi-list-box', linker: '/user/list'},
      // {title: '前往论坛', icon: 'mdi-notebook', linker: '/diaries'},
    ],
    selectedItem: 0,
    mini: true,
  }),
  computed: {
    ...mapGetters("User", ["userName"]),
  },
  methods: {
    ...GlobalMethods,
    ...mapActions("User", ["logout"]),
    userLogout() {
      this.logout()
    }
  }
}
</script>
<style></style>