<template>
  <div class="data-main">
    <div class="search">
      <div class="top">
        <el-input
          v-model="input"
          placeholder="数据检索"
          @keydown.enter="searchHandle"
        >
        </el-input>
        <el-button :icon="Search" color="black" @click="searchHandle"
          >搜索</el-button
        >
      </div>
    </div>
    <div class="body">
      <div class="left">
        <div class="classify">
          <div class="head">
            <strong
              >分类筛选（<span @click="classifyHandle('所有')">所有</span
              >）</strong
            >
          </div>
          <div class="content" v-for="(item, index) in classList" :key="index">
            <div class="title">
              <strong>{{ item.label }}</strong>
            </div>
            <div class="value">
              <div
                class="value-item"
                v-for="value in item.value"
                :key="value"
                @click="classifyHandle(value)"
              >
                {{ value }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="right">
        <div class="list">
          <div class="statistics">
            <div class="class">
              <strong
                >当前类别：<span style="color: #409eff">{{
                  classValue
                }}</span></strong
              >
            </div>
            <div
              :class="sortWord != 'update_time' ? 'sort' : 'sort focus'"
              @click="sortHandle('update_time')"
            >
              <strong>更新时间</strong
              ><el-icon style="margin-left: 5px"><ArrowDownBold /></el-icon>
            </div>
            <div
              :class="sortWord != 'download' ? 'sort' : 'sort focus'"
              @click="sortHandle('download')"
            >
              <strong>下载量</strong
              ><el-icon style="margin-left: 5px"><ArrowDownBold /></el-icon>
            </div>
            <div
              :class="sortWord != 'watch' ? 'sort' : 'sort focus'"
              @click="sortHandle('watch')"
            >
              <strong>浏览量</strong
              ><el-icon style="margin-left: 5px"><ArrowDownBold /></el-icon>
            </div>
            <div class="result">
              <strong
                >共
                <span style="color: #409eff">{{ total }}</span> 条结果</strong
              >
            </div>
          </div>
          <div v-if="skeletonFlag">
            <el-skeleton :rows="15" animated />
          </div>
          <div v-else>
            <div class="list-item" v-if="fileList.length > 0">
              <div v-for="(item, index) in fileList" :key="index" class="card">
                <data-card
                  :keyword="titleKeyword"
                  :fileInfo="item"
                  @click="toDetail(index)"
                ></data-card>
              </div>
            </div>
            <div v-else>
              <el-empty description="暂无数据" />
            </div>
          </div>

          <div class="pagination">
            <el-pagination
              background
              :page-size="8"
              layout="total, jumper, prev, pager, next"
              :pager-count="5"
              :total="total"
              v-model:current-page="currentPage"
              @current-change="pageChange"
              :hide-on-single-page="true"
            />
          </div>
        </div>
      </div>

      <div class="special">
        <div class="hot-data">
          <div class="title"><strong>热门数据</strong></div>
          <div v-if="hotSkeletonFlag">
            <el-skeleton :rows="5" animated />
          </div>
          <div v-else>
            <div
              class="content"
              v-for="(item, index) in hotDataList"
              :key="index"
            >
              <div class="number">{{ index + 1 }}</div>
              <div class="text" @click="toHotData(index)">
                {{ item.dataListName }}
              </div>
            </div>
          </div>
        </div>
        <div class="special-data">
          <div class="title"><strong>特色数据</strong></div>
          <el-skeleton :rows="5" animated v-if="skeletonFlag" />
          <div v-else v-loading="specialLoading">
            <div v-if="specialList.length === 0">
              <el-empty description="暂无数据" />
            </div>
            <div v-else>
              <ul
                class="content"
                v-for="(item, index) in specialList"
                :key="index"
              >
                <li class="text" @click="toSpecial(item.id)">
                  {{ item.dataListName }}
                </li>
              </ul>
              <div class="change">
                <div>
                  <svg
                    style="
                      width: 16px;
                      height: 16px;
                      margin-top: 3px;
                      margin-right: 5px;
                    "
                  >
                    <use xlink:href="#icon-reload"></use>
                  </svg>
                </div>
                <div>
                  <el-button type="primary" link @click="changeSpecialData"
                    >换一批</el-button
                  >
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="real-time">
          <div class="title"><strong>实时数据</strong></div>
          <div class="content">
            <ul>
              <li class="text" @click="toNav('WaterLevel')">实时水情</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <el-backtop :right="100" :bottom="100" />
    <page-copyright />
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import DataCard from "@/components/data/DataCard.vue";
import {
  fuzzyQueryDataList,
  getHot,
  getIdAndDataListName,
} from "@/api/request";
import router from "@/router";
import NProgress from "nprogress";
import { Search } from "@element-plus/icons-vue";
import PageCopyright from "@/layout/components/PageCopyright.vue";
// NProgress.configure({ showSpinner: false });

export default defineComponent({
  components: {
    DataCard,
    PageCopyright,
  },

  setup() {
    let specialStart = 0;
    const classList = [
      {
        label: "地形数据",
        value: [
          "DEM",
          "边界",
          "等高线",
          "DWG",
          "高程点",
          "固定断面线",
          "深泓线",
        ],
      },
      {
        label: "工程数据",
        value: [
          "航标",
          "护岸工程",
          "码头工程",
          "水利工程",
          "整治工程",
          "桥梁工程",
        ],
      },
      {
        label: "物理模型",
        value: ["浓度场", "照片"],
      },
      {
        label: "水文数据",
        value: ["潮位", "断面输沙率", "含沙量", "含盐度", "流速流向", "悬移质"],
      },
      {
        label: "遥感影像",
        value: ["遥感影像"],
      },
    ];

    const hotDataList = ref<{ dataListName: string; id: string }[]>([]);
    const specialList = ref<{ dataListName: string; id: string }[]>([]);
    const sortWord = ref("update_time");
    const skeletonFlag = ref(true);
    const hotSkeletonFlag = ref(true);
    const input = ref("");
    const classValue = ref("所有");

    const titleKeyword = ref("");

    const fileList = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);

    const specialLoading = ref(false);

    const searchData = async (
      page: number,
      size: number,
      titleKeyword: string,
      property: string,
      type: string
    ) => {
      const jsonData = {
        page: page,
        size: size,
        titleKeyword: titleKeyword,
        property: property,
        flag: false,
        type: type,
      };
      NProgress.start();
      const data = await fuzzyQueryDataList(jsonData);
      if (data != null && (data as any).code === 0) {
        fileList.value = data.data.list;
        total.value = data.data.total;
      }
      NProgress.done();
    };

    const searchHandle = async () => {
      titleKeyword.value = input.value;
      await searchData(
        0,
        8,
        titleKeyword.value,
        sortWord.value,
        classValue.value === "所有" ? "" : classValue.value
      );
      currentPage.value = 1;
    };

    const classifyHandle = async (val: string) => {
      classValue.value = val;
      await searchData(
        0,
        8,
        "",
        sortWord.value,
        classValue.value === "所有" ? "" : classValue.value
      );
      currentPage.value = 1;
      input.value = "";
      titleKeyword.value = "";
    };

    const sortHandle = async (val: string) => {
      sortWord.value = val;
      await searchData(
        currentPage.value - 1,
        8,
        titleKeyword.value,
        sortWord.value,
        classValue.value === "所有" ? "" : classValue.value
      );
    };

    const pageChange = async (val: number) => {
      await searchData(
        val - 1,
        8,
        titleKeyword.value,
        sortWord.value,
        classValue.value === "所有" ? "" : classValue.value
      );
      input.value = titleKeyword.value;
    };

    const toMap = () => {
      router.push({
        path: "/data/findMap",
      });
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
    const toHotData = (val: number) => {
      router.push({
        name: "shareFile",
        params: {
          id: hotDataList.value[val].id,
        },
      });
    };

    const toSpecial = (val: string) => {
      router.push({
        name: "shareFile",
        params: {
          id: val,
        },
      });
    };

    const toNav = (val: string) => {
      router.push({
        name: val,
      });
    };

    const changeSpecialData = async () => {
      specialLoading.value = true;
      const specialData = await getIdAndDataListName(4, specialStart);
      specialLoading.value = false;
      if (specialData != null && (specialData as any).code === 0) {
        specialList.value = [];
        specialData.data.forEach((item: { name: string; id: string }) => {
          specialList.value.push({
            dataListName: item.name,
            id: item.id,
          });
        });
        specialStart += 4;
      }
    };

    onMounted(async () => {
      await searchData(0, 8, "", "update_time", "");
      const data = await getHot(8);
      if (data != null && (data as any).code === 0) {
        hotDataList.value = data.data;
      }
      const specialData = await getIdAndDataListName(4, specialStart);
      if (specialData != null && (specialData as any).code === 0) {
        specialList.value = [];
        specialData.data.forEach((item: { name: string; id: string }) => {
          specialList.value.push({
            dataListName: item.name,
            id: item.id,
          });
        });
        specialStart += 4;
      }
      skeletonFlag.value = false;
      hotSkeletonFlag.value = false;
    });

    return {
      skeletonFlag,
      hotSkeletonFlag,
      input,
      fileList,
      total,
      toDetail,
      currentPage,
      toMap,
      pageChange,
      sortWord,
      classList,
      hotDataList,
      specialList,
      toSpecial,
      Search,
      searchHandle,
      classValue,
      classifyHandle,
      sortHandle,
      toHotData,
      titleKeyword,
      changeSpecialData,
      specialLoading,
      toNav,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-main {
  .search {
    width: calc(80vw + 20px);
    margin-left: 10vw;
    margin-top: 20px;
    margin-bottom: 20px;
    height: 60px;
    background: #f0f3f5;
    .top {
      padding-top: 15px;
      width: 80%;
      margin-left: 10%;
      .el-input {
        width: calc(100% - 100px);
        margin-right: 10px;
      }
      .el-button {
        width: 90px;
      }
    }
  }
  .body {
    margin-left: 10vw;
    display: flex;
    .left {
      width: 16vw;
      margin-right: 10px;
      .classify {
        border: solid 1px #d6d6d6;
        box-sizing: border-box;
        .head {
          background: rgba($color: #000000, $alpha: 0.7);
          height: 50px;
          line-height: 50px;
          color: white;
          text-align: center;
          font-size: 20px;
          span {
            color: #409eff;
            &:hover {
              text-decoration: underline;
              cursor: pointer;
            }
          }
        }
        .content {
          .title {
            padding-left: 10px;
            height: 40px;
            line-height: 40px;
            background: #e5ecf4;
            font-style: italic;
          }
          .value {
            display: flex;
            padding: 10px 15px 0;
            flex-wrap: wrap;
            cursor: pointer;
            .value-item {
              margin-right: 15px;
              margin-bottom: 10px;
              &:hover {
                text-decoration: underline;
                color: #409eff;
              }
            }
          }
        }
      }
    }
    .right {
      width: 48vw;
      .list {
        .statistics {
          height: 50px;
          background: rgba($color: #000000, $alpha: 0.7);
          color: white;
          display: flex;
          line-height: 50px;
          text-align: center;
          position: relative;
          cursor: pointer;
          .class {
            width: 200px;
          }
          .sort {
            width: 150px;
          }
          .focus {
            background: black;
          }
          .result {
            position: absolute;
            right: 25px;
          }
        }
        .list-item {
          padding: 0 10px;
          border: solid 1px #d6d6d6;
          .card {
            cursor: pointer;
            width: 100%;
            /deep/.el-tag__content {
              color: white;
            }
            &:last-child {
              /deep/ .data-card {
                border-bottom: none;
              }
            }
          }
        }
      }
    }
    .special {
      width: 16vw;
      margin-left: 10px;
      .title {
        height: 50px;
        background: rgba($color: #000000, $alpha: 0.7);
        color: white;
        line-height: 50px;
        text-align: center;
        font-size: 20px;
      }
      .hot-data {
        border: solid 1px #d6d6d6;
        box-sizing: border-box;
        .content {
          padding: 7px 10px;
          display: flex;
          line-height: 25px;
          cursor: pointer;

          .number {
            height: 25px;
            width: 25px;
            background: #999999;
            color: white;
            text-align: center;
            border-radius: 6px;
            margin-right: 10px;
          }
          &:nth-child(1),
          &:nth-child(2),
          &:nth-child(3) {
            .number {
              background: #107bce;
            }
          }
          .text {
            &:hover {
              text-decoration: underline;
              color: #409eff;
            }
          }
        }
      }
      .special-data {
        margin-top: 30px;
        margin-bottom: 30px;
        border: solid 1px #d6d6d6;
        box-sizing: border-box;
        cursor: pointer;
        .text:hover {
          text-decoration: underline;
          color: #409eff;
        }
        .change {
          height: 30px;
          line-height: 20px;
          margin-left: calc(100% - 90px);
          display: flex;
        }
      }
    }
    .real-time {
      margin-bottom: 30px;
      border: solid 1px #d6d6d6;
      box-sizing: border-box;
      cursor: pointer;
      .text:hover {
        text-decoration: underline;
        color: #409eff;
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