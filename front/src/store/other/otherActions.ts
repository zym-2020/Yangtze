import { ActionTree, ActionContext } from 'vuex'
import { RootState } from '@/store'
import { Mutations } from './otherMutations'
import { OtherState } from './otherState'

type AugmentedActionContext = {
    ['commit']<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<OtherState, RootState>, 'commit'>

export interface Actions {}

export const otherActions: ActionTree<OtherState, RootState> & Actions = {}