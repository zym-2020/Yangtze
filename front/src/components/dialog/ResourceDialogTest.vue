<template>
  <div class="resource-dialog">
    <div class="table">
      <div class="head">
        <el-icon size="20px" @click="backClick"><arrow-left /></el-icon>
        <div class="path">
          <div class="path-item">user</div>
          <div class="path-item separate">/</div>
          <div v-for="(item, index) in path" :key="index" class="item">
            <div class="path-item">{{ item.name }}</div>
            <div class="path-item separate">/</div>
          </div>
        </div>
      </div>
      <el-table
        :data="tableData"
        style="width: 100%"
        :default-sort="{ prop: 'name', order: 'descending' }"
        @cell-dblclick="dblclick"
        highlight-current-row
        :height="460"
      >
        <el-table-column
          prop="name"
          label="名称"
          sortable
          :sort-by="['folder', 'name']"
        >
        <!-- el-table的具名插槽 -->
          <template #default="scope">
            <div style="display: flex; align-items: center">
              <svg style="width: 20px; height: 20px" @click="open">
                <use
                  :xlink:href="
                    scope.row.folder === true
                      ? '#icon-wenjianjia'
                      : '#icon-wenjian'
                  "
                ></use>
              </svg>
              <span style="margin-left: 10px">{{ scope.row.name }}</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column
          prop="create_time"
          label="上传时间"
          sortable
          :sort-by="['folder', 'create_time']"
        >
          <template #default="scope">
            <span>{{ date(scope.row.create_time) }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="size"
          label="大小"
          sortable
          :sort-by="['folder', 'size']"
        />
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { findByLevel, findByParentId } from "@/api/request";
import { dateFormat } from "@/utils/common";
import { notice } from "@/utils/notice";
export default defineComponent({
  emits: ["selectedFile"],
  props: {
    type: {
      type: String,
    },
  },
  setup(props, context) {
    const tableData = ref([]);
    const level = ref(0);
    const path = ref<{ name: string; parentId: string; id: string }[]>([]);
    const fileList=ref<string[]>([])

    const date = (time: string) => {
      return dateFormat(time, "yyyy-MM-dd");
    };

    const dblclick = async (row: any) => {
      if (row.folder ) {
        //进入文件夹
        const dataList = await findByParentId(row.id);
        tableData.value = dataList.data;
        path.value.push({
          name: row.name,
          parentId: row.parent_id,
          id: row.id,
        });
        level.value = level.value + 1;
      } else {
        const tempArr = row.name.split(".");
        if (tempArr.length > 1 && tempArr[tempArr.length - 1] === "zip") {
          fileList.value.push(row.id)
          context.emit("selectedFile", { file: fileList.value, type: props.type });
        } else {
          notice("warning", "警告", "请选择zip文件");
        }
      }
    };

    const backClick = async () => {
      if (path.value.length > 0) {
        const dataList = await findByParentId(
          path.value[path.value.length - 1].parentId
        );
        if (dataList != null) {
          tableData.value = dataList.data;
          path.value.pop();
          level.value = level.value - 1;
        }
      }
    };

    onMounted(async () => {
      const tableList = await findByLevel(level.value);
      if (tableList != null) {
        tableData.value = tableList.data;
      }
    });
    return {
      tableData,
      dblclick,
      backClick,
      date,
      path,
    };
  },
});
</script>


<style lang="scss" scoped>
.resource-dialog {
  height: 500px;
  padding: 20px 10px 10px 10px;
  background: #a6bed7;
  .table {
    height: 100%;
    background: white;
    .head {
      height: 40px;
      display: flex;
      font-size: 16px;
      .el-icon {
        margin-top: 10px;
        margin-left: 5px;
        cursor: pointer;
      }
      .path {
        margin-top: 10px;
        width: 60%;
        display: flex;
        .item {
          display: flex;
        }
        .path-item {
          height: 20px;
          line-height: 20px;
        }
        .separate {
          color: #b7bbc3;
          margin: 0 5px;
        }
      }
    }
    .el-table {
      cursor: pointer;
    }
  }
}
</style>