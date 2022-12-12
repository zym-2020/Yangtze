<template>
  <div class="main">
    <div ref="container" class="map"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import { prefix } from "@/prefix";
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
        prefix + "vector/cwz1/{x}/{y}/{z}",
        // "http://t0.tianditu.gov.cn/DataServer?T=cia_w&x={x}&y={y}&l={z}&tk=35a94ab5985969d0b93229c30db6abd6",
        // "http://t0.tianditu.com/cva_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
      ],
    };
    // const test: AnySourceData = {
    //   type: "raster-dem",
    //   url: "mapbox://mapbox.mapbox-terrain-dem-v1",
    //   tileSize: 512,
    // };

    const initMap = () => {
      const map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        // style: 'mapbox://styles/mapbox-map-design/ckhqrf2tz0dt119ny6azh975y',
        style: {
          version: 8,

          sources: {
            tdtVec: tdtVec,
            tdtCva: tdtCva,
            // test: test
          },
          layers: [
            {
              id: "tdtVec",
              type: "raster",
              source: "tdtVec",
            },
            {
              id: "tdtCva",
              type: "circle",
              source: "tdtCva",
              "source-layer": "cwz1",
            },
            // {
            //   id: 'test',
            //   type: 'hillshade',
            //   source: 'test'
            // }
          ],
        },
        // style: "http://172.21.212.63:8991/mapServer/626a592bc27f00a2b6b029f1",
        center: [121.193496, 31.791046],
        // center: [-114.34411, 32.6141],
        zoom: 8,
        // accessToken: "pk.eyJ1Ijoid3lqcSIsImEiOiJjbDBnZDdwajUxMXRzM2htdWxubDh1MzJrIn0.2e2_rdU2nOUvtwltBIZtZg"
      });
      map.on("load", () => {
        map.addControl(new mapBoxGl.FullscreenControl(), "top-right");
      });
      // map.on('load', () => {
      //   map.addLayer({
      //     id: 'tdtVec',
      //     type: "raster",
      //     source: tdtVec
      //   })
      //   map.addControl(new mapBoxGl.FullscreenControl({container: document.querySelector('body')}));
      // })
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
  height: 800px;
  /deep/ .mapboxgl-ctrl-logo {
    display: none !important;
  }
  // /deep/ .mapboxgl-ctrl-attrib-button{display: none !important}
}
</style>
