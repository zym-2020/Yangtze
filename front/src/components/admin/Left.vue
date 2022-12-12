<template>
  <div class="left">
    <div class="title"><strong>管&nbsp;理&nbsp;员&nbsp;界&nbsp;面</strong></div>
    <div
      v-for="(item, index) in textList"
      :key="index"
      :class="active == index ? 'card active' : 'card'"
      @click="clickHandle(index)"
    >
      {{ item.label }}
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, watch } from "vue";
import router from "@/router";
export default defineComponent({
  emits: ["nav"],
  setup(_, context) {
    const textList = [
      {
        label: "资源条目管理",
        icon: "",
        route: "",
      },
      {
        label: "一张图管理",
        icon: "",
        route: "",
      },
      {
        label: "分析工程管理",
        icon: "",
        route: "",
      },
      {
        label: "用户管理",
        icon: "",
        route: "",
      },
      {
        label: "审核管理",
        icon: "",
        route: "",
      },
    ];

    const active = ref(0);

    const clickHandle = (param: number) => {
      active.value = param;
      context.emit("nav", param);
    };

    watch(
      () => router.currentRoute.value.name,
      (newValue, oldValue) => {
        if (newValue === "AdminData") {
          active.value = 0;
        } else if (newValue === "AdminProject") {
          active.value = 2;
        } else if (newValue === "AdminSenario") {
          active.value = 1;
        } else if (newValue === "AdminUser") {
          active.value = 3;
        } else if (newValue === "AdminAudit") {
          active.value = 4;
        }
      }
    );

    onMounted(() => {
      if (router.currentRoute.value.name === "AdminData") {
        active.value = 0;
      } else if (router.currentRoute.value.name === "AdminProject") {
        active.value = 2;
      } else if (router.currentRoute.value.name === "AdminSenario") {
        active.value = 1;
      } else if (router.currentRoute.value.name === "AdminUser") {
        active.value = 3;
      } else if (router.currentRoute.value.name === "AdminAudit") {
        active.value = 4;
      }
    });

    return {
      textList,
      active,
      clickHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.left {
  width: 250px;
  height: 100%;
  background: #21252b;
  .title {
    padding: 20px;
    color: white;
    text-align: center;
    font-size: 30px;
    height: 50px;
    line-height: 50px;
    cursor: pointer;
  }
  .card {
    border-radius: 6px;
    height: 50px;
    color: white;
    margin: 5px 10px;
    padding: 0 20px;
    line-height: 50px;
    transition: background 0.5s;
    &:hover {
      cursor: pointer;
      background: #0187fb;
    }
  }
  .active {
    background: #0187fb;
  }
}
</style>