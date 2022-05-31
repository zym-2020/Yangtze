<template>
  <div class="upload-main">
    <div class="main">
      <el-row :gutter="10">
        <el-col :span="9">
          <div class="left">
            <div class="left-list">
              数据类型：
              <el-select
                v-model="typeValue"
                placeholder="请选择类型"
                size="small"
              >
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </div>
            <div class="left-list">
              是否解析压缩包？
              <el-select
                v-model="boolValue"
                placeholder="是否解析压缩包?"
                size="small"
              >
                <el-option label="是" value="true" />
                <el-option label="否" value="false" />
              </el-select>
            </div>
            <div class="left-list">
              <el-upload
                action="#"
                ref="upload"
                :limit="1"
                :auto-upload="false"
                :on-exceed="handleExceed"
                :on-change="handelChange"
                :on-remove="handelRemove"
              >
                <template #trigger>
                  <el-button type="primary" size="small">select file</el-button>
                </template>
              </el-upload>
            </div>
            <div class="left-list">
              元数据描述:
              <el-input
                v-model="textarea"
                :rows="6"
                type="textarea"
                resize="none"
                placeholder="请输入元数据"
                style="margin-top: 5px"
              />
            </div>
          </div>
        </el-col>
        <el-col :span="15">
          <div class="right">
            <el-scrollbar height="340px">
              <el-empty description="暂无数据" v-if="treeData.length <= 0" />
              <el-tree :data="treeData" :props="defaultProps" v-else>
                <template #default="{ node, data }">
                  <span class="custom-tree-node">
                    <svg style="width: 17px; height: 17px">
                      <use
                        :xlink:href="
                          data.type === 'folder'
                            ? '#icon-wenjianjia'
                            : '#icon-wenjian'
                        "
                      ></use>
                    </svg>
                    {{ node.label }}
                  </span>
                </template>
              </el-tree>
            </el-scrollbar>
          </div>
        </el-col>
      </el-row>
      <div class="btn">
        <el-button
          type="warning"
          size="small"
          @click="preview"
          :disabled="boolValue === 'true' ? false : true"
          >预览</el-button
        >
        <el-button type="primary" size="small" @click="commit">确定</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { genFileId, UploadFile } from "element-plus";
import type { UploadInstance, UploadRawFile } from "element-plus";
import { notice } from "@/utils/notice";
import JSZip from "jszip";
import { getFileSize } from '@/utils/common'
import {
  createFileChunk,
  getFileMd5,
  handlePostFiles,
  checkStatus,
} from "@/utils/file";
import { getNoUpload, mergeFile } from "@/api/request";

