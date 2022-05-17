import { createRouter, createWebHistory, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/Index.vue'
import Home from '@/views/Home.vue'
import { shallowRef } from 'vue'

export const constantRoutes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Layout,
        children: [
            {
                path: '',
                component: Home,
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
        component: shallowRef(Layout),
        meta: {
            title: '数据列表'
        },
        children: [
            {
                path: '',
                component: () => import('@/views/data/Index.vue'),
            }
        ]
    },
    {
        path: "/scenario",
        component: shallowRef(Layout),
        meta: {
            title: '场景'
        },
        children: [
            {
                path: '',
                component: () => import('@/views/scenario/Index.vue'),
                meta: {
                    roles: ['member', 'admin']
                }
            }
        ]
    },
    {
        path: '/analyze',
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
                component: () => import('@/views/analyze/Index.vue'),
                meta: {
                    roles: ['member', 'admin']
                }
            },
            {
                path: 'project/:id',
                name: 'project',
                component: () => import('@/views/analyze/Project.vue'),
                meta: {
                    roles: ['member', 'admin']
                }
            }
        ]
    },
    {
        path: '/user',
        component: shallowRef(Layout),
        meta: {
            title: '用户空间'
        },
        children: [
            {
                path: '',
                redirect: '/user/space'
            },
            {
                path: 'space',
                component: () => import('@/views/user/Index.vue'),
                meta: {
                    roles: ['member', 'admin']
                }
            },
            {
                path: 'share',
                name: 'share',
                component: () => import('@/views/user/UploadShareFile.vue'),
                meta: {
                    roles: ['member', 'admin']
                }
            },
            {
                path: 'admin',
                component: () => import('@/views/user/Admin.vue'),
                meta: {
                    roles: ['admin']
                },
                children: [
                    {
                        path: '',
                        redirect: '/user/admin/resource'
                    },
                    {
                        path: 'resource',
                        component: () => import('@/views/user/views/ResourceManage.vue'),
                        meta: {
                            roles: ['admin']
                        }
                    },
                    {
                        path: 'scenario',
                        component: () => import('@/views/user/views/ScenarioManage.vue'),
                        meta: {
                            roles: ['admin']
                        }
                    },
                    {
                        path: 'project',
                        component: () => import('@/views/user/views/ProjectManage.vue'),
                        meta: {
                            roles: ['admin']
                        }
                    },
                    {
                        path: 'message',
                        component: () => import('@/views/user/views/MessageManage.vue'),
                        meta: {
                            roles: ['admin']
                        }
                    }
                ]
            }
        ]
    },
    {
        path: "/:catchAll(.*)",
        redirect: '/404'
    }
]

export function resetRouter() {
    const newRouter = createRouter({
        history: createWebHashHistory(),
        routes: constantRoutes
    });
    (router as any).matcher = (newRouter as any).matcher // reset router
}

export default router
