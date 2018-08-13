<template>
  <div id="userinfo">

    <blockquote class="layui-elem-quote layui-text">
      This is {{datasource.optType}} operation
    </blockquote>

    <form class="layui-form" action="">

      <div class="layui-form-item">

        <div class="layui-upload">
          <div class="layui-upload-list">
            <img class="layui-upload-img" name="imgdemo" id="imgdemo">
            <input type="hidden" name="image" id="image" v-model="datasource.image">
          </div>
          <button type="button" class="layui-btn" id="btn-upload">Image</button>
        </div>
        <label class="layui-form-label">User ID</label>
        <div class="layui-input-block">
          <input type="text" name="userId" id="userId" readonly="readonly" v-model="datasource.userId" autocomplete="off" placeholder="" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">User Name</label>
        <div class="layui-input-block">
          <input type="text" name="userName" id="userName" v-model="datasource.userName" lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>

      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label">Email</label>
          <div class="layui-input-inline">
            <input type="text" name="email" v-model="datasource.email" lay-verify="required|email" autocomplete="off" class="layui-input">
          </div>
        </div>
        <div class="layui-inline">
          <label class="layui-form-label">Telphone</label>
          <div class="layui-input-inline">
            <input type="tel" name="telphone" v-model="datasource.telphone" lay-verify="required|phone" autocomplete="off" class="layui-input">
          </div>
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">Country</label>
        <div class="layui-input-block">
          <select name="country" id="country" lay-filter="country" lay-verify="required" lay-search="">
            <option value="">Select your country</option>
            <option value="China">China</option>
            <option value="America">America</option>
          </select>
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">Address</label>
        <div class="layui-input-block">
          <input type="text" name="address" v-model="datasource.address" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">Activated</label>
        <div class="layui-input-block">
          <input type="checkbox" name="isActivated" id="isActivated" lay-filter="isActivated" lay-skin="switch" lay-text="YES|NO" v-model="datasource.isActivated">
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">Role</label>
        <div class="layui-input-block">
          <input v-for="(role, index) in roles" type="checkbox" :name="role.roleId" :title="role.roleName" lay-filter="roleSelected" v-model="role.selected">
          <!-- <input type="checkbox" name="like[write]" title="写作">
          <input type="checkbox" name="like[read]" title="阅读" checked="checked">
          <input type="checkbox" name="like[game]" title="游戏"> -->
        </div>
      </div>
      <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">Remark</label>
        <div class="layui-input-block">
          <textarea placeholder="" name="remark" v-model="datasource.remark" class="layui-textarea"></textarea>
        </div>
      </div>
      <div class="layui-form-item">
        <div class="layui-input-block">
          <button class="layui-btn" lay-submit="" lay-filter="formDemo">SUBMIT</button>
          <button type="reset" class="layui-btn layui-btn-primary">RESET</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "userinfo",
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
      // var accessToken = localStorage.getItem("token");
      var vm = this;
      vm.datasource = data;

      // 初始化时，没有头像
      $("#imgdemo").attr(
        "src",
        "http://" + API_SERVER + "/strive/upload/default.png"
      );
      // 初始化时，未激活
      $("input[name=isActivated]").attr("checked", false);
      // 初始化时，所有角色未选中
      $.each(vm.roles, function(index, item) {
        $("input[name=" + item.roleId + "]").attr("checked", false);
      });

      if (!this.datasource.userId) {
        vm.datasource.optType = "add";
        vm.$http
          .get("http://" + API_SERVER + "/strive/user/generateNewUserID")
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              layer.msg(result.message);
            } else {
              vm.datasource.userId = result.data;

              $("#userId").val(result.data);
            }
          });
      } else {
        vm.datasource.optType = "update";

        if (vm.datasource.userRoles) {
          $.each(vm.datasource.userRoles.split(","), function(index, item) {
            // 修改时，重新赋值角色信息
            $("input[name=" + item + "]").attr("checked", true);
          });
        }

        // 对select重新赋值
        $("#country").val(vm.datasource.country);

        // 重新渲染页面
        layui.use(["form"], function() {
          var form = layui.form;
          // form.render("select");
          // form.render("checkbox");

          // 重新渲染表单控件
          form.render();
        });
      }

      // select、checkbox、radio需通过jquery重新赋值并渲染
      if (vm.datasource.image) {
        $("#imgdemo").attr(
          "src",
          "http://" + API_SERVER + "/strive" + vm.datasource.image
        );
      }
      $("#country").val(vm.datasource.country);
      $("#isActivated").attr("checked", vm.datasource.isActivated);

      layui.use(["form"], function() {
        var form = layui.form;
        // form.render("select");
        // form.render("checkbox");

        // 重新渲染表单控件
        form.render();
      });
    }
  },
  created: function() {},
  mounted: function() {
    var accessToken = localStorage.getItem("token");
    var vm = this;
    vm.$http
      .get("http://" + API_SERVER + "/strive/role/queryRole?roleId=", {
        headers: {
          Authorization: "Bearer " + accessToken
        }
      })
      .then(rep => {
        var result = rep.body;
        if (result.code !== 0) {
          vm.layer.msg(result.message);
        } else {
          vm.roles = result.data;
        }
      });

    layui.use(["upload", "form"], function() {
      // layui自带的jquery
      // var $ = layui.jquery;
      var upload = layui.upload;
      var form = layui.form;

      // var imgBytes;

      // 普通图片上传
      upload.render({
        elem: "#btn-upload",
        url: "http://" + API_SERVER + "/strive/upload/user",
        before: function(obj) {
          // 预读本地文件示例，不支持ie8
          obj.preview(function(index, file, result) {
            // imgBytes = result;
          });
        },
        done: function(res, index, upload) {
          // 如果上传失败
          if (res.code !== 0) {
            return layer.msg("上传失败");
          }

          vm.datasource.image = res.data;
          // 上传成功
          $("#imgdemo").attr(
            "src",
            "http://" + API_SERVER + "/strive" + res.data
          );
        },
        error: function(index, upload) {
          layer.msg("上传失败！");
        }
      });

      // select、checkbox、radio、date类型都通过监听方式赋值
      // 监听checkbox开关
      form.on("switch(isActivated)", function(obj) {
        // // 冒泡提示
        // layer.tips(
        //   this.value + " " + this.name + "：" + obj.elem.checked,
        //   obj.othis
        // );
        vm.datasource.isActivated = obj.elem.checked;
      });

      form.on("checkbox(roleSelected)", function(obj) {
        // // 冒泡提示
        // layer.tips(
        //   this.value + " " + this.name + "：" + obj.elem.checked,
        //   obj.othis
        // );

        var listRole = [];

        if (vm.datasource.userRoles) {
          listRole = vm.datasource.userRoles.split(",");
        }

        // 删除已存在但正在取消的元素
        $.each(listRole, function(index, item) {
          if (item === obj.elem.name && !obj.elem.checked) {
            listRole.splice(index, 1);
          }
        });

        // 添加不存在但正在选定的元素
        if ($.inArray(obj.elem.name, listRole) === -1 && obj.elem.checked) {
          listRole.push(obj.elem.name);
        }

        // 重新赋值
        vm.datasource.userRoles = "";
        $.each(listRole, function(index, item) {
          vm.datasource.userRoles +=
            vm.datasource.userRoles === "" ? item : "," + item;
        });

        console.log(vm.datasource.userRoles);
      });

      // 监听select列表
      form.on("select(country)", function(obj) {
        vm.datasource.country = obj.value;
      });

      // 监听提交
      form.on("submit(formDemo)", function(data) {
        if (vm.datasource.optType === "add") {
          vm.$http
            .post("http://" + API_SERVER + "/strive/user/create", vm.datasource)
            .then(rep => {
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
            .post(
              "http://" +
                API_SERVER +
                "/strive/user/modify/" +
                vm.datasource.userId,
              vm.datasource
            )
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
.layui-upload-img {
  height: 100px;
  width: 100%;
}

.layui-upload {
  right: 20px;
  /* width: 100px; */
  /* margin-right: 0px; */
  position: absolute;
  width: 100px;
}

#userId,
#userName {
  width: 514px;
}
.layui-upload-list {
  margin: 0px 0px 10px 0px;
}

#btn-upload {
  width: 100%;
}
</style>
