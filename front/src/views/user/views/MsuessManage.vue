<template>
  <div class="data-card" v-show="n == 1">
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
          <div v-show="messageType == 'examine'">
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
          <span>{{ fileTemp }}</span>
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
      <el-col :span="5">
        <div class="time">
          <strong>管理员审核时间：</strong>
          <span>{{ dataExamineTime }}</span>
        </div>
      </el-col>
      <el-col :span="2">
        <div class="time">
          <el-icon color="green"><Select /></el-icon>
          <span>{{ examineStatusCheck }}</span>
        </div>
      </el-col>

      <el-col :span="4" :offset="8">
        <div class="time" style="float: right">
          <el-button type="primary" @click="createMessage">
            已知晓通知</el-button
          >
        </div>
      </el-col>
    </el-row>

    <div class="bottom-bottom" style="margin: 10px">
      <el-tag class="ml-2" type="success" style="margin-right: 10px"
        >水文</el-tag
      >
      <el-tag class="ml-2" type="info" style="margin-right: 10px">地质</el-tag>
      <el-tag class="ml-2" type="warning" style="margin-right: 10px"
        >工程</el-tag
      >
      <el-tag class="ml-2" type="danger" style="margin-right: 10px"
        >气象</el-tag
      >
      <hr style="border-color: #d8d8d8" />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from "vue";
import { imgBase64 } from "@/utils/common";
import { pageQuerys, addMessage, QueryByTime } from "@/api/request";
import { dateFormat, uuid } from "@/utils/common";
export default defineComponent({
  props: {
    fileInfo: {
      type: Object,
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
    const n = ref(1);

    const messageType = computed(() => {
      return (props.fileInfo as any).messageResponse;
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
      return (props.fileInfo as any).dataUploadTime;
    });
    const dataExamineTime = computed(() => {
      return (props.fileInfo as any).dataExamineTime;
    });
    const examineStatus = computed(() => {
      return (props.fileInfo as any).reply;
    });
    const fileId = computed(() => {
      return (props.fileInfo as any).fileId;
    });
    const titlemessage = computed(() => {
      if (messageType.value == "success") return "恭喜您，上传资源已审核通过";
      else if (messageType.value == "fail")
        return "十分抱歉，上传资源未通过审核";
      else if (messageType.value == "examine")
        return "请耐心等待，上传资源正在审核";
      else return "hhh";
    });
    const examineStatusCheck = computed(() => {
      if (messageType.value == "success") return "已审核";
      else if (messageType.value == "fail") return "未审核";
      else if (messageType.value == "examine") return "正在审核";
      else return "hhh";
    });
    const subtitleMessage = computed(() => {
      if (messageType.value == "success")
        return "您的资源已经上传成功，上传资源类型为地质类型";
      else if (messageType.value == "fail")
        return "您的资源未上传成功，上传资源类型为水文类型";
      else if (messageType.value == "examine") return "您的资源正在审核";
      else return "hhh";
    });
    const explainMessage = computed(() => {
      if (messageType.value == "success")
        return "您的资源已经上传成功，可在资源门户中查看数据";
      else if (messageType.value == "fail")
        return "您的资源未上传成功，建议重新审核数据";
      else if (messageType.value == "examine")
        return "审核完毕后会重新发送消息";
      else return "hhh";
    });
    const iconType = computed(() => {
      if (messageType.value == "success") return "success";
      else if (messageType.value == "fail") return "error";
      else if (messageType.value == "examine") return "info";
      else return "hhh";
    });
    const createMessage = async () => {
      const uid = uuid();
      const data = await addMessage({
        id: "84f7cc67-fbce-4837-9204-12929a1fdac5",
        dataName: "1234",
        dataUploadTime: "",
        dataExamineTime: "2012-02-25",
        dataCache: "hdfg",
        messageRequest: "fff",
        reply: true,
        fileId: "84f7cc67-fbce-4837-9204-12929a1fdac5",
        messageSender: "admin",
        messageReceiver: "ddd",
        messageResponse: "examine",
        messageType: "upload",
        
      });
      const tempData = await QueryByTime(fileId.value);
      if (tempData != null) {
        if ((tempData as any).code === 0) {
          fileTemp.value = tempData.data.list;
        }
      }
    };
    //let property=
    onMounted(() => {
      avatar.value = imgBase64("哈哈");
    });

    return {
      avatar,
      dataName,
      messageType,
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
      subtitleMessage,
      fileMeta,
      examineStatus,
      examineStatusCheck,
      explainMessage,
      fileId,
      createMessage,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-card {
  margin-left: 0px;
  //background-image: url("E:/水科院/Yangtze/front/src/assets/Video_2022-05-26_170841.mp4") 0 0/ 100% 100% no-repeat ;
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
</style>