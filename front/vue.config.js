const { resolve } = require('path')
module.exports = {
    publicPath: './',
    devServer: {
        host: '0.0.0.0',
        port: 8080,
        proxy: {
            '/Yangtze': {
                target: 'http://localhost:8002/',
                changeOrigin: true,
                pathRewrite: {
                  "^/Yangtze": ""
                }
            },
        }
    },
    configureWebpack() {
        return {
            resolve: {
                alias: {
                    '@': resolve('src'),
                }

            }
        }
    }
}