const CopyWebpackPlugin = require('copy-webpack-plugin')
const webpack = require('webpack')
const path = require('path')

let cesiumSource = './node_modules/cesium/Source'

const { resolve } = require('path')
module.exports = {
    publicPath: './',
    devServer: {
        host: '0.0.0.0',
        port: 8080,
        proxy: {
            '/Yangtze': {
                target: 'http://172.21.212.10:8002/',
                changeOrigin: true,
                pathRewrite: {
                    "^/Yangtze": ""
                }
            },
        }
    },
    
    configureWebpack: {
        plugins: [
            new CopyWebpackPlugin({
                patterns: [
                    { from: path.join(cesiumSource, 'Workers'), to: 'Workers' },
                    { from: path.join(cesiumSource, 'Assets'), to: 'Assets' },
                    { from: path.join(cesiumSource, 'Widgets'), to: 'Widgets' },
                    { from: path.join(cesiumSource, 'ThirdParty/Workers'), to: 'ThirdParty/Workers' },
                ],
            }),
            // new CopyWebpackPlugin([ { from: path.join(cesiumSource, 'Workers'), to: 'Workers'}]),
            // new CopyWebpackPlugin([ { from: path.join(cesiumSource, 'Assets'), to: 'Assets'}]),
            // new CopyWebpackPlugin([ { from: path.join(cesiumSource, 'Widgets'), to: 'Widgets'}]),
            // new CopyWebpackPlugin([ { from: path.join(cesiumSource, 'ThirdParty/Workers'), to: 'ThirdParty/Workers'}]),
            new webpack.DefinePlugin({
                CESIUM_BASE_URL: JSON.stringify('./')
            })
        ],
        resolve: {
            alias: {
                '@': resolve('src'),
                'cesium': resolve(cesiumSource)
            }
        }
    }
}