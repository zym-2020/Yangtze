<template>
  <div class="data-detail">
    <div class="left">
      <el-row>
        <el-col :span="12">
          <div class="basicInfo">
            <div class="divider">
              <div class="mark"></div>
              <div class="text"><strong>基本信息</strong></div>
            </div>
            <div class="basicInfo-item">
              <data-description
                :data="{ key: '数据时间：', value: fileMeta.time }"
              ></data-description>
            </div>
            <div class="basicInfo-item">
              <data-description
                :data="{ key: '空间位置：', value: fileMeta.range }"
              ></data-description>
            </div>
            <div class="basicInfo-item">
              <data-description
                :data="{ key: '数据类型：', value: fileMeta.type }"
              ></data-description>
            </div>
            <div class="basicInfo-item">
              <data-description
                :data="{ key: '数据标签：', value: fileInfo.tags }"
              ></data-description>
            </div>
            <div class="basicInfo-item">
              <data-description
                :data="{ key: '更新时间：', value: date(fileInfo.updateTime) }"
              ></data-description>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="avatar">
            <el-avatar shape="square" :size="200" :src="avatar" />
          </div>
        </el-col>
        <el-col :span="24">
          <div class="providerInfo">
            <div class="divider">
              <div class="mark"></div>
              <div class="text"><strong>数据提供方</strong></div>
            </div>
            <div class="providerInfo-item">
              <data-description
                :data="{ key: '联系人：', value: fileMeta.provider }"
              ></data-description>
            </div>
            <div class="providerInfo-item">
              <data-description
                :data="{
                  key: '电&#12288;话：',
                  value: fileMeta.provider_phone,
                }"
              ></data-description>
            </div>
            <div class="providerInfo-item">
              <data-description
                :data="{
                  key: '邮&#12288;箱：',
                  value: fileMeta.provider_email,
                }"
              ></data-description>
            </div>
            <div class="providerInfo-item">
              <data-description
                :data="{
                  key: '地&#12288;址：',
                  value: fileMeta.provider_address,
                }"
              ></data-description>
            </div>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="description">
            <div class="divider">
              <div class="mark"></div>
              <div class="text"><strong>数据摘要</strong></div>
            </div>
            <div class="des">
              {{ fileInfo.description }}
            </div>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="detail">
            <div class="divider">
              <div class="mark"></div>
              <div class="text"><strong>数据说明</strong></div>
            </div>
            <div class="editor-content-view" v-html="fileMeta.detail"></div>
          </div>
        </el-col>
        <el-col :span="24">
          <div class="download">
            <div class="divider">
              <div class="mark"></div>
              <div class="text"><strong>数据下载</strong></div>
            </div>
            <div class="type">
              <div class="type-item">
                <div class="type-item-top">
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-shujuku"></use>
                  </svg>
                  <div class="type-item-top-text">原始数据</div>
                </div>
                <div class="type-item-bottom">
                  <el-button size="small" v-if="fileMeta.get_online" @click="downloadOrigin">
                    下载
                  </el-button>
                  <el-button size="small" v-else> 添加订单 </el-button>
                </div>
              </div>
              <div class="type-item">
                <div class="type-item-top">
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-shujuku1"></use>
                  </svg>
                  <div class="type-item-top-text">整合数据</div>
                </div>
                <div class="type-item-bottom">
                  <el-button size="small" v-if="fileMeta.get_online">
                    下载
                  </el-button>
                  <el-button size="small" v-else> 添加订单 </el-button>
                </div>
              </div>
              <div class="type-item">
                <div class="type-item-top">
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-shuju"></use>
                  </svg>
                  <div class="type-item-top-text">可视化数据</div>
                </div>
                <div class="type-item-bottom">
                  <el-button size="small" v-if="fileMeta.get_online">
                    下载
                  </el-button>
                  <el-button size="small" v-else> 添加订单 </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="right">
      <div class="body">
        <div class="contributor">
          <div class="divider">
            <div class="mark"></div>
            <div class="text"><strong>条目创建者</strong></div>
          </div>
          <div class="creator">
            <el-avatar
              :src="avatarUrl"
            />
            <div class="name">{{fileMeta.name}}</div>
          </div>
          <el-divider content-position="left"
            >{{ date(fileInfo.createTime) }} 创建</el-divider
          >
        </div>
        <!-- <div class="history">
          <div class="divider">
            <div class="mark"></div>
            <div class="text"><strong>修改历史</strong></div>
          </div>
          <div class="content">
            上次更新时间：{{ date(fileInfo.updateTime) }}
          </div>
          <div class="content">
            更新次数：11
            <span style="color: #94bddb; cursor: pointer">查看</span>
          </div>
        </div> -->
        <div class="explain">
          <div class="divider">
            <div class="mark"></div>
            <div class="text"><strong>数据获取方式</strong></div>
          </div>
          <div class="content" v-if="fileMeta.get_online">在线获取</div>
          <div class="content" v-else>订单获取</div>
        </div>
        <div class="Process">
          <div class="divider">
            <div class="mark"></div>
            <div class="text"><strong>数据获取流程</strong></div>
          </div>
          <div class="online" v-if="fileMeta.get_online">
            <div class="title">在线获取</div>
            <div class="content">点击下载按钮</div>
          </div>
          <div class="list" v-else>
            <div class="title">订单获取</div>
            <div class="content">[1] 加入数据订单</div>
            <div class="content">[2] 简单填写数据使用用途</div>
            <div class="content">[3] 在线下载数据</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted } from "vue";
