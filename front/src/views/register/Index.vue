<template>
  <div>
    <video poster="../../assets/login/video-cover.jpeg" loop autoplay muted>
      <source src="../../assets/login/night.mp4" />
    </video>
    <div class="form">
      <p class="title">用户注册</p>
      <el-form :model="form" :rules="rules" ref="ruleFormRef">
        <el-form-item prop="email">
          <el-input
            v-model="form.email"
            prefix-icon="Message"
            size="large"
            placeholder="请输入邮箱"
          />
        </el-form-item>
        <el-form-item prop="name">
          <el-input
            v-model="form.name"
            prefix-icon="User"
            size="large"
            placeholder="请输入昵称"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            prefix-icon="Lock"
            show-password
            type="password"
            size="large"
            placeholder="请输入密码"
          />
        </el-form-item>
        <el-form-item prop="surePassword">
          <el-input
            v-model="form.surePassword"
            prefix-icon="Lock"
            show-password
            type="password"
            size="large"
            placeholder="请确定密码"
          />
        </el-form-item>
      </el-form>
      <el-button
        type="primary"
        class="btn"
        @click="registerHandler(ruleFormRef)"
        >注册</el-button
      >
      <div>
        <el-button type="text" @click="toLogin">去登录</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import router from "@/router";
import { defineComponent, reactive, ref } from "vue";
import type { FormInstance } from "element-plus";
import { register } from "@/api/request";
import { notice } from "@/utils/notice";
import { Base64 } from "js-base64";
export default defineComponent({
  setup() {
    const ruleFormRef = ref<FormInstance>();
    const form = reactive({
      email: "",
      name: "",
      password: "",
      surePassword: "",
    });
    const validatePass = (rule: any, value: string, callback: any) => {
      if (value != form.password) {
        callback(new Error("前后密码不一致"));
      } else {
        callback();
      }
    };
    const rules = reactive({
      email: [
        { required: true, message: "邮箱不能为空", trigger: "blur" },
        { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
      ],
      name: [{ required: true, message: "昵称不能为空", trigger: "blur" }],
      password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
      surePassword: [
        { required: true, message: "确认密码不能为空", trigger: "blur" },
        { validator: validatePass, trigger: "blur" },
      ],
    });

    const toLogin = () => {
      router.push({ path: "/login" });
    };

    const registerHandler = async (formEl: FormInstance | undefined) => {
      if (!formEl) return;
      await formEl.validate(async (valid) => {
        if (valid) {
          const jsonData = {
            email: form.email,
            name: form.name,
            password: Base64.btoa(form.password),
            roles: ["visiter"],
          };
          let data = (await register(jsonData)) as any;
          if (data != null) {
            if (data.code === -3) {
              notice("warning", "失败", "邮箱已经存在!");
            } else if (data.code === 0) {
              notice("success", "成功", "注册成功!");
              router.push({ path: "/login" });
            } else {
              notice("warning", "失败", "注册失败!");
            }
          }
        }
      });
    };

    return {
      form,
      rules,
      toLogin,
      ruleFormRef,
      registerHandler,
    };
  },
});
</script>

<style lang="scss" scoped>
video {
  position: absolute;
  /* Vertical and Horizontal center*/
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
}
.form {
  width: 450px;
  margin: 0 auto;
  margin-top: 20vh;
  text-align: center;
  .btn {
    width: 450px;
  }
  .title {
    color: white;
    font-size: 25px;
  }
  .el-form-item /deep/ .el-input__inner {
    background-color: rgba(255, 255, 255, 0.247);
    color: white;
  }
}
</style>
