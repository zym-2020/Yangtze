<template>
  <div class="main">
    <el-row>
      <el-col :span="6">
        <div class="left">
          <div class="avatar">
            <el-avatar
              :src="
                avatarUrl === ''
                  ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
                  : prefix + 'visual/getAvatar/' + avatarUrl
              "
              fit="cover"
            />
          </div>
          <div class="userInfo" v-if="!editFlag">
            <div v-if="skeletonFlag">
              <el-skeleton :rows="5" animated />
            </div>
            <div v-else>
              <el-button type="primary" @click="editClick"
                >编辑个人信息</el-button
              >
              <div class="info">
                <div title="邮箱">
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-email"></use>
                  </svg>
                </div>
                <div class="text email">
                  <strong>{{ email }}</strong>
                </div>
              </div>
              <div class="info" v-if="userInfo.name != ''">
                <div title="用户名">
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-nickname"></use>
                  </svg>
                </div>
                <div class="text">{{ userInfo.name }}</div>
              </div>
              <div class="info" v-if="userInfo.occupation != ''">
                <div title="职业">
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-zhiye"></use>
                  </svg>
                </div>
                <div class="text">{{ userInfo.occupation }}</div>
              </div>
              <div class="info" v-if="userInfo.department != ''">
                <div title="单位">
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-company"></use>
                  </svg>
                </div>
                <div class="text">{{ userInfo.department }}</div>
              </div>
            </div>
          </div>
          <div v-else class="userEdit">
            <div class="info">
              <div class="icon">
                <svg style="width: 20px; height: 20px; margin-top: 2px">
                  <use xlink:href="#icon-nickname"></use>
                </svg>
              </div>
              <el-input
                v-model="editInfo.name"
                placeholder="用户名"
                size="small"
              />
            </div>
            <div class="info">
              <div class="icon">
                <svg style="width: 20px; height: 20px; margin-top: 2px">
                  <use xlink:href="#icon-zhiye"></use>
                </svg>
              </div>
              <el-input
                v-model="editInfo.occupation"
                placeholder="职业"
                size="small"
              />
            </div>
            <div class="info">
              <div class="icon">
                <svg style="width: 20px; height: 20px; margin-top: 2px">
                  <use xlink:href="#icon-company"></use>
                </svg>
              </div>
              <el-input
                v-model="editInfo.department"
                placeholder="单位"
                size="small"
              />
            </div>
            <div class="edit-upload">
              <div class="icon">
                <svg style="width: 20px; height: 20px; margin-top: 2px">
                  <use xlink:href="#icon-shangchuantouxiang"></use>
                </svg>
              </div>
              <avatar-upload
                class="upload"
                @upload="upload"
                :pictureName="avatarUrl"
              ></avatar-upload>
            </div>
            <div class="btn">
              <el-button size="small" @click="commit">提交</el-button>
              <el-button @click="editFlag = false" size="small">取消</el-button>
            </div>
          </div>
          <div class="resource">我的资源</div>
          <el-divider />
          <div class="resource-count">
            <div class="text">
              <div class="count">3510</div>
              <div class="classify">文件</div>
            </div>
            <div class="text">
              <div class="count">74</div>
              <div class="classify">数据条目</div>
            </div>
            <div class="text">
              <div class="count">10</div>
              <div class="classify">工程</div>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="18">
        <div class="right">
          <div class="head">
            <div
              :class="activeName === 'file' ? 'head-item active' : 'head-item'"
              @click="headClick('file')"
            >
              文件
            </div>
            <div
              :class="
                activeName === 'dataList' ? 'head-item active' : 'head-item'
              "
              @click="headClick('dataList')"
            >
              数据条目
            </div>
            <div
              :class="
                activeName === 'project' ? 'head-item active' : 'head-item'
              "
              @click="headClick('project')"
            >
              工程
            </div>
          </div>
          <router-view v-slot="{ Component }">
            <keep-alive>
              <component :is="Component" />
            </keep-alive>
          </router-view>
        </div>
      </el-col>
    </el-row>
  </div>
  <page-copyright />
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref, watch } from "vue";
import { useStore } from "@/store";
import { getUserByEmail } from "@/api/request";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import { prefix } from "@/prefix";
import PageCopyright from "@/components/page/PageCopyright.vue";
import router from "@/router";
export default defineComponent({
  components: {
    AvatarUpload,
    PageCopyright,
  },
  setup() {
    const skeletonFlag = ref(true);
    const activeName = ref("file");
    const store = useStore();
    const editFlag = ref(false);
    const avatar = ref<File>();

    const route = computed(() => {
      return router.currentRoute.value;
    });
    const avatarUrl = computed(() => {
      return store.state.user.avatar;
    });
    const email = computed(() => {
      return store.state.user.email;
    });
    const userInfo = ref<{
      name: string;
      occupation: string;
      department: string;
    }>({
      name: "",
      occupation: "",
      department: "",
    });

    const editInfo = ref<{
      name: string;
      occupation: string;
      department: string;
    }>({
      name: "",
      occupation: "",
      department: "",
    });

    watch(
      () => router.currentRoute.value.name,
      (newVal) => {
        if (newVal === "UserSpaceFile") {
          activeName.value = "file";
        } else if (newVal === "UserSpaceDataList") {
          activeName.value = "dataList";
        } else {
          activeName.value = "project";
        }
      }
    );

    const getUserInfo = async () => {
      if (router.currentRoute.value.name === "UserSpaceFile") {
        activeName.value = "file";
      } else if (router.currentRoute.value.name === "UserSpaceDataList") {
        activeName.value = "dataList";
      } else {
        activeName.value = "project";
      }
      const data = await getUserByEmail();
      if (data != null) {
        userInfo.value.name = (data.data as any).name;
        userInfo.value.occupation = (data.data as any).occupation;
        userInfo.value.department = (data.data as any).department;
      }
    };

    const editClick = () => {
      editInfo.value = Object.assign({}, userInfo.value);
      editFlag.value = true;
    };

    const upload = (val: any) => {
      avatar.value = val;
    };

    const headClick = (param: string) => {
      if (param === "file") {
        router.push({
          name: "UserSpaceFile",
        });
      } else if (param === "dataList") {
        router.push({
          name: "UserSpaceDataList",
        });
      } else {
        router.push({
          name: "UserSpaceProject",
        });
      }
      activeName.value = param;
    };

    const commit = async () => {
      let avatarFile;
      if (avatar.value === undefined) {
        avatarFile = new Blob();
      } else {
        avatarFile = avatar.value;
      }
      store.dispatch("updateUserInfo", {
        name: editInfo.value.name,
        occupation: editInfo.value.occupation,
        department: editInfo.value.department,
        avatar: avatarFile as File,
      });
      userInfo.value = Object.assign({}, editInfo.value);
      editFlag.value = false;
    };

    onMounted(async () => {
      console.log("123");
      skeletonFlag.value = true;
      await getUserInfo();
      skeletonFlag.value = false;
    });

    return {
      route,
      activeName,
      userInfo,
      editFlag,
      upload,
      commit,
      avatarUrl,
      prefix,
      skeletonFlag,
      email,
      editClick,
      editInfo,
      headClick,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  padding: 0 16vw;
  .el-row {
    .left {
      margin-top: 50px;
      .avatar {
        position: relative;
        display: block;
        width: 100%;
        &:after {
          content: "";
          display: block;
          padding-bottom: 100%;
        }
        .el-avatar {
          position: absolute;
          top: 0;
          left: 0;
          width: 100%;
          height: 100%;
        }
      }

      .userInfo {
        cursor: pointer;

        .el-button {
          width: 100%;
          height: 40px;
          font-size: 22px;
          font-weight: 600;
          margin-top: 10px;
        }
        .info {
          padding-left: 10px;
          display: flex;
          margin-top: 10px;
          height: 20px;
          .text {
            font-size: 16px;
            height: 20px;
            line-height: 20px;
            margin-left: 10px;
            color: #00abff;
          }
          .email {
            color: #565656;
          }
        }
      }
      .userEdit {
        .info {
          display: flex;
          height: 25px;
          margin-top: 10px;
          .icon {
            margin-right: 10px;
          }
        }
        .edit-upload {
          display: flex;
          margin-top: 10px;
          margin-top: 10px;
          height: 110px;
          .icon {
            margin-right: 10px;
          }
          .upload {
            height: 100%;
            width: 100%;
          }
        }
        .btn {
          text-align: center;
          margin-top: 20px;
        }
      }

      .resource {
        margin-top: 50px;
        font-size: 22px;
        font-weight: 500;
      }
      .resource-count {
        height: 100px;
        width: 100%;
        display: flex;
        .text {
          width: calc(100% / 3);
          text-align: center;
          .count {
            color: #00abff;
            font-size: 25px;
          }
          .classify {
            margin-top: 15px;
          }
        }
      }
    }
    .right {
      padding-left: 20px;
      margin-top: 20px;

      .head {
        height: 60px;
        box-sizing: border-box;
        border-bottom: solid 1px #dcdfe6;
        display: flex;
        margin-bottom: 20px;

        .head-item {
          width: 200px;
          text-align: center;
          line-height: 60px;
          font-size: 18px;
          cursor: pointer;
        }
        .active {
          color: #00abff;
          border-bottom: solid 2px #00abff;
        }
      }
    }
  }
}
</style>