<template>
  <div class="map">
    <div ref="container" class="container"></div>
    <div class="top" ref="top">
      <div class="content">
        <top-content></top-content>
      </div>
      <div>
        <div class="control" @click="topControlClick">
          <div style="margin-left: 6px">
            <el-icon v-if="topFlag"><ArrowUpBold color="#A1A1A0" /></el-icon>
            <el-icon v-else><ArrowDownBold color="#A1A1A0" /></el-icon>
          </div>
        </div>
      </div>
    </div>
    <div class="right" ref="right">
      <div class="control" @click="rightControlClick">
        <div style="margin-top: 5px">
          <el-icon v-if="rightFlag"><ArrowRightBold color="#A1A1A0" /></el-icon>
          <el-icon v-else><ArrowLeftBold color="#A1A1A0" /></el-icon>
        </div>
      </div>
      <div class="content">
        <right-content
          ref="rightLayer"
          @setVisible="setVisible"
          @setLayers="setLayers"
          @toolClick="toolClick"
        ></right-content>
      </div>
    </div>
    <div class="tools" v-if="toolFlag">
      <tool-content
        @close="closeClick"
        @sectionByDIYClick="sectionByDIYClick"
      ></tool-content>
    </div>
    <div class="data-select-tool" v-if="dataSelectFlag">
      <data-select-tool
        :demLayers="demLayers"
        @dataSelectClose="dataSelectClose"
        @dataSelectChange="dataSelectChange"
      ></data-select-tool>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import RightContent from "@/components/analyse/RightContent.vue";
import TopContent from "@/components/analyse/TopContent.vue";
import ToolContent from "@/components/analyse/ToolContent.vue";
import DataSelectTool from "@/components/analyse/DataSelectTool.vue";
import router from "@/router";
import { notice } from "@/utils/notice";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { sectionUtil } from "@/components/tools/ts/mapUtils";

