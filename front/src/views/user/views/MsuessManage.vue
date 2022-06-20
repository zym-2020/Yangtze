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
        <div class="time">
          <el-icon color="green"><Select /></el-icon>
          <span>{{ examineStatusCheck }}</span>
        </div>
      </el-col>

      <el-col :span="4" :offset="4">
        <div style="float: right">
          <el-tooltip content='该条消息将被回收至【历史消息】' placement="bottom" effect="light">
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
            <!-- <el-button
              @click="showDetails = !showDetails"
              key="primary"
              type="primary"
              text="true"
              >查看数据描述</el-button
            > -->
            <!-- <n-icon>ArrowCircleDown16Regular</n-icon> -->
            <n-button
              strong
              secondary
              round
              type="primary"
              @click="showDetails = !showDetails"
              >查看数据描述
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
              <el-descriptions-item >
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconType">
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
                    <el-icon :style="iconStyle">
                      <iphone />
                    </el-icon>
                    资源下载量
                  </div>
                </template>
                {{ dataCache.download }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <location />
                    </el-icon>
                    资源浏览量
                  </div>
                </template>
                {{ dataCache.watch }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <location />
                    </el-icon>
                    数据提供者
                  </div>
                </template>
                {{ dataCache.creator }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <tickets />
                    </el-icon>
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
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    原始数据
                  </div>
                </template>
                {{ dataCache.originName }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    整合数据
                  </div>
                </template>
                {{ dataCache.structuredName }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    可视化数据
                  </div>
                </template>
                {{ dataCache.visualName }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    数据存放路径
                  </div>
                </template>
                {{ dataCache.originAddress }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源共享方式
                  </div>
                </template>
                No.1188, Wuzhong Avenue, Wuzhong District, Suzhou, Jiangsu
                Province
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源可视化来源
                  </div>
                </template>
                {{ dataCache.visualSource }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源可视化类型
                  </div>
                </template>
                {{ dataCache.visualType }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源共享方式
                  </div>
                </template>
                {{ dataCache.structuredSource }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源创造时间
                  </div>
                </template>
                {{ dataCache.createTime }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源更新时间
                  </div>
                </template>
                {{ dataCache.updateTime }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源状态
                  </div>
                </template>
                {{ dataCache.status }}
              </el-descriptions-item>
              <el-descriptions-item>
                <template #label>
                  <div class="cell-item">
                    <el-icon :style="iconStyle">
                      <office-building />
                    </el-icon>
                    资源封面
                  </div>
                </template>
                {{ dataCache.avatar }}
              </el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>
        <el-row style="margin: 10px">
          <el-col :span="1.5" :offset="10">
            <div style="text-align: center">
            <el-tooltip content='该条消息将被回收至【历史消息】' placement="bottom" effect="light">
              <el-button @click="checkYes">同意上传</el-button>
              </el-tooltip>
            </div>
          </el-col>
          <el-col :span="1.5" :offset="1">
            <div style="text-align: center">
            <el-tooltip content='该条消息将被回收至【历史消息】' placement="bottom" effect="light">
              <el-button @click="checkNo">不同意上传</el-button>
              </el-tooltip>
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
import {
  Iphone,
  Location,
  OfficeBuilding,
  Tickets,
  User,
} from "@element-plus/icons-vue";
//import  { ArrowCircleDown16Regular } from "vicons";
import {
  offlineMessage,
  responseMessage,
  onlineById,
  offlineById,
  offlineUserMessage,
} from "@/api/request";
import { NButton } from "naive-ui";
export default defineComponent({
  components: {
    NButton,
    //ArrowCircleDown16Regular
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

      //return dateFormat(date, "yyyy-MM-dd");
    });
    const dataExamineTime = computed(() => {
      return dateFormat(
        (props.fileInfo as any).dataExamineTime,
        "yyyy年MM月dd日hh时mm分"
      );
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
        await offlineMessage(fileId.value, dataUploadTime.value);
        //await function(){n.value=false}
      } else {
        await (n.value = !n.value);
        await offlineUserMessage(fileId.value, dataUploadTime.value);
        //await function(){n.value=false}
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
        console.log(dataCacheId.value);
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

    //element-ui

    onMounted(() => {
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