<style scoped>
.layui-btn {
  width: 120px;
}
</style>

<template>
  <div id="app" style="height:100%;width:100%;">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
      <legend>INPUT CONDITION</legend>
    </fieldset>
    <div class="layui-form">
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label">ID or Name</label>
          <div class="layui-input-inline">
            <input type="text" class="layui-input" id="content" v-model="content" @keyup="enterKeyup($event)">
          </div>
        </div>
        <div class="layui-inline">
          <label class="layui-form-label">Start Time</label>
          <div class="layui-input-inline">
            <input type="text" class="layui-input" id="bgtm" v-model="bgtm" placeholder="">
          </div>
        </div>
        <div class="layui-inline">
          <label class="layui-form-label">End Time</label>
          <div class="layui-input-inline">
            <input type="text" class="layui-input" id="edtm" v-model="edtm" placeholder="">
          </div>
        </div>
        <div class="layui-inline">
          <button class="layui-btn" @click="btnSearch();">
            <i class="layui-icon">&#xe615;</i> SEARCH</button>
          <button class="layui-btn layui-btn-normal" @click="btnAdd();">
            <i class="layui-icon">&#xe61f;</i> ADD</button>
        </div>
      </div>
    </div>
    <table class="layui-hide" id="table" lay-filter="tableEvent"></table>
    <div id="pager"></div>

    <userinfo ref="userinfo" v-show="false">
    </userinfo>
  </div>
</template>

