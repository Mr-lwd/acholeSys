<template>
  <!--  <div>-->
  <!--        <v-tabs>-->
  <!--          <v-tab>user</v-tab>-->
  <!--          <v-tab>manage</v-tab>-->
  <!--          <v-tab>banker</v-tab>-->
  <!--        </v-tabs>-->
  <component :is="selectRole"></component>
  <!--  </div>-->
</template>
<script>
import normaluser from "@/components/manage/normaluser";
import {mapGetters} from "vuex";


export default {
  name: 'manageBar',
  components: {
    normaluser,
  },
  data: () => ({
    bars: ['normaluser']
  }),
  methods: {},
  computed: {
    ...mapGetters("User",['whichRole']),
    selectRole() {
      return this.bars[this.whichRole]
    }
  },
  mounted() {
    // console.log(this.whichRole)
  },
  watch: {
    // choosebar:{
    //   handle(newVal, oldVal){
    //
    // }
  }
}
</script>
<style></style>