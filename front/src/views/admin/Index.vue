<template>
  <div class="admin">
    <left @nav="navHandle" />
    <div class="main">
      <admin-head></admin-head>
      <el-scrollbar>
        <router-view v-slot="{ Component }" v-if="route.meta.key === 'Admin'">
          <keep-alive>
            <component :is="Component" />
          </keep-alive>
        </router-view>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed } from "vue";
import Left from "@/components/admin/Left.vue";
import AdminHead from "@/components/admin/AdminHead.vue";
import router from "@/router";
export default defineComponent({
  components: {
    Left,
    AdminHead,
  },
  setup() {
    const route = computed(() => {
      return router.currentRoute.value;
    });

    const navHandle = (val: number) => {
      console.log(val);
      if (val === 0) {
        router.push({ name: "AdminData" });
      } else if (val === 1) {
        router.push({ name: "AdminSenario" });
      } else if (val === 2) {
        router.push({ name: "AdminProject" });
      } else if (val === 3) {
        router.push({ name: "AdminUser" });
      } else if (val === 4) {
        router.push({ name: "AdminAudit" });
      }
    };

    return {
      route,
      navHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin {
  height: 100%;
  display: flex;
  .main {
    width: calc(100% - 250px);
    background: #f6f8f9;
    .el-scrollbar {
      height: calc(100% - 80px);
      // height: 100%;
    }
  }
}
</style>