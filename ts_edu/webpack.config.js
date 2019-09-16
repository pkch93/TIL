const webpack = require('webpack')
const path = require('path')
const port = process.env.PORT || 8080

module.exports = {
    mode: 'development',
    entry: './src/index.ts',
    output: {
        path: __dirname + '/dist',
        filename: 'bundle.[hash].js'
    },
    resolve: {
        extensions: [
            '.ts',
            '.js',
            '.json',
        ],
        alias: {
            "@src": path.resolve(__dirname, 'src')
        }
    }
}
