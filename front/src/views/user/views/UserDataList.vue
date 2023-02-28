<template>
  <div class="share-main">
    <div class="head">
      <el-input v-model="search" placeholder="搜索" @keyup.enter="searchFile" />
      <el-button type="primary" plain @click="searchFile">搜索</el-button>
      <el-button type="info" plain @click="toAdd">创建数据条目</el-button>
      <el-select v-model="typeValue" class="typeSelect" @change="typeChange">
        <el-option
          v-for="item in typeOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select v-model="sortValue" class="sortSelect" @change="sortChange">
        <el-option
          v-for="item in sortOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
    </div>
    <div v-if="skeletonFlag">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else>
      <div v-if="fileList.length > 0">
        <div v-for="(item, index) in fileList" :key="index">
          <div class="card">
            <div class="left">
              <div class="name">
                <div class="text" :title="item.name" @click="Nav(index)">
                  {{ item.name }}
                </div>
                <div v-if="item.status === 1" class="online">
                  <el-tag type="success">Online</el-tag>
                </div>
                <div v-if="item.status === -1" class="offline">
                  <el-tag type="danger">Offline</el-tag>
                </div>
                <div v-if="item.status === 0" class="offline">
                  <el-tag type="info">审核中</el-tag>
                </div>
                <div class="operate">
                  <el-dropdown size="small">
                    <el-button type="primary" size="small">
                      操作<el-icon style="margin-left: 10px"
                        ><ArrowDown
                      /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          @click="commandHandle('1', item, index)"
                          ><el-icon><Edit /></el-icon>修改条目</el-dropdown-item
                        >
                        <el-dropdown-item
                          @click="commandHandle('2', item, index)"
                          v-if="item.status === 1"
                          ><el-icon><InfoFilled /></el-icon
                          >下线条目</el-dropdown-item
                        >
                        <el-dropdown-item
                          @click="commandHandle('3', item, index)"
                          v-if="item.status === -1"
                          ><el-icon><View /></el-icon>上线条目</el-dropdown-item
                        >
                        <el-dropdown-item
                          @click="commandHandle('4', item, index)"
                          ><el-icon><DeleteFilled /></el-icon
                          >删除条目</el-dropdown-item
                        >
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
              <div class="watch-download">
                <el-icon style="margin-right: 5px"><View /></el-icon>
                <span>{{ item.watch }}</span>
                <el-icon style="margin: 0 5px 0 30px"><Download /></el-icon>
                <span>{{ item.download }}</span>
              </div>
              <div class="update-time">
                <strong>上次更新于：</strong>
                {{ dateFormat(item.updateTime, "yyyy年MM月dd日hh时") }}
              </div>
              <div class="tag">
                <div
                  class="tag-item"
                  v-for="(tag, index) in item.tags"
                  :key="index"
                >
                  <div
                    class="circle"
                    :style="'background:' + generateColorByText(tag)"
                  ></div>
                  <div class="text">{{ tag }}</div>
                </div>
              </div>
            </div>
            <div class="right">
              <img :src="getAvatar(item.avatar, item.name)" alt="" />
            </div>
          </div>
        </div>
      </div>
      <el-empty description="暂无数据" v-else />
    </div>
    <div class="pagination">
      <el-pagination
        background
        layout="total, jumper, prev, pager, next"
        :total="total"
        v-model:current-page="currentPage"
        @current-change="currentChange"
        :hide-on-single-page="true"
        :pager-count="5"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { pageQueryByEmail, updateStatusById } from "@/api/request";
