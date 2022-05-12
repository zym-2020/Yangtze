export interface UserState {

    name: string
    email: string
    avatar: string
    roles: string[]
    id: string
}

export const state: UserState = {

    name: "",
    email: "",
    avatar: "",
    roles: [],
    id: ''
}