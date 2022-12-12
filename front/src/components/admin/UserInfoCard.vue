<template>
  <div class="user-info-card">
    <div class="bg"></div>
    <div class="main">
      <div class="avatar">
        <el-avatar
          :src="
            userInfo.avatar === ''
              ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
              : prefix + 'visual/getAvatar/' + userInfo.avatar
          "
          fit="cover"
        />
      </div>
      <div class="info">
        <div class="icon">
          <svg style="width: 20px; height: 20px">
            <use xlink:href="#icon-email"></use>
          </svg>
        </div>
        <div class="text email">
          <strong>邮&ensp;&ensp;箱：{{ userInfo.email }}</strong>
        </div>
      </div>
      <div class="info">
        <div class="icon">
          <svg style="width: 20px; height: 20px">
            <use xlink:href="#icon-nickname"></use>
          </svg>
        </div>
        <div class="text">用户名：{{ userInfo.name }}</div>
      </div>
      <div class="info">
        <div class="icon">
          <svg style="width: 20px; height: 20px">
            <use xlink:href="#icon-zhiye"></use>
          </svg>
        </div>
        <div class="text">
          职&ensp;&ensp;业：{{
            userInfo.occupation === "" ? "暂无信息" : userInfo.occupation
          }}
        </div>
      </div>
      <div class="info">
        <div class="icon">
          <svg style="width: 20px; height: 20px">
            <use xlink:href="#icon-company"></use>
          </svg>
        </div>
        <div class="text">
          单&ensp;&ensp;位：{{
            userInfo.department === "" ? "暂无信息" : userInfo.department
          }}
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import { prefix } from "@/prefix";
export default defineComponent({
  props: {
    userInfo: {
      type: Object,
    },
  },
  setup(props) {
    const avatarUrl = "";
    const userInfo = computed(() => {
      return props.userInfo;
    });


    return {
      avatarUrl,
      userInfo,
      prefix,
    };
  },
});
</script>

<style lang="scss" scoped>
.user-info-card {
  background: rgba($color: #000000, $alpha: 0);
  position: relative;
  height: 290px;
  .bg {
    position: absolute;
    height: 210px;
    width: 100%;
    background: white;
    bottom: 0;
    border-radius: 6px;
  }
  .main {
    padding: 20px 70px;
    width: 300px;
    position: absolute;
    top: 0;
    .avatar {
      height: 100px;
      margin-bottom: 20px;
      text-align: center;
      .el-avatar {
        width: 100px;
        height: 100px;
      }
    }
    .info {
      display: flex;
      line-height: 30px;
      .icon {
        margin-top: 5px;
      }
      .text {
        margin-left: 5px;
      }
    }
  }
}
</style>