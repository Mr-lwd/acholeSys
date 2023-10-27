import Vue from "vue";
import App from "./App.vue";
import "./registerServiceWorker";
import router from "./router";
import store from "./store";
import vuetify from "./plugins/vuetify";
// import axios from 'axios'
import request from "@/utils/request";
import Notifications from 'vue-notification'
// Vue.prototype.$http = axios
// axios.defaults.baseURL = '/api'
import './assets/global.css'
import 'viewerjs/dist/viewer.css'
import VueViewer from 'v-viewer'

Vue.config.productionTip = false;
Vue.use(Notifications)
Vue.prototype.$request = request
Vue.use(VueViewer)
// Vue.component('file-upload', VueUploadComponent)

router.beforeEach((to, from, next) => {
    const user = JSON.parse(localStorage.getItem('user'))
    let role = null;
    if (user) {
        role = user.role // 从用户信息中获取角色信息
    }

    const requiredRoles = to.matched.find(record => record.meta.roles);
    if (!requiredRoles || requiredRoles.length === 0) {
        // 如果该路由没有设置需要的角色信息，则放行
        next();
    } else if (role && requiredRoles.meta.roles.includes(role)) {
        // console.log(requiredRoles.meta.roles)
        // 如果用户的角色中包含需要的角色，则放行
        next();
    } else {
        // 否则，阻止用户访问该路由，并跳转到指定页面
        store.state.loading = true
        Vue.notify({
            title: "您未登录或权限不足！",
            text: '您即将跳转到首页!',
            type: 'warning'
        })
        setTimeout(() => {
            store.state.loading = false
            next("/login");
        }, 800);
    }
});

new Vue({
    el: '#app',
    router,
    store,
    vuetify,
    components: {App: App},
    template: '<App/>',
});



