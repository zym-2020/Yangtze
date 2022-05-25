<template>
  <div>
    <div class="head">
      <el-input v-model="search" placeholder="搜索" />
      <el-button type="primary" plain>搜索</el-button>
      <el-button type="info" plain @click="toAdd">创建共享条目</el-button>
    </div>
    <div class="body">
      <div v-for="(item, index) in fileList" :key="index">
        <div class="card">
          <data-card :fileInfo="updateKeys(item)">
            <template #creator>
              <div class="creator">
                <div style="line-height: 40px"><strong>创建人：</strong></div>
                <el-avatar :size="25" :src="getUserAvatar(item.userAvatar)" />
                <div class="name">{{ item.userName }}</div>
                <div class="btn">
                  <el-dropdown trigger="click">
                    <el-button size="small">
                      操作<el-icon class="el-icon--right"
                        ><arrow-down
                      /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          v-if="email === item.creator"
                          @click="operate(1, item)"
                          >编辑</el-dropdown-item
                        >
                        <el-dropdown-item command="2">下架</el-dropdown-item>
                        <el-dropdown-item command="3">删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </template>
          </data-card>
        </div>
      </div>
      <div class="pagination">
        <el-pagination background layout="prev, pager, next" :total="1000" />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent, onMounted, ref } from "vue";
import DataCard from "@/components/cards/DataCard.vue";
import router from "@/router";
import { pageQueryByAdmin } from "@/api/request";
import { useStore } from "@/store";
export default defineComponent({
  components: { DataCard },
  setup() {
    const activeName = ref("first");
    const search = ref("");
    const fileList = ref<any[]>([]);
    const store = useStore();
    const email = computed(() => {
      return store.state.user.email;
    });
    const toAdd = () => {
      router.push({ name: "share" });
    };

    const getUserAvatar = (url: string) => {
      return url != undefined && url != undefined && url != ""
        ? "http://localhost:8002" + url
        : "https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
    };

    const updateKeys = (object: any) => {
      const keyMap = {
        create_time: "createTime",
        origin_address: "originAddress",
        structured_source: "structuredSource",
        update_time: "updateTime",
        visual_source: "visualSource",
        visual_type: "visualType",
        origin_name: "originName",
        visual_name: "visualName",
        structured_name: "structuredName",
      };
      Object.keys(object).map((key) => {
        if (
          key === "create_time" ||
          key === "origin_address" ||
          key === "structured_source" ||
          key === "update_time" ||
          key === "visual_source" ||
          key === "visual_type" ||
          key === "origin_name" ||
          key === "visual_name" ||
          key === "structured_name"
        ) {
          const newKey = keyMap[key];
          object[newKey] = object[key];
          delete object[key];
        }
      });
      return object;
    };

    const operate = (param: number, fileInfo: any) => {
      if (param === 1) {
        router.push({
          name: "updateShare",
          params: {
            id: fileInfo.id,
          },
        });
      }
    };

    onMounted(async () => {
      const data = await pageQueryByAdmin("update_time", false, 0, 10);
      if (data != null) {
        fileList.value = data.data.list;
      }
    });

    return {
      activeName,
      search,
      toAdd,
      fileList,
      getUserAvatar,
      updateKeys,
      email,
      operate,
    };
  },
});
</script>

<style lang="scss" scoped>
.head {
  height: 70px;
  .el-input {
    width: 400px;
    margin-left: 30px;
    margin-top: 20px;
    margin-right: 20px;
  }
}
.body {
  margin-left: 20px;
  margin-right: 20px;
  .card {
    border: 1px solid #dcdfe6;
    box-sizing: border-box;
    border-radius: 6px;
    margin-bottom: 10px;
    padding: 10px;
    .creator {
      position: absolute;
      display: flex;
      right: 50px;
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

  .pagination {
    margin-top: 40px;
    margin-bottom: 40px;
    display: flex;
    justify-content: space-around;
  }
}
</style>