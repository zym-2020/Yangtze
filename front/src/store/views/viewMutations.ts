import { ViewState, TagView } from "./viewState";
import { MutationTree } from "vuex";

export interface Mutations<S = ViewState> {
    ADD_VIEW(state: S, view: TagView): void
    DEL_VIEW(state: S, path: string): void
    DEL_OTHER_VIEWS(state: S, path: string): void
    DEL_ALL_VIEWS(state: S): void
}

export const viewMutations: MutationTree<ViewState> & Mutations = {
    ADD_VIEW(state: ViewState, view: TagView) {
        if(state.views.some(item => item.path === view.path)) return
        state.views.push(view)
    },
    DEL_VIEW(state: ViewState, path: string) {
        for(let i = 0;i < state.views.length;i++) {
            if(state.views[i].path === path) {
                state.views.splice(i, 1)
                return
            }
        }
    },
    DEL_OTHER_VIEWS(state: ViewState, path: string) {
        state.views.forEach((item, index) => {
            if(item.path === path) {
                state.views.splice(index, 1)
            }
        })
    },
    DEL_ALL_VIEWS(state: ViewState) {
        state.views = []
    }
}