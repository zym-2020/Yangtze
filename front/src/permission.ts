import router from "@/router/index";
import NProgress from "nprogress";
import "nprogress/nprogress.css";
import { RouteLocationNormalized } from "vue-router";
import { useStore } from "@/store";
import { getToken } from "@/utils/auth";
import { toIdPages } from "@/utils/routerUtil";

const store = useStore();

NProgress.configure({ showSpinner: false });
router.beforeEach(
  async (
    to: RouteLocationNormalized,
    from: RouteLocationNormalized,
    next: any
  ) => {
    NProgress.start();
    if (getToken() != null) {
      if (to.path === "/login" || to.path === "/register") {
        window.document.title = "首页";
        next({ path: "/" });
        NProgress.done();
      } else {
        if (store.state.user.role === "") {
          try {
            await store.dispatch("getUserInfo", undefined);
            const role = store.state.user.role;
            store.dispatch("generateRoutes", role);
            store.state.permission.addRouters.forEach((item) => {
              router.addRoute(item);
            });
            window.document.title = to.meta.title as string;
            next({ ...to, replace: true });
            NProgress.done();
          } catch (err) {
            store.dispatch("logout", undefined);
            window.document.title = "登录";
            next("/login");
            NProgress.done();
          }
        } else {
          const code = await toIdPages(to);
          if (code === 1) {
            window.document.title = to.meta.title as string;
            next();
          } else if (code === -1) {
            window.document.title = "404";
            next("/404");
          } else if (code === 0) {
            window.document.title = to.meta.title as string;
            next();
          }

          NProgress.done();
        }
      }
    } else {
      if (to.path === "/login" || to.path === "/register" || to.path === "/") {
        window.document.title = to.meta.title as string;
        next();
        NProgress.done;
      } else {
        window.document.title = "登录";
        next("/login");
        NProgress.done;
      }
    }
    NProgress.done();
  }
);
