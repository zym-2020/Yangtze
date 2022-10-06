import { RootState } from '@/store'
import { PermissionState } from './permissionState'
import { ActionTree, ActionContext } from 'vuex'
import { Mutations } from './permissionMutations'
import { asyncRouters } from '@/router'
import { RouteRecordRaw } from 'vue-router'

type AugmentedActionContext = {
    commit<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<PermissionState, RootState>, 'commit'>

export type Actions = {
    generateRoutes({ commit }: AugmentedActionContext, role: string): void
    clearRouters({ commit }: AugmentedActionContext): void
}

export const permissionActions: ActionTree<PermissionState, RootState> & Actions = {
    generateRoutes({ commit }: AugmentedActionContext, role: string) {
        const temp = JSON.parse(JSON.stringify(asyncRouters))
        const result = filterAsyncRoutes(asyncRouters, role)
        commit('SET_ROUTERS', result)
    },
    clearRouters({ commit }: AugmentedActionContext) {
        commit('SET_ROUTERS', [])
    }
}

const filterAsyncRoutes = (asyncRouters: RouteRecordRaw[], role: string) => {
    const result: RouteRecordRaw[] = []
    asyncRouters.forEach(item => {
        const r = { ...item }
        if(hasPermission(r, role)) {
            if(r.children) {
                r.children = filterAsyncRoutes(r.children, role)
            }
            result.push(r)
        }
    })
    return result
}

const hasPermission = function (router: RouteRecordRaw, role: string) {
    if (router.meta != undefined && router.meta.role != undefined) {
        for(let i = 0; i < (router.meta.role as string[]).length; i++) {
            if((router.meta.role as string[])[i] === role) {
                return true
            }
        }
        return false

    } else {
        return true
    }
}
