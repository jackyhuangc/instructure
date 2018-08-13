/**
 * 这是全局的js配置文件，常量等配置信息应该包含在该文件中，使用JsDoc规范http://www.css88.com/doc/jsdoc/
 * @author Jacky
 * @version V1.0 2017.12.27
 */

// 命名规范
// http://blog.csdn.net/webgongcheng/article/details/52188464
// http://www.360doc.com/content/13/0217/06/19147_266057492.shtml
// https://www.cnblogs.com/chris-oil/p/5387129.html
// http://www.css88.com/doc/jsdoc/
// 文件夹    abc/cde/fgh1 或者 test-files  全部小写
// 文件      mapApply.css 或者 test.min.js 首字母小写
// js(类)：  AnimalFood                   单词首字母大写
// js(方法): testFunction()               首字母小写
// js(常量): SERVER_ADDRESS               全部大写+下划线
// js(变量): nCounter                     首字母小写
// html标签：btn_search                   全部小写+下划线

// 文件头部注释须标明作者及修改日期。必须为大区块样式添加合理注释;

// 文件夹及文件命名规则
// 用简短有意义的英文或者拼音（不能出现中文命名）来命名。
// 1)文件夹命名规则: 全部小写。例如(emotions, download, mail)。
// 2)html,js,css文件命名规则: 第一个单词首字母小写,之后每个单词首字母大写,html文件后缀名统一为.htm。例如(index.htm, customizeCity.htm, register.js, dateSelect.js, base.css, mapApply.css)。&nbsp;
// 3)图片命名规则: 第一个单词首字母小写,之后每个单词首字母大写,或者全部小写,单词间用下划线连接。例如(btn_sign.gif, bgTipBox.png)
// 4)全局定义以及全站公共部分共用文件common.css,开发过程中,每个页面请务必都要引入,此文件包含reset,常用规则(链接、字体、隐藏、清浮动等)、布局、各种模块基本样式及头部底部样式,此文件不可随意修改;
// 5)CSS属性书写顺序尽量遵循:显示属性-&gt;盒模型-&gt;文字属性-&gt;排版-&gt;其他。例如:#searchBtn{display:block;position:abtolute;left:2px;top:0;width:50px;height:19px;border:1px solid #ccc;padding:5px 2px;font:Arial 12px/19px;text-align:center;vertical-align:middle;color:#666;background:#999;cursor:pointer;}
// 6) 常用功能函数统一写在公用js文件commonToolFn.js里;
// 8) 常量所有字符大写，变量首字母小写;类命名:首字母大写驼峰式命名.如CommonTool;函数命名:首字母小写驼峰式命名.如arrEach();

// 版本控制规范
// 1) 代码提交前清理无关文件(比如缩略图缓存数据文件thumb.db等等);
// 2) 对于CSS以及JS约定好的代码注释必须加上之后再提交到SVN，方便SVN自动更新文件编辑信息以及版本号;
// 3) 变更文件(特别是模版文件以及重要的页面)比较大时最好做好备份工作，方便发布到正式线出现问题时快速撤回;
// 4) 代码修改或提交前获取下最新版本，有规律的提交代码；签入代码前经过良好的测试;

/**
 * API服务器地址
 */
const API_SERVER = "localhost:8081";

/**
 * 测试接口，以后的方法注释要用该方式
 * @param {Int} a 数据1
 * @param {Int} b 数据2
 */
function addTest(a, b) {
  return a + b;
}
