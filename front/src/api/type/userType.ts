
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

export interface SectionJsonData {
    DEMId: string,
    lat1: number,
    lon1: number,
    lat2: number,
    lon2: number,
    sectionName: string,
    projectName: string
}

export interface SectionContrastJsonData {
    lat1: number,
    lon1: number,
    lat2: number,
    lon2: number,
    sectionName: string,
    projectName: string
}

export interface AddFileJsonData {
    id?: string
    name: string
    address: string
    fileName: string
    level: number,
    parentId: string,
    meta: string
    folder: boolean
}

export interface RenameJsonData {
    id: string,
    name: string
}

export interface SetUserInfoWithoutAvatarJsonData {
    name: string
    contactEmail: string
    occupation: string
    department: string
}

export interface FuzzyQueryClassifyJsonData {
    size: number
    page: number
    property: string
    keyWord: string
    flag: boolean
    tags?: string[]
}

export interface DeleteShareFileByIdJsonDaya {
    size: number
    page: number
    property: string
    keyWord: string
    id: string
}

export interface UpdateStatusByIdJsonData {
    id: string
    status: number
}

export interface GetNoUploadJsonData {
    MD5: string
    total: number
    meta: {
        name: string
        total: number
        level: number
        parentId: string
        meta: string
    }
}