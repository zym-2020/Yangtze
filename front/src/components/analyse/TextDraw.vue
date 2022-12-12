<template>
  <div v-loading="loading" class="text-draw">
    <el-radio-group v-model="radio">
      <el-radio label="section" size="large">断面</el-radio>
      <el-radio label="region" size="large">区域</el-radio>
    </el-radio-group>
    <el-input
      v-model="textarea"
      resize="none"
      :rows="5"
      type="textarea"
      placeholder="例：116.401969,39.92069,116.401969,39.92069"
    />
    <div class="btn">
      <el-button type="primary" plain @click="btnClick">确定</el-button>
    </div>
  </div>
</template>

<script lang="ts">
import { uuid } from "@/utils/common";
import { notice } from "@/utils/notice";
import { defineComponent, ref } from "vue";
export default defineComponent({
  emits: ["createGeoJson"],
  setup(_, context) {
    const radio = ref("");
    const textarea = ref("");
    const loading = ref(false);

    const btnClick = () => {
      if (radio.value === "") {
        notice("warning", "警告", "请选择类型：断面/区域");
        return;
      } else {
        loading.value = true;
        const strList = textarea.value.split(",");
        if (
          strList.length % 2 != 0 &&
          strList.length <= 6 &&
          radio.value === "section" &&
          strList.length <= 2
        ) {
          notice("warning", "警告", "坐标检验不合格，请检查输入是否规范");
          loading.value = false;
          return;
        } else {
          if (
            radio.value === "region" &&
            strList[0] != strList[strList.length - 2] &&
            strList[1] != strList[strList.length - 1]
          ) {
            notice("warning", "警告", "坐标检验不合格，请检查输入是否规范");
            loading.value = false;
            return;
          } else {
            const result: number[][] = [];
            for (let i = 0; i < strList.length - 1; i = i + 2) {
              if (
                parseFloat(strList[i]) != NaN &&
                parseFloat(strList[i + 1]) != NaN &&
                parseFloat(strList[i]) >= -180 &&
                parseFloat(strList[i]) <= 180 &&
                parseFloat(strList[i + 1]) >= -90 &&
                parseFloat(strList[i + 1]) <= 90
              ) {
                let flag = true;
                for (let j = 0; j < result.length; j++) {
                  if (
                    parseFloat(strList[i]) == result[j][0] ||
                    parseFloat(strList[i + 1]) == result[j][1]
                  ) {
                    flag = false;
                    break;
                  }
                }
                if (flag) {
                  result.push([
                    parseFloat(strList[i]),
                    parseFloat(strList[i + 1]),
                  ]);
                } else {
                  loading.value = false;
                  notice(
                    "warning",
                    "警告",
                    "坐标检验不合格，请检查输入是否规范"
                  );
                  return;
                }
              } else {
                loading.value = false;
                notice("warning", "警告", "坐标检验不合格，请检查输入是否规范");
                return;
              }
            }
            let json;
            if (radio.value === "section") {
              json = {
                geometry: {
                  coordinates: result,
                  type: "LineString",
                },
                id: uuid(),
                type: "Feature",
                properties: {},
              };
              context.emit("createGeoJson", {
                geoJson: json,
                visualType: "geoJsonLine",
              });
            } else {
              json = {
                geometry: {
                  coordinates: [result],
                  type: "Polygon",
                },
                id: uuid(),
                type: "Feature",
                properties: {},
              };
              context.emit("createGeoJson", {
                geoJson: json,
                visualType: "geoJsonPolygon",
              });
            }
            loading.value = false;
          }
        }
      }
    };

    return {
      radio,
      textarea,
      btnClick,
      loading,
    };
  },
});
</script>

<style lang="scss" scoped>
.text-draw {
  padding: 0 10px 10px;
  .btn {
    text-align: center;
    margin-top: 10px;
  }
}
</style>