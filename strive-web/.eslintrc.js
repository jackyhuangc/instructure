// http://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,
  parser: 'babel-eslint',
  parserOptions: {
    sourceType: 'module'
  },
  env: {
    browser: true,
  },
  // https://github.com/standard/standard/blob/master/docs/RULES-en.md
  extends: 'standard',
  // required to lint *.vue files
  plugins: [
    'html'
  ],
  // add your custom rules here 此处改写eslint规则，使其更接近于vetur规则，vetur可格式化标签
  'rules': {
    // allow paren-less arrow functions
    'arrow-parens': 0,
    // allow async-await
    'generator-star-spacing': 0,
    // allow debugger during development
    'no-debugger': process.env.NODE_ENV === 'production' ? 2 : 0,
    'space-before-function-paren': [0, 'always'],// 函数定义时括号前面不要有空格
    'semi': [2, 'always'],// 语句强制分号结尾
    // 在JavaScript中有三种方式定义字符串，双引号、单引号、反义符（ECMAScript2015）。规定了字符串定义的方式
    'quotes': [2, 'double'],// 支持双引号
    "no-undef": 0,//关掉不能有未定义的变量
  }
}
