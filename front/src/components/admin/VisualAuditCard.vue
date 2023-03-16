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
import { changeFileVisualState } from "@/api/request";
export default defineComponent({
  props: {
    info: {
      type: Object,
    },
    keyword: {
      type: String,
    },
  },
  emits: ["auditHandleVisual"],
  setup(props, context) {
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

    return {
      info,
      jsonInfo,
      replaceHandle,
      operateHandle,
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
}
</style>