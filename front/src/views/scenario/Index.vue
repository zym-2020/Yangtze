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
  setup() {
    onMounted(async () => {
      const data = await getAreaShip('9e42268de47b4f66a4f6c9f2a981fa0d', '0', "122311026,32444351-118355607,32444351-118355607,30608509-122311026,30608509")
      console.log(data)
    })
  },
});
</script>

<style lang="scss" scoped>

</style>