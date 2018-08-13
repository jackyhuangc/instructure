<style scoped>
.layui-btn {
  width: 120px;
}
.layui-form-label {
  width: 120px;
}
</style>

<template>
  <div id="app" style="height:100%;width:100%;">
    <fieldset class="layui-elem-field layui-field-title" style="margin-top: 30px;">
      <legend>会员列表</legend>
    </fieldset>
    <div class="layui-form">
      <div class="layui-form-item">
        <div class="layui-inline">
          <label class="layui-form-label">编号|姓名|手机号</label>
          <div class="layui-input-inline">
            <input type="text" class="layui-input" id="content" v-model="content" @keyup="enterKeyup($event)">
          </div>
        </div>
        <div class="layui-inline">
          <button class="layui-btn" @click="btnSearch();">
            <i class="layui-icon">&#xe615;</i> 查询</button>
          <button class="layui-btn layui-btn-normal" @click="btnAdd();">
            <i class="layui-icon">&#xe61f;</i> 添加</button>
          </button>
        </div>
      </div>
    </div>
    <table class="layui-hide" id="table" lay-filter="tableEvent"></table>
    <div id="pager"></div>

    <detailInfo ref="detailInfo" v-show="false">
    </detailInfo>
  </div>
</template>

<script>
import detailInfo from "./detail/detailInfo.vue";
export default {
  components: {
    detailInfo
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
      var info = this.$refs.detailInfo;
      layer.open({
        type: 1,
        // skin: "layui-layer-azure",
        resize: false,
        title: "会员信息", // false,不显示标题
        area: ["800px", "500px"],
        content: $("#detailInfo"), // 捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
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

        vm.$http
          .post("http://" + API_SERVER + "/strive/member/query", {
            page: 1,
            size: 20,
            memberNo: $("#content").val()
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
                field: "memberNo",
                title: "会员编号",
                sort: true,
                width: 150
              },
              {
                field: "memberType",
                title: "会员类型",
                width: 100,
                templet: "#typeTpl",
                unresize: true
              },
              {
                field: "memberName",
                title: "会员名称",
                minWidth: 150
              },
              {
                field: "memberMobile",
                title: "手机号",
                minWidth: 150
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "memberEmail",
                title: "Email",
                minWidth: 150
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "memberQuotas",
                title: "账户余额",
                minWidth: 150
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "memberPoints",
                title: "剩余积分",
                minWidth: 150
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "memberStatus",
                title: "会员状态",
                sort: true,
                templet: "#switchTpl",
                unresize: true,
                width: 100
              },
              {
                field: "createdAt",
                title: "注册时间",
                sort: true,
                width: 200
              },
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

          if (obj.event === "edit") {
            var info = vm.$refs.detailInfo;
            layer.open({
              type: 1,
              resize: false,
              title: "会员信息", // false,不显示标题
              area: ["800px", "500px"],
              content: $("#detailInfo"), // 捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
              success: function(layero, index) {
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
              "Are you sure to delete [" + obj.data.memberNo + "]？",
              function(index) {
                vm.$http
                  .post(
                    "http://" +
                      API_SERVER +
                      "/strive/member/delete/" +
                      obj.data.memberNo
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
          .post("http://" + API_SERVER + "/strive/member/query", {
            page: vm.curr,
            size: vm.limit,
            memberNo: $("#content").val()
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