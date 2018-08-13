<style scoped>
.form {
  width: 100%;
  max-width: 400px;
  margin: 0px auto 0px auto;
}

.login-content {
  width: 100%;
  max-width: 400px;
  background-color: white;
  float: left;
  text-align: center;
  padding-top: 50px;
  border-radius: 4px;
  height: 300px;
}

.input-group {
  margin: 0px 0px 30px 0px !important;
}

.form-control,
.input-group {
  height: 40px;
}

.form-group {
  margin-bottom: 0px !important;
}

.link p {
  line-height: 20px;
  margin-top: 30px;
}

.btn-sm {
  width: 100%;
  font-size: 16px !important;
}

#userError {
  text-align: left;
  color: red;
  margin-top: 10px;
}

.login-parent {
  width: 0;
  height: 0;
  position: fixed;
  left: 50%;
  right: 50%;
  top: 50%;
  bottom: 50%;
}

.login-center {
  width: 1024px;
  height: 394px;
  margin-left: -512px;
  margin-top: -187px;
}

.remember-text {
  vertical-align: middle;
  font-size: 12px;
  font-family: Tahoma;
  color: #337ab7;
}

#remember {
  vertical-align: middle;
}

.logo,
.logo:visited,
.logo:focus,
.logo:hover {
  color: #326bd1;
  text-decoration: none;
}

.logo {
  font-size: 34px;
  font-family: "Microsoft YaHei";
  font-weight: bold;
  color: #326bd1;
}
</style>
<template>
 	<div id="app">

	</div>
</template>

<script>
export default {
  name: "app1",
  components: {},
  data() {
    return {
      loginUrl: "http://localhost:8080/admin/oauth/token",
      logoutUrl: "http://localhost:8080/admin/logout",
      loginModel: {
        username: "0000",
        password: "123456",
        grant_type: "password",
        client_id: "clientIdPassword"
      }
    };
  },
  methods: {
    login: function() {},
    enter_keyup: function(ev) {},
    requestError: function(xhr, errorType, error) {
      console.log(xhr.responseText);
    }
  },
  created: function() {
    console.log("created");
  },
  mounted: function() {
    console.log("mounted");

    var thisURL = document.URL;
    var code = "";
    // var state = "";
    if (thisURL.split("?").length > 1) {
      // 这是单点登录
      var getval = thisURL.split("?")[1];
      var urlParams = getval.split("&");
      for (var i = 0; i < urlParams.length; i++) {
        var key = urlParams[i].split("=")[0];
        var value = urlParams[i].split("=")[1];
        if (key === "code") {
          code = value;
        }
        if (key === "state") {
          // state = value;
        }
      }
    }

    // Github client信息
    var clientId = "94007a440d469597c7d7";
    var clientSecret = "14c3f4ce63de70d8ec8239e857b74599bae1a18d";

    // 方式1：由于github的跨域设置，jsonp方式也不行，必须用方式2中的代理解决
    // path = "https://github.com/login/oauth/access_token";

    // path += "?client_id=" + clientId;
    // path += "&client_secret=" + clientSecret;
    // path += "&code=" + code;

    // this.$jsonp(path).then(data => {
    //   console.log(data);
    // });

    // 方式2：注意：github返回的token不是json格式，且存在跨域问题，不能用ajax方式请求，需通过后端进行转换，前端解析
    // var path = "http://localhost:8088/admin/github/token";

    // path += "?clientId=" + clientId;
    // path += "&clientSecret=" + clientSecret;
    // path += "&code=" + code;

    var path = "https://github.com/login/oauth/access_token";

    path += "?client_id=" + clientId;
    path += "&client_secret=" + clientSecret;
    path += "&code=" + code;

    var url =
      "http://localhost:8088/admin/proxy?path=" + encodeURIComponent(path);
    this.$http
      .get(url)
      .then(response => {
        // alert(response);
        console.log(response);

        var result = response.bodyText;
        var accessToken = "";
        // var tokenType = "";
        if (result.split("&").length > 1) {
          // 这是单点登录
          var urlParams = result.split("&");
          for (var i = 0; i < urlParams.length; i++) {
            var key = urlParams[i].split("=")[0];
            var value = urlParams[i].split("=")[1];
            if (key === "access_token") {
              accessToken = value;
            }
            if (key === "token_type") {
              // tokenType = value;
            }
          }
        }

        console.log(accessToken);
        localStorage.setItem("token", accessToken);
        this.$http
          .get("https://api.github.com/user", {
            headers: {
              Authorization: "Bearer " + accessToken
            }
          })
          .then(rep => {
            console.log(rep);

            rep.body.username = rep.body.login;
            // 临时保存用户信息
            localStorage.setItem("user_info", JSON.stringify(rep.body));

            // 获取到用户信息后，再在本地跳转，可以进一步要求用户完善个人信息，也可以直接跳转到功能页面
            window.location.href = "/pages/main/index.html";
          });
      })
      .catch(function(response) {
        console.log(response);
      });
  },
  updated: function() {
    console.log("updated");
  }
};
</script>