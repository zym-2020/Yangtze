<template>
  <div class="overview-wrapper">
    <div class="content-wrapper">
      <h1 class="overview-title">Overview</h1>
      <p class="overview-text">
        This is overview text.This is over-view text.This is over-view text.This
        is over-view text.This is over-view text.This is over-view text.This is
        over-view text.This is over-view text.This is over-view text.
      </p>
    </div>
  </div>
</template>

<script>
export default {
  name: "OverView",
};
</script>

<style lang='scss' scope>
div.overview-wrapper {
  height: 20%;
  background-image: url("../assets/njy_cut.jpg");
  background-position: bootom right;
  background-size: cover;

  div.content-wrapper {
    position: absolute;
    background-color: rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(6px);
    width: 30%;
    height: 40%;
    left: 35%;
    top: 14%;
    border-radius: 3em 3em 3em 3em;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    align-content: flex-start;

    h1.overview-title {
      color: rgb(244, 250, 255);
      font-size: 40px;
      padding: 3%;
      height: fit-content;
    }

    p.overview-text {
      color: rgb(240, 245, 255);
      font-size: 24px;
      width: 80%;
      height: 70%;
      line-height: 1.75;
    }
  }
}
</style>