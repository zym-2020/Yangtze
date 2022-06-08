<template>
  <div class="upload-main">
    <div class="main">
      <div class="file-list">
        <el-scrollbar>
          <el-empty description="暂无数据" v-if="uploadFileList.length === 0" />
          <div v-else>
            <div v-for="(item, index) in uploadFileList" :key="index" class="file-item">
              <div class="name">{{ item.name }}</div>
              <div class="over">
                <el-icon @click="close(index)"><CloseBold /></el-icon>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </div>
      <div class="bottom">
        <div class="btn">
          <el-upload
            action="#"
            ref="upload"
            :multiple="true"
            :auto-upload="false"
            :show-file-list="false"
            :on-change="handelChange"
          >
            <template #trigger>
              <el-button type="warning" size="small">选择文件</el-button>
            </template>
          </el-upload>
          <el-button
            type="primary"
            size="small"
            @click="commit"
            style="margin-left: 10px"
            >确定</el-button
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { UploadFile } from "element-plus";
import { uuid } from "@/utils/common";

import { useStore } from "@/store";

export default defineComponent({
  props: {
    level: {
      type: Number,
    },
    parentId: {
      type: String,
    },
  },
  emits: ["commitFile"],
  setup(props, context) {
    const store = useStore();
    const uploadFileList = ref<UploadFile[]>([]);

    const handelChange = (file: UploadFile) => {
      if (file.status === "ready") {
        uploadFileList.value.push(file);
      }
    };

    const close = (index: number) => {
      uploadFileList.value.splice(index, 1)
    }

    const commit = () => {
      uploadFileList.value.forEach((item) => {
        store.commit("ADD_WAIT_ITEM", {
          id: uuid(),
          name: item.name,
          state: -2,
          file: item.raw as File,
        });
      });

      if (!store.state.other.uploadFlag) {
        store.dispatch("uploadFiles", {
          level: props.level as number,
          parentId: props.parentId as string,
        });
      }
      context.emit("commitFile");
      store.commit("SET_UPLOAD_DOT_FLAG", true);
    };

    return {
      commit,
      handelChange,
      uploadFileList,
      close
    };
  },
});
</script>

<style lang="scss" scoped>
.upload-main {
  width: 100%;
  height: 400px;
  padding: 20px 10px 10px 10px;
  background: #a6bed7;
  .main {
    height: 390px;
    background: #f0f0f0;
    padding-top: 10px;
    .file-list {
      height: 350px;
      margin: 0 20px;
      background: white;
      border: 1px solid;
      .file-item {
        height: 25px;
        line-height: 25px;
        position: relative;
        .name {
          margin: 0 10px;
        }
        .over {
          position: absolute;
          opacity: 0;
          top: 0;
          height: 25px;
          width: 100%;
          background: #7F7F7F;
          cursor: pointer;
          .el-icon {
            float: right;
            font-size: 17px;
            margin-top: 4px;
            margin-right: 4px;
            color: black;
          }
          &:hover {
            opacity: 0.2;
          }
          
        }
      }
    }
    .bottom {
      margin-top: 5px;
      display: flex;
      justify-content: space-around;
      .btn {
        display: flex;
        justify-content: space-around;
      }
    }
  }
}
</style>