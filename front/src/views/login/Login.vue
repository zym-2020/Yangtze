<template>
  <el-form :model="form" label-width="120px">
    <el-form-item label="Email">
      <el-input v-model="form.email" />
    </el-form-item>
    <el-form-item label="Password">
      <el-input v-model="form.password" />
    </el-form-item>
  </el-form>
  <el-button type="primary" plain @click="click">Login</el-button>
</template>

<script lang="ts">
import { defineComponent, ref, reactive } from "vue";
import { useStore } from "@/store/index";
import router from '@/router'
export default defineComponent({
  setup() {
    const store = useStore();
    const form = reactive({
      email: "123@qq.com",
      password: "123",
    });
    const click = async () => {
      await store.dispatch("login", form)

      router.push({path: '/'})
    };
    return {
      form,
      click
    };
  },
});
</script>