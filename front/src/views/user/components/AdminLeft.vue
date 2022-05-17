<template>
  <div class="admin-left">
    <el-menu
      class="el-menu-vertical-demo"
      :default-active="selectIndex"
      @select="selectHandle"
    >
      <el-menu-item index="1">
        <el-icon><MessageBox /></el-icon>
        <span>资源门户管理</span>
      </el-menu-item>

      <el-menu-item index="2">
        <el-icon><Film /></el-icon>
        <span>一张图管理</span>
      </el-menu-item>
      <el-menu-item index="3">
        <el-icon><Menu /></el-icon>
        <span>项目管理</span>
      </el-menu-item>
      <el-menu-item index="4">
        <el-icon><ChatLineRound /></el-icon>
        <span>消息</span>
      </el-menu-item>
    </el-menu>
  </div>
</template>


<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import router from "@/router";
export default defineComponent({
  setup() {
    const selectIndex = computed(() => {
      switch (router.currentRoute.value.path) {
        case "/user/admin/resource":
          return "1";
        case "/user/admin/scenario":
          return "2";
        case "/user/admin/project":
          return "3";
        case "/user/admin/message":
          return "4";
      }
    });
    const selectHandle = (index: string) => {
      switch (index) {
        case "1":
          router.push({ path: "resource" });
          break;
        case "2":
          router.push({ path: "scenario" });
          break;
        case "3":
          router.push({ path: "project" });
          break;
        case "4":
          router.push({ path: "message" });
          break;
      }
    };

    return {
      selectHandle,
      selectIndex
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-left {
  margin-top: 1px;
  height: calc(100vh - 61px);
  width: 100%;
  .el-menu {
    height: 100%;
  }
}
</style>
