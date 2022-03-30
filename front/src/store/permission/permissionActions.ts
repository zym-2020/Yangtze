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
    generateRoutes({ commit }: AugmentedActionContext, roles: string[]): void
}

export const permissionActions: ActionTree<PermissionState, RootState> & Actions = {
    generateRoutes({ commit }: AugmentedActionContext, roles: string[]) {
        const result = filterAsyncRoutes(asyncRouters, roles)
        commit('SET_ROUTERS', result)
    }
}

const filterAsyncRoutes = (asyncRouters: RouteRecordRaw[], roles: string[]) => {
    const result: RouteRecordRaw[] = []
    asyncRouters.forEach(item => {
        if(hasPermission(item, roles)) {
            if(item.children) {
                item.children = filterAsyncRoutes(item.children, roles)
            }
            result.push(item)
        }
    })
    return result
}

const hasPermission = function (router: RouteRecordRaw, roles: string[]) {
    if (router.meta && router.meta.roles) {
        return roles.some(role => {
            if (router.meta?.roles != undefined) {
                return (router.meta.roles as string[]).includes(role)
            }
        })
    } else {
        return true
    }
}
