<template>
  <div class="nav">
    <el-row>
      <el-col :span="1">
        <el-icon @click="hamburgerClick" :class="[{ 'is-active': flag }]">
          <fold />
        </el-icon>
      </el-col>
      <el-col :span="20">
        <el-breadcrumb>
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item
            v-for="(item, index) in breadcrumbs"
            :key="index"
            >{{ item }}</el-breadcrumb-item
          >
        </el-breadcrumb>
      </el-col>
      <el-col :span="3">
        <el-dropdown trigger="click" @command="handleCommand">
          <el-avatar
            class="ava"
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          />
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  reactive,
  onBeforeMount,
  toRefs,
  watch,
} from "vue";
import router from "@/router";
import { useStore } from "@/store";
import { getToken } from "@/utils/auth";
export default defineComponent({
  emits: {
    "hamburger-click": null,
  },
  setup(_, context) {
    const store = useStore();

    //hamburger相关变量函数
    const flag = ref(false);
    const hamburgerClick = () => {
      flag.value = !flag.value;
      context.emit("hamburger-click", flag.value);
    };

    //breadcrumbs相关函数变量
    const bread = reactive({
      breadcrumbs: [] as string[],
      getBreadcrumb: () => {
        let path = router.currentRoute.value.path;
        let meta = router.currentRoute.value.meta.bread as string;
        if (router.currentRoute.value.meta.tag) {
          if (path != "/") {
            bread.breadcrumbs = meta.split("/");
          } else {
            bread.breadcrumbs = [];
          }
        }
      },
      logout: () => {
        store.dispatch("logout", undefined);
        store.dispatch("delAllViews", undefined);
      },
      handleCommand: (command: string) => {
        if (command === "logout") {
          bread.logout();
        }
      },
    });
    watch(router.currentRoute, () => {
      if (getToken()) {
        bread.getBreadcrumb();
      }
    });
    onBeforeMount(() => {
      bread.getBreadcrumb();
    });

    return {
      flag,
      hamburgerClick,
      ...toRefs(bread),
    };
  },
});
</script>


<style lang="scss" scoped>
.nav {
  height: 50px;
  line-height: 50px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  .is-active {
    transform: rotate(180deg);
  }

  .el-icon {
    font-size: 20px;
    margin-top: 15px;
    margin-left: 20px;
    &:hover {
      cursor: pointer;
    }
  }
  .el-breadcrumb {
    margin-top: 16px;
    font-size: 18px;
  }

  .ava {
    margin-top: 5px;
    &:hover {
      cursor: pointer;
    }
  }
}
</style>