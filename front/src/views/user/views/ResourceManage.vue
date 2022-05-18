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
          <data-card :fileInfo="item">
            <template #creator>
              <div class="creator">
                <div style="line-height: 40px"><strong>创建人：</strong></div>
                <el-avatar
                  :size="25"
                  src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
                />
                <div class="name">zym</div>
                <div class="btn">
                  <el-dropdown trigger="click">
                    <el-button size="small">
                      操作<el-icon class="el-icon--right"
                        ><arrow-down
                      /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item>编辑</el-dropdown-item>
                        <el-dropdown-item>整改</el-dropdown-item>
                        <el-dropdown-item>删除</el-dropdown-item>
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
import { defineComponent, onMounted, ref } from "vue";
import DataCard from "@/components/cards/DataCard.vue";
import router from "@/router";
import { pageQueryOrderByDownload } from "@/api/request";
export default defineComponent({
  components: { DataCard },
  setup() {
    const activeName = ref("first");
    const search = ref("");
    const fileList = ref<any[]>([]);
    const toAdd = () => {
      router.push({ name: "share" });
    };

    onMounted(async () => {
      const data = await pageQueryOrderByDownload(0, 10);
      if (data != null) {
        console.log(data.data);
        fileList.value = data.data.list
      }
    });

    return {
      activeName,
      search,
      toAdd,
      fileList
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
        margin-top: 10px;
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