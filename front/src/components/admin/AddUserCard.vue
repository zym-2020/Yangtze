<template>
  <div class="add-user-card">
    <el-form label-width="70px" :model="form" :rules="rules" ref="ruleFormRef">
      <el-form-item label="邮&ensp;&ensp;箱" prop="email">
        <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item label="用户名" prop="name">
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="密&ensp;&ensp;码" prop="password">
        <el-input v-model="form.password" type="password" show-password />
      </el-form-item>
      <el-form-item label="部&ensp;&ensp;门">
        <el-input v-model="form.department" />
      </el-form-item>
      <el-form-item label="职&ensp;&ensp;业">
        <el-input v-model="form.occupation" />
      </el-form-item>
      <el-form-item label="头&ensp;&ensp;像">
        <avatar-upload @upload="upload" :pictureName="''"></avatar-upload>
      </el-form-item>
    </el-form>
    <div class="btn">
      <el-button type="primary" @click="commitHandle(ruleFormRef)"
        >确定</el-button
      >
      <el-button @click="cancelHandle">取消</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive } from "vue";
import { adminAddUser } from "@/api/request";
import AvatarUpload from "@/components/upload/AvatarUpload.vue";
import type { FormInstance, FormRules } from "element-plus";
import { Base64 } from "js-base64";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: { AvatarUpload },
  emits: ["cancel", "addUser"],
  setup(_, context) {
    const ruleFormRef = ref<FormInstance>();
    const form = ref({
      email: "",
      name: "",
      password: "",
      department: "",
      occupation: "",
    });

    const avatar = ref<File>();

    const rules = reactive<FormRules>({
      email: [
        {
          required: true,
          message: "邮箱不得为空",
          trigger: "blur",
        },
      ],
      name: [
        {
          required: true,
          message: "用户名不得为空",
          trigger: "blur",
        },
      ],
      password: [
        {
          required: true,
          message: "密码不得为空",
          trigger: "blur",
        },
      ],
    });

    const upload = (val: any) => {
      avatar.value = val;
    };

    const commitHandle = async () => {
      const jsonData = {
        email: form.value.email,
        name: form.value.name,
        password: Base64.btoa(form.value.password),
        department: form.value.department,
        occupation: form.value.occupation,
      };
      const formData = new FormData();
      if (avatar.value == undefined) {
        formData.append("file", new Blob());
      } else {
        formData.append("file", avatar.value);
      }
      formData.append("jsonString", JSON.stringify(jsonData));
      const data = await adminAddUser(formData);
      if (data != null && (data as any).code === 0) {
        notice("success", "成功", "成功添加用户");
        context.emit("addUser");
      } else if (data != null && (data as any).code === 3) {
        notice("error", "错误", "邮箱已经存在");
      }
    };

    const cancelHandle = () => {
      context.emit("cancel");
    };

    return {
      form,
      ruleFormRef,
      rules,
      commitHandle,
      cancelHandle,
      upload,
    };
  },
});
</script>

<style lang="scss" scoped>
.add-user-card {
  height: 400px;
  position: relative;
  .btn {
    position: absolute;
    bottom: 0px;
    right: 0px;
  }
}
</style>