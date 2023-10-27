const HtmlWebpackPlugin = require('html-webpack-plugin');
const VueLoaderPlugin = require('vue-loader/lib/plugin');
const VuetifyLoaderPlugin = require('vuetify-loader/lib/plugin');
const path = require('path')

module.exports = {
    mode: 'development',
    devtool: 'eval-cheap-module-source-map',
    entry: "./src/main.js",
    output: {
        clean: true,
        path: path.resolve(__dirname, './dist'),
        filename: 'scripts/[name].js',
        // publicPath: '/',
    },
    module: {
        rules: [
            {test: /\.vue$/, loader: 'vue-loader'},
            {test: /\.css$/, use: ['style-loader', 'css-loader']},
            {
                test: /\.s[ca]ss$/,
                use: [
                    'style-loader',
                    'css-loader',
                    'sass-loader',
                ],
            },
            {
                test: /\.(png|ico|jpg)$/,
                type: "asset/resource",
                generator: {
                    filename: "images/[id].[hash][ext]"
                }
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude: /node_modules/,
            },
        ]
    },
    plugins: [
        new VueLoaderPlugin(),
        new VuetifyLoaderPlugin(),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            // template: 'public/index.html',
            template: './public/index.html',
            inject: false,
        }),
    ],
    resolve: {
        extensions: ['.js', '.vue', '.json'],
        alias: {
            vue$: 'vue/dist/vue.esm.js',
            '@': path.join(__dirname, './src'),
        },
    },
    devServer: {
        historyApiFallback: true,
        open: true,
        host: "localhost",
        port: 8080,
        https: false,
        // proxy: {
        //     // 代理服务器，当请求的网址是http://localhost:3000的时候，会转成http://192.168.80.115:3000
        //     // /api 是 看接口文档所写的，每当访问本地的/api接口时，会转化为访问真实的服务器
        //     '/api': {
        //         target: 'http://localhost:9090', // 我们要代理的真实接口地址
        //         changeOrigin: true,
        //         ws: true,
        //         secure: true, //是否https接口
        //         pathRewrite: {
        //             "^/api": "",
        //         },
        //     },
        // },
        // host: '0.0.0.0',
        // port: 8080,
        // allowedHosts: 'all',
    },
}
