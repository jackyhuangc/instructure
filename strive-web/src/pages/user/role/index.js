/*引入Vue框架*/
import Vue from 'vue'
import VueResource from 'vue-resource'
import Index from './index.vue'

/*使用VueResource插件*/
Vue.use(VueResource)


var token = localStorage.getItem('token');
if (!token) {
  console.log("您未登录或长时间未操作，请重新登录！");
  window.localStorage.clear();
  window.location.href = "/";
}

Vue.http.interceptors.push(function (request, next) {

  var token = "default";
  if (window.localStorage) {
    token = localStorage.getItem('token');
  }

  Vue.http.headers.common.Authorization = 'Bearer ' + token;
  next(function (response) {
    console.log(response);
    // 如果返回401未授权，则跳转登录页面
    if (response.status == 401) {
      console.log("您未登录或长时间未操作，请重新登录！");
      window.localStorage.clear();
      window.location.href = "/";
    }
  })

})

// 根实例只能有一个
var vm = new Vue({
  el: '#app',
  template: '<Index/>',
  components: { Index } // 用Index组件渲染#app跟实例
});