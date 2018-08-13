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
          <label class="layui-form-label">ID Or Name</label>
          <div class="layui-input-inline">
            <input type="text" class="layui-input" id="content" v-model="content" @keyup="enterKeyup($event)">
          </div>
        </div>
        <div class="layui-inline">
          <button class="layui-btn" @click="btnSearch();">
            <i class="layui-icon">&#xe615;</i> SEARCH</button>
          <button class="layui-btn layui-btn-normal" @click="btnAdd();">
            <i class="layui-icon">&#xe61f;</i> ADD</button>
          </button>
        </div>
      </div>
    </div>
    <table class="layui-hide" id="table" lay-filter="tableEvent"></table>
    <div id="pager"></div>

    <roleinfo ref="roleinfo" v-show="false">
    </roleinfo>
  </div>
</template>

<script>
import roleinfo from "./detail/detailInfo.vue";
export default {
  components: {
    roleinfo
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
      var info = this.$refs.roleinfo;
      layer.open({
        type: 1,
        // skin: "layui-layer-azure",
        resize: false,
        title: "Role Info", // false,不显示标题
        area: ["800px", "500px"],
        content: $("#roleinfo"), // 捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
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
      var accessToken = localStorage.getItem("token");

      layui.use(["layer", "table", "form", "laypage"], function() {
        vm.layer = layui.layer;
        vm.table = layui.table;
        vm.form = layui.form;
        var laypage = layui.laypage;

        vm.$http
          .get(
            "http://" +
              API_SERVER +
              "/strive/role/queryRole?roleId=" +
              vm.content,
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
              vm.count = result.data.length;
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
        // 展示已知数据
        vm.table.render({
          elem: "#table",
          cols: [
            [
              // 标题栏
              {
                fixed: "left",
                field: "roleId",
                title: "Role ID",
                sort: true,
                width: 150
              },
              {
                field: "roleName",
                title: "Role Name",
                minWidth: 150
              },

              {
                field: "permissionCode",
                title: "Permission Code",
                minWidth: 300
                // event: "edit"
                // style: "cursor: pointer;"
              },
              {
                field: "isEnabled",
                title: "IsEnabled",
                sort: true,
                templet: "#switchTpl",
                unresize: true,
                width: 100
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
          if (obj.event === "detail") {
          } else if (obj.event === "edit") {
            var info = vm.$refs.roleinfo;
            layer.open({
              type: 1,
              // skin: "layui-layer-azure",
              resize: false,
              title: "Role Info", // false,不显示标题
              area: ["800px", "500px"],
              content: $("#roleinfo"), // 捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
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
              "Are you sure to delete [" + obj.data.roleId + "]？",
              function(index) {
                vm.$http
                  .post(
                    "http://" + API_SERVER + "/strive/role/delete",
                    obj.data.roleId
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
          .get(
            "http://" +
              API_SERVER +
              "/strive/role/queryRole?roleId=" +
              vm.content,
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