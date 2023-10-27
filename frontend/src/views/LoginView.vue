<template>
  <section class="dark-gradient fullpage-section">
    <animated-background />
    <v-container id="loginForm">
      <v-card class="mx-auto my-12 outer-card" max-width="450px">
        <v-card-title>
          <v-scroll-y-transition appear>
            <v-card class="v-header py-2 mx-auto my-n12" min-width="300px" raised>
              <v-card-title>
                <v-row justify="center">
                  <h3 style="color: white;">{{ formIndex == '0' ? 'L O G I N' : 'R e g i s t e r' }}</h3>
                </v-row>
              </v-card-title>
              <v-card-actions>
                <v-row justify="center">
                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn icon>
                        <router-link to="/">
                          <v-icon v-bind="attrs" v-on="on" style="color: white;">
                            mdi-home
                          </v-icon>
                        </router-link>
                      </v-btn>
                    </template>
                    <span>返回首页</span>
                  </v-tooltip>

                  <v-tooltip bottom>
                    <template v-slot:activator="{ on, attrs }">
                      <v-btn @click="changeFormindex()" icon>
                        <v-icon v-bind="attrs" v-on="on" style="color: white;">mdi-cached</v-icon>
                      </v-btn>
                    </template>
                    <span>切换</span>
                  </v-tooltip>
                </v-row>
              </v-card-actions>
            </v-card>
          </v-scroll-y-transition>
        </v-card-title>
        <br />
        <keep-alive>
          <component :is="whichpage" style="max-height: 60vh;overflow:auto;"></component>
        </keep-alive>
      </v-card>
    </v-container>
  </section>
</template>
<script>
import AnimatedBackground from '@/components/custom/animated-background'
import GlobalComputed from '@/helpers/global-computed'
import GlobalMethods from '@/helpers/global-methods'
import registerComponent from '@/components/login/register'
import loginComponent from '@/components/login/login'

export default {
  name: "loginPage",
  components: {
    AnimatedBackground,
    registerComponent,
    loginComponent,
  },
  data() {
    return {
      formIndex: 0,
      arr: ['loginComponent', 'registerComponent']
    }
  },
  computed: {
    ...GlobalComputed,
    whichpage() {
      return this.arr[this.formIndex]
    }
  },
  methods: {
    changeFormindex() {
      if (this.formIndex == 0) {
        this.formIndex = 1
      } else {
        this.formIndex = 0
      }
    },
    ...GlobalMethods,
  },
  mounted() {
  }
};
</script>
<style scoped>
.v-header {
  background-image: linear-gradient(60deg, #758bfd, #3e5fe2);
  text-align: center;
  width: 100%;
  margin-top: -40px;
}

h3 {
  /* font-family: "Times New Roman", Times, serif; */
  color: white;
}

.outer-card {
  border-radius: 8px !important;
  min-height: 300px;
  min-width: 200px;
}

.v-input__icon--prepend .v-icon {
  color: black;
}

#loginForm {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>