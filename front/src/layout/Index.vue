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
          <router-view class="view"/>
          <project/>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
import LeftMenu from "./components/LeftMenu.vue";
import NavBar from './components/NavBar.vue'
import TagBar from './components/TagBar.vue'
import Project from './components/Project.vue'
import { defineComponent } from "vue";
export default defineComponent({
  components: {
    LeftMenu,
    NavBar,
    TagBar,
    Project
  },
  setup() {
  },
});
</script>


<style lang="scss" scoped>
.el-main {
  padding: 0;
  .main-app {
    min-height: calc(100% - 80px);
    display: flex;
    .view {
      width: 100%
    }
    
  }
}
</style>