import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { store } from './store'
import ElementPlus from 'element-plus'
import * as Icons from '@element-plus/icons'
import 'element-plus/dist/index.css'
import '@/permission'
import '@/assets/iconfont/iconfont.css'
import '@/assets/iconfont/iconfont.js'
import 'mapbox-gl/dist/mapbox-gl.css'
import '@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css'
import { drag, analyseDrag } from '@/directives/drag'

const app = createApp(App)
Object.keys(Icons).forEach(key => {
    app.component(key, Icons[key as keyof typeof Icons])
})
app.directive("analyseDrag", analyseDrag)
app.directive("drag", drag)


app.use(ElementPlus)
app.use(store).use(router).mount('#app')

