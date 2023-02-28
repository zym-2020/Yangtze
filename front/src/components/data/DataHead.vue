<template>
  <div class="data-head">
    <el-row>
      <el-col :span="3">
        <div class="divider">
          <div class="mark"></div>
          <div class="text">
            <strong>{{ title }}</strong>
          </div>
        </div>
      </el-col>
      <el-col :span="21">
        <hr style="margin-top: 32px; color: #696969" />
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
export default defineComponent({
  props: {
    title: {
      type: String,
    },
  },
  setup(props) {
    const title = computed(() => {
      return props.title;
    });

    return {
      title,
    };
  },
});
</script>

<style lang="scss" scoped>
.data-head {
  .divider {
    margin-bottom: 10px;
    margin-top: 20px;
    height: 25px;
    display: flex;
    .mark {
      height: 25px;
      width: 8px;
      background: #458fd1;
    }
    .text {
      line-height: 25px;
      margin-left: 10px;
    }
  }
}
</style>