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
        <div>
          <el-row style="margin-top: 10px">
            <el-col :span="5">
              <div class="block">
                <el-image
                  style="width: 180px; height: 160px"
                  :src="avatar"
                  fit="fill"
                  :previewSrcList="srcList"
                  :initial-index="0"
                />
              </div>
            </el-col>
            <el-col :span="19">
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
                    :data="{ key: '数据时间：', value: fileMeta?.time }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{ key: '空间位置：', value: fileMeta?.range }"
                  ></data-description>
                </div>
              </el-col>
            </el-row>
            <el-row style="margin-top: 8px">
              <el-col :span="12">
                <div class="basicInfo-item" style="color: #ff8c00">
                  <data-description
                    :data="{ key: '数据类型：', value: fileMeta?.type }"
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
                    value: date((fileInfo as any).updateTime),
                  }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '数据大小：',
                      value: Size,
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
                      value: fileMeta?.provider,
                    }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '电&#12288;&#12288;话：',
                      value: fileMeta?.provider_phone,
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
                      value: fileMeta?.provider_email,
                    }"
                  ></data-description>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="basicInfo-item">
                  <data-description
                    :data="{
                      key: '地&#12288;&#12288;址：',
                      value: fileMeta?.provider_address,
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
                <!-- <el-icon><star-filled /></el-icon> -->
              </el-col>
            </el-row>
            <el-row>
              <el-divider>
                <el-icon><Monitor /></el-icon>
              </el-divider>
            </el-row>

            <el-row>
              <el-col>
                <div
                  :style="{
                    height: tableHeight,
                    border: '1px solid #8b7e66',
                    padding: '20px',
                  }"
                >
                  <el-table
                    :data="fileLis"
                    max-height="800"
                    :style="{ width: 900, height: tableHeight - 40 }"
                    stripe
                  >
                    <el-table-column
                      prop="名称"
                      label="文件名称"
                      width="450"
                      alignment="center"
                      :formatter="
                        (row) => {
                          return row.名称;
                        }
                      "
                    />
                    <el-table-column
                      prop="原始大小"
                      label="原始大小"
                      width="150"
                      alignment="center"
                      :formatter="
                        (row) => {
                          if (row.原始大小 / 1000 < 1000)
                            return (row.原始大小 / 1000).toFixed(2) + KB;
                          else return (row.原始大小 / 1000000).toFixed(2) + MB;
                        }
                      "
                    />
                    <el-table-column
                      prop="压缩后大小"
                      label="压缩大小"
                      width="150"
                      alignment="center"
                      :formatter="
                        (row) => {
                          if (row.压缩后大小 / 1000 < 1000)
                            return (row.压缩后大小 / 1000).toFixed(2) + KB;
                          else
                            return (row.压缩后大小 / 1000000).toFixed(2) + MB;
                        }
                      "
                    />
                    <el-table-column
                      prop="类型"
                      label="文件类型"
                      width="150"
                      alignment="center"
                    >
                    </el-table-column>
                  </el-table>
                  <el-row>
                    <el-col :span="4" :offset="20">
                      <div style="margin-top: 25px; text-align: right">
                        <span
                          >共<strong style="color: #ff8c00">{{
                            fileLis.length
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
            <el-row v-if="shpCheck">
              <el-col>
                <div
                  v-if="true"
                  class="scene-wrapper"
                  style="width: 950px; height: 900px"
                >
                  <div
                    class="content-wrapper"
                    style="width: 950px; height: 900px"
                  >
                    <centerVisualMap
                      mapId="1"
                      style="width: 950px; height: 900px"
                    ></centerVisualMap>
                  </div>
                </div>
              </el-col>
            </el-row>
            <el-row v-if="photoCheck">
              <el-col>
                <div class="detail">
                  <el-row>
                    <el-col :span="4">
                      <div class="divider">
                        <div class="mark"></div>
                        <div class="text">
                          <strong>物理模型数据可视</strong>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="20">
                      <hr style="margin-top: 12px; color: #696969" />
                      <!-- <el-icon><star-filled /></el-icon> -->
                    </el-col>
                  </el-row>
                </div>
                <div>
                  <el-carousel
                    :interval="2000"
                    type="card"
                    style="margin-top: 30px"
                  >
                    <el-carousel-item v-for="item in photoList" :key="item">
                      <el-image fit="fill" :src="item" />
                    </el-carousel-item>
                  </el-carousel>
                </div>
              </el-col>
            </el-row>
            <el-row v-if="tableCheck">
              <el-col>
                <div class="detail">
                  <el-row>
                    <el-col :span="4">
                      <div class="divider">
                        <div class="mark"></div>
                        <div class="text">
                          <strong>测站可视化图表</strong>
                        </div>
                      </div>
                    </el-col>
                    <el-col :span="20">
                      <hr style="margin-top: 12px; color: #696969" />
                      <!-- <el-icon><star-filled /></el-icon> -->
                    </el-col>
                  </el-row>
                </div>

                <el-row :gutter="20" justify="center">
                  <el-col :span="8" style="text-align: center">
                    <el-card
                      class="video"
                      shadow="always"
                      @click="
                        i = (i - 1 + nameTable.length) % nameTable.length;
                        changeTableName();
                      "
                      style="margin-top: 30px; margin-bottom: 10px"
                    >
                      <div class="text">
                        <el-icon style="margin-top: 5px"
                          ><ArrowLeftBold
                        /></el-icon>
                        <strong>{{
                          nameTable[
                            (i - 1 + nameTable.length) % nameTable.length
                          ]
                        }}</strong>
                      </div>
                    </el-card>
                  </el-col>
                  <el-col :span="8" style="text-align: center">
                    <el-card
                      shadow="always"
                      @click="changeTableName()"
                      style="
                        margin-top: 10px;
                        margin-bottom: 10px;
                        background-color: lightgrey;
                      "
                    >
                      <div class="text">
                        <strong>{{ nameTable[i] }}</strong>
                      </div>
                    </el-card>
                  </el-col>
                  <el-col :span="8" style="text-align: center">
                    <el-card
                      class="video"
                      shadow="always"
                      @click="
                        i = (i + 1) % nameTable.length;
                        changeTableName();
                      "
                      style="margin-top: 30px; margin-bottom: 10px"
                    >
                      <div class="text">
                        <strong>{{
                          nameTable[(i + 1) % nameTable.length]
                        }}</strong>
                        <el-icon><ArrowRightBold /></el-icon>
                      </div>
                    </el-card>
                  </el-col>
                </el-row>

                <div style="width: 100%; height: 80%">
                  <TideStatistics
                    :getName="getName"
                    :key="new Date().getTime()"
                  ></TideStatistics>
                </div>
              </el-col>
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
                <!-- <el-icon><star-filled /></el-icon> -->
              </el-col>
            </el-row>
            <div class="editor-content-view" v-html="fileMeta?.detail"></div>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="download">
            <div class="basicInfo">
              <el-row>
                <el-col :span="3">
                  <div class="divider">
                    <div class="mark"></div>
                    <div class="text">
                      <strong>数据下载</strong>
                    </div>
                  </div>
                </el-col>
                <el-col :span="20">
                  <hr style="margin-top: 12px; color: #696969" />
                  <!-- <el-icon><star-filled /></el-icon> -->
                </el-col>
              </el-row>
            </div>
            <div class="type">
              <el-row>
                <el-rol
                  :span="8"
                  style="margin-left: 101px; margin-right: 101px"
                >
                  <div class="type-item">
                    <div class="type-item-top">
                      <svg style="width: 20px; height: 20px">
                        <use xlink:href="#icon-shujuku"></use>
                      </svg>
                      <div class="type-item-top-text">原始数据</div>
                    </div>
                    <div class="type-item-bottom">
                      <el-button
                        size="small"
                        v-if="fileMeta?.get_online"
                        @click="downloadOrigin"
                      >
                        下载
                      </el-button>
                      <el-button size="small" v-else @click="downloadOrigin">
                        添加订单
                      </el-button>
                    </div>
                  </div>
                </el-rol>
                <el-rol
                  :span="8"
                  style="margin-left: 101px; margin-right: 101px"
                >
                  <div class="type-item">
                    <div class="type-item-top">
                      <svg style="width: 20px; height: 20px">
                        <use xlink:href="#icon-shujuku1"></use>
                      </svg>
                      <div class="type-item-top-text">整合数据</div>
                    </div>
                    <div class="type-item-bottom">
                      <el-button size="small" v-if="fileMeta?.get_online">
                        下载
                      </el-button>
                      <el-button size="small" v-else @click="downloadOrigin">
                        添加订单
                      </el-button>
                    </div>
                  </div>
                </el-rol>
                <el-rol
                  :span="8"
                  style="margin-left: 101px; margin-right: 101px"
                >
                  <div class="type-item">
                    <div class="type-item-top">
                      <svg style="width: 20px; height: 20px">
                        <use xlink:href="#icon-shuju"></use>
                      </svg>
                      <div class="type-item-top-text">可视化数据</div>
                    </div>
                    <div class="type-item-bottom">
                      <el-button size="small" v-if="fileMeta?.get_online">
                        下载
                      </el-button>
                      <el-button size="small" v-else @click="downloadOrigin">
                        添加订单
                      </el-button>
                    </div>
                  </div>
                </el-rol>
              </el-row>
            </div>
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
                  <!-- <el-icon><star-filled /></el-icon> -->
                </el-col>
              </el-row>
            </div>
            <el-col>
              <el-row>
                <el-col>
                  <!-- <div class="scene-wrapper" style="width: 950px; height: 308px">
                  <div
                    class="content-wrapper"
                    style="width: 950px; height: 308px"
                  >
                    <chart-container
                      chartId="3"
                      order="0"
                      styleType="3"
                      style="width: 950px; height: 308px"
                    ></chart-container>
                  </div>
                </div> -->

                  <div
                    :style="{
                      height: '350px',
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
                        :data="downLoadLis"
                        :style="{ width: 650, height: 300 }"
                        stripe
                      >
                        <!-- <el-table-column
                    v-for="(item, key, index) in fileLis[0]"
                    :key="index"
                    :prop="key"
                    :label="key"
                    width="200"
                    align="center"
                  > -->
                        <el-table-column
                          prop="download_time"
                          label="下载时间"
                          width="300"
                          alignment="center"
                          :formatter="
                            (row) => {
                              return new Date(
                                row.download_time
                              ).toLocaleString();
                            }
                          "
                        />
                        <el-table-column
                          prop="name"
                          label="用户姓名"
                          width="200"
                          alignment="center"
                          :formatter="
                            (row) => {
                              return row.name;
                            }
                          "
                        />
                        <el-table-column
                          prop="data_type"
                          label="数据格式"
                          width="150"
                          alignment="center"
                          :formatter="
                            (row) => {
                              if (row.data_type == 'origin') return '原始数据';
                              else if (row.data_type == 'structed')
                                return '整合数据';
                              else if (row.data_type == 'visual')
                                return '可视化数据';
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
                              >名用户下载</span
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
            <div class="name">{{ fileMeta?.name }}</div>
          </div>
          <!-- <el-divider content-position="center"
            >{{ date(fileInfo.createTime) }} 创建</el-divider
          > -->
          <el-divider content-position="center" color="orange"
            >数据缩略图</el-divider
          >
        </div>
      </div>
      <el-row>
        <el-col>
          <div style="border: #696969; padding: 20px">
            <images :src="thumbnail"></images>
            <!-- <images ></images> -->
          </div>
        </el-col>
      </el-row>
      <el-divider content-position="center" color="orange">空间位置</el-divider>
      <el-row>
        <el-col>
          <div>
            <div class="scene-wrapper" style="width: 440px; height: 440px">
              <div class="content-wrapper" style="width: 440px; height: 440px">
                <location-map
                  v-if="true"
                  style="width: 440px; height: 440px"
                ></location-map>
              </div>
            </div>
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
          <div class="content" v-if="fileMeta?.get_online">在线获取</div>

          <div class="content" v-if="!fileMeta?.get_online">订单获取</div>
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

        <div class="online" v-if="fileMeta?.get_online">
          <el-card shadow="always" style="margin: 10px; margin-left: 60px">
            <div class="content">[1]点击下载按钮</div>
          </el-card>
        </div>

        <div class="list" v-if="!fileMeta?.get_online">
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
      <div class="body">
        <div class="contributor">
          <!-- <el-divider content-position="center"
            >{{ date(fileInfo.createTime) }} 创建</el-divider
          > -->
          <el-divider content-position="center" color="orange"
            >相似数据</el-divider
          >
          <div v-for="(item, index) in similiarList" :key="index">
            <el-card
              shadow="always"
              style="margin: 10px; background-color: rgba(21, 69, 153, 0.2)"
              class="video"
            >
              <div class="content" @click="routeTo(item, index)">
                <li>{{ item.name }}</li>
              </div>
            </el-card>
          </div>

          <!-- </div> -->
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  computed,
  defineComponent,
  onMounted,
  ref,
  reactive,
  onBeforeMount,
  watch,
} from "vue";
import DataDescription from "../page/DataDescription.vue";
import dataTable from "../scenePart/dataTable.vue";
import { dateFormat, imgBase64 } from "@/utils/common";
import { decrypt } from "@/utils/auth";
import { useStore } from "@/store";
import {
  getDownloadURL,
  getOtherTags,
  getShareFileById,
  pageQueryDownloadHistory,
  ZipEntryPath,
  QueryHeightByName,
} from "@/api/request";
import "@/assets/css/wangeditor.css";
import { notice } from "@/utils/notice";
import DataTable from "../scenePart/dataTable.vue";
import centerVisualMap from "../scenePart/centerVisualMap.vue";
import TideStatistics from "../resourcePages/components/TideStatistics.vue";
import LocationMap from "../scenePart/LocationMap.vue";
import FindMap from "../scenePart/FindMap.vue";
import chartContainer from "../scenePart/chartContainer.vue";
import ChartContainer from "../scenePart/chartContainer.vue";
import images from "./components/Images.vue";
import { List } from "echarts";
import router from "@/router";
import { getTagName } from "@wangeditor/core/dist/core/src/utils/dom";

export default defineComponent({
  components: {
    DataDescription,
    dataTable,
    DataTable,
    chartContainer,
    ChartContainer,
    centerVisualMap,
    LocationMap,
    images,
    FindMap,
    TideStatistics,
  },
  props: {
    fileInfo: {
      type: Object,
    },
    fileMeta: {
      type: Object,
    },
  },
  setup(props) {
    const store = useStore();
    const fileInfo = computed(() => {
      return props.fileInfo;
    });
    const fileMeta = computed(() => {
      return props.fileMeta;
    });
    const KB = ref("KB");
    const MB = ref("MB");
    const getName = ref("一干河站潮位观测成果表");
    const Size = ref("");
    const mapref = ref();
    const shpCheck = ref(false);
    const photoCheck = ref(false);
    const tableHeight = ref();
    const tableCheck = ref(false);
    const i = ref(0);
    let userId = router.currentRoute.value.params.id;
    const similiarLis = ref<any[]>([]);
    const nameTable = reactive([
      "一干河站潮位观测成果表",
      "六滧站潮位观测成果表",
      "吴淞口站潮位观测成果表",
      "天生港灰场站潮位观测成果表",
      "天生港站潮位观测成果表",
      "太字圩港站潮位观测成果表",
      "如皋港站潮位观测成果表",
      "崇头站潮位观测成果表",
      "徐六泾站潮位观测成果表",
      "望虞河站潮位观测成果表",
      "杨林站潮位观测成果表",
      "江海油库码头站潮位观测成果表",
      "江阴站潮位观测成果表",
      "白茆站潮位观测成果表",
      "营船港站潮位观测成果表",
      "连兴港站潮位观测成果表",
      "青龙港站潮位观测成果表",
    ]);
    const changeTableName = () => {
      getName.value = nameTable[i.value];
      console.log(getName.value);
    };
    const downLoadLis = ref<any[]>([
      { download_time: "无", name: "无", data_type: "无" },
    ]);
    const fileLis = ref<any[]>([
      { download_time: "无", name: "无", data_type: "无" },
    ]);
    const similiarList = computed(() => {
      const lis = [];
      for (let i = 0; i < similiarLis.value.length; i++) {
        if (check((props.fileInfo as any).tags, similiarLis.value[i].tags))
          lis.push(similiarLis.value[i]);
      }
      return lis;
    });
    //检查是否为相似数据
    const check = (tag1: Array<String>, tag2: Array<String>): boolean => {
      let interset = tag1.filter(function (v) {
        return tag2.indexOf(v) > -1;
      }).length;
      return interset >= 2 ? true : false;
    };
    //照片列表
    const phot = ref<HTMLElement>();
    const photoList = reactive<String[]>([]);
    if (
      (router.currentRoute.value.params.fileInfo as any).tags.indexOf(
        "SHAPEFILE"
      ) != -1
    )
      shpCheck.value = true;

    if (
      (router.currentRoute.value.params.fileInfo as any).tags.indexOf("照片") !=
      -1
    )
      photoCheck.value = true;
    if (
      (router.currentRoute.value.params.fileInfo as any).tags.indexOf(
        "潮位数据"
      ) != -1
    )
      tableCheck.value = true;
    //缩略图
    const thumbnail = computed(() => {
      if (
        (props.fileInfo as any).thumbnail != "" &&
        (props.fileInfo as any).thumbnail != undefined &&
        (props.fileInfo as any).thumbnail != null
      ) {
        return "http://localhost:8002" + (props.fileInfo as any).thumbnail;
      }
      ("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
    });
    //头像
    const avatarUrl = computed(() => {
      return (props.fileMeta as any).avatar === ""
        ? "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        : "http://localhost:8002" + (props.fileMeta as any).avatar;
    });
    const avatar = computed(() => {
      if (
        (props.fileInfo as any).avatar != "" &&
        (props.fileInfo as any).avatar != undefined &&
        (props.fileInfo as any).avatar != null
      ) {
        return "http://localhost:8002" + (props.fileInfo as any).avatar;
      }
      return imgBase64(
        (props.fileInfo as any).name === undefined
          ? ""
          : (props.fileInfo as any).name
      );
    });
    //规整日期
    const date = (time: string) => {
      return dateFormat(time, "yyyy-MM-dd");
    };
    const downLoadTotal = ref(0);
    const downloadOrigin = async () => {
      const data = await getDownloadURL((fileInfo.value as any).id);
      if (data != null) {
        if ((data as any).code === 0) {
          window.location.href =
            "http://localhost:8002/download/downloadShareFile/" +
            store.state.user.id +
            "/" +
            decrypt(data.data, store.state.user.id);
        } else {
          notice("error", "错误", (data as any).msg);
        }
      }
    };
    const size = computed(() => {
      let sum = 0;
      let i = 0;
      for (; i < fileLis.value.length; i++) {
        sum = sum + fileLis.value[i].压缩后大小;
      }
      return sum / 1024 + "KB";
    });

    //路由跳转，相当于人为刷新界面
    const routeTo = async (item: any, index: number) => {
      const data = await getShareFileById(item.id);
      const data2 = await getOtherTags(item.id as string);
      const data3 = await ZipEntryPath(
        router.currentRoute.value.params.id as any
      );
      fileLis.value = data3 as any;
      tableHeight.value = fileLis.value.length * 40 + 82;
      const size = computed((): string => {
        let sum = 0;
        let i = 0;
        for (; i < fileLis.value.length; i++) {
          sum = sum + fileLis.value[i].压缩后大小;
        }
        if (sum / 1024 < 1024) return ((sum / 1024).toFixed(2) as string) + KB;
        else return ((sum / 1048576).toFixed(2) as string) + MB;

        // return (sum / 1000) as any as string;
      });
      Size.value = size.value;
      //       watch(router.currentRoute.value,(newValue,oldValue)=>{
      //   console.log("路有变化了",newValue,oldValue)},{immediate:true,deep:true}
      // )
      //console.log(router.currentRoute.value)
      similiarLis.value = data2.data.list;
      router.push({
        name: "shareFile",
        params: {
          id: item.id,
          fileInfo: JSON.stringify(data.data.list),
        },
      });
    };
    const srcList: any[] = reactive([`${avatar.value as any as string}`]);
    onBeforeMount(async () => {
      //查询并显示10条历史下载记录
      const data2 = await pageQueryDownloadHistory(10, 0, userId as string);
      if (data2 != null) {
        downLoadLis.value = data2.data.list;
        downLoadTotal.value = data2.data.total;
      } else console.log("nulll");
    });
    onMounted(async () => {
      //获取压缩文件
      const data = await ZipEntryPath(
        router.currentRoute.value.params.id as any
      );
      fileLis.value = data as any;
      if (photoCheck.value)
        for (let i = 0; i < fileLis.value.length; i++) {
          photoList.push(
            "http://localhost:8002/file/photos/" + fileLis.value[i].名称
          );
        }
      //是否为Shp类数据
      if (
        (router.currentRoute.value.params.fileInfo as any).tags.indexOf(
          "SHAPEFILE"
        ) != -1
      )
        if (
          (router.currentRoute.value.params.fileInfo as any).tags.indexOf(
            "潮位数据"
          ) != -1
        )
          //是否为潮位类数据
          tableCheck.value = true;
      //是否为Photo类数据
      if (
        (router.currentRoute.value.params.fileInfo as any).tags.indexOf(
          "照片"
        ) != -1
      )
        photoCheck.value = true;

      //动态生成压缩文件表格高度
      tableHeight.value = fileLis.value.length * 40 + 122;

      //const data2= await pageQueryDownloadHistory(10,0,userId as string)
      //根据标签获取相似数据
      const data1 = await getOtherTags(userId as string);
      similiarLis.value = data1.data.list;
      //计算压缩文件大小
      const size = computed((): string => {
        let sum = 0;
        let i = 0;
        for (; i < fileLis.value.length; i++) {
          sum = sum + fileLis.value[i].压缩后大小;
        }

        if (sum / 1024 < 1024)
          return ((sum / 1024).toFixed(2) as string) + KB.value;
        else return ((sum / 1048576).toFixed(2) as string) + MB.value;
      });
      Size.value = size.value;
    });

    return {
      fileInfo,
      fileMeta,
      phot,
      i,
      photoList,
      nameTable,
      date,
      tableCheck,
      avatar,
      downloadOrigin,
      avatarUrl,
      thumbnail,
      similiarList,
      similiarLis,
      downLoadLis,
      fileLis,
      routeTo,
      srcList,
      downLoadTotal,
      KB,
      MB,
      tableHeight,
      size,
      Size,
      mapref,
      shpCheck,
      photoCheck,
      getName,
      changeTableName,
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