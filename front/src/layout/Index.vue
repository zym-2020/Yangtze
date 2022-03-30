<template>
  <div>
    <el-container>
      <el-aside width="250px">
        <left-menu></left-menu>
      </el-aside>
      <el-main><router-view /></el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
import LeftMenu from "./components/LeftMenu.vue";

import { defineComponent } from "vue";
export default defineComponent({
  components: {
    LeftMenu,
  },
  setup() {},
});
</script>


<style lang="scss" scoped>
</style>