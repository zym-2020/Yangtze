<template>
  <div class="water-level-prediction">
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div class="content" v-else>
      <div v-if="modelList.length !== 0">
        <div class="model" v-for="(item, index) in modelList" :key="index">
          <div class="name" @click="clickHandle(item.id)">
            <span>{{ item.name }}</span>
          </div>
          <div class="detail" v-if="currentModelId === item.id">
            <div class="model-des">
              <span>{{ item.description }}</span>
            </div>
            <param-setting
              :modelId="item.id"
              @paramSettingCall="paramSettingCall"
            />
          </div>
        </div>
      </div>
      <el-empty description="暂无数据" v-else />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAllModels } from "@/api/request";
import { ModelInfo } from "@/type";
import ParamSetting from "./ParamSetting.vue";
export default defineComponent({
  components: { ParamSetting },
  setup() {
    const skeletonFlag = ref(true);
    const modelList = ref<ModelInfo[]>([]);
    const currentModelId = ref("");

    const clickHandle = (id: string) => {
      if (currentModelId.value === id) currentModelId.value = "";
      else currentModelId.value = id;
    };

    const paramSettingCall = () => {};

    onMounted(async () => {
      const res = await getAllModels();
      modelList.value = res.data.models;
      skeletonFlag.value = false;
      console.log(modelList.value);
    });

    return {
      skeletonFlag,
      modelList,
      currentModelId,
      clickHandle,
      paramSettingCall,
    };
  },
});
</script>

<style lang="scss" scoped>
@-webkit-keyframes scale-up-ver-top {
  0% {
    -webkit-transform: scaleY(0.4);
    transform: scaleY(0.4);
    -webkit-transform-origin: 100% 0%;
    transform-origin: 100% 0%;
  }
  100% {
    -webkit-transform: scaleY(1);
    transform: scaleY(1);
    -webkit-transform-origin: 100% 0%;
    transform-origin: 100% 0%;
  }
}
@keyframes scale-up-ver-top {
  0% {
    -webkit-transform: scaleY(0.4);
    transform: scaleY(0.4);
    -webkit-transform-origin: 100% 0%;
    transform-origin: 100% 0%;
  }
  100% {
    -webkit-transform: scaleY(1);
    transform: scaleY(1);
    -webkit-transform-origin: 100% 0%;
    transform-origin: 100% 0%;
  }
}
.water-level-prediction {
  background: #f0f0f0;
  padding: 20px;
  .content {
    .model {
      border-radius: 4px;
      box-sizing: border-box;
      border: solid 1px #bccbd7;
      margin-top: 10px;
      overflow: hidden;
      &:hover {
        .name {
          color: #4264fb;
        }
      }
      .name {
        height: 30px;
        line-height: 30px;
        padding: 0 10px;
        background: #f6f8f8;
        cursor: pointer;
        color: #6e718f;
      }
      .detail {
        -webkit-animation: scale-up-ver-top 0.4s
          cubic-bezier(0.39, 0.575, 0.565, 1) both;
        animation: scale-up-ver-top 0.4s cubic-bezier(0.39, 0.575, 0.565, 1)
          both;
        padding: 10px;
        .model-des {
          font-size: 16px;
          color: black;
          margin-bottom: 20px;
        }
      }
    }
  }
}
</style>