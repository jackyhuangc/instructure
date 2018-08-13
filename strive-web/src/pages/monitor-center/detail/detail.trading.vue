<template>
  <div id="app1" v-show="show" style="font-size: 16px;color:#FF6600; text-align:left; overflow:hidden;">

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
  methods: {},
  created: function() {},
  mounted: function() {
    var vm = this;
    var begin = change_time(-24 * 60);
    setInterval(function() {
      vm.$http
        .get(
          "http://118.89.35.114:8762/GetOrderBillByTime?begin=" +
            begin +
            "&end=2099-12-31%2000:00:00"
        )
        .then(rep => {
          $.each(rep.data, function(index, element) {
            $("#app1").append(
              "<div style='font-size: 16px;'>" +
                element.orderTime +
                "</br>" +
                element.investorID +
                "&nbsp;placed an order!&nbsp;" +
                "Price ¥" +
                formatCurrency(element.orderPrice) +
                "</div>"
            );
            begin = add_seconds(new Date(Date.parse(element.orderTime)), 1);
          });
          var div = document.getElementById("app1");
          div.scrollTop = div.scrollHeight;
        });
    }, 2000);
  },
  updated: function() {},
  destroyed: function() {}
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
#app {
  text-align: center;
  font-family: "雅黑";
  font-size: 26px;
}
</style>
