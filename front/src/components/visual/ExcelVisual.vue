<template>
  <div class="excel-main">
    <el-row
      :gutter="20"
      justify="center"
      style="margin-bottom: 20px"
      v-if="tableNameList.length > 1"
    >
      <el-col :span="8" style="text-align: center">
        <el-card
          class="video"
          shadow="always"
          style="margin-top: 30px; margin-bottom: 10px"
          @click="changeExcel('left')"
        >
          <div class="body">
            <div class="text" style="margin-left: 20px">
              <strong>{{ tableNameList[leftIndex] }}</strong>
            </div>
            <div class="icon icon-left">
              <el-icon><ArrowLeftBold /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <el-card
          shadow="always"
          style="
            margin-top: 10px;
            margin-bottom: 10px;
            background-color: lightgrey;
          "
        >
          <div class="text">
            <strong>{{ tableNameList[centerIndex] }}</strong>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8" style="text-align: center">
        <el-card
          class="video"
          shadow="always"
          style="margin-top: 30px; margin-bottom: 10px"
          @click="changeExcel('right')"
        >
          <div class="body">
            <div class="text">
              <strong>{{ tableNameList[rightIndex] }}</strong>
            </div>
            <div class="icon icon-right">
              <el-icon><ArrowRightBold /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    
    <sand-content
      :visualId="sandContentList[centerIndex]"
      ref="sandContent"
      v-if="sandContentList.length != 0"
    />
    <suspension
      :visualId="suspensionList[centerIndex]"
      ref="suspension"
      v-if="suspensionList.length != 0"
    />
    <rate-and-direction
      :visualId="rateDirectionList[centerIndex]"
      ref="rateAndDirection"
      v-if="rateDirectionList.length != 0"
    />
    <salinity
      :visualId="salinityList[centerIndex]"
      ref="salinity"
      v-if="salinityList.length != 0"
    />
    <flow-sand-z
      :visualId="flowSandZList[centerIndex]"
      ref="flowSandZ"
      v-if="flowSandZList.length != 0"
    />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import SandContent from "./SandContent.vue";
import Suspension from "./Suspension.vue";
import RateAndDirection from "./RateAndDirection.vue";
import Salinity from "./Salinity.vue";
import FlowSandZ from "./FlowSand_Z.vue";
export default defineComponent({
  components: {
    SandContent,
    Suspension,
    RateAndDirection,
    Salinity,
    FlowSandZ,
  },
  props: {
    sandContentList: {
      type: Array,
    },
    suspensionList: {
      type: Array,
    },
    rateDirectionList: {
      type: Array,
    },
    salinityList: {
      type: Array,
    },
    flowSandZList: {
      type: Array,
    },
    tableNameList: {
      type: Array,
    },
  },
  setup(props) {
    const sandContent = ref();
    const suspension = ref();
    const rateAndDirection = ref();
    const salinity = ref();
    const flowSandZ = ref();
    const centerIndex = ref(0);
    const leftIndex = computed(() => {
      return (
        (centerIndex.value - 1 + (tableNameList.value as string[]).length) %
        (tableNameList.value as string[]).length
      );
    });
    const rightIndex = computed(() => {
      return (centerIndex.value + 1) % (tableNameList.value as string[]).length;
    });
    const tableNameList = computed(() => {
      return props.tableNameList;
    });
    const sandContentList = computed(() => {
      return props.sandContentList;
    });
    const suspensionList = computed(() => {
      return props.suspensionList;
    });
    const rateDirectionList = computed(() => {
      return props.rateDirectionList;
    });
    const salinityList = computed(() => {
      return props.salinityList;
    });
    const flowSandZList = computed(() => {
      return props.flowSandZList;
    });

    const changeExcel = async (handle: string) => {
      if (handle === "left") {
        centerIndex.value =
          (centerIndex.value - 1 + (tableNameList.value as string[]).length) %
          (tableNameList.value as string[]).length;
      } else {
        centerIndex.value =
          (centerIndex.value + 1) % (tableNameList.value as string[]).length;
      }

      if (sandContentList.value?.length != 0) {
        await sandContent.value.refreshData(
          (sandContentList.value as string[])[centerIndex.value]
        );
      } else if (suspensionList.value?.length != 0) {
        await suspension.value.refreshData(
          (suspensionList.value as string[])[centerIndex.value]
        );
      } else if (rateDirectionList.value?.length != 0) {
        await rateAndDirection.value.refreshData(
          (rateDirectionList.value as string[])[centerIndex.value]
        );
      } else if (salinityList.value?.length != 0) {
        await salinity.value.refreshData(
          (salinityList.value as string[])[centerIndex.value]
        );
      } else if (flowSandZList.value?.length != 0) {
        await flowSandZ.value.refreshData(
          (flowSandZList.value as string[])[centerIndex.value]
        );
      }
    };

    return {
      sandContent,
      suspension,
      rateAndDirection,
      salinity,
      flowSandZ,
      tableNameList,
      sandContentList,
      suspensionList,
      rateDirectionList,
      salinityList,
      flowSandZList,
      centerIndex,
      leftIndex,
      rightIndex,
      changeExcel,
    };
  },
});
</script>

<style lang="scss" scoped>
.excel-main {
  width: 100%;
  .el-card {
    cursor: pointer;
  }
  .body {
    width: 100%;
    position: relative;
    .icon {
      position: absolute;
      top: 2px;
      height: 20px;
      width: 20px;
    }
    .icon-right {
      right: 0px;
    }
    .icon-left {
      left: 0px;
    }
  }
  .text {
    width: calc(100% - 20px);
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    -o-text-overflow: ellipsis;
  }
}
</style>