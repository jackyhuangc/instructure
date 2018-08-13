// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from "vue";
import VueResource from "vue-resource";
import Login from "./pages/sso/index.vue";

/* 使用VueResource插件 */
Vue.use(VueResource);
Vue.http.options.xhr = { withCredentials: true };
// Vue.http.options.emulateJSON = true
/* eslint-disable no-new */
// Vue.http.options.emulateHTTP = true;
Vue.http.options.emulateJSON = true;

// 这是我的单点登录服务器(password模式) mybatis.dao:strivesecret
Vue.http.headers.common.Authorization = "Basic c3RyaXZlOnN0cml2ZXNlY3JldA==";

// 这是我的单点登录服务器(authorization_code模式) acme:acmesecret
// Vue.http.headers.common.Authorization = "Basic YWNtZTphY21lc2VjcmV0";

new Vue({
  el: "#app",
  template: "<Login/>",
  components: { Login }
});
