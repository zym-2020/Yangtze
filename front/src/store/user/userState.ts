export interface UserState {
    token: string
    name: string
    email: string
    avatar: string
    roles: string[]
    id: string
}

export const state: UserState = {
    token: "",
    name: "",
    email: "",
    avatar: "",
    roles: [],
    id: ''
}