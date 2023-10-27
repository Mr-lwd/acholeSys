<template>
    <footer class="e-footer footer-bg strong-text--text" :class="{ 'phone': phoneOnly }">
        <v-container data-aos="fade-up" data-aos-duration="600" class="row-container text--text py-10">
            <v-row>
                <v-col :cols="pcOnly ? 4 : 12" class="pe-lg-12"
                    :class="{ 'd-flex text-center justify-center align-center flex-column': phoneOnly }">
                    <logo width="160" />
                    <div class="footer-content mt-6 mt-lg-10">
                        {{ description }}
                    </div>
                </v-col>
                <template>
                    <v-col :cols="pcOnly ? 4 : 12" class="ps-lg-12">
                        <v-subheader class="text--text"> {{ contact.subheader }} </v-subheader>
                        <v-list color="footer-bg" dark class="text--text" two-line>
                            <v-list-item v-for="item in contactItems" :key="item.name" :target="item.href ? '_blank' : ''"
                                :href="item.href ? item.href : (item.name === 'email' ? `mailto:${item.value}` : '')">
                                <v-list-item-icon>
                                    <v-icon :color="item.color">{{ item.icon }}</v-icon>
                                </v-list-item-icon>
                                <v-list-item-content>
                                    <v-list-item-subtitle class="mb-2">{{ item.name }}</v-list-item-subtitle>
                                    <v-list-item-title>{{ item.value }}</v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-list>
                    </v-col>
                    <v-col :cols="pcOnly ? 4 : 12" class="px-lg-12">
                        <v-subheader class="text--text"> {{ footerlinks }} </v-subheader>
                        <v-list color="footer-bg" dark>
                            <v-list-item v-for="link in links" :key="link.name" large tile text class="ms-2"
                                @click="navigateToLink(link.ref)">
                                <v-list-item-title class="strong-text--text">{{ link.name }}</v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </v-col>
                </template>
            </v-row>
        </v-container>
        <v-spacer />
        <div class="footer-watermark flex-column">
            <v-container class="text-center py-0">
                <div v-if="pcOnly" class="details-text text--text mb-3">{{ createdby }}</div>
                <!-- <div>© 2020 {{ copyrights }}</div> -->
            </v-container>
        </div>
    </footer>
</template>

<script>

import GlobalComputed from '@/helpers/global-computed'
import GlobalMethods from '@/helpers/global-methods'
import Logo from './logo'
import links from '@/helpers/links'

export default {
    name: 'e-footer',
    props: {
        light: {
            type: Boolean,
            default: false,
        }
    },
    components: { Logo },

    data() {
        return {
            links,
            contactItems: [
                { name: '邮件地址', icon: 'mdi-email', value: '1850245174@qq.com', color: '#fed8b1' },
                { name: 'github', icon: 'mdi-github', value: 'https://github.com/Mr-lwd/acholeSys', color: 'white', href: '' },
                { name: '运行网站', icon: 'mdi-web', value: 'http://116.205.234.27/', color: 'grey', href: '' },
            ],
            copyrights: "EBMSoft - All rights reserved.",
            description: "LASYS是一个专注于管控酒精摄入的个人向系统。为阖家辛福提供每日记录等功能支持。尽量减少饮酒可提高心脏健康、改善肝功能、促进更好的睡眠、提高免疫功能和维持健康体重等。",
            createdby: "this website was developed by Wendian Luo.",
            footerlinks: "导航至：",
            contact: {
                "subheader": "联系我们：",
                "email": "Email Address",
                "facebook": "Facebook Page",
                "website": "Website"
            }
        }
    },
    computed: {
        ...GlobalComputed,
    },
    methods: {

        ...GlobalMethods,
    },
}
</script>
<style lang="scss" scoped>
.e-footer {
    min-height: 500px;
    display: flex;
    justify-content: center;
    align-content: stretch;
    flex-direction: column;

    &.phone {
        min-height: 400px;
    }

    .footer-watermark {
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: rgba(0, 0, 0, .3);
        line-height: 1.6rem;
        justify-content: center;
        align-items: center;
        width: 100%;
        padding: 2rem 0;
    }

    .row-container {
        line-height: 1.9rem;
    }
}
</style>