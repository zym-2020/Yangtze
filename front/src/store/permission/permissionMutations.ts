import { MutationTree } from "vuex";
import { PermissionState } from "./permissionState";
import { RouteRecordRaw } from 'vue-router'
import { constantRoutes } from "@/router";

export interface Mutations<S = PermissionState> {
    SET_ROUTERS(state: S, routers: RouteRecordRaw[]): void
}

export const permissionMutations: MutationTree<PermissionState> & Mutations = {
    SET_ROUTERS(state: PermissionState, routers: RouteRecordRaw[]) {
        state.addRouters = routers
        state.routers = constantRoutes.concat(routers)
    }
}