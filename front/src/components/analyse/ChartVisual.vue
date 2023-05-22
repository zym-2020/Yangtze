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
    <flow-sand-z
      :visualId="chartInfo.visualId"
      v-if="chartInfo.visualType === 'flowSand_Z'"
    />
    <rate-and-direction
      :visualId="chartInfo.visualId"
      v-if="chartInfo.visualType === 'rateDirection'"
    />
    <salinity
      :visualId="chartInfo.visualId"
      v-if="chartInfo.visualType === 'salinity'"
    />
    <sand-content
      :visualId="chartInfo.visualId"
      v-if="chartInfo.visualType === 'sandContent'"
    />
    <suspension
      :visualId="chartInfo.visualId"
      v-if="chartInfo.visualType === 'suspension'"
    />
    <prediction-visual
      :id="chartInfo.id"
      v-if="chartInfo.visualType === 'prediction'"
    />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import SectionVisual from "@/components/visual/SectionVisual.vue";
import SectionContrast from "@/components/visual/SectionContrast.vue";
import SectionFlush from "@/components/visual/SectionFlush.vue";
import FlowSandZ from "@/components/visual/FlowSand_Z.vue";
import RateAndDirection from "@/components/visual/RateAndDirection.vue";
import Salinity from "@/components/visual/Salinity.vue";
import SandContent from "@/components/visual/SandContent.vue";
import Suspension from "@/components/visual/Suspension.vue";
import PredictionVisual from "@/components/visual/PredictionVisual.vue";

export default defineComponent({
  components: {
    SectionVisual,
    SectionContrast,
    SectionFlush,
    FlowSandZ,
    RateAndDirection,
    Salinity,
    SandContent,
    Suspension,
    PredictionVisual,
  },
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
