<template>
  <div class="data-statistics">
    <div class="main">
      <el-divider content-position="left">数据下载情况</el-divider>
      <download-statistics></download-statistics>
      <el-divider content-position="left">数据访问情况</el-divider>
      <browse-statistics></browse-statistics>
    </div>
  </div>
</template>



<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import BrowseStatistics from "./components/BrowseStatistics.vue";
import DownloadStatistics from "./components/DownloadStatistics.vue";
import TideStatistics from "./components/TideStatistics.vue";
import DownloadSta2 from "./components/DownSta2.vue";

export default defineComponent({
  components: {
    DownloadStatistics,
    BrowseStatistics,
    DownloadSta2,
    TideStatistics,
  },
  setup() {},
});
</script>

<style lang="scss" scoped>
.data-statistics {
  width: 100%;
  .main {
    width: 1250px;
    margin: 0 auto;
    .el-divider {
      margin-top: 40px;
      /deep/ .el-divider__text {
        font-size: 18px;
      }
    }
  }
}
</style>