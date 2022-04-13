export interface LayerData {
    id: number
    name: string
    type: string
    show: boolean
    tableName: string
    vectorType: string
}

export interface AnalysisResult {
    id: number
    name: string
    classify: string
    type: string
    show: boolean
    tableName: string
    vectorType: string
}

export interface ProjectResult {
    layerDataList: Array<LayerData>
    analysisResultList: Array<AnalysisResult>
}