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
import { getAllModels, generateConfig, predict } from "@/api/request";
import { ModelInfo } from "@/type";
import ParamSetting from "./ParamSetting.vue";
import { ElLoading } from "element-plus";
import router from "@/router";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { ParamSetting },
  emits: ["computeConfigHandle"],
  setup(_, context) {
    const skeletonFlag = ref(true);
    const modelList = ref<ModelInfo[]>([]);
    const currentModelId = ref("");

    const clickHandle = (id: string) => {
      if (currentModelId.value === id) currentModelId.value = "";
      else currentModelId.value = id;
    };

    const paramSettingCall = async (
      val: { parameter: string; key: string; value: string }[]
    ) => {
      context.emit("computeConfigHandle");
      const jsonData = {
        projectId: router.currentRoute.value.params.id as string,
        config: {
          model: currentModelId.value,
          parameters: val,
        },
      };
      const loading = ElLoading.service({
        lock: true,
        text: "Loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      const res = await generateConfig(jsonData);
      if ((res as any).code === 0) {
        const result = await predict({
          projectId: router.currentRoute.value.params.id as string,
          config: res.data,
        });
        if ((result as any).code === 0) {
          notice("success", "成功", "水位预报计算完成");
        } else {
          notice("error", "失败", "计算失败");
        }
      } else {
        notice("error", "失败", "计算失败");
      }

      loading.close();
    };

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