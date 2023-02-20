<template>
  <div class="data-list-card">
    <div class="image">
      <img :src="getAvatar(info.avatar, info.name)" />
      <el-tag
        class="update-time"
        effect="dark"
        :title="'上次更新时间：' + getTime(info.updateTime)"
        >{{ getTime(info.updateTime) }}</el-tag
      >
      <div class="creator" :title="'创建人：' + info.userName">
        <span v-html="replaceHandle(info.userName)"></span>
      </div>
      <el-tag class="special" effect="dark" type="danger" v-if="specialFlag"
        >特色数据</el-tag
      >
    </div>
    <div class="name">
      <span v-html="replaceHandle(info.name)"></span>
    </div>
    <div class="tags">
      <el-tag v-for="(item, index) in info.tags" :key="index"
        ><span v-html="replaceHandle(item)"></span
      ></el-tag>
    </div>
    <div class="des">
      <span v-html="replaceHandle(info.description)"></span>
    </div>
    <div class="btn">
      <el-icon style="margin-right: 5px; margin-top: 5px"><View /></el-icon>
      <span>{{ info.watch }}</span>
      <el-icon style="margin-right: 5px; margin-left: 10px"
        ><Download
      /></el-icon>
      <span>{{ info.download }}</span>
      <el-dropdown trigger="click" placement="top" @command="commandHandle">
        <el-button type="primary" text>操作</el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item v-if="info.status === -1" command="online"
              ><el-icon><SuccessFilled /></el-icon>上线</el-dropdown-item
            >
            <el-dropdown-item v-if="info.status === 1" command="offline"
              ><el-icon><WarningFilled /></el-icon>下线</el-dropdown-item
            >
            <el-dropdown-item v-if="!specialFlag" command="addSpecial"
              ><el-icon><StarFilled /></el-icon>标记为特色数据</el-dropdown-item
            >
            <el-dropdown-item v-if="specialFlag" command="delSpecial"
              ><el-icon><Star /></el-icon>取消特色数据</el-dropdown-item
            >
            <el-dropdown-item command="delete"
              ><el-icon><DeleteFilled /></el-icon>删除</el-dropdown-item
            >
          </el-dropdown-menu>
        </template>
      </el-dropdown>

      <el-button type="primary" text @click="clickHandle" class="check"
        >查看详情</el-button
      >
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
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
    specialList: {
      type: Array,
    },
  },
  emits: ["operateHandle"],
  setup(props, context) {
    const info: any = computed(() => {
      return props.info;
    });

    const specialFlag = computed(() => {
      if (props.specialList != undefined) {
        for (let i = 0; i < props.specialList.length; i++) {
          if (props.specialList[i] == props.info?.id) {
            return true;
          }
        }
      }
      return false;
    });

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

    const clickHandle = () => {
      router.push({
        name: "shareFile",
        params: {
          id: info.value.id,
          fileInfo: JSON.stringify(info.value),
        },
      });
    };

    const commandHandle = (val: string) => {
      context.emit("operateHandle", { type: val, id: info.value.id });
    };

    return {
      info,
      getAvatar,
      getTime,
      clickHandle,
      commandHandle,
      replaceHandle,
      specialFlag,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-list-card {
  width: calc(100% - 40px);
  height: 400px;
  border: solid 1px #e4e7ed;
  margin-bottom: 20px;
  padding: 20px;
  cursor: pointer;
  transition: all 500ms linear;
  &:hover {
    box-shadow: 0px 0px 10px rgba($color: #000000, $alpha: 0.3);
    transition: all 500ms linear;
  }
  .image {
    width: 100%;
    height: 230px;
    overflow: hidden;
    position: relative;
    img {
      width: 100%;
      height: 100%;
      transition: all 500ms linear;
      &:hover {
        transition: all 500ms linear;
        transform: scale(1.1);
      }
    }
    .update-time {
      position: absolute;
      top: 10px;
      right: 10px;
    }
    .creator {
      position: absolute;
      background: #409eff;
      padding: 0 5px;
      color: white;
      font-size: 13px;
      height: 24px;
      line-height: 24px;
      top: 40px;
      right: 10px;
      width: 80px;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
      border-radius: 4px;
      text-align: center;
      font-weight: 300;
    }
    .special {
      position: absolute;
      right: 10px;
      top: 70px;
    }
  }

  .name {
    height: 40px;
    line-height: 40px;
    font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
    font-weight: 600;
    font-size: 14px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }
  .tags {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    margin-bottom: 15px;
    height: 25px;
    .el-tag {
      border: solid 1px #1890ff;
      margin-right: 10px;
    }
  }
  .des {
    height: 45px;
    font-style: PingFang SC, Arial, Microsoft YaHei, sans-serif;
    font-size: 8px;
    color: #8c768c;
    line-height: 22px;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
  }
  .btn {
    position: relative;
    margin-top: 10px;
    color: #1890ff;
    .check {
      float: right;
      margin-right: 5px;
    }
    .el-dropdown {
      float: right;
    }
  }
}
</style>