export default defineComponent({
  components: { RightContent, TopContent, ToolContent, DataSelectTool },
  setup() {
    const container = ref<HTMLElement>();
    const right = ref<HTMLElement>();
    const rightLayer = ref()
    const rightFlag = ref(false);
    const top = ref<HTMLElement>();
    const topFlag = ref(false);
    const flag = ref(false);
    const map = ref<mapBoxGl.Map>();
    const layers = ref<any[]>([]);
    const sortLayers = ref<string[]>([]);
    const toolFlag = ref(false);
    const dataSelectFlag = ref(false);
    const currentDem = ref({
      id: "",
      name: "",
    });

    const draw = new MapboxDraw({
      controls: {
        combine_features: false,
        uncombine_features: false,
        trash: false,
        point: false,
        polygon: false,
      },
    });

    const demLayers = computed(() => {
      const temp: any[] = [];
      layers.value.forEach((item) => {
        if (item.type === "riverBed") {
          temp.push(item);
        }
      });
      return temp;
    });

    const tdtVec: AnySourceData = {
      type: "raster",
      tiles: [
        "http://t0.tianditu.com/vec_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
      ],
      tileSize: 256,
    };
    const txt: AnySourceData = {
      type: "raster",
      tiles: [
        "http://t0.tianditu.com/cva_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
      ],
      tileSize: 256,
    };

    const initMap = () => {
      map.value = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        style: {
          version: 8,
          sources: {
            tdtVec: tdtVec,
            txt: txt,
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

        attributionControl: false,
        center: [121.193496, 31.791046],
        zoom: 8,
      });
      map.value.on("load", () => {
        flag.value = true;
      });
    };

    const rightControlClick = () => {
      if (right.value) {
        if (rightFlag.value) {
          right.value.style.transition = "all 0.5s";
          right.value.style.right = "-300px";
          rightFlag.value = false;
        } else {
          right.value.style.transition = "all 0.5s";
          right.value.style.right = "30px";
          rightFlag.value = true;
        }
      }
    };

    const topControlClick = () => {
      if (top.value) {
        if (topFlag.value) {
          top.value.style.transition = "all 0.5s";
          top.value.style.top = "-50px";
          topFlag.value = false;
        } else {
          top.value.style.transition = "all 0.5s";
          top.value.style.top = "0";
          topFlag.value = true;
        }
      }
    };

    const addAllLayers = (layers: any[], sortLayers: string[]) => {
      sortLayers.forEach((item) => {
        for (let i = 0; i < layers.length; i++) {
          if (item === layers[i].id) {
            addLayer(layers[i]);
          }
        }
      });
    };

    const addLayer = (layer: any) => {
      if (map.value != undefined) {
        if (layer.type === "satellite") {
          map.value.addSource(layer.id, {
            type: "raster",
            tiles: [
              `http://localhost:8002/analyticDataSet/getRaster/${layer.id}/{x}/{y}/{z}`,
            ],
          });
          map.value.addLayer({
            id: layer.id,
            type: "raster",
            source: layer.id,
            paint: {
              "raster-opacity": 0.7,
            },
          });
        } else if (layer.type === "riverBed") {
          map.value.addSource(layer.id, {
            type: "raster-dem",
            tiles: [
              `http://localhost:8002/analyticDataSet/getRaster/${layer.id}/{x}/{y}/{z}`,
            ],
          });
          map.value.addLayer({
            id: layer.id,
            type: "hillshade",
            source: layer.id,
          });
        } else if (layer.type == "section") {
          map.value.addSource(layer.id, {
            type: "geojson",
            data: {
              type: "Feature",
              geometry: {
                type: "LineString",
                coordinates: layer.geoJson.coordinates,
              },
              properties: {},
            },
          });
          map.value.addLayer({
            id: layer.id,
            type: "line",
            source: layer.id,
            paint: {
              "line-color": "red"
            }
          });
        }
      }
    };

    const removeAllLayers = (oldSortList: string[]) => {
      oldSortList.forEach((item) => {
        if (map.value != undefined) {
          map.value.removeLayer(item);
          map.value.removeSource(item);
        }
      });
    };

    const setVisible = (val: any) => {
      console.log(val);
      if (map.value) {
        if (val.visibility) {
          map.value.setLayoutProperty(val.id, "visibility", "visible");
        } else {
          map.value.setLayoutProperty(val.id, "visibility", "none");
        }
      }
    };

    const setLayers = (val: any[]) => {
      val.forEach((item) => {
        if (item.isAdd) {
          addLayer(item);
        }
      });
    };

    const toolClick = () => {
      toolFlag.value = !toolFlag.value;
      if (!toolFlag.value) {
        dataSelectFlag.value = false;
      }
    };

    const closeClick = () => {
      toolFlag.value = false;
      dataSelectFlag.value = false;
      map.value?.removeControl(draw);
      map.value?.setPaintProperty(
        currentDem.value.id,
        "hillshade-shadow-color",
        "#000000"
      );
      currentDem.value.id = "";
      currentDem.value.name = "";
    };

    const sectionByDIYClick = () => {
      if (demLayers.value.length > 0) {
        dataSelectFlag.value = true;
      } else {
        notice("warning", "警告", "请检查是否添加了基础河床数据");
      }
    };

    const dataSelectClose = () => {
      dataSelectFlag.value = false;
      map.value?.removeControl(draw);
      map.value?.setPaintProperty(
        currentDem.value.id,
        "hillshade-shadow-color",
        "#000000"
      );
      currentDem.value.id = "";
      currentDem.value.name = "";
    };

    const addSection = (layer: any) => {
      addLayer(layer);
      console.log(rightLayer.value)
      rightLayer.value.addLayer(layer)
    };

    const dataSelectChange = (val: { id: string; name: string }) => {
      console.log(val);
      map.value?.setPaintProperty(val.id, "hillshade-shadow-color", "#CDE67B");
      if (currentDem.value.id === "") {
        map.value?.addControl(draw, "top-left");
        sectionUtil(
          map.value,
          draw,
          router.currentRoute.value.params.id as string,
          val.id,
          val.name,
          addSection
        );
      }
      currentDem.value.id = val.id;
      currentDem.value.name = val.name;
    };

    watch(
      () => {
        return router.currentRoute.value.path;
      },
      () => {
        if (router.currentRoute.value.name === "project") {
          currentDem.value.id = "";
          currentDem.value.name = "";
          map.value?.removeControl(draw);
          dataSelectFlag.value = false;
          removeAllLayers(sortLayers.value);
          layers.value = (
            router.currentRoute.value.params.projectInfo as any
          ).layers;
          sortLayers.value = (
            router.currentRoute.value.params.projectInfo as any
          ).sortLayers;
          if (flag.value) {
            addAllLayers(layers.value, sortLayers.value);
          } else {
            if (map.value) {
              map.value.on("load", () => {
                addAllLayers(layers.value, sortLayers.value);
              });
            }
          }
        }
      }
    );
    onMounted(async () => {
      console.log(router.currentRoute.value.params);
      initMap();
      layers.value = (
        router.currentRoute.value.params.projectInfo as any
      ).layers;
      sortLayers.value = (
        router.currentRoute.value.params.projectInfo as any
      ).sortLayers;
      if (flag.value) {
        addAllLayers(layers.value, sortLayers.value);
      } else {
        if (map.value) {
          map.value.on("load", () => {
            addAllLayers(layers.value, sortLayers.value);
          });
        }
      }
    });

    return {
      map,
      flag,
      container,
      right,
      rightControlClick,
      topControlClick,
      rightFlag,
      top,
      topFlag,
      setVisible,
      setLayers,
      toolFlag,
      toolClick,
      closeClick,
      dataSelectFlag,
      demLayers,
      sectionByDIYClick,
      dataSelectClose,
      dataSelectChange,
      rightLayer
    };
  },
});
</script>

