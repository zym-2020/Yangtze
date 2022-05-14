<template>
  <div class="scenario-main">
    <page-header :pageTitle="'一张图'"></page-header>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import PageHeader from "@/components/page/PageHeader.vue";
export default defineComponent({
  components: {
    PageHeader,
  },
  setup() {},
});
</script>

<style lang="scss" scoped>
.scenario-main {
  height: 200px;
}
</style>