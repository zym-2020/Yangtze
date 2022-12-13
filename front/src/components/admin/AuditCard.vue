<template>
  <div class="audit-card">
    <div class="image">
      <img :src="getAvatar(info.avatar, info.name)" />
    </div>
    <div class="name-creator">
      <div class="name"><span v-html="replaceHandle(info.name)"></span></div>
      <div class="creator">
        <span v-html="replaceHandle(info.userName)"></span>
      </div>
    </div>
    <div class="time">
      <div class="top">申请时间</div>
      <div class="bottom">{{ getTime(info.updateTime) }}</div>
    </div>
    <div class="btn">
      <div class="info">
        <el-button type="primary" text size="small">查看详情</el-button>
      </div>
      <div>
        <el-button type="primary" size="small" @click="operateHandle(1)"
          ><el-icon><Check /></el-icon>同意</el-button
        >
        <el-button type="danger" size="small" @click="operateHandle(-1)"
          ><el-icon><Close /></el-icon>拒绝</el-button
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import { updateStatusById } from "@/api/request";
import { notice } from "@/utils/notice";
import { dateFormat, imgBase64 } from "@/utils/common";
import { prefix } from "@/prefix";
import router from "@/router";
export default defineComponent({
  props: {
    info: {
      type: Object,
    },
    keyword: {
      type: String,
    },
  },
  emits: ["auditHandle"],
  setup(props, context) {
    const info: any = computed(() => {
      return props.info;
    });
    const operateHandle = async (param: number) => {
      const data = await updateStatusById(info.id, param);
      if (data != null && (data as any).code === 0) {
        notice("success", "成功", "审核成功");
        context.emit("auditHandle");
      }
    };

    const getAvatar = (avatar: string, name: string) => {
      if (avatar === "") {
        return imgBase64(name);
      } else {
        return prefix + "visual/getAvatar/" + avatar;
      }
    };

    const getTime = (time: string) => {
      return dateFormat(time, "yyyy-MM-dd hh:mm");
    };

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(" + props.keyword + ")", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" + props.keyword + "</span>"
      );
      return currentStr;
    };

    return {
      info,
      operateHandle,
      replaceHandle,
      getAvatar,
      getTime,
    };
  },
});
</script>


<style lang="scss" scoped>
.audit-card {
  width: calc(100% - 40px);
  padding: 20px 10px;
  display: flex;
  border-bottom: solid 1px #dcdfe6;
  height: 60px;
  position: relative;
  .image {
    height: 100%;
    width: 60px;
    img {
      height: 100%;
      width: 100%;
      object-fit: cover;
    }
  }
  .name-creator {
    margin-left: 20px;
    width: 30%;
    .name {
      height: 30px;
      line-height: 35px;
    }
    .creator {
      color: #8c8c8c;
      font-size: 14px;
    }
  }
  .time {
    color: #8c8c8c;
    font-size: 14px;
    .top {
      height: 30px;
      line-height: 35px;
    }
  }
  .btn {
    position: absolute;
    text-align: center;
    right: 20px;
    .info {
      margin-bottom: 10px;
    }
    .el-icon {
      margin-right: 5px;
    }
  }
}
</style>