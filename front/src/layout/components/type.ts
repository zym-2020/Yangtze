export interface LayerData {
    id: number
    name: string
    type: string
    data: string
    show: boolean
}

export interface AnalysisResult {
    name: string
    classify: string
    address: string
    type: string
    show: boolean
}

export interface ProjectResult {
    layerDataList: Array<LayerData>
    analysisResultList: Array<AnalysisResult>
}