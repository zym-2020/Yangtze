import { Mutations } from "./viewMutations";
import { ViewState } from "./viewState";
import { ActionTree, ActionContext } from 'vuex'
import { RootState } from '@/store'
import { TagView } from './viewState'


type AugmentedActionContext = {
    commit<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<ViewState, RootState>, 'commit'>

export type Actions = {
    addView({ commit }: AugmentedActionContext, view: TagView): void
    delView({ commit }: AugmentedActionContext, path: string): void
    delOtherViews({ commit }: AugmentedActionContext, path: string): void
    delAllViews({ commit }: AugmentedActionContext): void
}

export const viewActions: ActionTree<ViewState, RootState> & Actions = {
    addView({ commit }: AugmentedActionContext, view: TagView) {
        commit("ADD_VIEW", view)
    },
    delView({ commit }: AugmentedActionContext, path: string) {
        commit("DEL_VIEW", path)
    },
    delOtherViews({ commit }: AugmentedActionContext, path: string) {
        commit("DEL_OTHER_VIEWS", path)
    },
    delAllViews({ commit }: AugmentedActionContext) {
        commit("DEL_ALL_VIEWS", undefined)
    }
}