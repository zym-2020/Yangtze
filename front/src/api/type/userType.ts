
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

export interface MergeFileJsonData {
    MD5: string,
    type: string,
    name: string,
    total: number,
    level: number,
    parentId: string,
    meta: string
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

// export interface AddShareFileJsonData {
//     meta: {
//         provider: string
//         time: string
//         range: string
//         detail: string
//     }
//     fileInfo: {
//         name: string
//         description: string
//         originAddress: string
//         visualSource: string
//         visualType: string
//         structuredSource: string
//         tags: string[]
//     }
// }