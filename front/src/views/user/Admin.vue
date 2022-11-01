<template>
  <div class="admin">
    <div class="left">
      <admin-left :flag="flag" @nav="navHandle"></admin-left>
    </div>
    <div class="admin-main">
      <resource-manage v-if="flag === 1" />
      <project-manage v-if="flag === 2"></project-manage>
      <audit-manage v-if="flag === 3"></audit-manage>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref } from "vue";
import AdminLeft from "./components/AdminLeft.vue";
import ResourceManage from "./views/ResourceManage.vue";
import ProjectManage from "./views/ProjectManage.vue";
import AuditManage from "./views/AuditManage.vue";
export default defineComponent({
  components: {
    AdminLeft,
    ResourceManage,
    ProjectManage,
    AuditManage,
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
  height: 100%;
  .left {
    width: 200px;
  }
  .admin-main {
    width: calc(100% - 200px);
    height: 100%;
  }
}
</style>