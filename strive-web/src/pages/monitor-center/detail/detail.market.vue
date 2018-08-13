<template>
  <div id="app" v-show="show">
    <div id="main2" style="height:100%;width:100%;"></div>
  </div>
</template>

<script>
export default {
  name: "app_1",
  data() {
    return {
      show: true,
      title: "",
      datasource: {}
    };
  },
  methods: {
    calculateMA(dayCount, data) {
      var result = [];
      for (var i = 0, len = data.length; i < len; i++) {
        if (i < dayCount) {
          result.push("-");
          continue;
        }
        var sum = 0;
        for (var j = 0; j < dayCount; j++) {
          sum += data[i - j][1];
        }
        result.push(sum / dayCount);
      }
      return result;
    },

    curve(types, datas) {
      var myChart = echarts.init(document.getElementById("main2"));

      var option = {
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        series: [
          {
            name: "Terminal Platform",
            type: "pie",
            radius: ["50%", "70%"],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: "center"
              },
              emphasis: {
                show: false,
                textStyle: {
                  fontSize: "30",
                  fontWeight: "bold"
                }
              }
            },
            labelLine: {
              normal: {
                show: false
              }
            },
            data: datas
          }
        ]
      };

      myChart.setOption(option);
    }
  },
  created: function() {},
  mounted: function() {
    var vm = this;
    this.$http
      .get("http://118.89.35.114:8762/GetTerminalDistribution")
      .then(rep => {
        var types = [];
        var datas = [];

        $.each(rep.data, function(index, element) {
          types.push(element.terminal);
          datas.push({ name: element.terminal, value: element.users });
        });
        vm.curve(types, datas);
        // console.log(rep.bodyText);
        // console.log(rep);
      });
  },
  updated: function() {},
  destroyed: function() {}
};

// layui.use(['element', 'layer', 'form', 'layedit', 'laydate'], function() {
//   var element = layui.element()
//   var form = layui.form()
//     , layer = layui.layer
//     , layedit = layui.layedit
//     , laydate = layui.laydate;

// });
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
a {
  font-size: 14px;
}

body {
  margin: 0px;
}

input {
  border: none;
  width: 100%;
  background-color: #000;
  color: #fff;
  padding: 0px;
  font-size: 18px;
  height: 34px;
  line-height: 34px;
}
</style>
