<template>
  <div class="main">
    <div class="analyseHead">河床演变分析</div>
    <div class="image">
      <img src="/river_bed_img/TinEditingTinLineAdd32.png" alt="" />
      <img src="/river_bed_img/3DAnalystInterpolateLine32.png" alt="" />
      <img src="/river_bed_img/MapSendBehind32.png" alt="" />
      <img src="/river_bed_img/LayerServiceMap32.png" alt="" />
      <img src="/river_bed_img/3DAnalystInterpolatePolygon32.png" alt="" />
      <img src="/river_bed_img/RasterImageAnalysisPanSharpen32.png" alt="" />
      <img src="/river_bed_img/RasterImageAnalysisOrthoRectify32.png" alt="" />
      <img src="/river_bed_img/MapPackageMPKFile32.png" alt="" />
      <img src="/river_bed_img/Surface32.png" alt="" />
      <img src="/river_bed_img/SpatialAnalystContourTool32.png" alt="" />
      <img src="/river_bed_img/3DAnalystCreateSteepestPath32.png" alt="" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import { getCurrentProjectId } from '@/utils/project'
export default defineComponent({
  setup() {
       
  },
});
</script>

<style lang="scss" scoped>
.main {
  border: solid 1px #888888;
  padding: 1px;
  background: #fafafa;
  .analyseHead {
    background: #a0a0a0;
    line-height: 25px;
    height: 25px;
    &:hover {
      cursor: move;
    }
  }
  .image {
    img {
      margin-right: 10px;
      margin-top: 5px;
      &:hover {
        cursor: pointer;
      }
    }
  }
}
</style>
