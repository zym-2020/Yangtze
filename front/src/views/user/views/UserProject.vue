<template>
  <div class="project-main">
    <div v-if="skeletonFlag">
      <el-skeleton :rows="5" animated />
    </div>
    <div v-else>
      <div class="card">
        <div class="head">
          <div
            :class="active === 0 ? 'head-item active' : 'head-item'"
            @click="headClick(0)"
          >
            <div class="icon">
              <el-icon><Film /></el-icon>
            </div>
            <div class="text">{{ allTotal }}&nbsp;个项目</div>
          </div>
          <div
            :class="active === 1 ? 'head-item active' : 'head-item'"
            @click="headClick(1)"
          >
            <div class="icon">
              <el-icon><Unlock /></el-icon>
            </div>
            <div class="text">{{ publicTotal }}&nbsp;个公开项目</div>
          </div>
          <div
            :class="active === -1 ? 'head-item active' : 'head-item'"
            @click="headClick(-1)"
          >
            <div class="icon">
              <el-icon><Lock /></el-icon>
            </div>
            <div class="text">{{ privateTotal }}&nbsp;个私有项目</div>
          </div>
          <div class="btn">
            <el-button plain @click="createHandle">创建项目</el-button>
          </div>
        </div>
        <div v-if="data.length > 0">
          <div class="card-item" v-for="(item, index) in data" :key="index">
            <div class="name">
              <div class="icon">
                <el-icon v-if="item.isPublic" color="#21d86d"
                  ><Unlock
                /></el-icon>
                <el-icon v-else color="#dd001b"><Lock /></el-icon>
              </div>
              <div class="text" :title="item.projectName" @click="nav(index)">
                {{ item.projectName }}
              </div>
              <div class="more">
                <el-dropdown trigger="hover">
                  <el-icon><MoreFilled /></el-icon>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item
                        @click="commandHandle({ type: 'update', index: index })"
                        ><el-icon><Edit /></el-icon
                        >修改项目信息</el-dropdown-item
                      >
                      <el-dropdown-item
                        @click="commandHandle({ type: 'copy', index: index })"
                        ><el-icon><CopyDocument /></el-icon
                        >拷贝项目</el-dropdown-item
                      >
                      <el-dropdown-item
                        @click="commandHandle({ type: 'delete', index: index })"
                        ><el-icon><DeleteFilled /></el-icon
                        >删除项目</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
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
        layout="total, jumper, prev, pager, next"
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
        @createProject="createProject"
      ></create-project>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { getAllByEmail, deleteProject, getCount } from "@/api/request";
import CreateProject from "@/components/analyse/CreateProject.vue";
import { ElMessageBox } from "element-plus";
import router from "@/router";
import { notice } from "@/utils/notice";
import { prefix } from "@/prefix";
import { dateFormat } from "@/utils/common";
import NProgress from "nprogress";

export default defineComponent({
  components: { CreateProject },
  setup() {
    const skeletonFlag = ref(true);
    const title = ref("");
    const projectInfo = ref<any>();

    const data = ref<any[]>([]);
    const total = ref(0);
    const info = ref<any>();
    const createFlag = ref(false);
    const currentPage = ref(1);
    const active = ref(0);
    const allTotal = ref(0);
    const publicTotal = ref(0);
    const privateTotal = ref(0);

    const nav = (index: number) => {
      router.push({
        name: "project",
        params: {
          id: data.value[index].id,
          projectInfo: JSON.stringify(data.value[index]),
        },
      });
    };

    const createHandle = () => {
      projectInfo.value = undefined;
      info.value = undefined;
      createFlag.value = true;
      title.value = "创建项目"
    };

    const headClick = async (param: number) => {
      await getProjectList(0, 10, param);
      active.value = param;
    };

    const getProjectList = async (
      page: number,
      size: number,
      status: number
    ) => {
      NProgress.start();
      const projectList = await getAllByEmail(page, size, status);
      if (projectList != null) {
        data.value = projectList.data.list;
        total.value = projectList.data.total;
      }
      NProgress.done();
    };

    const currentChange = async (val: number) => {
      await getProjectList(val - 1, 10, active.value);
    };

    const commandHandle = (val: { type: string; index: number }) => {
      if (val.type === "update") {
        projectInfo.value = undefined;
        title.value = "修改项目";
        info.value = data.value[val.index];
        createFlag.value = true;
      } else if (val.type === "copy") {
        info.value = undefined;
        title.value = "拷贝项目";
        projectInfo.value = data.value[val.index];
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

    const createProject = (val: string) => {
      createFlag.value = false;
      router.push({
        name: "project",
        params: {
          id: val,
        },
      });
    };

    onMounted(async () => {
      const data = await getCount();
      if (data != null && (data as any).code === 0) {
        allTotal.value = data.data.allTotal;
        publicTotal.value = data.data.publicTotal;
        privateTotal.value = data.data.privateTotal;
      }
      await getProjectList(0, 10, active.value);
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
      nav,
      active,
      headClick,
      allTotal,
      publicTotal,
      privateTotal,
      createHandle,
      createProject,
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
      display: flex;
      position: relative;
      .head-item {
        color: #848c96;
        line-height: 50px;
        display: flex;
        margin-left: 10px;
        margin-right: 20px;
        cursor: pointer;

        .icon {
          margin-top: 3px;
          margin-right: 3px;
          font-size: 20px;
        }
        .text {
          font-size: 16px;
        }
      }
      .active {
        font-weight: 700;
        color: white;
      }
      .btn {
        position: absolute;
        right: 10px;
        top: 10px;
      }
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
        position: relative;
        .icon {
          margin-right: 10px;
          margin-top: 3px;
        }
        .text {
          max-width: calc(100% - 100px);
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
          &:hover {
            cursor: pointer;
            text-decoration: underline;
          }
        }

        .more {
          position: absolute;
          right: 0px;
          top: 10px;
          color: black;
          cursor: pointer;
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
    background: #050d21;
    .el-dialog__title {
      color: white;
    }
  }
  .el-dialog__body {
    padding: 0;
  }
}
</style>