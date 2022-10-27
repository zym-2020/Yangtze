<template>
  <div class="create-project">
    <div class="body">
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
          <el-form-item label="权限：" prop="permission">
            <el-radio-group v-model="form.isPublic">
              <el-radio label="true" size="large">公共</el-radio>
              <el-radio label="false" size="large">私密</el-radio>
            </el-radio-group>
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
import { defineComponent, onMounted, reactive, ref } from "vue";
import { addProject, updateProjectInfo } from "@/api/request";
import { notice } from "@/utils/notice";
import type { FormInstance, UploadFile } from "element-plus";

export default defineComponent({
  props: {
    info: {
      type: Object,
    },
  },
  emits: ["createProject", "updateProject"],
  setup(props, context) {
    const form = reactive({
      name: "",
      isPublic: "true",
    });

    const rules = reactive({
      name: [{ required: true, message: "项目名不能为空", trigger: "blur" }],
    });
    const ruleFormRef = ref<FormInstance>();
    const imageUrl = ref("");
    const file = ref<File>();

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
          if (props.info === undefined) {
            const formData = new FormData();
            if (file === undefined) {
              formData.append("file", new Blob());
            } else {
              formData.append("file", file.value as File);
            }
            formData.append("projectName", form.name);
            formData.append("isPublic", form.isPublic);
            const data = await addProject(formData);

            if (data != null && (data as any).code === 0) {
              context.emit("createProject", data.data);
            }
          } else {
            const formData = new FormData();
            if (file === undefined) {
              formData.append("file", new Blob());
            } else {
              formData.append("file", file.value as File);
            }
            formData.append("projectName", form.name);
            formData.append("isPublic", form.isPublic);
            formData.append("id", (props.info as any).id);
            const data = await updateProjectInfo(formData);
            if (data != null && (data as any).code === 0) {
              context.emit("updateProject", {
                avatar: data.data,
                id: (props.info as any).id,
                projectName: form.name,
                isPublic: form.isPublic === "true" ? true : false,
              });
              notice("success", "成功", "修改成功");
            }
          }
        }
      });
    };

    onMounted(() => {
      if (props.info != undefined) {
        form.name = (props.info as any).projectName;
        form.isPublic = (props.info as any).isPublic ? "true" : "false";
        imageUrl.value =
          "http://localhost:8002/visual/getAvatar/" +
          (props.info as any).avatar;
      }
    });

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
  height: 400px;
  width: 100%;
  .body {
    // background: #f0f0f0;
    height: 100%;
    .title {
      margin-left: 10px;
      height: 30px;
      line-height: 30px;
      font-size: 16px;
    }
    .form {
      padding-top: 10px;
      height: calc(100% - 30px);
      width: 80%;
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
          width: 100px;
          height: 100px;
          display: block;
        }
      }
      /deep/ .el-upload:hover {
        border-color: #a6bed7;
      }
      .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 100px;
        height: 100px;
        text-align: center;
      }
    }
  }
}
</style>