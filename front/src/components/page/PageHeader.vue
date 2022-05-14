<template>
  <div class="header">
    <div class="nav">
      <div class="home" @click="toHome">首页</div>
      <div class="Separator">/</div>
      <div>{{ pageTitle }}</div>
    </div>
    <div class="bottom">
      <div class="title">{{ pageTitle }}</div>
      <slot name="search"></slot>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import router from "@/router";
export default defineComponent({
  props: {
    pageTitle: {
      type: String,
    },
  },
  setup(props) {
    const toHome = () => {
      router.push({ path: "/" });
    };
    const pageTitle = computed(() => {
      return props.pageTitle;
    });

    return {
      toHome,
      pageTitle,
    };
  },
});
</script>

<style lang="scss" scoped>
.header {
  height: 100px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
  padding: 0 10%;
  .nav {
    display: flex;
    height: 40px;
    line-height: 40px;
    cursor: pointer;
    .Separator {
      margin: 0 5px;
    }
    .home {
      color: #4f71bc;
    }
  }
  .bottom {
    display: flex;
    .title {
      height: 60px;
      line-height: 60px;
      font-size: 30px;
    }
  }
}
</style>