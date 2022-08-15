<template>
  <SceneWrapper />
</template>

<script lang="ts">
import { defineComponent, onMounted } from "vue";
import { getAreaShip } from '@/api/request'
import SceneWrapper from "@/components/pageWrapper/sceneWrapper.vue";
export default defineComponent({
  components: {
    SceneWrapper,
  },
  setup() {  },
});
</script>

<style lang="scss" scoped>

</style>