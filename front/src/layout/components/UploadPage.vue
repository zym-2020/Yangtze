<template>
  <div class="upload-page">
    <div class="head">
      <p class="title">上传列表</p>
    </div>
    <div class="main">
      <el-scrollbar height="calc(100vh)">
        <div v-if="skeletonFlag">
          <el-skeleton :animated="true" />
          <br />
          <el-skeleton :animated="true" />
          <br />
          <el-skeleton :animated="true" />
          <br />
          <el-skeleton :animated="true" />
        </div>

        <div v-else>
          <div
            v-if="
              uploadList.length > 0 ||
              uploadedList.length > 0 ||
              waitList.length > 0
            "
          >
            <div v-for="(item, index) in uploadList" :key="index" class="data">
              <el-tooltip :content="item.name" placement="left" effect="light">
                <div class="cover"></div>
              </el-tooltip>
              <div class="content">
                <div class="icon">
                  <svg
                    style="
                      width: 30px;
                      height: 30px;
                      margin-left: 10px;
                      margin-top: 10px;
                    "
                  >
                    <use :xlink:href="getIcon(item.name)"></use>
                  </svg>
                </div>
                <div class="progress">
                  <div class="name">{{ item.name }}</div>

                  <el-progress
                    :percentage="getPercentage(item)"
                    :status="getStatus(item)"
                    :indeterminate="item.state === 0 ? true : false"
                  >
                    <template #default="{ percentage }">
                      <span class="percentage-value">{{
                        getText(percentage, item.state)
                      }}</span>
                    </template>
                  </el-progress>
                </div>
                <div class="operate">
                  <el-icon v-if="item.state != 0"><VideoPause /></el-icon>
                </div>
              </div>
            </div>

            <div v-for="(item, index) in waitList" :key="index" class="data">
              <el-tooltip :content="item.name" placement="left" effect="light">
                <div class="cover"></div>
              </el-tooltip>
              <div class="content">
                <div class="icon">
                  <svg
                    style="
                      width: 30px;
                      height: 30px;
                      margin-left: 10px;
                      margin-top: 10px;
                    "
                  >
                    <use :xlink:href="getIcon(item.name)"></use>
                  </svg>
                </div>
                <div class="progress">
                  <div class="name">{{ item.name }}</div>
                  <el-progress
                    :percentage="getPercentage(item)"
                    :status="getStatus(item)"
                    :indeterminate="item.state === 0 ? true : false"
                  >
                    <template #default="{ percentage }">
                      <span class="percentage-value">{{
                        getText(percentage, item.state)
                      }}</span>
                    </template>
                  </el-progress>
                </div>
                <div class="operate">
                  <el-icon @click="deleteRecord(index)"
                    ><CircleClose
                  /></el-icon>
                </div>
              </div>
            </div>

            <div
              v-for="(item, index) in uploadedList"
              :key="index"
              class="data"
            >
              <el-tooltip :content="item.name" placement="left" effect="light">
                <div class="cover"></div>
              </el-tooltip>

              <div class="content">
                <div class="icon">
                  <svg
                    style="
                      width: 30px;
                      height: 30px;
                      margin-left: 10px;
                      margin-top: 10px;
                    "
                  >
                    <use :xlink:href="getIcon(item.name)"></use>
                  </svg>
                </div>
                <div class="progress">
                  <div class="name">{{ item.name }}</div>
                  <el-progress
                    :percentage="getPercentage(item)"
                    :status="getStatus(item)"
                    :indeterminate="item.state === 0 ? true : false"
                  >
                    <template #default="{ percentage }">
                      <span class="percentage-value">{{
                        getText(percentage, item.state)
                      }}</span>
                    </template>
                  </el-progress>
                </div>
                <div class="operate">
                  <el-icon @click="deleteRecord(index)"
                    ><CircleClose
                  /></el-icon>
                </div>
              </div>
            </div>
          </div>
          <div v-else>
            <el-empty description="暂无记录" />
          </div>
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import { getRecords } from "@/api/request";
import { useStore } from "@/store";

