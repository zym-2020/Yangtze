<template>
  <div>
    <div class="main">
      <div class="list" @click="contrastClick">断面对比</div>
      <div class="list">删除</div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { getSectionContrastValue } from "@/api/request";
import { getCurrentProjectName } from "@/utils/project";
export default defineComponent({
  props: {
    sectionContrastContextData: {
      type: Object,
    },
  },
  emits: ['sendSectionContrastValue'],
  setup(props, context) {
    const contrastClick = async () => {
      const data = await getSectionContrastValue(
        getCurrentProjectName() as string,
        (props.sectionContrastContextData as any).label
      );
      if (data != null) {
        context.emit('sendSectionContrastValue', data)
      }
    };

    return {
      contrastClick,
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