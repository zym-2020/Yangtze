<template>
  <div class="pro">
    <div class="head" title="hah">项目管理</div>
    <div class="icon">
      <svg style="width: 20px; height: 20px" @click="open">
        <title>打开工程</title>
        <use xlink:href="#icon-wenjianjia"></use>
      </svg>
      <el-divider direction="vertical" />
      <svg style="width: 20px; height: 20px; margin-right: 100px">
        <title>移除工程</title>
        <use xlink:href="#icon-wenjianjiashanchu"></use>
      </svg>
      <el-divider direction="vertical" />
      <svg style="width: 20px; height: 20px">
        <title>创建工程</title>
        <use xlink:href="#icon-chuangjianrenwu"></use>
      </svg>
      <el-divider direction="vertical" />
      <svg style="width: 20px; height: 20px">
        <title>项目管理</title>
        <use xlink:href="#icon-liebiao-2"></use>
      </svg>
    </div>
    <div class="detail">
      <div v-if="flag == false">
        <el-empty description="请添加项目！" />
      </div>
      <div v-else>
        <el-tree
          :data="resource"
          :props="defaultProps"
          :highlight-current="true"
          :default-expand-all="true"
        />
      </div>
    </div>

    <el-dialog v-model="openFlag" width="600px">
      <template #title>
        <svg style="width: 20px; height: 20px">
          <use xlink:href="#icon-service"></use>
        </svg>
        请选择要添加的工程
      </template>
      <open-project v-if="openFlag" @selectProjectId="selectProjectId" />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  onMounted,
  reactive,
  ref,
  watch,
} from "vue";
import { getCurrentProjectId, getCurrentProjectName } from "@/utils/project";
import { getResult } from "@/api/request";
import OpenProject from "@/components/projectDialog/OpenProject.vue";
import { ProjectResult } from "./type";
import { useStore } from "@/store";

interface Children {
  label: string;
  children: Children[];
  address?: string;
  type?: string;
  classify?: string;
  show?: boolean;
}
export default defineComponent({
  components: {
    OpenProject,
  },

  setup() {
    const store = useStore();
    const flag = ref(false);
    const openFlag = ref(false);
    const selectId = ref("");
    const open = () => {
      openFlag.value = true;
    };
    const defaultProps = {
      children: "children",
      label: "label",
    };

    const resource = computed(() => {
      const result: Children[] = [
        {
          label: getCurrentProjectName() as string,
          children: [
            {
              label: "基础数据",
              children: [],
            },
            {
              label: "分析结果",
              children: [],
            },
          ],
        }
      ];
      store.state.resource.underlying.forEach((item) => {
        result[0].children[0].children.push({
          label: item.name,
          type: item.type,
          address: item.address,
          show: item.hasTiles,
          children: []
        })
      });
      store.state.resource.analyse.forEach((item) => {
        result[0].children[1].children.push({
          label: item.name,
          type: item.type,
          address: item.address,
          show: item.hasTiles,
          classify: item.classify,
          children: []
        })
      });
      return result;
    });

    const selectProjectId = async () => {
      if (
        getCurrentProjectId() != null &&
        selectId.value != getCurrentProjectId()
      ) {
        selectId.value = getCurrentProjectId() as string;
        let data = await getResult(selectId.value);
        if (data != null) {
          let temp: ProjectResult = JSON.parse(data.data);
          classify(temp);
          flag.value = true;
        }
      }
      openFlag.value = false;
    };

    const classify = (projectResult: ProjectResult) => {
      store.commit("SET_BASE_DATA", []);
      store.commit("SET_ANALYSE", []);
      projectResult.layerDataList.forEach((item) => {
        store.commit("ADD_BASE_DATA", {
          name: item.name,
          address: item.data,
          id: item.id,
          type: item.type,
          hasTiles: item.show,
        });
      });
      projectResult.analysisResultList.forEach((item) => {
        store.commit("ADD_ANALYSE", {
          name: item.name,
          address: item.address,
          type: item.type,
          classify: item.classify,
          hasTiles: item.show,
        });
      });
    };

    onMounted(async () => {
      if (getCurrentProjectId() != null) {
        flag.value = true;
        selectId.value = getCurrentProjectId() as string;
        let data = await getResult(selectId.value);
        if (data != null) {
          let temp: ProjectResult = JSON.parse(data.data);
          classify(temp);
        }
      } else {
        flag.value = false;
      }
    });
    return {
      open,
      flag,
      openFlag,
      selectProjectId,
      defaultProps,
      resource,
    };
  },
});
</script>


<style lang="scss" scoped>
.pro {
  width: 300px;
  border-left: solid 0.5px #dcdfe6;
  background: #f0f0f0;
  .head {
    background: rgba($color: #c8d5e3, $alpha: 0.4);
    height: 30px;
    line-height: 30px;
    padding-left: 10px;
  }
  .icon {
    height: 20px;
    line-height: 20px;
    padding-left: 10px;
    padding-top: 5px;
    .el-divider {
      margin-bottom: 8px;
    }
    svg {
      cursor: pointer;
    }
  }
  .detail {
    border: solid 0.5px;
    margin: 5px;
    height: calc(100% - 65px);
    background: white;
  }
}
</style>