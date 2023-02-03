<template>
  <div class="visual-data-bind">
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
            <span>左上：</span>
            <el-input
              v-model="leftTopInput"
              :disabled="coordinateDisabledFlag"
            />
          </div>
          <div class="right">
            <span>右上：</span>
            <el-input
              v-model="rightTopInput"
              :disabled="coordinateDisabledFlag"
            />
          </div>
        </div>
        <div class="bottom">
          <div class="left">
            <span>左下：</span>
            <el-input
              v-model="leftBottomInput"
              :disabled="coordinateDisabledFlag"
            />
          </div>
          <div class="right">
            <span>右下：</span>
            <el-input
              v-model="rightBottomInput"
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
        <el-progress :percentage="100" :status="status" v-if="progressFlag" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { notice } from "@/utils/notice";
import { createFileChunk } from "@/utils/file";
import { uuid } from '@/utils/common'
import { uploadParts } from '@/api/request'
import { defineComponent, onMounted, ref } from "vue";
export default defineComponent({
  props: {
    fileInfo: {
      type: Object,
    },
  },
  setup(props, context) {
    const upload = ref<any>();

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
    const leftTopInput = ref("");
    const rightTopInput = ref("");
    const leftBottomInput = ref("");
    const rightBottomInput = ref("");

    const uploadDisabledFlag = ref(true);
    const coordinateDisabledFlag = ref(true);
    const sridDisabledFlag = ref(true);

    const status = ref("");
    const progressFlag = ref(false);

    let uploadFile: any = undefined;

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
    };

    const fileChangeHandle = (val1: any, val2: any) => {
      if (val2.length > 1) {
        upload.value.handleRemove(val2[0]);
      }
      uploadFile = val1;
    };

    const fileRemoveHandle = () => {
      uploadFile = undefined;
      progressFlag.value = false;
    };

    const uploadHandle = () => {
      if (uploadFile != undefined) {
        console.log(uploadFile);
        // const fileList = createFileChunk(uploadFile.raw);
        // console.log(fileList);
        // progressFlag.value = true;
        // const uid = uuid()
        // const handle = async (file: Blob, number: number) => {

        // }
      } else {
        notice("warning", "警告", "请先选项文件");
      }
    };

    return {
      typeValue,
      options,
      sridInput,
      leftTopInput,
      rightTopInput,
      leftBottomInput,
      rightBottomInput,
      uploadDisabledFlag,
      coordinateDisabledFlag,
      sridDisabledFlag,
      selectChangeHandle,
      uploadHandle,
      status,
      progressFlag,
      fileChangeHandle,
      fileRemoveHandle,
      upload,
    };
  },
});
</script>

<style lang="scss" scoped>
.visual-data-bind {
  height: 600px;
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
}
</style>