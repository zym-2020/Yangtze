import { Store as VuexStore, CommitOptions, DispatchOptions, Module } from 'vuex'
import { RootState } from ".."
import { state, ViewState } from './viewState'
import { Mutations, viewMutations } from './viewMutations'
import { Actions, viewActions } from './viewActions'

export type ViewStore<S = ViewState> = Omit<VuexStore<S>, 'getters' | 'commit' | 'dispatch'> & {
    commit<K extends keyof Mutations, P extends Parameters<Mutations[K]>[1]>(key: K, payload: P, options?: CommitOptions): ReturnType<Mutations[K]>
} & {
    dispatch<K extends keyof Actions>(key: K, payload: Parameters<Actions[K]>[1], options?: DispatchOptions): ReturnType<Actions[K]>
}

export const store: Module<ViewState, RootState> = {
    state: state,
    mutations: viewMutations,
    actions: viewActions
}