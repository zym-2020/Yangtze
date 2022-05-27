<template>
  <div class="offline-dialog">
    <div class="head">
      <svg
        style="width: 30px; height: 30px; margin-top: 10px; margin-left: 20px"
      >
        <use xlink:href="#icon-anquan"></use>
      </svg>
      <div class="text">下线整改</div>
    </div>
    <div class="main">
      <el-scrollbar height="400px">
        <div class="scroll" v-show="step === 1">
          <el-collapse>
            <el-collapse-item
              :title="item.label"
              :name="index"
              v-for="(item, index) in collapseList"
              :key="index"
            >
              <el-row>
                <el-col
                  :span="12"
                  v-for="(detail, indexDetail) in item.data"
                  :key="indexDetail"
                >
                  <div class="report" @click="toNext(detail)">
                    <svg
                      style="
                        width: 40px;
                        height: 40px;
                        margin-top: 10px;
                        margin-right: 10px;
                      "
                    >
                      <use xlink:href="#icon-tousujubao"></use>
                    </svg>
                    <div class="text">{{ detail }}</div>
                  </div>
                </el-col>
              </el-row>
            </el-collapse-item>
          </el-collapse>
        </div>
        <div v-show="step === 2">
          <div class="body">
            <div class="cause">
              整改原因：<span>{{ topic }}</span>
            </div>
            <div class="description">
              <div>详情描述：</div>
              <el-input
                v-model="description"
                type="textarea"
                resize="none"
                placeholder="详情描述"
              />
            </div>
            <div class="picture">
              <div>添加图片凭证：</div>
              <avatar-upload @upload="upload"></avatar-upload>
            </div>
            <div class="notice">
              <strong>通知将以站内消息及邮件的形式通知用户！</strong>
            </div>
          </div>
        </div>
      </el-scrollbar>
    </div>
    <div class="foot">
      <div class="btn" v-if="step === 2">
        <el-button plain @click="step = 1">上一步</el-button>
        <el-button plain @click="commit">提交</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
export default defineComponent({
  components: { AvatarUpload },
  emits: ["commitInfo"],
  setup(props, context) {
    const collapseList = ref<any[]>([
      {
        label: "违法信息",
        data: [
          "色情低俗",
          "政治敏感",
          "血腥暴力",
          "贩卖野生物及制品",
          "危害社会安全",
          "侵犯公民个人信息",
        ],
      },
      {
        label: "数据问题",
        data: ["虚假数据", "数据与描述不符", "数据错误"],
      },
      {
        label: "数据涉密",
        data: ["数据涉密"],
      },
      {
        label: "数据溯源",
        data: ["数据来源不明", "数据侵权"],
      },
    ]);

    const step = ref(1);
    const topic = ref("");
    const description = ref("");
    const pictureFile = ref<File>();

    const commit = () => {
      context.emit("commitInfo", {
        topic: topic.value,
        description: description.value,
        pictureFile: pictureFile.value,
      });
    };

    const upload = (file: File) => {
      pictureFile.value = file;
    };

    const toNext = (content: string) => {
      topic.value = content;
      step.value = 2;
    };

    return {
      collapseList,
      step,
      toNext,
      topic,
      description,
      commit,
      upload,
    };
  },
});
</script>

<style lang="scss" scoped>
.offline-dialog {
  height: 500px;
  .head {
    height: 50px;
    display: flex;
    .text {
      line-height: 50px;
      font-size: 18px;
    }
  }
  .main {
    height: 400px;
    background: #e7f4fb;
    .scroll {
      margin: 0 30px;
      .report {
        height: 60px;
        display: flex;
        cursor: pointer;
        margin-top: 10px;
        &:hover {
          border: 1px solid #51a2e8;
        }
        .text {
          line-height: 60px;
          font-size: 18px;
        }
      }
      .el-collapse /deep/ .el-collapse-item__header {
        background: #e7f4fb;
        font-size: 16px;
        border-bottom: 1px solid #cbe1ee;
      }
      .el-collapse /deep/ .el-collapse-item__wrap {
        background: #e7f4fb;
      }
    }
    .body {
      margin: 30px;
      .cause {
        span {
          color: #0079e0;
        }
      }
      .description {
        margin-top: 30px;
        margin-bottom: 20px;
        .el-textarea {
          margin-top: 5px;
        }
      }
      .picture {
        display: flex;
        line-height: 98px;
      }
      .notice {
        margin-top: 20px;
      }
    }
  }
  .foot {
    height: 50px;
    background: #cbe1ee;
    position: relative;
    .btn {
      position: absolute;
      right: 20px;
      top: 10px;
    }
  }
}
</style>