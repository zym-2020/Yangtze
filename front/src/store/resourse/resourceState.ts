export interface Resource {
    name: string
    address: string
    id?: number
    type?: string
    classify?: string
    hasTiles?: boolean
}

export interface ResourceState {
    underlying: Resource[]
    analyse: Resource[]
}

export const state: ResourceState = {
    underlying: [],
    analyse: []
}