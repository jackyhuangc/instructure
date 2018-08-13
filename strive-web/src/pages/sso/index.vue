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
    <div class="login-parent">
      <div class="container-fluid login-center">
        <div class="row-fluid" style="text-align:center; ">

          <div style="margin-bottom:50px;">
            <a href="#" target="_parent" class="logo"><img src="../../../static/Content/images/logoV1.png" style="width:80px;" />后台</a>
          </div>
        </div>
        <div class="row-fluid">
          <div class="form">
            <div class="login-content">
              <form action="#" method="post" autocomplete="off">
                <div class="form-group">
                  <div class="col-md-12">
                    <div class="input-group">
                      <span class="input-group-addon">
                        <span class="fa fa-user"></span>
                      </span>
                      <input autocomplete="off" type="text" class="form-control UserName" placeholder="username|telphone" v-model="loginModel.username" @keyup="enter_keyup($event)" />
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-md-12">
                    <div class="input-group">
                      <span class="input-group-addon">
                        <span class="fa fa-lock"></span>
                      </span>
                      <input type="password" class="form-control Password" placeholder="password" v-model="loginModel.password" @keyup="enter_keyup($event)" />
                    </div>
                  </div>
                </div>
                <div class="form-group form-actions">
                  <div class="col-md-12">
                    <p class="text-left remove-margin" style="display:block;height:23px; vertical-align:middle;font-size: 12px;color:#337ab7">
                      <!--
                      <label v-show="sRemember">Auto Login</label>
                      <input type="checkbox" id="remember" name="remember" v-show="sRemember" v-model="remember" style="vertical-align:-3px;" />
                    -->
                    </p>

                    <input type="button" id="login" @click="login()" class="btn btn-sm btn-info" v-model="bottonText" />
                  </div>
                </div>
                <div class="form-group">
                  <div class="col-md-12">
                    <p id="userError">{{error}}</p>
                  </div>
                </div>
              </form>
            </div>
            <!--
            <div class="social-login center" style="text-align: center;font-family:Ya Hei;">
              Or connect with:
              <a class="btn btn-primary" @click="github()">
                <i class="ace-icon fa fa-github"></i>
              </a>
              <a class="btn btn-info" @click="local()">
                <i class="ace-icon fa fa-qq"></i>
              </a>
            </div>
            -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "app1",
  components: {},
  data() {
    return {
      loginUrl: "http://localhost:8088/admin/oauth/token",
      logoutUrl: "http://localhost:8088/admin/logout",
      loginModel: {
        username: "",
        password: "",
        grant_type: "password",
        client_id: "strive"
      },
      sRemember: true,
      remember: false,
      error: "",
      bottonText: "Login"
    };
  },
  methods: {
    login: function() {
      var vm = this;
      vm.msg = "";
      vm.result = "";
      console.log(this.$http.headers.common.Authorization);

      // this.$http.headers.common.Authorization = "Basic cmVhZGVyOnJlYWRlcg==";
      this.$http
        .post("http://localhost:8088/admin/oauth/token", this.loginModel)
        .then(response => {
          console.log(response);
          localStorage.setItem("token", response.body.access_token);

          vm.$http
            .get("http://localhost:8088/admin/me", {
              headers: {
                Authorization: "Bearer " + response.body.access_token
              }
            })
            .then(rep => {
              console.log(rep);
              // 临时保存用户信息
              localStorage.setItem("user_info", JSON.stringify(rep.body));

              var strUser = localStorage.getItem("user_info");
              console.log(strUser);
              // 获取到用户信息后，再在本地跳转，可以进一步要求用户完善个人信息，也可以直接跳转到功能页面
              window.location.href = "/pages/main/index.html";
            });
        })
        .catch(function(response) {
          console.log(response);
          vm.error = response.body.error_description;
        });
    },
    github: function() {
      var path = "https://github.com/login/oauth/authorize";
      path +=
        "?client_id=94007a440d469597c7d7&client_secret=14c3f4ce63de70d8ec8239e857b74599bae1a18d";
      path += "&state=abcde";

      window.location.href = path;
    },
    local: function() {
      var path = "http://localhost:8088/admin/oauth/authorize";
      path += "?client_id=acme&client_secret=acmesecret&scope=web";
      // path += '&scope=' + OAuthConfig.GITHUB_CLIENT_SCOPE
      path +=
        "&state=abcde&response_type=code&redirect_uri=http://localhost:9090/pages/sso/oauth_callback.html";

      window.location.href = path;
    },
    enter_keyup: function(ev) {
      if (ev.keyCode === 13) {
        this.login();
      }
    },
    requestError: function(xhr, errorType, error) {
      console.log(xhr.responseText);
    }
  },
  created: function() {
    console.log("created");
  },
  mounted: function() {
    console.log("mounted");
    // this.username = "admin";
    // this.password = "111111";
    // this.login();

    var accessToken = localStorage.getItem("token");
    if (accessToken) {
      this.$http
        .get("http://localhost:8088/admin/me", {
          headers: {
            Authorization: "Bearer " + accessToken
          }
        })
        .then(
          rep => {
            debugger;
            console.log(rep);

            // 如果token没过期,能正常使用，则直接进入首页
            rep.body.username = rep.body.investorID;
            // 临时保存用户信息
            localStorage.setItem("user_info", JSON.stringify(rep.body));
            // 获取到用户信息后，再在本地跳转，可以进一步要求用户完善个人信息，也可以直接跳转到功能页面
            window.location.href = "/pages/main/index.html";
          },
          error => {
            console.log(error);
            window.localStorage.clear();
          }
        );
    }
  },
  updated: function() {
    console.log("updated");
  }
};
</script>