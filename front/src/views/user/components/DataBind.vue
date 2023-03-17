<template>
  <div class="data-bind-main">
    <el-table :data="tableData" height="300px">
      <el-table-column label="名称" prop="name" />
      <el-table-column label="大小" prop="size" />
      <el-table-column align="right">
        <template #header>
          <el-button type="primary" circle size="small" @click="addClick">
            <el-icon><Plus /></el-icon>
          </el-button>
        </template>
        <template #default="scope">
          <el-button
            size="small"
            type="info"
            v-if="isAudit(scope.row)"
            @click="auditClick(scope.row)"
            >...</el-button
          >
          <el-button
            size="small"
            type="primary"
            v-if="isView(scope.row)"
            @click="viewClick(scope.row)"
            ><el-icon><View /></el-icon
          ></el-button>
          <el-button
            size="small"
            type="success"
            @click="downloadClick(scope.row)"
            ><el-icon><Download /></el-icon
          ></el-button>
          <el-button size="small" type="danger" @click="deleteClick(scope.row)"
            ><el-icon><Delete /></el-icon
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogFlag" width="800px" :show-close="false">
      <div class="dialogTable">
        <div class="table-head">
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
          :data="dialogTableData"
          height="300px"
          @cell-dblclick="dblclick"
          v-loading="loading"
          :row-class-name="tableRowClassName"
        >
          <el-table-column label="名称" prop="name">
            <template #default="scope">
              <div style="display: flex; align-items: center">
                <svg style="width: 20px; height: 20px">
                  <use :xlink:href="getIcon(scope.row.folder)"></use>
                </svg>
                <span style="margin-left: 10px">{{ scope.row.name }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="大小" prop="size" width="120" />
          <el-table-column align="right" width="140">
            <template #header>
              <p @click="listShow = true">
                已选：{{ tempTableData.length }} 条数据
              </p>
            </template>
            <template #default="scope">
              <el-checkbox
                v-model="scope.row.flag"
                size="large"
                v-if="!scope.row.folder"
                @change="checkboxChange(scope.row)"
              />
            </template>
          </el-table-column>
        </el-table>
        <div class="btn">
          <el-button type="primary" plain @click="commit">确定</el-button>
        </div>
      </div>

      <el-dialog v-model="listShow" width="500px" :show-close="false">
        <div class="list">
          <el-table :data="tempTableData" height="250px">
            <el-table-column label="名称" prop="name" />
            <el-table-column align="right" width="120">
              <template #header> {{ tempTableData.length }} 条 </template>
              <template #default="scope">
                <el-button
                  size="small"
                  type="danger"
                  @click="listDelete(scope.row)"
                  ><el-icon><Delete /></el-icon
                ></el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-dialog>
    </el-dialog>

    <el-dialog v-model="dataPreviewFlag" width="900px">
      <data-preview :fileInfo="fileInfo" v-if="dataPreviewFlag"></data-preview>
    </el-dialog>

    <el-dialog v-model="visualCompareFlag" width="900px">
      <visual-compare
        v-if="visualCompareFlag"
        :compareInfo="compareInfo"
      ></visual-compare>
    </el-dialog>
  </div>
</template>

<script lang="ts">
type DialogTableType = {
  id: string;
  name: string;
  folder: boolean;
  size: string;
  flag: boolean;
  parentId: string;
  visualType?: string;
  visualId?: string;
  view?: string;
};
import { defineComponent, onMounted, ref } from "vue";
import { findByFolderId, getDownloadURL } from "@/api/request";
import router from "@/router";
import DataPreview from "./DataPreview.vue";
import VisualCompare from "./VisualCompare.vue";
import { useStore } from "@/store";
import { decrypt } from "@/utils/auth";
import { prefix } from "@/prefix";
export default defineComponent({
  components: { DataPreview, VisualCompare },
  emits: ["changeData"],
  setup(props, context) {
    const store = useStore();
    const dialogFlag = ref(false);
    const listShow = ref(false);
    const dialogTableData = ref<DialogTableType[]>([]);
    const path = ref<{ name: string; parentId: string; id: string }[]>([]);
    const loading = ref(false);
    const tempTableData = ref<DialogTableType[]>([]);
    const tableData = ref<DialogTableType[]>([]);

    const fileInfo = ref<DialogTableType>();
    const dataPreviewFlag = ref(false);
    const visualCompareFlag = ref(false);

    const compareInfo = ref<{
      id: string;
      oldVisualId: string;
      oldVisualType: string;
      visualType: string;
      visualId: string;
      time: string;
      fileName: string;
    }>();

    const getIcon = (floder: boolean) => {
      if (floder) {
        return "#icon-wenjianjia";
      } else {
        return "#icon-wenjian";
      }
    };

    const addClick = () => {
      tempTableData.value = [];
      tableData.value.forEach((item) => {
        tempTableData.value.push(item);
      });
      dialogFlag.value = true;
    };

    const isAudit = (param: DialogTableType) => {
      if (param.visualType === "audit") return true;
      else return false;
    };

    const isView = (param: DialogTableType) => {
      if (param.visualType != "" && param.visualType != "audit") return true;
      else return false;
    };

    const auditClick = (val: any) => {
      const json = JSON.parse(val.visualId);
      json["fileName"] = val.name;
      json["id"] = val.id;
      compareInfo.value = json;
      visualCompareFlag.value = true;
    };

    const viewClick = (val: any) => {
      fileInfo.value = val;
      dataPreviewFlag.value = true;
    };

    const downloadClick = async (val: any) => {
      const key = await getDownloadURL(val.id);
      if (key != null && (key as any).code === 0) {
        const token = decrypt(key.data, store.state.user.id);
        window.location.href = `${prefix}file/downloadLocalFile/${store.state.user.id}/${token}`;
      }
    };

    const deleteClick = (val: DialogTableType) => {
      for (let i = 0; i < tableData.value.length; i++) {
        if (val.id === tableData.value[i].id) {
          tableData.value.splice(i, 1);
          val.flag = false;
          context.emit("changeData", tableData.value);
        }
      }
    };

    const checkboxChange = (val: DialogTableType) => {
      if (val.flag) {
        tempTableData.value.push(val);
      } else {
        for (let i = 0; i < tempTableData.value.length; i++) {
          if (tempTableData.value[i].id === val.id) {
            tempTableData.value.splice(i, 1);
            return;
          }
        }
      }
    };

    const checkFlag = (id: string) => {
      for (let i = 0; i < tempTableData.value.length; i++) {
        if (tempTableData.value[i].id === id) {
          return true;
        }
      }
      return false;
    };

    const listDelete = (row: DialogTableType) => {
      for (let i = 0; i < tempTableData.value.length; i++) {
        if (tempTableData.value[i].id === row.id) {
          tempTableData.value.splice(i, 1);
          row.flag = false;
          return;
        }
      }
    };

    const tableRowClassName = ({
      row,
      rowIndex,
    }: {
      row: DialogTableType;
      rowIndex: number;
    }) => {
      if (row.flag) {
        return "selected";
      } else {
        return "";
      }
    };

    const transitionData = (val: any) => {
      if ("fileName" in val) {
        dialogTableData.value.push({
          id: val.id,
          name: val.fileName,
          folder: false,
          size: val.size,
          flag: checkFlag(val.id),
          parentId: val.folderId,
          visualId: val.visualId,
          visualType: val.visualType,
          view: val.view,
        });
      } else {
        dialogTableData.value.push({
          id: val.id,
          name: val.folderName,
          folder: true,
          size: "",
          flag: false,
          parentId: val.parentId,
        });
      }
    };

    const dblclick = async (row: DialogTableType) => {
      if (row.folder) {
        loading.value = true;
        const data = await findByFolderId(row.id);
        if (data != null && (data as any).code === 0) {
          dialogTableData.value = [];
          (data.data as any[]).forEach((item) => {
            transitionData(item);
          });
          path.value.push({
            id: row.id,
            name: row.name,
            parentId: row.parentId,
          });
        }
        loading.value = false;
      }
    };

    const backClick = async () => {
      if (path.value.length > 0) {
        loading.value = true;
        let parentId: string = "-1";
        if (path.value[path.value.length - 1].parentId != "") {
          parentId = path.value[path.value.length - 1].parentId;
        }
        const dataList = await findByFolderId(parentId);
        if (dataList != null && (dataList as any).code === 0) {
          dialogTableData.value = [];
          (dataList.data as any[]).forEach((item) => {
            transitionData(item);
          });
          path.value.pop();
        }
        loading.value = false;
      }
    };

    const getTableData = (files: any[]) => {
      tableData.value = [];
      files.forEach((item) => {
        tableData.value.push({
          id: item.id,
          name: item.fileName,
          folder: false,
          size: item.size,
          flag: true,
          parentId: "",
          visualId: item.visualId,
          visualType: item.visualType,
          view: item.view,
        });
      });
    };

    const clearData = () => {
      tableData.value = [];
      tempTableData.value = [];
      dialogTableData.value.forEach((item) => {
        item.flag = false;
      });
    };

    const commit = () => {
      tableData.value = [];
      tempTableData.value.forEach((item) => {
        tableData.value.push(item);
      });
      context.emit("changeData", tableData.value);
      dialogFlag.value = false;
    };

    onMounted(async () => {
      if (router.currentRoute.value.params.files != undefined) {
        getTableData(router.currentRoute.value.params.files as any[]);
      }

      const data = await findByFolderId("-1");
      if (data != null && (data as any).code === 0) {
        dialogTableData.value = [];
        (data.data as any[]).forEach((item) => {
          transitionData(item);
        });
      }
    });

    return {
      tableData,
      addClick,
      deleteClick,
      dialogFlag,
      getIcon,
      dialogTableData,
      path,
      backClick,
      dblclick,
      loading,
      checkboxChange,
      tempTableData,
      tableRowClassName,
      listShow,
      listDelete,
      commit,
      clearData,
      getTableData,
      isAudit,
      isView,
      auditClick,
      viewClick,
      downloadClick,
      fileInfo,
      dataPreviewFlag,
      visualCompareFlag,
      compareInfo,
    };
  },
});
</script>


<style lang="scss" scoped>
.data-bind-main {
  width: 100%;
  border: solid 0.5px #ebeef5;
  padding: 0 5px;

  .dialogTable {
    padding: 15px 10px 10px 10px;
    cursor: pointer;
    .table-head {
      height: 25px;
      display: flex;
      position: relative;
      .el-icon {
        cursor: pointer;
      }
      .path {
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
      .btn {
        position: absolute;
        right: 0px;
      }
    }
    .btn {
      text-align: center;
      margin-top: 10px;
    }
    .el-checkbox {
      height: 20px;
    }
    .el-table {
      /deep/ .selected {
        --el-table-tr-bg-color: #f0f9eb;
      }
    }

    .list {
      padding: 0px 10px;
    }
  }
}
</style>