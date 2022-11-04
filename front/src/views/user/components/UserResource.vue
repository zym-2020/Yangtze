<template>
  <div class="resource-main">
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
      <div class="btn">
        <el-button size="small" @click="openCreateFolder">创建文件夹</el-button>
        <el-button size="small" @click="flushed">刷新</el-button>
        <el-button type="info" size="small" @click="uploadClick"
          >上传</el-button
        >
        <input
          type="file"
          style="display: none"
          multiple="multiple"
          @change="checkFile($event)"
          v-if="isShowFile"
          ref="upload"
        />
      </div>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      height="calc(80vh - 23px)"
      @cell-dblclick="dblclick"
      @selection-change="handleChange"
      highlight-current-row
      class="table"
    >
      <el-table-column
        prop="name"
        label="名称"
        sortable
        :sort-method="sortNameMethod"
        width="800"
      >
        <template #default="scope">
          <div class="table-name">
            <el-checkbox
              v-model="scope.row.flag"
              size="large"
              @change="changeHandle(scope.row)"
            />
            <div style="display: flex; align-items: center">
              <svg style="width: 20px; height: 20px" @click="open">
                <use :xlink:href="getIcon(scope.row)"></use>
              </svg>
              <span style="margin-left: 10px">{{ getName(scope.row) }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="size" label="大小">
        <template #default="scope">
          <span>{{ getSize(scope.row) }}</span>
        </template>
      </el-table-column>

      <el-table-column align="right">
        <template #header>
          <el-button
            size="small"
            text
            type="danger"
            :disabled="selectList.length === 0"
            @click="batDelete"
            ><strong>批量删除</strong></el-button
          >
        </template>
        <template #default="scope">
          <el-button size="small" type="primary" v-if="isVisual(scope.row)"
            ><el-icon><View /></el-icon
          ></el-button>
          <el-button
            size="small"
            type="success"
            v-if="!isFolder(scope.row)"
            @click="downloadClick(scope.row)"
            ><el-icon><Download /></el-icon
          ></el-button>
          <el-button size="small" type="danger" @click="deleteClick(scope.row)"
            ><el-icon><Delete /></el-icon
          ></el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="dialogCreateFolder"
      width="200px"
      :show-close="false"
      :modal="false"
    >
      <folder-dialog
        @createFolder="createFolder"
        v-if="dialogCreateFolder"
        :folderNames="folderNames"
      ></folder-dialog>
    </el-dialog>
  </div>
</template>

<script lang="ts">
type Folder = {
  id: string;
  folderName: string;
  parentId: string;
  flag: boolean;
};
type File = {
  id: string;
  fileName: string;
  visualType: string;
  size: string;
  uploader: string;
  folderId: string;
  visualId: string;
  flag: boolean;
};
import { defineComponent, onMounted, ref } from "vue";
import { ElMessageBox } from "element-plus";
import FolderDialog from "./FolderDialog.vue";
import {
  findByFolderId,
  addFolder,
  deleteFilesOrFolders,
  getDownloadURL,
} from "@/api/request";
import { notice } from "@/utils/notice";
import NProgress from "nprogress";
import { prefix } from "@/prefix";
import { useStore } from "@/store";
import { decrypt } from "@/utils/auth";
import { uuid, getFileSize } from "@/utils/common";

NProgress.configure({ showSpinner: false });
export default defineComponent({
  components: {
    FolderDialog,
  },
  setup() {
    const store = useStore();
    const tableData = ref<(Folder | File)[]>([]);
    const path = ref<{ name: string; parentId: string; id: string }[]>([]);
    const dialogUpload = ref(false);
    const dialogCreateFolder = ref(false);
    const selectList = ref<{ id: string; type: string }[]>([]);

    const selectTables = ref<any[]>([]);
    const folderNames = ref<string[]>([]);

    const upload = ref<HTMLElement>();
    const isShowFile = ref(true);

    const getIcon = (item: Folder | File) => {
      if ("fileName" in item) {
        return "#icon-wenjian";
      } else {
        return "#icon-wenjianjia";
      }
    };

    const getName = (item: Folder | File) => {
      if ("fileName" in item) {
        return item.fileName;
      } else {
        return item.folderName;
      }
    };

    const getSize = (item: Folder | File) => {
      if ("fileName" in item) {
        return item.size;
      } else {
        return "";
      }
    };

    const isFolder = (item: Folder | File) => {
      if ("fileName" in item) {
        return false;
      } else {
        return true;
      }
    };

    const isVisual = (item: Folder | File) => {
      if ("fileName" in item) {
        if (item.visualType != "") {
          return true;
        } else {
          return false;
        }
      } else {
        return false;
      }
    };

    const sortNameMethod = (a: Folder | File, b: Folder | File) => {
      if ("fileName" in a && "fileName" in b) {
        return a.fileName.localeCompare(b.fileName) > 0;
      }
      if ("folderName" in a && "folderName" in b) {
        return a.folderName.localeCompare(b.folderName) > 0;
      }
      if ("fileName" in a && "folderName" in b) {
        return false;
      }
      if ("folderName" in a && "fileName" in b) {
        return true;
      }
    };

    const dblclick = async (row: Folder | File) => {
      if ("folderName" in row) {
        NProgress.start();
        const data = await findByFolderId(row.id);
        if (data != null && (data as any).code === 0) {
          transitionData(data.data);
          path.value.push({
            id: row.id,
            name: row.folderName,
            parentId: row.parentId,
          });
        }
        NProgress.done();
      }
    };

    const backClick = async () => {
      if (path.value.length > 0) {
        NProgress.start();
        let parentId: string = "-1";
        if (path.value[path.value.length - 1].parentId != "") {
          parentId = path.value[path.value.length - 1].parentId;
        }
        const dataList = await findByFolderId(parentId);
        if (dataList != null && (dataList as any).code === 0) {
          transitionData(dataList.data);
          path.value.pop();
        }
        NProgress.done();
      }
    };

    const handleChange = (selection: any) => {
      console.log(selection);
      selectTables.value = selection;
    };

    const openCreateFolder = () => {
      tableData.value.forEach((item) => {
        if ("folderName" in item) {
          folderNames.value.push(item.folderName);
        }
      });
      dialogCreateFolder.value = true;
    };

    const createFolder = async (val: string) => {
      const jsonData = {
        folderName: val,
        parentId:
          path.value.length > 0 ? path.value[path.value.length - 1].id : "",
      };
      const data = await addFolder(jsonData);
      if (data != null && (data as any).code === 0) {
        tableData.value.push({
          id: data.data.id,
          folderName: data.data.folderName,
          parentId: data.data.parentId,
          uploader: data.data.uploader,
          flag: false,
        });
        notice("success", "成功", "文件夹创建成功！");
        dialogCreateFolder.value = false;
      } else {
        notice("error", "错误", "文件夹创建失败！");
      }
    };

    const deleteClick = async (item: Folder | File) => {
      ElMessageBox.confirm("确定删除文件夹及文件夹以下内容", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          const json: { files: string[]; folders: string[] } = {
            files: [],
            folders: [],
          };
          if ("folderName" in item) {
            json.folders.push(item.id);
          } else {
            json.files.push(item.id);
          }
          const data = await deleteFilesOrFolders(json);
          if (data != null && (data as any).code === 0) {
            for (let i = 0; i < tableData.value.length; i++) {
              if (tableData.value[i].id === item.id) {
                tableData.value.splice(i, 1);
                break;
              }
            }
            notice("success", "成功", "删除成功");
          }
        })
        .catch(() => {});
    };

    const batDelete = async () => {
      ElMessageBox.confirm("确定删除文件夹及文件夹以下内容", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          const json: { files: string[]; folders: string[] } = {
            files: [],
            folders: [],
          };
          selectList.value.forEach((item) => {
            if (item.type === "file") {
              json.files.push(item.id);
            } else {
              json.folders.push(item.id);
            }
          });
          const data = await deleteFilesOrFolders(json);
          if (data != null && (data as any).code === 0) {
            for (let i = 0; i < tableData.value.length; i++) {
              for (let j = 0; j < selectList.value.length; j++) {
                if (tableData.value[i].id === selectList.value[j].id) {
                  tableData.value.splice(i, 1);
                  i = i - 1;
                  break;
                }
              }
            }
            selectList.value = [];
            notice("success", "成功", "删除成功");
          }
        })
        .catch(() => {});
    };

    const downloadClick = async (param: File) => {
      const key = await getDownloadURL(param.id);
      if (key != null && (key as any).code === 0) {
        const token = decrypt(key.data, store.state.user.id);
        window.location.href = `${prefix}file/downloadLocalFile/${store.state.user.id}/${token}`;
      }
    };

    const flushed = async () => {
      NProgress.start();
      let id = "";
      if (path.value.length === 0) {
        id = "-1";
      } else {
        id = path.value[path.value.length - 1].id;
      }
      const data = await findByFolderId(id);
      if (data != null && (data as any).code === 0) {
        transitionData(data.data);
      }
      NProgress.done();
    };

    const changeHandle = (param: File | Folder) => {
      if (param.flag) {
        if ("folderName" in param) {
          selectList.value.push({ id: param.id, type: "folder" });
        } else {
          selectList.value.push({ id: param.id, type: "file" });
        }
      } else {
        for (let i = 0; i < selectList.value.length; i++) {
          if (param.id === selectList.value[i].id) {
            selectList.value.splice(i, 1);
          }
        }
      }
    };

    const uploadClick = () => {
      upload.value?.click();
      isShowFile.value = false;
    };

    const checkFile = (val: any) => {
      console.log(123, val.target.files);
      isShowFile.value = true;

      for (let i = 0; i < val.target.files.length; i++) {
        store.commit("ADD_WAIT_ITEM", {
          id: uuid(),
          name: val.target.files[i].name,
          file: val.target.files[i],
          size: getFileSize(val.target.files[i].size),
        });
      }
      store.dispatch("uploadFiles", {
        parentId:
          path.value.length === 0 ? "" : path.value[path.value.length - 1].id,
      });
    };

    const transitionData = (param: any[]) => {
      tableData.value = [];
      param.forEach((item) => {
        if ("folderName" in item) {
          tableData.value.push({
            id: item.id,
            folderName: item.folderName,
            parentId: item.parentId,
            flag: false,
          });
        } else {
          tableData.value.push({
            id: item.id,
            fileName: item.fileName,
            visualType: item.visualType,
            visualId: item.visualId,
            size: item.size,
            uploader: item.uploader,
            folderId: item.folderId,
            flag: false,
          });
        }
      });
    };

    onMounted(async () => {
      const tableList = await findByFolderId("-1");
      if (tableList != null && (tableList as any).code === 0) {
        console.log(tableList.data);
        transitionData(tableList.data);
      }
    });

    return {
      tableData,
      folderNames,
      path,
      dblclick,
      backClick,
      dialogUpload,
      uploadClick,
      dialogCreateFolder,
      openCreateFolder,
      createFolder,
      getIcon,
      getName,
      getSize,
      sortNameMethod,
      flushed,
      handleChange,
      selectTables,
      isFolder,
      deleteClick,
      downloadClick,
      changeHandle,
      selectList,
      batDelete,
      isVisual,
      upload,
      checkFile,
      isShowFile,
    };
  },
});
</script>


<style lang="scss" scoped>
.resource-main {
  position: relative;
  height: 100%;
  .table-head {
    height: 25px;
    display: flex;
    position: relative;
    .el-icon {
      cursor: pointer;
    }
    .path {
      cursor: pointer;
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
  .table {
    cursor: pointer;
    .table-name {
      display: flex;
      .el-checkbox {
        margin-right: 5px;
        height: 30px;
      }
    }
  }

  /deep/.el-dialog {
    .el-dialog__header {
      padding: 0;
    }
    .el-dialog__body {
      padding: 0;
    }
  }
  .user-folder-context-menu {
    position: absolute;
    z-index: 99;
  }
}
</style>