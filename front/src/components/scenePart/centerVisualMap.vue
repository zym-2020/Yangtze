<template>
  <div class="scene-map-wrapper">
    <div id="map"></div>
  </div>
</template>

<script lang="ts">
export default {
  name: "centerVisualMap",
};
</script>

<script setup lang='ts'>
import mapboxgl from "mapbox-gl";
import mapBoxGl from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import { onMounted } from "@vue/runtime-core";
import { frontData } from "../../frontData";
import { ref, watch } from "vue";
import { useStore } from "@/store";
import { QueryShpByName } from "@/api/request";
import { Console } from "console";
import router from "@/router";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { Coordinate } from "@element-plus/icons";
interface Props {
  mapId: string;
}
const store = useStore();
const props = defineProps<Props>();
const map = ref<mapBoxGl.Map>();
const pngName = ref("Yang");
const mapIndex = ref(props.mapId);
const mapName = ref("");
watch(
  () => router.currentRoute.value.params.id,
  async (newValue, oldValue) => {
    //避免跳转到其他页面时去调用后台的接口
    if (
      router.currentRoute.value.fullPath ==
      "/data/" + router.currentRoute.value.params.id
    ) {
      const data = await QueryShpByName(
        (router.currentRoute.value.params.fileInfo as any).name
      );
      mapName.value = data.data[0].shpPinYinName;
      let names = mapName.value;
      if (names[0] >= "0" && names[0] <= "9") names = "a" + names;
      mapboxgl.accessToken =
        "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ";

      const mapConfig = frontData["maps"];
      // init mapbox...
      map.value = new mapboxgl.Map({
        container: "map",
        style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
        center: [121.18, 31.723],
        zoom: 8,
      });
      const namess = "dengshenxian";

      // const popup = new mapboxgl.Popup({ closeOnClick: false })
      //   .setLngLat([121.18, 31.723])
      //   .setHTML("<h1>Hello World!</h1>")
      //   .addTo(map.value);

      map.value.on("load", () => {
        map.value?.addSource("test", {
          type: "vector",
          tiles: [
            "http://localhost:8002/vector/" + names + "/{x}/{y}/{z}",
          ],
        });

        map.value?.addLayer({
          id: "test",
          source: "test",
          type: "line",
          "source-layer": names,
          layout: {
            "line-join": "round",
            "line-cap": "round",
          },
          paint: {
            "line-color": "#0000CD",
            "line-width": 5,
          },
        });
        // 待完善的栅格显示
        // map.value?.addSource(layer.id, {
        //   type: "raster",
        //   tiles: [
        //     `http://localhost:8002/analyticDataSet/getSlope/${layer.demSlopeId}/{x}/{y}/{z}`,
        //   ],
        // });
        // map.value?.addLayer({
        //   id: layer.id,
        //   source: layer.id,
        //   type: "raster",
        // });
        //console.log("http://localhost:8002/vector/" +names+" /{x}/{y}/{z}")
      });
      map.value?.addControl(new mapboxgl.NavigationControl());
      const lineDraw = new MapboxDraw({
        controls: {
          combine_features: false,
          uncombine_features: false,
          trash: false,
          point: false,
          polygon: false,
        },
      });

      const polygonDraw = new MapboxDraw({
        controls: {
          combine_features: false,
          uncombine_features: false,
          trash: true,
          point: false,
          line_string: false,
        },
      });

      const updateArea = function (e: any) {
        if (e.type === "draw.create") {
          //图形绘制完成
        } else if (e.type === "draw.update") {
          //修改绘制图形后
        } else if (e.type === "draw.delete") {
          //删除绘制图形
        }
      };
      map.value.addControl(polygonDraw, "top-right");
      map.value.on("draw.create", function (feature) {});
      map.value.on("draw.delete", updateArea);
      map.value.on("draw.update", updateArea);
    }
  },
  { immediate: true, deep: true }
);
</script>

<style lang='scss'>
div.scene-map-wrapper {
  cursor: pointer;
  background-color: transparent;
  width: 52%;
  height: 63%;

  div#map {
    position: absolute;
    top: 0;
    bottom: 0;

    width: 100%;
    height: 100%;
    background: rgba(255, 255, 255, 0.2);
  }
}
</style>