<script>
import userinfo from "./detail/detailInfo.vue";
export default {
  components: {
    userinfo
  },
  data() {
    return {
      bgtm: "",
      edtm: "",
      content: "",
      curr: 1,
      limit: 10,
      count: -1,
      states: {
        selected: "000000",
        data: []
      },
      countries: {
        selected: "000000",
        data: []
      },
      listGrid: [],
      screenHeight: 768,
      table: null,
      layer: null
    };
  },
  watch: {},
  methods: {
    // 查询按钮事件
    btnSearch() {
      this.initPage();
    },
    // 添加按钮事件
    btnAdd() {
      var vm = this;
      var info = this.$refs.userinfo;
      layer.open({
        type: 1,
        // skin: "layui-layer-azure",
        resize: false,
        title: "User Info", // false,不显示标题
        area: ["800px", "500px"],
        content: $("#userinfo"), // 捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
        success: function(layero, index) {
          info.bindDataSource({});
        },
        cancel: function() {},
        end: function() {
          vm.initPage();
        }
      });
    },
    // Enter键事件
    enterKeyup: function(ev) {
      if (ev.keyCode === 13) {
        this.btnSearch();
      }
    },
    // 初始化控件
    initPage() {
      var vm = this;
      vm.count = -1;
      vm.curr = 1;
      // var accessToken = localStorage.getItem("token");

      layui.use(["layer", "table", "form", "laypage", "laydate"], function() {
        vm.layer = layui.layer;
        vm.table = layui.table;
        vm.form = layui.form;
        var laypage = layui.laypage;
        var laydate = layui.laydate;

        // 墨绿主题
        laydate.render({
          elem: "#bgtm",
          theme: "molv",
          type: "datetime",
          done: function(value, date, endDate) {
            vm.bgtm = value;
          }
        });

        laydate.render({
          elem: "#edtm",
          theme: "molv",
          type: "datetime",
          done: function(value, date, endDate) {
            vm.edtm = value;
          }
        });

        vm.$http
          .post("http://" + API_SERVER + "/strive/user/query", {
            page: 1,
            size: 20,
            userId: $("#content").val()
          })
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              vm.layer.msg(result.message);
            } else {
              vm.count = result.data.total;
              // 完整功能
              laypage.render({
                elem: "pager",
                count: vm.count,
                limit: vm.limit,
                layout: ["count", "prev", "page", "next", "limit"],
                jump: function(obj) {
                  vm.curr = obj.curr;
                  vm.limit = obj.limit;
                  if (result) {
                    console.log(result.data);
                    // 展示已知数据
                    vm.bindDataSource(result.data.list);
                    result = null;
                  } else {
                    vm.bindDataSource(null);
                  }
                }
              });
            }
          });
      });
    },
    // 绑定数据源
    bindDataSource(data) {
      var vm = this;
      // var accessToken = localStorage.getItem("token");
      if (data) {
        // 展示已知数据
        vm.table.render({
          elem: "#table",
          cols: [
            [
              // 标题栏
              {
                fixed: "left",
                field: "userId",
                title: "User ID",
                sort: true,
                width: 150
              },
              {
                field: "userName",
                title: "User Name",
                minWidth: 150
              },
              {
                field: "telphone",
                title: "Telphone",
                minWidth: 150
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "email",
                title: "Email",
                minWidth: 150
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "country",
                title: "Country",
                minWidth: 150
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "address",
                title: "Address",
                minWidth: 300
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "isActivated",
                title: "IsActivated",
                sort: true,
                templet: "#switchTpl",
                unresize: true,
                width: 100
              },
              {
                field: "addTime",
                title: "Registration Date",
                sort: true,
                width: 200
              },
              { field: "remark", title: "Remark", width: 300 },
              {
                fixed: "right",
                title: "操作",
                toolbar: "#barDemo",
                width: 178,
                align: "center"
              }
            ]
          ], //  toolbar: '#barDemo'
          limit: vm.limit, // 每页显示的条数（默认：10）。值务必对应 limits 参数的选项。优先级低于 page 参数中的 limit 参数
          data: data,
          skin: "line", // 表格风格
          even: true
        });

        // 监听单元格事件
        vm.table.on("tool(tableEvent)", function(obj) {
          // var data = obj.data;
          // layer.msg(JSON.stringify(data));

          if (obj.event === "detail") {
            // layer.alert(JSON.stringify(data));

            layer.prompt({ title: "Push Notification", formType: 2 }, function(
              text,
              index
            ) {
              var timestamp = Date.parse(new Date());
              // 混合签名 HMAC 进程将密钥与消息数据混合，使用哈希函数对混合结果进行哈希计算
              var signed = hex_hmac_md5("bdt@2017", "1-BDT-" + timestamp);
              console.log(signed);

              var header =
                "WJ " +
                "applicationId=1&partnerId=BDT&timestamp=" +
                timestamp +
                "&signature=" +
                signed;

              var notice = {
                url: "http://118.89.35.114:6677/api/account/PushNotification",
                header: header,
                data: {
                  InvestorID: "1000",
                  Alert: text,
                  Title: "notice test"
                }
              };

              vm.$http
                .post("http://" + API_SERVER + "/strive/proxy/post", notice)
                .then(rep => {
                  var result = rep.body;
                  if (result.Code === 0) {
                    layer.msg("push successful!", { time: 2000 }, function() {
                      layer.closeAll();
                    });
                  } else {
                    console.log(rep);
                    layer.msg(result.Error);
                  }
                })
                .catch(function(response) {
                  console.log(response);
                });

              // layer.close(index);
              // layer.msg("推送成功！<br>您最后写下了：" + text);
            });
          } else if (obj.event === "edit") {
            var info = vm.$refs.userinfo;
            layer.open({
              type: 1,
              // skin: "layui-layer-azure",
              resize: false,
              title: "User Info", // false,不显示标题
              area: ["800px", "500px"],
              content: $("#userinfo"), // 捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
              success: function(layero, index) {
                // layer.msg(JSON.stringify(obj.data));
                info.bindDataSource(obj.data);
              },
              cancel: function() {},
              end: function() {
                if (info.datasource.optType === "add") {
                  vm.initPage();
                } else {
                  // 最保险的做法，还是要刷新数据源，否则未成功更新数据库会有造成很多逻辑判断
                  // vm.initPage();

                  // 此处仅为简单方法，不刷新数据源的操作
                  obj.update(info.datasource);
                  vm.form.render();
                }
              }
            });
          } else if (obj.event === "del") {
            layer.confirm(
              "Are you sure to delete [" + obj.data.userId + "]？",
              function(index) {
                vm.$http
                  .post(
                    "http://" +
                      API_SERVER +
                      "/strive/user/delete/" +
                      obj.data.userId
                  )
                  .then(rep => {
                    var result = rep.body;
                    if (result.code === 0) {
                      layer.msg(
                        "delete successful!",
                        { time: 2000 },
                        function() {
                          vm.initPage();
                          // // 删除本地即可，不刷新数据源
                          // obj.del();
                          // layer.close(index);
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
              }
            );
          }
        });
      } else {
        vm.$http
          .post("http://" + API_SERVER + "/strive/user/query", {
            page: vm.curr,
            size: vm.limit,
            userId: $("#content").val()
          })
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              vm.layer.msg(result.message);
            } else {
              // 展示已知数据
              vm.bindDataSource(result.data.list);
            }
          });
      }
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