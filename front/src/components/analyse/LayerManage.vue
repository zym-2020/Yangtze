<template>
  <div class="layer-manage">
    <div class="bottom-resize"></div>
    <div class="layer-manage-body">
      <div class="input">
        <el-input v-model="serach" :suffix-icon="Search" />
      </div>
      <div class="content">
        <div class="scroll">
          <el-scrollbar>
            <div>
              <el-tree :data="treeData" :props="defaultProps">
                <template #default="{ data }">
                  <div class="custom">
                    <el-checkbox
                      v-model="data.flag"
                      :label="data.label"
                      size="large"
                    />
                  </div>
                </template>
              </el-tree>
            </div>
          </el-scrollbar>
        </div>
      </div>
      <div class="bottom">
        <strong style="margin-left: 30px">图层管理</strong>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
type Tree = {
  id: string;
  label: string;
  visualType: string;
  flag: boolean;
  children: Tree[];
};
import { defineComponent, ref } from "vue";
import { Search } from "@element-plus/icons-vue";
export default defineComponent({
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const serach = ref("");
    const treeData = ref<Tree[]>([]);

    const addLayer = (val: {
      id: string;
      name: string;
      visualType: string;
    }) => {
      treeData.value.unshift({
        id: val.id,
        label: val.name,
        visualType: val.visualType,
        children: [],
        flag: true,
      });
    };

    return {
      Search,
      serach,
      treeData,
      defaultProps,
      addLayer,
    };
  },
});
</script>

<style lang="scss" scoped>
.layer-manage {
  position: relative;
  padding: 5px;
  .bottom-resize {
    height: 5px;
    position: absolute;
    top: 0;
    left: 0;
    cursor: row-resize;
    width: 100%;
  }
  .layer-manage-body {
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
        .el-tree {
          background: none;
          /deep/ .el-tree-node__content {
            height: 40px;
          }
        }
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