import { createWebHistory, createRouter } from "vue-router";
import HomePage from '@/views/HomePage.vue';
import MapForAll from '@/views/MapForAll.vue';
import TheMap from '@/views/TheMap.vue'

const routes = [
    {
        path: '/', 
        name: 'HomePage', 
        component: HomePage,
    },
    {
        path: '/amap', 
        name: 'MapForAll', 
        component: MapForAll
    }, 
    {
        path: '/themap', 
        name: 'TheMap', 
        component: TheMap
    }
];


const router = createRouter({
    history: createWebHistory(),
    routes,
});
  
export default router;
