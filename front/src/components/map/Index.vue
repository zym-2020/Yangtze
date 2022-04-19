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
    <tools class="drag tools" v-drag></tools>
    <data-select class="drag data-select" v-if="dataSelectFlag"></data-select>
    <router-view
      class="drag router-view"
      v-analyseDrag
      @riverBed="riverBed"
      :map="map"
    />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import Tools from "@/components/tools/Index.vue";
import DataSelect from "../riverbed/components/DataSelect.vue";
import { useStore } from "@/store";
import { Resource, Analyse } from "@/store/resourse/resourceState";
import { mergeResource, watchAnalyse } from "@/utils/common";
export default defineComponent({
  components: {
    Tools,
    DataSelect,
  },
  setup() {
    const container = ref<HTMLElement>();
    const active = ref(1);
    const store = useStore();
    const dataSelectFlag = ref(false);
    const layerDataList = computed(() => {
      return store.state.resource.layerDataList;
    });
    const analyse = computed(() => {
      return store.state.resource.analyse;
    });
    const tdtVec: AnySourceData = {
      type: "raster",
      tiles: [
        // "http://t0.tianditu.gov.cn/DataServer?T=img_w&x={x}&y={y}&l={z}&tk=35a94ab5985969d0b93229c30db6abd6",
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
    const tif: AnySourceData = {
      type: "raster",
      tiles: ["http://localhost:8080/Yangtze/raster/getRaster/1/{x}/{y}/{z}"],
      bounds: [119.482547, 31.758138, 121.878795, 32.384769],
      maxzoom: 15,
      minzoom: 5,
    };
    const rasterDEM: AnySourceData = {
      type: "raster",
      tiles: ["http://localhost:8080/Yangtze/raster/getRaster/2/{x}/{y}/{z}"],
      bounds: [120.127027, 31.161315, 121.994353, 32.023517],
      maxzoom: 12,
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
            tif: tif,
            rasterDEM: rasterDEM,
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
            {
              id: "tif",
              type: "raster",
              source: "tif",
              paint: {
                "raster-opacity": 0.7,
              },
            },
            {
              id: "rasterDEM",
              type: "raster",
              source: "rasterDEM",
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
      });
    };
    const changeActive = (num: number) => {
      active.value = num;
    };

    const addLayer = (resource: Resource): void => {
      if (
        resource.type === "raster" &&
        resource.id != undefined &&
        resource.id != null
      ) {
        (map.value as mapBoxGl.Map).addLayer({
          id: resource.type + resource.id?.toString(),
          type: resource.type,
          source: {
            type: resource.type,
            tiles: [
              `http://localhost:8080/Yangtze/raster/getRaster/${resource.id}/{x}/{y}/{z}`,
            ],
          },
        });
      } else if (
        resource.type === "vector" &&
        resource.tableName != undefined &&
        resource.tableName != null &&
        resource.vectorType != undefined
      ) {
        (map.value as mapBoxGl.Map).addLayer({
          id: resource.type + resource.id?.toString(),
          type: resource.vectorType as "fill" | "circle" | "line",
          "source-layer": resource.tableName,
          source: {
            type: resource.type,
            tiles: [
              `http://localhost:8080/Yangtze/vector/${resource.tableName}/{x}/{y}/{z}`,
            ],
          },
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
        (map.value as mapBoxGl.Map).addLayer({
          id: resource.type + resource.id?.toString(),
          type: type as "fill" | "circle" | "line",
          source: {
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
          },
          paint: paint
        });
      }
    };
    const delLayer = (type: string, id: number, show: boolean) => {
      if (show) {
        (map.value as mapBoxGl.Map).removeLayer(type + id.toString());
      }
    };

    watch(layerDataList, (newVal: Resource[], oldVal: Resource[]) => {
      const add: Resource[] = newVal.filter((item) => {
        let flag = true;
        for (let i = 0; i < oldVal.length; i++) {
          if (item.id === oldVal[i].id && item.type === oldVal[i].type) {
            flag = false;
            break;
          }
        }
        if (flag) return item;
      });
      const del: Resource[] = oldVal.filter((item) => {
        let flag = true;
        for (let i = 0; i < newVal.length; i++) {
          if (item.id === newVal[i].id && item.type === newVal[i].type) {
            flag = false;
            break;
          }
        }
        if (flag) return item;
      });
      add.forEach((item) => {
        if (
          item.show &&
          (map.value as mapBoxGl.Map).getLayer(
            item.type + item.id?.toString()
          ) === undefined
        ) {
          if ((map.value as mapBoxGl.Map).loaded()) {
            addLayer(item);
          } else {
            (map.value as mapBoxGl.Map).on("load", () => {
              addLayer(item);
            });
          }
        }
      });

      del.forEach((item) => {
        delLayer(item.type, item.id as number, item.show as boolean);
      });
    });

    watch(analyse, (newVal: Analyse, oldVal: Analyse) => {
      watchAnalyse(
        map.value as mapBoxGl.Map,
        newVal,
        oldVal,
        addLayer,
        delLayer
      );
    });

    const riverBed = (val: number) => {
      if (val === 1) {
        dataSelectFlag.value = true;
      }
    };

    onMounted(async () => {
      initMap();
      const arr = mergeResource();
      if (arr.length > 0) {
        arr.forEach((item) => {
          if (
            item.show &&
            (map.value as mapBoxGl.Map).getLayer(
              item.type + item.id?.toString()
            ) === undefined
          )
            (map.value as mapBoxGl.Map).on("load", () => {
              addLayer(item);
            });
        });
      }
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
  position: relative;
  .container {
    height: 800px;
    /deep/ .mapboxgl-ctrl-logo {
      display: none !important;
    }
    // /deep/ .mapboxgl-ctrl-attrib-button {
    //   display: none !important;
    // }
  }
  .controller {
    height: calc(100% - 800px);
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