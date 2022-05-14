<template>
  <div class="project-card">
    <el-card class="card" shadow="hover">
      <template #header>
        <div class="card-header" :title="projectInfo.project_name">
          <strong>{{ projectInfo.project_name }}</strong>
        </div>
      </template>
      <div class="des" title="哈哈哈">
        <strong>简介：</strong>
        <div class="des-text">{{ projectInfo.description }}</div>
      </div>
      <div class="img">
        <el-avatar
          shape="square"
          fit="cover"
          v-if="
            projectInfo.avatar === '' || projectInfo.avatar === null
          "
          >{{ projectInfo.project_name }}</el-avatar
        >
        <img :src="'http://localhost:8002' + projectInfo.avatar" alt="" width="248" height="180" v-else />
      </div>
      <div class="foot">
        <div class="time" :title="getDate(projectInfo.create_time)">
          <svg style="width: 20px; height: 20px; margin-top: 5px">
            <use xlink:href="#icon-shijian"></use>
          </svg>
          <strong class="text">{{ getDate(projectInfo.create_time) }}</strong>
        </div>
        <div class="user" :title="projectInfo.name">
          <svg style="width: 20px; height: 20px; margin-top: 5px">
            <use xlink:href="#icon-nickname"></use>
          </svg>
          <strong class="text">{{ projectInfo.name }}</strong>
        </div>
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
  },
  setup(props) {
    const projectInfo = computed(() => {
      return props.projectInfo;
    });

    const getDate = (date: string) => {
      return dateFormat(date, "yyyy-MM-dd");
    };

    return {
      projectInfo,
      getDate,
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
      height: 20px;
      line-height: 20px;
      width: 248px;
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      -o-text-overflow: ellipsis;
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