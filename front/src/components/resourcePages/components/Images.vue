<template>
  <div class="goods-image">
    <el-row>
      <el-col>
        <!-- 大图容器 -->
        <div
          class="large"
          :style="[
            {
              backgroundImage: 'url(' + $props.src + ')',
              backgroundPositionX: position.backgroundPositionX,
              backgroundPositionY: position.backgroundPositionY,
            },
          ]"
          v-if="isShow"
        ></div>
      </el-col>
    </el-row>
    <el-row>
      <div class="middle" ref="target">
        <div>
          <img :src="$props.src" style="width: 100%; height: 100%" />
        </div>
        <!-- 蒙层容器 -->
        <div
          class="layer"
          :style="{ left: left + 'px', top: top + 'px' }"
          v-if="isShow"
        ></div>
      </div>
    </el-row>
  </div>
</template>
 
<script  lang="ts">
import {
  onMounted,
  reactive,
  ref,
  watch,
  defineComponent,
  computed,
} from "vue";
import { useMouseInElement } from "@vueuse/core";
export default defineComponent({
  name: "Images",
  props: {
    src: {
      type: String,
    },
  },

  setup(props, context) {
    const curId = ref(0);
    const target = ref(null);
    // elementX: 鼠标距离左侧的偏移值
    // elementY：表表距离顶部的偏移值
    // isOutside: 是否在容器外部  外部true  内部 false
    const { elementX, elementY, isOutside } = useMouseInElement(target);
    const left = ref(0); // 滑块距离左侧的距离
    const top = ref(0); // 滑块距离顶部的距离
    const isShow = ref(false); // 显示大图和蒙层图的显示和隐藏
    const position = reactive({
      // 大图显示的位置,默认是0
      backgroundPositionX: "0px",
      backgroundPositionY: "0px",
    });
    const thumbnail = computed(() => {
      if (!props.src) {
        return "";
      }
      return props.src;
    });

    watch(
      // 监听的对象
      [elementX, elementY, isOutside],
      () => {
        if (elementX.value < 100) {
          // 左侧最小距离
          left.value = 0;
        }
        if (elementX.value > 300) {
          left.value = 200;
        }
        if (elementX.value > 100 && elementX.value < 300) {
          left.value = elementX.value - 100;
        }
        if (elementY.value < 100) {
          // 左侧最小距离
          top.value = 0;
        }
        if (elementY.value > 300) {
          top.value = 200;
        }
        if (elementY.value > 100 && elementY.value < 300) {
          top.value = elementY.value - 100;
        }
        // 控制背景图移动
        // 1. 蒙层移动的方向和大图背景移动的方向是相反的
        // 2. 蒙层和大图由于面积大小是1:2的 蒙层每移动一个像素  大图移动俩个像素
        // backgrondPosition：x,y;
        position.backgroundPositionX =
          ((-left.value * 2) as any as string) + "px";
        position.backgroundPositionY =
          ((-top.value * 2) as any as string) + "px";
        //position.backgroundPositionX  =  position.backgroundPositionX.toString()   + "px";

        // 当isOutside的值发生变化的时候,立刻取反赋值给isShow
        // isOutside: 是否在容器外部  外部true  内部 false
        isShow.value = !isOutside.value;
      },
      {}
    );

    return {
      curId,
      target,
      left,
      top,
      position,
      isShow,
      thumbnail,
    };
  },
});
</script>
<style scoped lang="less">
.goods-image {
  width: 400px;
  height: 400px;
  position: relative;
  display: flex;
  z-index: 500;

  .large {
    position: absolute;
    top: 410px;
    //left: 412px;
    //text-align: center;
    width: 400px;
    height: 400px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-repeat: no-repeat;
    background-size: 800px 800px;
    background-color: #f8f8f8;
  }

  .middle {
    width: 400px;
    height: 400px;
    background: #f5f5f5;
    position: relative;
    cursor: move;

    .layer {
      width: 200px;
      height: 200px;
      background: rgba(0, 0, 0, 0.2);
      left: 0;
      top: 0;
      position: absolute;
    }
  }

  .small {
    width: 80px;

    li {
      width: 68px;
      height: 68px;
      margin-left: 12px;
      margin-bottom: 15px;
      cursor: pointer;

      &:hover,
      &.active {
        border: 2px solid #27ba9b;
      }
    }
  }
}
</style>

