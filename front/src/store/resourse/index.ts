import { Store as VuexStore, CommitOptions, DispatchOptions, Module } from 'vuex'
import { RootState } from ".."
import { ResourceState, state } from './resourceState'
import { Mutations, resourceMutations } from './resourceMutations'
import { Actions, resourceActions } from './resourceActions'

export type ResourceStore<S = ResourceState> = Omit<VuexStore<S>, 'getters' | 'commit' | 'dispatch'> & {
    commit<K extends keyof Mutations, P extends Parameters<Mutations[K]>[1]>(key: K, payload: P, options?: CommitOptions): ReturnType<Mutations[K]>
} & {
    dispatch<K extends keyof Actions>(key: K, payload: Parameters<Actions[K]>[1], options?: DispatchOptions): ReturnType<Actions[K]>
}

export const store: Module<ResourceState, RootState> = {
    state: state,
    mutations: resourceMutations,
    actions: resourceActions
}