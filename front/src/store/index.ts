import { createStore } from 'vuex'
import { UserState } from './user/userState'
import { UserStore, store as user } from './user'
import { PermissionState } from './permission/permissionState'
import { PermissionStore, store as permission } from './permission'
import { ViewState } from './views/viewState'
import { ViewStore, store as views } from './views'


export interface RootState {
  user: UserState,
  permission: PermissionState,
  views: ViewState
}

export type Store = UserStore<Pick<RootState, 'user'>> & PermissionStore<Pick<RootState, 'permission'>> & ViewStore<Pick<RootState, 'views'>>

export const store = createStore({
  modules: {
    user,
    permission,
    views
  }
})

export function useStore(): Store {
  return store as Store
}
