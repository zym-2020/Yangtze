<template>
  <div class="data-detail">
    <div class="left">
      <div class="data-card">
        <el-row>
          <el-col :span="20">
            <div class="top">
              <div class="text">{{ fileInfo?.name }}</div>
              <slot name="creator"></slot>
              <slot name="status"></slot>
              <el-tag
                style="margin-top: 10px; margin-left: 5px"
                type="danger"
                effect="dark"
                round
              >
                HOT
              </el-tag>
            </div>
          </el-col>
          <el-col :span="4">
            <div style="text-align: right; margin-top: 10px">
              <el-tooltip
                content="Public"
                placement="left-start"
                effect="light"
              >
                <el-icon :size="25" color="#00CD00"><Unlock /></el-icon>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>

        <!-- ////////////////////////////////////////////////////////////////// -->
        <div>
          <el-row style="margin-top: 10px">
            <el-col :span="7">
              <div class="block">
                <el-image
                  style="width: 250px; height: 160px"
                  :src="avatar"
                  fit="fill"
                  :previewSrcList="srcList"
                  :initial-index="0"
                />
              </div>
            </el-col>
            <el-col :span="17">
              <el-row>
                <div class="des">
                  {{ fileInfo?.description }}
                </div>
              </el-row>
              <el-row style="margin-top: 15px">
                <el-col>
                  <div class="bottom-bottom">
                    <el-tag
                      v-for="(item, index) in fileInfo?.tags"
                      :key="index"
                      style="
                        margin-right: 8px;
                        background-color: rgba(21, 69, 153, 0.8);
                        color: #e5cab9;
                      "
                      effect="dark"
                      round
                    >
                      {{ item }}
                    </el-tag>
                  </div>
                </el-col>
              </el-row>
            </el-col>
          </el-row>
        </div>
      </div>
      <!-- ////////////////////////////////////////////////////////////////// -->
      <el-row>
        <el-col :span="24">
          <div class="basicInfo">
            <el-row>
              <el-col :span="3">
                <div class="divider">
                  <div class="mark"></div>
                  <div class="text">
                    <strong>基本信息</strong>
                  </div>
                </div>
              </el-col>
              <el-col :span="20">
                <hr style="margin-top: 12px; color: #696969" />
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '数据时间：',
                      value: date(fileInfo.timeStamp),
                    }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{ key: '空间描述：', value: fileInfo.range }"
                  ></data-description>
                </div>
              </el-col>
            </el-row>
            <el-row style="margin-top: 8px">
              <el-col :span="12">
                <div class="basicInfo-item" style="color: #ff8c00">
                  <data-description
                    :data="{ key: '数据类型：', value: fileInfo?.type }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{ key: '数据标签：', value: fileInfo?.tags }"
                  ></data-description>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '更新时间：',
                      value: date(fileInfo?.updateTime),
                    }"
                  ></data-description>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="providerInfo">
            <el-row>
              <el-col :span="3">
                <div class="divider">
                  <div class="mark"></div>
                  <div class="text">
                    <strong>数据提供方</strong>
                  </div>
                </div>
              </el-col>
              <el-col :span="20">
                <hr style="margin-top: 12px; color: #696969" />
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="basicInfo-item" style="color: #ff8c00">
                  <data-description
                    :data="{
                      key: '作&#12288;&#12288;者：',
                      value: fileInfo.provider,
                    }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '电&#12288;&#12288;话：',
                      value: fileInfo.providerPhone,
                    }"
                  ></data-description>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '邮&#12288;&#12288;箱：',
                      value: fileInfo.providerEmail,
                    }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '地&#12288;&#12288;址：',
                      value: fileInfo.providerAddress,
                    }"
                  ></data-description>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-col>

        <el-col :span="24">
          <div class="description">
            <el-row>
              <el-divider>
                <el-icon><Monitor /></el-icon>
              </el-divider>
            </el-row>
            <!-- ///////////////////////////////////////////////////////////////////// -->
            <el-row>
              <el-col>
                <div
                  :style="{
                    border: '1px solid #8b7e66',
                    padding: '20px',
                  }"
                >
                  <el-table
                    :data="fileList"
                    max-height="800"
                    :style="{ width: 900 }"
                    stripe
                  >
                    <el-table-column
                      prop="名称"
                      label="文件名称"
                      width="650"
                      alignment="center"
                      :formatter="
                        (row) => {
                          return row.fileName;
                        }
                      "
                    />
                    <el-table-column
                      prop="文件大小"
                      label="文件大小"
                      width="150"
                      alignment="center"
                      :formatter="
                        (row) => {
                          return row.size;
                        }
                      "
                    />
                    <!-- ------------------------新增功能，单文件下载------------------------ -->

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
                    <!-- ------------------------新增功能，单文件下载------------------------ -->
                  </el-table>
                  <el-row>
                    <el-col :span="4" :offset="20">
                      <div style="margin-top: 25px; text-align: right">
                        <span
                          >共<strong style="color: #ff8c00">{{
                            fileList.length
                          }}</strong
                          >个文件</span
                        >
                      </div>
                    </el-col>
                  </el-row>
                </div>
              </el-col>
            </el-row>
            <el-row>
              <el-divider>
                <el-icon><Monitor /></el-icon>
              </el-divider>
              <el-row> </el-row>
            </el-row>
            <el-row style="margin-bottom: 20px">
              <el-col :span="3">
                <div class="divider">
                  <div class="mark"></div>
                  <div class="text">
                    <strong>数据可视</strong>
                  </div>
                </div>
              </el-col>
              <el-col :span="20">
                <hr style="margin-top: 12px; color: #696969" />
              </el-col>
            </el-row>
            <el-row v-if="visualSkeleton">
              <el-col>
                <div style="width: 950px; height: 500px">
                  <el-skeleton :rows="5" animated />
                </div>
              </el-col>
            </el-row>
            <el-row v-if="mapShow">
              <el-col>
                <div style="width: 950px; height: 900px">
                  <map-visual
                    :shpArray="shpArray"
                    :movePngArray="movePngArray"
                    :pngArray="pngArray"
                    :rasterTileArray="rasterTileArray"
                  />
                </div>
              </el-col>
            </el-row>
            <el-row v-if="photoShow">
              <el-col>
                <photo-visual :photoList="photoList" />
              </el-col>
            </el-row>

            <el-row v-if="excelShow">
              <div style="width: 950px">
                <excel-visual
                  :tableNameList="tableNameList"
                  :sandContentList="sandContentList"
                  :suspensionList="suspensionList"
                  :rateDirectionList="rateDirectionList"
                  :salinityList="salinityList"
                  :flowSandZList="flowSandZList"
                />
              </div>
            </el-row>
          </div>
        </el-col>

        <el-col :span="24">
          <div class="detail">
            <el-row>
              <el-col :span="3">
                <div class="divider">
                  <div class="mark"></div>
                  <div class="text">
                    <strong>数据使用说明</strong>
                  </div>
                </div>
              </el-col>
              <el-col :span="20">
                <hr style="margin-top: 12px; color: #696969" />
              </el-col>
            </el-row>
            <div class="editor-content-view" v-html="fileInfo.detail"></div>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <div class="download">
            <div class="basicInfo">
              <el-row>
                <el-col :span="5">
                  <div class="divider">
                    <div class="mark"></div>
                    <div class="text">
                      <strong>使用本数据的用户</strong>
                    </div>
                  </div>
                </el-col>
                <el-col :span="18">
                  <hr style="margin-top: 12px; color: #696969" />
                </el-col>
              </el-row>
            </div>
            <el-col>
              <el-row>
                <el-col>
                  <div
                    :style="{
                      border: '1px solid #8b7e66',
                      padding: '20px',
                      marginLeft: '140px',
                      marginTop: '30px',
                      marginBottom: '30px',
                      textAlign: 'center',
                    }"
                  >
                    <div>
                      <el-table
                        :data="downLoadList"
                        max-height="400"
                        :style="{ width: 650 }"
                        stripe
                      >
                        <el-table-column
                          prop="downloadTime"
                          label="下载时间"
                          width="300"
                          alignment="center"
                          :formatter="
                            (row) => {
                              return date(row.downloadTime);
                            }
                          "
                        />
                        <el-table-column
                          prop="name"
                          label="下载用户"
                          width="200"
                          alignment="center"
                          :formatter="
                            (row) => {
                              return row.name;
                            }
                          "
                        />
                      </el-table>
                    </div>
                    <div style="bottom: 0">
                      <el-row>
                        <el-col :span="4" :offset="20">
                          <div style="margin-top: 25px; text-align: right">
                            <span
                              >共<strong style="color: #ff8c00">{{
                                downLoadTotal
                              }}</strong
                              >次下载</span
                            >
                          </div>
                        </el-col>
                      </el-row>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </el-col>
          </div>
        </el-col>
      </el-row>
    </div>

    <div class="right">
      <div class="body">
        <div class="contributor">
          <div class="divider">
            <div class="mark"></div>
            <div class="text"><strong>条目创建者</strong></div>
          </div>
          <div class="creator">
            <el-avatar :src="avatarUrl" />
            <!-- 可能需要调用user接口来根据email查询姓名 -->
            <div class="name">{{ fileInfo.userName }}</div>
            <!-- 可能需要调用user接口来根据email查询姓名 -->
          </div>
          <el-divider content-position="center" color="orange"
            >数据缩略图</el-divider
          >
        </div>
      </div>
      <el-row>
        <el-col>
          <div style="border: #696969; padding: 20px">
            <images :src="thumbnail"></images>
          </div>
        </el-col>
      </el-row>
      <el-divider content-position="center" color="orange">空间位置</el-divider>
      <el-row>
        <el-col>
          <div>
            <location-map style="width: 440px; height: 440px"></location-map>
          </div>
        </el-col>
      </el-row>

      <div class="explain">
        <el-divider content-position="center" color="orange"
          >数据获取</el-divider
        >
        <div class="divider">
          <div class="mark"></div>

          <el-card
            shadow="always"
            style="margin: 10px; background-color: lightgrey"
          >
            <div class="text"><strong>数据获取方式</strong></div>
          </el-card>
        </div>
        <el-card shadow="always" style="margin: 10px; margin-left: 60px">
          <div class="content" v-if="fileInfo?.get_online">在线获取</div>

          <div class="content" v-if="!fileInfo?.get_online">订单获取</div>
        </el-card>
      </div>
      <div class="Process">
        <div class="divider">
          <div class="mark"></div>
          <el-card
            shadow="always"
            style="margin: 10px; background-color: lightgrey"
          >
            <div class="text"><strong>数据获取流程</strong></div>
          </el-card>
        </div>

        <div class="online" v-if="fileInfo?.get_online">
          <el-card shadow="always" style="margin: 10px; margin-left: 60px">
            <div class="content">[1]点击下载按钮</div>
          </el-card>
        </div>

        <div class="list" v-if="!fileInfo?.get_online">
          <el-card shadow="always" style="margin: 10px; margin-left: 60px">
            <div class="title">订单获取</div>
          </el-card>
          <el-card shadow="always" style="margin: 10px; margin-left: 60px">
            <div class="content">[1] 加入数据订单</div>
          </el-card>
          <el-card shadow="always" style="margin: 10px; margin-left: 110px">
            <div class="content">[2] 简单填写数据使用用途</div>
          </el-card>
          <el-card shadow="always" style="margin: 10px; margin-left: 160px">
            <div class="content">[3] 在线下载数据</div>
          </el-card>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, reactive } from "vue";
