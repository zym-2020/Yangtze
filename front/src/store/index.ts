import { createStore } from 'vuex'
import { UserState } from './user/userState'
import { UserStore, store as user } from './user'
import { PermissionState } from './permission/permissionState'
import { PermissionStore, store as permission } from './permission'
import { ViewState } from './views/viewState'
import { ViewStore, store as views } from './views'
import { ResourceStore, store as resource } from './resourse'
import { ResourceState } from './resourse/resourceState'
import { OtherStore, store as other } from './other'
import { OtherState } from './other/otherState'


export interface RootState {
  user: UserState,
  permission: PermissionState,
  views: ViewState
  resource: ResourceState,
  other: OtherState
}

export type Store = UserStore<Pick<RootState, 'user'>> & PermissionStore<Pick<RootState, 'permission'>> & ViewStore<Pick<RootState, 'views'>> & ResourceStore<Pick<RootState, 'resource'>> & OtherStore<Pick<RootState, 'other'>>

export const store = createStore({
  modules: {
    user,
    permission,
    views,
    resource,
    other
  }
})

export function useStore(): Store {
  return store as Store
}
