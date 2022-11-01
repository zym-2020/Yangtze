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
            <div>
              <data-collapse
                @selectList="getSelectList"
                @selectTitle="getSelectTitle"
                @tagRealList="gettagRealList"
              ></data-collapse>
            </div>
          </div>
        </div>
        <div class="right">
          <div class="list">
            <div>
              <el-row>
                <el-col :span="12">
                  <div class="nav">
                    <div class="home" @click="toHome"></div>
                    <div class="Separator"></div>
                    <div
                      :style="{
                        color: getsSelectList.length > 0 ? '' : '#FF6600',
                      }"
                    >
                      {{ selectTitle }}
                    </div>
                    <div v-if="getsSelectList.length > 0" class="Separator">
                      /
                    </div>
                    <div
                      v-if="getsSelectList.length > 0"
                      style="color: #ff6600"
                    >
                      {{ getsSelectList[0] }}
                    </div>
                  </div>
                </el-col>
                <el-col :span="6">
                  <div style="text-align: center; margin-top: 5px">
                    <el-button @click="toMap()" type="primary" round>
                      高级检索</el-button
                    >
                  </div>
                </el-col>
                <el-col :span="6">
                  <div style="margin-top: 5px">
                    <el-input
                      v-model="input"
                      placeholder="请输入关键字"
                      @keyup.enter="search"
                    >
                      <template #append>
                        <el-button
                          @click="search"
                          style="
                            background-color: rgba(153, 204, 204, 0.3);
                            color: black;
                          "
                          ><el-icon><Search /></el-icon
                        ></el-button>
                      </template>
                    </el-input>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div>
              <el-tag
                v-for="(item, index) in tagsList"
                size="large"
                @click="searchTag(item)"
                :key="index"
                :style="[
                  { marginTop: '6px' },
                  { marginRight: '8px' },
                  { width: 'auto' },
                  {
                    backgroundColor:
                      index < 3
                        ? 'rgba(100,149,237,0.8 )'
                        : index < 5
                        ? 'rgba(0,154,205,0.8)'
                        : index < 20
                        ? 'rgba(153, 204, 102)'
                        : index < 25
                        ? 'rgba(153, 204, 204)'
                        : 'rgba(9, 153, 102)',
                  },
                  { color: 'black' },
                ]"
              >
                {{ item }}
              </el-tag>
            </div>
            <div>
              <el-divider style="color: #00bfff">
                <el-icon><star-filled /></el-icon>
              </el-divider>
            </div>
            <div style="">
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
                  <div class="card">
                    <data-card
                      :fileInfo="item"
                      @toDetail="toDetail(index)"
                      :key="new Date().getTime()"
                    ></data-card>
                  </div>
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
                  :hide-on-single-page="false"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, reactive } from "vue";
