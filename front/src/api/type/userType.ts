
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
    creator: string,
    creatorName: string
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
    id: string
    DEMId: string,
    lat1: number,
    lon1: number,
    lat2: number,
    lon2: number,
    projectId: string
}

export interface SectionContrastJsonData {
    id: string
    lat1: number,
    lon1: number,
    lat2: number,
    lon2: number,
    demIds: string[]
    projectId: string
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
        size?: string
    }
}

export interface AddRecordJsonData {
    id?: string
    fileName: string
    state: number
}
export interface AddMessageJsonData {
    id: string
    dataName: string
    messageType: string
    dataUploadTime: string
    dataExamineTime: string
    dataCache: string,
    messageRequest: string,
    reply: boolean,
    fileId: string,
    messageSender: string,
    messageReceiver: string,
    messageResponse: string,
    replyUser: boolean
    
}

export interface UnPackJsonData {
    id: string
    parentId: string
    level: number
}

export interface UpdateParentIdAndLevelJsonData {
    parentId: string
    levelFrom: number
    levelTo: number
    files: string[]
    folders: string[]
}

export interface CompressFileJsonData {
    compressName: string
    parentId: string
    level: number
    files: string[]
    folders: string[]
}

export interface DeleteFilesOrFolders {
    files: string[]
    folders: string[]
}

export interface GetProjectsJsonData {
    size: number
    page: number
    keyWord: string
}


export interface Layer {
    id: string
    type: string
    name?: string
    tableName?: string
    vectorType?: string
    geoJson?: {
        coordinates: any[],
        type: string
    }
    sections?: {
        id: string;
        sectionId: string
        state: number
    }[]
    demSlopeId?: string
}

export interface GetFlushIdJsonData {
    projectId: string
    benchmark: string
    reference: string
    name: string
}

export interface ComputeContourJsonData {
    projectId: string
    demId: string
    interval: string
    shpName: string
    srid: string
}