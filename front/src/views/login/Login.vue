<template>
  <div>
    <video poster="../../assets/login/video-cover.jpeg" loop autoplay muted>
      <source src="../../assets/login/night.mp4" />
    </video>
    <div class="form">
      <p class="title">系统登录</p>
      <el-form :model="form" :rules="rules" ref="ruleFormRef">
        <el-form-item prop="email">
          <el-input v-model="form.email" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            prefix-icon="Lock"
            show-password
            type="password"
            size="large"
          />
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="click(ruleFormRef)" class="btn"
        >登录</el-button
      >
      <div>
        <el-button text @click="toRegister">注册</el-button>
        <el-button text>忘记密码？</el-button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from "vue";
import { useStore } from "@/store/index";
import router from "@/router";
import type { FormInstance } from "element-plus";
import { Base64 } from "js-base64";
export default defineComponent({
  setup() {
    const store = useStore();
    const ruleFormRef = ref<FormInstance>();
    const form = reactive({
      email: "nhri_admin@163.com",
      password: "123",
    });
    const click = async (formEl: FormInstance | undefined) => {
      if (!formEl) return;
      await formEl.validate(async (valid) => {
        if (valid) {
          let jsonData = {
            email: form.email,
            password: Base64.btoa(form.password),
          };
          await store.dispatch("login", jsonData);
          router.push({ path: "/" });
        }
      });
    };

    const rules = reactive({
      email: [
        { required: true, message: "邮箱不能为空", trigger: "blur" },
        { type: "email", message: "请输入正确的邮箱格式", trigger: "blur" },
      ],
      password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
    });

    const toRegister = () => {
      router.push({ path: "/register" });
    };

    return {
      form,
      click,
      rules,
      toRegister,
      ruleFormRef,
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
  margin-top: 25vh;
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