import DataCard from "@/components/data/DataCard.vue"
import { ElMessageBox } from "element-plus";
import router from "@/router";
import { notice } from "@/utils/notice";
import { generateColorByText, dateFormat, imgBase64 } from "@/utils/common";
import NProgress from "nprogress";
import { prefix } from "@/prefix";
export default defineComponent({
  components: { DataCard },
  setup() {
    const typeValue = ref("all");
    const sortValue = ref("watch");
    const fileList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
    const search = ref("");
    const keyword = ref("");
    const skeletonFlag = ref(true);

    const typeOptions = [
      {
        label: "All",
        value: "all",
      },
      {
        label: "Online",
        value: "online",
      },
      {
        label: "Offline",
        value: "offline",
      },
      {
        label: "审核中",
        value: "waiting",
      },
    ];

    const sortOptions = [
      {
        label: "浏览量",
        value: "watch",
      },
      {
        label: "下载量",
        value: "download",
      },
      {
        label: "上次更新时间",
        value: "update_time",
      },
    ];

    const Nav = (index: number) => {
      router.push({
        name: "shareFile",
        params: {
          id: fileList.value[index].id,
        },
      });
    };

    const pageQueryData = async (
      page: number,
      size: number,
      keyword: string,
      type: string,
      property: string
    ) => {
      NProgress.start();
      const jsonData = {
        page: page,
        size: size,
        keyword: keyword,
        type: type,
        property: property,
      };
      const data = await pageQueryByEmail(jsonData);
      if (data != null && (data as any).code === 0) {
        fileList.value = data.data.list;
        total.value = data.data.total;
      }
      NProgress.done();
    };

    const getAvatar = (avatar: string, name: string) => {
      if (avatar === "") {
        return imgBase64(name);
      } else {
        return prefix + "visual/getAvatar/" + avatar;
      }
    };

    const commandHandle = async (number: string, info: any, index: number) => {
      if (number === "1") {
        router.push({
          name: "updateShare",
          params: {
            id: info.id,
          },
        });
      } else if (number === "2") {
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
            if (data != null && (data as any).code === 0) {
              if (typeValue.value === "all") {
                fileList.value[index].status = -1;
              } else {
                await pageQueryData(
                  currentPage.value - 1,
                  10,
                  keyword.value,
                  typeValue.value,
                  sortValue.value
                );
              }
            }
            notice("success", "成功", "下线成功！");
          })
          .catch(() => {});
      } else if (number === "3") {
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
            const data = await updateStatusById(info.id, 0);
            notice("success", "成功", "请等待管理员审核");
            if (data != null && (data as any).code === 0) {
              if (typeValue.value === "all") {
                fileList.value[index].status = 0;
              } else {
                await pageQueryData(
                  currentPage.value - 1,
                  10,
                  keyword.value,
                  typeValue.value,
                  sortValue.value
                );
              }
            }
          })
          .catch(() => {});
      } else if (number === "4") {
        ElMessageBox.confirm("您确定要删除该条目吗？", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {})
          .catch(() => {});
      }
    };

    const currentChange = async (val: number) => {
      await pageQueryData(
        val - 1,
        10,
        keyword.value,
        typeValue.value,
        sortValue.value
      );
      search.value = keyword.value;
    };

    const searchFile = async () => {
      keyword.value = search.value;
      await pageQueryData(
        0,
        10,
        keyword.value,
        typeValue.value,
        sortValue.value
      );
    };

    const toAdd = () => {
      router.push({ name: "share" });
    };

    const typeChange = async () => {
      await pageQueryData(
        0,
        10,
        keyword.value,
        typeValue.value,
        sortValue.value
      );
      search.value = keyword.value;
      currentPage.value = 1;
    };

    const sortChange = async () => {
      await pageQueryData(
        currentPage.value - 1,
        10,
        keyword.value,
        typeValue.value,
        sortValue.value
      );
      search.value = keyword.value;
    };

    onMounted(async () => {
      await pageQueryData(0, 10, "", typeValue.value, sortValue.value);
      skeletonFlag.value = false;
    });

    return {
      Nav,
      fileList,
      commandHandle,
      total,
      currentPage,
      currentChange,
      search,
      searchFile,
      toAdd,
      generateColorByText,
      dateFormat,
      getAvatar,
      skeletonFlag,
      typeValue,
      sortValue,
      typeOptions,
      sortOptions,
      typeChange,
      sortChange,
    };
  },
});
</script>

<style lang="scss" scoped>
.share-main {
  .head {
    height: 50px;
    .el-input {
      width: 400px;
      margin-right: 20px;
    }
    .typeSelect {
      width: 100px;
      margin-right: 10px;
      margin-left: 10px;
    }
    .sortSelect {
      width: 150px;
      margin-right: 10px;
    }
  }

  .card {
    border-top: 1px solid #dcdfe6;
    box-sizing: border-box;
    padding: 25px 0;
    display: flex;

    .left {
      width: calc(100% - 200px);
      .name {
        font-weight: 900;
        font-size: 20px;
        color: #58a6ff;
        display: flex;
        height: 25px;
        position: relative;

        .text {
          line-height: 30px;
          max-width: calc(100% - 200px);
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          &:hover {
            border-bottom: solid #58a6ff 2px;
            cursor: pointer;
          }
        }

        .online,
        .offline {
          margin-left: 10px;
        }

        .operate {
          position: absolute;
          right: 10px;
          .el-dropdown {
            margin-top: 5px;
          }
        }
      }
      .watch-download {
        margin-top: 10px;
        color: #7f8992;
      }
      .update-time {
        margin-top: 10px;
        color: #7f8992;
        font-size: 14px;
      }
      .tag {
        display: flex;
        margin-top: 20px;
        flex-wrap: wrap;
        // justify-content: space-between;
        .tag-item {
          display: flex;
          height: 20px;
          .circle {
            height: 14px;
            width: 14px;
            margin-top: 3px;
            border-radius: 50%;
          }
          .text {
            margin-left: 3px;
            margin-right: 15px;
            font-size: 14px;
            line-height: 20px;
            color: #7f8992;
          }
        }
      }
    }

    .right {
      width: 200px;
      img {
        height: 125px;
        width: 100%;
      }
    }
  }
}
.pagination {
  margin-top: 10px;
  margin-bottom: 30px;
  display: flex;
  justify-content: space-around;
}
</style>