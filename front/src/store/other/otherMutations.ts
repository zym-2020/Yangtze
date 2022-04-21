import { MutationTree } from 'vuex'
import { OtherState } from './otherState'

export type Mutations<S = OtherState> = {
    SET_DATA_SELECT(state: S, dataSelect: {id: string, name: string}): void
}

export const otherMutations: MutationTree<OtherState> & Mutations = {
    SET_DATA_SELECT(state: OtherState, dataSelect: {id: string, name: string}) {
        state.dataSelect = JSON.parse(JSON.stringify(dataSelect))
    }
}