<template>
  <div class="admin-head">
    <div class="menu">
      <div class="menu-item" @click="navHandle('/')">首页</div>
      <div class="menu-item" @click="navHandle('/data')">资源门户</div>
      <div class="menu-item" @click="navHandle('/scenario')">一张图</div>
      <div class="menu-item" @click="navHandle('/analyze')">分析中心</div>
      <div class="menu-item" @click="navHandle('/')">帮助</div>
    </div>

    <div class="avatar">
      <el-dropdown trigger="click" @command="userNav">
        <el-button type="primary" color="black" :dark="false">
          <el-avatar
            :size="40"
            :src="
              avatarUrl === ''
                ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
                : prefix + 'visual/getAvatar/' + avatarUrl
            "
          />
          &nbsp;&nbsp;&nbsp;账号
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="5">退出</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from "vue";
import router from "@/router";
import { prefix } from "@/prefix";
import { useStore } from "@/store";
export default defineComponent({
  setup() {
    const store = useStore();

    const userNav = () => {
      store.dispatch("logout", undefined);
    };

    const avatarUrl = computed(() => {
      return store.state.user.avatar;
    });

    const navHandle = (param: string) => {
      router.push({
        path: param,
      });
    };

    return {
      navHandle,
      userNav,
      prefix,
      avatarUrl,
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-head {
  height: 80px;
  background: #ffffff;
  padding: 0 50px;
  display: flex;
  width: calc(100% - 100px);
  position: relative;

  .menu {
    transition: all 500ms linear;
    width: 1000px;
    overflow: hidden;
    display: flex;

    .menu-item {
      width: 200px;
      flex-shrink: 0;
      line-height: 80px;
      cursor: pointer;
      font-size: 20px;
      text-align: center;
      box-sizing: border-box;
      font-weight: 700;
      &:hover {
        color: #0187fb;
        border-bottom: solid 2px #0187fb;
      }
    }
  }
  .icon {
    line-height: 80px;
  }

  .avatar {
    border-radius: 3vh;
    width: 200px;
    position: absolute;
    right: 20px;
    .el-dropdown {
      width: 100%;
      height: 100%;

      .el-button {
        // box-sizing: border-box;
        border-radius: 3vh;
        border-width: 1px;
        border-color: #b4b4b4c9;
        margin-top: 10px;
        width: 70%;
        height: 76%;
        font-size: 2vh;
        font-weight: 600;
        // background-image: linear-gradient(
        //   30deg,
        //   #ffffff 0%,
        //   #d5f1ff 40%,
        //   #b8f1ff 100%
        // );
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        -webkit-animation: hue 60s infinite linear;

        &:hover {
          // border-width: 2px;
          border-color: #b4b4b4;
          // background-image: linear-gradient(
          //   30deg,
          //   #ffffff 0%,
          //   #d5f1ff 40%,
          //   #39d6fd 100%
          // );
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          -webkit-animation: hue 60s infinite linear;
        }
      }
    }
  }
}
</style>