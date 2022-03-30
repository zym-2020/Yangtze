<template>
  <div>
    <el-menu
      class="el-menu-vertical-demo"
      :collapse="isCollapse"
    >
      <el-sub-menu
        v-for="(item, index) in routers"
        :key="index"
        :index="index + ''"
      >
        <template #title>
          <el-icon><location /></el-icon>
          <span>{{ item.meta.title }}</span>
        </template>
        <el-menu-item-group v-if="item.children">
          <el-menu-item :index="child.meta.title" v-for="(child, child_index) in item.children" :key="child_index" @click="click(item.path, child.path)">{{child.meta.title}}</el-menu-item>
        </el-menu-item-group>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from "vue";
import { useStore } from "@/store";
import router from "@/router"
import path from 'path'
export default defineComponent({
  setup() {
    const isCollapse = ref(false);
    const store = useStore();

    const routers = computed(() => {
      return store.state.permission.addRouters;
    });

    const click = (item: string, child: string) => {
      router.push(path.resolve(item, child))
    }

    return {
      isCollapse,
      routers,
      click
    };
  },
});
</script>
