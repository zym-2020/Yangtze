<template>
  <div class="data-main">
    <page-header :pageTitle="'数据门户'"></page-header>
    <div class="data-body">
      <div class="body">
        <div class="left">
          <div class="content">
            <div class="video" style="margin-bottom: 30px">
              <div>
                <el-card style="text-align: center">
                  <strong>资源类别</strong>
                </el-card>
              </div>
            </div>

            <data-collapse @selectList="getSelectList"></data-collapse>
          </div>
        </div>

        <div class="right">
          <div class="list">
            <el-input v-model="input" placeholder="请输入关键字">
              <template #append>
                <el-button @click="search"
                  ><el-icon><Search /></el-icon
                ></el-button>
              </template>
            </el-input>
            <div class="statistics">
              <div class="result">
                共<span>{{ total }}</span
                >条结果
              </div>
              <div class="sort">
                排序方式
                <el-select v-model="selectValue" @change="search">
                  <el-option
                    v-for="(item, index) in options"
                    :key="index"
                    :label="item.label"
                    :value="item.value"
                  />
                </el-select>
              </div>
            </div>
            <el-divider />
            <div v-if="skeletonFlag">
              <div v-for="(item, index) in fileList" :key="index">
                <data-card
                  :fileInfo="item"
                  class="card"
                  @toDetail="toDetail(index)"
                >
                  <template #border>
                    <hr style="border-color: #d8d8d8" />
                  </template>
                </data-card>
              </div>
            </div>
            <div v-else>
              <el-skeleton :rows="5" animated v-for="item in 3" :key="item" />
            </div>

            <div class="pagination">
              <el-pagination
                background
                :page-size="10"
                layout="prev, pager, next"
                :total="total"
                v-model:current-page="currentPage"
                @current-change="pageChange"
                :hide-on-single-page="true"
              />
            </div>
          </div>
        </div>
        <div>
          <div>
            <el-card
              shadow="always"
              style="margin-top: 110px"
              class="video2"
              @click="isCoor"
              >空间位置查询
            </el-card>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import PageHeader from "@/components/page/PageHeader.vue";
import DataCollapse from "@/components/page/DataCollapse.vue";
import DataCard from "@/components/cards/DataCard.vue";
import { fuzzyQueryDataList } from "@/api/request";
import router from "@/router";

import NProgress from "nprogress";
NProgress.configure({ showSpinner: false });
export default defineComponent({
  components: {
    PageHeader,
    DataCollapse,
    DataCard,
  },

  setup() {
    const skeletonFlag = ref(false);
    const input = ref("");
    const keyWord = ref("");
    const selectValue = ref("watch");
    const fileList = ref<any[]>([]);
    let classify: string[] = [];
    const total = ref(0);
    const currentPage = ref(1);
    const searchMap = ref(false);

    const options = ref<{ label: string; value: string }[]>([
      {
        label: "下载量",
        value: "download",
      },
      {
        label: "浏览量",
        value: "watch",
      },
      {
        label: "上次更新时间",
        value: "update",
      },
      {
        label: "名称",
        value: "name",
      },
    ]);

    const isCoor = () => {
      searchMap.value = !searchMap.value;
      router.push({
        path: "/data/spaceSearch",
      });
    };

    const search = async () => {
      NProgress.start();
      keyWord.value = input.value;
      const jsonData = {
        page: 0,
        size: 10,
        keyword: keyWord.value,
        tags: classify,
        property: "",
        flag: false,
        type: "",
      };

      switch (selectValue.value) {
        case "download":
          jsonData.property = "download";
          break;
        case "watch":
          jsonData.property = "watch";
          break;
        case "update":
          jsonData.property = "update_time";
          break;
        case "name":
          jsonData.property = "name";
          break;
      }
      const data = await fuzzyQueryDataList(jsonData);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = (data as any).data.list;
          total.value = (data as any).data.total;
          currentPage.value = 1;
        }
      }
      NProgress.done();
    };

    const pageChange = async () => {
      const jsonData = {
        page: currentPage.value - 1,
        size: 10,
        keyword: keyWord.value,
        tags: classify,
        property: "",
        flag: false,
        type: "",
      };

      NProgress.start();
      switch (selectValue.value) {
        case "download":
          jsonData.property = "download";
          break;
        case "watch":
          jsonData.property = "watch";
          break;
        case "update":
          jsonData.property = "update_time";
          break;
        case "name":
          jsonData.property = "name";
          break;
      }
      const data = await fuzzyQueryDataList(jsonData);
      console.log(data);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = (data as any).data.list;
          total.value = (data as any).data.total;
        }
      }
      NProgress.done();
    };

    const toDetail = (index: number) => {
      router.push({
        name: "shareFile",
        params: {
          id: fileList.value[index].id,
          fileInfo: JSON.stringify(fileList.value[index]),
        },
      });
    };

    const getSelectList = async (val: string[]) => {
      NProgress.start();
      classify = val;
      const jsonData = {
        page: 0,
        size: 10,
        keyword: keyWord.value,
        tags: classify,
        property: "",
        flag: false,
        type: "",
      };

      switch (selectValue.value) {
        case "download":
          jsonData.property = "download";
          break;
        case "watch":
          jsonData.property = "watch";
          break;
        case "update":
          jsonData.property = "update_time";
          break;
        case "name":
          jsonData.property = "name";
          break;
      }
      const data = await fuzzyQueryDataList(jsonData);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
          currentPage.value = 1;
        }
      }
      NProgress.done();
    };

    onMounted(async () => {
      let jsonData = {
        page: currentPage.value - 1,
        size: 10,
        keyword: keyWord.value,
        tags: classify,
        property: "watch",
        flag: false,
        type: "",
      };
      const data = await fuzzyQueryDataList(jsonData);

      skeletonFlag.value = true;
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
        }
      }
    });

    return {
      skeletonFlag,
      input,
      searchMap,
      options,
      selectValue,
      fileList,
      total,
      toDetail,
      currentPage,

      pageChange,
      search,
      getSelectList,
      isCoor,
    };
  },
});
</script>

