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
    <router-view class="router-view" v-analyseDrag/>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import Tools from "@/components/tools/Index.vue";
export default defineComponent({
  components: {
    Tools,
  },
  setup() {
    const container = ref<HTMLElement>();
    const active = ref(1);
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
      tiles: ["http://localhost:8080/Yangtze/raster/getRaster/{x}/{y}/{z}"],
      bounds: [119.482547, 31.758138, 121.878795, 32.384769],
      maxzoom: 15,
      minzoom: 5,
    };
    const dem: AnySourceData = {
      type: "raster-dem",
      tiles: ["http://localhost:8080/Yangtze/raster/getRaster/{x}/{y}/{z}"],
      encoding: "terrarium",
    };

    const initMap = () => {
      const map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        style: {
          version: 8,
          sources: {
            tdtVec: tdtVec,
            txt: txt,
            // dem: dem,
            tif: tif,
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
            // {
            //   id: "dem",
            //   type: "hillshade",
            //   source: "dem",
            //   paint: {
            //     "hillshade-exaggeration": 0.5,
            //     // "hillshade-accent-color": "red",
            //     "hillshade-highlight-color": "blue"
            //   },
            // },
          ],
        },

        center: [121.193496, 31.791046],
        zoom: 8,
      });
    };

    const changeActive = (num: number) => {
      active.value = num;
    };

    onMounted(() => {
      initMap();
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