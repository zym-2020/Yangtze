<template>
  <div class="right-visual">
    <div ref="container" class="container"></div>
    <el-dialog v-model="chartVisual" width="900px" id="chart">
      <chart-visual :chartVisualInfo="chartVisualInfo"></chart-visual>
    </el-dialog>
    <el-dialog
      v-model="dialogVisible"
      :width="300"
      :title="visualType == 'geoJsonLine' ? '断面名称：' : '区域名称：'"
    >
      <div class="name">
        <el-input v-model="inputValue" />
        <div class="btn">
          <el-button type="primary" plain size="small" @click="clickHandle"
            >确定</el-button
          >
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, nextTick, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import {
  getCoordinates,
  getAnalyticGeoJson,
  updateBasemap,
  getContent,
} from "@/api/request";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import ChartVisual from "./ChartVisual.vue";
import { prefix } from "@/prefix";
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
    const dialogVisible = ref(false);
    const inputValue = ref("");
    const visualType = ref("");

    const volumeList = ref<
      { id: string; coordinates: number[][]; description: string }[]
    >([]);
    let geoJson: any;

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
        visualType.value = "geoJsonLine";
        geoJson = e.features[0];
        dialogVisible.value = true;
        lineDraw.deleteAll();
      }
      if (map.hasControl(polygonDraw)) {
        visualType.value = "geoJsonPolygon";
        geoJson = e.features[0];
        dialogVisible.value = true;
        polygonDraw.deleteAll();
      }
    };

    const clickHandle = () => {
      context.emit("drawHandle", {
        geoJson: geoJson,
        visualType: visualType.value,
        fileName: inputValue.value,
      });
      dialogVisible.value = false;
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

    const inPolygon = (
      lon: number,
      lat: number,
      coordinates: number[][],
      id: string
    ) => {
      if (
        lon > coordinates[0][0] &&
        lon < coordinates[2][0] &&
        lat > coordinates[2][1] &&
        lat < coordinates[0][1] &&
        map.getLayoutProperty(id, "visibility") != "none"
      ) {
        return true;
      } else {
        return false;
      }
    };

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
      });
      map.doubleClickZoom.disable();
      map.on("dblclick", (e) => {
        for (let i = 0; i < volumeList.value.length; i++) {
          if (
            inPolygon(
              e.lngLat.lng,
              e.lngLat.lat,
              volumeList.value[i].coordinates,
              volumeList.value[i].id
            )
          ) {
            new mapBoxGl.Popup()
              .setLngLat([
                (volumeList.value[i].coordinates[0][0] +
                  volumeList.value[i].coordinates[2][0]) /
                  2,
                (volumeList.value[i].coordinates[0][1] +
                  volumeList.value[i].coordinates[2][1]) /
                  2,
              ])
              .setHTML(volumeList.value[i].description)
              .addTo(map);
          }
        }
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
            tiles: [`${prefix}visual/getRaster/${param.visualId}/{x}/{y}/{z}`],
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
        } else if (param.visualType === "volume") {
          const content = await getContent(param.visualId);
          if (content != null && (content as any).code === 0) {
            map.addSource(param.id, {
              type: "image",
              url: `${prefix}visual/getPngResource/${param.visualId}`,
              coordinates: content.data.coordinates,
            });
            map.addLayer({
              id: param.id,
              type: "raster",
              source: param.id,
            });
            const description = `深度：${content.data.deep}，容积：${content.data.volume}㎡`;
            volumeList.value.push({
              id: param.id,
              coordinates: content.data.coordinates,
              description: description,
            });
            // map.on("dblclick", (e) => {
            //   console.log(map.getLayoutProperty(param.id, "visibility"));

            //   const description = `深度：${content.data.deep}，容积：${content.data.volume}㎡`;
            //   console.log(description);
            //   new mapBoxGl.Popup()
            //     .setLngLat([
            //       (content.data.coordinates[1][0] +
            //         content.data.coordinates[0][0]) /
            //         2,
            //       (content.data.coordinates[1][1] +
            //         content.data.coordinates[0][1]) /
            //         2,
            //     ])
            //     .setHTML(description)
            //     .addTo(map);
            // });
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
          const geojson = await getAnalyticGeoJson(param.id);
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
        for (let i = 0; i < volumeList.value.length; i++) {
          if (id === volumeList.value[i].id) {
            volumeList.value.splice(i, 1);
            return;
          }
        }
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
      dialogVisible,
      inputValue,
      visualType,
      clickHandle,
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
  /deep/ .el-dialog {
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
  .name {
    padding: 10px;
    height: 70px;
    .btn {
      position: relative;
      margin-top: 10px;
      .el-button {
        position: absolute;
        right: 0px;
      }
    }
  }
}
</style>