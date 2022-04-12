import { createStore } from 'vuex'
import { UserState } from './user/userState'
import { UserStore, store as user } from './user'
import { PermissionState } from './permission/permissionState'
import { PermissionStore, store as permission } from './permission'
import { ViewState } from './views/viewState'
import { ViewStore, store as views } from './views'
import { ResourceStore, store as resource } from './resourse'
import { ResourceState } from './resourse/resourceState'


export interface RootState {
  user: UserState,
  permission: PermissionState,
  views: ViewState
  resource: ResourceState
}

export type Store = UserStore<Pick<RootState, 'user'>> & PermissionStore<Pick<RootState, 'permission'>> & ViewStore<Pick<RootState, 'views'>> & ResourceStore<Pick<RootState, 'resource'>>

export const store = createStore({
  modules: {
    user,
    permission,
    views,
    resource
  }
})

export function useStore(): Store {
  return store as Store
}
