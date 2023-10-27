<template>
  <div>
    <v-app app>
      <managebar></managebar>
      <v-main style="padding: 0;background-image: linear-gradient(to right top, #a1d1ff, #c2daff, #dce5ff, #f0f1ff, #ffffff);">
        <router-view></router-view>
      </v-main>
    </v-app>
  </div>
</template>
<script>
import {mapActions} from 'vuex'
import Loading from '@/components/default/loading'
import managebar from '@/components/manage/managebar'
import GlobalComputed from '@/helpers/global-computed'
import GlobalMethods from '@/helpers/global-methods'

export default {
  name: 'userView',
  // props: ['query'],
  components: {
    Loading,
    managebar
  },
  data() {
    return {
      id: '',
    }
  },
  computed: {
    ...GlobalComputed
  },
  methods: {
    ...GlobalMethods,
    ...mapActions("User", ["isLogin"])
  },
  created() {
    this.$store.commit('SET_LOADING', true);
    window.setTimeout(() => {
      this.$store.commit('SET_LOADING', false);
    }, 400);
    // this.isLogin()
  }
}
</script>
<style></style>