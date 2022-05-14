<template>
  <div class="data-card">
    <div class="top">
      <el-avatar :size="40" :src="avatar" />
      <div class="text">hahah</div>
    </div>
    <div class="des">
      数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述数据描述
    </div>
    <div class="bottom">
      <div class="bottom-top">
        <div class="time">
          <strong>上次更新时间：</strong>
          <span>2022-05-14</span>
        </div>
        <div class="watch">
          <el-icon><View /></el-icon>
          <span>1520</span>
        </div>
      </div>
      <div class="bottom-bottom">
        <el-tag class="ml-2" type="success">Tag 2</el-tag>
        <el-tag class="ml-2" type="info">Tag 3</el-tag>
        <el-tag class="ml-2" type="warning">Tag 4</el-tag>
        <el-tag class="ml-2" type="danger">Tag 5</el-tag>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { imgBase64 } from "@/utils/common";
export default defineComponent({
  setup() {
    const avatar = ref("");
    onMounted(() => {
      avatar.value = imgBase64("哈哈");
    });

    return {
      avatar,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-card {
  margin-bottom: 20px;
  .top {
    display: flex;
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