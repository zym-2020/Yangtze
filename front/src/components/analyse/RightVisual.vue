<template>
  <div class="right-visual">
    <div ref="container" class="container"></div>
    <el-dialog v-model="chartVisual" width="900px" title="断面图">
      <chart-visual :chartVisualInfo="chartVisualInfo"></chart-visual>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, nextTick, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import { getCoordinates, getGeoJson, updateBasemap } from "@/api/request";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import ChartVisual from "./ChartVisual.vue";
import { notice } from "@/utils/notice";
import { prefix } from '@/prefix'
import router from "@/router";
export default defineComponent({
  components: { ChartVisual },
  props: {
    layerList: {
      type: Array,
    },
  },
  emits: ["drawHandle"],
  setup(props, context) {
    const container = ref<HTMLElement>();
    let map: mapBoxGl.Map;
    const chartVisual = ref(false);
    const chartVisualInfo = ref();

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

    const operateDraw = (param: number) => {
      if (param === 0) {
        if (map.hasControl(lineDraw)) {
          map.removeControl(lineDraw);
        }
        if (map.hasControl(polygonDraw)) {
          map.removeControl(polygonDraw);
        }
      } else if (param === 1) {
        if (map.hasControl(polygonDraw)) {
          map.removeControl(polygonDraw);
        }
        if (!map.hasControl(lineDraw)) {
          map.addControl(lineDraw, "top-left");
        }
      } else if (param === 2) {
        if (map.hasControl(lineDraw)) {
          map.removeControl(lineDraw);
        }
        if (!map.hasControl(polygonDraw)) {
          map.addControl(polygonDraw, "top-left");
        }
      }
    };

    const draw = async (e: any) => {
      if (map.hasControl(lineDraw)) {
        context.emit("drawHandle", {
          geoJson: e.features[0],
          visualType: "geoJsonLine",
        });
        lineDraw.deleteAll();
      }
      if (map.hasControl(polygonDraw)) {
        context.emit("drawHandle", {
          geoJson: e.features[0],
          visualType: "geoJsonPolygon",
        });
        polygonDraw.deleteAll();
      }
    };

    const basemap = computed(() => {
      if (
        (router.currentRoute.value.params.projectInfo as any).basemap === ""
      ) {
        return {
          version: 8,
          sources: {},
          layers: [],
        };
      } else {
        return (router.currentRoute.value.params.projectInfo as any).basemap;
      }
    });

    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        accessToken:
          "pk.eyJ1Ijoiam9obm55dCIsImEiOiJja2xxNXplNjYwNnhzMm5uYTJtdHVlbTByIn0.f1GfZbFLWjiEayI6hb_Qvg",
        style: basemap.value,
        center: [121.18, 31.723],
        zoom: 8,
      });
      map.on("load", async () => {
        await initLayers();
        // map.addSource("png", {
        //   type: "image",
        //   url: "/flowTex1.png",
        //   coordinates: [
        //     [120.04433328184923, 32.09360568405092],
        //     [121.95857869699789, 32.09360568405092],
        //     [121.95857869699789, 31.168340998477692],
        //     [120.04433328184923, 31.168340998477692],
        //   ],
        // });
        // map.addLayer({
        //   id: "png",
        //   type: "raster",
        //   source: "png",
        // });
      });
    };

    const initLayers = async () => {
      for (let i = (props.layerList as any[]).length - 1; i >= 0; i--) {
        await addMapLayer((props.layerList as any[])[i]);
      }
      map.on("draw.create", draw);
    };

    const addMapLayer = async (param: {
      id: string;
      visualId: string;
      visualType: string;
    }) => {
      if (map.getLayer(param.id) === undefined) {
        let type: "line" | "circle" | "fill" = "line";
        if (
          param.visualType === "lineVectorTile3D" ||
          param.visualType === "lineVectorTile" ||
          param.visualType === "flushContour"
        ) {
          type = "line";
        } else if (
          param.visualType === "pointVectorTile" ||
          param.visualType === "pointVectorTile3D"
        ) {
          type = "circle";
        } else if (
          param.visualType === "polygonVectorTile" ||
          param.visualType === "polygonVectorTile3D"
        ) {
          type = "fill";
        }
        if (
          param.visualType === "lineVectorTile3D" ||
          param.visualType === "lineVectorTile" ||
          param.visualType === "pointVectorTile" ||
          param.visualType === "pointVectorTile3D" ||
          param.visualType === "polygonVectorTile" ||
          param.visualType === "polygonVectorTile3D" ||
          param.visualType === "flushContour"
        ) {
          map.addSource(param.id, {
            type: "vector",
            tiles: [
              `${prefix}visual/getVectorTiles/${param.visualId}/{x}/{y}/{z}`,
            ],
          });
          map.addLayer({
            id: param.id,
            source: param.id,
            type: type,
            "source-layer": param.visualId,
          });
        } else if (
          param.visualType === "rasterTile" ||
          param.visualType === "elevationFlush" ||
          param.visualType === "slope"
        ) {
          map.addSource(param.id, {
            type: "raster",
            tiles: [
              `${prefix}visual/getRaster/${param.visualId}/{x}/{y}/{z}`,
            ],
          });
          map.addLayer({
            id: param.id,
            type: "raster",
            source: param.id,
          });
        } else if (
          param.visualType === "png" ||
          param.visualType === "movePng" ||
          param.visualType === "regionFlush"
        ) {
          const coordinates = await getCoordinates(param.visualId);
          if (coordinates != null && (coordinates as any).code === 0) {
            map.addSource(param.id, {
              type: "image",
              url: `${prefix}visual/getPngResource/${param.visualId}`,
              coordinates: coordinates.data,
            });
            map.addLayer({
              id: param.id,
              type: "raster",
              source: param.id,
            });
          }
        } else if (
          param.visualType === "geoJsonLine" ||
          param.visualType === "geoJsonPoint" ||
          param.visualType === "geoJsonPolygon"
        ) {
          let type: "fill" | "circle" | "line" = "line";
          if (param.visualType === "geoJsonPoint") {
            type = "circle";
          } else if (param.visualType === "geoJsonPolygon") {
            type = "fill";
          }
          const geojson = await getGeoJson(param.id);
          if (geojson != null && (geojson as any).code === 0) {
            map.addSource(param.id, {
              type: "geojson",
              data: geojson.data,
            });
            if (type === "fill") {
              map.addLayer({
                id: param.id,
                type: type,
                source: param.id,
                paint: {
                  "fill-opacity": 0.5,
                  "fill-color": "#f24545",
                },
              });
            } else {
              map.addLayer({
                id: param.id,
                type: type,
                source: param.id,
              });
            }
          }
        }
      }
    };

    const addChart = async (param: {
      id: string;
      name: string;
      visualType: string;
      visualId: string;
    }) => {
      chartVisualInfo.value = param;
      chartVisual.value = true;
    };

    const removeLayer = (id: string) => {
      if (map.getLayer(id) != undefined) {
        map.removeLayer(id);
        map.removeSource(id);
      }
    };

    const moveLayer = (param: { drop: string; target: string }) => {
      if (param.target === "") {
        map.moveLayer(param.drop);
      } else {
        map.moveLayer(param.drop, param.target);
      }
    };

    const changeLayerState = (param: { id: string; flag: boolean }) => {
      if (map.getLayer(param.id) != undefined) {
        if (param.flag) {
          map.setLayoutProperty(param.id, "visibility", "visible");
        } else {
          map.setLayoutProperty(param.id, "visibility", "none");
        }
      }
    };

    const changeBasemap = async (param: number, url: string) => {
      let list: any[] = [];
      if (param != 0) {
        list = map.getStyle().layers.slice(param * -1);
      }
      const source: any = {};
      list.forEach((item) => {
        for (const i in map.getStyle().sources) {
          if (item.id === i) {
            source[i] = map.getStyle().sources[i];
          }
        }
      });
      console.log(list, source);
      if (url != "") {
        map.setStyle(url);
        map.once("styledata", () => {
          for (const i in source) {
            map.addSource(i, source[i]);
          }
          list.forEach((item) => {
            map.addLayer(item);
          });
        });
      } else {
        map.setStyle({
          version: 8,
          sources: source,
          layers: list,
        });
      }
      await updateBasemap({
        projectId: router.currentRoute.value.params.id as string,
        url: url,
      });
    };

    const mapResize = () => {
      map.resize();
    };

    onMounted(() => {
      initMap();
    });

    return {
      container,
      mapResize,
      addMapLayer,
      removeLayer,
      changeLayerState,
      operateDraw,
      addChart,
      chartVisual,
      chartVisualInfo,
      moveLayer,
      changeBasemap,
      // chart,
      // btnClick,
      // flag,
    };
  },
});
</script>

<style lang="scss" scoped>
.right-visual {
  height: calc(100% - 10px);
  width: 100%;
  position: relative;
  padding: 5px;
  .container {
    height: 100%;
    width: 100%;
    border-radius: 8px;
  }
  /deep/.el-dialog {
    .el-dialog__header {
      padding: 10px;
      background: #4c75a9;
      margin: 0px;
      .el-dialog__title {
        color: white;
      }
      .el-dialog__headerbtn {
        height: 40px;
        .el-icon {
          color: white;
        }
      }
    }
    .el-dialog__body {
      padding: 0px;
    }
  }
}
</style>