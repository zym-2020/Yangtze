<template>
  <div class="project-card">
    <el-card class="card" shadow="hover">
      <template #header>
        <div class="card-header" :title="projectInfo.projectName">
          <div class="icon">
            <el-icon v-if="projectInfo.isPublic" color="#21d86d"><Unlock /></el-icon>
            <el-icon v-else color="#dd001b"><Lock /></el-icon>
          </div>
          <div class="text">
            <strong>{{ projectInfo.projectName }}</strong>
          </div>
        </div>
      </template>

      <div class="img">
        <el-avatar
          shape="square"
          fit="cover"
          v-if="projectInfo.avatar === ''"
          >{{ projectInfo.projectName }}</el-avatar
        >
        <img
          :src="'http://localhost:8002/visual/getAvatar/' + projectInfo.avatar"
          width="248"
          height="180"
          v-else
        />
      </div>
      <div class="foot">
        <div class="time" :title="getDate(projectInfo.createTime)">
          <svg style="width: 20px; height: 20px; margin-top: 5px">
            <use xlink:href="#icon-shijian"></use>
          </svg>
          <strong class="text">{{ getDate(projectInfo.createTime) }}</strong>
        </div>
        <div class="user" :title="projectInfo.userName" v-if="flag">
          <svg style="width: 20px; height: 20px; margin-top: 5px">
            <use xlink:href="#icon-nickname"></use>
          </svg>
          <strong class="text">{{ projectInfo.userName }}</strong>
        </div>
        <slot name="operate" v-else />
      </div>
    </el-card>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import { dateFormat } from "@/utils/common";
export default defineComponent({
  props: {
    projectInfo: {
      type: Object,
    },
    flag: {
      type: Boolean,
    },
  },
  setup(props) {
    const projectInfo = computed(() => {
      return props.projectInfo;
    });

    const getDate = (date: string) => {
      return dateFormat(date, "yyyy-MM-dd");
    };

    const flag = computed(() => {
      return props.flag;
    });

    return {
      projectInfo,
      getDate,
      flag,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-card {
  height: 300px;
  width: 270px;
  cursor: pointer;
  .card {
    /deep/ .el-card__header {
      padding: 10px;
    }
    /deep/ .el-card__body {
      padding: 10px;
    }
    .card-header {
      display: flex;

      .icon {
        width: 20px;
        margin-top: 1px;
      }
      .text {
        height: 20px;
        line-height: 20px;
        width: 228px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
      }
    }
    .des {
      display: flex;
      height: 29px;
      .des-text {
        width: 200px;
        overflow: hidden;
        white-space: nowrap;
        text-overflow: ellipsis;
        -o-text-overflow: ellipsis;
      }
    }
    .img {
      height: 180px;
      .el-avatar {
        height: 180px;
        width: 248px;
      }
    }
    .foot {
      height: 30px;
      position: relative;
      display: flex;
      .time,
      .user {
        position: absolute;
        font-size: 13px;
        display: flex;

        .text {
          line-height: 30px;
          margin-left: 5px;
          width: 90px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          -o-text-overflow: ellipsis;
        }
      }
      .time {
        left: 0px;
      }
      .user {
        left: 50%;
      }
    }
  }
}
</style>