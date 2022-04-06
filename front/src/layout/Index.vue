<template>
  <div>
    <el-container>
      <el-aside width="auto">
        <left-menu></left-menu>
      </el-aside>
      <el-main>
        <nav-bar></nav-bar>
        <tag-bar/>
        <div class="main-app">
          <router-view />
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
import LeftMenu from "./components/LeftMenu.vue";
import NavBar from './components/NavBar.vue'
import TagBar from './components/TagBar.vue'
import { defineComponent, ref } from "vue";
export default defineComponent({
  components: {
    LeftMenu,
    NavBar,
    TagBar
  },
  setup() {
  },
});
</script>


<style lang="scss" scoped>
.el-main {
  padding: 0;
  .main-app {
    // background: #F2F7FF;
    min-height: calc(100% - 80px);
  }
}
</style>