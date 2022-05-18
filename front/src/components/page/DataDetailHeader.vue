<template>
  <div class="data-detail-header">
    <div class="main">
      <div class="top">
        <div class="name">{{ name }}</div>
        <div class="info">
          <div class="icon">
            <el-icon><View /></el-icon>浏览量
            <el-divider direction="vertical" />
            <div>0</div>
          </div>
          <div class="icon">
            <el-icon><Download /></el-icon>下载量
            <el-divider direction="vertical" />
            <div>0</div>
          </div>
        </div>
      </div>
      <div class="bottom">
        <div
          :class="active === 1 ? 'active data-item' : 'data-item'"
          @click="activeClick(1)"
        >
          数据详情
        </div>
        <div
          :class="active === 2 ? 'active data-item' : 'data-item'"
          @click="activeClick(2)"
        >
          相关统计
        </div>
        <div
          :class="active === 3 ? 'active data-item' : 'data-item'"
          @click="activeClick(3)"
        >
          相似数据
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, ref } from "vue";
export default defineComponent({
  props: {
    name: {
      type: String,
    },
  },
  emits: ["activeClick"],
  setup(props, context) {
    const active = ref(1);
    const name = computed(() => {
      return props.name;
    });
    const activeClick = (number: number) => {
      active.value = number;
      context.emit("activeClick", number);
    };

    return {
      active,
      activeClick,
      name,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-detail-header {
  height: 100px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  padding: 0px 100px;
  .main {
    margin: 0 auto;
    .top {
      height: 60px;
      display: flex;
      position: relative;
      .name {
        margin-left: 20px;
        font-size: 26px;
        color: #458fd1;
        line-height: 60px;
      }
      .info {
        display: flex;
        position: absolute;
        top: 15px;
        right: 0px;
        .icon {
          height: 30px;
          border-radius: 8px;
          display: flex;
          border: 1px solid #EDEDED;
          line-height: 30px;
          padding: 0px 10px;
          background: #F1F1F1;
          margin-left: 10px;
          .el-icon {
            margin-top: 7px;
            margin-right: 5px;
          }
          .el-divider {
            margin-top: 7px;
          }
        }
      }
    }
    .bottom {
      display: flex;
      height: 40px;
      .data-item {
        padding: 0 20px;
        cursor: pointer;
        box-sizing: border-box;
        line-height: 40px;
      }
      .active {
        border-bottom: 2px solid #f78166;
      }
    }
  }
}
</style>