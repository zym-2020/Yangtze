<template>
  <div class="resource-main">
    <div class="table-head">
      <el-icon size="20px"><arrow-left /></el-icon>
      <div class="path">
        <div class="path-item">user</div>
        <div class="path-item separate">/</div>
      </div>
      <div class="btn">
        <el-button size="small">创建文件夹</el-button>
        <el-button size="small">刷新</el-button>
        <el-button type="info" size="small" @click="dialogUpload = true"
          >上传</el-button
        >
      </div>
    </div>
    <el-table
      :data="tableData"
      style="width: 100%"
      max-height="calc(80vh - 23px)"
      :default-sort="{ prop: 'name', order: 'descending' }"
      @row-contextmenu="contextMenuClick"
      highlight-current-row
      class="table"
    >
      <el-table-column type="selection" width="40" />
      <el-table-column
        prop="name"
        label="名称"
        sortable
        :sort-by="['type', 'name']"
      >
        <template #default="scope">
          <div style="display: flex; align-items: center">
            <svg style="width: 20px; height: 20px" @click="open">
              <use
                :xlink:href="
                  scope.row.type === 'folder'
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
        prop="time"
        label="上传时间"
        sortable
        :sort-by="['type', 'time']"
      />
      <el-table-column
        prop="size"
        label="大小"
        sortable
        :sort-by="['type', 'size']"
      />
    </el-table>

    <el-dialog v-model="dialogUpload" width="30%" :show-close="false">
      <upload-dialog></upload-dialog>
    </el-dialog>

    <user-folder-context-menu
      class="user-folder-context-menu"
      v-show="folderFlag"
    ></user-folder-context-menu>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import UserFolderContextMenu from "@/components/contextMenu/UserFolderContextMenu.vue";
import UploadDialog from "./UploadDialog.vue";
export default defineComponent({
  components: {
    UserFolderContextMenu,
    UploadDialog,
  },
  setup() {
    const folderFlag = ref(false);
    const tableData = [
      {
        time: "2016-05-03",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-02",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-04",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-03",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-02",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-04",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },

      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-03",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-02",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-03",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-02",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-04",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-03",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-02",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-04",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },

      {
        time: "2016-05-01",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
      {
        time: "2016-05-03",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "folder",
      },
      {
        time: "2016-05-02",
        name: "Tom",
        size: "No. 189, Grove St, Los Angeles",
        type: "file",
      },
    ];
    const dialogUpload = ref(false);

    const contextMenuClick = (row: any, column: any, event: any) => {
      event.preventDefault();
      folderFlag.value = false;
      folderFlag.value = true;
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
      if (table.clientHeight - (event.clientY - table.offsetTop) < 150) {
        menu.style.top = event.clientY - table.offsetTop - 150 + "px";
      } else {
        menu.style.top = event.clientY - table.offsetTop + "px";
      }

      function closeMenu() {
        folderFlag.value = false;
        document.removeEventListener("click", closeMenu);
      }
      document.addEventListener("click", closeMenu);
    };



    return {
      tableData,
      contextMenuClick,
      folderFlag,
      dialogUpload,
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
    .path {
      width: 60%;
      display: flex;
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