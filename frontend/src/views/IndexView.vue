<template>
  <div class="page page-sections">
    <navbar :light-theme="lightNavbar"/>
    <section id="headerSection" class="header-section dark-gradient fullpage-section" data-section="顶栏">
      <animated-background/>
      <v-container v-if="!loading" :class="{ 'align-center': phoneOnly }" class="fill-height pt-12">
        <v-slide-y-transition appear>
          <h1 class="strong-text--text">
            <animate-text :delay="500" :duration="1500" :stop-after="0" :text="sections.header.title"/>
          </h1>
        </v-slide-y-transition>
        <v-slide-x-transition appear>
          <h2 class="text--text mt-5">
            <animate-text :delay="2000" :duration="2500" :text="sections.header.subtitle"
                          @done="showSection1Actions = true"></animate-text>
          </h2>
        </v-slide-x-transition>
        <div :class="{ 'fade-up-off': !showSection1Actions, 'fade-up-on': showSection1Actions }" class="actions">
          <v-btn :large="pcOnly" class="me-6" color="strong-text" depressed outlined
                 tile @click="navigateToLink('#aboutSection')">{{ sections.actions.about }}
          </v-btn>
          <v-btn @click="routerPushpath('/login')" :large="pcOnly" color="secondary" outlined tile>
            {{ sections.actions.try }}
          </v-btn>
        </div>
      </v-container>

      <div class="section-scroll-icon">
        <scroll-icon @click="scrollToSection(2)"/>
      </div>
    </section>
        <e-section id="aboutSection" :headline="sections.about.title" :subtitle="sections.about.subtitle"
               data-section="简介"
               divider light>
      <v-row>
        <v-col :cols="pcOnly ? 5 : 12" :data-aos="pcOnly ? (isRtl ? 'fade-left' : 'fade-right') : 'fade-up'"
               class="" data-aos-duration="800">
          <v-img contain max-height="320" src="./img/vector1.png" width="100%"></v-img>
        </v-col>
        <v-spacer></v-spacer>
        <v-col :cols="pcOnly ? 6 : 12" :data-aos="pcOnly ? (isRtl ? 'fade-right' : 'fade-left') : 'fade-up'"
               class="mt-6 mt-lg-0" data-aos-duration="800">
          <h1 class="headline">{{ sections.about.row1.title }}</h1>
          <p :class="{ 'text-center': phoneOnly }" class="medium-text text-light--text mt-6 mt-lg-8">
            {{ sections.about.row1.content }}
          </p>
          <div class="actions">
            <v-btn color="primary mt-3" depressed large tile @click="navigateToLink('#algorithmSection')">{{
                sections.algorithm.title
              }}
            </v-btn>
            <v-btn class="ms-lg-4 mt-3" color="primary" depressed large outlined tile
                   @click="navigateToLink('#timelineSection')">{{ sections.timeline.title }}
            </v-btn>
          </div>
        </v-col>
      </v-row>
<!--      <v-row class=" flex-row-reverse" style="margin-top: 3.5rem">-->
<!--        <v-col :cols="pcOnly ? 5 : 12" :data-aos="pcOnly ? (isRtl ? 'fade-right' : 'fade-left') : 'fade-up'"-->
<!--               class="" data-aos-duration="800">-->
<!--          <v-img contain max-height="320" src="./img/vector2.png" width="100%"></v-img>-->
<!--        </v-col>-->
<!--        <v-spacer></v-spacer>-->
<!--        <v-col :cols="pcOnly ? 6 : 12" :data-aos="pcOnly ? (isRtl ? 'fade-left' : 'fade-right') : 'fade-up'"-->
<!--               class="mt-6 mt-lg-0" data-aos-duration="800">-->
<!--          <h1 class="headline">{{ sections.about.row2.title }}</h1>-->
<!--          <p :class="{ 'text-center': phoneOnly }" class="medium-text text-light&#45;&#45;text mt-6 mt-lg-8">-->
<!--            {{ sections.about.row2.content }}-->
<!--          </p>-->

