<template>
  <div class="main">
    <div ref="container" class="map"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";

export default defineComponent({
  setup() {
    const container = ref<HTMLElement>();
    const tdtVec: AnySourceData = {
      type: "raster",
      tiles: [
        // "http://t0.tianditu.gov.cn/DataServer?T=img_w&x={x}&y={y}&l={z}&tk=35a94ab5985969d0b93229c30db6abd6",
        "http://t0.tianditu.com/vec_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
      ],
      tileSize: 256,
    };
    const tdtCva: AnySourceData = {
      type: "vector",
      tiles: [
        "http://localhost:8080/Yangtze/vector/data/{x}/{y}/{z}"
        // "http://t0.tianditu.gov.cn/DataServer?T=cia_w&x={x}&y={y}&l={z}&tk=35a94ab5985969d0b93229c30db6abd6",
        // "http://t0.tianditu.com/cva_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
      ]
    };

    const initMap = () => {
      const map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        style: {
          version: 8,
          
          sources: {
            tdtVec: tdtVec,
            tdtCva: tdtCva,
          },
          layers: [
            {
              id: "tdtVec",
              type: "raster",
              source: "tdtVec",
            },
            {
              id: "tdtCva",
              type: "line",
              source: "tdtCva",
              "source-layer": 'test_shape',
              
            },

          ],
        },
        
        center: [114.280541,30.591006],
        zoom: 10,

      });
    };

    onMounted(() => {
      initMap();
    });

    return {
      container,
      tdtVec,
      tdtCva,
      initMap,
    };
  },
});
</script>


<style lang="scss" scoped>
.map {
  height: 500px;

  
}
</style>
