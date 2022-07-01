<template>
  <div class="left-tools">
    <div class="title">分析工具</div>
    <div class="body">
      <div class="tools">
        <el-scrollbar>
          <el-tree
            :data="analyzeList"
            :props="defaultProps"
            node-key="id"
            :default-expanded-keys="treeExpandData"
            :highlight-current="true"
          >
            <template #default="{ node, data }">
              <span class="custom-tree-node" @dblclick="dblclickHandle(data)">
                <div class="img">
                  <img :src="data.icon" alt="" width="18" height="18" />
                </div>
                <div>{{ node.label }}</div>
              </span>
            </template>
          </el-tree>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
interface Tree {
  id: string;
  label: string;
  children?: Tree[];
  icon?: string;
}
import { computed, defineComponent, ref } from "vue";
import { treeData } from "./ts/leftToolTreeData";
import { useStore } from "@/store";
import { notice } from "@/utils/notice";
export default defineComponent({
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const store = useStore();
    const treeExpandData = ref(["1", "2"]);
    const analyzeList = ref<Tree[]>(treeData);

    const sectionDataExist = computed(() => {
      for (let i = 0; i < store.state.resource.layerDataList.length; i++) {
        if (store.state.resource.layerDataList[i].type === "riverBed") {
          return true;
        }
      }
      return false;
    });

    const dblclickHandle = (data: any) => {
      console.log(data)
      if (data.children === undefined || data.children.length === 0) {
        if (data.id === "1-1-1") {
          if (sectionDataExist.value) {
            console.log(store.state.other.editState)
            store.commit("SET_EDIT_STATE", {
              type: "section",
              flag: true,
              state: "",
            });
          } else {
            notice("warning", "警告", "请先添加河床数据源！");
          }
        } else if (data.id === '1-2-1') {
          
        }
      }
    };

    return {
      analyzeList,
      defaultProps,
      treeExpandData,
      dblclickHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.left-tools {
  height: calc(100vh - 60px);
  border-right: solid 1px;
  background: #e0e5eb;
  .title {
    padding-left: 10px;
    height: 30px;
    line-height: 30px;
  }
  .body {
    height: calc(100% - 30px);
    .tools {
      border: solid 1px;
      height: calc(100% - 10px);
      width: 290px;
      margin-left: 4px;
      background: white;
      .el-scrollbar {
        height: 100%;
        .custom-tree-node {
          display: flex;
          .img {
            margin-right: 5px;
            img {
              margin-top: 2px;
            }
          }
        }
      }
    }
  }
}
</style>