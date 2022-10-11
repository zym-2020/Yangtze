<template>
  <div>
    <el-tree :data="data" :props="defaultProps">
      <template #default="{ data }">
        <div class="custom">
          <div class="icon">
            <el-icon v-if="data.flag"><FolderOpened /></el-icon>
            <el-icon v-else><Document /></el-icon>
          </div>
          <div class="text">
            <strong v-if="data.flag">{{ data.label }}</strong>
            <span v-else>{{ data.label }}</span>
          </div>
        </div>
      </template>
    </el-tree>
  </div>
</template>

<script lang="ts">
type Tree = {
  id: string;
  flag: boolean;
  label: string;
  children: Tree[];
};
import { Search, ArrowLeft } from "@element-plus/icons-vue";
import { defineComponent, ref } from "vue";
export default defineComponent({
  setup() {
    const defaultProps = {
      children: "children",
      label: "label",
    };
    const data = ref<Tree[]>([]);

    const addData = (
      param: {
        fileId: string;
        fileName: string;
        dataListId: string;
        dataListName: string;
      }[]
    ) => {
      param.forEach((item) => {
        let flag1 = true;
        for (let i = 0; i < data.value.length; i++) {
          if (data.value[i].id === item.dataListId) {
            let flag = true;
            for (let j = 0; j < data.value[i].children.length; j++) {
              if (data.value[i].children[j].id === item.fileId) {
                flag = false;
                break;
              }
            }
            if (flag) {
              data.value[i].children.push({
                id: item.fileId,
                label: item.fileName,
                flag: false,
                children: [],
              });
            }
            flag1 = false;
            break;
          }
        }
        if (flag1) {
          data.value.push({
            id: item.dataListId,
            label: item.dataListName,
            flag: true,
            children: [],
          });
          data.value[data.value.length - 1].children.push({
            id: item.fileId,
            label: item.fileName,
            flag: false,
            children: [],
          });
        }
      });
      console.log(data.value);
    };

    return {
      defaultProps,
      data,
      Search,
      addData,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-tree {
  background: none;
  /deep/ .el-tree-node__content {
    height: 40px;
  }
}
.custom {
  display: flex;
  .icon {
    margin-top: 12px;
    margin-right: 5px;
  }
  .text {
    line-height: 40px;
  }
}
</style>