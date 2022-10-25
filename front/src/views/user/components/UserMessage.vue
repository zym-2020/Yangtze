<template>
  <el-container class="">
    <el-header width="200px">
      <el-menu
        :default-active="activeIndex2"
        class="el-menu-demo"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        @select="handleSelect"
        :ellipsis="false"
      >
        <el-menu-item index="1" @click="showweclomes">系统通知</el-menu-item>
        <el-sub-menu index="2">
          <template #title>管理员通知</template>
          <el-menu-item index="2-0" @click="showall">全部通知</el-menu-item>
          <el-menu-item index="2-1" @click="showyes">审核通过</el-menu-item>
          <el-menu-item index="2-2" @click="showfail">审核未通过</el-menu-item>
          <el-menu-item index="2-3" @click="showexamines"
            >正在审核</el-menu-item
          >
          <el-sub-menu index="2-4">
            <template #title>上传结果</template>
            <el-menu-item index="2-4-1">上传成功</el-menu-item>
            <el-menu-item index="2-4-2">上传失败</el-menu-item>
            <el-menu-item index="2-4-3">下载结果</el-menu-item>
          </el-sub-menu>
        </el-sub-menu>
        <el-menu-item index="3" @click="showHistoryMessage"
          >历史通知</el-menu-item
        >
      </el-menu>
    </el-header>

    <el-main>
      <el-scrollbar>
        <div v-for="(item, index) in fileList" :key="index">
          <msuess :fileInfo="item" :showDe="false"> </msuess>
        </div>
        <mweclome v-show="showweclome"></mweclome>
      </el-scrollbar>
    </el-main>
  </el-container>
</template>

<script lang="ts">
import { Menu as IconMenu, Message, Setting } from "@element-plus/icons-vue";
import { defineComponent, onMounted, ref, reactive, computed } from "vue";
import msuess from "@/views/user/views/MsuessManage.vue";
import { dateFormat } from "@/utils/common";
import mfail from "@/views/user/views/MfailManage.vue";
import mexamine from "@/views/user/views/MexamineManage.vue";
import muploadfail from "@/views/user/views/MuploadfailManage.vue";
import mweclome from "@/views/user/views/MweclomeManage.vue";
import mdownloadfail from "@/views/user/views/MdownloadfailManage.vue";
import {
  QueryByUserType,
  QueryByUserEmail,
  QueryHistoryMessage,
} from "@/api/request";
import NProgress from "nprogress";
export default defineComponent({
  components: {
    msuess,
    mfail,
    mexamine,
    muploadfail,
    mweclome,
    mdownloadfail,
  },
  setup() {
    const op = ref(false);
    //let nowTime=ref('');
    const activeIndex = ref("1");
    const activeIndex2 = ref("1");
    let showsuess = ref(false);
    let showfailinfo = ref(false);
    let showexamine = ref(false);
    let showuploadfailinfo = ref(false);
    let showweclome = ref(true);
    let showdownloadfailinfo = ref(false);
    const fileList = ref<any[]>([]);
    const handleSelect = (key: string, keyPath: string[]) => {
      console.log(key, keyPath);
    };
    const handleOpen = (key: string, keyPath: string[]) => {
      console.log(key, keyPath);
    };
    const handleClose = (key: string, keyPath: string[]) => {
      console.log(key, keyPath);
    };
    const showyes = async () => {
      NProgress.start();
      const property = ref("success");
      const data = await QueryByUserType(property.value);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
        }
        NProgress.done();
        showweclome.value = false;
      }
    };
    const showfail = async () => {
      NProgress.start();
      const property = ref("fail");
      const data = await QueryByUserType(property.value);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
        }
        NProgress.done();
        showweclome.value = false;
      }
    };

    const showexamines = async () => {
      NProgress.start();
      const property = ref("examine");
      const data = await QueryByUserType(property.value);
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
        }
        NProgress.done();
        showweclome.value = false;
      }
    };
    const showweclomes = async () => {
      showweclome.value = true;
      const data = await QueryByUserType("hhhhhh");
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
        }
      }
    };
    const showall = async () => {
      const data = await QueryByUserEmail();
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
        }
      }
    };
    const showHistoryMessage = async () => {
      showweclome.value = false;
      const data = await QueryHistoryMessage();
      if (data != null) {
        if ((data as any).code === 0) {
          fileList.value = data.data.list;
        }
      }
    };

    return {
      setInterval,
      handleOpen,
      handleClose,
      activeIndex,
      activeIndex2,
      handleSelect,
      showsuess,
      showexamine,
      showfailinfo,
      showuploadfailinfo,
      showdownloadfailinfo,
      showweclome,
      fileList,
      showyes,
      showfail,
      showexamines,
      showweclomes,
      showall,
      showHistoryMessage,
      op,
    };
  },
});
</script>

<style lang="scss" scoped>
</style>