import DataDescription from "../page/DataDescription.vue";
import dataTable from "../scenePart/dataTable.vue";
import { dateFormat, imgBase64 } from "@/utils/common";
import { decrypt } from "@/utils/auth";
import { useStore } from "@/store";
import {
  pageQueryDownloadHistory,
  getCoordinates,
  findFiles,
  getDownloadURL,
} from "@/api/request";
import "@/assets/css/wangeditor.css";
import { notice } from "@/utils/notice";
import DataTable from "../scenePart/dataTable.vue";
import RateAndDirection from "../scenePart/RateAndDirection.vue";
import TideStatistics from "../resourcePages/components/TideStatistics.vue";
import SectionTide from "../resourcePages/components/SectionTide.vue";
import SandContent from "../resourcePages/components/SandContent.vue";
import LocationMap from "../scenePart/LocationMap.vue";

import chartContainer from "../scenePart/chartContainer.vue";
import ChartContainer from "../scenePart/chartContainer.vue";
import images from "./components/Images.vue";
import router from "@/router";

import MapVisual from "@/components/visual/MapVisual.vue";
import PhotoVisual from "@/components/visual/PhotoVisual.vue";
import ExcelVisual from "@/components/visual/ExcelVisual.vue";
import { prefix } from "@/prefix";
export default defineComponent({
  components: {
    DataDescription,
    dataTable,
    DataTable,
    chartContainer,
    ChartContainer,
    LocationMap,
    images,
    TideStatistics,
    RateAndDirection,
    SandContent,
    SectionTide,
    MapVisual,
    PhotoVisual,
    ExcelVisual,
  },
  props: {
    fileInfo: {
      type: Object,
    },
  },
  setup(props) {
    const store = useStore();
    const visualSkeleton = ref(true);
    const mapShow = ref(false);
    const photoShow = ref(false);
    const excelShow = ref(false);
    const shpArray = ref<
      { visualId: string; type: "line" | "fill" | "circle" }[]
    >([]);
    const pngArray = ref<{ visualId: string; coordinates: number[][] }[]>([]);
    const rasterTileArray = ref<string[]>([]);
    const movePngArray = ref<{ visualId: string; coordinates: number[][] }[]>(
      []
    );
    const photoList = ref<string[]>([]);
    const tableNameList = ref<string[]>([]);
    const sandContentList = ref<string[]>([]);
    const suspensionList = ref<string[]>([]);
    const rateDirectionList = ref<string[]>([]);
    const salinityList = ref<string[]>([]);
    const flowSandZList = ref<string[]>([]);
    const Yang = ref("长江区域");
    const downLoadList = ref<any[]>([]);
    const fileList = ref<any[]>([]);

    const downLoadTotal = computed(() => {
      return props.fileInfo?.download;
    });
    const location = computed(() => {
      return props.fileInfo?.location;
    });

    const fileInfo = computed(() => {
      return props.fileInfo;
    });

    const thumbnail = computed(() => {
      if ((props.fileInfo as any).thumbnail != "") {
        return prefix + "visual/getAvatar/" + (props.fileInfo as any).thumbnail;
      } else {
        return "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
      }
    });
    //头像
    const avatarUrl = computed(() => {
      return (props.fileInfo as any).userAvatar === ""
        ? "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        : prefix + "visual/getAvatar/" + (props.fileInfo as any).userAvatar;
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

    const srcList: any[] = reactive([`${avatar.value as any as string}`]);
    //规整日期
    const date = (time: string) => {
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

    const initVisual = async () => {
      //  获取file文件
      const fileData = await findFiles(
        router.currentRoute.value.params.id as string
      );
      if (fileData != null && (fileData as any).code === 0) {
        fileList.value = fileData.data;
      }
      console.log("gg", fileList.value);
      //获取file文件的可视化方法
      let MapFlag = false;
      let photoFlag = false;
      let excelFlag = false;
      for (let i = 0; i < fileList.value.length; i++) {
        if (fileList.value[i].visualType != "") {
          if (
            fileList.value[i].visualType === "lineVectorTile3D" ||
            fileList.value[i].visualType === "lineVectorTile"
          ) {
            shpArray.value.push({
              visualId: fileList.value[i].visualId,
              type: "line",
            });
            MapFlag = true;
          }
          if (
            fileList.value[i].visualType === "pointVectorTile" ||
            fileList.value[i].visualType === "pointVectorTile3D"
          ) {
            shpArray.value.push({
              visualId: fileList.value[i].visualId,
              type: "circle",
            });
            MapFlag = true;
          }
          if (
            fileList.value[i].visualType === "polygonVectorTile" ||
            fileList.value[i].visualType === "polygonVectorTile3D"
          ) {
            shpArray.value.push({
              visualId: fileList.value[i].visualId,
              type: "fill",
            });
            MapFlag = true;
          }
          if (fileList.value[i].visualType == "rasterTile") {
            rasterTileArray.value.push(fileList.value[i].visualId);
            MapFlag = true;
          }
          if (fileList.value[i].visualType == "png") {
            const coordinates = await getCoordinates(
              fileList.value[i].visualId
            );
            if (coordinates != null && (coordinates as any).code === 0) {
              pngArray.value.push({
                visualId: fileList.value[i].visualId,
                coordinates: coordinates.data,
              });
            }
            MapFlag = true;
          }
          if (fileList.value[i].visualType == "movePng") {
            const coordinates = await getCoordinates(
              fileList.value[i].visualId
            );
            if (coordinates != null && (coordinates as any).code === 0) {
              movePngArray.value.push({
                visualId: fileList.value[i].visualId,
                coordinates: coordinates.data,
              });
            }
            MapFlag = true;
          }
          if (fileList.value[i].visualType === "photo") {
            photoList.value.push(
              `${prefix}visual/getPhoto/${fileList.value[i].id}`
            );
            photoFlag = true;
          }
          if (fileList.value[i].visualType === "sandContent") {
            sandContentList.value.push(fileList.value[i].visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (fileList.value[i].visualType === "suspension") {
            suspensionList.value.push(fileList.value[i].visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (fileList.value[i].visualType === "rateDirection") {
            rateDirectionList.value.push(fileList.value[i].visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (fileList.value[i].visualType === "salinity") {
            salinityList.value.push(fileList.value[i].visualId);
            tableNameList.value.push(fileList.value[i].fileName);
            excelFlag = true;
          }
          if (fileList.value[i].visualType === "flowSand_Z") {
            flowSandZList.value.push(fileList.value[i].visualId);
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

    onMounted(async () => {
      await initVisual();
      await initDownloadHistory();
    });

    return {
      fileInfo,
      Yang,
      photoList,
      date,
      avatar,
      downloadOrigin,
      location,
      avatarUrl,
      thumbnail,
      downLoadList,
      fileList,
      srcList,
      sandContentList,
      suspensionList,
      rateDirectionList,
      salinityList,
      flowSandZList,
      downLoadTotal,
      visualSkeleton,
      mapShow,
      photoShow,
      excelShow,
      shpArray,
      pngArray,
      tableNameList,
      movePngArray,
      rasterTileArray,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 20px;
  margin: 0;
  text-align: center;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: #d3dce6;
}

.video:hover {
  //background-color: #859ecc;
  transform: scale(1.2);
}
.data-detail {
  border-color: #c4bfbf;
  border-style: solid;
  //padding:10px;
  width: 1440px;
  margin: 0 auto;
  display: flex;

  .left {
    width: 950px;
    margin-top: 10px;
    margin-left: 20px;

    .avatar {
      position: relative;
      .el-avatar {
        position: absolute;
        right: 100px;
      }
    }

    .providerInfo {
      margin-top: 20px;
      .providerInfo-item {
        /deep/ .data-description {
          .value {
            width: 840px;
          }
        }
      }
    }

    .divider {
      height: 25px;
      display: flex;
      position: relative;
      .mark {
        height: 25px;
        width: 8px;
        background: #458fd1;
      }
      .text {
        line-height: 25px;
        margin-left: 10px;
        //text-align:justify;
      }
    }
    .basicInfo-item,
    .providerInfo-item {
      margin-top: 10px;
    }

    .description {
      margin-top: 20px;
      display:flex .des {
        margin: 10px;
      }
    }
    .detail {
      margin-top: 40px;
    }

    .download {
      margin-top: 30px;
      margin-bottom: 20px;
      .type {
        margin-top: 20px;
        //display: flex;
        .type-item {
          flex: 1;
          .type-item-top {
            display: flex;
            .type-item-top-text {
              margin-left: 15px;
            }
          }
          .type-item-bottom {
            margin-top: 10px;
          }
        }
      }
    }
  }
  .right {
    width: 440px;
    margin-left: 10px;
    margin-top: 10px;
    min-height: calc(100vh - 170px);
    background: #f6f7fa;
    .body {
      margin: 20px 10px;
      .divider {
        margin-bottom: 10px;
        margin-top: 20px;
        height: 25px;
        display: flex;
        .mark {
          height: 25px;
          width: 8px;
          background: #00d083;
        }
        .text {
          line-height: 25px;
          margin-left: 10px;
        }
      }
      .contributor {
        margin-bottom: 10px;
        .creator {
          margin-top: 10px;
          display: flex;
          .name {
            line-height: 40px;
            margin-left: 10px;
            font-size: 18px;
          }
        }
        .el-divider {
          /deep/ .el-divider__text {
            background: #f6f7fa;
          }
        }
      }
      .Process {
        .online,
        .list {
          margin-left: 10px;
          .title {
            color: #94bddb;
            margin-bottom: 10px;
            margin-top: 10px;
          }
        }
      }
      .content {
        font-size: 14px;
        margin-left: 10px;
        margin-bottom: 5px;
      }
    }
  }
}

.data-card {
  margin-bottom: 5px;
  .top {
    display: flex;
    position: relative;
    height: 40px;
    line-height: 40px;
    .text {
      //margin-left: 10px;
      font-size: 22px;
      color: #4fb5ea;
    }
  }
  .des {
    margin-top: 8px;
    font-size: 14px;
    display: flex;
    line-height: 30px;
    justify-content: center;
    align-items: center;
  }
  .bottom {
    .bottom-top {
      display: flex;
      margin-top: 10px;
      .watch,
      .download {
        margin-left: 20px;
        position: relative;
        .el-icon {
          position: absolute;
          top: 2px;
        }
        span {
          margin-left: 20px;
        }
      }
    }
    .bottom-bottom {
      margin-top: 10px;
      .el-tag {
        margin-right: 10px;
      }
    }
  }
}

div.scene-wrapper {
  height: 93.8vh;
  background: linear-gradient(50deg, #448bc5 0%, darken(#448bc5, 70%) 100%);
  background-size: 200% 200%;
  animation: background 6s ease infinite;
  @keyframes background {
    0% {
      background-position: 0% 50%;
    }
    50% {
      background-position: 100% 50%;
    }
    100% {
      background-position: 0% 50%;
    }
  }

  div.content-wrapper {
    position: absolute;
    height: 100%;
    width: 100%;
    //top: 6%;
    background-color: transparent;
    display: flex;
    flex-flow: column wrap;
    justify-content: space-around;
    align-content: space-around;

    div {
      box-sizing: border-box;

      &.overall-data-wrapper {
        height: 27%;
        width: 23%;
        background-color: rgba(255, 255, 255, 0.3);
        border-radius: 0.6em;

        div.clock {
          text-align: center;
          position: absolute;
          left: 15%;
          top: -2%;
          line-height: 1em;
          p {
            font-family: Impact, Haettenschweiler, "Arial Narrow Bold",
              sans-serif;
            color: rgba(255, 255, 255, 0.8);
            letter-spacing: 0.05em;
            font-size: 2em;
            text-shadow: 0 0.5px 0 #ccc, 0 1px 0 #c9c9c9, 0 1.5px 0 #bbb,
              0 2px 0 #b9b9b9, 0 2.5px 0 #aaa, 0 3px 0.5px rgba(0, 0, 0, 0.1),
              0 0 2.5px rgba(0, 0, 0, 0.1), 0 0.5px 1.5px rgba(0, 0, 0, 0.3),
              0 1.5px 2.5px rgba(0, 0, 0, 0.2), 0 2.5px 5px rgba(0, 0, 0, 0.25),
              0 5px 5px rgba(0, 0, 0, 0.2), 0 10px 10px rgba(0, 0, 0, 0.15);
          }

          p {
            width: 100%;
            height: 100%;
            line-height: 100px;
            text-align: center;
            vertical-align: middle;
          }
        }
      }
    }
  }
}
</style>