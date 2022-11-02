<template>
  <div class="main">
    <page-header :pageTitle="'一张图'">
      <template #search> </template>
    </page-header>
    <div class="main-body">
      <el-row>
        <el-col :span="8" v-for="(item, index) in cardList" :key="index">
          <scenario-card
            :cardInfo="{
              img: item.img,
              title: item.title,
            }"
            @click="navHandle(index)"
          ></scenario-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import PageHeader from "@/components/page/PageHeader.vue";
import ScenarioCard from "@/components/cards/ScenarioCard.vue";
import router from "@/router";
export default defineComponent({
  components: { PageHeader, ScenarioCard },
  setup() {
    const cardList: { img: string; title: string }[] = [
      {
        img: require("../../assets/homePage/tide.png"),
        title: "潮位实时监测一张图",
      },
      {
        img: require("../../assets/homePage/flow.png"),
        title: "区域流场一张图",
      },
      {
        img: "https://geomodeling.njnu.edu.cn/static/repository/theme/670f7fbb-14e4-475f-8b94-9750efd13b88.jpg",
        title: "AIS一张图",
      },
      {
        img: require("@/assets/homePage/projects.png"),
        title: "工程方案一张图",
      },
    ];

    const navHandle = (index: number) => {
      console.log(index);
      switch (index) {
        case 0:
          router.push({ path: "/scenario/scenarioOne" });
          break;
        case 1:
          router.push({ path: "/scenario/scenarioTwo" });
          break;
        case 2:
          router.push({ path: "/scenario/scenarioThree" });
          break;
        case 3:
          router.push({ path: "/scenario/scenarioFour" });
          break;
      }
    };

    return {
      cardList,
      navHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  height: 100%;
  .main-body {
    margin-top: 20px;
    .scenario-card {
      margin: 0 auto 20px;
    }
  }
}
</style>