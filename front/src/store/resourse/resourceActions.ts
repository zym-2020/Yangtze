import { ActionTree, ActionContext } from 'vuex'
import { Mutations } from './resourceMutations'
import { ResourceState, Resource } from './resourceState'
import { RootState } from '@/store'
import { setResult } from '@/api/request'
import { ProjectJsonBean } from '@/api/type/userType'
import { notice } from '@/utils/notice'


type AugmentedActionContext = {
    ['commit']<K extends keyof Mutations>(key: K, payload: Parameters<Mutations[K]>[1]): ReturnType<Mutations[K]>
} & Omit<ActionContext<ResourceState, RootState>, 'commit'>

export interface Actions {
    setResource({ commit }: AugmentedActionContext, jsonParam: {layerDataList: Resource[], analysisResultList: Resource[], id: number}): void
}

export const resourceActions: ActionTree<ResourceState, RootState> & Actions = {
    async setResource({ commit }: AugmentedActionContext, jsonParam: {layerDataList: Resource[], analysisResultList: Resource[], id: number}) {
        const jsonData: ProjectJsonBean = {
            layerDataList: [],
            analysisResultList: []
        }
        jsonParam.layerDataList.forEach(item => {
            jsonData.layerDataList.push({
                id: item.id as number,
                name: item.name,
                type: item.type as string,
                data: item.address as string,
                show: item.hasTiles as boolean
            })
        })

        jsonParam.analysisResultList.forEach(item => {
            jsonData.analysisResultList.push({
                name: item.name,
                classify: item.classify as string,
                address: item.address,
                type: item.type as string,
                show: item.hasTiles as boolean
            })
        })
        await setResult(jsonData, jsonParam.id)
        notice("success", "成功", "数据加载成功")
        commit("SET_BASE_DATA", jsonParam.layerDataList)
        commit("SET_ANALYSE", jsonParam.analysisResultList)
    }
}