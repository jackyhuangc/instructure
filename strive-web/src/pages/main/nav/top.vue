<template>
  <div>
    <ul class="layui-nav layui-layout-left">
      <!--li>
    			<a href="/" target="rightFrame" class="selected" style="width:90px;" @click="menuClick(1)"><img src="../../../../static/Content/images/icons/1_06.png" title="首页" /><h2>首页</h2></a>
    		</li>
    		<li>
    			<a id="test" href="/" target="rightFrame" style="width:90px;" @click="menuClick(2)"><img src="../../../../static/Content/images/icons/1_06.png" title="首页" /><h2>首页</h2></a>
    		 ROUTER路由方式配置
    			<router-link to="/left1"><img src="../../../../static/Content/images/icons/1_06.png" title="首页1" /><h2>首页1</h2></router-link>
    		</li-->

      <li v-for="(item,index) in navs.data" class="layui-nav-item" @click="menuClick($event,item.code)" v-on:mouseenter="showNavBar($event)" v-on:mouseleave="hiddenNavBar($event)">
        <a href="javascript:;" target="right_frame">
          {{item.title}}
        </a>
        <!-- 默认都不显示 layui-nav-bar-->
        <span class="layui-nav-bar" style="top: 55px; width: 100%; opacity: 0; "></span>
      </li>
    </ul>
  </div>

</template>

<script>
export default {
  name: "top",
  props: ["menus"],
  data() {
    return {
      navs: {
        selected: "000000",
        data: []
      }
    };
  },
  components: {},
  methods: {
    exit: function() {},
    menuClick: function(evt, code) {
      var vm = this;
      // 将内容页切换成介绍页面
      $("#right_frame").attr("src", "/static/intruduce.html");

      // 根据权限切换左侧菜单
      var pCodes = vm.permissionCode;
      vm.menus.data = [];
      this.$http.get("../../../../static/globalInit.json").then(
        response => {
          var initData = JSON.parse(response.bodyText);
          initData.permission = vm.filterPermissions(
            initData.permission,
            pCodes
          );
          if (initData && initData.permission) {
            for (var i = 0; i < initData.permission.length; i++) {
              if (initData.permission[i].code === code) {
                vm.menus.data = initData.permission[i].child;

                // 自动展开一级菜单
                $(".layui-nav-itemed").removeClass("layui-nav-itemed");
                $($(".layui-nav-item")[i]).addClass("layui-nav-itemed");

                // 取消左侧选中样式
                $(".layui-this").removeClass("layui-this");
                break;
              }
            }
          }
        },
        response => {}
      );
    },
    filterPermissions: function(permissions, pCodes) {
      if (!pCodes) return null;

      var permissionCodes = pCodes.split(",");

      for (var i = permissions.length - 1; i >= 0; i--) {
        if ($.inArray(permissions[i].code, permissionCodes) === -1) {
          permissions.splice(i, 1);
        } else {
          if (permissions[i].child.length > 0) {
            permissions[i].child = this.filterPermissions(
              permissions[i].child,
              pCodes
            );
          } else {
            if (permissions[i].target === 1) {
              // 增加单点登录链接
              var token = "default";
              if (window.localStorage) {
                token = localStorage.getItem("token");
              }

              permissions[i].url = permissions[i].url + "?token=" + token;
            }
          }
        }
      }
      return permissions;
    },
    showNavBar: function(evt) {
      var span = $(evt.target).children("span");
      span.css("opacity", "1");
    },
    hiddenNavBar: function(evt) {
      var span = $(evt.target).children("span");
      span.css("opacity", "0");
    }
  },
  created: function() {},
  mounted() {
    var vm = this;
    vm.accessToken = localStorage.getItem("token");
    var userInfo = JSON.parse(localStorage.getItem("user_info"));
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
          var pCodes = result.data.permissionCode;
          vm.permissionCode = pCodes;
          this.$http.get("../../../../static/globalInit.json").then(
            response => {
              var initData = JSON.parse(response.bodyText);
              initData.permission = vm.filterPermissions(
                initData.permission,
                pCodes
              );

              if (initData && initData.permission) {
                vm.navs.data = initData.permission;

                // 默认展示最前面的子菜单
                vm.menus.data = initData.permission[0].child;
              }
            },
            response => {}
          );
        }
      });

    console.log("mounted");
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
