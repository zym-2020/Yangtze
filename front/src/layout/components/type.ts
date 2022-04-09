export interface LayerData {
    name: string
    type: string
    data: string
}

export interface AnalysisResult {
    name: string
    classify: string
    address: string
}

export interface ProjectResult {
    layerDataList: Array<LayerData>
    analysisResultList: Array<AnalysisResult>
}