<template>
  <div class="header-main">
    <el-row>
      <el-col :span="1">
        <div class="grid-content" />
      </el-col>
      <el-col :span="3">
        <div class="grid-content name">水文综合信息<br />管理与分析平台</div>
      </el-col>
      <el-col :span="2" :offset="1">
        <div class="grid-content index" @click="nav('home')">首页</div>
      </el-col>
      <el-col :span="2" :offset="0">
        <div class="grid-content data" @click="nav('resource')">资源门户</div>
      </el-col>
      <el-col :span="2">
        <div class="grid-content amap" @click="nav('map')">一张图</div>
      </el-col>
      <el-col :span="2">
        <div class="grid-content analysis" @click="nav('analyse')">
          分析中心
        </div>
      </el-col>
      <el-col :span="2">
        <div class="grid-content help">帮助</div>
      </el-col>
      <el-col :span="2" :offset="7">
        <el-dropdown trigger="hover" @command="userNav">
          <el-button
            type="primary"
            color="rgba(219, 219, 219, 0.5)"
            :dark="false"
          >
            <el-avatar
              :size="32"
              :src="
                avatarUrl === ''
                  ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
                  : prefix + 'visual/getAvatar/' + avatarUrl
              "
            />
            &nbsp;&nbsp;&nbsp;账号
          </el-button>
          <template #dropdown>
            <el-dropdown-menu :router="true" :default-active="$route.path">
              <el-dropdown-item command="1">个人空间</el-dropdown-item>
              <el-dropdown-item v-if="adminFlag" command="2"
                >admin界面</el-dropdown-item
              >

              <el-dropdown-item command="3">上传记录</el-dropdown-item>

              <el-dropdown-item command="5">退出</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { prefix } from '@/prefix'
export default {
  name: "HeaderComponent",
};
</script>

<script lang="ts" setup>
import { computed, defineEmits, ref } from "vue";
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

const nav = (param: string) => {
  if (param === "home") {
    router.push({ path: "/" });
  } else if (param === "resource") {
    router.push({ path: "/data" });
  } else if (param === "map") {
    router.push("/scenario");
  } else if (param === "analyse") {
    router.push({ path: "/analyze" });
  }
};

const userNav = (param: string) => {
  if (param === "1") {
    router.push({ path: "/user/space" });
  } else if (param === "2") {
    router.push({ path: "/user/admin" });
  } else if (param === "5") {
    store.dispatch("logout", undefined);
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
div.header-main {
  position: fixed;
  height: 7vh;
  width: 96%;
  padding-left: 1%;
  padding-right: 3%;
  padding-top: 0px;
  background-image: linear-gradient(
    rgba(13, 21, 27, 0.5),
    rgba(24, 64, 95, 0.2)
  );
  background-color: rgb(0, 3, 26);
  transition: 0.7s all;
  z-index: 999;
  font-family: "Microsoft YaHei";

  .el-row {
    margin-top: 3px;
    height: 6vh;
  }
  .el-row:last-child {
    margin-bottom: 0;
  }
  .el-col {
    border-radius: 4px;
    height: 6vh;

    .grid-content {
      border-radius: 4px;
      min-height: 36px;
      height: 6vh;
      color: #ffffff;
      line-height: 6vh;
      text-align: center;
      font-size: 2vh;
      font-weight: 600;
      transition-duration: 0.5s;
      &:hover {
        cursor: pointer;
        transition-duration: 0.3s;
        &.index {
          font-size: 2.5vh;
          background-image: linear-gradient(
            45deg,
            #ffffff 0%,
            #9edcfc 40%,
            #39d6fd 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.data {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #fdfdd7 40%,
            #fff23a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.amap {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #d5ffd5 40%,
            #3aff3a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.analysis {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #ffe0bc 40%,
            #ffa33a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
        &.help {
          font-size: 2.5vh;
          background-image: linear-gradient(
            30deg,
            #fffcd2 0%,
            #ebbfb0 40%,
            #ff443a 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
      }
      &.name {
        font-weight: 500;
        text-align: left;
        font-size: 3vh;
        line-height: 3vh;
        padding-left: 2vh;
        color: aliceblue;
        background-image: linear-gradient(
          45deg,
          #ffffff 0%,
          #9edcfc 40%,
          #39d6fd 100%
        );
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;
      }
    }
    &:first-child {
      background-color: transparent;
      .grid-content {
        background-color: transparent;
        background-image: url(../../assets/header/logo.png);
        background-size: contain;
        background-repeat: no-repeat;
      }
    }
    &:last-child {
      border-radius: 3vh;
      .el-dropdown {
        border-radius: 3vh;
        width: 100%;
        height: 100%;

        .el-button {
          border-radius: 3vh;
          border-width: 1px;
          border-color: #b4b4b4c9;
          margin-top: 7%;
          width: 70%;
          height: 76%;
          font-size: 2vh;
          font-weight: 600;
          background-image: linear-gradient(
            30deg,
            #ffffff 0%,
            #d5f1ff 40%,
            #b8f1ff 100%
          );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;

          &:hover {
            border-width: 2px;
            border-color: #b4b4b4;
            background-image: linear-gradient(
              30deg,
              #ffffff 0%,
              #d5f1ff 40%,
              #39d6fd 100%
            );
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            -webkit-animation: hue 60s infinite linear;
          }
        }
      }
    }
  }
}

ul.el-dropdown-menu {
  background-color: rgb(2, 9, 76);
  font-size: 2vh;
  width: 8vw;

  /deep/.el-dropdown-menu__item {
    font-size: 2vh;
    font-family: "Microsoft YaHei";
    color: rgb(224, 253, 255);
    font-weight: 500;
    text-align: center;
  }

  /deep/.el-dropdown-menu__item:focus {
    background-color: rgb(0, 6, 37);
    color: #f3f3f3;
    font-weight: 600;
  }
}

/deep/.el-scrollbar__wrap {
  background-color: #39d6fd !important;
}
</style>