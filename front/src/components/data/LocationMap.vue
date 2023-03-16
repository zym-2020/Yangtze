<template>
  <div class="scene-map-wrapper2">
    <div id="map2" v-show="coordinates.length > 0"></div>
    <img src="/png/nolocation.png" v-show="coordinates.length === 0" />
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, onMounted } from "vue";
import mapboxgl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import router from "@/router";
export default defineComponent({
  setup() {
    let map: mapboxgl.Map;

    const computeCenter = (location: string[]): [number, number] => {
      if (location.length > 0) {
        const center: [number, number] = [0, 0];
        for (let i = 0; i < location.length; i = i + 2) {
          center[0] += parseFloat(location[i]);
          center[1] += parseFloat(location[i + 1]);
        }
        center[0] = center[0] / (location.length / 2);
        center[1] = center[1] / (location.length / 2);
        return center;
      } else {
        return [121.18, 31.723];
      }
    };

    const coordinates = computed(() => {
      if (
        (router.currentRoute.value.params.fileInfo as any).location.length > 0
      ) {
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
        return [];
      }
    });

    const setData = () => {
      if (coordinates.value.length > 0) {
        map.setCenter(
          computeCenter(
            (router.currentRoute.value.params.fileInfo as any).location
          )
        );
        map.removeLayer("someS id");
        map.removeSource("someS id");
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
      }
    };

    onMounted(() => {
      if (coordinates.value.length > 0) {
        mapboxgl.accessToken =
          "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ";
        map = new mapboxgl.Map({
          container: "map2",
          // 这是osm的style
          // style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",

          // 下方是天地图的style
          style: {
            version: 8,
            sources: {
              tdtVec: {
                type: "raster",
                tiles: [
                  "http://t0.tianditu.com/vec_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
                ],
                tileSize: 256,
              },
              txt: {
                type: "raster",
                tiles: [
                  "http://t0.tianditu.com/cva_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
                ],
                tileSize: 256,
              },
            },
            layers: [
              {
                id: "tdtVec",
                type: "raster",
                source: "tdtVec",
              },
              {
                id: "txt",
                type: "raster",
                source: "txt",
              },
            ],
          },
          center: computeCenter(
            (router.currentRoute.value.params.fileInfo as any).location
          ),
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
      }
    });

    return {
      setData,
      coordinates,
    };
  },
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
  img {
    width: 100%;
    height: 100%;
  }
}
</style>