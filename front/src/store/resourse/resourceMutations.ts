import { MutationTree } from 'vuex'
import { ResourceState, Resource, Analyse } from './resourceState'

export type Mutations<S = ResourceState> = {
    SET_BASE_DATA(state: S, resource: Resource[]): void
    ADD_BASE_DATA(state: S, resource: Resource): void
    SET_ANALYSE(state: S, analyse: Analyse): void
    ADD_ANALYSE(state: S, AnalyseResource: { classify: string; resource: Resource }): void
    INIT(state: S): void
    SET_LAYER_SORT(state: S, jsonParam: { type: string, layers: string[]}): void
    SET_TEMP_LAYERS(state: S, layers: string[]): void
    ADD_TEMP_LAYER(state: S, layer: string): void
    DEL_TEPM_LAYER(state: S, index: number): void
    SET_SELECTED_LAYER(state: S, jsonParam: {id: string; flag: boolean}): void
}

export const resourceMutations: MutationTree<ResourceState> & Mutations = {
    SET_BASE_DATA(state: ResourceState, resource: Resource[]) {
        state.layerDataList = JSON.parse(JSON.stringify(resource))
    },
    ADD_BASE_DATA(state: ResourceState, resource: Resource) {
        state.layerDataList.push(resource)
    },
    SET_ANALYSE(state: ResourceState, analyse: Analyse) {
        state.analyse = JSON.parse(JSON.stringify(analyse))
    },
    ADD_ANALYSE(state: ResourceState, AnalyseResource: { classify: string; resource: Resource }) {
        switch (AnalyseResource.classify) {
            case "断面形态":
                state.analyse.section.classifyCount += 1
                state.analyse.section.analysisResultList.push(AnalyseResource.resource)
                break;
            case "断面比较":
                state.analyse.sectionContrast.classifyCount += 1
                state.analyse.sectionContrast.analysisResultList.push(AnalyseResource.resource)
                break;
            case "断面面积冲淤":
                state.analyse.area.classifyCount += 1
                state.analyse.area.analysisResultList.push(AnalyseResource.resource)
                break;
            case "边界分析":
                state.analyse.boundary.classifyCount += 1
                state.analyse.boundary.analysisResultList.push(AnalyseResource.resource)
                break;
            case "汊道断面比较":
                state.analyse.branch.classifyCount += 1
                state.analyse.branch.analysisResultList.push(AnalyseResource.resource)
                break;
            case "冲淤等深线":
                state.analyse.deep.classifyCount += 1
                state.analyse.deep.analysisResultList.push(AnalyseResource.resource)
                break;
            case "等深线比较":
                state.analyse.deepContrast.classifyCount += 1
                state.analyse.deepContrast.analysisResultList.push(AnalyseResource.resource)
                break;
            case "特定高程冲淤":
                state.analyse.elev.classifyCount += 1
                state.analyse.elev.analysisResultList.push(AnalyseResource.resource)
                break;
            case "深泓线比较":
                state.analyse.line.classifyCount += 1
                state.analyse.line.analysisResultList.push(AnalyseResource.resource)
                break;
            case "河床坡度提取":
                state.analyse.slope.classifyCount += 1
                state.analyse.slope.analysisResultList.push(AnalyseResource.resource)
                break;
            case "河道容积计算":
                state.analyse.volume.classifyCount += 1
                state.analyse.volume.analysisResultList.push(AnalyseResource.resource)
                break;
            case "任意区域冲淤":
                state.analyse.anyArea.classifyCount += 1
                state.analyse.anyArea.analysisResultList.push(AnalyseResource.resource)
                break;
        }
    },
    INIT(state: ResourceState) {
        state.layerDataList = JSON.parse(JSON.stringify([]))
        state.analyse = JSON.parse(JSON.stringify({
            section: { classifyCount: 0, classify: '断面形态', analysisResultList: [] },
            sectionContrast: { classifyCount: 0, classify: '断面比较', analysisResultList: [] },
            area: { classifyCount: 0, classify: '断面面积冲淤', analysisResultList: [] },
            boundary: { classifyCount: 0, classify: '边界分析', analysisResultList: [] },
            branch: { classifyCount: 0, classify: '汊道断面比较', analysisResultList: [] },
            deep: { classifyCount: 0, classify: '冲淤等深线', analysisResultList: [] },
            deepContrast: { classifyCount: 0, classify: '等深线比较', analysisResultList: [] },
            elev: { classifyCount: 0, classify: '特定高程冲淤', analysisResultList: [] },
            line: { classifyCount: 0, classify: '深泓线比较', analysisResultList: [] },
            slope: { classifyCount: 0, classify: '河床坡度提取', analysisResultList: [] },
            volume: { classifyCount: 0, classify: '河道容积计算', analysisResultList: [] },
            anyArea: { classifyCount: 0, classify: '任意区域冲淤', analysisResultList: [] }
        }))
        state.layerSort.type = ""
        state.layerSort.layers = []
        state.tempLayer = []
        state.selectedLayer.id = ""
        state.selectedLayer.flag = false
    },
    SET_LAYER_SORT(state: ResourceState, jsonParam: { type: string, layers: string[]}) {
        state.layerSort.type = jsonParam.type
        state.layerSort.layers = jsonParam.layers
    },
    SET_TEMP_LAYERS(state: ResourceState, layers: string[]) {
        state.tempLayer = layers
    },
    ADD_TEMP_LAYER(state: ResourceState, layer: string) {
        state.tempLayer.unshift(layer)
    },
    DEL_TEPM_LAYER(state: ResourceState, index: number) {
        state.tempLayer.splice(index, 1)
    },
    SET_SELECTED_LAYER(state: ResourceState, jsonParam: {id: string; flag: boolean}) {
        state.selectedLayer.id = jsonParam.id
        state.selectedLayer.flag = jsonParam.flag
    }
}