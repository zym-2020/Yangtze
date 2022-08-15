import { ActionTree, ActionContext } from 'vuex'
import { Mutations } from './resourceMutations'
import { ResourceState, Resource, Analyse } from './resourceState'
import { RootState } from '@/store'
// import { setResult } from '@/api/request'
import { notice } from '@/utils/notice'


type AugmentedActionContext = {
    ['commit']<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<ResourceState, RootState>, 'commit'>

export interface Actions {
    setResource({ commit }: AugmentedActionContext, jsonParam: { projectJsonBean: { layerDataList: Resource[], analyse: Analyse }, id: string }): Promise<void>
}

export const resourceActions: ActionTree<ResourceState, RootState> & Actions = {
    async setResource({ commit }: AugmentedActionContext, jsonParam: { projectJsonBean: { layerDataList: Resource[], analyse: Analyse }, id: string }) {
        // const data = await setResult(jsonParam.projectJsonBean, jsonParam.id)
        // // notice("success", "成功", "数据加载成功")
        // if (data != null) {
        //     if ((data as any).code === 0) {
        //         commit("SET_BASE_DATA", jsonParam.projectJsonBean.layerDataList)
        //         commit("SET_ANALYSE", jsonParam.projectJsonBean.analyse)
        //     }
        //     return (data as any).code
        // } else {
        //     return null
        // }
    }

}