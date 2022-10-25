<template>
    <div>
        <div id="bot-text">
            水利部交通运输部国家能源局南京水利科学研究院<br>
            南科院联系电话：025-85828808
            &nbsp;地址：南京市广州路223号 　邮编：210029
        </div>
        <div id="img-logo"></div>
    </div>
</template>

<script>
export default {
    name: 'BottomInfo'
}
</script>

<style lang="scss" scoped>
div#bot-text {
    position: absolute;
    width: 42vw;
    height: 6vh;
    bottom: 0vh;
    right: 6vw;
    color: #e6e6e6;
    text-align: right;
    font-family: 'SimSun';
}

div#img-logo {
    position: absolute;
    width: 80px;
    height: 80px;
    right: 2vw;
    bottom: 0vh;
    background-image: url("../../assets/homePage/blueLogo.png");
}
</style>