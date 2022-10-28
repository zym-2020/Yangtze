<template>
  <div class="header-main">
    <header id="topnav" class="topNav">
      <nav role="navigation">
        <ul>
          <li id="logo">
            <div id="logo-pic"></div>
          </li>
          <li v-for="port in ports" :key="port.index">
            <a :href="port.href">{{ port.text }}</a>
          </li>
          <li v-if="login">
            <el-dropdown
              trigger="click"
              @command="userNav"
              style="margin-right: 10px"
            >
              <div class="avatar">
                <el-badge :is-dot="dotFlag">
                  <el-avatar
                    :src="
                      avatarUrl === ''
                        ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
                        : 'http://localhost:8002/visual/getAvatar/' +
                          avatarUrl
                    "
                    :size="40"
                  />
                </el-badge>
              </div>
              <template #dropdown>
                <el-dropdown-menu
                  :router="true"
                  :default-active="$route.path"
                >
                  <el-dropdown-item command="1">个人空间</el-dropdown-item>
                  <el-dropdown-item v-if="adminFlag" command="2"
                    >admin界面</el-dropdown-item
                  >

                  <el-dropdown-item command="3">上传记录</el-dropdown-item>
                  <el-dropdown-item command="4" :index="path"
                    >消息
                    <!-- <router-link :to="{name:UserChild}"></router-link> -->
                  </el-dropdown-item>

                  <el-dropdown-item command="5">退出</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </li>
        </ul>
      </nav>
    </header>
  </div>
</template>

<script lang="ts">
export default {
    name: 'HeaderComponent'
}
</script>



<script lang="ts" setup>
import { computed, defineEmits, ref } from 'vue'
import router from "@/router";
import { useStore } from "@/store";
import { getToken } from "@/utils/auth";

const emit = defineEmits(["openUploadList"]);
const store = useStore();

const path = ref("/user/space");
const login = computed(() => {
  if (getToken() === null) {
    return false;
  } else {
    return true;
  }
});
const avatarUrl = computed(() => {
  return store.state.user.avatar;
});
const adminFlag = computed(() => {
  if (store.state.user.role === "admin") {
    return true;
  } else {
    return false;
  }
});
const dotFlag = computed(() => {
  return store.state.other.uploadDotFlag;
});

const userNav = (param: string) => {
  if (param === "1") {
    router.push({ path: "/user/space" });
  } else if (param === "2") {
    router.push({ path: "/user/admin" });
  } else if (param === "5") {
    store.dispatch("logout", undefined);
  } else if (param === "4") {
    router.push({ path: "/message" });
  } else if (param === "3") {
    store.commit("SET_UPLOAD_DOT_FLAG", false);
    emit("openUploadList");
  }
};

const ports = computed(() => {
  const tempPorts = [
    { href: "#/data", text: "资源门户" },
    { href: "#/scenario", text: "一张图" },
    { href: "#/analyze", text: "分析中心" },
  ];
  if (getToken() === null) {
    tempPorts.push({ href: "#/login", text: "登录" });
  }
  return tempPorts;
});

const toHome = () => {
  router.push({ path: "/" });
};

</script>

<style scoped lang="scss">
* {
  margin: 0;
  padding: 0;
  line-height: 0.8;
}
header {
  position: fixed;
  height: 7vh;
  width: 100%;
  padding-top: 0px;
  background-image: linear-gradient(
    rgba(13, 21, 27, 0.75),
    rgba(24, 64, 95, 0.25)
  );
  background-color: rgb(8, 27, 87);
  transition: 0.7s all;
  z-index: 999;
  font-family: 'Microsoft YaHei';
}
body {
  &.header-fixed {
    header {
      position: fixed;
      top: 0;
      right: 0;
      padding-top: 0;
      width: 100%;
      z-index: 2;
    }
  }
}
li {
  list-style: none;
  .el-dropdown {
    margin-left: 10px;
    margin-top: 10px;
    cursor: pointer;
  }
}
nav {
  height: 7vh;
  overflow: hidden;
  div#logo {
    width: 8%;
    height: 90%;
    margin-right: 0px;
    background-color: red;
    z-index: 1;
  }

  ul {
    float: right;
  }
  li {
    display: inline;
    float: left;
    &:last-child {
      float: right;
    }
    div#logo-pic {
      background-image: url("../../assets/header/logo.png");
    }
  }
  a {
    display: inline-block;
    color: rgb(245, 245, 245);
    text-decoration: none;
    font-family: 'Microsoft YaHei';
    font-weight: 600;
    font-size: 1vw;
    padding: 24px;
    transition: 1s all;
    background-image: url("/wave_cut_new.png");
    background-position: top left;
    &:hover {
      transition: 2s all;
      border-bottom-left-radius: 10%;
      border-bottom-right-radius: 10%;
      background-position: bottom right;
    }
  }
}
</style>