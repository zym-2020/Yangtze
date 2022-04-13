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
    <tools class="drag" v-drag></tools>
    <router-view class="router-view" v-analyseDrag />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import Tools from "@/components/tools/Index.vue";
import { useStore } from "@/store";
import { Resource } from "@/store/resourse/resourceState";
import { uuid } from "@/utils/common";
// import { getResult } from '@/api/request'
export default defineComponent({
  components: {
    Tools,
  },
  setup() {
    const container = ref<HTMLElement>();
    const active = ref(1);
    const store = useStore();
    const underlying = computed(() => {
      return store.state.resource.underlying;
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

    let map: mapBoxGl.Map;
    const initMap = () => {
      map = new mapBoxGl.Map({
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

        center: [121.193496, 31.791046],
        zoom: 8,
      });
    };
    const changeActive = (num: number) => {
      active.value = num;
    };

    const addLayer = (
      type: string,
      tableName?: string,
      id?: number,
      vectorType?: "fill" | "circle" | "line"
    ) => {
      const uid = uuid();
      if (type === "raster" && id != undefined && id != null) {
        map.addSource(uid, {
          type: type,
          tiles: [
            `http://localhost:8080/Yangtze/raster/getRaster/${id}/{x}/{y}/{z}`,
          ],
        });
        map.addLayer({
          id: uuid(),
          source: uid,
          type: type,
        });
      } else if (
        type === "vector" &&
        tableName != undefined &&
        tableName != null &&
        vectorType != undefined
      ) {
        map.addSource(uid, {
          type: type,
          tiles: [
            `http://localhost:8080/Yangtze/vector/${tableName}/{x}/{y}/{z}`,
          ],
        });
        map.addLayer({
          id: type + id?.toString(),
          source: uid,
          type: vectorType,
          "source-layer": tableName,
        });
      }
    };
    const delLayer = (type: string, id: number, show: boolean) => {
      if(show) {
        map.removeLayer(type + id.toString())
      }
    };

    watch(underlying, (newVal: Resource[], oldVal: Resource[]) => {
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
          map.getLayer(item.type + item.id.toString()) === undefined
        )
          addLayer(
            item.type,
            item.tableName,
            item.id,
            item.vectorType as "fill" | "circle" | "line"
          );
      });
      
      del.forEach(item => {
        delLayer(item.type, item.id, item.show as boolean)
      })
    });

    watch(analyse, (newVal: Resource[], oldVal: Resource[]) => {
      console.log(newVal);
      console.log(oldVal);
    });

    onMounted(async () => {
      initMap();
      const arr = store.state.resource.underlying.concat(store.state.resource.analyse)
      if (arr.length > 0) {
        store.state.resource.underlying.forEach((item) => {
          if (
            item.show &&
            map.getLayer(item.type + item.id.toString()) === undefined
          )
            map.on("load", () => {
              addLayer(
                item.type,
                item.tableName,
                item.id,
                item.vectorType as "fill" | "circle" | "line"
              );
            });
        });
      }
    });

    return {
      container,
      active,
      initMap,
      changeActive,
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
    /deep/ .mapboxgl-ctrl-attrib-button {
      display: none !important;
    }
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
    left: calc(100% - 129px);
    top: 3px;
  }
  .router-view {
    position: absolute;
    z-index: 99;
    top: 3px;
    left: 5px;
  }
}
</style>