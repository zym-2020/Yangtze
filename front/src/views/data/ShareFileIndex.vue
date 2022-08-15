<template>
  <div class="share-file">
    <data-detail-header
      :name="fileInfo.name"
      :info="{ watch: fileInfo.watch, download: fileInfo.download }"
      @activeClick="activeClick"
    ></data-detail-header>
    <div class="main">
      <data-detail
        v-show="active === 1"
        class="detail"
        :fileInfo="fileInfo"
        :fileMeta="fileMeta"
      ></data-detail>
      <data-statistics v-if="active === 2"></data-statistics>
      <similar-data v-if="active === 3"></similar-data>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import {
  addWatchCount,
} from "@/api/request";
import DataDetailHeader from "@/components/page/DataDetailHeader.vue";
import DataDetail from "@/components/resourcePages/DataDetail.vue";
import DataStatistics from "@/components/resourcePages/DataStatistics.vue";
import SimilarData from "@/components/resourcePages/SimilarData.vue";
import router from "@/router";
export default defineComponent({
  components: { DataDetailHeader, DataDetail, DataStatistics, SimilarData },
  setup() {
    const active = ref(1);
    const fileInfo = computed(() => {
      return router.currentRoute.value.params.fileInfo
    });
    const fileMeta = computed(() => {
      return router.currentRoute.value.params.fileMeta
    });


    const activeClick = (val: number) => {
      active.value = val;
    };
    onMounted(async () => {
      if (
        router.currentRoute.value.params.id != null &&
        router.currentRoute.value.params.id != undefined
      ) {
        addWatchCount(router.currentRoute.value.params.id as string);
      }
    });

    return {
      fileInfo,
      fileMeta,
      active,
      activeClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.share-file {
  overflow-y:hidden ;
  .main {
    width: 100%;

  }
}
</style>