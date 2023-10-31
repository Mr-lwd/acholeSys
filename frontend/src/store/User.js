import request from "@/utils/request";
import Vue from 'vue'
import router from '@/router'


export default {
    namespaced: true,
    //创建一个User.js，创建他的state/mutations/actions

    //需要维护的状态
    state: {
        loading: false, //登录状态
        user: null, //当前登录的用户
    },
    //计算属性
    getters: {
        status(state) {
            if (state.loading) {
                return 'loading'
            } else if (state.user) {
                return 'login'
            } else {
                return 'unLogin'
            }
        },
        whichRole(state) {
            switch (state.user.role) {
                case 'user':
                    return 0
                case 'admin':
                    return 1
                // case 'banker':
                //     return 2
            }
        },
        userName(state) {
            return state.user.name
        }
    },

    //改变loading和user状态 
    mutations: {
        //设置loadingp
        setLoading(state, msg) {
            state.loading = msg
        },
        //设置用户
        setUser(state, msg) {
            state.user = msg
        },
    },

    //actions中提交改变的状态  
    actions: {
        //登陆方法ctx相当于store
        async login(ctx, msg) {
            //登陆状态设置为true
            ctx.commit("setLoading", true)
            await request.post('/user/login', msg)
                .then(response => {
                    console.log('login',response)
                    if (response.code == '200') {
                        //登录成功
                        localStorage.setItem("user", JSON.stringify(response.data))
                        ctx.commit('setUser', response.data)
                        //登陆成功后登陆状态设置为false
                        ctx.commit('setLoading', false)
                        Vue.notify({
                            title: '验证成功',
                            text: '您即将跳转页面!',
                            type: 'success'
                        })
                        setTimeout(() => {
                            router.push({
                                path: "/" + response.data.role,
                            });
                        }, 600);
                    } else {
                        Vue.notify({
                            title: '验证错误',
                            text: '请检查用户信息!',
                            type: 'error'
                        })
                    }
                    ctx.commit('setLoading', false)
                    //返回登陆是否成功
                })
                .catch(error => {
                    ctx.commit('setLoading', false)
                    console.log(error)
                });
        },
        async register(ctx, msg) {
            await request.post('/user/register', msg)
                .then(response => {
                    // console.log(response)
                    if (response.code == '200') {
                        Vue.notify({
                            title: '注册成功',
                            text: '您可以返回登录!',
                            type: 'success'
                        })
                    } else {
                        Vue.notify({
                            title: '信息有误',
                            text: response.msg,
                            type: 'warning'
                        })
                    }
                })
                .catch(error => {
                    console.log(error)
                });
        },
        //判断是否已登录
        isLogin(ctx) {
            //正在登录中
            ctx.commit('setLoading', true)
            const user = JSON.parse(localStorage.getItem('user'))
            // console.log('isLogin', user)
            if (user) {
                ctx.commit('setUser', user)
                ctx.commit('setLoading', false)
                Vue.notify({
                    title: '您已经登陆',
                    text: '即将跳转后台界面!',
                    type: 'success'
                })
                setTimeout(() => {
                    router.push({
                        path: "/" + user.role,
                    });
                }, 600);
            } else {
                ctx.commit('setLoading', false)//user == null
                console.log('未登录')
            }
        },
        checkLogin(ctx) {
            const user = JSON.parse(localStorage.getItem('user'))
            if (user) {
                ctx.commit('setUser', user)
            } else {
                console.log('未登录')
            }
        },
        //注销
        logout(ctx) {
            Vue.notify({
                title: '退出登录',
                text: '即将跳转登录界面!',
                type: 'success'
            })
            setTimeout(() => {
                localStorage.removeItem("user")
                ctx.commit('setLoading', false)
                ctx.commit('setUser', null);
                ctx.commit('setLoading', false);
                router.push({
                    path: "/login",
                });
            }, 600);
        },
        saveDetails(ctx, form) {
            return new Promise((resolve, reject) => {
                request.post('/user', form).then(
                    res => {
                        console.log(res)
                        if (res) {
                            // Vue.notify({
                            //     title: '修改成功',
                            //     type: 'success'
                            // })
                        }
                        resolve(res)
                    }
                ).catch(err => {
                    reject(err)
                })
            })
        },
        savePasswd(ctx, args) {
            // console.log('args', args)
            return new Promise((resolve, reject) => {
                request.post(`/user/updatePasswd?id=${args.id}&oldPasswd=${args.oldPasswd}&newPasswd=${args.newPasswd}`).then(
                    res => {
                        // console.log(res)
                        if (res.code == 200) {
                            // Vue.notify({
                            //     title: '修改成功',
                            //     type: 'success'
                            // })
                        }else{
                            Vue.notify({
                                title: '旧密码错误',
                                type: 'error'
                            })
                        }
                        resolve(res)
                    }
                ).catch(err => {
                    reject(err)
                })
            })
        },
        refreshDetails({state, commit}) {
            return new Promise((resolve, reject) => {
                const id = state.user.id
                request.get(`/user/${id}`).then(
                    res => {
                        console.log('refresh', res)
                        localStorage.removeItem("user")
                        localStorage.setItem("user", JSON.stringify(res))
                        commit('setUser', res)
                        // console.log(res)
                        resolve(res)
                    }
                ).catch(err => {
                    reject(err)
                })
            })
        }
    }
}