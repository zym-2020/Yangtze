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
          @deleteLayer="deleteLayer"
          @sortMapLayers="sortMapLayers"
        ></right-content>
      </div>
    </div>
    <div class="tools" v-if="toolFlag">
      <tool-content
        :demLayers="demLayers"
        @close="closeClick"
        @sectionByDIYClick="sectionByDIYClick"
        @slopeClick="slopeClick"
        @flushSilt="flushSilt"
        @contourClick="contourClick"
        @regionByDIYClick="regionByDIYClick"
      ></tool-content>
    </div>
    <div class="legend" v-if="slopeFlag">
      <Legend />
    </div>
  </div>

  <el-dialog v-model="dataSelectFlag" width="400px" title="请选择数据集">
    <data-select
      :demLayers="demLayers"
      :dataSelectType="dataSelectType"
      @dataSelectReturn="dataSelectReturn"
    ></data-select>
  </el-dialog>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import "mapbox-gl/dist/mapbox-gl.css";
import RightContent from "@/components/analyse/RightContent.vue";
import TopContent from "@/components/analyse/TopContent.vue";
import ToolContent from "@/components/analyse/ToolContent.vue";
import router from "@/router";
import { notice } from "@/utils/notice";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { uuid } from "@/utils/common";
import Legend from "@/components/tools/Legend.vue";
import DataSelect from "@/components/tools/DataSelect.vue";
import {
  addLayers,
  addSection,
  checkLayerState,
  getFlushId,
  sortLayer,
  addRegion,
  checkAddRegion,
  getRegionLayer,
} from "@/api/request";
import { useStore } from "@/store";

