
export interface LoginJsonData {
    email: string,
    password: string
}

export interface RegisterJsonData {
    email: string,
    name: string,
    password: string,
    roles: string[]
}

export interface ProjectJsonData {
    projectName: string,
    description: string,
    result: string
}

export interface LayerData {
    id: number,
    name: string
    type: string
    show: boolean
    tableName?: string
    vectorType?: string
}
export interface AnalysisResult {
    id: number
    name: string
    classify: string
    type: string
    show: boolean
    tableName?: string
    vectorType?: string
}
export interface ProjectJsonBean {
    layerDataList: LayerData[]
    analysisResultList: AnalysisResult[]
}