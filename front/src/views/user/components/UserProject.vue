<template>
  <div class="project-main">
    <el-scrollbar height="80vh" :always="false">
      <div style="padding: 0 10px">
        <el-row :gutter="20">
          <el-col :span="6" v-for="(item, index) in data" :key="index">
            <div class="card">
              <div class="img">
                <img src="/protheme/沪苏通长江公路大桥工程.jpg" alt="" />
              </div>
              <div class="detail">
                <div class="head">{{ item.projectName }}</div>
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
import { getProjectId } from "@/api/request";
export default defineComponent({
  setup() {
    const data = ref([]);

    const getProjectList = async () => {
      const projectList = await getProjectId();
      if (projectList != null) {
        data.value = projectList.data;
      }
    };

    onMounted(async () => {
      await getProjectList();
    });

    return {
      data,
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
      margin-bottom: 10px;
      cursor: pointer;
      .img {
        height: 70%;
        text-align: center;
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
        }
        .des {
          height: 60%;
        }
      }
    }
  }
}
</style>