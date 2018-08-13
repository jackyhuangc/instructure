<template>
  <ul class="layui-nav layui-nav-tree" lay-filter="test">
    <li :class="index==0?'layui-nav-item layui-nav-itemed':'layui-nav-item'" v-for="(item,index) in menus.data" @click="menuClick($event,item.code)" v-on:mouseenter="showNavBar($event)" v-on:mouseleave="hiddenNavBar($event)">
      <a class="" href="javascript:;">{{item.title}}
        <span class="layui-nav-more"></span>
      </a>
      <dl class="layui-nav-child">
        <dd v-for="(chd,ind) in item.child" v-on:mouseenter="showNavBar($event)" v-on:mouseleave="hiddenNavBar($event)">
          <a :href="chd.url" :target="chd.target==1?'_blank':'right_frame'">{{chd.title}}</a>

          <span class="layui-nav-bar" style="top: 0px; height: 40px; opacity: 0;"></span>
        </dd>
      </dl>
      <span class="layui-nav-bar" style="top: 0px; height: 45px; opacity: 0;"></span>
    </li>
  </ul>
</template>

<script>
export default {
  name: "left",
  props: ["menus"], // 可用于值传递
  components: {},
  methods: {
    exit: function() {},
    menuClick: function(evt, code) {
      // evt 怎么会是由a标签触发?
      var $elm = $(evt.target).parent();

      if ($elm.hasClass("layui-nav-item")) {
        // 父节点展开判断
        if ($elm.hasClass("layui-nav-itemed")) {
          $elm.removeClass("layui-nav-itemed");
        } else {
          // 确实需要展开，需先折叠之前所有已展开的节点
          $(".layui-nav-itemed").removeClass("layui-nav-itemed");
          $elm.addClass("layui-nav-itemed");
        }
      } else {
        // 只有最里层的a标签dd才加选中样式
        $(".layui-this").removeClass("layui-this");
        $elm.addClass("layui-this");
      }
    },
    showNavBar: function(evt) {
      $(".layui-nav-bar").css("opacity", "0");

      // 移入显示待选状态layui-nav-bar
      var span = $(evt.target).children("span");
      span.css("opacity", "1");
    },
    hiddenNavBar: function(evt) {
      // 如果这是里层a标签，则移出后将其对应的父节点li标签layui-nav-bar显示
      if ($(evt.target).children("a")) {
        $(evt.target)
          .parent()
          .parent()
          .children("span")
          .css("opacity", "1");
      }

      // 移出关闭待选状态layui-nav-bar
      var span = $(evt.target).children("span");
      span.css("opacity", "0");
    }
  },
  created: function() {},
  mounted() {},
  beforeUpdate() {},
  updated: function() {
    // 页面绘制完成后，绑定折叠事件
    $(".title").unbind("click");
    $(".title").click(function() {
      var $ul = $(this).next("ul");
      $("dd")
        .find("ul")
        .slideUp();
      if ($ul.is(":visible")) {
        $(this)
          .next("ul")
          .slideUp();
      } else {
        $(this)
          .next("ul")
          .slideDown();
      }
    });

    // 页面绘制完成后，绑定菜单点击事件
    $(".menuson li.active").removeClass("active");
    $(".menuson li").unbind("click");
    $(".menuson li").click(function() {
      $(".menuson li.active").removeClass("active");
      $(this).addClass("active");
    });
  },
  destroyed: function() {}
};
</script>

<style scoped>

</style>