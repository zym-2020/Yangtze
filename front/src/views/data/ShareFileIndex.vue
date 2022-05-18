<template>
  <div class="share-file">
    <data-detail-header
      :name="fileInfo.name"
      @activeClick="activeClick"
    ></data-detail-header>
    <div class="main">
      <data-detail
        v-show="active === 1"
        class="detail"
        :fileInfo="fileInfo"
        :fileMeta="fileMeta"
      ></data-detail>
      <data-statistics v-show="active === 2"></data-statistics>
      <similar-data v-show="active === 3"></similar-data>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import {
  getFileInfoAndMeta,
  getFileMetaById,
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
    const fileInfo = ref<any>({});
    const fileMeta = ref<any>({});

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
      if (router.currentRoute.value.params.fileInfo === undefined) {
        const data = await getFileInfoAndMeta(
          router.currentRoute.value.params.id as string
        );
        if (data != null) {
          if ((data as any).code != 0) {
            router.replace({ path: "/404" });
          } else {
            fileInfo.value = data.data.fileInfo;
            fileMeta.value = data.data.fileMeta;
          }
        }
      } else {
        fileInfo.value = JSON.parse(
          router.currentRoute.value.params.fileInfo as string
        );
        const data = await getFileMetaById(fileInfo.value.meta);
        if (data != null) {
          if ((data as any).code === 0) {
            fileMeta.value = data.data;
          }
        }
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
  .main {
    width: 100%;
  }
}
</style>