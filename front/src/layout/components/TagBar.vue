<template>
  <div class="tag">
    <el-scrollbar>
      <div class="scroll">
        <el-tag :type="active == -1 ? 'success' : 'info'" @click="toHome">
          首页
        </el-tag>
        <el-tag
          :type="active == index ? 'success' : 'info'"
          v-for="(item, index) in views"
          :key="index"
          :closable="true"
          @close="closeHandle(item.path)"
          @click="clickHandle(item.path)"
          >{{ item.title }}</el-tag
        >
      </div>
    </el-scrollbar>
  </div>
</template>


<script lang="ts">
import { defineComponent, computed, watch, ref, onMounted } from "vue";
import { useStore } from "@/store";
import router from "@/router";
export default defineComponent({
  setup() {
    const store = useStore();
    const views = computed(() => {
      return store.state.views.views;
    });
    const active = ref(-1);

    watch(router.currentRoute, (newRouter) => {
      let flag = true;
      for (let i = 0; i < views.value.length; i++) {
        if (views.value[i].path === newRouter.path) {
          active.value = i;
          flag = false;
          break;
        }
      }
      if (flag) {
        active.value = -1;
      }
    });

    onMounted(() => {
      for (let i = 0; i < views.value.length; i++) {
        if (views.value[i].path === router.currentRoute.value.path) {
          active.value = i;
          break;
        }
      }
    });

    const closeHandle = (path: string) => {
      store.dispatch("delView", path);
      if (router.currentRoute.value.path === path && views.value.length > 0) {
        router.push({ path: views.value[views.value.length - 1].path });
      } else if (views.value.length == 0) {
        router.push({ path: "/" });
      }
    };
    const clickHandle = (path: string) => {
      router.push({ path: path });
    };
    const toHome = () => {
      router.push({ path: "/" });
    };

    return {
      views,
      active,
      closeHandle,
      clickHandle,
      toHome,
    };
  },
});
</script>

<style lang="scss" scoped>
.tag {
  height: 30px;
  .scroll {
    .el-tag {
      margin-top: 3px;
      margin-left: 5px;
      &:hover {
        cursor: pointer;
      }
    }
    display: flex;
  }
}
</style>
