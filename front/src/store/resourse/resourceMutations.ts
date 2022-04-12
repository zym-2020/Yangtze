import { MutationTree } from 'vuex'
import { ResourceState, Resource } from './resourceState'

export type Mutations<S = ResourceState> = {
    SET_BASE_DATA(state: S, resource: Resource[]):void
    ADD_BASE_DATA(state: S, resource: Resource): void
    SET_ANALYSE(state: S, resource: Resource[]): void
    ADD_ANALYSE(state: S, resource: Resource): void
}

export const resourceMutations: MutationTree<ResourceState> & Mutations = {
    SET_BASE_DATA(state: ResourceState, resource: Resource[]) {
        state.underlying = JSON.parse(JSON.stringify(resource))
    },
    ADD_BASE_DATA(state: ResourceState, resource: Resource) {
        state.underlying.push(resource)
    },
    SET_ANALYSE(state: ResourceState, resource: Resource[]) {
        state.analyse = JSON.parse(JSON.stringify(resource))
    },
    ADD_ANALYSE(state: ResourceState, resource: Resource) {
        state.analyse.push(resource)
    }
}