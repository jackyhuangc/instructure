<style scoped>
/* .layui-btn {
  width: 120px;
} */

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

.layui-upload-list {
  margin: 0px 0px 10px 0px;
}

#btn-upload {
  width: 100%;
}

#userId,
#userName {
  width: 514px;
}
</style>

<template>
  <div id="app" style="margin:15px;">
    <blockquote class="layui-elem-quote layui-text">
      Take care of your infomation！
      <a href="Mailto:huangchao911@aliyun.com?Subject=Hello&Body=Hello" target="_blank">Any problem, contact jacky please？</a>
    </blockquote>

    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
      <legend>Personal Infomation</legend>
    </fieldset>
    <form class="layui-form" action="">
      <div class="layui-upload">
        <div class="layui-upload-list">
          <img class="layui-upload-img" name="imgdemo" id="imgdemo">
          <input type="hidden" name="image" id="image" v-model="datasource.image">
        </div>
        <button type="button" class="layui-btn" id="btn-upload">Image</button>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">User ID</label>
        <div class="layui-input-block">
          <input type="text" name="userId" id="userId" v-model="datasource.userId" readonly="readonly" autocomplete="off" placeholder="" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item" style="width:514px;">
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
            <input type="tel" name="telphone" readonly="readonly" v-model="datasource.telphone" lay-verify="required" autocomplete="off" class="layui-input">
          </div>
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">Country</label>
        <div class="layui-input-block">
          <select name="country" id="country" v-model="datasource.country" lay-filter="country" lay-verify="required" lay-search="">
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
          <input type="checkbox" name="isActivated" id="isActivated" lay-filter="isActivated" lay-skin="switch" lay-text="YES|NO" v-model="datasource.isActivated" disabled="">
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">Role</label>
        <div class="layui-input-block">
          <input v-for="(role, index) in roles" type="checkbox" :name="role.roleId" :title="role.roleName" lay-filter="roleSelected" v-model="role.selected" disabled="">
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
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      roles: [],
      accessToken: "",
      datasource: {
        optType: "update"
      }
    };
  },
  watch: {},
  methods: {
    // 1、初始化控件
    initPage() {
      var vm = this;

      var accessToken = localStorage.getItem("token");
      vm.accessToken = accessToken;
      // 初始化角色信息
      vm.$http
        .get("http://" + API_SERVER + "/strive/role/queryRole?roleID=", {
          headers: {
            Authorization: "Bearer " + vm.accessToken
          }
        })
        .then(rep => {
          var result = rep.body;
          if (result.code !== 0) {
            vm.layer.msg(result.message);
          } else {
            vm.roles = result.data;

            // 2、初始化完成后，开始绑定数据
            vm.bindDataSource(null);
          }
        });

      // form表单及事件初始化
      layui.use(["upload", "form"], function() {
        // layui自带的jquery
        // var $ = layui.jquery;
        var upload = layui.upload;
        var form = layui.form;

        // 普通图片上传
        upload.render({
          elem: "#btn-upload",
          url: "http://" + API_SERVER + "/strive/upload",
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
          vm.datasource.isActivated = obj.elem.checked;
        });

        form.on("checkbox(roleSelected)", function(obj) {
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
            // nothing todo
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
    // 绑定数据源
    bindDataSource(data) {
      var vm = this;

      var userInfo = JSON.parse(localStorage.getItem("user_info"));
      // 查询当前用户信息
      vm.$http
        .get("http://" + API_SERVER + "/strive/user/" + userInfo.userId, {
          headers: {
            Authorization: "Bearer " + vm.accessToken
          }
        })
        .then(rep => {
          var result = rep.body;
          if (result.code !== 0) {
            vm.layer.msg(result.message);
          } else {
            vm.datasource = result.data;
            if (vm.datasource.image) {
              $("#imgdemo").attr(
                "src",
                "http://" + API_SERVER + "/strive" + vm.datasource.image
              );
            } else {
              $("#imgdemo").attr(
                "src",
                "http://" + API_SERVER + "/strive/upload/default.png"
              );
            }

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
        });
    }
  },
  created: function() {},
  mounted() {
    this.initPage();
  },
  beforeUpdate() {},
  updated: function() {},
  destroyed: function() {}
};
</script>