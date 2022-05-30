<template>
  <div class="data-description">
    <div class="key">
      <div class="text">{{ key }}</div>
    </div>
    <div class="value" v-if="!flag">{{ value }}</div>
    <div v-else class="value">
      <el-tag v-for="(item, index) in value" :key="index">
        {{ item }}
      </el-tag>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
export default defineComponent({
  props: {
    data: {
      type: Object,
    },
  },
  setup(props) {
    const key = computed(() => {
      return (props.data as any).key;
    });
    const value = computed(() => {
      return (props.data as any).value;
    });
    const flag = computed(() => {
      return (props.data as any).key === "数据标签：";
    });

    return {
      key,
      value,
      flag,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-description {
  display: flex;
  .key {
    width: 100px;
    .text {
      float: right;
    }
  }
  .value {
    width: 350px;
    .el-tag {
      margin-right: 5px;
      margin-bottom: 5px;
    }
  }
}
</style>