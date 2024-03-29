<template>
  <div class="project-card">
    <div class="image">
      <el-avatar shape="square" fit="cover" v-if="propsInfo.avatar === ''">{{
        propsInfo.projectName
      }}</el-avatar>

      <img :src="prefix + 'visual/getAvatar/' + propsInfo.avatar" v-else />
    </div>
    <div class="text">
      <div class="name">
        <span v-html="replaceHandle(propsInfo.projectName)"></span>
      </div>
      <div class="creator">
        <el-icon><User /></el-icon>
        <span v-html="replaceHandle(propsInfo.userName)"></span>
      </div>
      <div class="time">{{ getTime(propsInfo.createTime) }}</div>
    </div>
    <div class="operate">
      <el-button
        type="primary"
        text
        style="margin-right: 5px"
        @click="clickHandle"
        >查看详情</el-button
      >
      <el-dropdown trigger="click" placement="top">
        <el-button type="primary" text>操作</el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item
              @click="commandHandle({ type: 'update', index: index })"
              ><el-icon><Edit /></el-icon>修改项目信息</el-dropdown-item
            >
            <el-dropdown-item
              @click="commandHandle({ type: 'copy', index: index })"
              ><el-icon><CopyDocument /></el-icon>拷贝项目</el-dropdown-item
            >
            <el-dropdown-item
              @click="commandHandle({ type: 'delete', index: index })"
              ><el-icon><DeleteFilled /></el-icon>删除项目</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import { dateFormat } from "@/utils/common";
import { prefix } from "@/prefix";
import { ElMessageBox } from "element-plus";
import { deleteProject } from "@/api/request";
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
  emits: ["delete", "copyAndUpdate"],
  setup(props, context) {
    const propsInfo = computed(() => {
      return props.info;
    });

    const getTime = (time: string) => {
      return dateFormat(time, "yyyy-MM-dd hh:mm");
    };

    const commandHandle = (val: { type: string; index: number }) => {
      if (val.type === "update") {
        let temp = { ...props.info };
        context.emit("copyAndUpdate", {
          title: "修改项目",
          projectInfo: undefined,
          info: temp,
        });
      } else if (val.type === "copy") {
        let temp = { ...props.info };
        context.emit("copyAndUpdate", {
          title: "拷贝项目",
          projectInfo: temp,
          info: undefined,
        });
      } else if (val.type === "delete") {
        ElMessageBox.confirm("确定删除该项目吗?该操作执行后无法撤销", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const res = await deleteProject(propsInfo.value?.id);
            if (res != null && (res as any).code === 0) {
              context.emit("delete");
            }
          })
          .catch(() => {});
      }
    };

    const clickHandle = () => {
      router.push({
        name: "project",
        params: {
          id: propsInfo.value?.id,
        },
      });
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
      getTime,

      propsInfo,
      prefix,
      commandHandle,

      clickHandle,
      replaceHandle,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-card {
  border: solid 1px #e4e7ed;
  height: 320px;
  margin-bottom: 20px;
  background: white;
  transition: all 500ms linear;
  cursor: pointer;
  position: relative;
  &:hover {
    box-shadow: 0px 0px 20px rgba($color: #000000, $alpha: 0.3);
    transition: all 500ms linear;
  }
  .image {
    height: 220px;
    width: 100%;
    overflow: hidden;
    .el-avatar {
      height: 100%;
      width: 100%;
      transition: all 500ms linear;
      &:hover {
        transition: all 500ms linear;
        transform: scale(1.1);
      }
    }

    img {
      height: 100%;
      width: 100%;
      transition: all 500ms linear;
      object-fit: cover;
      &:hover {
        transition: all 500ms linear;
        transform: scale(1.1);
      }
    }
  }

  .text {
    padding: 18px 20px 0;
    .name {
      height: 30px;
      line-height: 30px;
      font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
      font-weight: 600;
      font-size: 16px;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .creator {
      color: #1890ff;
      font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
      height: 25px;
      line-height: 25px;
      font-size: 14px;
      span {
        margin-left: 10px;
      }
    }
    .time {
      height: 20px;
      font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
      line-height: 20px;
      color: #979ca8;
      font-size: 12px;
    }
  }

  .operate {
    position: absolute;
    right: 20px;
    bottom: 20px;
  }
}
</style>