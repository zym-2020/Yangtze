<template>
  <div class="data-description">
    <div class="key">
      <div class="text">{{ key }}</div>
    </div>
    <div class="value">{{ value }}</div>
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

    return {
      key,
      value,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-description {
  width: 100%;
  display: flex;
  .key {
    width: 100px;
    .text {
      float: right;
    }
  }
  .value {
    width: calc(100% - 200px);
    word-wrap:break-word;
  }
}
</style>