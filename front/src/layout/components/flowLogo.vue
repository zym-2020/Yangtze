<template>
  <div class="logo" @click="toHome">
    <h1 class="logo-text">NHRI</h1>
    <h1 class="logo-text">NHRI</h1>
  </div>
</template>

<script>
export default {};
</script>

<style lang="scss" scoped>
.logo {
  font-family: "Lato", "Verdana", sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-weight: 400;
  margin: 0;
  padding: 0;
}
.logo {
  float: left;
  margin-top: 0;
  font-size: 48px;
  line-height: 50px;
  color: #fff;
  &:hover {
    cursor: pointer;
  }
  h1 {
    &.logo-text {
      position: absolute;
      padding: 10px;
      color: #fff;
      font-size: 1em;
    }
    &:nth-child(1) {
      color: transparent;
      -webkit-text-stroke: 2px #03a9f4;
    }
    &:nth-child(2) {
      color: #03a9f4;
      animation: water-wave 5s infinite;
    }
  }
}

@keyframes water-wave {
  0%,
  100% {
    clip-path: polygon(
      0% 18%,
      15% 28%,
      32% 72%,
      54% 70%,
      70% 18%,
      84% 20%,
      100% 60%,
      100% 100%,
      0% 100%
    );
  }
  50% {
    clip-path: polygon(
      0 80%,
      16% 45%,
      34% 20%,
      51% 45%,
      67% 60%,
      84% 72%,
      100% 18%,
      100% 100%,
      0% 100%
    );
  }
}
</style>