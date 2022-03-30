import { RouteRecordRaw } from 'vue-router'
import { constantRoutes } from '@/router'

export interface PermissionState {
    routers: RouteRecordRaw[],
    addRouters: RouteRecordRaw[]
}

export const state: PermissionState = {
    routers: constantRoutes,
    addRouters: []
}