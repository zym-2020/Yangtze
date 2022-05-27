// TODO: Make it a component
<template>
  <div class="single-stat" v-for="stat in tideStat" :key="stat.index">
    <h2>{{ stat.name }}</h2>
    <span class="static">水位：</span
    ><span class="dyn" :part="stat.index">{{ stat.val }}</span>
    <span class="static">时刻：</span
    ><span class="dyn" :part="stat.index">{{ stat.time }}</span>
  </div>
</template>

<script>
export default {};
</script>

<style lang='scss'>
div.stat-data-wrapper {
  background-color: rgba(255, 255, 255, 0.2);
  height: 45%;
  width: 23%;
  border-radius: 0.6em;

  div.drop {
    margin-top: 4%;
    margin-left: 3%;
    display: inline-block;
    width: 7em;
    height: 2em;
    line-height: 1.2em;
    padding: 0.2em 1.2em;
    background: rgba(0, 11, 68, 0.6);
    color: white;
    font-family: Tahoma;
    font-size: 1.2em;
    text-align: center;
    border: 2px solid rgba(0, 11, 68, 0.8);
    border-radius: 2em;
    cursor: default;
    transition: 0.5s;

    span::selection {
      background: rgba(255, 255, 255, 0.8);
      color: rgb(142, 194, 255);
    }
    &:active {
      position: relative;
      top: 2px;
    }
    &:hover {
      background: #2955a7;
      color: white;
    }

    span {
      padding-left: 0.2em;
      font-weight: bold;
    }
  }
  div.down {
    margin-top: 3%;
    display: inline-block;
    padding: 4px 8px 6px 4px;
    background: rgba(0, 11, 68, 0.8);
    font-family: Tahoma;
    font-size: 1.2em;
    border: 2px solid rgba(0, 11, 68, 0.8);
    border-radius: 8px;
    vertical-align: top;
    position: relative;
    transition: 0.5s;
    transform: scale(0) translateX(15em) translateY(-3em);
    z-index: 2000;

    &.opened {
      transform: scale(1) translateX(0) translateY(0);
    }

    ul {
      padding-left: 4px;
    }
    a {
      text-decoration: none;
    }
    li {
      list-style-type: none;
      margin-bottom: 6px;
      padding: 4px 20px;
      padding-right: 18px;
      color: white;
      border-radius: 6px;
      cursor: pointer;
      transition: 0.5s;
    }
    li:active {
      position: relative;
      top: 2px;
    }
    li:hover {
      background: rgba(255, 255, 255, 0.8);
      color: rgb(0, 28, 59);
    }
  }

  h2.stat-title {
    font-size: 2.2em;
    position: absolute;
    right: 1.2%;
    bottom: 40.2%;
    color: rgba(255, 255, 255, 0.9);
  }

  div.stats-container {
    width: 22%;
    height: 36%;
    position: absolute;
    right: 1em;
    bottom: 1em;
    background-color: transparent;
    display: flex;
    flex-flow: row wrap;
    justify-content: space-around;
    align-content: space-around;

    div.single-stat {
      width: 46%;
      height: 46%;
      background-color: rgba(221, 230, 255, 0.3);
      border-radius: 1em;
      display: flex;
      flex-flow: row wrap;
      align-items: flex-start;
      align-content: space-around;
      gap: 0em 0em;

      h2 {
        padding-top: 0.1em;
        width: 100%;
        font-size: 1.5em;
        color: #003ab8;
        text-align: center;
      }
      span {
        line-height: 1em;
        font-size: 1.8em;
        font-family: Impact, Haettenschweiler, "Arial Narrow Bold", sans-serif;
        transition: ease-in-out 0.5s;
        width: 40%;
        text-align: left;
        &.static {
          width: 50%;
          margin-left: 5%;
          text-align: right;
          color: aliceblue;
          font-size: 1.5em;
        }
        &.dyn {
          animation: ease-in-out 2s;
        }
        @each $index, $colorP in $colorMap {
          &[part="#{$index}"] {
            color: $colorP;
          }
        }
      }
    }
  }
}
</style>