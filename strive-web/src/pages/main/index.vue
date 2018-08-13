<template>
  <div id="app">
    <div class="layui-layout layui-layout-admin">
      <div class="layui-header">
        <div class="layui-logo">Shopping Admin</div>
        <!-- 头部区域（可配合layui已有的水平导航） -->
        <top v-bind:menus="menus" ref="top">
        </top>
        <ul class="layui-nav layui-layout-right">
          <li class="layui-nav-item">
            <a href="javascript:;">
              <img src="http://t.cn/RCzsdCq" class="layui-nav-img">{{user}}
            </a>
            <dl class="layui-nav-child">
              <dd>
                <a href="/pages/user/personal/index.html" target="right_frame">About me</a>
              </dd>
              <dd>
                <a href="javascript:;" @click="changePassword();">Change Password</a>
              </dd>
              <dd>
                <a href="javascript:;" @click="download();">Download</a>
              </dd>
            </dl>
          </li>
          <li class="layui-nav-item">
            <a href="javascript:void(0)" @click="exit();">退出</a>
          </li>
        </ul>
      </div>

      <div class="layui-side layui-bg-black">
        <div class="layui-side-scroll">
          <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
          <left v-bind:menus="menus" ref="left">
          </left>
        </div>
      </div>

      <div class="layui-body">
        <!-- 内容主体区域 -->
        <div style="padding:0px;height:100%;overflow:hidden;">
          <iframe name="right_frame" src="/pages/monitor-center/index.html" frameborder="0" height="100%" width="100%"></iframe>
        </div>

      </div>

      <div class="layui-footer">
        <!-- 底部固定区域 -->
        © copyright - huangchao911@aliyun.com
      </div>
    </div>

    <download ref="down">
    </download>
  </div>
</template>

<script>
import top from "./nav/top.vue";
import left from "./nav/left.vue";
import download from "./tool/tool.download.vue";
export default {
  name: "app1",
  components: {
    top,
    left,
    download
  },
  data() {
    return {
      user: "",
      accessToken: "",
      // 左侧菜单
      menus: {
        selected: "",
        data: []
      }
    };
  },
  methods: {
    changePassword: function() {
      var vm = this;
      layer.prompt({ title: "请输入当前密码:", formType: 1 }, function(
        pass,
        index
      ) {
        layer.close(index);
        // 重新设置新密码
        layer.prompt({ title: "请输入新密码:", formType: 1 }, function(
          newPass,
          index
        ) {
          layer.close(index);
          var userInfo = JSON.parse(localStorage.getItem("user_info"));
          vm.$http
            .post(
              "http://" +
                API_SERVER +
                "/strive/user/modifyPassword/" +
                userInfo.userId +
                "/" +
                pass +
                "/" +
                newPass
            )
            .then(rep => {
              var result = rep.body;
              if (result.code === 0) {
                layer.msg(
                  "modify password successful!",
                  { time: 2000 },
                  function() {
                    layer.closeAll();
                  }
                );
              } else {
                console.log(rep);
                layer.msg(result.message);
              }
            })
            .catch(function(response) {
              console.log(response);
            });
        });
      });
    },
    download: function() {
      layer.open({
        type: 1,
        skin: "layui-layer-azure",
        title: false, // 不显示标题
        area: ["600px", "330px"],
        content: $("#panel_down"), // 捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        success: function(layero, index) {},
        cancel: function() {}
      });
    },
    exit: function() {
      window.localStorage.clear();
      window.location = "/";
    }
  },
  created: function() {
    console.log("created");
    // 初始化操作，如菜单
  },
  mounted() {
    console.log("mounted");
    var vm = this;
    vm.accessToken = localStorage.getItem("token");
    var userInfo = JSON.parse(localStorage.getItem("user_info"));
    this.user = userInfo.userName;
    vm.$http
      .get("http://" + API_SERVER + "/strive/user/" + userInfo.userId, {
        headers: {
          Authorization: "Bearer " + vm.accessToken
        }
      })
      .then(rep => {
        var result = rep.body;
        if (result.code !== 0) {
          layer.msg(result.message);
        } else {
          $(".layui-nav-img").attr(
            "src",
            "http://" + API_SERVER + "/strive" + result.data.image
          );
        }
      });
  },
  beforeUpdate() {
    console.log("beforeUpdate");
  },
  updated: function() {
    console.log("updated");
  },
  destroyed: function() {
    console.log("destroyed");
  }
};
</script>

<style scoped>
</style>
