<template>
  <div>
    <div class="head">
      <el-input v-model="search" placeholder="搜索" @keyup.enter="searchFile" />
      <el-button type="primary" plain @click="searchFile">搜索</el-button>
      <el-button type="info" plain @click="toAdd">创建共享条目</el-button>
    </div>
    <div class="body">
      <div v-for="(item, index) in fileList" :key="index">
        <div class="card">
          <data-card :fileInfo="item">
            <template #status>
              <div v-if="item.status === 1" class="online">
                <el-tag type="success">Online</el-tag>
              </div>
              <div v-else class="offline">
                <el-tag type="info">Offline</el-tag>
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
                          v-if="email === item.creator"
                          @click="operate(1, item)"
                          >编辑</el-dropdown-item
                        >
                        <el-dropdown-item
                          v-if="item.status === 1"
                          @click="operate(2, item)"
                          >下线</el-dropdown-item
                        >
                        <el-dropdown-item
                          v-if="item.status === -1"
                          @click="operate(3, item, index)"
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
      <div class="pagination">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          v-model:current-page="currentPage"
          @current-change="currentChange"
          :hide-on-single-page="true"
        />
      </div>
    </div>
    <el-dialog v-model="offlineFlag" width="600px" :modal="false">
      <offline-dialog @commitInfo="commitInfo" />
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import DataCard from "@/components/cards/DataCard.vue";
import router from "@/router";
import {
  fuzzyQueryAdmin,
  deleteByAdmin,
  updateStatusById,
} from "@/api/request";
import { useStore } from "@/store";
import { ElMessageBox } from "element-plus";
import OfflineDialog from "@/components/dialog/OfflineDialog.vue";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { DataCard, OfflineDialog },
  setup() {
    const search = ref("");
    const keyword = ref("");
    const fileList = ref<any[]>([]);
    const store = useStore();
    const email = computed(() => {
      return store.state.user.email;
    });
    const currentPage = ref(1);
    const total = ref(0);
    const toAdd = () => {
      router.push({ name: "share" });
    };
    const offlineFlag = ref(false);
    const offlineItem = ref<any>();

    const operate = async (param: number, fileInfo: any, index: number) => {
      if (param === 1) {
        router.push({
          name: "updateShare",
          params: {
            id: fileInfo.id,
            fileInfo: JSON.stringify(fileInfo),
          },
        });
      } else if (param === 2) {
        offlineFlag.value = true;
        offlineItem.value = fileInfo;
      } else if (param === 3) {
        const data = await updateStatusById(fileInfo.id as string, 1);
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value[index].status = 1;
            notice("success", "成功", "条目上线成功");
          }
        }
      } else if (param === 4) {
        ElMessageBox.confirm("确定删除该数据条目？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const data = await deleteByAdmin({
              size: 10,
              page: currentPage.value - 1,
              keyword: keyword.value,
              property: "update_time",
              id: fileInfo.id,
              flag: true,
              tags: [],
              type: "",
            });
            if (data != null) {
              if ((data as any).code === 0) {
                fileList.value = data.data;
                total.value = total.value;
              }
            }
          })
          .catch(() => {});
      } else if (param === 5) {
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
      const data = await fuzzyQueryAdmin({
        property: "update_time",
        flag: true,
        keyword: keyword.value,
        tags: [],
        page: val - 1,
        size: 10,
        type: "",
      });
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
          search.value = keyword.value;
        }
      }
    };

    const searchFile = async () => {
      keyword.value = search.value;
      const data = await fuzzyQueryAdmin({
        property: "update_time",
        flag: true,
        keyword: keyword.value,
        tags: [],
        page: 0,
        size: 10,
        type: "",
      });
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
        }
      }
    };

    const commitInfo = async (val: any) => {
      const data = await updateStatusById((offlineItem.value as any).id, -1);
      if (data != null) {
        if ((data as any).code === 0) {
          offlineItem.value.status = -1;
          notice("success", "成功", "条目成功下线");
        }
      }
      offlineFlag.value = false;
    };

    onMounted(async () => {
      const data = await fuzzyQueryAdmin({
        property: "update_time",
        flag: true,
        keyword: "",
        tags: [],
        page: 0,
        size: 10,
        type: "",
      });
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
        }
      }
    });

    return {
      search,
      toAdd,
      fileList,
      email,
      operate,
      searchFile,
      currentPage,
      currentChange,
      total,
      offlineFlag,
      commitInfo,
    };
  },
});
</script>

<style lang="scss" scoped>
.head {
  height: 70px;
  .el-input {
    width: 400px;
    margin-left: 30px;
    margin-top: 20px;
    margin-right: 20px;
  }
}
.body {
  margin-left: 20px;
  margin-right: 20px;
  .card {
    cursor: pointer;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    border-radius: 6px;
    margin-bottom: 10px;
    padding: 10px;
    .online,
    .offline {
      margin-left: 10px;
    }
    .creator {
      position: absolute;
      display: flex;
      right: 50px;
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

  .pagination {
    margin-top: 40px;
    margin-bottom: 40px;
    display: flex;
    justify-content: space-around;
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
</style>