import { Store as VuexStore, CommitOptions, DispatchOptions, Module } from 'vuex'
import { RootState } from ".."
import { state, OtherState } from './otherState'
import { otherMutations, Mutations } from './otherMutations'
import { otherActions, Actions } from './otherActions'

export type OtherStore<S = OtherState> = Omit<VuexStore<S>, 'getters' | 'commit' | 'dispatch'> & {
    commit<K extends keyof Mutations, P extends Parameters<Mutations[K]>[1]>(key: K, payload: P, options?: CommitOptions): ReturnType<Mutations[K]>
} & {
    dispatch<K extends keyof Actions>(key: K, payload: Parameters<Actions[K]>[1], options?: DispatchOptions): ReturnType<Actions[K]>
}

export const store: Module<OtherState, RootState> = {
    state: state,
    mutations: otherMutations,
    actions: otherActions
}