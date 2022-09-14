<template>
  <div class="share-main">
    <el-scrollbar height="80vh" :always="false" v-if="fileList.length > 0">
      <div v-for="(item, index) in fileList" :key="index">
        <div class="card">
          <data-card :fileInfo="item" @clickName="toShareFile(index)">
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
                        <el-dropdown-item @click="operate(1, item)"
                          >编辑</el-dropdown-item
                        >
                        <el-dropdown-item
                          v-if="item.status === 1"
                          @click="operate(2, item)"
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
      :current-page="currentPage"
      @current-change="currentChange"
      :hide-on-single-page="true"
    />
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import {
  pageQueryByEmail,
  offlineById,
  updateStatusById,
  deleteShareFileAsMember,
  deleteAsMember,
  deleteByAdmin,
} from "@/api/request";
import { notice } from "@/utils/notice";
import axios from "axios";
import DataCard from "@/components/cards/DataCard.vue";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import { addMessage, QueryByTime, examineById } from "@/api/request";
export default defineComponent({
  components: { DataCard },
  setup() {
    const fileList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);

    const operate = async (number: number, info: any) => {
      if (number === 1) {
        router.push({
          name: "updateShare",
          params: {
            id: info.id,
          },
        });
        // console.log(router.currentRoute.value.fullPath)
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
                fileList.value.forEach((item) => {
                  if (item.id === info.id) {
                    item.status = -1;
                  }
                });
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
        ).then(async () => {
          const jsonCache = JSON.stringify(info);
          // 暂缓进行重构
          // const data = await addMessage({
          //   id: "",
          //   dataName: info.name,
          //   dataUploadTime: "",
          //   dataExamineTime: "2012-02-25",
          //   dataCache: jsonCache,
          //   messageRequest: "fff",
          //   reply: false,
          //   fileId: info.id,
          //   messageSender: "fgz",
          //   messageReceiver: " ",
          //   messageResponse: "examine",
          //   messageType: "online",
          //   replyUser:false,
          // });
          //console.log(info.id)
          await updateStatusById(info.id, 1);
        });
      } else if (number === 4) {
        ElMessageBox.confirm("您确定要删除该条目吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const data = await deleteByAdmin({
              id: info.id,
              size: 10,
              page: currentPage.value - 1,
              tags: info.tagList,
              property: "id",
              flag: true,
              keyword: "",
              status: -1,
            });
            if ((data as any).code === 0) {
              fileList.value = data.data;
              notice("success", "成功", "删除成功！");
            }
          })
          .catch(() => {});
      }
    };

    const currentChange = () => {};

    const toShareFile = (index: number) => {
      router.push({
        name: "shareFile",
        params: {
          id: fileList.value[index].id,
          fileInfo: JSON.stringify(fileList.value[index]),
        },
      });
    };

    onMounted(async () => {
      let jsonDatass = {
        page: 0,
        size: 10,
        keyword: "",
        tags: [],
        property: "id",
        flag: false,
      };
      const data = await axios
        .post("http://172.21.213.244:8002/dataList/fuzzyQuery", jsonDatass, {
          headers: {
            authorization:
              "Bearer eyJhbGciOiJIUzUxMiJ9.eyJyb2xlcyI6IltcImFkbWluXCJdIiwibmFtZSI6Inp5bSIsImlkIjoiNDM2MDg1MTQtZDgzMy00YjUwLTlhNzQtMDliNjM4YjkzODkxIiwiZXhwIjoxNjYyNzI4NTM4LCJlbWFpbCI6IjEyM0BxcS5jb20ifQ.UK366cK1dP0bZqCmaZKGmYDz1XndpmUh0tdxWFZ-9y-bT54_gqOAGRW0UopFKyf36mSZJWc_CInYiYq1-WF2vw",
          },
        })
        .then((res) => {
          console.log("tt", res.data);
          return res.data;
        });
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
        } else {
          notice("error", "失败", (data as any).msg);
        }
      }
    });

    return {
      fileList,
      operate,
      total,
      currentPage,
      currentChange,
      toShareFile,
    };
  },
});
</script>

<style lang="scss" scoped>
.share-main {
  height: 80vh;
  border-bottom: solid 0.5px #ebeef5;
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
}
.pagination {
  margin-top: 10px;
  display: flex;
  justify-content: space-around;
}
</style>