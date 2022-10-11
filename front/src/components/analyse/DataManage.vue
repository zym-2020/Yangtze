<template>
  <div class="data-manage">
    <div class="data-manage-body">
      <div class="input">
        <el-input v-model="serach" :suffix-icon="Search" />
      </div>
      <div class="content">
        <div class="scroll">
          <el-scrollbar>
            <data-manage-card ref="dataManageCard"></data-manage-card>
          </el-scrollbar>
        </div>
      </div>
      <div class="bottom">
        <strong style="margin-left: 30px">数据管理</strong>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
import DataManageCard from "./card/DataManageCard.vue";
export default defineComponent({
  components: { DataManageCard },
  setup() {
    const serach = ref("");
    const dataManageCard = ref();

    const addData = (
      val: {
        fileId: string;
        fileName: string;
        dataListId: string;
        dataListName: string;
      }[]
    ) => {
      dataManageCard.value.addData(val);
    };

    return {
      serach,
      Search,
      dataManageCard,
      addData,
    };
  },
});
</script>


<style lang="scss" scoped>
.data-manage {
  padding: 5px;
  .data-manage-body {
    background: #f0f0f0;
    height: 100%;
    border-radius: 8px;
    .input {
      padding: 5px 10px;
    }
    .content {
      padding: 0 10px;
      height: calc(100% - 90px);
      .scroll {
        height: 100%;
      }
    }
    .bottom {
      height: 40px;
      margin-top: 10px;
      border-bottom-left-radius: 8px;
      border-bottom-right-radius: 8px;
      background: rgba($color: #abadb3, $alpha: 0.5);
      line-height: 40px;
      font-size: 20px;
    }
  }
}
</style>