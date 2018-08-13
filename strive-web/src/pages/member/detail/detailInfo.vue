<template>
  <div id="detailInfo">

    <blockquote class="layui-elem-quote layui-text">
      {{datasource.optType}} operation
    </blockquote>

    <form class="layui-form" action="">

      <div class="layui-form-item">

        <div class="layui-upload">
          <div class="layui-upload-list">
            <img class="layui-upload-img" name="imgdemo" id="imgdemo">
            <input type="hidden" name="memberImage" id="memberImage" v-model="datasource.memberImage">
          </div>
          <button type="button" class="layui-btn" id="btn-upload">Image</button>
        </div>
        <label class="layui-form-label">会员编号</label>
        <div class="layui-input-block">
          <input type="text" name="memberNo" id="memberNo" readonly="readonly" v-model="datasource.memberNo" autocomplete="off" placeholder="" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">会员名称</label>
        <div class="layui-input-block">
          <input type="text" name="memberName" id="memberName" v-model="datasource.memberName" lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>

      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label">手机号</label>
          <div class="layui-input-inline">
            <input type="tel" name="memberMobile" v-model="datasource.memberMobile" lay-verify="required|phone" autocomplete="off" class="layui-input">
          </div>
        </div>
        <div class="layui-inline">
          <label class="layui-form-label">邮箱</label>
          <div class="layui-input-inline">
            <input type="text" name="memberEmail" v-model="datasource.memberEmail" lay-verify="required|memberEmail" autocomplete="off" class="layui-input">
          </div>
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">地区</label>
        <div class="layui-input-block">
          <select name="country" id="country" lay-filter="country" lay-verify="required" lay-search="">
            <option value="">-请选择-</option>
            <option value="China">四川省-成都市-新都区</option>
            <option value="America">四川省-成都市-高新区</option>
          </select>
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">街道</label>
        <div class="layui-input-block">
          <input type="text" name="address" v-model="datasource.address" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">类型</label>
        <div class="layui-input-block">
          <input type="radio" name="memberType" lay-filter="typeFilter" value="0" title="普通" checked="">
          <input type="radio" name="memberType" lay-filter="typeFilter" value="1" title="VIP">
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">状态</label>
        <div class="layui-input-block">
          <input type="checkbox" name="memberStatus" id="memberStatus" lay-filter="statusFilter" lay-skin="switch" lay-text="激活|停用" v-model="datasource.memberStatus">
        </div>
      </div>

      <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">备注</label>
        <div class="layui-input-block">
          <textarea placeholder="" name="memberRemark" v-model="datasource.memberRemark" class="layui-textarea"></textarea>
        </div>
      </div>
      <div class="layui-form-item">
        <div class="layui-input-block">
          <button class="layui-btn" lay-submit="" lay-filter="formDemo">提交</button>
          <button type="reset" class="layui-btn layui-btn-primary">重置</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "detailInfo",
  data() {
    return {
      show: true,
      title: "",
      datasource: {
        optType: "add"
      }
    };
  },
  methods: {
    bindDataSource: function(data) {
      var vm = this;
      vm.datasource = data;

      // 初始化时，没有头像
      $("#imgdemo").attr(
        "src",
        "http://" + API_SERVER + "/strive/upload/default.png"
      );

      if (!this.datasource.memberNo) {
        vm.datasource.optType = "add";
        vm.$http
          .get("http://" + API_SERVER + "/strive/member/generateNewMemberNo")
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              layer.msg(result.message);
            } else {
              vm.datasource.memberNo = result.data;

              $("#memberNo").val(result.data);
            }
          });

        // 初始化时，普通会员
        vm.datasource.memberType = 0;
        $(
          "input[name='memberType'][type=radio][value='" +
            vm.datasource.memberType +
            "']"
        ).attr("checked", true);

        // 初始化时，未激活
        vm.datasource.memberStatus = true;
        $("input[name='memberStatus']").attr(
          "checked",
          vm.datasource.memberStatus
        );
      } else {
        vm.datasource.optType = "update";

        // 对select重新赋值
        // $("#country").val(vm.datasource.country);

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
      if (vm.datasource.memberImage) {
        $("#imgdemo").attr(
          "src",
          "http://" + API_SERVER + "/strive" + vm.datasource.memberImage
        );
      }
      // $("#country").val(vm.datasource.country);
      $(
        "input[name='memberType'][type=radio][value='" +
          vm.datasource.memberType +
          "']"
      ).attr("checked", true);
      $("input[name='memberStatus']").attr(
        "checked",
        vm.datasource.memberStatus
      );

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
    var vm = this;

    layui.use(["upload", "form"], function() {
      // layui自带的jquery
      // var $ = layui.jquery;
      var upload = layui.upload;
      var form = layui.form;

      // 普通图片上传
      upload.render({
        elem: "#btn-upload",
        url: "http://" + API_SERVER + "/strive/upload/member",
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

          vm.datasource.memberImage = res.data;
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

      form.on("radio(typeFilter)", function(data) {
        vm.datasource.memberType = data.value;
      });

      form.on("switch(statusFilter)", function(obj) {
        vm.datasource.memberStatus = obj.elem.checked;
      });

      // 监听select列表
      form.on("select(country)", function(obj) {
        vm.datasource.country = obj.value;
      });

      // 监听提交
      form.on("submit(formDemo)", function(data) {
        debugger;
        if (vm.datasource.optType === "add") {
          vm.$http
            .post(
              "http://" + API_SERVER + "/strive/member/create",
              vm.datasource
            )
            .then(rep => {
              var result = rep.body;
              if (result.code === 0) {
                layer.msg("添加成功!", { time: 2000 }, function() {
                  layer.closeAll();
                });
              } else {
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
                "/strive/member/modify/" +
                vm.datasource.memberNo,
              vm.datasource
            )
            .then(rep => {
              var result = rep.body;
              if (result.code === 0) {
                layer.msg("更新成功!", { time: 2000 }, function() {
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

#memberNo,
#memberName {
  width: 514px;
}
.layui-upload-list {
  margin: 0px 0px 10px 0px;
}

#btn-upload {
  width: 100%;
}
</style>
