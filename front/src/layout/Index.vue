<template>
  <div>
    <el-container>
      <el-header>
        <header-component></header-component>
      </el-header>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">

import HeaderComponent from './components/Header.vue'
import { defineComponent } from "vue";
export default defineComponent({
  components: {
    HeaderComponent
  },
  setup() {},
});
</script>


<style lang="scss" scoped>
.el-header {
  padding: 0;
  height: 60px;
}
.el-main {
  padding: 0;
  height: 100%;
}
</style>