<template>
  <div class="admin-user">
    <div class="main">
      <div class="operate">
        <el-button type="primary" @click="addFlag = true">
          <el-icon style="margin-right: 5px"><Plus /></el-icon>
          添加</el-button
        >
        <el-button type="danger" @click="batchDeleteHandle"
          ><el-icon style="margin-right: 5px"><Delete /></el-icon
          >批量删除</el-button
        >
        <el-button type="primary" class="search" @click="searchHandle">
          <el-icon style="margin-right: 5px"><Search /></el-icon>
          查询</el-button
        >
        <el-input v-model="input" placeholder="请输入关键词" @keydown.enter="searchHandle"/>
      </div> 
      <el-skeleton :rows="5" animated v-if="skeletonFlag"/>
      <el-table
        v-else
        :data="userList"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
          :selectable="checkSelect"
        />
        <el-table-column label="id" width="320">
          <template #default="scope">
            <span v-html="replaceHandle(scope.row.id)"></span>
          </template>
        </el-table-column>
        <el-table-column label="用户名" width="300">
          <template #default="scope">
            <strong
              ><span v-html="replaceHandle(scope.row.name)"></span
            ></strong>
          </template>
        </el-table-column>
        <el-table-column label="权限" width="100">
          <template #default="scope">
            <el-tag>{{ scope.row.role }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="email">
          <template #default="scope">
            <span v-html="replaceHandle(scope.row.email)"></span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="350">
          <template #default="scope">
            <el-button
              size="small"
              type="primary"
              @click="userInfoHandle(scope.row)"
              >详细信息</el-button
            >

            <el-button size="small" @click="resetHandle(scope.row.id)"
              >重置密码</el-button
            >
            <el-button
              size="small"
              type="danger"
              v-if="scope.row.role != 'admin'"
              @click="deleteHandle"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination
          background
          v-model:current-page="currentPage"
          layout="total, prev, pager, next, jumper"
          :page-size="16"
          :total="total"
          :pager-count="5"
          @current-change="handleCurrentChange"
        ></el-pagination>
      </div>
    </div>
    <el-dialog
      v-model="userInfoFlag"
      :show-close="false"
      width="440px"
      class="user-info"
    >
      <user-info-card :userInfo="userInfo" />
    </el-dialog>
    <el-dialog
      v-model="resetFlag"
      width="400px"
      title="重置密码"
      :close-on-click-modal="false"
    >
      <reset-password @cancel="resetFlag = false" :id="resetId" />
    </el-dialog>
    <el-dialog
      v-model="addFlag"
      width="400px"
      title="添加用户"
      :close-on-click-modal="false"
    >
      <add-user-card @cancel="addFlag = false" @addUser="addUserHandle" />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAllUserInfo, deleteUser, batchDelete } from "@/api/request";
import UserInfoCard from "@/components/admin/UserInfoCard.vue";
import ResetPassword from "@/components/admin/ResetPassword.vue";
import AddUserCard from "@/components/admin/AddUserCard.vue";
import { ElMessageBox } from "element-plus";
import { notice } from "@/utils/notice";
import NProgress from "nprogress";
export default defineComponent({
  components: { UserInfoCard, ResetPassword, AddUserCard },
  setup() {
    const userList = ref<any[]>([]);
    const skeletonFlag = ref(true)

    const total = ref(0);
    const currentPage = ref(1);
    const input = ref("");
    const keyword = ref("");

    const userInfoFlag = ref(false);
    const userInfo = ref<any>();

    const resetFlag = ref(false);
    const resetId = ref("");

    const selectedList = ref<any[]>([]);

    const addFlag = ref(false);

    const checkSelect = (row: any) => {
      if (row.role === "admin") {
        return false;
      } else {
        return true;
      }
    };

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(" + keyword.value + ")", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" + keyword.value + "</span>"
      );
      return currentStr;
    };

    const getData = async (page: number, size: number, keyword: string) => {
      NProgress.start();
      const data = await getAllUserInfo(page, size, keyword);
      console.log(data);
      if (data != null && (data as any).code === 0) {
        userList.value = data.data.list;
        total.value = data.data.total;
      }
      NProgress.done();
    };

    const handleCurrentChange = async (val: number) => {
      await getData(val - 1, 20, keyword.value);
      input.value = keyword.value;
    };

    const handleSelectionChange = (val: any[]) => {
      selectedList.value = val;
    };

    const userInfoHandle = (param: any) => {
      userInfoFlag.value = true;
      userInfo.value = param;
    };

    const resetHandle = (param: string) => {
      resetId.value = param;
      resetFlag.value = true;
    };

    const deleteHandle = (param: string) => {
      ElMessageBox.confirm("确定删除该用户？删除后无法恢复", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          const data = await deleteUser(param);
          if (data != null && (data as any).code === 0) {
            notice("success", "成功", "删除成功");
          }
        })
        .catch(() => {
          notice("info", "删除取消", "删除取消");
        });
    };

    const batchDeleteHandle = async () => {
      ElMessageBox.confirm("确定删除所选用户？删除后无法恢复", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(async () => {
          const data = await batchDelete(selectedList.value);
          if (data != null && (data as any).code === 0) {
            notice("success", "成功", "删除成功");
          }
        })
        .catch(() => {
          notice("info", "删除取消", "删除取消");
        });
    };

    const addUserHandle = async () => {
      await getData(0, 20, "");
      currentPage.value = 1;
      keyword.value = "";
      input.value = "";
      addFlag.value = false;
    };

    const searchHandle = async () => {
      keyword.value = input.value;
      await getData(0, 20, keyword.value);
    };

    onMounted(async () => {
      await getData(0, 20, keyword.value);
      skeletonFlag.value = false
    });

    return {
      userList,
      total,
      currentPage,
      replaceHandle,
      handleCurrentChange,
      checkSelect,
      input,
      userInfoFlag,
      userInfoHandle,
      userInfo,
      resetFlag,
      resetHandle,
      deleteHandle,
      handleSelectionChange,
      batchDeleteHandle,
      resetId,
      addFlag,
      addUserHandle,
      searchHandle,
      skeletonFlag
    };
  },
});
</script>

<style lang="scss" scoped>
.admin-user {
  padding: 20px;
  .main {
    background: white;
    padding: 20px;
    .operate {
      position: relative;
      height: 50px;
      .el-input {
        width: 200px;
        float: right;
      }
      .search {
        float: right;
      }
    }
    .page {
      margin-top: 25px;
      display: flex;
      justify-content: center;
    }
  }

  /deep/ .user-info {
    background: rgba($color: #000000, $alpha: 0);
    box-shadow: none;
    .el-dialog__header {
      padding: 0px;
    }
    .el-dialog__body {
      padding: 0;
    }
  }
}
</style>