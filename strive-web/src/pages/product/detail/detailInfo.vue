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
            <input type="hidden" name="productImage" id="productImage" v-model="datasource.productImage">
          </div>
          <button type="button" class="layui-btn" id="btn-upload">Image</button>
        </div>
        <label class="layui-form-label">产品编号</label>
        <div class="layui-input-block">
          <input type="text" name="productNo" id="productNo" readonly="readonly" v-model="datasource.productNo" autocomplete="off" placeholder="" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">产品名称</label>
        <div class="layui-input-block">
          <input type="text" name="productName" id="productName" v-model="datasource.productName" lay-verify="required" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">产品类型</label>
        <div class="layui-input-block">
          <select name="productAttr" id="productAttr" lay-filter="productAttr" lay-verify="required" lay-search="">
            <option value="">-请选择-</option>
            <option value="XXXXXXX-ABC">XXXXXXX-ABC</option>
            <option value="YYYYYYY-ABC">YYYYYYY-ABC</option>
          </select>
        </div>
      </div>
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label">产品单价</label>
          <div class="layui-input-inline">
            <input type="text" name="productPrice" v-model="datasource.productPrice" lay-verify="required|number" autocomplete="off" class="layui-input">
          </div>
        </div>
        <div class="layui-inline">
          <label class="layui-form-label">折扣率</label>
          <div class="layui-input-inline">
            <input type="text" name="productDiscountRate" v-model="datasource.productDiscountRate" lay-verify="required|number" autocomplete="off" class="layui-input">
          </div>
        </div>
      </div>
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label">兑换积分</label>
          <div class="layui-input-inline">
            <input type="text" name="productPoints" v-model="datasource.productPoints" lay-verify="required|number" autocomplete="off" class="layui-input">
          </div>
        </div>
        <div class="layui-inline">
          <label class="layui-form-label">邮费</label>
          <div class="layui-input-inline">
            <input type="text" name="productDeliverFee" v-model="datasource.productDeliverFee" lay-verify="required|number" autocomplete="off" class="layui-input">
          </div>
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">促销类型</label>
        <div class="layui-input-block">
          <input type="radio" name="productPromotion" lay-filter="promotionFilter" value="0" title="不促销" checked="">
          <input type="radio" name="productPromotion" lay-filter="promotionFilter" value="1" title="折扣促销">
          <input type="radio" name="productPromotion" lay-filter="promotionFilter" value="2" title="限时促销">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">促销开始</label>
        <div class="layui-input-block">
          <input type="text" name="productPromotionBegin" id="productPromotionBegin" v-model="datasource.productPromotionBegin" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>
      <div class="layui-form-item">
        <label class="layui-form-label">促销结束</label>
        <div class="layui-input-block">
          <input type="text" name="productPromotionEnd" id="productPromotionEnd" v-model="datasource.productPromotionEnd" placeholder="" autocomplete="off" class="layui-input">
        </div>
      </div>

      <div class="layui-form-item">
        <label class="layui-form-label">状态</label>
        <div class="layui-input-block">
          <input type="checkbox" name="productStatus" id="productStatus" lay-filter="statusFilter" lay-skin="switch" lay-text="激活|停用" v-model="datasource.productStatus">
        </div>
      </div>

      <div class="layui-form-item layui-form-text">
        <label class="layui-form-label">商品描述</label>
        <div class="layui-input-block">
          <textarea placeholder="" name="productIntroduce" v-model="datasource.productIntroduce" class="layui-textarea"></textarea>
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

      if (!this.datasource.productNo) {
        vm.datasource.optType = "add";
        vm.$http
          .get("http://" + API_SERVER + "/strive/product/generateNewProductNo")
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              layer.msg(result.message);
            } else {
              vm.datasource.productNo = result.data;

              $("#productNo").val(result.data);
            }
          });

        // 初始化时，普通产品
        vm.datasource.productPromotion = 0;
        $(
          "input[name='productPromotion'][type=radio][value='" +
            vm.datasource.productPromotion +
            "']"
        ).attr("checked", true);

        // 初始化时，未激活
        vm.datasource.productStatus = true;
        $("input[name='productStatus']").attr(
          "checked",
          vm.datasource.productStatus
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
      if (vm.datasource.productImage) {
        $("#imgdemo").attr(
          "src",
          "http://" + API_SERVER + "/strive" + vm.datasource.productImage
        );
      }
      // $("#country").val(vm.datasource.country);
      $(
        "input[name='productPromotion'][type=radio][value='" +
          vm.datasource.productPromotion +
          "']"
      ).attr("checked", true);
      $("input[name='productStatus']").attr(
        "checked",
        vm.datasource.productStatus
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

    layui.use(["upload", "form", "laydate"], function() {
      // layui自带的jquery
      // var $ = layui.jquery;
      var upload = layui.upload;
      var form = layui.form;
      var laydate = layui.laydate;

      laydate.render({
        elem: "#productPromotionBegin",
        theme: "molv",
        type: "datetime",
        done: function(value, date, endDate) {
          vm.productPromotionBegin = value;
        }
      });

      laydate.render({
        elem: "#productPromotionEnd",
        theme: "molv",
        type: "datetime",
        done: function(value, date, endDate) {
          vm.productPromotionEnd = value;
        }
      });

      // 普通图片上传
      upload.render({
        elem: "#btn-upload",
        url: "http://" + API_SERVER + "/strive/upload/product",
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

          vm.datasource.productImage = res.data;
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

      form.on("radio(promotionFilter)", function(data) {
        vm.datasource.productPromotion = data.value;
      });

      form.on("switch(statusFilter)", function(obj) {
        vm.datasource.productStatus = obj.elem.checked;
      });

      // 监听select列表
      form.on("select(productAttr)", function(obj) {
        vm.datasource.productAttr = obj.value;
      });

      // 监听提交
      form.on("submit(formDemo)", function(data) {
        debugger;
        if (vm.datasource.optType === "add") {
          vm.$http
            .post(
              "http://" + API_SERVER + "/strive/product/create",
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
                "/strive/product/modify/" +
                vm.datasource.productNo,
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

#productNo,
#productName {
  width: 514px;
}
.layui-upload-list {
  margin: 0px 0px 10px 0px;
}

#btn-upload {
  width: 100%;
}
</style>
