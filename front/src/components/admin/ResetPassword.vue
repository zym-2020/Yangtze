<template>
  <div class="reset-password">
    <el-form label-width="80px" :model="form" :rules="rules" ref="ruleFormRef">
      <el-form-item label="密&ensp;&ensp;&ensp;&ensp;码" prop="ps1">
        <el-input v-model="form.ps1" type="password" show-password />
      </el-form-item>
      <el-form-item label="确认密码" prop="ps2">
        <el-input v-model="form.ps2" type="password" show-password />
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
import { Base64 } from "js-base64";
import { resetPassword } from "@/api/request";
import { notice } from "@/utils/notice";
import type { FormInstance, FormRules } from "element-plus";
export default defineComponent({
  emits: ["cancel", "commit"],
  props: {
    id: {
      type: String,
    },
  },
  setup(props, context) {
    const form = ref<{ ps1: string; ps2: string }>({
      ps1: "",
      ps2: "",
    });

    const ruleFormRef = ref<FormInstance>();
    const validatePass = (rule: any, value: string, callback: any) => {
      if (value != form.value.ps1) {
        callback(new Error("前后密码不一致"));
      } else {
        callback();
      }
    };

    const rules = reactive<FormRules>({
      ps1: [
        {
          required: true,
          message: "密码不得为空",
          trigger: "blur",
        },
      ],
      ps2: [
        {
          required: true,
          message: "密码不得为空",
          trigger: "blur",
        },
        { validator: validatePass, trigger: "blur" },
      ],
    });

    const cancelHandle = () => {
      context.emit("cancel");
    };

    const commitHandle = async (formEl: FormInstance | undefined) => {
      if (!formEl) return;
      await formEl.validate(async (valid, fields) => {
        if (valid) {
          const data = await resetPassword({
            id: props.id as string,
            password: Base64.btoa(form.value.ps1),
          });
          if (data != null && (data as any).code === 0) {
            notice("success", "成功", "密码重置成功!");
            context.emit("cancel");
          }
        }
      });
    };

    return {
      form,
      cancelHandle,
      commitHandle,
      ruleFormRef,
      rules,
    };
  },
});
</script>

<style lang="scss">
.reset-password {
  position: relative;
  height: 120px;
  .btn {
    float: right;
  }
}
</style>