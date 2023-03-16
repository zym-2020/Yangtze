<template>
  <div>
    <div class="head">
      <div class="info">
        <div class="top-left">
          <div></div>
          <div class="title">
            <strong>{{ fileInfo.name }}</strong>
            <img src="/png/hot.png" v-if="fileInfo.watch >= 20" />
          </div>
          <div class="tags">
            <span>标签：</span>
            <el-tag
              v-for="(item, index) in fileInfo.tags"
              :key="index"
              effect="dark"
              color="#446aad"
            >
              {{ item }}
            </el-tag>
          </div>
          <div class="des"><span>简介：</span>{{ fileInfo.description }}</div>
          <div class="creator">
            <span>条目作者：</span><strong>{{ fileInfo.userName }}</strong>
          </div>
        </div>
        <div class="top-right">
          <img :src="avatar" />
          <el-row>
            <el-col :span="12"
              ><div class="icon">
                <el-icon><View /></el-icon></div
            ></el-col>
            <el-col :span="12"
              ><div class="icon">
                <el-icon><Download /></el-icon></div
            ></el-col>
            <el-col :span="12"><div class="text">浏览量</div></el-col>
            <el-col :span="12"><div class="text">下载量</div></el-col>
            <el-col :span="12"
              ><div class="num">（{{ fileInfo.watch }}）</div></el-col
            >
            <el-col :span="12"
              ><div class="num">（{{ fileInfo.download }}）</div></el-col
            >
          </el-row>
        </div>
      </div>
    </div>

    <div class="data-detail">
      <div class="left">
        <div class="basicInfo">
          <data-head :title="'基本信息'"></data-head>
          <el-row>
            <el-col :span="12">
              <data-description
                :data="{
                  key: '数据时间：',
                  value: fileInfo.timeStamp,
                }"
              ></data-description>
            </el-col>
            <el-col :span="12">
              <data-description
                style="color: #ff8c00"
                :data="{ key: '数据类型：', value: fileInfo.type }"
              ></data-description>
            </el-col>
            <el-col :span="12">
              <data-description
                :data="{ key: '创建时间：', value: date(fileInfo.createTime) }"
              ></data-description>
            </el-col>
            <el-col :span="12">
              <data-description
                :data="{
                  key: '更新时间：',
                  value: date(fileInfo.updateTime),
                }"
              ></data-description>
            </el-col>
            <el-col :span="24">
              <data-description
                style="width: 100%"
                :data="{ key: '空间描述：', value: fileInfo.range }"
              ></data-description>
            </el-col>
          </el-row>
        </div>
        <div class="providerInfo">
          <data-head :title="'数据提供方'"></data-head>
          <el-row>
            <el-col :span="12">
              <data-description
                :data="{
                  key: '作&#12288;&#12288;者：',
                  value: fileInfo.provider,
                }"
              ></data-description>
            </el-col>
            <el-col :span="12">
              <data-description
                style="color: #ff8c00"
                :data="{
                  key: '电&#12288;&#12288;话：',
                  value: fileInfo.providerPhone,
                }"
              ></data-description>
            </el-col>
            <el-col :span="12">
              <data-description
                :data="{
                  key: '邮&#12288;&#12288;箱：',
                  value: fileInfo.providerEmail,
                }"
              ></data-description>
            </el-col>
            <el-col :span="12">
              <data-description
                :data="{
                  key: '地&#12288;&#12288;址：',
                  value: fileInfo.providerAddress,
                }"
              ></data-description>
            </el-col>
          </el-row>
        </div>

        <div class="data-list">
          <data-head :title="'数据列表'"></data-head>
          <el-skeleton v-if="fileSkeleton" />
          <div class="list" v-else>
            <div v-if="fileList.length === 0">
              <el-empty description="暂无数据" />
            </div>
            <div v-else>
              <el-table :data="fileList" max-height="500" stripe>
                <el-table-column prop="fileName" label="文件名称" width="650" />
                <el-table-column prop="size" label="文件大小" width="150" />

                <el-table-column fixed="right" label="下载">
                  <template #default="scope">
                    <el-button
                      type="success"
                      icon="Share"
                      round
                      size="small"
                      @click.prevent="downloadOrigin(scope.$index)"
                    >
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
              <div class="bottom">
                <div class="text">
                  共
                  <strong style="color: #ff8c00">{{ fileList.length }}</strong>
                  个文件
                  <el-button type="success" link circle @click="downloadAll">
                    <strong>下载所有</strong>
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="visual">
          <data-head :title="'数据预览'"></data-head>
          <el-skeleton :rows="5" animated v-if="visualSkeleton" />
          <div v-else>
            <div v-if="mapShow" class="map">
              <map-visual
                :shpArray="shpArray"
                :movePngArray="movePngArray"
                :pngArray="pngArray"
                :rasterTileArray="rasterTileArray"
              />
            </div>
            <div v-if="photoShow" class="photo">
              <photo-visual :photoList="photoList" />
            </div>
            <div v-if="excelShow" class="excel">
              <excel-visual
                :tableNameList="tableNameList"
                :sandContentList="sandContentList"
                :suspensionList="suspensionList"
                :rateDirectionList="rateDirectionList"
                :salinityList="salinityList"
                :flowSandZList="flowSandZList"
              />
            </div>
            <div v-if="!mapShow && !photoShow && !excelShow">
              <el-empty description="数据暂不支持预览" />
            </div>
          </div>
        </div>
        <div class="use">
          <data-head :title="'其他描述'"></data-head>
          <div class="editor-content-view" v-html="fileInfo.detail"></div>
        </div>
      </div>

      <div class="right">
        <div class="title"><strong>空间位置</strong></div>
        <location-map style="width: 100%; height: 300px"></location-map>
        <div class="title"><strong>近10天访问记录</strong></div>
        <div class="visit">
          <statistics :dataListId="fileInfo.id"></statistics>
        </div>
        <div class="title"><strong>相似数据</strong></div>
        <el-skeleton :rows="5" animated v-if="similarSkeleton" />
        <div class="similar" v-else>
          <div v-if="similarDataList.length > 0">
            <div
              v-for="(item, index) in similarDataList"
              :key="index"
              class="similar-item"
              :title="item.name"
              @click="similarClick(item.id)"
            >
              <el-icon><Right /></el-icon>
              {{ item.name }}
            </div>
            <div class="page">
              <el-pagination
                small
                layout="jumper, prev, pager, next"
                :total="similarTotal"
                :page-size="10"
                v-model:current-page="currentPageSimilar"
                @current-change="pageChangeSimilar"
                hide-on-single-page
                :pager-count="5"
              />
            </div>
          </div>
          <div v-else>
            <el-empty description="暂无相似数据" />
          </div>
        </div>
      </div>
    </div>

    <page-copyright />
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import DataDescription from "@/components/data/DataDescription.vue";
import DataHead from "@/components/data/DataHead.vue";
import { dateFormat, imgBase64 } from "@/utils/common";
import { decrypt } from "@/utils/auth";
import { useStore } from "@/store";
import {
  getDownloadURLDataList,
  pageQueryDownloadHistory,
  getCoordinates,
  findFiles,
  getDownloadURL,
  getSimilarData,
} from "@/api/request";
import "@/assets/css/wangeditor.css";
import { notice } from "@/utils/notice";
import LocationMap from "@/components/data/LocationMap.vue";
import router from "@/router";
import MapVisual from "@/components/visual/MapVisual.vue";
import PhotoVisual from "@/components/visual/PhotoVisual.vue";
import ExcelVisual from "@/components/visual/ExcelVisual.vue";
import Statistics from "@/components/visual/Statistics.vue";
import PageCopyright from "@/layout/components/PageCopyright.vue";
import { prefix } from "@/prefix";
export default defineComponent({
  components: {
    DataHead,
    DataDescription,
    LocationMap,
    MapVisual,
    PhotoVisual,
    ExcelVisual,
    Statistics,
    PageCopyright,
  },
  props: {
    fileInfo: {
      type: Object,
    },
  },
  setup(props) {
    const store = useStore();
    const visualSkeleton = ref(true);
    const fileSkeleton = ref(true);
    const similarSkeleton = ref(true);
    const mapShow = ref(false);
    const photoShow = ref(false);
    const excelShow = ref(false);
    const shpArray = ref<
      {
        visualId: string;
        type: "line" | "fill" | "circle";
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const pngArray = ref<
      {
        visualId: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const rasterTileArray = ref<
      { visualId: string; view: { zoom: number; center: number[] } }[]
    >([]);
    const movePngArray = ref<
      {
        visualId: string;
        name: string;
        coordinates: number[][];
        view: { zoom: number; center: number[] };
      }[]
    >([]);
    const photoList = ref<string[]>([]);
    const tableNameList = ref<string[]>([]);
    const sandContentList = ref<string[]>([]);
    const suspensionList = ref<string[]>([]);
    const rateDirectionList = ref<string[]>([]);
    const salinityList = ref<string[]>([]);
    const flowSandZList = ref<string[]>([]);
    const downLoadList = ref<any[]>([]);
    const fileList = ref<any[]>([]);
    const similarDataList = ref<[]>();
    const similarTotal = ref(0);
    const currentPageSimilar = ref(1);

    const location = computed(() => {
      return props.fileInfo?.location;
    });

    const fileInfo = computed(() => {
      return props.fileInfo;
    });

    const avatar = computed(() => {
      if ((props.fileInfo as any).avatar != "") {
        return prefix + "visual/getAvatar/" + (props.fileInfo as any).avatar;
      }
      return imgBase64(
        (props.fileInfo as any).name === undefined
          ? ""
          : (props.fileInfo as any).name
      );
    });

    const date = (time: string) => {
      if (time === "" || time === undefined || time === null) {
        return "";
      }
      return dateFormat(time, "yyyy-MM-dd");
    };

    //下载
    const downloadOrigin = async (val: number) => {
      const data = await getDownloadURL(fileList.value[val].id);
      if (data != null) {
        if ((data as any).code === 0) {
          window.location.href =
            prefix +
            "file/downloadInList/" +
            store.state.user.id +
            "/" +
            decrypt(data.data, store.state.user.id) +
            "/" +
            fileInfo.value?.id;
        } else {
          notice("error", "错误", (data as any).msg);
        }
      }
    };

    // 下载所有
    const downloadAll = async () => {
      const data = await getDownloadURLDataList(fileInfo.value?.id);
      if (data != null && (data as any).code === 0) {
        window.location.href =
          prefix +
          "dataList/downloadAll/" +
          store.state.user.id +
          "/" +
          decrypt(data.data, store.state.user.id);
      }
    };

    const similarClick = (val: string) => {
      router.push({
        name: "shareFile",
        params: {
          id: val,
        },
      });
    };

    const pageChangeSimilar = async (val: number) => {
      const data = await getSimilarData(
        fileInfo.value?.type,
        fileInfo.value?.id,
        10,
        val - 1
      );
      if (data != null && (data as any).code === 0) {
        similarDataList.value = data.data.list;
        similarTotal.value = data.data.total;
      }
    };

    const initVisual = async () => {
      //  获取file文件
      fileSkeleton.value = true;
      const fileData = await findFiles(fileInfo.value?.id);
      if (fileData != null && (fileData as any).code === 0) {
        fileList.value = fileData.data;
      }
      fileSkeleton.value = false;
      //获取file文件的可视化方法
      let MapFlag = false;
      let photoFlag = false;
      let excelFlag = false;
      visualSkeleton.value = true;
      for (let i = 0; i < fileList.value.length; i++) {
        let visualType: string, visualId: string;
        if (fileList.value[i].visualType === "audit") {
          const json = JSON.parse(fileList.value[i].visualId);
          visualType = json["oldVisualType"];
          visualId = json["visualId"];
        } else {
          visualType = fileList.value[i].visualType;
          visualId = fileList.value[i].visualId;
        }
        if (visualType != "") {
          if (
            visualType === "lineVectorTile3D" ||
            visualType === "lineVectorTile"
          ) {
            shpArray.value.push({
              visualId: visualId,
              type: "line",
              view: JSON.parse(fileList.value[i].view),
            });
            MapFlag = true;
          }
          if (
            visualType === "pointVectorTile" ||
            visualType === "pointVectorTile3D"
          ) {
            shpArray.value.push({
              visualId: visualId,
              type: "circle",
              view: JSON.parse(fileList.value[i].view),
            });
            MapFlag = true;
          }
          if (
            visualType === "polygonVectorTile" ||
            visualType === "polygonVectorTile3D"
          ) {
            shpArray.value.push({
              visualId: visualId,
              type: "fill",
              view: JSON.parse(fileList.value[i].view),
            });
            MapFlag = true;
          }
          if (visualType == "rasterTile") {
            rasterTileArray.value.push({
              visualId: visualId,
              view: JSON.parse(fileList.value[i].view),
            });
            MapFlag = true;
          }
          if (visualType == "png") {
            const coordinates = await getCoordinates(visualId);
            if (coordinates != null && (coordinates as any).code === 0) {
              pngArray.value.push({
                visualId: visualId,
                coordinates: coordinates.data,
                view: JSON.parse(fileList.value[i].view),
              });
            }
            MapFlag = true;
          }
          if (visualType == "movePng") {
            const coordinates = await getCoordinates(visualId);
            if (coordinates != null && (coordinates as any).code === 0) {
              movePngArray.value.push({
                name: fileList.value[i].fileName,
                visualId: visualId,
                coordinates: coordinates.data,
                view: JSON.parse(fileList.value[i].view),
              });
            }
            MapFlag = true;
          }
          if (visualType === "photo") {
            photoList.value.push(
              `${prefix}visual/getPhoto/${fileList.value[i].id}`
            );
            photoFlag = true;
          }
          if (visualType === "sandContent") {
            sandContentList.value.push(visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (visualType === "suspension") {
            suspensionList.value.push(visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (visualType === "rateDirection") {
            rateDirectionList.value.push(visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (visualType === "salinity") {
            salinityList.value.push(visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (visualType === "flowSand_Z") {
            flowSandZList.value.push(visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
        }
      }
      mapShow.value = MapFlag;
      photoShow.value = photoFlag;
      excelShow.value = excelFlag;
      visualSkeleton.value = false;
    };

    const initDownloadHistory = async () => {
      const data = await pageQueryDownloadHistory(10, 0, props.fileInfo?.id);
      if (data != null && (data as any).code === 0) {
        downLoadList.value = data.data.list;
      }
    };

    const initSimilarData = async () => {
      similarSkeleton.value = true;
      const data = await getSimilarData(
        fileInfo.value?.type,
        fileInfo.value?.id,
        10,
        0
      );
      if (data != null && (data as any).code === 0) {
        similarDataList.value = data.data.list;
        similarTotal.value = data.data.total;
      }
      similarSkeleton.value = false;
    };

    onMounted(async () => {
      await initVisual();
      await initSimilarData();
      await initDownloadHistory();
    });

    return {
      fileInfo,
      photoList,
      date,
      avatar,
      downloadOrigin,
      location,
      downLoadList,
      fileList,
      sandContentList,
      suspensionList,
      rateDirectionList,
      salinityList,
      flowSandZList,
      visualSkeleton,
      fileSkeleton,
      similarSkeleton,
      mapShow,
      photoShow,
      excelShow,
      shpArray,
      pngArray,
      tableNameList,
      movePngArray,
      rasterTileArray,
      similarDataList,
      similarTotal,
      currentPageSimilar,
      pageChangeSimilar,
      similarClick,
      downloadAll,
    };
  },
});
</script>

<style lang="scss" scoped>
.head {
  background: url("/resource/details_top.jpg");
  background-size: cover;
  height: 300px;
  padding: 25px 0;

  .info {
    color: white;
    display: flex;
    height: 100%;
    width: 66vw;
    margin: 0 auto;
    .top-left {
      height: 100%;
      width: calc(100% - 400px);
      color: #cccccc;
      .title {
        width: calc(100% - 100px);
        font-size: 30px;
        color: white;
      }
      .tags {
        height: 50px;
        line-height: 50px;
        font-family: "Microsoft YaHei";
        .el-tag {
          margin-right: 5px;
        }
      }
      .des {
        font-family: "Microsoft YaHei";
        width: calc(100% - 100px);
        height: 100px;
        display: -webkit-box;
        overflow: hidden;
        -webkit-line-clamp: 4;
        -webkit-box-orient: vertical;
        line-height: 25px;
      }
      .creator {
        font-family: "Microsoft YaHei";
        strong {
          cursor: pointer;
          &:hover {
            color: #3caae0;
            text-decoration: underline;
          }
        }
      }
    }
    .top-right {
      right: 0;
      top: 0;
      width: 400px;
      height: 100%;
      img {
        width: 100%;
        height: 220px;
        border-radius: 6px;
        object-fit: cover;
      }
      .icon {
        text-align: center;
        font-size: 26px;
      }
      .text {
        text-align: center;
        line-height: 25px;
      }
      .num {
        text-align: center;
        font-size: 14px;
      }
    }
  }
}

.data-detail {
  width: 66vw;
  margin: 0 auto 30px;
  display: flex;

  .left {
    width: 70%;
    .data-head {
      width: 100%;
    }
    .basicInfo,
    .providerInfo {
      .el-col {
        margin-top: 20px;
      }
    }

    .data-list {
      .list {
        border: 1px solid #8b7e66;
        padding: 20px;
        .bottom {
          position: relative;
          height: 30px;
          .text {
            position: absolute;
            right: 0;
            top: 5px;
          }
        }
      }
    }
    .visual {
      .map,
      .photo,
      .excel {
        width: 100%;
      }
    }
  }

  .right {
    width: calc(30% - 10px);
    margin-left: 10px;

    .title {
      font-size: 20px;
      background: #232d46;
      color: white;
      height: 40px;
      line-height: 40px;
      margin-top: 30px;
      padding-left: 20px;
    }

    .similar {
      border: solid 1px #d6d6d6;
      box-sizing: border-box;
      padding: 15px;
      .similar-item {
        width: 100%;
        height: 30px;
        line-height: 30px;
        cursor: pointer;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        &:hover {
          font-weight: bolder;
          color: #465083;
        }
      }
      .page {
        display: flex;
        justify-content: center;
        margin-top: 5px;
      }
    }
    .visit {
      height: 300px;
      width: 100%;
      border: solid 1px #d6d6d6;
      box-sizing: border-box;
    }
  }
}
</style>