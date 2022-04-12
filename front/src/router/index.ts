import { createRouter, createWebHistory, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import Layout from '@/layout/Index.vue'
import Home from '@/views/home/Index.vue'
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
                // component: () => import('@/views/home/cesium.vue')
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
            alwaysShow: true,
            icon: '#icon-guanli'
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
            alwaysShow: true,
            icon: '#icon-mobanguanli'
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
            alwaysShow: true,
            icon: '#icon-shujuyanjiu'
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
            alwaysShow: true,
            icon: '#icon-changjing'
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
            alwaysShow: true,
            icon: '#icon-haitu'
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
            alwaysShow: true,
            icon: '#icon-tiqushuxing'
        },
        children: [
            {
                path: 'section',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '断面形态',
                    alwaysShow: true,
                    bread: '河床演变/断面形态',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/section/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/section/Index.vue')
                    }
                ]
            },
            {
                path: 'sectionContrast',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '断面比较',
                    alwaysShow: true,
                    bread: '河床演变/断面比较',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/sectionContrast/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/sectionContrast/Index.vue')
                    }
                ]
            },
            {
                path: 'branch',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '汊道断面比较',
                    alwaysShow: true,
                    bread: '河床演变/汊道断面比较',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/branch/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/branch/Index.vue')
                    }
                ]
            },
            {
                path: 'area',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '断面面积冲淤',
                    alwaysShow: true,
                    bread: '河床演变/断面面积冲淤',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/area/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/area/Index.vue')
                    }
                ]
            },
            {
                path: 'elev',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '特定高程冲淤',
                    alwaysShow: true,
                    bread: '河床演变/特定高程冲淤',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/elev/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/elev/Index.vue')
                    }
                ]
            },
            {
                path: 'volume',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '河道容积计算',
                    alwaysShow: true,
                    bread: '河床演变/河道容积计算',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/volume/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/volume/Index.vue')
                    }
                ]
            },
            {
                path: 'slope',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '河床坡度提取',
                    alwaysShow: true,
                    bread: '河床演变/河床坡度提取',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/slope/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/slope/Index.vue')
                    }
                ]
            },
            {
                path: 'line',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '深泓线比较',
                    alwaysShow: true,
                    bread: '河床演变/深泓线比较',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/line/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/line/Index.vue')
                    }
                ]
            },
            {
                path: 'deepContrast',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '等深线对比',
                    alwaysShow: true,
                    bread: '河床演变/等深线对比',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/deepContrast/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/deepContrast/Index.vue')
                    }
                ]
            },
            {
                path: 'boundary',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '边界分析',
                    alwaysShow: true,
                    bread: '河床演变/边界分析',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/riverbed/deepContrast/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/riverbed/boundary/Index.vue')
                    }
                ]
            }
        ]
    },
    {
        path: '/hydrology',
        component: shallowRef(Layout),
        meta: {
            title: '水文分析',
            alwaysShow: true,
            icon: '#icon-yanmeifenxi'
        },
        children: [
            {
                path: 'process',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '潮位过程',
                    alwaysShow: true,
                    bread: '水文分析/潮位过程',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/hydrology/process/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/hydrology/process/Index.vue')
                    }
                ]
            },
            {
                path: 'stream',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '流速流向',
                    alwaysShow: true,
                    bread: '水文分析/流速流向',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/hydrology/stream/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/hydrology/stream/Index.vue')
                    }
                ]
            },
            {
                path: 'sand',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '含沙量过程',
                    alwaysShow: true,
                    bread: '水文分析/含沙量过程',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/hydrology/sand/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/hydrology/sand/Index.vue')
                    }
                ]
            },
            {
                path: 'sandRate',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '流量及输沙率',
                    alwaysShow: true,
                    bread: '水文分析/流量及输沙率',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/hydrology/sandRate/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/hydrology/sandRate/Index.vue')
                    }
                ]
            },
            {
                path: 'particle',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '悬移质颗分',
                    alwaysShow: true,
                    bread: '水文分析/悬移质颗分',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/hydrology/particle/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/hydrology/particle/Index.vue')
                    }
                ]
            },
            {
                path: 'achievement',
                component: () => import('@/components/map/Index.vue'),
                meta: {
                    title: '断面成果',
                    alwaysShow: true,
                    bread: '水文分析/断面成果',
                    tag: true
                },
                children: [
                    {
                        path: '',
                        redirect: '/hydrology/achievement/map'
                    },
                    {
                        path: 'map',
                        component: () => import('@/components/hydrology/achievement/Index.vue')
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
