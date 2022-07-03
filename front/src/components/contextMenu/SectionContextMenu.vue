<template>
  <div>
    <div class="main">
      <div class="list" @click="showSection" disabled>断面图</div>
      <div class="list" @click="deleteSection">删除</div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getSectionValue } from "@/api/request";
import { useStore } from "@/store";
import { Analyse } from "@/store/resourse/resourceState";
import router from "@/router";
import { notice } from "@/utils/notice";
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
        (props.contextData as any).id,
        router.currentRoute.value.params.id as string
      );
      if (sectionValue != null) {
        context.emit("sendSectionValue", sectionValue);
      }
    };
    const deleteSection = async () => {
      
    };

    return {
      showSection,
      deleteSection,
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