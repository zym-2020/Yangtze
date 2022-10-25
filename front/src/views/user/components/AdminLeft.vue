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

      <!-- <el-badge :is-dot="realBadge" class="item">
        <el-menu-item index="4" @click="changeShowBadge">
          <el-icon><ChatLineRound /></el-icon>
          消息
        </el-menu-item>
      </el-badge> -->
      <el-menu-item index="4" @click="changeShowBadge">
        <el-icon><ChatLineRound /></el-icon>
        消息
      </el-menu-item>
    </el-menu>
  </div>
</template>


<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { useStore } from "@/store";
import router from "@/router";
import { useStyle } from "naive-ui/es/_mixins";
import { CountReply, CountUserReply } from "@/api/request";
export default defineComponent({
  setup() {
    const store = useStore();
    const showBadge = ref(true);
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
    const realBadge = async () => {
      return await CountReply();
    };
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
    function changeShowBadge() {
      store.commit("SET_MESSAGE_BADGE", true);
    }

    return {
      store,
      showBadge,
      selectHandle,
      selectIndex,
      changeShowBadge,
      realBadge,
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
.item {
  width: 98%;
}
</style>
