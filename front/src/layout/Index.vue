<template>
  <div>
    <el-container>
      <el-header>
        <header-component></header-component>
      </el-header>
      <el-main>
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
      </el-main>
    </el-container>
  </div>
</template>

<script lang="ts">
import HeaderComponent from "./components/Header.vue";
import { computed, defineComponent } from "vue";
import router from "@/router";
export default defineComponent({
  components: {
    HeaderComponent,
  },
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
.el-header {
  padding: 0;
  height: 60px;
}
.el-main {
  padding: 0;
  height: 100%;
}
</style>