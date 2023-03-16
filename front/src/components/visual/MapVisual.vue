<template>
  <div class="map-visual">
    <el-row
      :gutter="20"
      justify="center"
      style="margin-bottom: 20px"
      v-if="movePngArray.length > 0"
    >
      <el-col :span="8" style="text-align: center">
        <el-card
          class="video"
          shadow="always"
          style="margin-top: 30px; margin-bottom: 10px"
          @click="changeRaster('left')"
        >
          <div class="body">
            <div class="text" style="margin-left: 20px">
              <strong>{{ movePngArray[leftIndex].name }}</strong>
            </div>
            <div class="icon icon-left">
              <el-icon><ArrowLeftBold /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <el-card
          shadow="always"
          style="
            margin-top: 10px;
            margin-bottom: 10px;
            background-color: lightgrey;
          "
        >
          <div class="text">
            <strong>{{ movePngArray[centerIndex].name }}</strong>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <el-card
          class="video"
          shadow="always"
          style="margin-top: 30px; margin-bottom: 10px"
          @click="changeRaster('right')"
        >
          <div class="body">
            <div class="text">
              <strong>{{ movePngArray[rightIndex].name }}</strong>
            </div>
            <div class="icon icon-right">
              <el-icon><ArrowRightBold /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <div ref="container" class="container"></div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
import { prefix } from "@/prefix";
export default defineComponent({
  props: {
    shpArray: {
      type: Array,
    },
    pngArray: {
      type: Array,
    },
    rasterTileArray: {
      type: Array,
    },
    movePngArray: {
      type: Array,
    },
  },

  setup(props) {
    const container = ref<HTMLElement>();
    let map: mapBoxGl.Map;

    let cneter: [number, number];
    let zoom: number;

    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        // 这是osm的style
        // style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",

        // 下方是天地图的style
        style: {
          version: 8,
          sources: {
            tdtVec: {
              type: "raster",
              tiles: [
                "http://t0.tianditu.com/vec_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=vec&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
              ],
              tileSize: 256,
            },
            txt: {
              type: "raster",
              tiles: [
                "http://t0.tianditu.com/cva_w/wmts?tk=35a94ab5985969d0b93229c30db6abd6&SERVICE=WMTS&REQUEST=GetTile&VERSION=1.0.0&LAYER=cva&STYLE=default&TILEMATRIXSET=w&TILEMATRIX={z}&TILEROW={y}&TILECOL={x}&FORMAT=tiles",
              ],
              tileSize: 256,
            },
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
        center: cneter,
        zoom: zoom,
        accessToken:
          "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ",
      });
      map.on("load", () => {
        if (props.rasterTileArray != undefined) {
          (
            props.rasterTileArray as {
              visualId: string;
              view: { zoom: number; center: number[] };
            }[]
          ).forEach((item) => {
            map.addSource(item.visualId, {
              type: "raster",
              tiles: [`${prefix}visual/getRaster/${item.visualId}/{x}/{y}/{z}`],
            });
            map.addLayer({
              id: item.visualId,
              type: "raster",
              source: item.visualId,
            });
          });
        }

        if (props.pngArray != undefined) {
          (
            props.pngArray as {
              visualId: string;
              coordinates: number[][];
              view: { zoom: number; center: number[] };
            }[]
          ).forEach((item) => {
            map.addSource(item.visualId, {
              type: "image",
              url: `${prefix}visual/getPngResource/${item.visualId}`,
              coordinates: item.coordinates,
            });
            map.addLayer({
              id: item.visualId,
              type: "raster",
              source: item.visualId,
            });
          });
        }

        if (movePngArray.value.length > 0) {
          map.addSource(movePngArray.value[0].visualId, {
            type: "image",
            url: `${prefix}visual/getPngResource/${movePngArray.value[0].visualId}`,
            coordinates: movePngArray.value[0].coordinates,
          });
          map.addLayer({
            id: movePngArray.value[0].visualId,
            type: "raster",
            source: movePngArray.value[0].visualId,
          });
        }

        if (props.shpArray != undefined) {
          (
            props.shpArray as {
              visualId: string;
              type: "line" | "circle" | "fill";
              view: { zoom: number; center: number[] };
            }[]
          ).forEach((item) => {
            map.addSource(item.visualId, {
              type: "vector",
              tiles: [
                `${prefix}visual/getVectorTiles/${item.visualId}/{x}/{y}/{z}`,
              ],
            });
            map.addLayer({
              id: item.visualId,
              source: item.visualId,
              type: item.type,
              "source-layer": item.visualId,
            });
          });
        }
      });
    };

    const movePngArray = computed<any[]>(() => {
      return props.movePngArray as any[];
    });

    const centerIndex = ref(0);

    const leftIndex = computed(() => {
      return (
        (centerIndex.value - 1 + (movePngArray.value as any[]).length) %
        (movePngArray.value as any[]).length
      );
    });
    const rightIndex = computed(() => {
      return (centerIndex.value + 1) % (movePngArray.value as any[]).length;
    });

    const changeRaster = (handle: string) => {
      const temp = centerIndex.value;
      if (handle === "left") {
        centerIndex.value =
          (centerIndex.value - 1 + (movePngArray.value as string[]).length) %
          (movePngArray.value as string[]).length;
      } else {
        centerIndex.value =
          (centerIndex.value + 1) % (movePngArray.value as string[]).length;
      }
      map.addSource(movePngArray.value[centerIndex.value].visualId, {
        type: "image",
        url: `${prefix}visual/getPngResource/${
          movePngArray.value[centerIndex.value].visualId
        }`,
        coordinates: movePngArray.value[centerIndex.value].coordinates,
      });
      map.addLayer({
        id: movePngArray.value[centerIndex.value].visualId,
        type: "raster",
        source: movePngArray.value[centerIndex.value].visualId,
      });
      map.setZoom(movePngArray.value[centerIndex.value].view.zoom);
      map.setCenter(movePngArray.value[centerIndex.value].view.center);
      map.removeLayer(movePngArray.value[temp].visualId);
      map.removeSource(movePngArray.value[temp].visualId);
    };

    onMounted(() => {
      const arr = [
        ...(props.movePngArray as any[]),
        ...(props.pngArray as any[]),
        ...(props.rasterTileArray as any[]),
        ...(props.shpArray as any[]),
      ];
      for (let i = 0; i < arr.length; i++) {
        cneter = arr[i].view.center;
        zoom = arr[i].view.zoom;
      }
      initMap();
    });

    return {
      container,
      movePngArray,
      changeRaster,
      centerIndex,
      leftIndex,
      rightIndex,
    };
  },
});
</script>

<style lang="scss" scoped>
.map-visual {
  .el-card {
    cursor: pointer;
  }
  .body {
    width: 100%;
    position: relative;
    .icon {
      position: absolute;
      top: 2px;
      height: 20px;
      width: 20px;
    }
    .icon-right {
      right: 0px;
    }
    .icon-left {
      left: 0px;
    }
  }
  .text {
    width: calc(100% - 20px);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
  }
  .container {
    height: 500px;
    width: 100%;
  }
}
</style>