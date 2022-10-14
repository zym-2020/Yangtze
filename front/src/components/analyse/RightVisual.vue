<template>
  <div class="right-visual">
    <div ref="container" class="container"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, nextTick, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import { getLayersInfo, getCoordinates } from "@/api/request";
import router from "@/router";
import { notice } from "@/utils/notice";
export default defineComponent({
  setup() {
    const container = ref<HTMLElement>();
    let map: mapBoxGl.Map;

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