<template>
  <div class="admin">
    <div class="left">
      <admin-left :flag="flag" @nav="navHandle"></admin-left>
    </div>
    <div class="admin-main">
      <router-view v-slot="{ Component }" v-if="route.meta.key === 'UserAdmin'">
        <keep-alive>
          <component :is="Component" />
        </keep-alive>
      </router-view>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref } from "vue";
import AdminLeft from "./components/AdminLeft.vue";
import router from "@/router";
export default defineComponent({
  components: {
    AdminLeft,
  },
  setup() {
    const flag = ref(1);

    const route = computed(() => {
      return router.currentRoute.value;
    });

    const navHandle = (val: number) => {
      console.log(val)
    };

    return {
      flag,
      navHandle,
      route,
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