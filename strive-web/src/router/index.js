import Vue from 'vue'
import Router from 'vue-router'
// import A from '@/modules/a/a'
// import B from '@/modules/b/b'

Vue.use(Router)

// export default new Router({
//     routes: [
//         {
//             path: '/a/',
//             name: 'a',
//             component: A
//         },
//         {
//             path: '/b/',
//             name: 'b',
//             component: B
//         }
//     ]
// })

// 创建路由实例并配置路由映射  
// path:'*',redirect:'/home'  重定向到path是/home的映射
const router = new VueRouter({
    routes: [{
        path: '/top', component: require('/pages/main/top.vue')
    }, {
        path: '/left', component: require('/pages/main/left.vue')
    }, {
        path: '*', redirect: '/'
    }]
})


// 输出router
export default router;
