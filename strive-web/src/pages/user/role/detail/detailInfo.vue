<template>
  <div id="roleinfo">

    <blockquote class="layui-elem-quote layui-text">
      This is {{datasource.optType}} operation
    </blockquote>

    <form class="layui-form" action="">

      <div class="layui-form-item">

        <label class="layui-form-label">Role ID</label>
        <div class="layui-input-block">
          <input type="text" name="roleId" id="roleId" readonly="readonly" v-model="datasource.roleId" autocomplete="off" placeholder="" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">Role Name</label>
        <div class="layui-input-block">
          <input type="text" name="roleName" id="roleName" v-model="datasource.roleName" lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">Enabled</label>
        <div class="layui-input-block">
          <input type="checkbox" name="isEnabled" id="isEnabled" lay-filter="isEnabled" lay-skin="switch" lay-text="YES|NO" v-model="datasource.isEnabled">
        </div>
      </div>

      <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">Remark</label>
        <div class="layui-input-block">
          <textarea placeholder="" name="remark" id="remark" v-model="datasource.remark" class="layui-textarea"></textarea>
        </div>
      </div>
      <div class="layui-form-item">
        <div class="layui-input-block">
          <button class="layui-btn" lay-submit="" lay-filter="formDemo">SUBMIT</button>
          <button type="reset" class="layui-btn layui-btn-primary">RESET</button>
        </div>
      </div>
    </form>

    <div id="permissionTree" name="permissionTree" class="ztree" style="border:1px solid #d5d5d5;overflow-y:auto;">
    </div>
  </div>
</template>

<script>
export default {
  name: "roleinfo",
  data() {
    return {
      show: true,
      title: "",
      roles: [],
      datasource: {
        optType: "add"
      }
    };
  },
  methods: {
    bindDataSource: function(data) {
      var accessToken = localStorage.getItem("token");
      var vm = this;
      vm.datasource = data;

      // 初始化时，禁用状态
      $("input[name=isEnabled]").attr("checked", false);

      if (!this.datasource.roleId) {
        vm.datasource.optType = "add";
        vm.datasource.isEnabled = true;
        vm.$http
          .get("http://" + API_SERVER + "/strive/role/generateNewRoleID", {
            headers: {
              Authorization: "Bearer " + accessToken
            }
          })
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              vm.layer.msg(result.message);
            } else {
              vm.datasource.roleId = result.data;
              $("#roleId").val(result.data);
            }
          });
      } else {
        vm.datasource.optType = "update";
      }

      var setting = {
        check: {
          enable: true
        },
        data: {
          simpleData: {
            enable: true
          }
        }
      };

      var zNodes = [];

      vm.$http.get("../../../../../static/globalInit.json").then(
        response => {
          var initData = JSON.parse(response.bodyText);
          if (initData && initData.permission) {
            zNodes = vm.initZTree(initData.permission);
            if (vm.datasource.permissionCode) {
              var permissionCodes = vm.datasource.permissionCode.split(",");
              for (var i = 0; i < zNodes.length; i++) {
                if ($.inArray(zNodes[i].id, permissionCodes) > -1) {
                  zNodes[i].checked = true;
                }
              }
            }
            $.fn.zTree.init($("#permissionTree"), setting, zNodes);
          }
        },
        response => {}
      );

      $.fn.zTree.init($("#permissionTree"), setting, zNodes);

      $("#isEnabled").attr("checked", vm.datasource.isEnabled);
      // 重新渲染页面
      layui.use(["form"], function() {
        var form = layui.form;
        // form.render("select");
        // form.render("checkbox");

        // 重新渲染表单控件
        form.render();
      });
    },
    initZTree: function(data) {
      if (data) {
        var zNodes = [];
        for (var i = 0; i < data.length; i++) {
          var item = data[i];
          zNodes.push({
            id: item.code,
            pId: item.parentCode,
            name: item.title,
            open: true,
            checked: false
          });
          if (item.child.length > 0) {
            var ret = this.initZTree(item.child);
            zNodes = $.merge(zNodes, ret);
          }
        }
        return zNodes;
      }
    }
  },
  created: function() {},
  mounted: function() {
    var vm = this;

    layui.use(["upload", "form"], function() {
      // layui自带的jquery
      // var $ = layui.jquery;
      var form = layui.form;

      // select、checkbox、radio、date类型都通过监听方式赋值
      // 监听checkbox开关
      form.on("switch(isEnabled)", function(obj) {
        vm.datasource.isEnabled = obj.elem.checked;
      });

      // 监听提交
      form.on("submit(formDemo)", function(data) {
        var treeObj = $.fn.zTree.getZTreeObj("permissionTree");
        var nodes = treeObj.getCheckedNodes(true);
        vm.datasource.permissionCode = "";

        if (nodes.length === 0) {
          layer.msg("Select Permission, Please!");
          return false;
        }
        if (nodes != null) {
          for (var i = 0; i < nodes.length; i++) {
            vm.datasource.permissionCode += nodes[i].id + ",";
          }
        }

        if (vm.datasource.optType === "add") {
          vm.$http
            .post("http://" + API_SERVER + "/strive/role/add", vm.datasource)
            .then(rep => {
              debugger;
              var result = rep.body;
              if (result.code === 0) {
                layer.msg("add successful!", { time: 2000 }, function() {
                  layer.closeAll();
                });
              } else {
                console.log(rep);
                layer.msg(result.message);
              }
            })
            .catch(function(response) {
              console.log(response);
            });
        } else {
          vm.$http
            .post("http://" + API_SERVER + "/strive/role/update", vm.datasource)
            .then(rep => {
              var result = rep.body;
              if (result.code === 0) {
                layer.msg("update successful!", { time: 2000 }, function() {
                  layer.closeAll();
                });
              } else {
                layer.msg(result.message);
              }
            })
            .catch(function(response) {
              console.log(response);
            });
        }

        return false;
      });
    });
  },
  updated: function() {},
  destroyed: function() {}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#roleId,
#roleName,
#remark {
  width: 314px;
}
#remark {
  height: 145px;
}
.ztree {
  font-size: 14px;
  font-family: "Helvetica Neue", Helvetica, "PingFang SC", 微软雅黑, Tahoma;
}
#permissionTree {
  position: absolute;
  right: 20px;
  top: 61.6px;
  height: 290px;
  width: 300px;
}
</style>
