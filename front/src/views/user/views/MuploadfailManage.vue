<template>
  <div class="data-card" v-show="n == 1">
    <div class="top" style="margin: 10px">
      <el-avatar :size="40" :src="avatar" />
      <div class="text">管理员</div>
      <slot name="creator"></slot>
    </div>
    <div>
      <el-row style="height: 200px">
        <el-col :span="10" :offset="6">
          <el-result
            icon="warning"
            title="上传资源失败了！请检查网络！"
            sub-title="请检查数据的完备性以及网络是否有异常！"
          >
            <!-- <template #extra>
          <el-button type="primary">Back</el-button>
        </template> -->
          </el-result>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="10" :offset="6">
          <div style="text-align: center">您的资源上传失败</div>
        </el-col>
      </el-row>
    </div>
    <div>
      <el-row>
        <div class="watch" style="margin: 10px">
          <strong>资源名称：</strong>
          <span> 冲淤数据</span>
        </div>
      </el-row>
      <el-row style="margin-left: 10px">
        <el-col :span="3">
          <div class="time">
            <strong>上传时间：</strong>
            <span>{{ supportTime }}</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="time">
            <strong>管理员审核时间：</strong>
            <span>{{ checkTime }}</span>
          </div>
        </el-col>
        <el-col :span="2">
          <div class="time">
            <el-icon color="green"><Select /></el-icon>
            <span>已审核</span>
          </div>
        </el-col>

        <el-col :span="4" :offset="11">
          <div class="time" style="float: right">
            <el-button type="primary" @click="n++"> 已知晓通知</el-button>
          </div>
        </el-col>
      </el-row>

      <div class="bottom-bottom" style="margin: 10px">
        <el-tag class="ml-2" type="success" style="margin-right: 10px"
          >水文</el-tag
        >
        <el-tag class="ml-2" type="info" style="margin-right: 10px"
          >地质</el-tag
        >
        <el-tag class="ml-2" type="warning" style="margin-right: 10px"
          >工程</el-tag
        >
        <el-tag class="ml-2" type="danger" style="margin-right: 10px"
          >气象</el-tag
        >
      </div>
    </div>
    <hr style="border-color: #d8d8d8; margin: 10px" />
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import { imgBase64 } from "@/utils/common";
export default defineComponent({
  setup() {
    const datatype = ref("气象类型");
    const avatar = ref("");
    const supportTime = ref("2022-5-18");
    const checkTime = ref("2022-5-19");
    const n = ref(1);
    const name = ref("冲淤数据");
    onMounted(() => {
      avatar.value = imgBase64("哈哈");
    });
    // function showinfo(){
    //        showinfos=false
    //        return showinfos
    // }
    return {
      avatar,
      datatype,
      supportTime,
      checkTime,
      n,
      name,
      // showinfos,
      // showinfo
    };
  },
});
</script>

<style lang="scss" scoped>
div1 {
  width: 100px;

  height: 20px;

  border: 1px solid red;

  float: left;
}
.data-card {
  margin-left: 10px;
  margin-right: 0px;
  border-radius: 30px;
  .top {
    display: flex;
    position: relative;
    height: 40px;
    line-height: 40px;
    .text {
      margin-left: 10px;
      font-size: 22px;
      color: #06090a;
    }
  }
  .des {
    margin-top: 15px;
  }
  .bottom {
    .bottom-top {
      display: flex;
      margin-top: 10px;
      .watch {
        margin-left: 20px;
        position: relative;
        .el-icon {
          position: absolute;
          top: 2px;
        }
        span {
          margin-left: 20px;
        }
      }
    }
    .bottom-bottom {
      margin-top: 10px;
      .el-tag {
        margin-right: 10px;
      }
    }
  }
}
</style>