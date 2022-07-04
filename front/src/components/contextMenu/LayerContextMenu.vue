<template>
  <div class="main">
    <div class="context-item border-item" @click="showChirt('section')">
      断面形态
    </div>
    <div class="context-item border-item" @click="showChirt('sectionContrast')">
      断面比较
    </div>
    <div class="context-item" @click="delClick">删除图层</div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted } from "vue";
export default defineComponent({
  props: {
    contextItem: {
      type: Object,
    },
  },
  emits: ["delLayer", "showChirt"],
  setup(props, context) {
    const showChirt = (type: string) => {
      // if ((props.contextItem as any).type === "section") {
      //   context.emit("showChirt", {
      //     id: (props.contextItem as any).id,
      //     type: type,
      //   });
      // } else if ((props.contextItem as any).type === "sectionContrast") {
      //   context.emit("showChirt", {
      //     id: (props.contextItem as any).id,
      //     type: type,
      //   });
      // }
      context.emit("showChirt", {type: type, layer: props.contextItem});
    };
    const delClick = () => {
      context.emit("delLayer", (props.contextItem as any).id);
    };
    return {
      delClick,
      showChirt,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  border-radius: 6px;
  width: 150px;
  padding: 10px;
  background: rgba($color: #000000, $alpha: 1);
  .context-item {
    height: 30px;
    line-height: 30px;
    color: white;
    cursor: pointer;
    font-size: 14px;
    &:hover {
      background: rgba($color: white, $alpha: 0.5);
    }
    padding-left: 10px;
  }
  .border-item {
    border-bottom: solid 1px white;
    box-sizing: border-box;
  }
}
</style>