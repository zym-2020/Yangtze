import { MutationTree } from 'vuex'
import { UserState } from './userState'

export type Mutations<S = UserState> = {
    ["SET_TOKEN"](state: S, token: string): void
    ["SET_NAME"](state: S, name: string): void,
    ["SET_AVATAR"](state: S, avatar: string): void,
    ["SET_ROLES"](state: S, roles: string[]): void,
    ["SET_EMAIL"](state: S, email: string): void
}

export const userMutations: MutationTree<UserState> & Mutations = {
    ["SET_TOKEN"](state: UserState, token: string) {
        state.token = token
    },
    ["SET_NAME"](state: UserState, name: string) {
        state.name = name
    },
    ["SET_AVATAR"](state: UserState, avatar: string) {
        state.avatar = avatar
    },
    ["SET_ROLES"](state: UserState, roles: string[]) {
        state.roles = roles
    },
    ["SET_EMAIL"](state: UserState, email: string) {
        state.email = email
    }
}