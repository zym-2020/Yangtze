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
          <data-card :fileInfo="updateKeys(item)">
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
                <div style="line-height: 40px"><strong>创建人：</strong></div>
                <el-avatar :size="25" :src="getUserAvatar(item.userAvatar)" />
                <div class="name">{{ item.userName }}</div>
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
          :current-page="currentPage"
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
  pageQueryByAdmin,
  deleteShareFileById,
  updateStatusById,
} from "@/api/request";
import { useStore } from "@/store";
import { ElMessageBox } from "element-plus";
import OfflineDialog from "@/components/dialog/OfflineDialog.vue";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { DataCard, OfflineDialog },
  setup() {
    const activeName = ref("first");
    const search = ref("");
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

    const getUserAvatar = (url: string) => {
      return url != undefined && url != undefined && url != ""
        ? "http://172.21.212.10:8002" + url
        : "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
    };

    const updateKeys = (object: any) => {
      const keyMap = {
        create_time: "createTime",
        origin_address: "originAddress",
        structured_source: "structuredSource",
        update_time: "updateTime",
        visual_source: "visualSource",
        visual_type: "visualType",
        origin_name: "originName",
        visual_name: "visualName",
        structured_name: "structuredName",
      };
      Object.keys(object).map((key) => {
        if (
          key === "create_time" ||
          key === "origin_address" ||
          key === "structured_source" ||
          key === "update_time" ||
          key === "visual_source" ||
          key === "visual_type" ||
          key === "origin_name" ||
          key === "visual_name" ||
          key === "structured_name"
        ) {
          const newKey = keyMap[key];
          object[newKey] = object[key];
          delete object[key];
        }
      });
      return object;
    };

    const operate = async (param: number, fileInfo: any, index: number) => {
      if (param === 1) {
        router.push({
          name: "updateShare",
          params: {
            id: fileInfo.id,
          },
        });
      } else if (param === 2) {
        offlineFlag.value = true;
        offlineItem.value = fileInfo;
      } else if (param === 3) {
        const data = await updateStatusById({
          id: fileInfo.id as string,
          status: 1,
        });
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value[index].status = 1;
            notice("success", "成功", "条目上线成功");
          }
        }
      } else if (param === 4) {
        ElMessageBox.confirm("这将删除数据条目及条目下的数据！", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const data = await deleteShareFileById({
              size: 10,
              page: currentPage.value - 1,
              keyWord: search.value,
              property: "update_time",
              id: fileInfo.id,
            });
            if (data != null) {
              if ((data as any).code === 0) {
                fileList.value = data.data;
                total.value = total.value - 1;
              }
            }
          })
          .catch(() => {});
      }
    };

    const currentChange = () => {};

    const searchFile = async () => {
      const data = await pageQueryByAdmin({
        property: "update_time",
        flag: true,
        keyWord: search.value,
        page: 0,
        size: 10,
      });
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
        }
      }
    };

    const commitInfo = async (val: any) => {
      const data = await updateStatusById({
        id: (offlineItem.value as any).id,
        status: -1,
      });
      if (data != null) {
        if ((data as any).code === 0) {
          offlineItem.value.status = -1;
          notice("success", "成功", "条目成功下线");
        }
      }
      offlineFlag.value = false;
    };

    onMounted(async () => {
      const data = await pageQueryByAdmin({
        property: "update_time",
        flag: true,
        keyWord: "",
        page: 0,
        size: 10,
      });
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
        }
      }
    });

    return {
      activeName,
      search,
      toAdd,
      fileList,
      getUserAvatar,
      updateKeys,
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