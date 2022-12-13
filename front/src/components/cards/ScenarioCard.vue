<template >
  <div id="box">
    <div class="image">
      <img :src="avatar" />
    </div>
    <div class="name">
      {{ name }}
    </div>
    <div class="des">
      {{ des }}
    </div>
    <div class="btn">
      <el-button type="primary" size="large" @click="clickHandle"
        >查看详情</el-button
      >
    </div>
  </div>
</template>
  
  <script lang="ts">
import { defineComponent, computed } from "vue";
import router from "@/router";
export default defineComponent({
  props: {
    dataInfo: Object,
  },
  setup(props) {
    const name = computed(() => {
      return (props.dataInfo as any).name;
    });

    const des = computed(() => {
      return (props.dataInfo as any).des;
    });

    const avatar = computed(() => {
      return (props.dataInfo as any).avatar;
    });

    const clickHandle = () => {
      router.push({
        path: (props.dataInfo as any).path,
      });
    };

    return {
      des,
      name,
      avatar,
      clickHandle,
    };
  },
});
</script>

  
<style lang="scss" scoped>
div#box {
  border-radius: 6px;
  width: 100%;
  // height: 650px;
  // height: calc(100% - 40px);
  height: 100%;
  padding: 20px;
  position: relative;

  .image {
    width: 100%;
    height: 80%;
    img {
      width: 100%;
      height: 100%;
    }
  }

  .name {
    font-size: 30px;
    font-weight: 800;
    margin-top: 20px;
  }
  .des {
    font-size: 20px;
    line-height: 50px;
    color: #8c8c8c;
    font-weight: 500;
  }
  .btn {
    position: absolute;
    bottom: 30px;
    right: 20px;
  }
}
</style>