import PageHeader from "@/components/page/PageHeader.vue";
import DataCollapse from "@/components/page/DataCollapse.vue";
import DataCard from "@/components/cards/DataCard.vue";
import { dateFormat } from "@/utils/common";
import axios from "axios";
import { fuzzyQueryDataList } from "@/api/request";
import router from "@/router";
import { ElNotification } from "element-plus";
import NProgress from "nprogress";
NProgress.configure({ showSpinner: false });
interface User {
  date: string;
  name: string;
  address: string;
}
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
    const selectValue = ref("download");
    const selectTitle = ref();
    const fileList = ref<any[]>([]);
    const classify = ref<any[]>([]);
    const total = ref(0);
    const currentPage = ref(1);
    const getsSelectList = ref<any[]>([]);
    const searchMap = ref(false);
    //用来接收左侧二级分类
    const getTagList = ref<any[]>([]);
    const upTag = ref("");

    const tagsList = ref([
      "2002以前",
      "2002~2012",
      "2012~2022",
      "长江区域",
      "南京区域",
      "白茆小沙",
      "福中+福北",
      "横港沙",
      "黄铁沙",
      "护漕港边滩",
      "沪通大桥",
      "江阴大桥",
      "苏通大桥",
      "双涧沙",
      "通白",
      "通州沙",
      "民主沙",
      "福姜沙",
      "新开沙",
      "西水道",
      "shp",
      "dwg",
      "txt",
      "jpg",
      "excel",
      "栅格文件",
      "矢量文件",
      "文本数据",
      "图片",
      "遥感影像",
    ]);

    const jsonDataSum = reactive({
      page: 0,
      size: 10,
      keyword: "",
      tags: [],
      property: "id",
      flag: false,
      type: "",
    });
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

    const gettagRealList = async (val: string[]) => {
      getTagList.value = val;
      let jsonData = {
        page: 0,
        size: 10,
        keyword: "",
        tags: [],
        property: "id",
        flag: false,
        type: "",
      };
      if (getTagList.value.length != 0) {
        jsonData.type = getTagList.value[0];
        jsonDataSum.type = getTagList.value[0];
        const data = await fuzzyQueryDataList(jsonData);
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = (data as any).data.list;
            total.value = (data as any).data.total;
          }
          ElNotification({
            title: "检索成功",
            type: "success",
            offset: 80,
            position: "top-right",
          });
        }
      } else {
        const data = await fuzzyQueryDataList(jsonData);
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = (data as any).data.list;
            total.value = (data as any).data.total;
          }
          ElNotification({
            title: "检索成功",
            type: "success",
            offset: 80,
            position: "top-right",
          });
        }
      }
      currentPage.value = 1;
    };

    const search = async () => {
      NProgress.start();
      keyWord.value = input.value;
      switch (selectValue.value) {
        case "download":
          jsonDataSum.property = "download";
          jsonDataSum.flag = false;
          break;
        case "watch":
          jsonDataSum.property = "watch";
          jsonDataSum.flag = false;
          break;
        case "update":
          jsonDataSum.property = "update_time";
          jsonDataSum.flag = false;
          break;
        case "name":
          jsonDataSum.property = "name";
          jsonDataSum.flag = false;
          break;
      }
      jsonDataSum.keyword = keyWord.value;
      const data = await fuzzyQueryDataList(jsonDataSum);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = (data as any).data.list;
          total.value = (data as any).data.total;
          currentPage.value = 1;
        }
      }
      NProgress.done();
    };

    const pageChange = async (val: number) => {
      console.log(val);
      // classify记录了分类的项目
      classify.value = getsSelectList.value;
      NProgress.start();
      // switch (selectValue.value) {
      //   case "download":
      //     jsonDataSum.property = "download";
      //     jsonDataSum.flag = false;
      //     break;
      //   case "watch":
      //     jsonDataSum.property = "watch";
      //     jsonDataSum.flag = false;
      //     break;
      //   case "update":
      //     jsonDataSum.property = "update_time";
      //     jsonDataSum.flag = false;
      //     break;
      //   case "name":
      //     jsonDataSum.property = "name";
      //     jsonDataSum.flag = false;
      //     break;
      // }
      // (jsonDataSum.tags as any)[0] = upTag.value;
      // jsonDataSum.page = val - 1;
      const data = await fuzzyQueryDataList({
        page: val - 1,
        size: 10,
        keyword: "",
        tags: [],
        property: "id",
        flag: false,
        type: jsonDataSum.type,
      });
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = (data as any).data.list;
          total.value = (data as any).data.total;
        }
      }
      NProgress.done();
    };

    const toHome = () => {
      router.push({ path: "/" });
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
    const searchTag = async (item: string) => {
      let jsonData = {
        page: 0,
        size: 10,
        keyword: "",
        tags: [""],
        property: "id",
        flag: false,
        type: "",
      };

      if (upTag.value == item) {
        console.log("1");
        const data = await fuzzyQueryDataList(jsonData);
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
          }
        }
      } else {
        console.log(item);
        upTag.value = item;
        jsonData.tags[0] = upTag.value;
        const data = await fuzzyQueryDataList(jsonData);
        if (data != null) {
          if ((data as any).code === 0) {
            fileList.value = data.data.list;
            total.value = data.data.total;
          }
          ElNotification({
            title: "检索成功",
            type: "success",
            offset: 80,
            position: "top-right",
          });
        }
      }
    };
    const getSelectTitle = async (val: string) => {
      console.log("hhh" + val);
      selectTitle.value = val;
      if (selectTitle.value == undefined) getsSelectList.value = [];
    };
    //新增type字段来对条目本身的类型来进行查询
    const getSelectList = async (val: any[]) => {
      classify.value = val;
      NProgress.start();
      switch (selectValue.value) {
        case "download":
          jsonDataSum.property = "download";
          jsonDataSum.flag = false;
          break;
        case "watch":
          jsonDataSum.property = "watch";
          jsonDataSum.flag = false;
          break;
        case "update":
          jsonDataSum.property = "update_time";
          jsonDataSum.flag = false;
          break;
        case "name":
          jsonDataSum.property = "name";
          jsonDataSum.flag = false;
          break;
      }
      jsonDataSum.page = currentPage.value - 1;
      NProgress.done();
      getsSelectList.value = val;
    };

    onMounted(async () => {
      let jsonDatass = {
        page: 0,
        size: 10,
        keyword: "",
        tags: [],
        property: "id",
        flag: false,
        type: "",
      };
      const data = await fuzzyQueryDataList(jsonDatass);
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
      getTagList,
      searchTag,
      fileList,
      total,
      tagsList,
      selectTitle,
      getsSelectList,
      toDetail,
      getSelectTitle,
      gettagRealList,
      currentPage,
      upTag,
      toMap,
      pageChange,
      search,
      getSelectList,
      toHome,
    };
  },
});
</script>

<style lang="scss" scoped>
/deep/.el-tag__content {
  color: black;
}
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
    // background-image:url('~@/assets/water4.png');
    // background-repeat: repeat;
    // background-size:cover;
    // background-attachment:fixed;
    //  height:100%;
    //  position: fixed;
    //  width:100%;
    .body {
      width: 1550px;
      margin-left: auto;
      margin: 0 auto;
      margin-top: 10px;
      display: flex;
      .left {
        background-color: rgba(255, 255, 255, 0.3); //是否有左侧竖条
        width: 280px;
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
        width: 1100px;
        //float:left;

        .list {
          margin: 30px 20px 10px 20px;
          .statistics {
            height: 50px;
            position: relative;
            .result {
              position: absolute;
              left: 10px;
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
          width:100%;
          /deep/.el-tag__content {
            color: white;
          }
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

.video {
  width: 250px;
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

.nav {
  display: flex;
  height: 40px;
  line-height: 40px;
  margin-bottom: 20px;
  cursor: pointer;
  .Separator {
    margin: 0 5px;
  }
  .home {
    color: black;
  }
}
</style>