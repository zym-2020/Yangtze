<template>
  <div class="chart-visual">
    <section-visual
      :chartInfo="chartInfo"
      v-if="chartInfo.visualType === 'section'"
    ></section-visual>
    <section-contrast
      :chartInfo="chartInfo"
      v-if="chartInfo.visualType === 'sectionContrast'"
    />
    <section-flush
      :chartInfo="chartInfo"
      v-if="chartInfo.visualType === 'sectionFlush'"
    />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import SectionVisual from "@/components/visual/SectionVisual.vue";
import SectionContrast from "@/components/visual/SectionContrast.vue";
import SectionFlush from "@/components/visual/SectionFlush.vue";
export default defineComponent({
  components: { SectionVisual, SectionContrast, SectionFlush },
  props: {
    chartVisualInfo: {
      type: Object,
    },
  },
  setup(props) {
    const chartInfo = computed(() => {
      return props.chartVisualInfo;
    });

    onMounted(() => {
      console.log(chartInfo.value);
    });

    return {
      chartInfo,
    };
  },
});
</script>


<style lang="scss" scoped>
</style>
