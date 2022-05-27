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
        <el-button size="small" @click="dialogCreateFolder = true"
          >创建文件夹</el-button
        >
        <el-button size="small">刷新</el-button>
        <el-button type="info" size="small" @click="dialogUpload = true"
          >上传</el-button
        >
      </div>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      height="calc(80vh - 23px)"
      :default-sort="{ prop: 'name', order: 'ascending' }"
      @row-contextmenu="contextMenuClick"
      @cell-dblclick="dblclick"
      highlight-current-row
      class="table"
    >
      <el-table-column type="selection" width="40" />
      <el-table-column
        prop="name"
        label="名称"
        sortable
        :sort-by="['folder', 'name']"
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

    <el-dialog
      v-model="dialogCreateFolder"
      width="200px"
      :show-close="false"
      :modal="false"
    >
      <folder-dialog
        @createFolder="createFolder"
        v-if="dialogCreateFolder"
        :tableData="tableData"
      ></folder-dialog>
    </el-dialog>

    <el-dialog v-model="dialogUpload" width="30%" :show-close="false">
      <upload-dialog
        :level="level"
        :parentId="path.length > 0 ? path[path.length - 1].id : '-1'"
      ></upload-dialog>
    </el-dialog>

    <user-folder-context-menu
      class="user-folder-context-menu"
      v-show="folderFlag"
      :contextMenuInstance="contextMenuInstance"
      @delSuccess="delSuccess"
      @rename="contextRename"
    ></user-folder-context-menu>
  </div>
</template>

<script lang="ts">
import { defineComponent, nextTick, onMounted, ref } from "vue";
import UserFolderContextMenu from "@/components/contextMenu/UserFolderContextMenu.vue";
import UploadDialog from "./UploadDialog.vue";
import FolderDialog from "./FolderDialog.vue";
import { findByLevel, addFile, findByParentId, rename } from "@/api/request";
import { dateFormat, uuid } from "@/utils/common";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: {
    UserFolderContextMenu,
    UploadDialog,
    FolderDialog,
  },
  setup() {
    const folderFlag = ref(false);
    const renameId = ref("");
    const tableData = ref<any[]>([]);
    const path = ref<{ name: string; parentId: string; id: string }[]>([]);
    const dialogUpload = ref(false);
    const dialogCreateFolder = ref(false);
    const level = ref(0);
    const contextMenuInstance = ref({});
    const renameValue = ref("");
    let oldName = "";

    const contextMenuClick = (row: any, column: any, event: any) => {
      event.preventDefault();
      folderFlag.value = false;
      folderFlag.value = true;
      contextMenuInstance.value = row;
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

    const dblclick = async (row: any) => {
      if (row.folder) {
        const dataList = await findByParentId(row.id);
        tableData.value = dataList.data;
        path.value.push({
          name: row.name,
          parentId: row.parent_id,
          id: row.id,
        });
        level.value = level.value + 1;
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

    const date = (time: string) => {
      return dateFormat(time, "yyyy-MM-dd");
    };

    const createFolder = async (val: string) => {
      const data = await addFile({
        id: uuid(),
        name: val,
        address: "",
        fileName: "",
        level: level.value,
        parentId:
          path.value.length > 0 ? path.value[path.value.length - 1].id : "-1",
        meta: "",
        folder: true,
      });
      dialogCreateFolder.value = false;
      if ((data as any).code === 0) {
        tableData.value.push({
          name: val,
          level: level.value,
          parentId:
            path.value.length > 0 ? path.value[path.value.length - 1].id : "-1",
          folder: true,
          create_time: date(new Date().toString()),
        });
      } else {
        notice("error", "错误", "创建文件失败！");
      }
    };

    const delSuccess = () => {
      tableData.value.forEach((item, index) => {
        if (item.id === (contextMenuInstance.value as any).id) {
          tableData.value.splice(index, 1);
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
      if (oldName != renameValue.value) {
        const data = await rename({
          id: (contextMenuInstance.value as any).id,
          name: renameValue.value,
        });
        if (data != null && (data as any).code === 0) {
          tableData.value.forEach((item) => {
            if (item.id === (contextMenuInstance.value as any).id) {
              item.name = renameValue.value;
            }
          });
          notice("success", "成功", "重命名成功");
        }
      }
      renameId.value = "";
    };

    onMounted(async () => {
      const tableList = await findByLevel(level.value);
      if (tableList != null) {
        tableData.value = tableList.data;
      }
    });

    return {
      tableData,
      path,
      contextMenuClick,
      dblclick,
      backClick,
      folderFlag,
      dialogUpload,
      dialogCreateFolder,
      createFolder,
      date,
      level,
      contextMenuInstance,
      delSuccess,
      contextRename,
      renameId,
      renameValue,
      blurHandle,
      enterHandle,
    };
  },
});
</script>


<style lang="scss" scoped>
.resource-main {
  position: relative;
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