<template>
  <div class="share-main">
    <div class="head">
      <el-input v-model="search" placeholder="搜索" @keyup.enter="searchFile" />
      <el-button type="primary" plain @click="searchFile">搜索</el-button>
      <el-button type="info" plain @click="toAdd">创建共享条目</el-button>
    </div>
    <el-scrollbar :always="false" v-if="fileList.length > 0">
      <div v-for="(item, index) in fileList" :key="index">
        <div class="card">
          <data-card :fileInfo="item">
            <template #status>
              <div v-if="item.status === 1" class="online">
                <el-tag type="success">Online</el-tag>
              </div>
              <div v-if="item.status === -1" class="offline">
                <el-tag type="info">Offline</el-tag>
              </div>
              <div v-if="item.status === 0" class="offline">
                <el-tag type="info">审核中</el-tag>
              </div>
            </template>
            <template #creator>
              <div class="creator">
                <div class="btn">
                  <el-dropdown trigger="click">
                    <el-button size="small">
                      操作<el-icon class="el-icon--right"
                        ><arrow-down
                      /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          @click="operate(1, item)"
                          v-if="item.status != 0"
                          >编辑</el-dropdown-item
                        >
                        <el-dropdown-item
                          v-if="item.status === 1"
                          @click="operate(2, item, index)"
                          >下线</el-dropdown-item
                        >
                        <el-dropdown-item
                          v-if="item.status === -1"
                          @click="operate(3, item)"
                          >上线</el-dropdown-item
                        >
                        <el-dropdown-item @click="operate(4, item)"
                          >删除</el-dropdown-item
                        >
                        <el-dropdown-item @click="operate(5, item, index)"
                          >跳转</el-dropdown-item
                        >
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </template>
          </data-card>
        </div>
      </div>
    </el-scrollbar>
    <el-empty description="暂无数据" v-else />
  </div>
  <div class="pagination" v-if="fileList.length > 0">
    <el-pagination
      background
      layout="prev, pager, next"
      :total="total"
      v-model:current-page="currentPage"
      @current-change="currentChange"
      :hide-on-single-page="true"
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { pageQueryByEmail, updateStatusById } from "@/api/request";
import DataCard from "@/components/cards/DataCard.vue";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { DataCard },
  setup() {
    const fileList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
    const search = ref("");
    const keyword = ref("");

    const operate = async (number: number, info: any, index: number) => {
      if (number === 1) {
        router.push({
          name: "updateShare",
          params: {
            id: info.id,
          },
        });
      } else if (number === 2) {
        ElMessageBox.confirm(
          "您确定要下线条目吗？下线后若想重新上线需要管理员审核",
          "警告",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(async () => {
            const data = await updateStatusById(info.id, -1);
            if (data != null) {
              if ((data as any).code === 0) {
                fileList.value[index].status = -1;
              }
            }
          })
          .catch(() => {});
      } else if (number === 3) {
        ElMessageBox.confirm(
          "您确定上线条目吗？上线后需要管理员审核通过后发布至资源门户，同时请关注消息界面中的通知",
          "警告",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
        )
          .then(async () => {
            await updateStatusById(info.id, 0);
            notice("success", "成功", "请等待管理员审核");
            fileList.value[index].status = 0;
          })
          .catch(() => {});
      } else if (number === 4) {
        ElMessageBox.confirm("您确定要删除该条目吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {})
          .catch(() => {});
      } else if (number === 5) {
        router.push({
          name: "shareFile",
          params: {
            id: fileList.value[index].id,
            fileInfo: JSON.stringify(fileList.value[index]),
          },
        });
      }
    };

    const currentChange = async (val: number) => {
      const jsonData = {
        page: val - 1,
        size: 10,
        keyword: keyword.value,
      };
      const data = await pageQueryByEmail(jsonData);
      if (data != null && (data as any).code === 0) {
        fileList.value = data.data.list;
        total.value = data.data.total;
        search.value = keyword.value;
      }
    };

    const searchFile = async () => {
      keyword.value = search.value;
      const jsonData = {
        page: currentPage.value - 1,
        size: 10,
        keyword: keyword.value,
      };
      const data = await pageQueryByEmail(jsonData);
      if (data != null && (data as any).code === 0) {
        fileList.value = data.data.list;
        total.value = data.data.total;
      }
    };

    const toAdd = () => {
      router.push({ name: "share" });
    };

    onMounted(async () => {
      const jsonData = {
        page: 0,
        size: 10,
        keyword: "",
      };
      const data = await pageQueryByEmail(jsonData);
      if (data != null && (data as any).code === 0) {
        fileList.value = data.data.list;
        total.value = data.data.total;
      }
    });

    return {
      fileList,
      operate,
      total,
      currentPage,
      currentChange,
      search,
      searchFile,
      toAdd,
    };
  },
});
</script>

<style lang="scss" scoped>
.share-main {
  height: 80vh;
  border-bottom: solid 0.5px #ebeef5;
  .head {
    height: 50px;
    .el-input {
      width: 400px;
      margin-right: 20px;
    }
  }
  .el-scrollbar {
    height: calc(100% - 50px);
  }
  .card {
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    border-radius: 6px;
    margin-bottom: 10px;
    padding: 10px;
    cursor: pointer;
    .online,
    .offline {
      margin-left: 10px;
    }
    .creator {
      position: absolute;
      display: flex;
      right: 300px;
      .el-avatar {
        margin-top: 8px;
      }
      .name {
        line-height: 40px;
        margin: 0px 5px;
      }
      .btn {
        margin-top: 10px;
      }
    }
  }
}
.pagination {
  margin-top: 10px;
  display: flex;
  justify-content: space-around;
}
</style>