interface Tree {
  label: string;
  children: Tree[];
  type: string;
}
export default defineComponent({
  props: {
    level: {
      type: Number,
    },
    parentId: {
      type: String,
    },
  },
  setup(props) {
    const options = ref([
      { label: "地形", value: "地形" },
      { label: "水文", value: "水文" },
      { label: "潮汐", value: "潮汐" },
    ]);
    const typeValue = ref("");
    const boolValue = ref("");
    const boolZipClick = ref(false);
    const textarea = ref("");
    const upload = ref<UploadInstance>();
    const uploadFile = ref<UploadFile[]>([]);
    const treeData = ref<Tree[]>([]);
    const defaultProps = {
      children: "children",
      label: "label",
    };

    const handleExceed = (files: File[]) => {
      upload.value?.clearFiles();
      const file = files[0] as UploadRawFile;
      file.uid = genFileId();
      upload.value?.handleStart(file);
    };

    const handelChange = (file: UploadFile) => {
      if (file.status === "ready") {
        uploadFile.value[0] = file;
      }
    };

    const handelRemove = () => {
      uploadFile.value = [];
    };

    const preview = () => {
      treeData.value = [];
      if (uploadFile.value.length > 0) {
        if (!boolZipClick.value) {
          const zip = new JSZip();
          const nameArr = uploadFile.value[0]?.name.split(".");
          const suffix = nameArr[nameArr.length - 1];
          if (suffix === "zip") {
            console.log(uploadFile.value[0]?.raw);
            if (
              (uploadFile.value[0]?.raw?.size as number) <
              1024 * 1024 * 1024 * 2
            ) {
              boolZipClick.value = true;
              zip
                .loadAsync(uploadFile.value[0]?.raw as any)
                .then((res) => {
                  console.log(2);
                  classify(res);
                })
                .then(() => {
                  console.log(1);
                  boolZipClick.value = false;
                });
            } else {
              notice("warning", "警告", "仅支持小于2G的数据浏览");
            }
          } else {
            notice("error", "错误", "请上传zip数据");
          }
        } else {
          notice("warning", "警告", "计算中，请勿重复点击");
        }
      } else {
        notice("warning", "警告", "请先加载数据");
      }
    };

    const classify = (zip: JSZip) => {
      zip.forEach((item) => {
        const temp = item.split("/");
        classifyArr(treeData.value, temp);
      });
    };

    const classifyArr = (tree: Tree[], arr: string[]) => {
      for (let i = 0; i < arr.length; i++) {
        if (arr[i] != "") {
          let flag = true;
          for (let j = 0; j < tree.length; j++) {
            if (tree[j].label === arr[i]) {
              tree = tree[j].children;
              flag = false;
              break;
            }
          }
          if (flag) {
            let type = "file";
            if (i < arr.length - 1) {
              type = "folder";
            }
            tree.push({ label: arr[i], children: [], type: type });
            tree = tree[tree.length - 1].children;
          }
        }
      }
    };

    const commit = () => {
      if (typeValue.value === "") {
        notice("warning", "警告", "请选择数据类型");
      } else if (boolValue.value === "") {
        notice("warning", "警告", "请选择是否解析压缩包？");
      } else if (uploadFile.value.length === 0) {
        notice("warning", "警告", "请加载上传数据");
      } else {
        getFileMd5(uploadFile.value[0].raw as File, async (md5: string) => {
          const fileChunk = createFileChunk(uploadFile.value[0].raw as File);
          
          const chunkList = await getNoUpload({
            MD5: md5,
            total: fileChunk.length,
            meta: {
              name: uploadFile.value[0].name,
              total: fileChunk.length,
              level: props.level as number,
              parentId: props.parentId as string,
              meta: "",
              size: getFileSize((uploadFile.value[0].size) as number)
            },
          });
          if (chunkList != null && (chunkList as any).code === 0) {
            await handlePostFiles(chunkList.data, fileChunk, md5);
            if (chunkList.data.length === 0) {
              const key = await mergeFile(md5);
              if (key != null && (key as any).code === 0) {
                checkStatus(key.data);
              } else {
                notice("error", "失败", "文件合并时出错，请重新上传");
              }
            }
          } else {
            notice(
              "warning",
              "警告",
              "上传初始化失败，请检查文件及相关描述是否出错"
            );
          }
        });
      }
    };

    return {
      options,
      typeValue,
      boolValue,
      textarea,
      commit,
      handleExceed,
      handelRemove,
      upload,
      handelChange,
      preview,
      treeData,
      defaultProps,
      boolZipClick,
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
    height: 100%;
    width: 100%;
    background: #f0f0f0;
    .left {
      margin-left: 10px;
      margin-top: 10px;
      height: 340px;
      .left-list {
        margin-bottom: 10px;
        .el-select {
          margin-top: 5px;
          width: 100%;
        }
      }
    }
    .right {
      margin-right: 10px;
      background: white;
      margin-top: 10px;
      border: solid 1px;
      height: 340px;
      .custom-tree-node {
        height: 20px;
      }
    }
    .btn {
      text-align: center;
      margin-top: 10px;
    }
  }
}
</style>