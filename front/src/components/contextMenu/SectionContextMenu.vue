<template>
  <div>
    <div class="main">
      <div class="list" @click="showSection">断面图</div>
      <div class="list" @click="deleteSection">删除</div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { getSectionValue, delSection } from "@/api/request";
import { getCurrentProjectName, getCurrentProjectId } from "@/utils/project";
import { useStore } from "@/store";
import { Analyse } from "@/store/resourse/resourceState"
export default defineComponent({
  props: {
    contextData: {
      type: Object,
    },
  },
  emits: ["sendSectionValue"],
  setup(props, context) {
    const store = useStore();
    const showSection = async () => {
      console.log(props.contextData);
      const sectionValue = await getSectionValue(
        getCurrentProjectName() as string,
        (props.contextData as any).label,
        (props.contextData as any).selectDemName,
        parseInt((props.contextData as any).selectDemId)
      );
      if (sectionValue != null) {
        context.emit("sendSectionValue", sectionValue);
      }
    };
    const deleteSection = async () => {
        const analyse: Analyse = JSON.parse(JSON.stringify(store.state.resource.analyse))
        analyse.section.analysisResultList.forEach((item, index) => {
            if(item.id === (props.contextData as any).id) {
                analyse.section.analysisResultList.splice(index, 1)
            }
        })
        const layerDataList = store.state.resource.layerDataList
        await store.dispatch("setResource", {projectJsonBean: {layerDataList: layerDataList, analyse: analyse}, id: parseInt(getCurrentProjectId() as string)})
        await delSection(getCurrentProjectName() as string, (props.contextData as any).label, (props.contextData as any).selectDemName)
    }
    return {
      showSection,
      deleteSection
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  width: 100px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  .list {
    height: 30px;
    line-height: 30px;
    text-align: center;
    border: solid 0.5px;
    &:hover {
      cursor: pointer;
      background: #aca2a2;
    }
  }
  background: #d7d1d1;
}
</style>