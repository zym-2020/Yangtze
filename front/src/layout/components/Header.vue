<template>
  <div class="header-main">
    <div class="img" @click="toNav('首页')">
      <img src="/logo.jpg" alt="" height="50" />
    </div>
    <div :class="active === 0 ? 'active nav' : 'nav'" @click="toNav('首页')">
      首页
    </div>
    <div :class="active === 1 ? 'active nav' : 'nav'" @click="toNav('资源')">
      资源
    </div>
    <div :class="active === 2 ? 'active nav' : 'nav'" @click="toNav('场景')">
      场景
    </div>
    <div :class="active === 3 ? 'active nav' : 'nav'" @click="toNav('应用')">
      应用
    </div>
    <el-dropdown trigger="click" @command="userNav">
      <div class="avatar">
        <el-avatar
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          :size="40"
        />
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item command="1">个人空间</el-dropdown-item>
          <el-dropdown-item v-if="adminFlag" command="2"
            >admin界面</el-dropdown-item
          >
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import router from "@/router";
import { useStore } from "@/store";
export default defineComponent({
  setup() {
    const store = useStore();
    const active = ref(0);
    const adminFlag = computed(() => {
      let flag = false;
      store.state.user.roles.forEach((item) => {
        if (item === "admin") {
          flag = true;
        }
      });
      return flag;
    });
    const toNav = (param: string) => {
      switch (param) {
        case "首页":
          active.value = 0;
          router.push({ path: "/" });
          break;
        case "资源":
          active.value = 1;
          router.push({ path: "/data" });
          break;
        case "场景":
          active.value = 2;
          router.push({ path: "/scenario" });
          break;
        case "应用":
          active.value = 3;
          router.push({ path: "/analyze" });
      }
    };

    const userNav = (param: string) => {
      if (param === "1") {
        router.push({ path: "/user/space" });
      } else if (param === "2") {
        router.push({ path: "/user/admin" });
      }
    };

    return {
      toNav,
      active,
      adminFlag,
      userNav
    };
  },
});
</script>

<style lang="scss" scoped>
.header-main {
  height: 100%;
  width: 100%;
  display: flex;
  background: #0089ce;
  line-height: 60px;
  position: relative;
  .img {
    height: 50px;
    margin-top: 5px;
    margin-left: 20px;
    cursor: pointer;
  }
  .nav {
    font-size: 18px;
    text-align: center;
    width: 80px;
    color: white;
    height: 60px;
    &:hover {
      box-sizing: border-box;
      border-bottom: solid 3px #bdd8f4;
      cursor: pointer;
    }
  }
  .active {
    box-sizing: border-box;
    border-bottom: solid 3px #bdd8f4;
    cursor: pointer;
  }
  .el-dropdown {
    position: absolute;
    right: 10%;
    .avatar {
      height: 60px;
      .el-avatar {
        margin-top: 10px;
        cursor: pointer;
      }
    }
  }
}
</style>