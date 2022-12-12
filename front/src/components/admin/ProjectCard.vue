<template>
  <div class="project-card">
    <div class="image">
      <el-avatar shape="square" fit="cover" v-if="info.avatar === ''">{{
        info.projectName
      }}</el-avatar>
      <img :src="prefix + 'visual/getAvatar/' + info.avatar" v-else />
    </div>
    <div class="text">
      <div class="name">
        {{ info.projectName }}
      </div>
      <div class="creator">
        <el-icon><User /></el-icon>
        <span>{{ info.userName }}</span>
      </div>
      <div class="time">{{ getTime(info.createTime) }}</div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import { dateFormat } from "@/utils/common";
import { prefix } from "@/prefix";
export default defineComponent({
  props: {
    info: {
      type: Object,
    },
  },
  setup(props) {
    const info = computed(() => {
      return props.info;
    });
    const getTime = (time: string) => {
      return dateFormat(time, "yyyy-MM-dd hh:mm");
    };

    return {
      getTime,
      info,
      prefix,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-card {
  border: solid 1px #e4e7ed;
  height: 320px;
  margin-bottom: 20px;
  background: white;
  transition: all 500ms linear;
  cursor: pointer;
  &:hover {
    box-shadow: 0px 0px 20px rgba($color: #000000, $alpha: 0.3);
    transition: all 500ms linear;
  }
  .image {
    height: 220px;
    width: 100%;
    overflow: hidden;

    img {
      height: 100%;
      width: 100%;
      transition: all 500ms linear;
      &:hover {
        transition: all 500ms linear;
        transform: scale(1.1);
      }
    }
  }

  .text {
    padding: 18px 20px 0;
    .name {
      height: 30px;
      line-height: 30px;
      font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
      font-weight: 600;
      font-size: 16px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .creator {
      color: #1890ff;
      font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
      height: 25px;
      line-height: 25px;
      font-size: 14px;
      span {
        margin-left: 10px;
      }
    }
    .time {
      height: 20px;
      font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
      line-height: 20px;
      color: #979ca8;
      font-size: 12px;
    }
  }
}
</style>