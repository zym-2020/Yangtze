<template>
  <el-skeleton :rows="5" animated v-if="skeleton" />
  <div class="upload-page" v-else>
    <div class="gif">
      <img src="/gif/upload.gif" alt="" />
    </div>
    <el-empty
      description="暂无记录"
      v-if="
        uploadedList.length === 0 &&
        Object.keys(uploading).length === 0 &&
        waitList.length === 0
      "
    />
    <div class="scroll" v-else>
      <el-scrollbar>
        <div class="card" v-for="(value, key) in uploading" :key="key">
          <div class="file-name" :title="value.name">{{ value.name }}</div>
          <el-progress
            :text-inside="true"
            :stroke-width="16"
            :percentage="value.progress"
          />
          <div class="file-size">{{ value.size }}</div>
          <div class="icon" @click="closeHandle('uploading', index, key)">
            <el-icon><CircleClose /></el-icon>
          </div>
        </div>

        <div class="card" v-for="(item, index) in waitList" :key="index">
          <div class="file-name" :title="item.name">{{ item.name }}</div>
          <el-progress :text-inside="true" :stroke-width="16" :percentage="0">
            <span>等待上传...</span>
          </el-progress>
          <div class="file-size">{{ item.size }}</div>
          <div class="icon" @click="closeHandle('wait', index)">
            <el-icon><CircleClose /></el-icon>
          </div>
        </div>
        <div class="card" v-for="(item, index) in uploadedList" :key="index">
          <div class="file-name" :title="item.name">
            {{ item.name }}
          </div>
          <el-progress
            :text-inside="true"
            :stroke-width="16"
            :percentage="100"
            status="success"
          />
          <div class="size-time">
            <div class="file-size">
              {{ item.size }}
            </div>
            <div class="file-time">
              {{ getDate(item.time) }}
            </div>
          </div>

          <div class="icon" @click="closeHandle('uploaded', index, item.id)">
            <el-icon><CircleClose /></el-icon>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <div></div>
  </div>
</template>

<script lang="ts">
type Record = {
  id: string;
  fileName: string;
  uploader: string;
  uploadTime: string;
  size: string;
};
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import { useStore } from "@/store";
import { dateFormat } from "@/utils/common";
export default defineComponent({
  setup() {
    const store = useStore();
    const skeleton = ref(true);

    const waitList = computed(() => {
      return store.state.other.waitList;
    });

    const uploading = computed(() => {
      return store.state.other.uploading;
    });

    const uploadedList = computed(() => {
      return store.state.other.uploadedList;
    });

    const getDate = (date: string) => {
      return dateFormat(date, "yyyy-MM-dd hh:mm");
    };

    const closeHandle = async (param: string, index: number, id: string) => {
      if (param === "wait") {
        store.commit("REMOVE_WIAT_ITEM", index);
      } else if (param === "uploaded") {
        await store.dispatch("delUploadedItem", id);
      } else {
        const temp = store.state.other.uploading[id];
        console.log(temp);
        store.commit("UPDATE_UPLOADING", {
          id: id,
          value: {
            name: temp.name,
            size: temp.size,
            state: -1,
            progress: temp.progress,
          },
        });
      }
    };

    onMounted(async () => {
      skeleton.value = true;
      await store.dispatch("initUploadedList", undefined);
      skeleton.value = false;
    });

    return {
      skeleton,
      waitList,
      uploading,
      uploadedList,
      closeHandle,
      getDate,
    };
  },
});
</script>

<style lang="scss" scoped>
.upload-page {
  height: 100%;
  .gif {
    margin: 0 auto;
    width: 300px;
    height: 300px;
    img {
      width: 100%;
      height: 100%;
    }
  }
  .scroll {
    height: calc(100% - 350px);
    .card {
      height: 100px;
      border: 1px solid #c4c4c4;
      box-shadow: 0px 2px 4px rgba(196, 196, 196, 0.5);
      border-radius: 10px;
      margin-bottom: 15px;
      position: relative;
      .file-name {
        height: 40px;
        font-family: "Inter";
        font-style: normal;
        font-weight: 400;
        font-size: 18px;
        line-height: 45px;
        margin-left: 10%;
        width: 80%;
        text-overflow: ellipsis;
        overflow: hidden;
        white-space: nowrap;
        // margin-bottom: 5px;
      }
      .el-progress {
        width: 80%;
        margin: 0 auto;
        /deep/ .el-progress-bar__innerText {
          color: black;
          line-height: 12px;
        }
      }
      .size-time {
        height: 20px;
        font-family: "Inter";
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 20px;
        margin: 5px 10% 0;
        color: #8c8c8c;
        position: relative;
        .file-size {
          position: absolute;
          left: 0;
        }
        .file-time {
          position: absolute;
          right: 0;
        }
      }

      .icon {
        position: absolute;
        top: 10px;
        right: 10px;
        color: #8c8c8c;
        cursor: pointer;
      }
    }
  }
}
</style>