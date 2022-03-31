<template>
  <div>
    <el-container>
      <el-aside width="auto">
        <left-menu :isCollapse="isCollapse"></left-menu>
      </el-aside>
      <el-main>
        <nav-bar @hamburger-click="hamburgerClick"></nav-bar>
        <tag-bar/>
        <router-view />
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
    const isCollapse = ref(false)
    const hamburgerClick = (flag: boolean) => {
      isCollapse.value = flag
    }

    return {
      hamburgerClick,
      isCollapse
    }
  },
});
</script>


<style lang="scss" scoped>
.el-main {
  padding: 0;
}
</style>