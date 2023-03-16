<template>
  <div class="visual-data-bind" v-loading="loading">
    <div class="visual-head">绑定可视化数据</div>
    <div class="visual-body">
      <div class="visual-type">
        <div><strong>可视化类型：</strong></div>
        <el-select
          v-model="typeValue"
          placeholder="选择可视化类型"
          @change="selectChangeHandle"
        >
          <el-option-group
            v-for="(group, index) in options"
            :key="index"
            :label="group.label"
          >
            <el-option
              v-for="item in group.options"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-option-group>
        </el-select>
      </div>

      <div class="view">
        <div class="flex-item">
          <div><strong>视图缩放层级：</strong></div>
          <el-select v-model="selectValue" :disabled="viewDisabledFlag">
            <el-option
              v-for="item in zoomOptions"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
        </div>
        <div class="flex-item">
          <div><strong>视图经度：</strong></div>
          <el-input-number
            v-model="longValue"
            :max="180"
            :min="-180"
            :disabled="viewDisabledFlag"
          />
        </div>
        <div class="flex-item">
          <div><strong>视图纬度：</strong></div>
          <el-input-number
            v-model="latValue"
            :max="90"
            :min="-90"
            :disabled="viewDisabledFlag"
          />
        </div>
      </div>

      <div class="srid">
        <div><strong>矢量SRID：</strong></div>
        <el-input
          v-model="sridInput"
          :disabled="sridDisabledFlag"
          placeholder="请输入srid值"
        />
      </div>

      <div class="coordinate">
        <div><strong>png坐标位置：</strong></div>
        <div class="top">
          <div class="left">
            <span>左：</span>
            <el-input v-model="leftInput" :disabled="coordinateDisabledFlag" />
          </div>
          <div class="right">
            <span>右：</span>
            <el-input v-model="rightInput" :disabled="coordinateDisabledFlag" />
          </div>
        </div>
        <div class="bottom">
          <div class="left">
            <span>上：</span>
            <el-input v-model="topInput" :disabled="coordinateDisabledFlag" />
          </div>
          <div class="right">
            <span>下：</span>
            <el-input
              v-model="bottomInput"
              :disabled="coordinateDisabledFlag"
            />
          </div>
        </div>
      </div>

      <div class="upload">
        <div class="upload-title"><strong>可视化衍生数据上传：</strong></div>
        <el-upload
          ref="upload"
          action="#"
          :auto-upload="false"
          :on-change="fileChangeHandle"
          :on-remove="fileRemoveHandle"
        >
          <template #trigger>
            <el-button
              type="primary"
              style="margin-right: 10px"
              :disabled="uploadDisabledFlag"
              >选择文件</el-button
            >
          </template>

          <el-button
            type="success"
            @click="uploadHandle"
            :disabled="uploadDisabledFlag"
          >
            上传文件
          </el-button>
        </el-upload>
        <el-progress
          :percentage="percentage"
          :status="status"
          v-if="progressFlag"
        />
      </div>

      <div class="btn">
        <el-button type="primary" @click="confirmClick">确定</el-button>
        <el-button
          type="info"
          @click="cancelClick"
          :disabled="fileInfo.visualType === ''"
          >取消当前绑定</el-button
        >
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { notice } from "@/utils/notice";
import { createFileChunk } from "@/utils/file";
import { uuid } from "@/utils/common";
import {
  uploadParts,
  mergeParts,
  bindVisualData,
  cancelVisualBind,
} from "@/api/request";
import { computed, defineComponent, ref } from "vue";
export default defineComponent({
  props: {
    fileInfo: {
      type: Object,
    },
  },
  emits: ["updateVisualFile"],
  setup(props, context) {
    const upload = ref<any>();

    const selectValue = ref(8);
    const zoomOptions = [
      1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
    ];
    const longValue = ref<number | "">(121);
    const latValue = ref<number | "">(32);
    const options = [
      {
        label: "基础地理数据",
        options: [
          {
            label: "pointVectorTile（矢量点数据）",
            value: "pointVectorTile",
          },
          {
            label: "lineVectorTile（矢量线数据）",
            value: "lineVectorTile",
          },
          {
            label: "polygonVectorTile（矢量面数据）",
            value: "polygonVectorTile",
          },
          {
            label: "pointVectorTile3D（带有ZM属性的矢量点数据）",
            value: "pointVectorTile3D",
          },
          {
            label: "lineVectorTile3D（带有ZM属性的矢量线数据）",
            value: "lineVectorTile3D",
          },
          {
            label: "polygonVectorTile3D（带有ZM属性的矢量面数据）",
            value: "polygonVectorTile3D",
          },
          {
            label: "png（针对栅格数据，将栅格数据以png的形式加载到地图上）",
            value: "png",
          },
          {
            label: "movePng（同png，一般呈多组可进行演变）",
            value: "movePng",
          },
          {
            label: "rasterTile（栅格切片类型）",
            value: "rasterTile",
          },
        ],
      },
      {
        label: "数模数据",
        options: [
          {
            label: "rateDirection（流速流向）",
            value: "rateDirection",
          },
          {
            label: "sandContent（含沙量）",
            value: "sandContent",
          },
          {
            label: "salinity（含盐度）",
            value: "salinity",
          },
          {
            label: "suspension（悬移质颗分）",
            value: "suspension",
          },
          {
            label: "flowSand_Z（流量输沙率）",
            value: "flowSand_Z",
          },
          {
            label: "tide（潮位）",
            value: "tide",
          },
        ],
      },
      {
        label: "物理模型",
        options: [
          {
            label: "photo（图片类型）",
            value: "photo",
          },
        ],
      },
    ];
    const typeValue = ref("");
    const sridInput = ref("");
    const leftInput = ref("");
    const rightInput = ref("");
    const topInput = ref("");
    const bottomInput = ref("");

    const viewDisabledFlag = ref(true);
    const uploadDisabledFlag = ref(true);
    const coordinateDisabledFlag = ref(true);
    const sridDisabledFlag = ref(true);

    const status = ref("");
    const progressFlag = ref(false);
    const percentage = ref(0);

    const loading = ref(false);

    let uploadFile: any = undefined;
    let uploadedFileName: string = "";
    let uploadFlag: boolean = true;

    const fileInfo = computed(() => {
      return props.fileInfo;
    });

    const init = () => {
      sridInput.value = "";
      leftInput.value = "";
      rightInput.value = "";
      topInput.value = "";
      bottomInput.value = "";
      status.value = "";
      progressFlag.value = false;
      percentage.value = 0;
      uploadFile = undefined;
      uploadedFileName = "";
    };

    const selectChangeHandle = (val: string) => {
      if (val === "png" || val === "movePng") {
        coordinateDisabledFlag.value = false;
      } else {
        coordinateDisabledFlag.value = true;
      }

      if (val === "photo") {
        uploadDisabledFlag.value = true;
      } else {
        uploadDisabledFlag.value = false;
      }

      if (
        val === "polygonVectorTile3D" ||
        val === "pointVectorTile3D" ||
        val === "lineVectorTile3D" ||
        val === "polygonVectorTile" ||
        val === "pointVectorTile" ||
        val === "lineVectorTile"
      ) {
        sridDisabledFlag.value = false;
      } else {
        sridDisabledFlag.value = true;
      }
      if (
        val === "png" ||
        val === "movePng" ||
        val === "rasterTile" ||
        val === "polygonVectorTile3D" ||
        val === "pointVectorTile3D" ||
        val === "lineVectorTile3D" ||
        val === "polygonVectorTile" ||
        val === "pointVectorTile" ||
        val === "lineVectorTile"
      ) {
        viewDisabledFlag.value = false;
      } else {
        viewDisabledFlag.value = true;
      }
      init();
    };

    const fileChangeHandle = (val1: any, val2: any) => {
      if (val2.length > 1) {
        upload.value.handleRemove(val2[0]);
      }
      uploadFile = val1;
      uploadedFileName = "";
    };

    const fileRemoveHandle = () => {
      uploadFile = undefined;
      uploadedFileName = "";
      status.value = "";
      percentage.value = 0;
      progressFlag.value = false;
    };

    const uploadHandle = async () => {
      if (uploadFlag) {
        uploadFlag = false;
      } else {
        notice("warning", "警告", "请不要重复点击");
        return;
      }
      if (uploadFile != undefined) {
        uploadedFileName = "";
        const fileList = createFileChunk(uploadFile.raw);
        const total = fileList.length;
        progressFlag.value = true;
        const uid = uuid();
        let flag = false;
        const handle = async () => {
          if (fileList.length > 0 && !flag) {
            const file = fileList.shift();
            const number = total - fileList.length - 1;
            const formData = new FormData();
            formData.append("file", file?.file as Blob);
            const data = await uploadParts(uid, number, formData);
            if (data != null && (data as any).code === 0) {
              percentage.value = parseFloat(
                (((number + 1) * 100) / total).toFixed(2)
              );
              handle();
            } else {
              flag = true;
            }
          }
        };
        for (let i = 0; i < 5; i++) {
          await handle();
        }
        if (fileList.length == 0 && !flag) {
          const data = await mergeParts(
            uid,
            total,
            typeValue.value,
            uploadFile.name
          );
          if (data != null && (data as any).code === 0) {
            status.value = "success";
            uploadedFileName = data.data;
          } else {
            status.value = "exception";
          }
        } else {
          status.value = "exception";
        }
        uploadFlag = true;
      } else {
        notice("warning", "警告", "请先选项文件");
      }
    };

    const confirmClick = async () => {
      const jsonData: {
        id: string;
        fileName: string;
        type: string;
        srid: string;
        coordinates: number[][];
        view: {
          zoom: number;
          center: number[];
        } | null;
      } = {
        id: (props.fileInfo as any).id,
        fileName: uploadedFileName,
        type: typeValue.value,
        coordinates: [],
        srid: sridInput.value,
        view: null,
      };
      if (typeValue.value != "") {
        if (typeValue.value != "photo") {
          if (uploadedFileName == "") {
            notice("warning", "警告", "请上传文件");
            return;
          }
          if (
            typeValue.value === "polygonVectorTile3D" ||
            typeValue.value === "pointVectorTile3D" ||
            typeValue.value === "lineVectorTile3D" ||
            typeValue.value === "polygonVectorTile" ||
            typeValue.value === "pointVectorTile" ||
            typeValue.value === "lineVectorTile"
          ) {
            if (sridInput.value === "") {
              notice("warning", "警告", "srid不能为空");
              return;
            }
          }
          if (typeValue.value === "png" || typeValue.value === "movePng") {
            if (
              leftInput.value === "" ||
              rightInput.value === "" ||
              topInput.value === "" ||
              bottomInput.value === ""
            ) {
              notice("warning", "警告", "请完善png坐标位置");
              return;
            } else {
              jsonData.coordinates.push([
                parseFloat(leftInput.value),
                parseFloat(topInput.value),
              ]);
              jsonData.coordinates.push([
                parseFloat(rightInput.value),
                parseFloat(topInput.value),
              ]);
              jsonData.coordinates.push([
                parseFloat(rightInput.value),
                parseFloat(bottomInput.value),
              ]);
              jsonData.coordinates.push([
                parseFloat(leftInput.value),
                parseFloat(bottomInput.value),
              ]);
            }
          }
          if (!viewDisabledFlag.value) {
            console.log(1)
            if (longValue.value === "" || latValue.value === "") {
              notice("warning", "警告", "视图经纬度不得为空");
              return;
            } else {
              jsonData.view = {
                zoom: selectValue.value,
                center: [longValue.value, latValue.value],
              };
            }
          }
        }
      } else {
        notice("warning", "警告", "请选择类型");
        return;
      }
      console.log(jsonData);

      loading.value = true;
      const data = await bindVisualData(jsonData);
      if (data != null && (data as any).code === 0) {
        context.emit("updateVisualFile", {
          visualType: typeValue.value,
          visualId: data.data,
        });
        notice("success", "成功", "可视化数据绑定成功");
      } else if (data != null && (data as any).code === 1) {
        notice(
          "warning",
          "警告",
          "当前文件绑定数据条目，若更改可视化绑定需要通过管理审核!"
        );
        context.emit("updateVisualFile", {
          visualType: "audit",
          visualId: (data as any).msg,
        });
      }
      loading.value = false;
    };

    const cancelClick = async () => {
      const data = await cancelVisualBind(fileInfo.value!.id);
      if (data != null && (data as any).code === 0) {
        notice("success", "成功", "取消可视化数据绑定");
        context.emit("updateVisualFile", { visualType: "", visualId: "" });
      }
    };

    return {
      longValue,
      latValue,
      typeValue,
      selectValue,
      options,
      zoomOptions,
      sridInput,
      leftInput,
      rightInput,
      topInput,
      bottomInput,
      uploadDisabledFlag,
      coordinateDisabledFlag,
      viewDisabledFlag,
      sridDisabledFlag,
      selectChangeHandle,
      uploadHandle,
      status,
      progressFlag,
      fileChangeHandle,
      fileRemoveHandle,
      upload,
      percentage,
      confirmClick,
      cancelClick,
      loading,
      fileInfo,
    };
  },
});
</script>

