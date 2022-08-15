<template>
  <div class="scene-map-wrapper2">
    <div id="map"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import "@mapbox/mapbox-gl-draw/dist/mapbox-gl-draw.css";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import mapBoxGl from "mapbox-gl";
import mapboxgl from "mapbox-gl";
import { emit } from "process";
export default defineComponent({
  name: "FindMap",
  setup(props, { emit }) {
    onMounted(() => {
      const map = ref<mapBoxGl.Map>();
      mapboxgl.accessToken =
        "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ";
      map.value = new mapboxgl.Map({
        container: "map",
        style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
        center: [121.18, 31.273],
        zoom: 8,
      });
      //控件
      map.value?.addControl(new mapboxgl.NavigationControl());
      //自定义绘制面
      const polygonDraw = new MapboxDraw({
        controls: {
          combine_features: false,
          uncombine_features: false,
          trash: true,
          point: false,
          line_string: false,
        },
      });
      //绘制事件
      const updateArea = function (e: any) {
        if (e.type === "draw.create") {
          //图形绘制完成
          emit("getCoor", e.features[0].geometry.coordinates[0]);
        } else if (e.type === "draw.update") {
          //修改绘制图形后
          emit("getCoor", e.features[0].geometry.coordinates[0]);
        } else if (e.type === "draw.delete") {
          emit("getCoor", e.features[0].geometry.coordinates[0]);
          //删除绘制图形
        }
      };
      map.value.addControl(polygonDraw, "top-right");
      map.value.on("draw.create", updateArea);
      map.value.on("draw.delete", updateArea);
      map.value.on("draw.update", updateArea);
    });
  },
});
</script>

<style lang="scss" scoped>
div.scene-map-wrapper2 {
  cursor: pointer;
  background-color: transparent;
  width: 52%;
  height: 63%;

  div#map {
    position: absolute;
    top: 200px;
    bottom: 0;

    width: 50%;
    height: 80%;
    background: rgba(255, 255, 255, 0.2);
  }
}
</style>