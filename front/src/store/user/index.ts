import { UserState, state } from "./userState"
import { Store as VuexStore, CommitOptions, DispatchOptions, Module } from 'vuex'
import { Mutations, userMutations } from "./userMutations"
import { Actions, userActions } from './userActions'
import { RootState } from ".."

export type UserStore<S = UserState> = Omit<VuexStore<S>, 'getters' | 'commit' | 'dispatch'> & {
    commit<K extends keyof Mutations, P extends Parameters<Mutations[K]>[1]>(key: K, payload: P, options?: CommitOptions): ReturnType<Mutations[K]>
} & {
    dispatch<K extends keyof Actions>(key: K, payload: Parameters<Actions[K]>[1], options?: DispatchOptions): ReturnType<Actions[K]>
}

export const store: Module<UserState, RootState> = {
    state: state,
    mutations: userMutations,
    actions: userActions
}