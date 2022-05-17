<template>
  <div class="admin">
    <div class="left">
      <admin-left></admin-left>
    </div>
    <div class="admin-main">
      <el-scrollbar class="scroll">
        <router-view />
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import AdminLeft from "./components/AdminLeft.vue";
export default defineComponent({
  components: { AdminLeft },
  setup() {},
});
</script>

<style lang="scss" scoped>
.admin {
  display: flex;
  .left {
    width: 300px;
  }
  .admin-main {
    width: calc(100% - 300px);
    height: calc(100vh - 60px);
    .scroll {
      max-height: 100%;
    }
  }
}
</style>