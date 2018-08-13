import Vue from 'vue'
import VueResource from 'vue-resource'
import VueRouter from 'vue-router'
import Index from './index.vue'
import router from './router'

/*使用VueResource插件*/
Vue.use(VueResource)
Vue.use(VueRouter)


Vue.http.options.xhr = { withCredentials: true }
// Vue.http.options.emulateJSON = true
//emulateHTTP

var token = localStorage.getItem('token');
if (!token) {
  console.log("您未登录或长时间未操作，请重新登录！");
  window.localStorage.clear();
  window.location.href = "/";
}
// else {
//   Vue.http.get("/api/UserCenter/GetUserInfoByToken"
//     + "?token=" + token)
//     .then((rep) => {
//       console.log(rep.data);
//     });
// }

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

new Vue({
  el: '#app',
  router,
  template: '<Index/>',
  components: { Index }
})
