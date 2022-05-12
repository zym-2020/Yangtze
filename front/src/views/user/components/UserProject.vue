<template>
  <div class="project-main">
    <el-scrollbar height="80vh" :always="false">
      <div style="padding: 0 10px">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item, index) in data" :key="index">
            <div class="card" @click="toProject(index)">
              <div class="img">
                <el-avatar
                  shape="square"
                  fit="cover"
                  v-if="
                    item.avatar === '' ||
                    item.avatar === null ||
                    item.avatar === undefined
                  "
                  >{{ item.projectName }}</el-avatar
                >
                <img :src="item.avatar" width="248" height="180" v-else />
              </div>
              <div class="detail">
                <div class="head" :title="item.projectName">
                  {{ item.projectName }}
                </div>
                <div class="des">{{ item.des }}</div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-scrollbar>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getProjectsByEmail } from "@/api/request";
import router from "@/router";
export default defineComponent({
  setup() {
    const data = ref([]);

    const getProjectList = async () => {
      const projectList = await getProjectsByEmail();
      if (projectList != null) {
        data.value = projectList.data;
      }
    };

    const toProject = (index: number) => {
      router.push({
        name: "project",
        params: {
          id: (data.value[index] as any).id,
          name: (data.value[index] as any).projectName,
          result: (data.value[index] as any).result,
        },
      });
    };

    onMounted(async () => {
      await getProjectList();
    });

    return {
      data,
      toProject,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-main {
  height: 80vh;
  border-bottom: solid 0.5px #ebeef5;
  .el-scrollbar {
    .card {
      height: 38vh;
      border: solid 1px #ebeef5;
      background: rgba($color: #d0d8e0, $alpha: 0.3);
      margin-bottom: 20px;
      cursor: pointer;
      .img {
        height: 70%;
        text-align: center;
        .el-avatar,
        img {
          height: 80%;
          width: 80%;
          margin-top: 10%;
        }
      }
      .detail {
        height: 30%;
        padding: 0 10%;
        .head {
          height: 30%;
          font-size: 24px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
          -o-text-overflow: ellipsis;
        }
        .des {
          // height: 60%;
          word-wrap: break-word;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 2;
          overflow: hidden;
        }
      }
    }
  }
}
</style>