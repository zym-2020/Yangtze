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
                show: item.show as boolean === undefined ? true : item.show as boolean,
                tableName: item.tableName,
                vectorType: item.vectorType
            })
        })

        jsonParam.analysisResultList.forEach(item => {
            jsonData.analysisResultList.push({
                id: item.id,
                name: item.name,
                classify: item.classify as string,
                type: item.type as string,
                show: item.show as boolean,
                tableName: item.tableName,
                vectorType: item.vectorType
            })
        })
        await setResult(jsonData, jsonParam.id)
        notice("success", "成功", "数据加载成功")
        commit("SET_BASE_DATA", jsonData.layerDataList)
        commit("SET_ANALYSE", jsonData.analysisResultList)
    }
}