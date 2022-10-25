<template>
  <div class="header-main">
    <header id="topnav" class="topNav">
      <div class="inner">
        <div class="logo" @click="toHome">
          <h1 class="logo-text">NHRI</h1>
          <h1 class="logo-text">NHRI</h1>
        </div>
        <nav role="navigation">
          <ul>
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
      </div>
    </header>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import router from "@/router";
import { useStore } from "@/store";
import { getToken } from "@/utils/auth";

export default defineComponent({
  emits: ["openUploadList"],
  setup(_, context) {
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
        context.emit("openUploadList");
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

    return {
      ports,
      toHome,
      userNav,
      adminFlag,
      login,
      avatarUrl,
      dotFlag,
      path,
    };
  },
});
</script>

<style scoped lang="scss">
body {
  &.header-fixed {
    .logo {
      font-size: 30px;
    }
  }
}
* {
  margin: 0;
  padding: 0;
  line-height: 0.8;
}
ul,
li,
a,
.logo {
  font-family: "Lato", "Verdana", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-weight: 400;
  margin: 0;
  padding: 0;
}
.inner {
  width: 90%;
  position: relative;
  margin: 0 auto;
}
header {
  position: fixed;
  width: 100%;
  padding-top: 0px;
  background-image: linear-gradient(
    rgba(13, 21, 27, 0.8),
    rgba(24, 64, 95, 0.3)
  );
  transition: 0.7s all;
  z-index: 999;
  backdrop-filter: blur(8px);
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
  overflow: hidden;
  ul {
    float: right;
  }
  li {
    display: inline;
    float: left;
    &:last-child {
      float: right;
    }
  }
  a {
    display: inline-block;
    color: rgba(245, 247, 255, 1);
    text-decoration: none;
    font-family: "SimHei";
    font-size: 16px;
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
.logo {
  float: left;
  margin-top: 0;
  font-size: 48px;
  line-height: 50px;
  color: #fff;
  &:hover {
    cursor: pointer;
  }
  h1 {
    &.logo-text {
      position: absolute;
      padding: 10px;
      color: #fff;
      font-size: 1em;
    }
    &:nth-child(1) {
      color: transparent;
      -webkit-text-stroke: 2px #03a9f4;
    }
    &:nth-child(2) {
      color: #03a9f4;
      animation: water-wave 5s infinite;
    }
  }
}

@keyframes water-wave {
  0%,
  100% {
    clip-path: polygon(
      0% 18%,
      15% 28%,
      32% 72%,
      54% 70%,
      70% 18%,
      84% 20%,
      100% 60%,
      100% 100%,
      0% 100%
    );
  }
  50% {
    clip-path: polygon(
      0 80%,
      16% 45%,
      34% 20%,
      51% 45%,
      67% 60%,
      84% 72%,
      100% 18%,
      100% 100%,
      0% 100%
    );
  }
}
</style>