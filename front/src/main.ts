import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import { store } from './store'
import ElementPlus from 'element-plus'
import * as Icons from '@element-plus/icons'
import 'element-plus/dist/index.css'
import '@/permission'
import '@/assets/iconfont/iconfont.css'

const app = createApp(App)
Object.keys(Icons).forEach(key => {
    app.component(key, Icons[key as keyof typeof Icons])
})
app.use(ElementPlus)
app.use(store).use(router).mount('#app')

