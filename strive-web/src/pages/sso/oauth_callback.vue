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
    var state = "";
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
          state = value;
        }
      }
    }

    // 获取access_token
    var path = "http://localhost:8088/admin/oauth/token";
    path +=
      "?client_id=acme&client_secret=acmesecret&grant_type=authorization_code";

    // redirect_uri必须与授权时的redirect_uri匹配
    path += "&redirect_uri=http://localhost:9090/pages/sso/oauth_callback.html";
    path += "&code=" + code + "&state=" + state + "&scope=web"; // scope可以不传，但不能乱传？？？

    this.$http
      .post(path)
      .then(response => {
        // alert(response);
        console.log(response);
        localStorage.setItem("token", response.body.access_token);
        this.$http
          .get("http://localhost:8088/admin/me", {
            headers: {
              Authorization: "Bearer " + response.body.access_token
            }
          })
          .then(rep => {
            debugger;
            console.log(rep);

            rep.body.username = rep.body.investorID;
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