<template>
  <div class="map">
    <div ref="container" class="container"></div>
    <div class="controller">
      <div class="head">
        <div
          :class="active === 1 ? 'output active' : 'output'"
          @click="changeActive(1)"
        >
          控制台输出
        </div>
        <div
          :class="active === 2 ? 'log active' : 'log'"
          @click="changeActive(2)"
        >
          日志
        </div>
      </div>
      <div class="body">|</div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";

import DataSelect from "../riverbed/components/DataSelect.vue";
import { useStore } from "@/store";
import { Resource } from "@/store/resourse/resourceState";
import { mergeResource, computedLayers } from "@/utils/common";
import router from "@/router";
export default defineComponent({
  components: {
    DataSelect,
  },
  setup() {
    const container = ref<HTMLElement>();
    const flag = ref(false);
    const active = ref(1);
    const store = useStore();
    const dataSelectFlag = ref(false);

    const layers = computed(() => {
      return computedLayers();
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

    const map = ref<mapBoxGl.Map>();
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
        (map.value as mapBoxGl.Map).addControl(
          new mapBoxGl.FullscreenControl(),
          "top-right"
        );
        flag.value = true;
      });
    };

    const changeActive = (num: number) => {
      active.value = num;
    };

    const addLayer = (resource: Resource): void => {
      if (resource.type === "satellite") {
        map.value?.addSource(resource.id as string, {
          type: "raster",
          tiles: [
            `http://localhost:8002/analyticDataSet/getRaster/${resource.id}/{x}/{y}/{z}`,
          ],
        });
        map.value?.addLayer({
          id: resource.id as string,
          type: "raster",
          source: resource.id,
          paint: {
            "raster-opacity": 0.7,
          },
        });
      } else if (resource.type === "riverBed") {
        map.value?.addSource(resource.id as string, {
          type: "raster-dem",
          tiles: [
            `http://localhost:8002/analyticDataSet/getRaster/${resource.id}/{x}/{y}/{z}`,
          ],
        });
        map.value?.addLayer({
          id: resource.id as string,
          type: "hillshade",
          source: resource.id,
        });
      } else if (
        resource.type === "vector" &&
        resource.tableName != undefined &&
        resource.tableName != null &&
        resource.vectorType != undefined
      ) {
        map.value?.addSource(resource.id as string, {
          type: resource.type,
          tiles: [
            `http://localhost:8002/vector/${resource.tableName}/{x}/{y}/{z}`,
          ],
        });
        map.value?.addLayer({
          id: resource.id as string,
          type: resource.vectorType as "fill" | "circle" | "line",
          "source-layer": resource.tableName,
          source: resource.id as string,
        });
      } else if (
        resource.type === "geoJson" &&
        resource.geoJson != undefined &&
        resource.geoJson != null
      ) {
        let type: string = "";
        let paint: {} = {};
        switch (resource.geoJson.type) {
          case "Point" || "MultiPoint":
            type = "circle";
            paint = {
              "circle-color": "#AAC6EE",
              "circle-radius": 2,
            };
            break;
          case "LineString" || "MultiLineString":
            type = "line";
            paint = {
              "line-color": "#AAC6EE",
              "line-width": 3,
            };
            break;
          case "Polygon" || "MultiPolygon":
            type = "fill";
            paint = {
              "fill-color": "#AAC6EE",
              "fill-outline-color": "#E8C826",
            };
            break;
        }
        map.value?.addSource(resource.id as string, {
          type: "geojson",
          data: {
            type: "Feature",
            geometry: {
              type: resource.geoJson.type as
                | "Point"
                | "MultiPoint"
                | "LineString"
                | "MultiLineString"
                | "Polygon"
                | "MultiPolygon",
              coordinates: resource.geoJson.coordinates,
            },
            properties: {},
          },
        });
        map.value?.addLayer({
          id: resource.id as string,
          type: type as "fill" | "circle" | "line",
          source: resource.id as string,
          paint: paint,
        });
      } else if (
        resource.type === "raster" &&
        resource.id != undefined &&
        resource.id != null
      ) {
        map.value?.addSource(resource.id as string, {
          type: resource.type,
          tiles: [
            `http://localhost:8002/raster/getRaster/${resource.id}/{x}/{y}/{z}`,
          ],
        });
        map.value?.addLayer({
          id: resource.id as string,
          type: resource.type,
          source: resource.id as string,
        });
      }
    };

    const delLayer = (id: string, show: boolean) => {
      if (show && map.value?.getLayer(id)) {
        (map.value as mapBoxGl.Map).removeLayer(id);
        map.value?.removeSource(id);
      }
    };

    watch(layers, (newVal: any[], oldVal: any[]) => {
      const add: Resource[] = newVal.filter((item) => {
        let flag = true;
        for (let i = 0; i < oldVal.length; i++) {
          if (item.id === oldVal[i].id) {
            flag = false;
            break;
          }
        }
        if (flag) return item;
      });
      const del: Resource[] = oldVal.filter((item) => {
        let flag = true;
        for (let i = 0; i < newVal.length; i++) {
          if (item.id === newVal[i].id) {
            flag = false;
            break;
          }
        }
        if (flag) return item;
      });
      console.log(add);
      add.forEach((item, index) => {
        if (
          (map.value as mapBoxGl.Map).getLayer(
            add[add.length - 1 - index].id as string
          ) === undefined
        ) {
          if (flag.value) {
            addLayer(add[add.length - 1 - index]);
          } else {
            map.value?.on("load", () => {
              addLayer(add[add.length - 1 - index]);
            });
          }
        }
      });
      del.forEach((item) => {
        if (
          (map.value as mapBoxGl.Map).getLayer(item.id as string) != undefined
        ) {
          if (flag.value) {
            delLayer(item.id as string, true);
          } else {
            map.value?.on("load", () => {
              delLayer(item.id as string, true);
            });
          }
        }
      });
    });

    watch(
      () => router.currentRoute.value.path,
      () => {
        if (router.currentRoute.value.name === "project") {
          map.value?.setCenter([121.193496, 31.791046]);
          map.value?.setZoom(8);
        }
      }
    );

    watch(store.state.resource.layerSort, (newVal) => {
      console.log("newVal", newVal);
      if (newVal.type === "before" || newVal.type === "after") {
        newVal.layers.forEach((item, index) => {
          if (index < newVal.layers.length - 1) {
            map.value?.moveLayer(newVal.layers[index + 1], item);
          }
        });
      } else if (newVal.type === "" && newVal.layers.length === 0) {
        console.log(layers.value, flag.value);
        if (flag.value) {
          layers.value.forEach((item, index) => {
            if (index < layers.value.length - 1) {
              map.value?.moveLayer(layers.value[index + 1].id, item.id);
            }
          });
        } else {
          layers.value.forEach((item, index) => {
            if (index < layers.value.length - 1) {
              map.value?.on("load", () => {
                map.value?.moveLayer(layers.value[index + 1].id, item.id);
              });
            }
          });
        }
      }
    });

    watch(store.state.resource.selectedLayer, (newVal) => {
      if (newVal.flag) {
        map.value?.setLayoutProperty(newVal.id, "visibility", "none");
      } else {
        map.value?.setLayoutProperty(newVal.id, "visibility", "visible");
      }
    });

    const riverBed = (val: number) => {
      if (val === 0) {
        dataSelectFlag.value = false;
      } else if (val === 1) {
        dataSelectFlag.value = true;
      } else if (val === 2) {
        dataSelectFlag.value = false;
        store.commit("SET_DATA_SELECTS", [
          { id: "3", name: "199801_dem" },
          { id: "4", name: "200408_dem" },
          { id: "5", name: "200602_dem" },
        ]);
      }
    };

    onMounted(async () => {
      initMap();

      // const arr = mergeResource();
      // if (arr.length > 0) {
      //   arr.forEach((item) => {
      //     if (
      //       item.show &&
      //       (map.value as mapBoxGl.Map).getLayer(
      //         item.id as string
      //       ) === undefined
      //     )
      //       if (flag.value) {
      //         addLayer(item);
      //       } else {
      //         (map.value as mapBoxGl.Map).once("load", () => {
      //           addLayer(item);
      //         });
      //       }
      //   });
      // }
    });

    return {
      container,
      active,
      initMap,
      changeActive,
      riverBed,
      dataSelectFlag,
      map,
    };
  },
});
</script>

<style lang="scss" scoped>
.map {
  width: 100%;
  height: 100%;
  position: relative;
  .container {
    height: calc(100% - 200px);
    width: 100%;
    /deep/ .mapboxgl-ctrl-logo {
      display: none !important;
    }
  }
  .controller {
    height: 200px;
    .head {
      height: 40px;
      display: flex;
      line-height: 40px;
      .output {
        margin: 0 20px;
        cursor: pointer;
      }
      .log {
        cursor: pointer;
      }
      .active {
        border-bottom: solid 0.5px rgba($color: #21a2f1, $alpha: 0.8);
        color: rgba($color: #21a2f1, $alpha: 0.8);
      }
    }
    .body {
      margin: 5px 20px 0px;
    }
  }
  .drag {
    position: absolute;
    z-index: 99;
  }
  .tools {
    left: calc(100% - 180px);
    top: 3px;
  }
  .data-select {
    left: 5px;
    top: calc(800px - 60px);
  }
  .router-view {
    top: 3px;
    left: 5px;
  }
}
</style>