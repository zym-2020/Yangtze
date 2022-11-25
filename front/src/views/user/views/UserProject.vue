<template>
  <div class="project-main">
    <div v-if="skeletonFlag">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else>
      <div class="card">
        <div class="head"></div>
        <div v-if="data.length > 0">
          <div class="card-item" v-for="(item, index) in data" :key="index">
            <div class="name">
              <div class="icon">
                <el-icon v-if="item.isPublic" color="#21d86d"
                  ><Unlock
                /></el-icon>
                <el-icon v-else color="#dd001b"><Lock /></el-icon>
              </div>
              <div class="text">
                {{ item.projectName }}
              </div>
            </div>
            <div class="time">
              {{ dateFormat(item.createTime, "yyyy年MM月dd日hh时") }}
            </div>
          </div>
        </div>
        <div v-else>
          <el-empty description="暂无数据" />
        </div>
      </div>
    </div>
    <div class="pagination">
      <el-pagination
        background
        layout="prev, pager, next"
        :total="total"
        :pager-count="5"
        v-model:current-page="currentPage"
        @current-change="currentChange"
        :hide-on-single-page="true"
      />
    </div>

    <el-dialog
      v-model="createFlag"
      width="500px"
      :show-close="false"
      :title="title"
    >
      <create-project
        v-if="createFlag"
        :projectInfo="projectInfo"
        :info="info"
        @updateProject="updateProject"
        @copyProject="copyProject"
      ></create-project>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAllByEmail, deleteProject } from "@/api/request";
import ProjectCard from "@/components/cards/ProjectCard.vue";
import CreateProject from "@/components/tools/CreateProject.vue";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import { notice } from "@/utils/notice";
import { prefix } from "@/prefix";
import { dateFormat } from "@/utils/common";
export default defineComponent({
  components: { ProjectCard, CreateProject },
  setup() {
    const skeletonFlag = ref(true);
    const title = ref("");
    const projectInfo = ref<any>();

    const data = ref<any[]>([]);
    const total = ref(0);
    const info = ref<any>();
    const createFlag = ref(false);
    const currentPage = ref(1);

    const getProjectList = async (page: number, size: number) => {
      const projectList = await getAllByEmail(page, size);
      if (projectList != null) {
        data.value = projectList.data.list;
        total.value = projectList.data.total;
      }
    };

    const currentChange = async (val: number) => {
      await getProjectList(val - 1, 10);
    };

    const commandHandle = (val: { type: string; index: number }) => {
      console.log(val);
      if (val.type === "nav") {
        router.push({
          name: "project",
          params: {
            id: data.value[val.index].id,
            projectInfo: JSON.stringify(data.value[val.index]),
          },
        });
      } else if (val.type === "update") {
        projectInfo.value = undefined;
        title.value = "修改项目";
        info.value = data.value[val.index];
        createFlag.value = true;
      } else if (val.type === "delete") {
        ElMessageBox.confirm("确定删除该项目吗?该操作执行后无法撤销", "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(async () => {
            const res = await deleteProject(data.value[val.index].id);
            if (res != null && (res as any).code === 0) {
              data.value.splice(val.index, 1);
              notice("success", "成功", "删除成功");
            }
          })
          .catch(() => {});
      } else if (val.type === "copy") {
        info.value = undefined;
        title.value = "修改项目";
        projectInfo.value = data.value[val.index];
        createFlag.value = true;
      }
    };

    const updateProject = (val: {
      avatar: string;
      id: string;
      projectName: string;
      isPublic: boolean;
    }) => {
      for (let i = 0; i < data.value.length; i++) {
        if (data.value[i].id === val.id) {
          data.value[i].projectName = val.projectName;
          data.value[i].isPublic = val.isPublic;
          data.value[i].avatar = val.avatar;
          createFlag.value = false;
          return;
        }
      }
    };

    const copyProject = (val: string) => {
      createFlag.value = false;
      router.push({
        name: "project",
        params: {
          id: val,
        },
      });
    };

    onMounted(async () => {
      await getProjectList(0, 10);
      console.log(data.value);
      skeletonFlag.value = false;
    });

    return {
      title,
      data,
      commandHandle,
      info,
      createFlag,
      updateProject,
      total,
      currentPage,
      currentChange,
      projectInfo,
      copyProject,
      skeletonFlag,
      prefix,
      dateFormat,
    };
  },
});
</script>

<style lang="scss" scoped>
.project-main {
  .card {
    border: solid #dcdfe6 1px;
    border-radius: 6px;
    overflow: hidden;

    .head {
      height: 50px;
      border-bottom: solid #dcdfe6 1px;
      background: #4c4c4c;
    }
    .card-item {
      border-bottom: solid #dcdfe6 1px;
      padding: 15px 20px;
      .name {
        display: flex;
        height: 30px;
        font-size: 18px;
        color: #25aef3;
        line-height: 30px;
        .icon {
          margin-right: 10px;
          margin-top: 3px;
        }
        .text:hover {
          cursor: pointer;
          text-decoration: underline;
        }
      }
      .time {
        font-size: 12px;
        margin-top: 5px;
        color: #7f8992;
      }
    }
  }
  .pagination {
    margin-top: 30px;
    margin-bottom: 50px;
    display: flex;
    justify-content: space-around;
  }
}
/deep/.el-dialog {
  .el-dialog__header {
    padding: 10px;
    margin: 0;
    background: #25aef3;
    .el-dialog__title {
      color: white;
    }
  }
  .el-dialog__body {
    padding: 0;
  }
}
</style>