<template>
  <div>
    <el-scrollbar height="100vh">
      <el-menu class="el-menu-vertical-demo" :default-active="active">
        <el-sub-menu
          v-for="(item, index) in routers"
          :key="index"
          :index="index + ''"
        >
          <template #title>
            <svg style="width: 20px; height: 20px; margin-right: 10px">
              <use :xlink:href="item.meta.icon"></use>
            </svg>
            <span>{{ item.meta.title }}</span>
          </template>
          <el-menu-item-group v-if="item.children">
            <el-menu-item
              :index="child.meta.title"
              v-for="(child, child_index) in item.children"
              :key="child_index"
              @click="click(item.path, child.path)"
              >{{ child.meta.title }}</el-menu-item
            >
          </el-menu-item-group>
        </el-sub-menu>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, watch, ref } from "vue";
import { useStore } from "@/store";
import router from "@/router";
import path from "path";
export default defineComponent({
  setup() {
    const store = useStore();
    const active = ref("");
    const routers = computed(() => {
      let temp: any[] = [];
      store.state.permission.addRouters.forEach((item) => {
        if (item.path != "/:catchAll(.*)") {
          temp.push(item);
        }
      });
      return temp;
    });

    const click = (item: string, child: string) => {
      router.push(path.resolve(item, child));
    };

    watch(router.currentRoute, (newRouter) => {
      active.value = newRouter.meta.title as string;
    });

    return {
      routers,
      click,
      active,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 300px;
}
.el-menu-vertical-demo {
  min-height: 100vh;
}
</style>
