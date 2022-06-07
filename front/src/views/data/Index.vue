<template>
  <div class="data-main">
    <page-header :pageTitle="'数据门户'"></page-header>
    <div class="data-body">
      <div class="body">
        <div class="left">
          <div class="content">
            <div class="title">
              <strong>类别</strong>
            </div>
            <data-collapse></data-collapse>
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
            <div v-for="(item, index) in fileList" :key="index">
              <data-card
                :fileInfo="item"
                class="card"
                @clickName="toDetail(index)"
              ></data-card>
            </div>

            <div class="pagination">
              <el-pagination
                background
                layout="prev, pager, next"
                :total="total"
                :current-page="currentPage"
                @current-change="pageChange"
              />
            </div>
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
import { pageQuery, fuzzyQuery } from "@/api/request";
import router from "@/router";

import NProgress from 'nprogress'
NProgress.configure({ showSpinner: false })
export default defineComponent({
  components: {
    PageHeader,
    DataCollapse,
    DataCard,
  },
  setup() {
    const input = ref("");
    const keyWord = ref("");
    const selectValue = ref("download");
    const fileList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
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

    const search = async () => {
      NProgress.start()
      keyWord.value = input.value;
      let property = "";
      let flag = false;
      switch (selectValue.value) {
        case "download":
          property = "download";
          flag = false;
          break;
        case "watch":
          property = "watch";
          flag = false;
          break;
        case "update":
          property = "update_time";
          flag = false;
          break;
        case "name":
          property = "name";
          flag = false;
          break;
      }
      if (keyWord.value === "") {
        const data = await pageQuery(property, flag, 0, 10);
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
            currentPage.value = 1;
          }
        }
      } else {
        const data = await fuzzyQuery(property, flag, keyWord.value, 0, 10);
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
            currentPage.value = 1;
          }
        }
      }
      NProgress.done()
    };

    const pageChange = () => {};

    const toDetail = (index: number) => {
      router.push({
        name: "shareFile",
        params: {
          id: fileList.value[index].id,
          fileInfo: JSON.stringify(fileList.value[index]),
        },
      });
    };

    onMounted(async () => {
      const data = await pageQuery("download", false, 0, 10);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
          total.value = data.data.total;
        }
      }
    });

    return {
      input,
      options,
      selectValue,
      fileList,
      total,
      toDetail,
      currentPage,
      pageChange,
      search,
      
    };
  },
});
</script>

<style lang="scss" scoped>
.data-main {
  .data-body {
    .body {
      width: 1250px;
      margin: 0 auto;
      margin-top: 10px;
      display: flex;
      .left {
        width: 300px;
        background: #f6f7fa;
        min-height: calc(100vh - 170px);
        .content {
          margin: 20px 15px 10px 15px;
          .title {
            height: 50px;
            border-bottom: solid 1px #d2d2d2;
          }
        }
      }
      .right {
        width: 950px;
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
  }
}
.pagination {
  margin-top: 40px;
  margin-bottom: 40px;
  display: flex;
  justify-content: space-around;
}
</style>