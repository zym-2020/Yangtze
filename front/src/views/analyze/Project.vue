<template>
  <div>
    <div class="project-main">
      <div class="tools">1</div>
      <div class="map"></div>
      <div class="layer">
        <project-layer :projectName="projectName"></project-layer>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import ProjectLayer from "@/components/layer/ProjectLayer.vue";
import { ResourceState } from "@/store/resourse/resourceState";
import router from "@/router";
import { useStore } from "@/store";
export default defineComponent({
  components: { ProjectLayer },
  setup() {
    const store = useStore();
    const projectName = ref('')
    const classify = (projectResult: ResourceState) => {
      store.commit("INIT", undefined);
      projectResult.layerDataList.forEach((item) => {
        store.commit("ADD_BASE_DATA", item);
      });
      store.commit("SET_ANALYSE", projectResult.analyse);
    };
    onMounted(() => {
      const result: ResourceState = JSON.parse(
        router.currentRoute.value.params.result as string
      );
      classify(result)
      projectName.value = router.currentRoute.value.params.project_name as string
      console.log(store.state.resource)
    });

    return {
        projectName
    }
  },
});
</script>

<style lang="scss" scoped>
.project-main {
  display: flex;
  .tools {
    width: 300px;
  }
  .map {
    width: calc(100% - 600px);
  }
  .layer {
    width: 300px;
  }
}
</style>