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
                    keepAlive: true
                }
            },
        ]
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('../views/login/Login.vue')
    },
    {
        path: '/register',
        name: 'Register',
        component: () => import('@/views/register/Index.vue')
    },
    {
        path: '/404',
        name: "404",
        component: () => import('@/views/404/Index.vue')
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
        meta: {
            title: '数据列表'
        },
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
                    keepAlive: true
                }
            },
            {
                path: "spaceSearch",
                name: 'SpaceSearch',
                component: () => import('@/views/data/SpaceSearch.vue')
            },
            {
                path: ':id',
                name: 'shareFile',
                component: () => import('@/views/data/ShareFileIndex.vue'),
                meta: {
                    role: ['member', 'admin']
                }
            }
        ]
    },
    {
        path: "/scenario",
        name: 'Scenario',
        component: shallowRef(Layout),
        meta: {
            title: '场景'
        },
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
                    keepAlive: true
                }
            },
            {
                path: 'scenarioOne',
                component: () => import('@/views/scenario/realTimeMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true
                }
            },
            {
                path: 'scenarioTwo',
                component: () => import('@/views/scenario/TheMap.vue'),
                meta: {
                    role: ['member', 'admin']
                }
            },
            {
                path: 'scenarioThree',
                component: () => import('@/views/scenario/aisShip.vue'),
                meta: {
                    role: ['member', 'admin']
                }
            },
            {
                path: 'scenarioFour',
                component: () => import('@/views/scenario/projectMap.vue'),
                meta: {
                    role: ['member', 'admin']
                }
            },
        ]
    },
    {
        path: '/analyze',
        name: 'Analyze',
        component: shallowRef(Layout),
        meta: {
            title: '分析'
        },
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
                    // keepAlive: true
                }
            },
            {
                path: 'project/:id',
                name: 'project',
                component: () => import('@/views/analyze/Project.vue'),
                meta: {
                    role: ['member', 'admin']
                }
            }
        ]
    },
    {
        path: '/user',
        name: 'User',
        component: shallowRef(Layout),
        meta: {
            title: '用户空间'
        },
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
                    keepAlive: true
                }
            },
            {
                path: 'share',
                name: 'share',
                component: () => import('@/views/user/UploadShareFile.vue'),
                meta: {
                    role: ['member', 'admin']
                }
            },
            {
                path: 'updateShare/:id',
                name: 'updateShare',
                component: () => import('@/views/user/UpdateShareFile.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true
                }
            },
            {
                path: 'admin',
                name: 'UserAdmin',
                component: () => import('@/views/user/Admin.vue'),
                meta: {
                    role: ['admin']
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