<style lang="scss" scoped>
@-webkit-keyframes scale-up-hor-left {
  0% {
    -webkit-transform: scaleX(0.4);
    transform: scaleX(0.4);
    -webkit-transform-origin: 0% 0%;
    transform-origin: 0% 0%;
  }
  100% {
    -webkit-transform: scaleX(1);
    transform: scaleX(1);
    -webkit-transform-origin: 0% 0%;
    transform-origin: 0% 0%;
  }
}
@keyframes scale-up-hor-left {
  0% {
    -webkit-transform: scaleX(0.4);
    transform: scaleX(0.4);
    -webkit-transform-origin: 0% 0%;
    transform-origin: 0% 0%;
  }
  100% {
    -webkit-transform: scaleX(1);
    transform: scaleX(1);
    -webkit-transform-origin: 0% 0%;
    transform-origin: 0% 0%;
  }
}

.map {
  height: 100%;
  position: relative;
  overflow: hidden;
  .container {
    height: 100%;
    /deep/ .mapboxgl-ctrl-bottom-left {
      display: none !important;
    }
  }
  .top {
    position: absolute;
    z-index: 99;
    height: 70px;
    width: 800px;
    top: -50px;
    left: calc(50% - 400px);

    .content {
      height: 50px;
    }
    .control {
      width: 30px;
      height: 20px;
      background: white;
      border-bottom-left-radius: 4px;
      border-bottom-right-radius: 4px;
      margin: 0 auto;
      cursor: pointer;
    }
  }
  .right {
    position: absolute;
    z-index: 99;
    top: 60px;
    right: -300px;
    height: 70%;
    width: 320px;
    display: flex;
    .control {
      width: 20px;
      height: 30px;
      border-top-left-radius: 4px;
      border-bottom-left-radius: 4px;
      background: white;
      cursor: pointer;
    }
    .content {
      width: 300px;
      background: rgba($color: #000000, $alpha: 0.5);
    }
  }
  .tools {
    position: absolute;
    z-index: 99;
    top: 60px;
    left: 30px;
    -webkit-animation: scale-up-hor-left 0.4s
      cubic-bezier(0.39, 0.575, 0.565, 1) both;
    animation: scale-up-hor-left 0.4s cubic-bezier(0.39, 0.575, 0.565, 1) both;
  }
  .data-select-tool {
    position: absolute;
    z-index: 99;
    top: 80px;
    left: 350px;
    -webkit-animation: scale-up-hor-left 0.4s
      cubic-bezier(0.39, 0.575, 0.565, 1) both;
    animation: scale-up-hor-left 0.4s cubic-bezier(0.39, 0.575, 0.565, 1) both;
  }
}
</style>