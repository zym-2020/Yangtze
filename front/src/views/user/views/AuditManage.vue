<template>
  <div class="body">
    <div v-for="(item, index) in fileList" :key="index">
      <div class="card">
        <data-card :fileInfo="item">
          <template #status>
            <div v-if="item.status === 1" class="online">
              <el-tag type="success">Online</el-tag>
            </div>
            <div v-else class="offline">
              <el-tag type="info">审核中</el-tag>
            </div>
          </template>
          <template #creator>
            <div class="creator">
              <div class="btn">
                <el-dropdown trigger="click">
                  <el-button size="small">
                    操作<el-icon class="el-icon--right"><arrow-down /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item @click="operate(1, index)"
                        >查看</el-dropdown-item
                      >
                      <el-dropdown-item @click="operate(2, index)"
                        >同意上线</el-dropdown-item
                      >
                      <el-dropdown-item @click="operate(3, index)"
                        >拒绝上线</el-dropdown-item
                      >
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
          </template>
        </data-card>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import DataCard from "@/components/cards/DataCard.vue";
import { fuzzyQueryAdmin, updateStatusById } from "@/api/request";
import router from "@/router";
import { notice } from "@/utils/notice";
export default defineComponent({
  components: {
    DataCard,
  },
  setup() {
    const fileList = ref<any[]>([]);
    const toatl = ref(0);
    const getData = async (page: number) => {
      const data = await fuzzyQueryAdmin({
        page: page,
        size: 10,
        keyword: "",
        tags: [],
        property: "id",
        flag: false,
        type: "",
        status: 0,
      });
      if (data != null && (data as any).code === 0) {
        fileList.value = data.data.list;
        toatl.value = data.data.toatl;
      }
    };

    const operate = async (param: number, index: number) => {
      if (param === 1) {
        router.push({
          name: "shareFile",
          params: {
            id: fileList.value[index].id,
            fileInfo: JSON.stringify(fileList.value[index]),
          },
        });
      } else if (param === 2) {
        const data = await updateStatusById(
          fileList.value[index].id as string,
          1
        );
        if (data != null && (data as any).code === 0) {
          notice("success", "成功", "上线成功");
          fileList.value.splice(index, 1);

        }
      } else if (param === 3) {
        const data = await updateStatusById(
          fileList.value[index].id as string,
          -1
        );
        if (data != null && (data as any).code === 0) {
          notice("success", "成功", "拒绝上线");
          fileList.value.splice(index, 1);
        }
      }
    };

    onMounted(async () => {
      await getData(0);
    });
    return {
      fileList,
      operate,
    };
  },
});
</script>

<style lang="scss" scoped>
.body {
  margin-left: 20px;
  margin-right: 20px;
  padding-top: 20px;
  .card {
    cursor: pointer;
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    border-radius: 6px;
    margin-bottom: 10px;
    padding: 10px;
    .online,
    .offline {
      margin-left: 10px;
    }
    .creator {
      position: absolute;
      display: flex;
      right: 300px;
      .el-avatar {
        margin-top: 8px;
      }
      .name {
        line-height: 40px;
        margin: 0px 5px;
      }
      .btn {
        margin-top: 10px;
      }
    }
  }
}
</style>