<!--          <div class="actions">-->
<!--            <v-btn color="primary" depressed large tile @click="navigateToLink('#productsSection')">{{-->
<!--                sections.products.title-->
<!--              }}-->
<!--            </v-btn>-->
<!--          </div>-->
<!--        </v-col>-->
<!--      </v-row>-->
    </e-section>


    <e-section id="productsSection" :headline="sections.products.title" :subtitle="sections.products.subtitle" background="light"
               data-section="功能" light>
      <v-row class="flex-wrap justify-center">
        <v-col v-for="(project, i) in projects" :key="i" :cols="pcOnly ? 4 : 12">
          <v-card v-ripple="project.href ? { class: 'soft-ripple' } : false" :data-aos-delay="400 * i"
                  :data-aos-duration="800" :href="project.href ? project.href : ''" :target="project.href ? '_blank' : ''"
                  class="card-shadow fill-height" data-aos="fade-up">
            <v-img :src="project.image" contain height="250"/>
            <div class="py-1 px-3">
              <v-card-title class="primary--text mb-1">{{ project.title }}</v-card-title>
              <v-card-subtitle>{{ project.subtitle }}</v-card-subtitle>
            </div>
          </v-card>
        </v-col>
      </v-row>
    </e-section>

    <cols-section id="algorithmSection" :cols="algorithmCols" :headline="sections.algorithm.title"
                  :subtitle="sections.algorithm.subtitle">
    </cols-section>

    <e-section id="timelineSection" :headline="sections.timeline.title" :subtitle="sections.timeline.subtitle" data-section="时间线"
               divider light>
      <timeline :data="timelineComputed" :data-aos-duration="800" data-aos="fade-up"/>
    </e-section>

    <e-footer/>

    <scroll-to-top/>
  </div>
</template>

<script>
// @ is an alias to /src
import 'particles.js';
import AOS from 'aos';
import 'aos/dist/aos.css'; // You can also use <link> for styles
import ScrollToTop from '@/components/custom/scroll-to-top'
import AnimateText from '@/components/custom/animate-text'
import ScrollIcon from '@/components/custom/scroll-icon'
// import ContactForm from '@/components/custom/contact-form'
import AnimatedBackground from '@/components/custom/animated-background'
import ColsSection from '@/components/default/cols-section'
import ESection from '@/components/default/e-section'
import Timeline from '@/components/default/timeline'
import EFooter from '@/components/main/e-footer'
import IndexCharts from '@/components/main/indexcharts';

import GlobalComputed from '@/helpers/global-computed'
import GlobalMethods from '@/helpers/global-methods'

import Navbar from '@/components/main/navbar'

