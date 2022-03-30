export interface UserState {
    token: string
    name: string
    email: string
    avatar: string
    roles: string[]
}

export const state: UserState = {
    token: "",
    name: "",
    email: "",
    avatar: "",
    roles: []
}