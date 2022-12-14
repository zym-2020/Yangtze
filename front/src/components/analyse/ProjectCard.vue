<template>
  <div class="project-card">
    <div class="image">
      <el-avatar shape="square" fit="cover" v-if="info.avatar === ''">{{
        info.projectName
      }}</el-avatar>

      <img :src="prefix + 'visual/getAvatar/' + info.avatar" v-else />
    </div>
    <div class="name">
      <span v-html="replaceHandle(info.projectName)"></span>
    </div>
    <div class="creator">
      <span v-html="replaceHandle(info.userName)"></span>
    </div>
    <div class="time">{{ time }}</div>
    <div class="btn">
      <el-button type="primary" text @click="clickHandle">查看详情</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import { prefix } from "@/prefix";
import { dateFormat } from "@/utils/common";
import router from "@/router";
export default defineComponent({
  props: {
    info: {
      type: Object,
    },
    keyword: {
      type: String,
    },
  },
  setup(props) {
    const info = computed(() => {
      return props.info;
    });

    const time = computed(() => {
      return dateFormat((props.info as any).createTime, "yyyy-MM-dd hh:mm");
    });

    const clickHandle = () => {
      router.push({
        name: "project",
        params: {
          id: (info.value as any).id,
          projectInfo: JSON.stringify(info.value),
        },
      });
    };

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(" + props.keyword + ")", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" + props.keyword + "</span>"
      );
      return currentStr;
    };

    return {
      info,
      replaceHandle,
      time,
      prefix,
      clickHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-card {
  padding: 20px;
  border: solid 1px #e4e7ed;
  border-radius: 4px;
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 500ms linear;
  position: relative;
  &:hover {
    box-shadow: 0px 0px 20px rgba($color: #000000, $alpha: 0.3);
    transition: all 500ms linear;
  }
  .image {
    width: 100%;
    height: 200px;
    overflow: hidden;
    .el-avatar {
      height: 100%;
      width: 100%;
      transition: all 500ms linear;
      &:hover {
        transition: all 500ms linear;
        transform: scale(1.1);
      }
    }
    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
      transition: all 500ms linear;
      &:hover {
        transition: all 500ms linear;
        transform: scale(1.1);
      }
    }
  }
  .name {
    font-weight: 600;
    font-size: 20px;
    line-height: 40px;
  }
  .creator {
    font-size: 14px;
    color: #409eff;
  }
  .time {
    color: #979ca8;
    font-size: 14px;
    line-height: 40px;
  }
  .btn {
    position: absolute;
    bottom: 20px;
    right: 20px;
  }
}
</style>