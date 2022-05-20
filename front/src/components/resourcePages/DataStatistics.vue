<template>
  <div class="data-statistics">
    <div class="main">
      <el-divider content-position="left">数据下载情况</el-divider>
      <download-statistics :downloadData="tableData"></download-statistics>
      <el-divider content-position="left">数据访问情况</el-divider>
      <browse-statistics></browse-statistics>
      <el-divider content-position="left">数据更新情况</el-divider>
      <renew-statistics></renew-statistics>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { pageQueryDownloadHistory } from "@/api/request";
import BrowseStatistics from './components/BrowseStatistics.vue'
import router from "@/router";
import DownloadStatistics from "./components/DownloadStatistics.vue";
import RenewStatistics from './components/RenewStatistics.vue'
export default defineComponent({
  components: { DownloadStatistics, BrowseStatistics,RenewStatistics },
  setup() {
    const tableData = ref([]);

    onMounted(async () => {
      const data = await pageQueryDownloadHistory(
        10,
        0,
        router.currentRoute.value.params.id as string
      );
      if (data != null) {
        if ((data as any).code === 0) {
          tableData.value = data.data;
        }
      }
    });
    return {
      tableData,
    };
  },
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