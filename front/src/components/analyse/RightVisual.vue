<template>
  <div class="right-visual">
    <div ref="container" class="container"></div>
  </div>
</template>

<script lang="ts">
import { defineComponent, nextTick, onMounted, ref } from "vue";
import mapBoxGl, { AnySourceData } from "mapbox-gl";
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