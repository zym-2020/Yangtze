<template>
  <div class="param-setting" v-loading="loading">
    <el-skeleton :rows="5" animated v-if="skeletonFlag" />
    <div v-else>
      <el-row>
        <el-col :span="6" class="title">参数名</el-col>
        <el-col :span="10" class="title">参数描述</el-col>
        <el-col :span="8" class="title">实参</el-col>
      </el-row>
      <el-divider />
      <div v-for="(item, index) in params" :key="index">
        <el-row>
          <el-col :span="6">
            <div class="label">
              <svg class="icon-svg" v-if="!item.optional">
                <use xlink:href="#icon-bixuan"></use>
              </svg>
              <span>{{ item.name }}</span>
            </div>
          </el-col>
          <el-col :span="10">
            <div class="description">
              {{ item.description }}
            </div>
          </el-col>
          <el-col :span="8">
            <div class="file-path" v-if="item.type === 'file'">
              <el-input disabled v-model="parameterList[index].key" />
              <file-upload
                :actionProp="action"
                :indexProp="index"
                @responseHandle="responseHandle"
                @beforUpload="beforUpload"
              />
            </div>
            <div class="input" v-if="item.type === 'input'">
              <el-input v-model="parameterList[index].key" />
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="btn">
        <el-button @click="confirmClick" :disabled="disabledFlag" size="small"
          >确定</el-button
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { getModelConfig } from "@/api/request";
import { ModelConfig } from "@/type";
import { UploadInstance } from "element-plus";
import { getToken } from "@/utils/auth";
import router from "@/router";
import FileUpload from "@/components/upload/FileUpload.vue";
export default defineComponent({
  props: {
    modelId: {
      type: String,
    },
  },
  emits: ["paramSettingCall"],
  components: { FileUpload },
  setup(props, context) {
    const loading = ref(false);
    const skeletonFlag = ref(true);
    const ModelConfig = ref<ModelConfig>();

    const parameterList = ref<{ key: string; value: string }[]>([]);

    const header = {
      Authorization: `Bearer ${getToken()}`,
    };
    const action = ref(
      `/Yangtze/analyticDataSet/uploadParameter/${router.currentRoute.value.params.id}`
    );

    const params = computed(() => {
      if (ModelConfig.value) {
        return ModelConfig.value.parameters.parameterList;
      } else {
        return [];
      }
    });

    const disabledFlag = computed(() => {
      for (let i = 0; i < parameterList.value.length; i++) {
        if (parameterList.value[i].key === "") return true;
      }
      return false;
    });

    const init = async () => {
      const res = await getModelConfig(props.modelId!);
      if (res) {
        ModelConfig.value = res.data;
        for (
          let i = 0;
          i < ModelConfig.value!.parameters.parameterList.length;
          i++
        ) {
          parameterList.value.push({ key: "", value: "" });
        }
      }
    };

    const beforUpload = () => {
      loading.value = true;
    };

    const responseHandle = (val: {
      index: number;
      key: string;
      value: string;
    }) => {
      parameterList.value[val.index].key = val.key;
      parameterList.value[val.index].value = val.value;
      loading.value = false;
    };

    const confirmClick = () => {
      const temp = [];
      parameterList.value.forEach((item, index) => {
        temp.push({
          parameter: params.value[index],
          key: item.key,
          value: item.value,
        });
      });
      context.emit("paramSettingCall", parameterList.value);
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      await init();
      skeletonFlag.value = false;
    });

    return {
      loading,
      skeletonFlag,
      ModelConfig,
      params,
      parameterList,
      disabledFlag,
      header,
      action,
      confirmClick,
      responseHandle,
      beforUpload,
    };
  },
});
</script>

<style lang="scss" scoped>
.param-setting {
  .des {
    font-size: 17px;
    margin-bottom: 20px;
  }
  .title {
    font-weight: 500;
  }
  .el-divider {
    margin-top: 10px;
  }

  .el-row {
    margin-top: 10px;
  }
  .label {
    font-size: 12px;
    width: calc(100% - 15px);
    display: flex;
    color: black;
    word-wrap: break-word;
    word-break: break-all;
    .icon-svg {
      width: 10px;
      height: 10px;
    }
  }
  .file-path {
    height: 32px;
    display: flex;
    .el-input {
      width: calc(100% - 50px);
      margin-right: 10px;
    }
  }
  .input {
    .el-input {
      width: calc(100% - 50px);
    }
  }
  .description {
    color: #9292b5;
    font-size: 12px;
    width: calc(100% - 15px);
    word-wrap: break-word;
    word-break: break-all;
  }

  .btn {
    position: relative;
    height: 40px;
    .el-button {
      position: absolute;
      right: 0px;
      bottom: 0px;
      box-sizing: border-box;
      border: solid 1px #081c42;
      color: #081c42;
      &:hover {
        background: #f5f6f8;
      }
    }
  }
}
.my-header {
  font-size: 20px;
  color: #969696;
  span {
    color: black;
  }
}
</style>