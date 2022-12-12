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
                    title: '水文综合信息管理与分析平台',
                    key: 'Home'
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
                name: 'DataList',
                component: () => import('@/views/data/Index.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,         //此处缓存与该也没的固钉会有页面的bug
                    title: '资源门户',
                    key: 'DataList'
                }
            },
            {
                path: "findMap",
                name: 'FindMap',
                component: () => import('@/components/scenePart/FindMap.vue'),
                meta: {
                    title: '高级检索',
                    role: ['member', 'admin'],
                    key: 'FindMap'
                }
            },
            {
                path: ':id',
                name: 'shareFile',
                component: () => import('@/views/data/ShareFileIndex.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '数据详情',
                    key: 'ShareFile'
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
                name: 'ScenarioList',
                component: () => import('@/views/scenario/Index.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,
                    title: '一张图',
                    key: 'ScenarioList'
                }
            },
            {
                path: 'scenarioOne',
                name: 'ScenarioOne',
                component: () => import('@/views/scenario/realTimeMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,
                    title: '一张图',
                    key: 'ScenarioOne'
                }
            },
            {
                path: 'scenarioTwo',
                name: 'ScenarioTwo',
                component: () => import('@/views/scenario/TheMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '一张图',
                    keepAlive: true,
                    key: 'ScenarioTwo'
                }
            },
            {
                path: 'scenarioThree',
                component: () => import('@/views/scenario/aisShip.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '一张图',
                    keepAlive: true,
                    key: 'ScenarioThree'
                }
            },
            {
                path: 'scenarioFour',
                name: 'ScenarioFour',
                component: () => import('@/views/scenario/projectMap.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '一张图',
                    keepAlive: true,
                    key: 'ScenarioFour'
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
                    title: '分析中心',
                    keepAlive: true,
                    key: 'AnalyzeList'
                }
            },
            {
                path: 'project/:id',
                name: 'project',
                component: () => import('@/views/analyze/Project.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '分析中心',
                    key: 'Project'
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
                },
                redirect: '/user/space/file',
                children: [
                    {
                        path: 'file',
                        name: 'UserSpaceFile',
                        component: () => import('@/views/user/views/UserFile.vue'),
                        meta: {
                            role: ['member', 'admin'],
                            key: 'UserSpace',
                        },
                    },
                    {
                        path: 'dataList',
                        name: 'UserSpaceDataList',
                        component: () => import('@/views/user/views/UserDataList.vue'),
                        meta: {
                            role: ['member', 'admin'],
                            key: 'UserSpace',
                        },
                    },
                    {
                        path: 'project',
                        name: 'UserSpaceProject',
                        component: () => import('@/views/user/views/UserProject.vue'),
                        meta: {
                            role: ['member', 'admin'],
                            key: 'UserSpace',
                        },
                    }
                ]
            },
            {
                path: 'share',
                name: 'share',
                component: () => import('@/views/user/UploadShareFile.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '上传条目',
                    key: 'share'
                }
            },
            {
                path: 'updateShare/:id',
                name: 'updateShare',
                component: () => import('@/views/user/UpdateShareFile.vue'),
                meta: {
                    role: ['member', 'admin'],
                    keepAlive: true,
                    title: '更新条目',
                    key: 'updateShare'
                }
            },
            {
                path: 'admin',
                name: 'UserAdmin',
                component: () => import('@/views/user/Admin.vue'),
                meta: {
                    role: ['admin'],
                    title: '管理员空间',
                    keepAlive: true
                },
                redirect: '/user/admin/resource',
                children: [
                    {
                        path: 'resource',
                        name: 'AdminResource',
                        component: () => import('@/views/user/views/ResourceManage.vue'),
                        meta: {
                            role: ['member', 'admin'],
                            key: 'UserAdmin',
                        }
                    },
                    {
                        path: 'project',
                        name: 'AdminProject',
                        component: () => import('@/views/user/views/ProjectManage.vue'),
                        meta: {
                            role: ['member', 'admin'],
                            key: 'UserAdmin',
                        }
                    },
                    {
                        path: 'audit',
                        name: 'AdminAudit',
                        component: () => import('@/views/user/views/AuditManage.vue'),
                        meta: {
                            role: ['member', 'admin'],
                            key: 'UserAdmin',
                        }
                    }
                ]

            },
            {
                path: 'visualBind/:id',
                name: 'VisualBind',
                component: () => import('@/views/user/views/VisualDataBind.vue'),
                meta: {
                    role: ['member', 'admin'],
                    title: '数据可视化绑定',
                    key: 'VisualBind'
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
