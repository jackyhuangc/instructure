<style scoped>
.tools {
  height: 33px;
  margin-top: 10px;
  margin-bottom: 10px;
}

.tools .toolbar {
  margin-bottom: 0px;
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
          <label class="layui-form-label">Log Content</label>
          <div class="layui-input-inline">
            <input type="text" class="layui-input" id="content" v-model="content" @keyup="enterKeyup($event)">
          </div>
        </div>
        <div class="layui-inline">
          <button class="layui-btn" @click="btnClick();">
            <i class="layui-icon">&#xe615;</i> SEARCH</button>
        </div>
      </div>
    </div>
    <table class="layui-hide" id="table" lay-filter="tableEvent"></table>
    <div id="pager"></div>
  </div>
</template>

<script>
// import marketinfo from "./detail/detail.market.vue";
// import users from "./detail/detail.users.vue";
// import balanceinfo from "./detail/detail.balance.vue";
// import tradinginfo from "./detail/detail.trading.vue";
export default {
  components: {
    // users,
    // marketinfo,
    // balanceinfo,
    // tradinginfo
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
    btnClick() {
      this.initPage();
    },
    // Enter键事件
    enterKeyup: function(ev) {
      if (ev.keyCode === 13) {
        this.btnClick();
      }
    },
    // 初始化控件
    initPage() {
      var vm = this;
      vm.count = -1;
      vm.curr = 1;
      var accessToken = localStorage.getItem("token");

      layui.use(["layer", "table", "laypage", "laydate"], function() {
        vm.layer = layui.layer;
        vm.table = layui.table;
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
          .get(
            "http://" +
              API_SERVER +
              "/strive/querySystemLog?bgtm=" +
              vm.bgtm +
              "&edtm=" +
              vm.edtm +
              "&content=" +
              vm.content +
              "&count=" +
              vm.count +
              "&page=" +
              vm.curr +
              "&size=" +
              vm.limit,
            {
              headers: {
                Authorization: "Bearer " + accessToken
              }
            }
          )
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              vm.layer.msg(result.message);
            } else {
              vm.count = result.count;
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
                    // 展示已知数据
                    vm.bindDataSource(result.data);
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
      var accessToken = localStorage.getItem("token");
      if (data) {
        // 增加自定义编号
        var cnt = 1;
        data.forEach(function(item) {
          console.log(item);
          item.ID = (vm.curr - 1) * vm.limit + cnt;
          cnt++;
        });
        // 展示已知数据
        vm.table.render({
          elem: "#table",
          cols: [
            [
              // ID
              {
                field: "ID",
                title: "ID",
                sort: true,
                width: 100,
                event: "setSign"
              },
              // 标题栏
              {
                field: "userId",
                title: "User ID",
                sort: true,
                width: 150,
                event: "setSign"
              },
              {
                field: "optModule",
                title: "Module",
                minWidth: 150,
                event: "setSign"
              },
              {
                field: "optType",
                title: "Operation",
                minWidth: 100,
                event: "setSign"
              },
              {
                field: "optContent",
                title: "Log Content",
                minWidth: 300,
                event: "setSign"
              },
              {
                field: "optTime",
                title: "Opt Time",
                sort: true,
                width: 250,
                event: "setSign"
              },
              {
                field: "remark",
                title: "Remark",
                width: 300,
                event: "setSign"
              }
            ]
          ],
          limit: vm.limit, // 每页显示的条数（默认：10）。值务必对应 limits 参数的选项。优先级低于 page 参数中的 limit 参数
          data: data,
          skin: "line", // 表格风格
          even: true
        });

        // 监听单元格事件
        vm.table.on("tool(tableEvent)", function(obj) {
          // var data = obj.data;
          // layer.msg(JSON.stringify(data));
          // if (obj.event === "setSign") {
          //   layer.prompt(
          //     {
          //       formType: 2,
          //       title: "修改 ID 为 [" + data.ID + "] 的用户签名",
          //       value: data.ID
          //     },
          //     function(value, index) {
          //       layer.close(index);
          //       // 这里一般是发送修改的Ajax请求
          //       // 同步更新表格和缓存对应的值
          //       obj.update({
          //         ID: value
          //       });
          //     }
          //   );
          // }
        });
      } else {
        vm.$http
          .get(
            "http://" +
              API_SERVER +
              "/strive/querySystemLog?bgtm=" +
              vm.bgtm +
              "&edtm=" +
              vm.edtm +
              "&content=" +
              vm.content +
              "&count=" +
              vm.count +
              "&page=" +
              vm.curr +
              "&size=" +
              vm.limit,
            {
              headers: {
                Authorization: "Bearer " + accessToken
              }
            }
          )
          .then(rep => {
            var result = rep.body;
            if (result.code !== 0) {
              vm.layer.msg(result.message);
            } else {
              // 展示已知数据
              vm.bindDataSource(result.data);
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