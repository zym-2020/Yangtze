<template>
  <div class="admin">
    <div class="left">
      <admin-left :flag="flag" @nav="navHandle"></admin-left>
    </div>
    <div class="admin-main">
      <el-scrollbar class="scroll">
        <resource-manage v-show="flag === 1" />
        <scenario-manage v-show="flag === 2"></scenario-manage>
        <project-manage v-show="flag === 3"></project-manage>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref } from "vue";
import AdminLeft from "./components/AdminLeft.vue";
import ResourceManage from "./views/ResourceManage.vue";
import ProjectManage from "./views/ProjectManage.vue";
import ScenarioManage from "./views/ScenarioManage.vue";
export default defineComponent({
  components: {
    AdminLeft,
    ResourceManage,
    ProjectManage,
    ScenarioManage,
  },
  setup() {
    const flag = ref(1);

    const navHandle = (val: number) => {
      flag.value = val;
    };

    return {
      flag,
      navHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin {
  display: flex;

  .left {
    width: 200px;
  }
  .admin-main {
    width: calc(100% - 200px);
    height: calc(100vh - 63px);
    .scroll {
      height: 100%;
      /deep/ .el-scrollbar__view {
        height: 100%;
      }
    }
  }
}
</style>