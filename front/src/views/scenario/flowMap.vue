<template>
    <div id="map"></div>
    <!-- <div id="stats"></div> -->
</template>

<script lang="ts">
export default {
  name: "FlowMap",
};
</script>

<script setup lang="ts">
import { onMounted } from "vue";
import "mapbox-gl/dist/mapbox-gl.css";
// import axios, { AxiosResponse } from 'axios';
import { GetMap } from '../../utils/customLayer/cusLayer';
// import { FlowLayerNew, FlowLayer } from './customLayer/flowLayer';
// import { ShaderURL, loadShaderSrc, FLowParams, FlowLimit } from './renderUtils/flowFieldNew'
import { FlowFieldManager } from '../../utils/customLayer/flowfield';
import { FlowLayer } from '../../utils/customLayer/flowLayer';
// const flowParams: FLowParams = {
//   lineNum: 65536, 
//   segmentNum: 16, 
//   fullLife: 65536 * 3, 
//   progressRate: 0.0,
//   speedFactor: 2.0,
//   dropRate: 0.003,
//   dropRateBump: 0.001,
//   fillWidth: 1.0,
//   aaWidth: 1.0,
//   // content: "particle pool"
//   // content: "flow field"
//   content: "none"
// }

// const shaderURL: ShaderURL = {
//     "update": {
//         "vert": "http://localhost:8080/shaders/update.vert", 
//         "frag": "http://localhost:8080/shaders/update.frag", 
//         "tfVars": ['newPosition', 'aliveTime']
//     }, 
//     "draw": {
//         "vert": "http://localhost:8080/shaders/ribbonParticle.vert", 
//         "frag": "http://localhost:8080/shaders/ribbonParticle.frag", 
//     }, 
//     "poolDebug": {
//         "vert": "http://localhost:8080/shaders/showPool.vert", 
//         "frag": "http://localhost:8080/shaders/showPool.frag", 
//     }, 
//     "textureDebug": {
//         "vert": "http://localhost:8080/shaders/texture.vert", 
//         "frag": "http://localhost:8080/shaders/texture.frag", 
//     }
// }

// const bbox = [
//   120.062456,32.050219, 
//   121.896437,31.280738
// ];

onMounted(async () => {
  console.log("Mounted...");
  const map = GetMap(
    'pk.eyJ1IjoieWNzb2t1IiwiYSI6ImNsMWVsdnpxNDBzcDgzYnA0MDJrcW1hOXQifQ.-5KUoc4jAJbAcBEWgbMGSA', 
    {
      container: 'map', 
      style: 'mapbox://styles/johnnyt/clblx2t3v000a14proaq4e9qv', // style URL
      center: [ 120.980697,31.684162 ], // starting position [lng, lat]
      zoom: 9,
    }
  );

  // const flowLimit = await axios.get('http://localhost:8080/json/flow_field_description.json').
  // then((res: AxiosResponse<FlowLimit>) => {
  //   return res.data;
  // });
  // const shaderSrc = await loadShaderSrc(shaderURL);
  // const flowLayerNew = new FlowLayerNew('flow', '2d', flowLimit, flowParams, shaderSrc);


  let flowFieldManager = new FlowFieldManager('./flow/json/flow_field_description.json');
  const flowLayer = new FlowLayer('flow', '2d', flowFieldManager);

  map.on('load', () => {
    map.addLayer(flowLayer);
    // map.on('idle', () => {
    // })
  });
})

</script>

<style>
body {
  margin: 0;
}
#map {
  margin: 0;
  width: 100vw;
  height: 93vh;
}
</style>