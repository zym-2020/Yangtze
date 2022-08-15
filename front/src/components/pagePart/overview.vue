<template>
  <div class="overview-wrapper">
    <div class="content-wrapper">
      <h1 class="overview-title">概述</h1>
      <p class="overview-text">
        南京水利科学研究院简称南京水科院，是中国综合性水利科学研究机构、国家级社会公益类非营利性科研机构。主要从事基础理论、应用基础研究和高新技术开发，
        承担水利、交通、能源等领域中具有前瞻性、基础性和关键性的科学研究任务。
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "OverView",
};
</script>

<style lang='scss' scoped>
div.overview-wrapper {
  height: 58vh;
  background-image: url("../../assets/home/njy_cut.jpg");
  // background-position: bottom right;
  background-size: cover;

  div.content-wrapper {
    position: absolute;
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(6px);
    width: 30%;
    height: 40%;
    left: 35%;
    top: 14%;
    border-radius: 1em;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-content: flex-start;

    h1.overview-title {
      color: rgb(244, 250, 255);
      font-size: 40px;
      padding: 3%;
      height: fit-content;
      margin-top: 0;
      margin-bottom: 0;
    }

    p.overview-text {
      margin-top: 0.6em;
      color: rgb(240, 245, 255);
      font-size: 24px;
      width: 80%;
      height: 70%;
      line-height: 1.75;
      word-spacing: 0.1em;
    }
  }
}
</style>