import DataDescription from "../page/DataDescription.vue";
import { dateFormat, imgBase64 } from "@/utils/common";
import { decrypt } from '@/utils/auth'
import { useStore } from '@/store'
import { getDownloadURL } from '@/api/request'
import "@/assets/css/wangeditor.css";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { DataDescription },
  props: {
    fileInfo: {
      type: Object,
    },
    fileMeta: {
      type: Object,
    },
  },
  setup(props) {
    const store = useStore()
    const fileInfo = computed(() => {
      return props.fileInfo;
    });
    const fileMeta = computed(() => {
      return props.fileMeta;
    });

    const avatarUrl = computed(() => {
      return (props.fileMeta as any).avatar === '' ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png' : 'http://localhost:8002' + (props.fileMeta as any).avatar
    })

    const avatar = computed(() => {
      if (
        (props.fileInfo as any).avatar != "" &&
        (props.fileInfo as any).avatar != undefined &&
        (props.fileInfo as any).avatar != null
      ) {
        return "http://localhost:8002" + (props.fileInfo as any).avatar;
      }

      return imgBase64((props.fileInfo as any).name === undefined ? '' : (props.fileInfo as any).name);
    });

    const date = (time: string) => {
      return dateFormat(time, "yyyy-MM-dd");
    };

    const downloadOrigin = async () => {
      const data = await getDownloadURL((fileInfo.value as any).id)
      if(data != null) {
        if((data as any).code === 0) {
          window.location.href = "http://172.21.213.177:8002/download/downloadShareFile/" + store.state.user.id + "/" + decrypt(data.data, store.state.user.id)
        } else {
          notice("error", "错误", (data as any).msg)
        }
      }
    }

    return {
      fileInfo,
      fileMeta,
      date,
      avatar,
      downloadOrigin,
      avatarUrl
    };
  },
});
</script>

<style lang="scss" scoped>
.data-detail {
  width: 1250px;
  margin: 0 auto;
  display: flex;

  .left {
    width: 950px;
    margin-top: 10px;

    .avatar {
      position: relative;
      .el-avatar {
        position: absolute;
        right: 100px;
      }
    }

    .providerInfo {
      margin-top: 40px;
      .providerInfo-item {
        /deep/ .data-description {
          .value {
            width: 840px;
          }
        }
      }
    }

    .divider {
      height: 25px;
      display: flex;
      .mark {
        height: 25px;
        width: 8px;
        background: #458fd1;
      }
      .text {
        line-height: 25px;
        margin-left: 10px;
      }
    }
    .basicInfo-item, .providerInfo-item {
      margin-top: 10px;
    }


    .description {
      margin-top: 40px;

      .des {
        margin: 10px;
      }
    }
    .detail {
      margin-top: 40px;
    }

    .download {
      margin-top: 30px;
      margin-bottom: 80px;
      .type {
        margin-top: 20px;
        display: flex;
        .type-item {
          flex: 1;
          .type-item-top {
            display: flex;
            .type-item-top-text {
              margin-left: 15px;
            }
          }
          .type-item-bottom {
            margin-top: 10px;
          }
        }
      }
    }
  }
  .right {
    width: 300px;
    margin-top: 10px;
    min-height: calc(100vh - 170px);
    background: #f6f7fa;
    .body {
      margin: 20px 10px;
      .divider {
        margin-bottom: 10px;
        margin-top: 20px;
        height: 25px;
        display: flex;
        .mark {
          height: 25px;
          width: 8px;
          background: #00d083;
        }
        .text {
          line-height: 25px;
          margin-left: 10px;
        }
      }
      .contributor {
        margin-bottom: 10px;
        .creator {
          margin-top: 10px;
          display: flex;
          .name {
            line-height: 40px;
            margin-left: 10px;
            font-size: 18px;
          }
        }
        .el-divider {
          /deep/ .el-divider__text {
            background: #f6f7fa;
          }
        }
      }
      .Process {
        .online,
        .list {
          margin-left: 10px;
          .title {
            color: #94bddb;
            margin-bottom: 10px;
            margin-top: 10px;
          }
        }
      }
      .content {
        font-size: 14px;
        margin-left: 10px;
        margin-bottom: 5px;
      }
    }
  }
}
</style>