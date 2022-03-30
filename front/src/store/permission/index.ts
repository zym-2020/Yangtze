import { Store as VuexStore, CommitOptions, DispatchOptions, Module } from 'vuex'
import { RootState } from ".."
import { PermissionState, state } from './permissionState'
import { Mutations, permissionMutations } from './permissionMutations'
import { Actions, permissionActions } from './permissionActions'


export type PermissionStore<S = PermissionState> = Omit<VuexStore<S>, 'getters' | 'commit' | 'dispatch'> &
{
    commit<K extends keyof Mutations, P extends Parameters<Mutations[K]>[1]>(key: K, payload: P, options?: CommitOptions): ReturnType<Mutations[K]>
} & {
    dispatch<K extends keyof Actions>(key: K, payload: Parameters<Actions[K]>[1], options?: DispatchOptions): ReturnType<Actions[K]>
}

export const store: Module<PermissionState, RootState> = {
    state: state,
    mutations: permissionMutations,
    actions: permissionActions
}