<style lang="scss" scoped>
.warning-row {
  background: #0c90b8;
}
.success-row {
  background: #0c90b8;
}

.video2 {
  width: 60px;
  height: 180px;
  border-radius: 15px;
  background-color: rgb(255, 255, 255);
  //opacity: 0.6;
  transition: all 0.3s ease-in-out;
  margin-left: 45px;
  margin-right: 45px;
  // -webkit-animation适配-webkit内核的浏览器
  // -webkit-animation: ripple 1s linear infinite;
  //animation: ripple 1s linear infinite;
  cursor: pointer;
}

.video2:hover {
  background-color: #859ecc;
  transform: scale(1.2);
}
@-webkit-keyframes ripple {
  0% {
    /* 在box四周添加三层白色阴影 */
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }

  100% {
    /* 分别改变三层阴影的距离
          形成两帧的动画,然后在transition的过渡下形成动画 */
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(50, 100, 245, 0);
  }
}
@keyframes ripple {
  0% {
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }
  100% {
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(255, 255, 255, 0);
  }
}
.data-main {
  .data-body {
    .body {
      width: 1550px;
      margin-left: 100px;
      margin: 0 auto;
      margin-top: 10px;
      display: flex;
      .left {
        width: 400px;
        background: #f6f7fa;
        min-height: calc(100vh - 170px);
        .content {
          margin: 20px 15px 10px 15px;
          .title {
            height: 50px;
            border-bottom: solid 1px #d2d2d2;
            text-align: center;
          }
        }
      }
      .right {
        width: 1000px;
        //float:left;

        .list {
          margin: 30px 20px 10px 20px;
          .statistics {
            height: 50px;
            position: relative;
            .result {
              position: absolute;
              left: 0px;
              top: 20px;
              span {
                color: #22a4f1;
              }
            }
            .sort {
              position: absolute;
              right: 0px;
              top: 20px;
            }
          }
        }
        .card {
          cursor: pointer;
          margin-bottom: 40px;
        }
      }
    }
    .body2 {
      width: 150px;
    }
  }
}
.pagination {
  margin-top: 40px;
  margin-bottom: 40px;
  display: flex;
  justify-content: space-around;
}

.video {
  width: 366px;
  height: 61px;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.6);
  //opacity: 0.6;
  transition: all 0.3s ease-in-out;
  // -webkit-animation适配-webkit内核的浏览器
  //-webkit-animation: ripple 1s linear infinite;
  //animation: ripple 1s linear infinite;
}

.video:hover {
  background-color: #ffffff;
  transform: scale(1.2);
}
@-webkit-keyframes ripple {
  0% {
    /* 在box四周添加三层白色阴影 */
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }

  100% {
    /* 分别改变三层阴影的距离
          形成两帧的动画,然后在transition的过渡下形成动画 */
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(50, 100, 245, 0);
  }
}
@keyframes ripple {
  0% {
    box-shadow: 0 0 0 0 rgb(255, 255, 255 / 25%),
      0 0 0 10px rgb(255, 255, 255 / 25%), 0 0 0 20px rgb(255, 255, 255 / 25%);
  }
  100% {
    box-shadow: 0 0 0 10px rgb(255, 255, 255 / 25%),
      0 0 0 20px rgb(255, 255, 255 / 25%), 0 0 0 40px rgba(50, 100, 245, 0);
  }
}
</style>