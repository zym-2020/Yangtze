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
import { computed, onMounted } from "@vue/runtime-core";
import router from "@/router";

const coordinates = computed(() => {
  if ((router.currentRoute.value.params.fileInfo as any).location.length > 0) {
    const result: number[][] = [];
    for (
      let i = 0;
      i + 1 <
      (router.currentRoute.value.params.fileInfo as any).location.length;
      i = i + 2
    ) {
      result.push([
        parseFloat(
          (router.currentRoute.value.params.fileInfo as any).location[i]
        ),
        parseFloat(
          (router.currentRoute.value.params.fileInfo as any).location[i + 1]
        ),
      ]);
    }
    return result;
  } else {
    return [
      [120.60871093751484, 32.09452444899182],
      [122.08637207034224, 32.09452444899182],
      [122.08637207034224, 31.152025646717746],
      [120.60871093751484, 31.152025646717746],
      [120.60871093751484, 32.09452444899182],
    ];
  }
});

onMounted(async () => {
  console.log();
  mapboxgl.accessToken =
    "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ";
  const map = new mapboxgl.Map({
    container: "map2",
    style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
    center: [121.18, 31.723],
    zoom: 7,
  });
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
              coordinates: [coordinates.value],
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
  height: 100%;
  width: 100%;
  div#map2 {
    width: 100%;
    height: 100%;
  }
}
</style>