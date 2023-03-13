import {
  createRouter,
  createWebHistory,
  createWebHashHistory,
  RouteRecordRaw,
} from "vue-router";
import Layout from "@/layout/Index.vue";
import Home from "@/views/Home.vue";
import { shallowRef } from "vue";

export const constantRoutes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "/",
    component: Layout,
    children: [
      {
        path: "",
        name: "Home",
        component: Home,
        meta: {
          keepAlive: true,
          title: "深水航道水沙环境与工程安全平台",
          key: "Home",
        },
      },
    ],
  },
  {
    path: "/login",
    name: "Login",
    component: () => import("../views/login/Login.vue"),
    meta: {
      title: "登录",
    },
  },
  {
    path: "/register",
    name: "Register",
    component: () => import("@/views/register/Index.vue"),
    meta: {
      title: "注册",
    },
  },
  {
    path: "/404",
    name: "404",
    component: () => import("@/views/404/Index.vue"),
    meta: {
      title: "404",
    },
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes: constantRoutes,
});

export const asyncRouters: Array<RouteRecordRaw> = [
  {
    path: "/data",
    name: "Data",
    component: shallowRef(Layout),
    redirect: "/data/list",
    children: [
      {
        path: "list",
        name: "DataList",
        component: () => import("@/views/data/Index.vue"),
        meta: {
          role: ["member", "admin"],
          keepAlive: true, //此处缓存与该也没的固钉会有页面的bug
          title: "资源门户",
          key: "DataList",
        },
      },
      {
        path: "findMap",
        name: "FindMap",
        component: () => import("@/components/scenePart/FindMap.vue"),
        meta: {
          title: "高级检索",
          role: ["member", "admin"],
          key: "FindMap",
        },
      },
      {
        path: ":id",
        name: "shareFile",
        component: () => import("@/views/data/ShareFileIndex.vue"),
        meta: {
          role: ["member", "admin"],
          title: "数据详情",
          key: "ShareFile",
        },
      },
    ],
  },
  {
    path: "/scenario",
    name: "Scenario",
    component: shallowRef(Layout),
    children: [
      {
        path: "",
        redirect: "/scenario/list",
      },
      {
        path: "list",
        name: "ScenarioList",
        component: () => import("@/views/scenario/Index.vue"),
        meta: {
          role: ["member", "admin"],
          keepAlive: true,
          title: "一张图",
          key: "ScenarioList",
        },
      },
      {
        path: "scenarioOne",
        name: "ScenarioOne",
        component: () => import("@/views/scenario/realTimeMap.vue"),
        meta: {
          role: ["member", "admin"],
          keepAlive: true,
          title: "实时监测",
          key: "ScenarioOne",
        },
      },
      {
        path: "scenarioTwo",
        name: "ScenarioTwo",
        component: () => import("@/views/scenario/flowMap.vue"),
        meta: {
          role: ["member", "admin"],
          title: "长江流场",
          keepAlive: false,
          key: "ScenarioTwo",
        },
      },
      {
        path: "scenarioFour",
        name: "ScenarioFour",
        component: () => import("@/views/scenario/projectMap.vue"),
        meta: {
          role: ["member", "admin"],
          title: "工程方案",
          keepAlive: true,
          key: "ScenarioFour",
        },
      },
      {
        path: "waterway",
        name: "Waterway",
        component: () => import("@/views/scenario/WaterwayMap.vue"),
        meta: {
          role: ["member", "admin"],
          title: "航道一张图",
          keepAlive: true,
          key: "Waterway",
        },
      },
    ],
  },
  {
    path: "/analyze",
    name: "Analyze",
    component: shallowRef(Layout),
    children: [
      {
        path: "",
        redirect: "/analyze/list",
      },
      {
        path: "list",
        name: "AnalyzeList",
        component: () => import("@/views/analyze/Index.vue"),
        meta: {
          role: ["member", "admin"],
          title: "分析中心",
          keepAlive: true,
          key: "AnalyzeList",
        },
      },
      {
        path: "project/:id",
        name: "project",
        component: () => import("@/views/analyze/Project.vue"),
        meta: {
          role: ["member", "admin"],
          title: "分析中心",
          key: "Project",
        },
      },
    ],
  },

  {
    path: "/user",
    name: "User",
    component: shallowRef(Layout),
    children: [
      {
        path: "",
        name: "UserChild",
        redirect: "/user/space",
      },
      {
        path: "space",
        name: "UserSpace",
        component: () => import("@/views/user/Index.vue"),
        meta: {
          role: ["member", "admin"],
          keepAlive: true,
          title: "个人空间",
        },
        redirect: "/user/space/file",
        children: [
          {
            path: "file",
            name: "UserSpaceFile",
            component: () => import("@/views/user/views/UserFile.vue"),
            meta: {
              role: ["member", "admin"],
              key: "UserSpace",
            },
          },
          {
            path: "dataList",
            name: "UserSpaceDataList",
            component: () => import("@/views/user/views/UserDataList.vue"),
            meta: {
              role: ["member", "admin"],
              key: "UserSpace",
            },
          },
          {
            path: "project",
            name: "UserSpaceProject",
            component: () => import("@/views/user/views/UserProject.vue"),
            meta: {
              role: ["member", "admin"],
              key: "UserSpace",
            },
          },
        ],
      },
      {
        path: "share",
        name: "share",
        component: () => import("@/views/user/UploadShareFile.vue"),
        meta: {
          role: ["member", "admin"],
          title: "上传条目",
          key: "share",
        },
      },
      {
        path: "updateShare/:id",
        name: "updateShare",
        component: () => import("@/views/user/UpdateShareFile.vue"),
        meta: {
          role: ["member", "admin"],
          keepAlive: true,
          title: "更新条目",
          key: "updateShare",
        },
      },
    ],
  },

  {
    path: "/admin",
    name: "Admin",
    component: () => import("@/views/admin/Index.vue"),
    meta: {
      role: ["admin"],
      title: "管理员界面",
    },
    redirect: "/admin/data",
    children: [
      {
        path: "data",
        name: "AdminData",
        component: () => import("@/views/admin/AdminDataList.vue"),
        meta: {
          role: ["admin"],
          key: "Admin",
        },
      },
      {
        path: "senario",
        name: "AdminSenario",
        component: () => import("@/views/admin/AdminSenario.vue"),
        meta: {
          role: ["admin"],
          key: "Admin",
        },
      },
      {
        path: "project",
        name: "AdminProject",
        component: () => import("@/views/admin/AdminProject.vue"),
        meta: {
          role: ["admin"],
          key: "Admin",
        },
      },
      {
        path: "user",
        name: "AdminUser",
        component: () => import("@/views/admin/AdminUser.vue"),
        meta: {
          role: ["admin"],
          key: "Admin",
        },
      },
      {
        path: "audit",
        name: "AdminAudit",
        component: () => import("@/views/admin/AdminAudit.vue"),
        meta: {
          role: ["admin"],
          key: "Admin",
        },
      },
    ],
  },
  {
    path: "/:catchAll(.*)",
    name: "Redirect404",
    redirect: "/404",
  },
];

export function resetRouter() {
  const newRouter = createRouter({
    history: createWebHashHistory(),
    routes: constantRoutes,
  });
  const tempList: any[] = [];
  router.getRoutes().forEach((item) => {
    for (let i = 0; i < newRouter.getRoutes().length; i++) {
      if (item.name === newRouter.getRoutes()[i].name) {
        return;
      }
    }
    tempList.push(item.name);
  });
  tempList.forEach((item) => {
    router.removeRoute(item);
  });
}

export default router;
