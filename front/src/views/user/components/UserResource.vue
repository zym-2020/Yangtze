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
        <el-button type="info" size="small" @click="dialogUpload = true"
          >上传</el-button
        >
      </div>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      height="calc(80vh - 23px)"
      @row-contextmenu="contextMenuClick"
      @cell-dblclick="dblclick"
      @selection-change="handleChange"
      highlight-current-row
      class="table"
    >
      <el-table-column type="selection" width="40" />
      <el-table-column
        prop="name"
        label="名称"
        sortable
        :sort-method="sortNameMethod"
      >
        <template #default="scope">
          <div v-if="renameId === scope.row.id">
            <el-input
              v-model="renameValue"
              v-inputFocus
              @blur="blurHandle"
              @keyup.enter="enterHandle"
            />
          </div>
          <div style="display: flex; align-items: center" v-else>
            <svg style="width: 20px; height: 20px" @click="open">
              <use :xlink:href="getIcon(scope.row)"></use>
            </svg>
            <span style="margin-left: 10px">{{ getName(scope.row) }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column
        prop="size"
        label="大小"
        sortable
        :sort-method="sortSizeMethod"
      >
        <template #default="scope">
          <span>{{ getSize(scope.row) }}</span>
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

    <el-dialog v-model="dialogUpload" width="30%" :show-close="false">
      <upload-dialog
        :level="level"
        :parentId="path.length > 0 ? path[path.length - 1].id : '-1'"
        @commitFile="dialogUpload = false"
      ></upload-dialog>
    </el-dialog>

    <el-dialog v-model="moveFlag" width="500px" :show-close="false">
      <move-dialog
        :moveItemList="moveItemList"
        @moveResult="moveResult"
      ></move-dialog>
    </el-dialog>

    <user-folder-context-menu
      class="user-folder-context-menu"
      v-show="folderFlag"
      :contextMenuInstance="contextMenuInstance"
      :selectList="selectTables"
      @delSuccess="delSuccess"
      @rename="contextRename"
      @unPack="contextUnpack"
      @move="move"
    ></user-folder-context-menu>
  </div>
</template>

<script lang="ts">
type Folder = {
  id: string;
  folderName: string;
  parentId: string;
};
type File = {
  id: string;
  fileName: string;
  visualType: string;
  size: string;
  uploader: string;
  location: string;
  time: string;
  folderId: string;
  visualId: string;
};
import { defineComponent, onMounted, ref } from "vue";
import UserFolderContextMenu from "@/components/contextMenu/UserFolderContextMenu.vue";
import UploadDialog from "./UploadDialog.vue";
import FolderDialog from "./FolderDialog.vue";
import { findByFolderId, addFolder, renameTemp } from "@/api/request";
import { notice } from "@/utils/notice";
import MoveDialog from "./MoveDialog.vue";
import NProgress from "nprogress";

NProgress.configure({ showSpinner: false });
export default defineComponent({
  components: {
    UserFolderContextMenu,
    UploadDialog,
    FolderDialog,
    MoveDialog,
  },
  setup() {
    const folderFlag = ref(false);
    const moveFlag = ref(false);
    const renameId = ref("");
    const tableData = ref<(Folder | File)[]>([]);
    const path = ref<{ name: string; parentId: string; id: string }[]>([]);
    const dialogUpload = ref(false);
    const dialogCreateFolder = ref(false);
    const level = ref(0);
    const contextMenuInstance = ref<any>({});
    const moveItemList = ref<any[]>([]);
    const selectTables = ref<any[]>([]);
    const renameValue = ref("");
    const folderNames = ref<string[]>([]);

    let oldName = "";

    const contextMenuClick = (row: any, column: any, event: any) => {
      event.preventDefault();
      folderFlag.value = false;
      folderFlag.value = true;

      contextMenuInstance.value = row;
      if (path.value.length > 0) {
        contextMenuInstance.value.parentName =
          path.value[path.value.length - 1].name;
      } else {
        contextMenuInstance.value.parentName = "user";
      }

      const menu: any = document.querySelector(".user-folder-context-menu");
      const table = document.querySelector(".el-tabs__content") as HTMLElement;
      if (
        table.clientWidth -
          (event.clientX -
            table.offsetLeft +
            10 -
            document.body.clientWidth * 0.1) <
        300
      ) {
        menu.style.left = table.clientWidth - 300 + "px"; //300 => menu.clientWidth     150 => menu.clientHeight
      } else {
        menu.style.left =
          event.clientX -
          table.offsetLeft +
          10 -
          document.body.clientWidth * 0.1 +
          "px";
      }
      if (table.clientHeight - (event.clientY - table.offsetTop - 60) < 150) {
        menu.style.top = event.clientY - table.offsetTop - 150 + "px";
      } else {
        menu.style.top = event.clientY - table.offsetTop - 60 + "px";
      }

      function closeMenu() {
        folderFlag.value = false;
        document.removeEventListener("click", closeMenu);
      }
      document.addEventListener("click", closeMenu);
    };

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

    const sortSizeMethod = (a: Folder | File, b: Folder | File) => {
      if ("fileName" in a && "fileName" in b) {
        return a.size.localeCompare(b.size) > 0;
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
          tableData.value = data.data;
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
          tableData.value = dataList.data;
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
          dialogCreateFolder.value = true;
        }
      });
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
        });
        notice("success", "成功", "文件夹创建成功！");
        dialogCreateFolder.value = false
      } else {
        notice("error", "错误", "文件夹创建失败！");
      }
    };

    const delSuccess = (val: any[]) => {
      tableData.value.forEach((item, index) => {
        for (let i = 0; i < val.length; i++) {
          if (item.id === val[i]) {
            tableData.value.splice(index, 1);
          }
        }
      });
    };

    const contextRename = () => {
      renameId.value = (contextMenuInstance.value as any).id;
      renameValue.value = (contextMenuInstance.value as any).name;
      oldName = (contextMenuInstance.value as any).name;
    };

    const blurHandle = () => {
      renameId.value = "";
    };

    const enterHandle = async () => {
      // if (oldName != renameValue.value) {
      //   const data = await renameTemp({
      //     id: (contextMenuInstance.value as any).id,
      //     name: renameValue.value,
      //   });
      //   if (data != null && (data as any).code === 0) {
      //     tableData.value.forEach((item) => {
      //       if (item.id === (contextMenuInstance.value as any).id) {
      //         item.name = renameValue.value;
      //       }
      //     });
      //     notice("success", "成功", "重命名成功");
      //   }
      // }
      // renameId.value = "";
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
        tableData.value = data.data;
      }
      NProgress.done();
    };

    const contextUnpack = async () => {
      await flushed();
    };

    const move = () => {
      if (selectTables.value.length > 0) {
        moveItemList.value = selectTables.value;
      } else {
        moveItemList.value.push(contextMenuInstance.value);
      }
      moveFlag.value = true;
    };

    const moveResult = async (val: string) => {
      if (val === "success") {
        await flushed();
      }
      moveFlag.value = false;
    };

    onMounted(async () => {
      const tableList = await findByFolderId("-1");
      if (tableList != null && (tableList as any).code === 0) {
        console.log(tableList.data);
        tableData.value = tableList.data;
      }
    });

    return {
      tableData,
      folderNames,
      path,
      contextMenuClick,
      dblclick,
      backClick,
      folderFlag,
      dialogUpload,
      dialogCreateFolder,
      openCreateFolder,
      createFolder,
      level,
      contextMenuInstance,
      delSuccess,
      contextRename,
      renameId,
      renameValue,
      blurHandle,
      enterHandle,
      getIcon,
      getName,
      getSize,
      sortNameMethod,
      sortSizeMethod,
      flushed,
      contextUnpack,
      move,
      moveFlag,
      moveResult,
      handleChange,
      moveItemList,
      selectTables,
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