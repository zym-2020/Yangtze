<template>
  <div class="param-setting">
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
              <el-input disabled v-model="parameterList[index]" />
              <el-button
                type="success"
                circle
                @click="fileAndPathClick(item.type, index)"
                ><el-icon><FolderOpened /></el-icon
              ></el-button>
            </div>
            <div class="input" v-if="item.type === 'input'">
              <el-input v-model="parameterList[index]" />
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
export default defineComponent({
  props: {
    modelId: {
      type: String,
    },
  },
  emits: ["paramSettingCall"],

  setup(props, context) {
    const skeletonFlag = ref(true);
    const ModelConfig = ref<ModelConfig>();

    const parameterList = ref<string[]>([]);

    const params = computed(() => {
      if (ModelConfig.value) {
        return ModelConfig.value.parameters.parameterList;
      } else {
        return [];
      }
    });

    const disabledFlag = computed(() => {
      for (let i = 0; i < parameterList.value.length; i++) {
        if (parameterList.value[i] === "") return true;
      }
      return false;
    });

    const init = async () => {
      const res = await getModelConfig(props.modelId!);
      if (res) {
        ModelConfig.value = res.data;
        parameterList.value = new Array(
          ModelConfig.value?.parameters.parameterList.length
        ).fill("");
      }
    };

    const fileAndPathClick = (type: string, index: number) => {};

    const confirmClick = () => {
      context.emit("paramSettingCall", parameterList.value);
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      await init();
      skeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      ModelConfig,
      params,
      parameterList,
      disabledFlag,
      confirmClick,
      fileAndPathClick,
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