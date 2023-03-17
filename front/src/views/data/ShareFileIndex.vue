<template>
  <div class="main">
    <data-detail class="detail" :fileInfo="fileInfo"></data-detail>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import DataDetail from "@/components/data/DataDetail.vue";
import router from "@/router";
export default defineComponent({
  components: { DataDetail },
  setup() {
    const fileInfo = computed(() => {
      return router.currentRoute.value.params.fileInfo;
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