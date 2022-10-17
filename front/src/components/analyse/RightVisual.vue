<template>
  <div class="right-visual">
    <div ref="container" class="container"></div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, nextTick, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import { getLayersInfo, getCoordinates, getGeoJson } from "@/api/request";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import router from "@/router";
import { notice } from "@/utils/notice";
export default defineComponent({
  emits: ["drawHandle"],
  setup(_, context) {
    const container = ref<HTMLElement>();
    let map: mapBoxGl.Map;

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
      // const jsonData: {
      //   projectId: string;
      //   geoJson: any;
      //   fileName: string;
      //   visualType: string;
      // } = {
      //   projectId: router.currentRoute.value.params.id as string,
      //   geoJson: e.features[0],
      //   fileName: "断面形态1",
      //   visualType: "geoJsonLine",
      // };
      // const data = await addDraw(jsonData);
      // if (data != null && (data as any).code === 0) {
      //   notice("success", "成功", "绘制断面!");
      //   await addMapLayer({
      //     id: data.data as string,
      //     visualType: "geoJsonLine",
      //     visualId: "",
      //   });

      // }
    };

    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        accessToken:
          "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ",
        style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
        center: [121.18, 31.723],
        zoom: 7,
      });
      map.on("load", async () => {
        await initLayers();
      });
    };

    const initLayers = async () => {
      const data = await getLayersInfo(
        router.currentRoute.value.params.id as string
      );
      if (data != null && (data as any).code === 0) {
        const temp: {
          id: string;
          fileName: string;
          visualType: string;
          visualId: string;
        }[] = data.data;
        for (let i = temp.length - 1; i >= 0; i--) {
          addMapLayer(temp[i]);
        }
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
          param.visualType === "lineVectorTile"
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
          param.visualType === "polygonVectorTile3D"
        ) {
          map.addSource(param.id, {
            type: "vector",
            tiles: [
              `http://localhost:8002/visual/getVectorTiles/${param.visualId}/{x}/{y}/{z}`,
            ],
          });
          map.addLayer({
            id: param.id,
            source: param.id,
            type: type,
            "source-layer": param.visualId,
          });
        } else if (param.visualType === "rasterTile") {
          map.addSource(param.id, {
            type: "raster",
            tiles: [
              `http://localhost:8002/visual/getRaster/${param.visualId}/{x}/{y}/{z}`,
            ],
          });
          map.addLayer({
            id: param.id,
            type: "raster",
            source: param.id,
          });
        } else if (
          param.visualType === "png" ||
          param.visualType === "movePng"
        ) {
          const coordinates = await getCoordinates(param.visualId);
          if (coordinates != null && (coordinates.data as any).code === 0) {
            map.addSource(param.id, {
              type: "image",
              url: `http://localhost:8002/visual/getPngResource/${param.visualId}`,
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
            map.addLayer({
              id: param.id,
              type: type,
              source: param.id,
            });
          }
        }
      }
    };

    const removeLayer = (id: string) => {
      if (map.getLayer(id) != undefined) {
        map.removeLayer(id);
        map.removeSource(id);
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
}
</style>