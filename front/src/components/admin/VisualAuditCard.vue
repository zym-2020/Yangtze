<template>
  <div class="visual-audit-card">
    <div class="name-creator">
      <div class="name">
        <span v-html="replaceHandle(info.fileName)"></span>
      </div>
      <div class="creator">
        <span v-html="replaceHandle(info.name)"></span>
      </div>
    </div>
    <div class="time">
      <div class="top">申请时间</div>
      <div class="bottom">{{ jsonInfo.time }}</div>
    </div>

    <div class="btn">
      <div class="info">
        <el-button type="primary" text size="small" @click="viewCompare"
          >查看详情</el-button
        >
        <el-button type="primary" text size="small" @click="downloadClick"
          >下载文件</el-button
        >
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

    <el-dialog v-model="visualCompareFlag" width="900px">
      <visual-compare
        v-if="visualCompareFlag"
        :compareInfo="compareInfo"
      ></visual-compare>
    </el-dialog>
  </div>
</template>


<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import { changeFileVisualState, getDownloadURL } from "@/api/request";
import VisualCompare from "@/views/user/components/VisualCompare.vue";
import { prefix } from '@/prefix'
import { useStore } from '@/store'
import { decrypt } from '@/utils/auth'
export default defineComponent({
  props: {
    info: {
      type: Object,
    },
    keyword: {
      type: String,
    },
  },
  components: { VisualCompare },
  emits: ["auditHandleVisual"],
  setup(props, context) {
    const store = useStore()
    const visualCompareFlag = ref(false);
    const compareInfo = ref<{
      oldVisualId: string;
      oldVisualType: string;
      visualType: string;
      visualId: string;
      time: string;
      fileName: string;
    }>();
    const info = computed(() => {
      return props.info;
    });

    const jsonInfo = computed(() => {
      return JSON.parse(props.info!.visualId);
    });

    const replaceHandle = (currentStr: string) => {
      const res = new RegExp("(" + props.keyword + ")", "g");
      currentStr = currentStr.replace(
        res,
        "<span style='color:red;'>" + props.keyword + "</span>"
      );
      return currentStr;
    };

    const operateHandle = async (param: number) => {
      if (param === 1 || param === -1) {
        await changeFileVisualState(info.value!.id, param);
        context.emit("auditHandleVisual");
      }
    };

    const viewCompare = () => {
      const json = JSON.parse(info.value!.visualId);
      json["fileName"] = info.value!.fileName;
      compareInfo.value = json;
      visualCompareFlag.value = true;
    };

    const downloadClick = async () => {
      const key = await getDownloadURL(info.value!.id);
      if (key != null && (key as any).code === 0) {
        const token = decrypt(key.data, store.state.user.id);
        window.location.href = `${prefix}file/downloadLocalFile/${store.state.user.id}/${token}`;
      }
    };

    return {
      info,
      jsonInfo,
      replaceHandle,
      operateHandle,
      viewCompare,
      visualCompareFlag,
      compareInfo,
      downloadClick
    };
  },
});
</script>

<style lang="scss" scoped>
.visual-audit-card {
  width: calc(100% - 40px);
  padding: 20px 10px;
  height: 60px;
  border-bottom: solid 1px #dcdfe6;
  display: flex;
  position: relative;
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

  /deep/.el-dialog {
    .el-dialog__header {
      padding: 0;
    }
    .el-dialog__body {
      padding: 0;
    }
  }
}
</style>