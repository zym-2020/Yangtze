export interface Resource {
    name: string
    id: number
    type: string
    classify?: string
    show?: boolean
    tableName?: string
    vectorType?: string
}

export interface ResourceState {
    underlying: Resource[]
    analyse: Resource[]
}

export const state: ResourceState = {
    underlying: [],
    analyse: []
}