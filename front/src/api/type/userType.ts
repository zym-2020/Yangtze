
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

export interface layerData {
    id: number,
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
export interface ProjectJsonBean {
    layerDataList: layerData[]
    analysisResultList: AnalysisResult[]
}