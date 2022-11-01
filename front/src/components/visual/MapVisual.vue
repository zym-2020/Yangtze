<template>
  <div ref="container" class="container"></div>
</template>

<script lang="ts">
import { defineComponent, onBeforeUnmount, onMounted, ref } from "vue";
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
    let timeout: any;
    const initMap = () => {
      map = new mapBoxGl.Map({
        container: container.value as HTMLElement,
        style: "mapbox://styles/16651699376/ckmpu8kuk0h8r17msqpz351vf",
        center: [121.18, 31.723],
        zoom: 8,
        accessToken:
          "pk.eyJ1IjoiMTY2NTE2OTkzNzYiLCJhIjoiY2ttMDh5amJpMHE2dzJ3cTd5eWZsMGQxZyJ9.XErH3kSOuRC_OWXWCpDLkQ",
      });
      map.on("load", () => {
        if (props.rasterTileArray != undefined) {
          (props.rasterTileArray as string[]).forEach((item) => {
            map.addSource(item, {
              type: "raster",
              tiles: [`${prefix}visual/getRaster/${item}/{x}/{y}/{z}`],
            });
            map.addLayer({
              id: item,
              type: "raster",
              source: item,
            });
          });
        }

        if (props.pngArray != undefined) {
          (
            props.pngArray as { visualId: string; coordinates: number[][] }[]
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

        if (props.movePngArray != undefined) {
          (
            props.movePngArray as {
              visualId: string;
              coordinates: number[][];
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
          let count = 0;
          function handle() {
            timeout = setTimeout(() => {
              map.moveLayer(
                (
                  props.movePngArray as {
                    visualId: string;
                    coordinates: number[][];
                  }[]
                )[count].visualId
              );
              count =
                (count + 1) %
                (
                  props.movePngArray as {
                    visualId: string;
                    coordinates: number[][];
                  }[]
                ).length;
              console.log("handle");
              handle();
            }, 1500);
          }
          if (props.movePngArray.length != 0) {
            handle();
          }
        }

        if (props.shpArray != undefined) {
          (
            props.shpArray as {
              visualId: string;
              type: "line" | "circle" | "fill";
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

    onMounted(() => {
      initMap();
    });

    onBeforeUnmount(() => {
      clearTimeout(timeout);
    });

    return {
      container,
    };
  },
});
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
  width: 100%;
}
</style>