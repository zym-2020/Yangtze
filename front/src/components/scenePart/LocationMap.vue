<template>
  <div class="scene-map-wrapper2">
    <div id="map2"></div>
  </div>
</template>

<script lang="ts">
export default {
  name: "LocationMap",
};
</script>

<script setup lang='ts'>
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { onMounted } from "@vue/runtime-core";
import { frontData } from "../../frontData";
import { ref, watch } from "vue";
import {
  QueryCoordinatesByName,
} from "@/api/request";
import router from "@/router";
const mapName = ref("");
const coordinates = ref<any[]>();

watch(
  () => router.currentRoute.value.params.id,
  async (newValue, oldValue) => {
    //避免跳转到其他页面时去调用后台的接口
if(router.currentRoute.value.fullPath==("/data/"+router.currentRoute.value.params.id)){
    const data = await QueryCoordinatesByName(
      (router.currentRoute.value.params.fileInfo as any).name
    );
    if (data != undefined && data != null && data.data.length != 0) {
      if ((data as any).code === 0) {
        coordinates.value = [
          [
            data.data[0].coordinates[0] as number,
            data.data[0].coordinates[1] as number,
          ],
          [
            data.data[0].coordinates[2] as number,
            data.data[0].coordinates[3] as number,
          ],
          [
            data.data[0].coordinates[4] as number,
            data.data[0].coordinates[5] as number,
          ],
          [
            data.data[0].coordinates[6] as number,
            data.data[0].coordinates[7] as number,
          ],
          [
            data.data[0].coordinates[0] as number,
            data.data[0].coordinates[1] as number,
          ],
        ];
      } else {
        coordinates.value = [
          [120.60871093751484, 32.09452444899182],
          [122.08637207034224, 32.09452444899182],
          [122.08637207034224, 31.152025646717746],
          [120.60871093751484, 31.152025646717746],
          [120.60871093751484, 32.09452444899182],
        ];
      }
    }}
  },
  { immediate: true, deep: true }
);
onMounted(async () => {
  mapboxgl.accessToken =
    "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ";
  const map = new mapboxgl.Map({
    container: "map2",
    style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
    center: [121.18, 31.723],
    zoom: 7,
  });
  coordinates.value = [
    [120.60871093751484, 32.09452444899182],
    [122.08637207034224, 32.09452444899182],
    [122.08637207034224, 31.152025646717746],
    [120.60871093751484, 31.152025646717746],
    [120.60871093751484, 32.09452444899182],
  ];
  map.on("load", () => {
    map.addSource("someS id", {
      type: "geojson",
      data: {
        type: "FeatureCollection",
        features: [
          {
            type: "Feature",
            properties: {},
            geometry: {
              type: "Polygon",
              coordinates: [coordinates.value as any[]],
            },
          },
        ],
      },
    });

    map.addLayer({
      id: "someS id",
      source: "someS id",
      type: "fill",
      layout: {},
      paint: {
        "fill-color": "#6495ED",
        "fill-opacity": 0.5,
      },
    });
  });
});
</script>

<style lang='scss'>
div.scene-map-wrapper2 {
  cursor: pointer;
  background-color: transparent;
  width: 52%;
  height: 63%;

  div#map2 {
    position: absolute;
    top: 0;
    bottom: 0;

    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.2);
  }
}
</style>