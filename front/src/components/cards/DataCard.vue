<template>
  <div class="data-card">
    <div class="top">
      <el-avatar :size="40" :src="avatar" />
      <div class="text" @click="clickName">{{ name }}</div>
      <slot name="creator"></slot>
    </div>
    <div class="des">
      {{ description }}
    </div>
    <div class="bottom">
      <div class="bottom-top">
        <div class="time">
          <strong>上次更新时间：</strong>
          <span>{{ updateTime }}</span>
        </div>
        <div class="watch">
          <el-icon><View /></el-icon>
          <span>{{ watch }}</span>
        </div>
      </div>
      <div class="bottom-bottom">
        <el-tag v-for="(item, index) in tagList" :key="index">
          {{ item }}
        </el-tag>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import { imgBase64, generateColorByText, dateFormat } from "@/utils/common";

export default defineComponent({
  props: {
    fileInfo: {
      type: Object,
    },
  },
  emits: ['clickName'],
  setup(props, context) {
    const name = computed(() => {
      return (props.fileInfo as any).name;
    });
    const avatar = computed(() => {
      return imgBase64(name.value);
    });
    const description = computed(() => {
      return (props.fileInfo as any).description;
    });
    const updateTime = computed(() => {
      return dateFormat((props.fileInfo as any).updateTime, "yyyy-MM-dd");
    });
    const watch = computed(() => {
      return (props.fileInfo as any).watch;
    });
    const tagList = computed(() => {
      return (props.fileInfo as any).tags;
    });

    const getColor = (text: string) => {
      return generateColorByText(text);
    };

    const clickName = () => {
      context.emit('clickName')
    }

    return {
      avatar,
      name,
      description,
      updateTime,
      watch,
      tagList,
      getColor,
      clickName
    };
  },
});
</script>

<style lang="scss" scoped>
.data-card {
  margin-bottom: 20px;
  .top {
    display: flex;
    position: relative;
    height: 40px;
    line-height: 40px;
    .text {
      margin-left: 10px;
      font-size: 22px;
      color: #4fb5ea;
    }
  }
  .des {
    margin-top: 15px;
    font-size: 14px;
    line-height: 30px;
  }
  .bottom {
    .bottom-top {
      display: flex;
      margin-top: 10px;
      .watch {
        margin-left: 20px;
        position: relative;
        .el-icon {
          position: absolute;
          top: 2px;
        }
        span {
          margin-left: 20px;
        }
      }
    }
    .bottom-bottom {
      margin-top: 10px;
      .el-tag {
        margin-right: 10px;
      }
    }
  }
}
</style>