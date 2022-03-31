export interface TagView {
    path: string
    title: string
}

export interface ViewState {
    views: TagView[]
}

export const state: ViewState = {
    views: []
} 