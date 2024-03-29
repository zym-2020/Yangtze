import { MutationTree } from 'vuex'
import { UserState } from './userState'

export type Mutations<S = UserState> = {

    ["SET_NAME"](state: S, name: string): void,
    ["SET_AVATAR"](state: S, avatar: string): void,
    ["SET_ROLES"](state: S, role: string): void,
    ["SET_EMAIL"](state: S, email: string): void
    ["SET_ID"](state: S, id: string): void
}

export const userMutations: MutationTree<UserState> & Mutations = {

    ["SET_NAME"](state: UserState, name: string) {
        state.name = name
    },
    ["SET_AVATAR"](state: UserState, avatar: string) {
        state.avatar = avatar
    },
    ["SET_ROLES"](state: UserState, role: string) {
        state.role = role
    },
    ["SET_EMAIL"](state: UserState, email: string) {
        state.email = email
    },
    ["SET_ID"](state: UserState, id: string) {
        state.id = id
    }
}