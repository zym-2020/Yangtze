<template>
  <div class="main">
    <data-detail class="detail" :fileInfo="fileInfo"></data-detail>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import { addWatchCount, addBrowseHistory } from "@/api/request";
import DataDetail from "@/components/resourcePages/DataDetail.vue";
import router from "@/router";
export default defineComponent({
  components: { DataDetail },
  setup() {
    const fileInfo = computed(() => {
      return router.currentRoute.value.params.fileInfo;
    });

    onMounted(async () => {
      if (
        router.currentRoute.value.params.id != null &&
        router.currentRoute.value.params.id != undefined
      ) {
        addWatchCount(router.currentRoute.value.params.id as string);
        addBrowseHistory(router.currentRoute.value.params.id as string);
      }
    });

    return {
      fileInfo,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  width: 100%;
}
</style>