export default {
  name: "Home",
  components: {
    ScrollToTop,
    AnimateText,
    Timeline,
    ColsSection,
    ESection,
    ScrollIcon,
    EFooter,
    AnimatedBackground,
    // detailpage,
    Navbar,
    IndexCharts
  },
  data() {
    return {
      tabChart: 0,
      showSection1Actions: false,
      currentScrollTop: 0,
      contactItems: [
        {name: 'email', icon: 'mdi-email', value: 'info@ebmsoft.net', color: 'primary'},
        {
          name: 'facebook',
          icon: 'mdi-facebook',
          value: '@EBMSoft.net',
          color: 'blue darken-1',
          href: 'https://facebook.com/ebmsoft.net'
        },
        {name: 'website', icon: 'mdi-web', value: 'www.ebmsoft.net', color: 'grey', href: 'https://ebmsoft.net'},
      ],
      projects: [
        {
          image: './img/userdetails.png',
          title: "面向个人使用的私人记录",
          subtitle: "一个高效的记录平台，人性化UI"
        },
        {
          image: './img/calendar.png',
          title: "动态台历快捷添加",
          subtitle: "包括但不限于切换各种立体样式、快速增加记录"
        },
        {
          image: './img/list.png',
          title: "特殊记录筛选和过滤",
          subtitle: "记录了以往所有已添加的条目"
        },
      ],
      // teamMembers: [
      //   {
      //     name: 'Ebrahim Maher',
      //     job: 'Founder, CEO & Developer',
      //     avatar: './img/team/ebrahim-maher.jpg',
      //     description: 'Founder and CEO of EBMSoft company '
      //   }
      // ],
      sections: {
        header: {
          title: "Less Alcohol System",
          subtitle: "希望每日记录的形式能够减少酒精摄入和关注家庭的系统"
        },
        chart: {
          title: "在线数据",
          subtitle: "包含每日记录和图表"
        },
        about: {
          title: "简介",
          subtitle: "具体工作",
          row1: {
            title: "工作和具体目标",
            content: "主要目标是帮助家庭成员能够随时随地记录家人每日的喝酒情况。酒精被国际卫生组织划归为可以导致癌症发生的物质,属于一类致癌物，减少不必要的酒精摄入，保重身体健康。尽量减少饮酒可提高心脏健康、改善肝功能、促进更好的睡眠、提高免疫功能和维持健康体重等"
          },
          // row2: {
          //   title: "实时借贷投资系统",
          //   content: "我们将加密货币的外部API集成到我们的系统中，以实现实时借贷和投资。这使用户能够利用市场机会并更有效地管理他们的投资。项目提供有关BTC等货币价格，贷款利率和其他关键指标的实时数据，使我们的系统能够代表用户做出明智的投资决策。"
          // }
        },
        // contact: {
        //     subtitle: "Leave us a message and we'll reply as soon as possible"
        // },
        timeline: {
          title: "开发时间线",
          subtitle: "那些重要的日期和事件"
        },
        products: {
          title: "功能 & 系统",
          subtitle: "实现的所有功能及搭建的完整系统"
        },
        algorithm: {
          title: "核心的框架",
          subtitle: "框架是整个系统中的主心骨",
          cols: [
            {
              headline: "Vuetify组件库",
              description: "Vuetify 是一个基于 Vue.js 的 Material Design 组件库，可以帮助开发者快速构建美观、易用的 Web 应用程序。在使用 Vuetify 的基础上，团队开发了一个BTC模拟金融系统。"
            },
            {
              headline: "Webpack",
              description: "Webpack是一个开源的前端打包工具，可以将多个模块打包成一个或多个静态资源文件，用于网站或应用程序的部署。Webpack支持多种模块化方案，例如CommonJS、AMD、ES6模块等，并且可以通过插件和加载器扩展其功能。"
            },
            {
              headline: "MyBatis-Plus",
              description: "MyBatis-Plus通过代码生成器可以自动生成Mapper接口、实体类和XML映射文件。MyBatis-Plus还提供了丰富的查询API，可以轻松实现分页、排序、条件查询等功能，使得数据访问更加简单和高效。"
            }
          ]
        },
        actions: {
          try: "立即开始",
          about: "关于系统",
          products: "系统功能"
        }
      },
      timeline: [
        {
          date: "2023-10-5",
          title: "开始逐步搭建 平台 / 系统",
          icon: "mdi-numeric-1",
          content: "基于Vuetify组件库和Vue-cli开发平台首页等"
        },
        {
          date: "2023-10-10",
          title: "注册功能 / 登录持久化/ JWT",
          icon: "mdi-numeric-2",
          content: "使用token验证、Springboot JWT"
        },
        {
          date: "2023-10-20",
          title: "实现系统用户基本功能",
          icon: "mdi-numeric-3",
          content: "个人信息管理、每日填写信息、图表显示"
        },
        {
          date: "2023-10-25",
          title: "系统功能完全实现",
          icon: "mdi-numeric-4",
          content: "论坛功能，界面美化"
        },
        {
          date: "2024-08-06",
          title: "三年服务器延长",
          icon: "mdi-numeric-5",
          content: "Cloud Service & Mysql"
        }
      ],
      // contact: {
      //     subheader: "or contact us via:",
      //     email: "Email Address",
      //     facebook: "Facebook Page",
      //     website: "Website"
      // }
    }
  },
  computed: {
    algorithmCols() {
      return [
        {
          color: 'blue',
          icon: 'mdi-tools',
          headline: this.sections.algorithm.cols[0].headline,
          description: this.sections.algorithm.cols[0].description
        },
        {
          color: 'deep-orange',
          icon: 'mdi-data-matrix',
          headline: this.sections.algorithm.cols[1].headline,
          description: this.sections.algorithm.cols[1].description
        },
        {
          color: 'teal',
          icon: 'mdi-webhook',
          headline: this.sections.algorithm.cols[2].headline,
          description: this.sections.algorithm.cols[2].description
        },
      ];
    },
    timelineComputed() {
      return [
        {date: this.timeline[0].date, title: this.timeline[0].title, content: this.timeline[0].content, icon:this.timeline[0].icon},
        {date: this.timeline[1].date, title: this.timeline[1].title, content: this.timeline[1].content, icon:this.timeline[1].icon},
        {date: this.timeline[2].date, title: this.timeline[2].title, content: this.timeline[2].content, icon:this.timeline[2].icon},
        {date: this.timeline[3].date, title: this.timeline[3].title, content: this.timeline[3].content, icon:this.timeline[3].icon},
        {date: this.timeline[4].date, title: this.timeline[4].title, content: this.timeline[4].content, icon:this.timeline[4].icon},
      ];
    },
    ...GlobalComputed
  },
  methods: {
    scrollToSection(n) {
      let i = n - 1,
          element = document.querySelectorAll('.page-sections section')[i];
      if (element) {
        this.scrollToElement(element);
      }
    },
    scrollObserver() {
      window.addEventListener('scroll', () => {
        this.currentScrollTop = document.documentElement.scrollTop;
      })
    },
    handleScrollChange(scrollTop) {
      const sections = document.querySelectorAll('.page-sections section');
      sections.forEach(section => {
        let offsetTop = section.offsetTop - this.navbarHeight, // decrease navbarHeight
            offsetBottom = offsetTop + section.offsetHeight;

        if (scrollTop >= offsetTop && scrollTop < offsetBottom) {
          let sectionName = section.getAttribute('data-section');
          if (sectionName) {
            this.$store.commit('SET_ACTIVE_SECTION', sectionName)
          }
          // section is visible...
          if (section.getAttribute('data-theme') === 'light') {

            if (!this.lightNavbar) {
              this.$store.commit('SET_LIGHT_NAVBAR', true);
            }
          } else {
            if (this.lightNavbar) {
              this.$store.commit('SET_LIGHT_NAVBAR', false);
            }
          }
        }
      })

    },

    ...GlobalMethods
  },
  watch: {
    currentScrollTop(top) {
      this.handleScrollChange(top);
    },
    tabChart: {
      handler(newVal, oldVal) {
        this.$refs.indexChart.reInitCharts(newVal)
      },
    }
  },

  mounted() {
    this.scrollObserver();
    AOS.init({container: document.documentElement, once: true, easing: 'ease'});
  }
};
</script>
<style lang="scss" scoped>
section {

  .container {
    z-index: 1;
    display: flex;
    justify-content: center;
    align-items: start;
    flex-direction: column;

    &,
    * {
      z-index: 1;
    }
  }

  &.header-section {
    color: white;
  }

  h1 {
    font-size: 4rem;
    font-weight: 300;

    @media(max-width: 960px) {
      text-align: center;
      font-size: 1.7rem;
      line-height: 2.4rem;
      font-weight: 400;
    }
  }

  h2 {
    font-size: 2.75rem;
    word-break: break-word !important;
    font-weight: 200;

    @media(max-width: 960px) {
      text-align: center;
      font-size: 1.3rem;
      font-weight: 300;
    }
  }

  .actions {
    margin-top: 3rem;

    @media(max-width: 960px) {
      margin: 2rem 0;
      width: 100%;
      text-align: center;
      justify-content: center;
    }

    .v-btn {
      @media(min-width: 960px) {
        min-width: 130px;
        min-height: 45px;
      }
    }
  }

  .section-scroll-icon {
    z-index: 2;
    position: absolute;
    bottom: 8%;
    left: 50%;
    transform: translateX(-50%);
  }
}
</style>