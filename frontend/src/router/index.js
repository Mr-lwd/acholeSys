import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/views/IndexView.vue";
import Login from "@/views/LoginView.vue";
import UserView from "@/views/UserView";
import Diaries from "@/views/Diaries";

Vue.use(VueRouter);

const routes = [
    {
        path: "/",
        name: "home",
        component: Home
    },
    {
        path: "/login",
        name: "login",
        component: Login
    },
    {
        path: "/user",
        name: "user",
        component: UserView,
        redirect: '/user/userdetails',
        meta: {
            roles: ['user']
        },
        children: [
            {
                path: 'userdetails',
                name: 'userdetails',
                component: () => import("@/components/manage/users/userdetails"),
                meta: {
                    roles: ['user']
                },
            },
            {
                path: 'calendar',
                name: 'calendar',
                component: () => import("@/components/manage/users/calendar"),
                meta: {
                    roles: ['user']
                },
            },
            {
                path: 'list',
                name: 'list',
                component: () => import("@/components/manage/users/list.vue"),
                meta: {
                    roles: ['user']
                },
            },
        ]
    },
    {
        path: "/diaries",
        name:"diaries",
        component: Diaries,
    }
];

const router = new VueRouter({
    mode: "hash",
    // base: process.env.BASE_URL,
    // base: "/",
    routes,
});

export default router;
