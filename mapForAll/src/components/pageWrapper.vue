<template>
  <div class="page-wrapper">
    <OverView />
    <CarouselGallery />
    <UsageScene
      v-for="scene in scenes"
      :key="scene.index"
      :seq="scene.index"
      :pos="scene.pos"
    />
    <InvitePart />
  </div>
</template>

<script>
// page part assembled except navbar
import { ref } from "vue";
import OverView from "./overview.vue";
import CarouselGallery from "./carousel.vue";
import UsageScene from "./usageScene.vue";
import InvitePart from "./invite.vue";

export default {
  name: "pageWrapper",
  components: {
    OverView,
    CarouselGallery,
    UsageScene,
    InvitePart,
  },
  setup() {
    // 发送至各个部分介绍 以方便各个组件添加相应图片
    const scenes = ref([
      { index: "1", pos: "left" },
      { index: "2", pos: "right" },
      { index: "3", pos: "left" },
    ]);
    return { scenes };
  },
};
</script>

<style lang='scss'>
div.page-wrapper {
  height: 100%;
  width: 100%;
  background-color: rgb(5, 9, 39);
}
</style>