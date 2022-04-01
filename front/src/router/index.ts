import { createRouter, createWebHistory, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Home from '../views/Home.vue'
import Layout from '@/layout/Index.vue'
import { shallowRef } from 'vue'

export const constantRoutes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Layout,
        children: [
            {
                path: '',
                component: Home
            },
        ]
    },
    {
        path: '/404',
        component: () => import('@/views/404/Index.vue')
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
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes
})



export const asyncRouters: Array<RouteRecordRaw> = [
    {
        path: '/dataManage',
        component: shallowRef(Layout),
        meta: {
            title: '数据管理',
            alwaysShow: true
        },
        children: [
            {
                path: 'handle',
                component: () => import('@/components/dataManage/handel/Index.vue'),
                meta: {
                    title: '数据处理',
                    alwaysShow: true,
                    bread: '数据管理/数据处理',
                    tag: true
                }
            },
            {
                path: 'upload',
                component: () => import('@/components/dataManage/upload/Index.vue'),
                meta: {
                    title: '数据上传',
                    alwaysShow: true,
                    bread: '数据管理/数据上传',
                    tag: true
                }
            },
            {
                path: 'download',
                component: () => import('@/components/dataManage/download/Index.vue'),
                meta: {
                    title: '数据下载',
                    alwaysShow: true,
                    bread: '数据管理/数据下载',
                    tag: true
                }
            },
            {
                path: 'manage',
                component: () => import('@/components/dataManage/manage/Index.vue'),
                meta: {
                    title: '数据管理',
                    alwaysShow: true,
                    bread: '数据管理/数据管理',
                    tag: true
                }
            }
        ]
    },
    {
        path: '/projects',
        component: shallowRef(Layout),
        meta: {
            title: '项目管理',
            roles: ['Admin'],
            alwaysShow: true
        },
        children: [
            {
                path: 'create',
                component: () => import('@/components/projects/create/Index.vue'),
                meta: {
                    title: '创建项目',
                    roles: ['Admin'],
                    alwaysShow: true,
                    bread: '项目管理/创建项目',
                    tag: true
                }
            },
            {
                path: 'manage',
                component: () => import('@/components/projects/manage/Index.vue'),
                meta: {
                    title: '项目管理',
                    roles: ['Admin'],
                    alwaysShow: true,
                    bread: '项目管理/项目管理',
                    tag: true
                }
            }
        ]
    },
    {
        path: '/mathematics',
        component: shallowRef(Layout),
        meta: {
            title: '数学模型',
            alwaysShow: true
        },
        children: [
            {
                path: 'generation',
                component: () => import('@/components/mathematics/generation/Index.vue'),
                meta: {
                    title: '生成数据',
                    alwaysShow: true,
                    bread: '数学模型/生成数据',
                    tag: true
                }
            },
            {
                path: 'visualization',
                component: () => import('@/components/mathematics/visualization/Index.vue'),
                meta: {
                    title: '流场可视化',
                    alwaysShow: true,
                    bread: '数学模型/流场可视化',
                    tag: true
                }
            },
            {
                path: '3d',
                component: () => import('@/components/mathematics/3d/Index.vue'),
                meta: {
                    title: '显示三维流场',
                    alwaysShow: true,
                    bread: '数学模型/显示三维流场',
                    tag: true
                }
            },
            {
                path: 'contrast',
                component: () => import('@/components/mathematics/contrast/Index.vue'),
                meta: {
                    title: '工程前后对比',
                    alwaysShow: true,
                    bread: '数学模型/工程前后对比',
                    tag: true
                }
            }
        ]
    },
    {
        path: '/physical',
        component: shallowRef(Layout),
        meta: {
            title: '物理模型',
            alwaysShow: true
        },
        children: [
            {
                path: 'map',
                component: () => import('@/components/physical/map/Index.vue'),
                meta: {
                    title: '底图',
                    alwaysShow: true,
                    bread: '物理模型/底图',
                    tag: true
                }
            },
            {
                path: 'photo',
                component: () => import('@/components/physical/photo/Index.vue'),
                meta: {
                    title: '图片',
                    alwaysShow: true,
                    bread: '物理模型/图片',
                    tag: true
                }
            },
            {
                path: 'video',
                component: () => import('@/components/physical/video/Index.vue'),
                meta: {
                    title: '视频',
                    alwaysShow: true,
                    bread: '物理模型/视频',
                    tag: true
                }
            }
        ]
    },
    {
        path: '/forecast',
        component: shallowRef(Layout),
        meta: {
            title: '潮汐预报',
            alwaysShow: true
        },
        children: [
            {
                path: 'parameter',
                component: () => import('@/components/forecast/parameter/Index.vue'),
                meta: {
                    title: '更新参数',
                    alwaysShow: true,
                    bread: '潮汐预报/更新参数',
                    tag: true
                }
            },
            {
                path: 'station',
                component: () => import('@/components/forecast/station/Index.vue'),
                meta: {
                    title: '选择潮位站',
                    alwaysShow: true,
                    bread: '潮汐预报/选择潮位站',
                    tag: true
                }
            },
            {
                path: 'flux',
                component: () => import('@/components/forecast/flux/Index.vue'),
                meta: {
                    title: '流量预报',
                    alwaysShow: true,
                    bread: '潮汐预报/流量预报',
                    tag: true
                }
            }
        ]
    },
    {
        path: '/riverbed',
        component: shallowRef(Layout),
        meta: {
            title: '河床演变',
            alwaysShow: true
        },
        children: [
            {
                path: 'section',
                component: () => import('@/components/riverbed/section/Index.vue'),
                meta: {
                    title: '断面形态',
                    alwaysShow: true,
                    bread: '河床演变/断面形态',
                    tag: true
                }
            },
            {
                path: 'sectionContrast',
                component: () => import('@/components/riverbed/sectionContrast/Index.vue'),
                meta: {
                    title: '断面比较',
                    alwaysShow: true,
                    bread: '河床演变/断面比较',
                    tag: true
                }
            },
            {
                path: 'branch',
                component: () => import('@/components/riverbed/branch/Index.vue'),
                meta: {
                    title: '汊道断面比较',
                    alwaysShow: true,
                    bread: '河床演变/汊道断面比较',
                    tag: true
                }
            },
            {
                path: 'area',
                component: () => import('@/components/riverbed/area/Index.vue'),
                meta: {
                    title: '断面面积冲淤',
                    alwaysShow: true,
                    bread: '河床演变/断面面积冲淤',
                    tag: true
                }
            },
            {
                path: 'elev',
                component: () => import('@/components/riverbed/elev/Index.vue'),
                meta: {
                    title: '特定高程冲淤',
                    alwaysShow: true,
                    bread: '河床演变/特定高程冲淤',
                    tag: true
                }
            },
            {
                path: 'volume',
                component: () => import('@/components/riverbed/volume/Index.vue'),
                meta: {
                    title: '河道容积计算',
                    alwaysShow: true,
                    bread: '河床演变/河道容积计算',
                    tag: true
                }
            },
            {
                path: 'slope',
                component: () => import('@/components/riverbed/slope/Index.vue'),
                meta: {
                    title: '河床坡度提取',
                    alwaysShow: true,
                    bread: '河床演变/河床坡度提取',
                    tag: true
                }
            },
            {
                path: 'line',
                component: () => import('@/components/riverbed/line/Index.vue'),
                meta: {
                    title: '深泓线比较',
                    alwaysShow: true,
                    bread: '河床演变/深泓线比较',
                    tag: true
                }
            },
            {
                path: 'deepContrast',
                component: () => import('@/components/riverbed/deepContrast/Index.vue'),
                meta: {
                    title: '等深线对比',
                    alwaysShow: true,
                    bread: '河床演变/等深线对比',
                    tag: true
                }
            },
            {
                path: 'boundary',
                component: () => import('@/components/riverbed/boundary/Index.vue'),
                meta: {
                    title: '边界分析',
                    alwaysShow: true,
                    bread: '河床演变/边界分析',
                    tag: true
                }
            }
        ]
    },
    {
        path: '/hydrology',
        component: shallowRef(Layout),
        meta: {
            title: '水文分析',
            alwaysShow: true
        },
        children: [
            {
                path: 'process',
                component: () => import('@/components/hydrology/process/Index.vue'),
                meta: {
                    title: '潮位过程',
                    alwaysShow: true,
                    bread: '水文分析/潮位过程',
                    tag: true
                }
            },
            {
                path: 'stream',
                component: () => import('@/components/hydrology/stream/Index.vue'),
                meta: {
                    title: '流速流向',
                    alwaysShow: true,
                    bread: '水文分析/流速流向',
                    tag: true
                }
            },
            {
                path: 'sand',
                component: () => import('@/components/hydrology/sand/Index.vue'),
                meta: {
                    title: '含沙量过程',
                    alwaysShow: true,
                    bread: '水文分析/含沙量过程',
                    tag: true
                }
            },
            {
                path: 'sandRate',
                component: () => import('@/components/hydrology/sandRate/Index.vue'),
                meta: {
                    title: '流量及输沙率',
                    alwaysShow: true,
                    bread: '水文分析/流量及输沙率',
                    tag: true
                }
            },
            {
                path: 'particle',
                component: () => import('@/components/hydrology/particle/Index.vue'),
                meta: {
                    title: '悬移质颗分',
                    alwaysShow: true,
                    bread: '水文分析/悬移质颗分',
                    tag: true
                }
            },
            {
                path: 'achievement',
                component: () => import('@/components/hydrology/achievement/Index.vue'),
                meta: {
                    title: '断面成果',
                    alwaysShow: true,
                    bread: '水文分析/断面成果',
                    tag: true
                }
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
