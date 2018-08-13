<template>
  <div id="app" v-show="show">
    <div id="main3" style="height:100%;width:100%;"></div>
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
      var myChart = echarts.init(document.getElementById("main3"));

      var option = {
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "shadow"
          }
        },
        // legend: {
        //     data: ['2011年', '2012年']
        // },
        grid: {
          x: 0,
          x2: 0,
          y: 10,
          y2: 20
        },
        xAxis: {
          show: false,
          type: "value",
          boundaryGap: [0, 1],
          splitLine: { show: false }
        },
        yAxis: {
          show: false,
          type: "category",
          data: types,

          axisLabel: {
            margin: -45,
            textStyle: {
              color: "red",
              fontSize: 14
            }
          },
          splitLine: { show: false }
        },
        series: [
          {
            name: "Users",
            type: "bar",
            data: datas,
            itemStyle: {
              normal: {
                color: function(params) {
                  var colorList = [
                    "#C1232B",
                    "#B5C334",
                    "#FCCE10",
                    "#E87C25",
                    "#27727B",
                    "#FE8463",
                    "#9BCA63",
                    "#FAD860",
                    "#F3A43B",
                    "#60C0DD",
                    "#D7504B",
                    "#C6E579",
                    "#F4E001",
                    "#F0805A",
                    "#26C0C0",
                    "#C1232B",
                    "#B5C334",
                    "#FCCE10",
                    "#E87C25",
                    "#27727B",
                    "#FE8463",
                    "#9BCA63",
                    "#FAD860",
                    "#F3A43B",
                    "#60C0DD",
                    "#D7504B",
                    "#C6E579",
                    "#F4E001",
                    "#F0805A",
                    "#26C0C0"
                  ];
                  return colorList[params.dataIndex];
                }
              },
              emphasis: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: "rgba(0, 0, 0, 0.5)"
              }
            }
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
      .get("http://118.89.35.114:8762/GetRegionDistribution")
      .then(rep => {
        var types = [];
        var datas = [];

        $.each(rep.data, function(index, element) {
          types.push(element.region);
          datas.push({ name: element.region, value: element.users });
        });
        vm.curve(types, datas);
        // console.log(rep.bodyText);
        // console.log(rep);
      });
  },
  updated: function() {},
  destroyed: function() {}
};
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
