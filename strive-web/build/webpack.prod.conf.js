var path = require('path')
var utils = require('./utils')
var webpack = require('webpack')
var config = require('../config')
var merge = require('webpack-merge')
var baseWebpackConfig = require('./webpack.base.conf')
var CopyWebpackPlugin = require('copy-webpack-plugin')
var HtmlWebpackPlugin = require('html-webpack-plugin')
var ExtractTextPlugin = require('extract-text-webpack-plugin')
var OptimizeCSSPlugin = require('optimize-css-assets-webpack-plugin')

var env = config.build.env

var webpackConfig = merge(baseWebpackConfig, {
  module: {
    rules: utils.styleLoaders({
      sourceMap: config.build.productionSourceMap,
      extract: true
    })
  },
  devtool: config.build.productionSourceMap ? '#source-map' : false,
  output: {
    path: config.build.assetsRoot,
    filename: utils.assetsPath('js/[name].[chunkhash].js'),
    chunkFilename: utils.assetsPath('js/[id].[chunkhash].js')
  },
  plugins: [
    // http://vuejs.github.io/vue-loader/en/workflow/production.html
    new webpack.DefinePlugin({
      'process.env': env
    }),
    new webpack.optimize.UglifyJsPlugin({
      compress: {
        warnings: false
      },
      sourceMap: true
    }),
    // extract css into its own file
    new ExtractTextPlugin({
      filename: utils.assetsPath('css/[name].[contenthash].css')
    }),
    // Compress extracted CSS. We are using this plugin so that possible
    // duplicated CSS from different components can be deduped.
    new OptimizeCSSPlugin({
      cssProcessorOptions: {
        safe: true
      }
    }),
    // generate dist index.html with correct asset hash for caching.
    // you can customize output by editing /index.html
    // see https://github.com/ampedandwired/html-webpack-plugin
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
    //   chunks: []//需要引入的chunk(模块资源)，不配置就会引入所有页面的资源(js/css),这是个很重要的属性，你可以不配置试试效果，这里是配置为不引用      
    // }),
    // new HtmlWebpackPlugin({
    //   filename: './pages/fileupload-rs/index.html',//指定生成的html存放路径
    //   template: './src/pages/fileupload-rs/index.html',//指定html模板路径
    //   inject: true,//是否将js等注入页面,以及指定注入的位置'head'或'body'
    //   chunks: []//需要引入的chunk(模块资源)，不配置就会引入所有页面的资源(js/css),这是个很重要的属性，你可以不配置试试效果
    // }),

    // 暂时没用到
    // // split vendor js into its own file
    // new webpack.optimize.CommonsChunkPlugin({
    //   name: 'vendor',
    //   minChunks: function (module, count) {
    //     // any required modules inside node_modules are extracted to vendor
    //     return (
    //       module.resource &&
    //       /\.js$/.test(module.resource) &&
    //       module.resource.indexOf(
    //         path.join(__dirname, '../node_modules')
    //       ) === 0
    //     )
    //   },
    //   filename: "/static/js/vendor.js"//导出的文件的名称
    // }),
    // // extract webpack runtime and module manifest to its own file in order to
    // // prevent vendor hash from being updated whenever app bundle is updated
    // new webpack.optimize.CommonsChunkPlugin({
    //   name: 'manifest',
    //   chunks: ['vendor'],
    //   filename: "/static/js/manifest.js"//导出的文件的名称
    // }),

    // copy custom static assets
    new CopyWebpackPlugin([{
      from: path.resolve(__dirname, '../static'),
      to: config.build.assetsSubDirectory,
      ignore: ['.*']
    }])
  ]
})

if (config.build.productionGzip) {
  var CompressionWebpackPlugin = require('compression-webpack-plugin')

  webpackConfig.plugins.push(
    new CompressionWebpackPlugin({
      asset: '[path].gz[query]',
      algorithm: 'gzip',
      test: new RegExp(
        '\\.(' +
        config.build.productionGzipExtensions.join('|') +
        ')$'
      ),
      threshold: 10240,
      minRatio: 0.8
    })
  )
}

if (config.build.bundleAnalyzerReport) {
  var BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin
  webpackConfig.plugins.push(new BundleAnalyzerPlugin())
}

module.exports = webpackConfig
