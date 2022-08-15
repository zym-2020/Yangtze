<template ></template>



<script lang="ts">
import { defineComponent } from "vue";
import images from "./components/Images.vue";
import LocationMap from "../scenePart/LocationMap.vue";
import centerVisualMap from "../scenePart/centerVisualMap.vue";
export default defineComponent({
  components:{images,LocationMap,centerVisualMap},
  setup() {},
});
</script>

<style lang="scss" scoped>
</style>