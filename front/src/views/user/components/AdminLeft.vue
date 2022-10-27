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
    </el-menu>
  </div>
</template>


<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { useStore } from "@/store";

export default defineComponent({
  props: {
    flag: {
      type: Number,
    },
  },
  emits: ["nav"],
  setup(props, context) {
    const store = useStore();
    const showBadge = ref(true);
    const selectIndex = computed(() => {
      switch (props.flag) {
        case 1:
          return "1";
        case 2:
          return "2";
        case 3:
          return "3";
      }
    });

    const selectHandle = (index: string) => {
      switch (index) {
        case "1":
          context.emit("nav", 1);
          break;
        case "2":
          context.emit("nav", 2);
          break;
        case "3":
          context.emit("nav", 3);
          break;
      }
    };

    return {
      store,
      showBadge,
      selectHandle,
      selectIndex,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-left {
  height: calc(100vh - 63px);
  width: 100%;
  .el-menu {
    height: 100%;
  }
}
.item {
  width: 98%;
}
</style>
