<template>
  <div>
    <el-carousel :interval="2000" type="card" style="margin-top: 30px" indicator-position="none">
      <el-carousel-item v-for="item in photoList" :key="item">
        <el-image fit="fill" :src="item" />
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
export default defineComponent({
  props: {
    photoList: {
      type: Array,
    },
  },
  setup(props) {
    const photoList = computed(() => {
      return props.photoList;
    });

    return {
      photoList,
    };
  },
});
</script>

<style lang="scss" scoped>
</style>