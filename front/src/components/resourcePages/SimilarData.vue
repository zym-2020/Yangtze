<template >
  <div class="data-similiar">
    <div class="main">
      <div
        :style="[
          { width: '100%' },
          {
            height: len > 4 ? 450 + Math.trunc(len / 4) * 400 + 'px' : '775px',
          },
          { backgroundColor: 'rgba(237, 237, 237, 0.5)' },
          { position: 'relative' },
        ]"
      >
        <div v-for="(item, index) in list" :key="index">
          <div
            :style="[
              { position: 'absolute' },
              { left: 20 + (index % 4) * 460 + 'px' },
              { top: Math.trunc(index / 4) * 400 + 'px' },
              { zIndex: '20' },
            ]"
          >
            <SimiliarDataComponent :similarInfo="item"></SimiliarDataComponent>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>



<script lang="ts">
import { defineComponent, ref, computed, onMounted } from "vue";
import SimiliarDataComponent from "./SimiliarDataComponent.vue";
export default defineComponent({
  components: {
    SimiliarDataComponent,
  },
  props: {
    similarList: Object,
  },
  setup(props) {
    const count = ref<number>(0);
    const list = computed(() => {
      return props.similarList as any[];
    });
    const len = computed(() => {
      return (props.similarList as any).length;
    });
    onMounted(() => {
      console.log(props.similarList);
    });
    return {
      count,
      list,
      len,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-similiar {
  width: 100%;
  .main {
    margin: 0 auto;
    .el-divider {
      margin-top: 40px;
      /deep/ .el-divider__text {
        font-size: 18px;
      }
    }
  }
}
</style>