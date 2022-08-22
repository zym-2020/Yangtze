<template>
  <div class="data-card" v-show="n">
    <div class="top" style="margin: 5px">
      <el-avatar :size="40" :src="avatar" />
      <div class="text">{{ messageSender }}</div>
      <slot name="creator"></slot>
    </div>
    <div>
      <el-row style="height: 200px">
        <el-col :span="10" :offset="6">
          <el-result
            :icon="iconType"
            :title="titlemessage"
            :sub-title="subtitleMessage"
          >
          </el-result>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10" :offset="6">
          <div style="text-align: center">
            {{ explainMessage }}
          </div>
          <div v-show="messageResponse == 'examine'">
            <el-progress
              :percentage="100"
              status="success"
              :indeterminate="true"
              :duration="5"
              stroke-width="6"
            />
          </div>
        </el-col>
      </el-row>
    </div>
    <el-row>
      <el-col :span="5">
        <div class="watch" style="margin: 10px">
          <strong>资源名称：</strong>
          <span> {{ dataName }}</span>
        </div>
      </el-col>
      <el-col :span="5">
        <div class="watch" style="margin: 10px">
          <strong>发送至：</strong>
          <span> {{ messageReceiver }}</span>
        </div>
      </el-col>
    </el-row>
    <el-row style="margin-left: 10px">
      <el-col :span="5">
        <div class="time">
          <strong>上传时间：</strong>
          <span>{{ dataUploadTime }}</span>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="time">
          <strong>管理员审核时间：</strong>
          <span>{{ dataExamineTime }}</span>
        </div>
      </el-col>
      <el-col :span="4">
        <div v-if="messageResponse == 'success'" class="time">
          <el-icon :color="iconColor">
            <Select />
          </el-icon>
          <span>{{ examineStatusCheck }}</span>
        </div>
        <div v-else-if="messageResponse == 'fail'" class="time">
          <el-icon :color="iconColor">
            <CloseBold />
          </el-icon>
          <span>{{ examineStatusCheck }}</span>
        </div>
        <div v-else-if="messageResponse == 'examine'" class="time">
          <el-icon
            :color="iconColor"
            style="margin-top: 5px; margin-right: 3px"
          >
            <Clock />
          </el-icon>
          <span>{{ examineStatusCheck }}</span>
        </div>
      </el-col>
      <el-col :span="4" :offset="4">
        <div style="float: right">
          <el-tooltip
            content="该条消息将被回收至【历史消息】"
            placement="bottom"
            effect="light"
          >
            <el-button type="primary" @click="deleteMessage">
              已知晓通知</el-button
            >
          </el-tooltip>
        </div>
      </el-col>
    </el-row>
    <div v-show="showDetail">
      <el-row>
        <el-col :span="4" :offset="10">
          <div
            v-show="showDetails"
            class="flex justify-space-between mb-4 flex-wrap gap-4"
          >
            <n-button
              strong
              secondary
              round
              type="primary"
              @click="showDetails = !showDetails"
              ><el-icon><ArrowDownBold /></el-icon>
              查看数据描述
            </n-button>
          </div>
        </el-col>
      </el-row>
      <div v-show="!showDetails" style="margin-top: 20px">
        <el-row>
          <el-col>
            <hr style="border-color: #d8d8d8" />
            <el-descriptions
              class="margin-top"
              title="资源审核"
              :column="2"
              :size="size"
              border
            >
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <user />
                    </el-icon>
                    资源名称
                  </div>
                </template>
                {{ dataCache.name }}
              </el-descriptions-item>

              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Download /></el-icon>
                    资源下载量
                  </div>
                </template>
                {{ dataCache.download }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><View /></el-icon>
                    资源浏览量
                  </div>
                </template>
                {{ dataCache.watch }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Stamp /></el-icon>
                    数据提供者
                  </div>
                </template>
                {{ dataCache.creator }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><List /></el-icon>
                    资源类别
                  </div>
                </template>
                <el-tag
                  v-for="(item, index) in dataCacheTags"
                  :key="index"
                  class="ml-2"
                  :type="tagsType[index % 4]"
                  style="margin-right: 10px"
                  >{{ item }}</el-tag
                >
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源描述
                  </div>
                </template>
                {{ dataCache.description }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Cloudy /></el-icon>
                    原始数据
                  </div>
                </template>
                {{ dataCache.originName }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><DataAnalysis /></el-icon>
                    整合数据
                  </div>
                </template>
                {{ dataCache.structuredName }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><DataLine /></el-icon>
                    可视化数据
                  </div>
                </template>
                {{ dataCache.visualName }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><LocationInformation /></el-icon>
                    数据存放路径
                  </div>
                </template>
                {{ dataCache.originAddress }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Pointer /></el-icon>
                    资源共享方式
                  </div>
                </template>
                No.1188, Wuzhong Avenue, Wuzhong District, Suzhou, Jiangsu
                Province
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><SetUp /></el-icon>
                    资源可视化来源
                  </div>
                </template>
                {{ dataCache.visualSource }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><PriceTag /></el-icon>
                    资源可视化类型
                  </div>
                </template>
                {{ dataCache.visualType }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Share /></el-icon>
                    资源共享方式
                  </div>
                </template>
                {{ dataCache.structuredSource }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Histogram /></el-icon>
                    资源创造时间
                  </div>
                </template>
                {{ dataCache.createTime }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Sort /></el-icon>
                    资源更新时间
                  </div>
                </template>
                {{ dataCache.updateTime }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><StarFilled /></el-icon>
                    资源状态
                  </div>
                </template>
                {{ dataCache.status }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon><Picture /></el-icon>
                    资源封面
                  </div>
                </template>
                {{ dataCache.avatar }}
              </el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>
        <el-row style="margin: 20px">
          <el-col :span="1.5" :offset="10">
            <div style="text-align: center">
              <n-button type="primary" @click="checkYes" ghost
                >同意上传</n-button
              >
            </div>
          </el-col>
          <el-col :span="1.5" :offset="1">
            <div style="text-align: center">
              <n-button type="error" @click="checkNo" ghost
                >不同意上传</n-button
              >
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="bottom-bottom" style="margin: 10px">
      <el-tag
        v-for="(item, index) in dataCacheTags"
        :key="index"
        class="ml-2"
        :type="tagsType[index % 4]"
        style="margin-right: 10px"
        >{{ item }}</el-tag
      >
      <hr style="border-color: #d8d8d8" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from "vue";
import { imgBase64 } from "@/utils/common";
import { ElMessageBox } from "element-plus";
import { dateFormat } from "@/utils/common";
import axios from "axios";
import {
  Iphone,
  Location,
  OfficeBuilding,
  Tickets,
  User,
} from "@element-plus/icons-vue";
import {
  offlineMessage,
  responseMessage,
  onlineById,
  offlineById,
  offlineUserMessage,
  QueryCode,
  QueryShpByName,
  getFilePath,
  ZipEntryPath,
  QueryHeightByName,
} from "@/api/request";
import { NButton } from "naive-ui";
export default defineComponent({
  components: {
    NButton,
  },
  props: {
    fileInfo: {
      type: Object,
    },
    showDe: {
      type: Boolean,
    },
  },
  setup(props, context) {
    const fileList = ref<any[]>([]);
    const fileTemp = ref<any[]>([]);
    const fileMeta = computed(() => {
      return props.fileInfo as any;
    });
    const total = ref(0);
    const avatar = ref("");
    const n = ref(true);
    const tagsType = ref(["success", "info", "warning", "danger"]);
    const showDetail = computed(() => {
      return props.showDe as any;
    });
    const showDetails = ref(true);
    const showMessage = ref(true);

    const messageResponse = computed(() => {
      return (props.fileInfo as any).messageResponse;
    });

    const messageType = computed(() => {
      return (props.fileInfo as any).messageType;
    });
    const messageSender = computed(() => {
      return (props.fileInfo as any).messageSender;
    });
    const messageReceiver = computed(() => {
      return (props.fileInfo as any).messageReceiver;
    });
    const dataName = computed(() => {
      return (props.fileInfo as any).dataName;
    });
    const dataUploadTime = computed(() => {
      return dateFormat(
        (props.fileInfo as any).dataUploadTime,
        "yyyy年MM月dd日hh时mm分"
      );
    });
    const dataExamineTime = computed(() => {
      return dateFormat(
        (props.fileInfo as any).dataExamineTime,
        "yyyy年MM月dd日hh时mm分"
      );
    });
    const dataUploadTimeFormat = computed(() => {
      return (props.fileInfo as any).dataUploadTime;
    });
    const dataExamineTimeFormat = computed(() => {
      return (props.fileInfo as any).dataExamineTime;
    });

    const examineStatus = computed(() => {
      return (props.fileInfo as any).reply;
    });
    const fileId = computed(() => {
      return (props.fileInfo as any).fileId;
    });
    const id = computed(() => {
      return (props.fileInfo as any).id;
    });
    const dataCache = computed(() => {
      const data = JSON.parse((props.fileInfo as any).dataCache);
      return data;
    });
    const dataCacheId = computed(() => {
      return JSON.parse((props.fileInfo as any).dataCache).id;
    });
    const dataCacheTags = computed(() => {
      return JSON.parse((props.fileInfo as any).dataCache).tags;
    });

    const titlemessage = computed(() => {
      if (messageResponse.value == "success" && typeMessage.value == "上传")
        return "您的上传资源已审核通过";
      else if (messageResponse.value == "fail" && typeMessage.value == "上传")
        return "十分抱歉，上传资源未通过审核";
      else if (
        messageResponse.value == "examine" &&
        typeMessage.value == "上传"
      )
        return "请耐心等待，上传资源正在审核";
      else if (
        messageResponse.value == "success" &&
        typeMessage.value == "上线"
      )
        return "您的上线资源已审核通过";
      else if (messageResponse.value == "fail" && typeMessage.value == "上线")
        return "十分抱歉，上线资源未通过审核";
      else if (
        messageResponse.value == "examine" &&
        typeMessage.value == "上线"
      )
        return "请耐心等待，上线资源正在审核";
      else return "";
    });
    const typeMessage = computed(() => {
      if (messageType.value == "upload") return "上传";
      else if (messageType.value == "online") return "上线";
      else return "hhh";
    });
    const examineStatusCheck = computed(() => {
      if (messageResponse.value == "success") return "消息已审核为——通过";
      else if (messageResponse.value == "fail") return "消息已审核为——未通过";
      else if (messageResponse.value == "examine") return "消息正在审核";
      else return "";
    });
    const iconColor = computed(() => {
      if (messageResponse.value == "success") return "green";
      else if (messageResponse.value == "examine") return "orange";
      else if (messageResponse.value == "fail") return "red";
      else return "";
    });
    const subtitleMessage = computed(() => {
      if (messageResponse.value == "success" && typeMessage.value == "上传")
        return "恭喜您，您的资源已经上传成功，上传资源类型为";
      else if (messageResponse.value == "fail" && typeMessage.value == "上传")
        return "很遗憾，您的资源未上传成功，上传资源类型为";
      else if (
        messageResponse.value == "examine" &&
        typeMessage.value == "上传"
      )
        return "请耐心等待，您的上传资源正在审核";
      else if (
        messageResponse.value == "success" &&
        typeMessage.value == "上线"
      )
        return "恭喜您，您的资源已经上线成功，上线资源类型为";
      else if (messageResponse.value == "fail" && typeMessage.value == "上线")
        return "很遗憾，您的资源未上线成功，上线资源类型为";
      else if (
        messageResponse.value == "examine" &&
        typeMessage.value == "上线"
      )
        return "请耐心等待，您的上线资源正在审核";
      else return "";
    });
    const explainMessage = computed(() => {
      if (messageResponse.value == "success" && typeMessage.value == "上传")
        return "您的上传资源已经共享成功，可在资源门户中查看数据";
      else if (messageResponse.value == "fail" && typeMessage.value == "上传")
        return "您的上传资源未共享成功，建议重新检查数据";
      else if (
        messageResponse.value == "examine" &&
        typeMessage.value == "上传"
      )
        return "您的上传资源被审核完毕后会重新发送消息";
      else if (
        messageResponse.value == "success" &&
        typeMessage.value == "上线"
      )
        return "您的上线资源已经共享成功，可在资源门户中查看数据";
      else if (messageResponse.value == "fail" && typeMessage.value == "上线")
        return "您的上线资源未共享成功，建议重新检查数据";
      else if (
        messageResponse.value == "examine" &&
        typeMessage.value == "上线"
      )
        return "您的上线资源被审核完毕后会重新发送消息";
      else return "";
    });
    const iconType = computed(() => {
      if (messageResponse.value == "success") return "success";
      else if (messageResponse.value == "fail") return "error";
      else if (messageResponse.value == "examine") return "info";
      else return "";
    });
    const deletes = async () => {
      await deleteMessage();
    };

    const deleteMessage = async () => {
      if (props.showDe == true) {
        await (n.value = !n.value);
        await offlineMessage(fileId.value, dataUploadTimeFormat.value);
      } else {
        await (n.value = !n.value);
        await offlineUserMessage(fileId.value, dataUploadTimeFormat.value);
      }
    };
    const checkYes = async () => {
      ElMessageBox.confirm(
        "您确定要同意该用户的上线请求吗？同意上线后该数据将上线至资源门户供其他用户查看与下载",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "我再想想",
          type: "warning",
        }
      ).then(async () => {
        await responseMessage("success", id.value);
        await onlineById(dataCacheId.value);
      });
    };
    const checkNo = async () => {
      ElMessageBox.confirm(
        "您确定要拒绝该用户的上线请求吗？拒绝后该数据将保存至个人空间，但不会共享至资源门户",
        "警告",
        {
          confirmButtonText: "确定",
          cancelButtonText: "我再想想",
          type: "warning",
        }
      ).then(async () => {
        await responseMessage("fail", id.value);
        await offlineById(dataCacheId.value);
      });
    };
    onMounted(async () => {
       avatar.value = imgBase64("哈哈");
       const dataList =ref<any[]>()
      const datass=await QueryHeightByName("徐六泾站潮位观测成果表")
      dataList.value=datass.data[0].height
      console.log(dataList.value)
      // const data = await responseBinary('222');
      // const data2 =await queryByMsi(3333);
      // const TempData=data2.data.list;
      // console.log("fgztest",TempData)
      // console.log("123456", data)

      // axios.get('http://172.21.212.10:8003/ship/getShipBinary', {
      //   responseType: 'arraybuffer'
      // }).then((res) => {
      //   const dataView = new DataView(res.data)
      //   console.log("32_0",dataView.getInt32(0))
      //   console.log("32_1",dataView.getInt32(4))
      //   console.log("32_2",dataView.getInt32(8))
      //   console.log("32_3",dataView.getInt32(12))
      //   console.log("32_4",dataView.getInt32(16))
      //   console.log("32_5",dataView.getInt32(20))
      //   console.log("32_6",dataView.getInt32(24))
      //   console.log("32_7",dataView.getInt32(28))
      //   console.log("32_8",dataView.getInt32(32))
      //   console.log("32_9",dataView.getInt32(36))
      //   console.log("32_10",dataView.getInt32(40))
      //   console.log("32_11",dataView.getInt32(44))
      //   console.log("8_0",dataView.getInt8(48))
      //   console.log("8_1",dataView.getInt8(49))
      // })
      // const dataView = new DataView(data as any as ArrayBuffer)
      // console.log("123", dataView, dataView.getInt32(0))
      // const enc = new TextEncoder();
      // const intBuffer = enc.encode(data as any as string);
      // console.log("byteLength: ", intBuffer.byteLength);
      // console.log("dsada", new Int32Array((intBuffer.reverse()).buffer).reverse());


      // const d = new DataView(data as any as ArrayBuffer)
      // let intbuffer = new Int32Array(str.length);
      // for (let i = 0 ; i < str.length; ++i) {
      //   intbuffer[i] = str.charCodeAt(i);
      // }
      // console.log("hhqhqhqhqhhq", str.length);
      avatar.value = imgBase64("哈哈");
    });
    return {
      avatar,
      dataName,
      messageResponse,
      messageSender,
      messageReceiver,
      dataUploadTime,
      dataExamineTime,
      fileList,
      fileTemp,
      total,
      n,
      iconType,
      titlemessage,
      typeMessage,
      subtitleMessage,
      fileMeta,
      examineStatus,
      examineStatusCheck,
      iconColor,
      explainMessage,
      fileId,
      deleteMessage,
      showMessage,
      deletes,
      showDetails,
      dataCache,
      checkYes,
      checkNo,
      id,
      showDetail,
      dataCacheId,
      dataCacheTags,
      tagsType,
    };
  },
});
</script>

<style lang="scss" scoped>
.el-popper.is-customized {
  /* Set padding to ensure the height is 32px */
  padding: 6px 12px;
  background: linear-gradient(90deg, rgb(159, 229, 151), rgb(204, 229, 129));
}

.el-popper__arrow::before {
  background: linear-gradient(45deg, #b2e68d, #bce689);
  right: 0;
}

.data-card {
  margin-left: 0px;
  margin-right: 0px;
  border-radius: 30px;
  .top {
    display: flex;
    position: relative;
    height: 40px;
    line-height: 40px;
    .text {
      margin-left: 10px;
      font-size: 22px;
      color: #06090a;
    }
  }
}
.time {
  margin-right: 10px;
  margin-top: 10px;
  margin-bottom: 3px;
  float: left;
}

.el-descriptions {
  margin-top: 20px;
}
.cell-item {
  display: flex;
  align-items: center;
}
.margin-top {
  margin-top: 20px;
}
</style>