<style lang="scss" scoped>
.visual-data-bind {
  height: 640px;
  position: relative;
  .visual-head {
    height: 50px;
    background: black;
    color: white;
    line-height: 50px;
    padding-left: 20px;
    font-size: 20px;
  }
  .visual-body {
    width: calc(100% - 40px);
    height: calc(100% - 90px);
    padding: 20px;
    .visual-type {
      strong {
        font-size: 16px;
      }
      .el-select {
        width: 100%;
        margin-top: 10px;
      }
    }
  }

  .view {
    margin-top: 20px;
    display: flex;
    .flex-item {
      flex: 1;
      strong {
        font-size: 16px;
      }
    }

    .el-select {
      margin-top: 5px;
      width: 150px;
    }
    .el-input-number {
      margin-top: 5px;
    }
  }

  .srid {
    margin-top: 20px;
    strong {
      font-size: 16px;
    }
    .el-input {
      margin-top: 5px;
    }
  }

  .coordinate {
    margin-top: 20px;
    strong {
      font-size: 16px;
    }
    .top,
    .bottom {
      margin-top: 10px;
      display: flex;
      .left {
        width: 50%;
      }
      .right {
        width: 50%;
        strong {
          margin-left: 8px;
        }
      }
      .el-input {
        width: calc(100% - 50px);
      }
    }
  }
  .upload {
    margin-top: 20px;
    .upload-title {
      font-size: 16px;
      margin-bottom: 10px;
    }
    .el-progress {
      margin-top: 10px;
    }
  }

  .btn {
    position: absolute;
    bottom: 30px;
    width: calc(100% - 40px);
    text-align: center;
  }
}
</style>