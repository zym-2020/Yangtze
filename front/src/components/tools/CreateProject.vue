<template>
  <div class="create-project">
    <div class="body">
      <div class="title">创建项目</div>
      <div class="form">
        <el-form
          :model="form"
          label-width="120px"
          label-position="top"
          :rules="rules"
          ref="ruleFormRef"
        >
          <el-form-item label="项目名：" prop="name">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="项目描述：">
            <el-input v-model="form.description" />
          </el-form-item>
          <el-form-item label="封面：">
            <el-upload
              class="avatar-uploader"
              action="#"
              :show-file-list="false"
              :auto-upload="false"
              :on-change="change"
            >
              <img v-if="imageUrl" :src="imageUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-form>
        <div class="btn">
          <el-button @click="commit(ruleFormRef)">提交</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import { addProject, addProjectWithoutAvatar } from "@/api/request";
// import { Plus } from "@element-plus/icons-vue";
import { notice } from "@/utils/notice";
import type { FormInstance, UploadFile } from "element-plus";
import { useStore } from "@/store";

export default defineComponent({
  emits: ["createProject"],
  //   components: { Plus },
  setup(_, context) {
    const form = reactive({
      name: "",
      description: "",
    });
    const result =
      '{"analyse":{"anyArea":{"analysisResultList":[],"classify":"任意区域冲淤","classifyCount":0},"area":{"analysisResultList":[],"classify":"断面面积冲淤","classifyCount":0},"boundary":{"analysisResultList":[],"classify":"边界分析","classifyCount":0},"branch":{"analysisResultList":[],"classify":"汊道断面比较","classifyCount":0},"deep":{"analysisResultList":[],"classify":"冲淤等深线","classifyCount":0},"deepContrast":{"analysisResultList":[],"classify":"等深线比较","classifyCount":0},"elev":{"analysisResultList":[],"classify":"特定高程冲淤","classifyCount":0},"line":{"analysisResultList":[],"classify":"深泓线比较","classifyCount":0},"section":{"analysisResultList":[],"classify":"断面形态","classifyCount":0},"sectionContrast":{"analysisResultList":[],"classify":"断面比较","classifyCount":0},"slope":{"analysisResultList":[],"classify":"河床坡度提取","classifyCount":0},"volume":{"analysisResultList":[],"classify":"河道容积计算","classifyCount":0}},"layerDataList":[]}';

    const rules = reactive({
      name: [{ required: true, message: "项目名不能为空", trigger: "blur" }],
    });
    const ruleFormRef = ref<FormInstance>();
    const imageUrl = ref("");
    const file = ref<File>();
    const store = useStore();

    const change = (uploadFile: UploadFile) => {
      if (uploadFile.status === "ready") {
        imageUrl.value = URL.createObjectURL(uploadFile.raw);
        file.value = uploadFile.raw;
      }
    };

    const commit = async (formEl: FormInstance | undefined) => {
      if (!formEl) return;
      await formEl.validate(async (valid) => {
        if (valid) {
          let data;
          if (file.value === undefined) {
            data = await addProjectWithoutAvatar({
              projectName: form.name,
              description: form.description,
              result: result,
            });
            if (data != null) {
              if ((data as any).code === 0) {
                notice("success", "成功", "创建成功");
              } else {
                notice("error", "错误", "创建错误");
              }
            }
          } else {
            const formData = new FormData();
            formData.append("projectName", form.name);
            formData.append("description", form.description);
            formData.append("result", result);
            formData.append("file", file.value);
            data = await addProject(formData);
            if (data != null) {
              if ((data as any).code === 0) {
                notice("success", "成功", "创建成功");
              } else {
                notice("error", "错误", "创建错误");
              }
            }
          }

          data.data.name = store.state.user.name;
          context.emit("createProject", data.data);
        }
      });
    };

    return {
      form,
      commit,
      rules,
      ruleFormRef,
      imageUrl,
      change,
    };
  },
});
</script>

<style lang="scss" scoped>
.create-project {
  height: 350px;
  padding: 20px 10px 10px 10px;
  width: 100%;
  background: #a6bed7;
  .body {
    background: #f0f0f0;
    height: 100%;
    .title {
      margin-left: 10px;
      height: 30px;
      line-height: 30px;
      font-size: 16px;
    }
    .form {
      height: calc(100% - 30px);
      width: 70%;
      margin: 0 auto;
      .btn {
        text-align: center;
      }
      /deep/ .el-upload {
        border: 1px dashed #8c939d;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        //   transition: var(--el-transition-duration-fast);
        .avatar {
          width: 50px;
          height: 50px;
          display: block;
        }
      }
      /deep/ .el-upload:hover {
        border-color: #a6bed7;
      }
      .el-icon.avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 50px;
        height: 50px;
        text-align: center;
      }
    }
  }
}
</style>