export default defineComponent({
  setup() {
    const skeletonFlag = ref(false);
    const dataList = ref<any[]>([]);
    const store = useStore();

    const uploadedList = computed(() => {
      return store.state.other.uploadedList;
    });
    const waitList = computed(() => {
      return store.state.other.waitList;
    });
    const uploadList = computed(() => {
      return store.state.other.uploadList;
    });

    const getIcon = (fileName: string) => {
      const fileExtName = fileName.substring(
        fileName.lastIndexOf("."),
        fileName.length
      );
      if (
        fileExtName === ".zip" ||
        fileExtName === ".7z" ||
        fileExtName === ".tar" ||
        fileExtName === ".rar"
      ) {
        return "#icon-zip";
      } else {
        return "#icon-wenjian";
      }
    };
    const getPercentage = (item: any) => {
      if (item.state === 1) {
        return 100;
      } else if (item.state === -1) {
        return 33;
      } else if (item.state === 0) {
        return 33;
      } else if (item.state === 2) {
        return item.progress;
      } else if (item.state === -2) {
        return 0;
      }
    };
    const getStatus = (item: any) => {
      if (item.state === 1) {
        return "success";
      } else if (item === -1) {
        return "exception";
      } else {
        return "";
      }
    };
    const getText = (percentage: number, state: number) => {
      if (state === 0) {
        return "loading";
      } else if (state === -2) {
        return "waiting";
      } else {
        return percentage + "%";
      }
    };

    const test = () => {
      console.log(uploadList);
      console.log(uploadedList);
      console.log(waitList);
    };

    const deleteRecord = async (index: number) => {
      await store.dispatch("delUploadedItem", index);
    };

    onMounted(async () => {
      skeletonFlag.value = true;
      store.commit("SET_UPLOADED_LIST", []);
      const data = await getRecords(-6);
      if (data != null) {
        if ((data as any).code === 0) {
          for (let i = data.data.length - 1; i >= 0; i--) {
            store.commit("ADD_UPLOADED_ITEM", {
              id: data.data[i].id,
              name: data.data[i].file_name,
              state: data.data[i].state,
            });
          }
        }
      }

      skeletonFlag.value = false;
    });

    return {
      dataList,
      skeletonFlag,
      getIcon,
      getPercentage,
      getStatus,
      getText,
      deleteRecord,
      uploadedList,
      uploadList,
      waitList,
      test,
    };
  },
});
</script>

<style lang="scss" scoped>
.upload-page {
  width: 300px;
  height: 100%;
  background: #4a4a4a;
  .head {
    height: 30px;
    border-bottom: 1px solid rgba($color: #ebeef5, $alpha: 0.5);
    box-sizing: border-box;
    .title {
      line-height: 30px;
      margin-left: 20px;
      margin-top: 0px;
      margin-bottom: 0px;
      color: white;
    }
  }
  .main {
    height: calc(100vh - 50px);
    .el-scrollbar {
      height: calc(100vh - 50px);
      .data {
        position: relative;
        height: 50px;
        cursor: pointer;
        &:hover {
          .cover {
            opacity: 0.2;
          }
        }
        .content {
          margin-top: 10px;
          height: 50px;
          margin: 0px 10px;
          display: flex;
          color: white;
          /deep/ .el-progress__text {
            color: white;
          }
          .icon {
            width: 50px;
          }
          .progress {
            margin-top: 10px;
            font-size: 14px;
            width: 180px;
            .name {
              width: 120px;
              overflow: hidden;
              white-space: nowrap;
              text-overflow: ellipsis;
              -o-text-overflow: ellipsis;
            }
          }
          .operate {
            .el-icon {
              margin-top: 27px;
            }
          }
        }
        .cover {
          position: absolute;
          height: 50px;
          width: 100%;
          opacity: 0;
          background: white;
          top: 0px;
        }
      }
    }
  }
}
</style>