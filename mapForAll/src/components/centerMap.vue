<template>
  <div class="scene-map-wrapper">
    <div id="map"></div>
  </div>
</template>

<script lang="ts">
export default {
  name: 'centerMap'
}
</script>

<script setup lang='ts'>
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';
import { onMounted } from '@vue/runtime-core';
import { frontData } from '../frontData';
import { ref } from 'vue';

interface Props {
    mapId: string, 
}

const props = defineProps<Props>();

const mapIndex = ref(props.mapId);

onMounted(() => {
    // mapbox key
    mapboxgl.accessToken = 'pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg';

    const mapConfig = frontData['maps'];
    // init mapbox...
    const map = new mapboxgl.Map(mapConfig[+mapIndex.value]['initStyle'] as mapboxgl.MapboxOptions);

    if(mapConfig[+mapIndex.value]['dynamicFunc'] !== undefined) {
      map.on('load', ()=>{
        (mapConfig[+mapIndex.value]['dynamicFunc'] as ((map: mapboxgl.Map)=>void))(map);
      });
    }
});

</script>

<style lang='scss'>

div.scene-map-wrapper {
    cursor: pointer;
    background-color: transparent;
    width: 52%;
    height: 63%;

    div#map {
        width: 100%;
        height: 100%;
        background: rgba(255, 255, 255, 0.2);
    }
}

</style>