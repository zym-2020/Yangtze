<template>
  <div>
    <div class="project-main">
      <div class="tools">
        <left-tools></left-tools>
      </div>
      <div class="main-map">
        <main-map></main-map>
      </div>
      <div class="layer">
        <project-layer :projectName="projectName"></project-layer>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch } from "vue";
import LeftTools from "@/components/tools/LeftTools.vue";
import ProjectLayer from "@/components/layer/ProjectLayer.vue";
import MainMap from "@/components/map/Index.vue";
import { ResourceState } from "@/store/resourse/resourceState";
import router from "@/router";
import { useStore } from "@/store";
import { findProjectById } from "@/api/request";
export default defineComponent({
  components: { ProjectLayer, MainMap, LeftTools },
  setup() {
    const projectResult = ref<ResourceState>();
    const store = useStore();
    const projectName = ref("");

    const classify = (projectResult: ResourceState) => {
      store.commit("INIT", undefined);
      projectResult.layerDataList.forEach((item) => {
        store.commit("ADD_BASE_DATA", item);
      });
      store.commit("SET_ANALYSE", projectResult.analyse);
    };

    watch(
      () => router.currentRoute.value.path,
      async () => {
        console.log("Project.watch")
        if (router.currentRoute.value.name === "project") {
          const data = await findProjectById(
            router.currentRoute.value.params.id as string
          );
          if (data != null) {
            if (data.data === null) {
              router.replace({ path: "/404" });
            } else {
              projectResult.value = JSON.parse((data.data as any).result);
              classify(projectResult.value as ResourceState);
              projectName.value = (data.data as any).project_name;
            }
          }
        } else {
          store.commit("INIT", undefined);
        }
      }
    ),
      onMounted(async () => {
        if (router.currentRoute.value.params.result === undefined) {
          console.log("Project.if")
          const data = await findProjectById(
            router.currentRoute.value.params.id as string
          );
          if (data != null) {
            if (data.data === null) {
              router.replace({ path: "/404" });
            } else {
              projectResult.value = JSON.parse((data.data as any).result);
              classify(projectResult.value as ResourceState);
              projectName.value = (data.data as any).project_name;
            }
          }
        } else {
          console.log("Project.else")
          projectResult.value = JSON.parse(
            router.currentRoute.value.params.result as string
          );
          classify(projectResult.value as ResourceState);
          projectName.value = router.currentRoute.value.params.name as string;
        }
      });

    return {
      projectName,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-main {
  display: flex;
  .tools {
    width: 300px;
  }
  .main-map {
    width: calc(100% - 600px);
  }
  .layer {
    width: 300px;
  }
}
</style>