<template>
  <div class="visual-bind">
    <div class="visual-bind-body">
      <div class="title">数据<span>可视化绑定</span>说明</div>
      <div class="detail">
        数据可视化主要旨在借助于图形化手段，清晰有效地传达数据信息，本平台目前支持16种特定地理，水文数据可视化（矢量数据、栅格数据、浓度场、含沙量......），需要用户上传符号要求的相应的可视化衍生数据
      </div>
      <div>
        <!-- <el-row>
          <el-col :span="8">
            <div class="select-type">
              <div class="head">
                <div class="number">01.</div>
                <div class="operate-title">选择可视化类型</div>
                <div class="description">为原始数据选择相应的可视化类型</div>
                <div class="image">
                  <img src="/png/格式.svg.png" alt="" />
                </div>
              </div>
              <div class="select-type-body">
                <el-select
                  v-model="typeValue"
                  placeholder="请选择类型"
                  size="large"
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
                <div class="example">
                  <img src="/png/polygon.png" alt="" />
                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="upload">
              <div class="head">
                <div class="number">02.</div>
                <div class="operate-title">上传可视化数据</div>
                <div class="description">可视化数据需要按规定要求组织</div>
                <div class="image">
                  <img src="/png/折线图.svg.png" alt="" />
                </div>
              </div>
              <div class="upload-body">
                <div class="explain"></div>
                <el-upload drag action="#" multiple>
                  <el-icon class="el-icon--upload"><upload-filled /></el-icon>
                  <div class="el-upload__text">
                    拖拽或者 <em>点击上传文件</em>
                  </div>
                </el-upload>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="preview">
              <div class="head">
                <div class="number">03.</div>
                <div class="operate-title">数据预览</div>
                <div class="description">可视化数据预览</div>
                <div class="image">
                  <img src="/png/示意图.svg.png" alt="" />
                </div>
              </div>
              <div class="preview-body">
                <el-button type="primary" plain>预览</el-button>
                <div class="scenario">
                  <el-empty description="暂无可预览数据" />
                </div>
              </div>
            </div>
          </el-col>
        </el-row> -->
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
// import { UploadFilled } from "@element-plus/icons-vue";
export default defineComponent({

  setup() {
    const typeValue = ref("");
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
            value: "pointVectorTile",
          },
          {
            label: "lineVectorTile3D（带有ZM属性的矢量线数据）",
            value: "lineVectorTile",
          },
          {
            label: "polygonVectorTile3D（带有ZM属性的矢量面数据）",
            value: "polygonVectorTile",
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

    return {
      typeValue,
      options,
    };
  },
});
</script>

<style lang="scss" scoped>
.visual-bind {
  padding-top: 30px;
  height: calc(100% - 30px);
  .visual-bind-body {
    font-family: "Gilroy";
    font-style: normal;
    .title {
      height: 51px;
      text-align: center;

      font-weight: 800;
      font-size: 46.3862px;
      line-height: 50px;
      span {
        color: rgba(117, 191, 122, 1);
      }
    }
    .detail {
      height: 100px;
      font-weight: 500;
      font-size: 23px;
      line-height: 26px;
      width: 620px;
      color: #9e9da2;
      margin: 20px auto 20px;
    }

    .select-type {
      height: 600px;
      .select-type-body {
        .el-select {
          width: 80%;
          margin: 20px 10%;
        }
        .example {
          height: 350px;
          width: 460px;
          margin: 0 auto;
          img {
            height: 100%;
          }
        }
      }
    }
    .upload {
      height: 600px;
      .explain {
        height: 200px;
      }
      .upload-body {
        width: 80%;
        margin: 20px auto;
        .el-upload__text {
          font-size: 18px;
          font-weight: bold;
        }
      }
    }
    .preview {
      height: 600px;
      .preview-body {
        text-align: center;
        .el-button {
          margin-top: 20px;
          width: 80%;
        }
        .scenario {
          height: 400px;
          width: 80%;
          margin: 10px auto 0;
        }
      }
    }

    .head {
      height: 150px;
      position: relative;
      .number {
        position: absolute;
        width: 107px;
        height: 120px;
        font-weight: 500;
        font-size: 75px;
        line-height: 120px;
        color: #75bf7a;
        left: 50px;
      }
      .operate-title {
        position: absolute;
        font-weight: 600;
        font-size: 33px;
        line-height: 30px;
        height: 30px;
        left: 165px;
        top: 60px;
      }
      .description {
        position: absolute;
        font-weight: 500;
        font-size: 22px;
        line-height: 23px;
        color: #9e9da2;
        left: 50px;
        top: 110px;
      }
      .image {
        position: absolute;
        height: 150px;
        right: 50px;
        img {
          height: 100%;
        }
      }
    }
  }

  .file-info {
    position: absolute;
    top: 30px;
    left: 70%;
  }
}
</style>