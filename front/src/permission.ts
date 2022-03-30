import router from '@/router/index'
import NProgress from 'nprogress'
import { RouteLocationNormalized } from 'vue-router'
import { useStore } from '@/store'
import { getToken } from '@/utils/auth'

const store = useStore()

NProgress.configure({ showSpinner: false })
router.beforeEach(async (to: RouteLocationNormalized, from: RouteLocationNormalized, next: any) => {
    NProgress.start()
    if (getToken() != null) {
        if (to.path === '/login') {
            next({ path: '/' })
            NProgress.done()
        } else {
            if(store.state.user.roles.length === 0) {
                try {
                    await store.dispatch("getUserInfo", undefined)
                    const roles = store.state.user.roles
                    store.dispatch("generateRoutes", roles)
                    store.state.permission.addRouters.forEach(item => {
                        router.addRoute(item)
                    })
                    next({ ...to, replace: true })
                    NProgress.done()
                } catch(err) {
                    store.dispatch("logout", undefined)
                    next('/login')
                    NProgress.done()
                }

            } else {
                next()
            }
        }
    } else {
        if(to.path === '/login') {
            next()
            NProgress.done
        } else {
            next('/login')
            NProgress.done
        }
    }

    
})