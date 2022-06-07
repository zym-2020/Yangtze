<template>
  <div class="admin">
    <div class="left">
      <admin-left></admin-left>
    </div>
    <div class="admin-main">
      <el-scrollbar class="scroll">
        <!-- <router-view /> -->
        <router-view v-slot="{ Component }">
          <keep-alive>
            <component
              :is="Component"
              v-if="route.meta.keepAlive"
              :key="route.path"
            />
          </keep-alive>
          <component
            :is="Component"
            v-if="!route.meta.keepAlive"
            :key="route.path"
          />
        </router-view>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from "vue";
import AdminLeft from "./components/AdminLeft.vue";
import router from '@/router'
export default defineComponent({
  components: { AdminLeft },
  setup() {
    const route = computed(() => {
      return router.currentRoute.value;
    });

    return {
      route,
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
    height: calc(100vh - 60px);
    .scroll {
      max-height: 100%;
    }
  }
}
</style>