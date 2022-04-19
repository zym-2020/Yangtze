
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




export interface NewShapeJsonData {
    geoJson: [],
    fileName: string,
    type: string
    source: string
    projectName: string
    category: string
    meta: string
}