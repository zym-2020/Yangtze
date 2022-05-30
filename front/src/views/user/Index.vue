<template>
  <div class="main">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="left">
          <div class="avatar">
            <el-avatar
              :size="80"
              :src="
                avatarUrl === ''
                  ? 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
                  : 'http://localhost:8002' + avatarUrl
              "
              fit="cover"
            />
          </div>
          <div class="userInfo" v-if="!editFlag">
            <div v-if="userInfo.id != ''">
              <el-button type="primary" round @click="editFlag = true"
                >编辑</el-button
              >
              <div
                class="info"
                v-if="
                  userInfo.name != '' &&
                  userInfo.name != undefined &&
                  userInfo.name != null
                "
              >
                <div>
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-nickname"></use>
                  </svg>
                </div>
                <div class="text">{{ userInfo.name }}</div>
              </div>
              <div
                class="info"
                v-if="
                  userInfo.contactEmail != '' &&
                  userInfo.contactEmail != undefined &&
                  userInfo.contactEmail != null
                "
              >
                <div>
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-email"></use>
                  </svg>
                </div>
                <div class="text">{{ userInfo.contactEmail }}</div>
              </div>
              <div
                class="info"
                v-if="
                  userInfo.occupation != '' &&
                  userInfo.occupation != undefined &&
                  userInfo.occupation != null
                "
              >
                <div>
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-zhiye"></use>
                  </svg>
                </div>
                <div class="text">{{ userInfo.occupation }}</div>
              </div>
              <div
                class="info"
                v-if="
                  userInfo.department != '' &&
                  userInfo.department != undefined &&
                  userInfo.department != null
                "
              >
                <div>
                  <svg style="width: 20px; height: 20px">
                    <use xlink:href="#icon-company"></use>
                  </svg>
                </div>
                <div class="text">{{ userInfo.department }}</div>
              </div>
            </div>
            <div v-else>
              <el-skeleton :rows="5" animated />
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
                v-model="userInfo.name"
                placeholder="用户名"
                size="small"
              />
            </div>
            <div class="info">
              <div class="icon">
                <svg style="width: 20px; height: 20px; margin-top: 2px">
                  <use xlink:href="#icon-email"></use>
                </svg>
              </div>
              <el-input
                v-model="userInfo.contactEmail"
                placeholder="联系邮箱"
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
                v-model="userInfo.occupation"
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
                v-model="userInfo.department"
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
              <avatar-upload class="upload" @upload="upload"></avatar-upload>
            </div>
            <div class="btn">
              <el-button size="small" @click="commit">提交</el-button>
              <el-button @click="editFlag = flase" size="small">取消</el-button>
            </div>
          </div>
        </div>
      </el-col>
      <el-col :span="20">
        <div class="right">
          <el-tabs v-model="activeName">
            <el-tab-pane label="资源" name="resource">
              <user-resource></user-resource>
            </el-tab-pane>
            <el-tab-pane label="工程" name="project">
              <user-project></user-project>
            </el-tab-pane>
            <el-tab-pane label="共享条目" name="share">
              <user-share-file></user-share-file>
            </el-tab-pane>
            <el-tab-pane label="消息" name="message">
              <user-message></user-message>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, reactive, ref } from "vue";
import UserResource from "./components/UserResource.vue";
import UserProject from "./components/UserProject.vue";
import { useStore } from "@/store";
import { getUserByEmail } from "@/api/request";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import UserShareFile from "./components/UserShareFile.vue";
import UserMessage from "./components/UserMessage.vue";

export default defineComponent({
  components: {
    UserResource,
    UserProject,
    AvatarUpload,
    UserShareFile,
    UserMessage,
  },
  setup() {
    const activeName = ref("resource");
    const store = useStore();
    const flag = ref(false);
    const editFlag = ref(false);
    const avatar = ref<File>();
    const avatarUrl = computed(() => {
      return store.state.user.avatar;
    });
    const userInfo = reactive({
      id: "",
      name: "",
      contactEmail: "",
      roles: [],
      avatar: "",
      occupation: "",
      department: "",
    });
    const getUserInfo = async () => {
      const data = await getUserByEmail();
      if (data != null) {
        userInfo.id = (data.data as any).id;
        userInfo.name = (data.data as any).name;
        userInfo.contactEmail = (data.data as any).contactEmail;
        userInfo.roles = (data.data as any).roles;
        userInfo.avatar = (data.data as any).avatar;
        userInfo.occupation = (data.data as any).occupation;
        userInfo.department = (data.data as any).department;
      }
    };

    const upload = (val: any) => {
      avatar.value = val;
      flag.value = true;
    };

    const commit = async () => {
      await store.dispatch("updateUserInfo", {
        name: userInfo.name,
        avatar: avatar.value,
        contactEmail: userInfo.contactEmail,
        occupation: userInfo.occupation,
        department: userInfo.department,
        flag: flag.value,
      });
      editFlag.value = false;
    };

    onMounted(async () => {
      await getUserInfo();
    });
    return {
      activeName,
      userInfo,
      editFlag,
      upload,
      commit,
      avatarUrl,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  padding: 0 10%;
  .left {
    .avatar {
      text-align: center;
    }
    margin-top: 20px;
    .userInfo {
      cursor: pointer;
      .el-button {
        width: 100%;
        margin-top: 10px;
      }
      .info {
        padding-left: 10px;
        display: flex;
        margin-top: 10px;
        height: 20px;
        .text {
          height: 20px;
          line-height: 20px;
          margin-left: 10px;
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
      }
    }
  }
  .right {
    margin-top: 20px;
  }
}
</style>