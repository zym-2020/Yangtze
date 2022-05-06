<template>
  <div class="main">
    <el-row :gutter="20">
      <el-col :span="4">
        <div class="left">
          <el-avatar
            :size="80"
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          />
          <div>
                
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
          </el-tabs>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref } from "vue";
import UserResource from "./components/UserResource.vue";
import UserProject from "./components/UserProject.vue";
import { useStore } from '@/store'
export default defineComponent({
  components: { UserResource, UserProject },
  setup() {
    const activeName = ref("resource");
    const store = useStore()
    const userInfo = reactive({
        id: '',
        name: '',
        email: '',
        roles: []
    })
    const getUserInfo = () => {
      userInfo.id = store.state.user.id
      userInfo.name = store.state.user.name
      userInfo.email = store.state.user.email
      userInfo.roles = store.state.user.roles as any
    };

    onMounted(() => {
      getUserInfo();
    });
    return {
      activeName,
    };
  },
});
</script>

<style lang="scss" scoped>
.main {
  padding: 0 10%;
  .left {
    text-align: center;
    margin-top: 20px;
  }
  .right {
    margin-top: 20px;
  }
}
</style>