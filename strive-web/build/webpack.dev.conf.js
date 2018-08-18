var utils = require('./utils')
var webpack = require('webpack')
var config = require('../config')
var merge = require('webpack-merge')
var baseWebpackConfig = require('./webpack.base.conf')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var FriendlyErrorsPlugin = require('friendly-errors-webpack-plugin')

// add hot-reload related code to entry chunks
Object.keys(baseWebpackConfig.entry).forEach(function (name) {
  baseWebpackConfig.entry[name] = ['./build/dev-client'].concat(baseWebpackConfig.entry[name])
})

module.exports = merge(baseWebpackConfig, {
  module: {
    rules: utils.styleLoaders({
      sourceMap: config.dev.cssSourceMap
    })
  },
  // cheap-module-eval-source-map is faster for development
  devtool: '#cheap-module-eval-source-map',
  plugins: [
    new webpack.DefinePlugin({
      'process.env': config.dev.env
    }),

    //这不需要分析，几乎所有服务器通用的，当前路径无返回，则默认去找当前路径下index.html,index.htm等。
    //意思是你访问'http://127.0.0.1:8088',当前服务器程序无任何路由拦截输出，然后发现有绑定的静态目录文件夹，然后去那文件夹的根目录找index.html，没找到再依次找其他后缀的index文件。
    // 这是默认首页 new HtmlWebpackPlugin(), // Generates default index.html 
    new HtmlWebpackPlugin({
      filename: 'index.html',
      template: 'index.html',
      inject: true,
      chunks: ['main']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/main/index.html',
      template: './src/pages/main/index.html',
      inject: true,
      chunks: ['pages/main/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/monitor-center/index.html',
      template: './src/pages/monitor-center/index.html',
      inject: true,
      chunks: ['pages/monitor-center/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/member/index.html',
      template: './src/pages/member/index.html',
      inject: true,
      chunks: ['pages/member/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/product/index.html',
      template: './src/pages/product/index.html',
      inject: true,
      chunks: ['pages/product/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/user/index.html',
      template: './src/pages/user/index.html',
      inject: true,
      chunks: ['pages/user/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/user/personal/index.html',
      template: './src/pages/user/personal/index.html',
      inject: true,
      chunks: ['pages/user/personal/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/user/role/index.html',
      template: './src/pages/user/role/index.html',
      inject: true,
      chunks: ['pages/user/role/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/systemlog/index.html',
      template: './src/pages/systemlog/index.html',
      inject: true,
      chunks: ['pages/systemlog/index']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/sso/oauth_callback.html',
      template: './src/pages/sso/oauth_callback.html',
      inject: true,
      chunks: ['pages/sso/oauth_callback']
    }),
    new HtmlWebpackPlugin({
      filename: './pages/sso/oauth_callback.github.html',
      template: './src/pages/sso/oauth_callback.github.html',
      inject: true,
      chunks: ['pages/sso/oauth_callback_github']
    }),
    // new HtmlWebpackPlugin({
    //   filename: './pages/fileupload/index.html',//指定生成的html存放路径
    //   template: './src/pages/fileupload/index.html',//指定html模板路径
    //   inject: true,//是否将js等注入页面,以及指定注入的位置'head'或'body'
    //   chunks: []//需要引入的chunk(模块资源)，不配置就会引入所有页面的资源(js/css),这是个很重要的属性，你可以不配置试试效果
    // }),
    // new HtmlWebpackPlugin({
    //   filename: './pages/fileupload-rs/index.html',//指定生成的html存放路径
    //   template: './src/pages/fileupload-rs/index.html',//指定html模板路径
    //   inject: true,//是否将js等注入页面,以及指定注入的位置'head'或'body'
    //   chunks: []//需要引入的chunk(模块资源)，不配置就会引入所有页面的资源(js/css),这是个很重要的属性，你可以不配置试试效果
    // }),
    // https://github.com/glenjamin/webpack-hot-middleware#installation--usage
    new webpack.HotModuleReplacementPlugin(),
    new webpack.NoEmitOnErrorsPlugin(),
    // https://github.com/ampedandwired/html-webpack-plugin
    new FriendlyErrorsPlugin()
  ]
})
