import Vue from 'vue'
import VueRouter from 'vue-router'
// import A from '@/modules/a/a'
// import B from '@/modules/b/b'

Vue.use(VueRouter)

// 创建路由实例并配置路由映射  
// path:'*',redirect:'/home'  重定向到path是/home的映射
const router = new VueRouter({
    routes: [{
        path: '/left', component: require('../nav/left.vue')
    }
        // , {
        //     path: '/top', component: require('../top.vue')
        // }
    ]
})


// 输出router
export default router;