export default defineComponent({
  components: {
    RightContent,
    TopContent,
    ToolContent,
    Legend,
    DataSelect,
  },
  setup() {
    const store = useStore();
    const container = ref<HTMLElement>();
    const right = ref<HTMLElement>();
    const rightLayer = ref();
    const rightFlag = ref(false);
    const nameCount = ref<any>(
      (router.currentRoute.value.params.projectInfo as any).nameCount
    );
    const top = ref<HTMLElement>();
    const topFlag = ref(false);
    const flag = ref(false);
    const map = ref<mapBoxGl.Map>();
    const layers = ref<any[]>([]);
    const toolFlag = ref(false);
    const sectionDIYflag = ref(false);
    const regionByDIYFlag = ref(false);
    const dataSelectFlag = ref(false);
    const polygonCoordinates = ref<any[]>([]);
    const dataSelectType = ref("");

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
        trash: false,
        point: false,
        line_string: false,
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

    const slopeFlag = computed(() => {
      for (let i = 0; i < layers.value.length; i++) {
        if (layers.value[i].type === "slope") {
          return true;
        }
      }
      return false;
    });

    const getCoordinates = (coordinates: any[]) => {
      const result: any[] = [];
      coordinates.forEach((item) => {
        result.push([item.longitude, item.latitude]);
      });
      console.log(result);
      return result;
    };

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
        accessToken:
          "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg",
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

    const addAllLayers = (layers: any[]) => {
      layers.forEach((item) => {
        addLayer(item);
      });
    };

    const addLayer = (layer: any) => {
      if (flag.value) {
        if (layer.type === "satellite") {
          map.value?.addSource(layer.id, {
            type: "raster",
            tiles: [
              `http://localhost:8002/analyticDataSet/getRaster/${layer.id}/{x}/{y}/{z}`,
            ],
          });
          map.value?.addLayer({
            id: layer.id,
            type: "raster",
            source: layer.id,
            paint: {
              "raster-opacity": 0.7,
            },
          });
        } else if (layer.type === "riverBed" || layer.type === "flush") {
          map.value?.addSource(layer.id, {
            type: "raster",
            tiles: [
              `http://localhost:8002/analyticDataSet/getRaster/${layer.id}/{x}/{y}/{z}`,
            ],
          });
          map.value?.addLayer({
            id: layer.id,
            type: "raster",
            source: layer.id,
          });
        } else if (layer.type === "section") {
          map.value?.addSource(layer.id, {
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
          map.value?.addLayer({
            id: layer.id,
            type: "line",
            source: layer.id,
            paint: {
              "line-color": "red",
            },
          });
        } else if (layer.type === "slope") {
          map.value?.addSource(layer.id, {
            type: "raster",
            tiles: [
              `http://localhost:8002/analyticDataSet/getSlope/${layer.demSlopeId}/{x}/{y}/{z}`,
            ],
          });
          map.value?.addLayer({
            id: layer.id,
            source: layer.id,
            type: "raster",
          });
        } else if (
          layer.type === "contour" ||
          layer.type === "deepHorizonLine"
        ) {
          map.value?.addSource(layer.id, {
            type: "vector",
            tiles: [
              `http://localhost:8002/analyticDataSet/${layer.name}/{x}/{y}/{z}`,
            ],
          });
          map.value?.addLayer({
            id: layer.id,
            source: layer.id,
            type: "line",
            "source-layer": layer.name,
          });
        } else if (layer.type === "region") {
          map.value?.addSource(layer.id, {
            type: "image",
            url: `http://localhost:8002/project/getRegion/${store.state.user.email}/${router.currentRoute.value.params.id}/${layer.id}`,
            coordinates: getCoordinates(layer.points),
          });
          map.value?.addLayer({
            id: layer.id,
            source: layer.id,
            type: "raster",
          });
        }
      }
    };

    const deleteLayer = (val: string) => {
      if (map.value?.getLayer(val) != undefined) {
        map.value.removeLayer(val);
        map.value.removeSource(val);
      }
    };

    const removeAllLayers = (layers: any[]) => {
      layers.forEach((item) => {
        if (map.value != undefined) {
          map.value.removeLayer(item.id);
          map.value.removeSource(item.id);
        }
      });
    };

    const removeControl = () => {
      if (map.value?.hasControl(lineDraw)) {
        map.value.removeControl(lineDraw);
      }
      if (map.value?.hasControl(polygonDraw)) {
        map.value.removeControl(polygonDraw);
      }
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
        addLayer(item);
        console.log(layers.value);
      });
    };

    const toolClick = () => {
      toolFlag.value = true;
    };

    const closeClick = () => {
      sectionDIYClose();
      regionByDIYClose();
      toolFlag.value = false;
    };

    const sectionByDIYClick = () => {
      if (!sectionDIYflag.value) {
        if (demLayers.value.length > 0) {
          removeControl();
          map.value?.addControl(lineDraw, "top-left");
          map.value?.off("draw.create", drawPolygon);
          map.value?.on("draw.create", drawSection);
          sectionDIYflag.value = true;
          regionByDIYFlag.value = false
        } else {
          notice("warning", "警告", "请检查是否添加了基础河床数据");
        }
      }
    };

    const regionByDIYClick = () => {
      if (!regionByDIYFlag.value) {
        if (demLayers.value.length > 0) {
          removeControl();
          map.value?.off("draw.create", drawSection);
          map.value?.on("draw.create", drawPolygon);
          map.value?.addControl(polygonDraw, "top-left");
          regionByDIYFlag.value = true;
          sectionDIYflag.value = false
        } else {
          notice("warning", "警告", "请检查是否添加了基础河床数据");
        }
      }
    };

    const sectionDIYClose = () => {
      removeControl();
      sectionDIYflag.value = false;
      map.value?.off("draw.create", drawSection);
    };

    const regionByDIYClose = () => {
      removeControl();
      regionByDIYFlag.value = false;
    };

    const getName = (type: string) => {
      let name = "";
      if (type === "section") {
        name = "断面形态_" + nameCount.value.section;
        nameCount.value.section = nameCount.value.section + 1;
      } else if (type === "sectionContrast") {
        name = "断面比较_" + nameCount.value.sectionContrast;
        nameCount.value.sectionContrast = nameCount.value.sectionContrast + 1;
      }
      return name;
    };
    const drawLayer = (layer: any) => {
      addLayer(layer);
      layer.show = true;
      layer.name = getName(layer.type);
      rightLayer.value.addLayer(layer);
    };

    const drawSection = async () => {
      const coordinates = (lineDraw.getAll().features[0].geometry as any)
        .coordinates;
      const uid = uuid();
      const layer = {
        id: uid,
        type: "section",

        geoJson: {
          coordinates: coordinates as any[],
          type: "LineString",
        },
      };

      lineDraw.deleteAll();
      drawLayer(layer);
      const res = await addSection(
        layer,
        router.currentRoute.value.params.id as string
      );
      async function handle(params: any) {
        const data = await checkLayerState(
          router.currentRoute.value.params.id as string,
          uid
        );
        if (data != null) {
          if ((data as any).code === 0) {
            if (data.data === 1) {
              rightLayer.value.updateLayer(params);
              rightLayer.value.showResult({ layer: params, type: "section" });
            } else if (data.data === 0) {
              setTimeout(async () => {
                await handle(params);
              }, 1000);
            }
          }
        }
      }
      if (res != null && (res as any).code === 0) {
        await handle(res.data);
      }
    };

    const drawPolygon = () => {
      const coordinates = (polygonDraw.getAll().features[0].geometry as any)
        .coordinates;
      polygonCoordinates.value = coordinates;
      dataSelectType.value = "region";
      dataSelectFlag.value = true;

      polygonDraw.deleteAll();
    };

    const dataSelectReturn = async (val: any) => {
      dataSelectFlag.value = false;
      if (val.type === "region") {
        const key = await addRegion(
          router.currentRoute.value.params.id as string,
          val.data,
          polygonCoordinates.value
        );
        async function handle(param: string) {
          const data = await checkAddRegion(param);
          if (data != null) {
            if ((data as any).code === 0) {
              if (data.data === 1) {
                const layer = await getRegionLayer(
                  router.currentRoute.value.params.id as string,
                  key.data
                );
                console.log(layer.data);
                layer.data.show = true;
                rightLayer.value.addLayer(layer.data);
                addLayer(layer.data);
                notice("success", "成功", "计算成功");
              } else if (data.data === 0) {
                setTimeout(async () => {
                  await handle(param);
                }, 1000);
              } else {
                notice("error", "错误", "区域计算错误");
              }
            }
          }
        }
        if (key != null && (key as any).code === 0) {
          await handle(key.data);
        }
      } else if (val.type === "slope") {
        const json = [
          {
            id: uuid(),
            type: "slope",
            name: val.data.name + "河床坡度",
            demSlopeId: val.data.id,
            show: true,
          },
        ];
        const data = await addLayers(
          json,
          router.currentRoute.value.params.id as string
        );
        if (data != null && (data as any).code === 0) {
          addLayer(json[0]);
          rightLayer.value.addLayer(json[0]);
        }
      } else if (val.type === "flushSilt") {
        const benchmark = val.data.benchmark.id;
        const reference = val.data.reference.id;
        const name = val.data.reference.name + "_" + val.data.benchmark.name;
        if (benchmark != "" && reference != "") {
          const data = await getFlushId({
            projectId: router.currentRoute.value.params.id as string,
            benchmark: benchmark,
            reference: reference,
            name: name,
          });
          if (data != null && (data as any).code === 0) {
            const flushId = data.data;
            const layer = {
              id: flushId,
              name: name,
              type: "flush",
              show: true,
            };
            addLayer(layer);
            rightLayer.value.addLayer(layer);
          }
        }
      } else if (val.type === "contour") {
        console.log(val.data);
      }
    };

    const slopeClick = async () => {
      dataSelectType.value = "slope";
      dataSelectFlag.value = true;
    };

    const flushSilt = async () => {
      dataSelectType.value = "flushSilt";
      dataSelectFlag.value = true;
    };

    const contourClick = () => {
      dataSelectType.value = "contour";
      dataSelectFlag.value = true;
    };

    const sortMapLayers = async (val: any) => {
      const dragId = val.dragId;
      console.log(val);
      for (let i = 0; i < val.tree.length; i++) {
        if (dragId === val.tree[i].id) {
          if (i === val.tree.length - 1) {
            map.value?.moveLayer(dragId);
          } else {
            map.value?.moveLayer(dragId, val.tree[i + 1].id);
          }
          break;
        }
      }
      await sortLayer(
        (router.currentRoute.value.params.projectInfo as any).id,
        val.tree
      );
    };

    watch(
      () => {
        return router.currentRoute.value.path;
      },
      () => {
        if (router.currentRoute.value.name === "project") {
          removeControl();
          sectionDIYflag.value = false;
          regionByDIYFlag.value = false;
          removeAllLayers(layers.value);
          layers.value = (
            router.currentRoute.value.params.projectInfo as any
          ).layers;

          if (flag.value) {
            addAllLayers(layers.value);
          } else {
            if (map.value) {
              map.value.on("load", () => {
                addAllLayers(layers.value);
              });
            }
          }
        }
      }
    );

    onMounted(async () => {
      initMap();

      layers.value = (
        router.currentRoute.value.params.projectInfo as any
      ).layers;
      console.log(layers.value);
      if (flag.value) {
        addAllLayers(layers.value);
      } else {
        if (map.value) {
          map.value.on("load", () => {
            addAllLayers(layers.value);
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
      sectionDIYflag,
      regionByDIYFlag,
      demLayers,
      sectionByDIYClick,
      regionByDIYClick,
      slopeClick,
      sectionDIYClose,
      rightLayer,
      deleteLayer,
      slopeFlag,
      flushSilt,
      contourClick,
      sortMapLayers,
      dataSelectFlag,
      dataSelectReturn,
      dataSelectType,
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
  .tools,
  .data-select-tool,
  .data-multi-select {
    position: absolute;
    z-index: 99;
    -webkit-animation: scale-up-hor-left 0.4s
      cubic-bezier(0.39, 0.575, 0.565, 1) both;
    animation: scale-up-hor-left 0.4s cubic-bezier(0.39, 0.575, 0.565, 1) both;
  }
  .tools {
    top: 60px;
    left: 30px;
  }
  .data-select-tool {
    top: 80px;
    left: 350px;
  }
  .data-multi-select {
    top: 80px;
    left: 350px;
  }
  .legend {
    position: absolute;
    z-index: 99;
    bottom: 0;
    left: 0;
  }
}
</style>