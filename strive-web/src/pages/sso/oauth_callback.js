/*引入Vue框架*/
import Vue from 'vue'
import VueResource from 'vue-resource'
import Oauth_Callback from './oauth_callback.vue'

/*使用VueResource插件*/
Vue.use(VueResource)

Vue.http.headers.common.Authorization = "Basic YWNtZTphY21lc2VjcmV0";

var thisURL = document.URL;
var code = "";
if (thisURL.split("?").length > 1) {
  // 这是单点登录
  var getval = thisURL.split("?")[1];
  var urlParams = getval.split("&");
  for (var i = 0; i < urlParams.length; i++) {
    var key = urlParams[i].split("=")[0];
    var value = urlParams[i].split("=")[1];
    if (key === "code") {
      code = value;
      break;
    }
  }
}

if (!code) {
  console.log("无效的回调信息，请重新认证！");
  window.localStorage.clear();
  window.location.href = "/";
}

new Vue({
  el: '#app',
  template: '<Oauth_Callback/>',
  components: { Oauth_Callback }
})
