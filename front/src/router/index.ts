import { createRouter, createWebHistory, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/Index.vue'
import Home from '@/views/Home.vue'
import { shallowRef } from 'vue'

export const constantRoutes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: '/',
        component: Layout,
        children: [
            {
                path: '',
                name: 'Home',
                component: Home,
                meta: {
                    keepAlive: true,
                    title: '首页'
                }
            },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/login/Login.vue'),
        meta: {
            title: '登录'
        }
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/register/Index.vue'),
        meta: {
            title: '注册'
        }
    },
    {
        path: '/404',
        name: "404",
        component: () => import('@/views/404/Index.vue'),
        meta: {
            title: '404'
        }
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes
})



export const asyncRouters: Array<RouteRecordRaw> = [
    {
        path: "/data",
        name: 'Data',
        component: shallowRef(Layout),
        children: [
            {
                path: '',
                redirect: '/data/list'
            },
            {
                path: 'list',
                name: 'List',
                component: () => import('@/views/data/Index.vue'),
                meta: {
                    keepAlive: true,
                    title: '资源门户'
                }
            },
            {
                path: "findMap",
                name: 'FindMap',
                component: () => import('@/components/scenePart/FindMap.vue'),
                meta: {
                    title: '高级检索'
                }
            },
            {
                path: ':id',
                name: 'shareFile',
                component: () => import('@/views/data/ShareFileIndex.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '数据详情'
                }
            }
        ]
    },
    {
        path: "/scenario",
        name: 'Scenario',
        component: shallowRef(Layout),
        children: [
            {
                path: "",
                redirect: "/scenario/list"
            },
            {
                path: "list",
                component: () => import('@/views/scenario/Index.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,
                    title: '一张图'
                }
            },
            {
                path: 'scenarioOne',
                component: () => import('@/views/scenario/realTimeMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,
                    title: '一张图'
                }
            },
            {
                path: 'scenarioTwo',
                component: () => import('@/views/scenario/TheMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '一张图'
                }
            },
            {
                path: 'scenarioThree',
                component: () => import('@/views/scenario/shipMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '一张图'
                }
            },
            {
                path: 'scenarioFour',
                component: () => import('@/views/scenario/projectMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '一张图'
                }
            },
        ]
    },
    {
        path: '/analyze',
        name: 'Analyze',
        component: shallowRef(Layout),
        children: [
            {
                path: '',
                redirect: '/analyze/list'
            },
            {
                path: 'list',
                name: 'AnalyzeList',
                component: () => import('@/views/analyze/Index.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '分析中心'
                    // keepAlive: true
                }
            },
            {
                path: 'project/:id',
                name: 'project',
                component: () => import('@/views/analyze/Project.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '分析中心'
                }
            }
        ]
    },
    {
        path: '/user',
        name: 'User',
        component: shallowRef(Layout),
        children: [
            {
                path: '',
                name: 'UserChild',
                redirect: '/user/space'
            },
            {
                path: 'space',
                name: 'UserSpace',
                component: () => import('@/views/user/Index.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,
                    title: '个人空间'
                }
            },
            {
                path: 'share',
                name: 'share',
                component: () => import('@/views/user/UploadShareFile.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '上传条目'
                }
            },
            {
                path: 'updateShare/:id',
                name: 'updateShare',
                component: () => import('@/views/user/UpdateShareFile.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,
                    title: '更新条目'
                }
            },
            {
                path: 'admin',
                name: 'UserAdmin',
                component: () => import('@/views/user/Admin.vue'),
                meta: {
                    role: ['admin'],
                    title: '管理员空间',
                    keepAlive: true,
                }

            },
            {
                path: 'visualBind/:id',
                name: 'VisualBind',
                component: () => import('@/views/user/views/VisualDataBind.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '数据可视化绑定'
                }
            }

        ]
    },
    {
        path: "/:catchAll(.*)",
        name: 'Redirect404',
        redirect: '/404'
    }
]

export function resetRouter() {
    const newRouter = createRouter({
        history: createWebHashHistory(),
        routes: constantRoutes
    });
    const tempList: any[] = []
    router.getRoutes().forEach(item => {
        for (let i = 0; i < newRouter.getRoutes().length; i++) {
            if (item.name === newRouter.getRoutes()[i].name) {
                return
            }
        }
        tempList.push(item.name)
    })
    tempList.forEach(item => {
        router.removeRoute(item)
    })

}

export default router
