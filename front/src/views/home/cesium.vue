<template>
  <div id="container" class="box">
    <div id="cesiumContainer"></div>
    <div id="toolbar">
      <button @click="showProvince" class="toolbarItem">中国行政区划</button>
      <button @click="showProvince2" class="toolbarItem">
        中国行政区划(界线)
      </button>
      <button @click="showLevel1" class="toolbarItem">一级地名</button>
      <button @click="showLevel2" class="toolbarItem">二级地名</button>
      <button @click="showLevel3" class="toolbarItem">三级地名</button>
      <button @click="showDock" class="toolbarItem">码头</button>
    </div>
  </div>
</template>

<script>
import * as Cesium from "cesium/Cesium";
import "../../../node_modules/cesium/Source/Widgets/widgets.css";
export default {
  name: "Cesium",
  data() {
    return {
      viewer: null,
      redBox: null,
      ellipse: null,
      province: null,
      province2: null,
      level1: null,
      level2: null,
      level3: null,
      dock: null,
      showProvinceButton: false,
      showProvinceButton2: false,
      showLevel1Button: false,
      showLevel2Button: false,
      showLevel3Button: false,
      showDockButton: false,
    };
  },
  mounted() {
    this.cesiumInit();
    // this.addBox();
    // this.addEllipse(); //纹理测试
    // this.add3DTiles();
    this.addGeoJSON_level1();
    this.addGeoJSON_level2();
    this.addGeoJSON_level3();
    this.addGeoJSON_dock();
    this.addProvince();
    this.addProvince2();
    // this.addShpfile();
  },
  methods: {
    cesiumInit() {
      Cesium.Ion.defaultAccessToken =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJqdGkiOiI5N2RjODQxYy0xNDI0LTRmNmYtOTJjNy02Njk3NGFmNGZlMzMiLCJpZCI6ODg1MTgsImlhdCI6MTY0OTI1MzIzNn0._0nz9pC6RF2dXjkzTilweCdZOP6jE-9Efc1QqjMOR_Q";
      this.viewer = new Cesium.Viewer("cesiumContainer", {
        terrainProvider: Cesium.createWorldTerrain(),
        //   geocoder: false,
        //   homeButton: false,
        sceneModePicker: false,
        // infobox: false,
        baseLayerPicker: false,
        navigationHelpButton: false,
        animation: false,
        timeline: false,
        fullscreenButton: false,
        vrButton: false,
      });
      // 提示错误Blocked script execution in ‘about:blank’ because the document’s frame is sandboxed and the ‘allow-scripts’ permission is not set.的解决方法
      let iframe = document.getElementsByClassName("cesium-infoBox-iframe")[0];
      iframe.setAttribute(
        "sandbox",
        "allow-same-origin allow-scripts allow-popups allow-forms"
      );
      iframe.setAttribute("src", ""); //必须设置src为空 否则不会生效
      this.viewer._cesiumWidget._creditContainer.style.display = "none"; // 隐藏版权
      this.viewer.scene.debugShowFramesPerSecond = true; //显示帧数
      // 不显示星空且背景透明
      this.viewer.scene.skyBox.show = true;
      this.viewer.scene.backgroundColor = new Cesium.Color(0.0, 0.0, 0.0, 0.5);
      // 视角大小
      Cesium.Camera.DEFAULT_VIEW_FACTOR = 0;
      // 设置home在中国(左下角，右上角)
      const ChinaRectangle = Cesium.Rectangle.fromDegrees(
        119.080032,
        30.959241,
        122.789387,
        32.389027
      );
      Cesium.Camera.DEFAULT_VIEW_RECTANGLE = ChinaRectangle;
      // this.viewer.camera.flyHome(8);
    },
    addBox() {
      this.redBox = this.viewer.entities.add({
        name: "Red box with black outline",
        position: Cesium.Cartesian3.fromDegrees(
          119.080032,
          30.959241,
          250000.0
        ),
        box: {
          dimensions: new Cesium.Cartesian3(400000.0, 300000.0, 500000.0),
          material: Cesium.Color.RED.withAlpha(0.5),
          outline: true,
          outlineColor: Cesium.Color.BLACK,
        },
      });
      this.viewer.zoomTo(this.viewer.entities);
    },
    addEllipse() {
      //方法一，构造时赋材质
      this.ellipse = this.viewer.entities.add({
        position: Cesium.Cartesian3.fromDegrees(-103.0, 40.0),
        ellipse: {
          semiMinorAxis: 250000.0,
          semiMajorAxis: 400000.0,
          // material: Cesium.Color.BLUE.withAlpha(0.5), //可设置不同的MaterialProperty
          // 棋盘纹理
          // material: new Cesium.CheckerboardMaterialProperty({
          //   evenColor: Cesium.Color.BLACK,
          //   oddColor: Cesium.Color.WHITE,
          //   repeat: new Cesium.Cartesian2(4, 4),
          // }),
          // 网格
          material: new Cesium.GridMaterialProperty({
            color: Cesium.Color.YELLOW,
            cellAlpha: 0.2,
            lineCount: new Cesium.Cartesian2(8, 8),
            lineThickness: new Cesium.Cartesian2(2.0, 2.0),
          }),
        },
      });
      // ellipse.material = new Cesium.GridMaterialProperty({
      //   color: Cesium.Color.YELLOW,
      //   cellAlpha: 0.2,
      //   lineCount: new Cesium.Cartesian2(8, 8),
      //   lineThickness: new Cesium.Cartesian2(2.0, 2.0),
      // });
      //完整的这么写
      // ellipse.material = new Cesium.ImageMaterialProperty({
      //   image: "../images/cats.jpg",
      //   color: Cesium.Color.BLUE,
      //   repeat: new Cesium.Cartesian2(4, 4),
      // });

      // //也可以简单的写成
      // ellipse.material = "../images/cats.jpg";
      // 棋盘纹理
      // this.ellipse.material = new Cesium.CheckerboardMaterialProperty({
      //   evenColor: Cesium.Color.BLACK,
      //   oddColor: Cesium.Color.WHITE,
      //   repeat: new Cesium.Cartesian2(4, 4),
      // });
      // 条纹纹理
      // this.ellipse.material = new Cesium.StripeMaterialProperty({
      //   evenColor: Cesium.Color.WHITE,
      //   oddColor: Cesium.Color.BLUE,
      //   repeat: 32,
      //   offset: 20,
      //   orientation: Cesium.StripeOrientation.VERTICAL,
      // });
      this.viewer.zoomTo(this.viewer.entities);
    },
    add3DTiles() {
      // 加载3DTiles数据
      this.viewer.scene.primitives.add(
        new Cesium.Cesium3DTileset({
          url: "data/tileset.json",
          // url: "data/level_3dtiles_clt/tileset.json",
          maximumScreenSpaceError: 2, //最大的屏幕空间误差
          maximumNumberOfLoadedTiles: 1000, //最大加载瓦片个数
          material: Cesium.Color.PINK.withAlpha(0.5),
        })
      );
    },
    addGeoJSON_level1() {
      // 加载GeoJSON数据
      let promise = Cesium.GeoJsonDataSource.load(
        "data/level1_Project_FeaturesToJSON.json",
        {
          stroke: Cesium.Color.HOTPINK,
          fill: Cesium.Color.PINK.withAlpha(0.5),
          // strokeWidth: 3,
          markerSymbol: "1",
        }
      );
      let that = this;
      promise.then(function (dataSource) {
        that.viewer.dataSources.add(dataSource);
        that.level1 = dataSource;
        that.level1.show = false;
      });
    },
    showLevel1() {
      if (!this.showLevel1Button) {
        this.level1.show = true;
        this.showLevel1Button = true;
      } else {
        this.level1.show = false;
        this.showLevel1Button = false;
      }
    },
    addGeoJSON_level2() {
      // 加载GeoJSON数据
      let promise = Cesium.GeoJsonDataSource.load(
        "data/level2_Project_FeaturesToJSON.json",
        {
          stroke: Cesium.Color.HOTPINK,
          fill: Cesium.Color.PINK.withAlpha(0.5),
          // strokeWidth: 3,
          markerSymbol: "2",
          markerColor: Cesium.Color.PINK.withAlpha(1),
        }
      );
      let that = this;
      promise.then(function (dataSource) {
        that.viewer.dataSources.add(dataSource);
        that.level2 = dataSource;
        that.level2.show = false;
      });
    },
    showLevel2() {
      if (!this.showLevel2Button) {
        this.level2.show = true;
        this.showLevel2Button = true;
      } else {
        this.level2.show = false;
        this.showLevel2Button = false;
      }
    },
    addGeoJSON_level3() {
      // 加载GeoJSON数据
      let promise = Cesium.GeoJsonDataSource.load(
        "data/level3_Project_FeaturesToJSON.json",
        {
          stroke: Cesium.Color.HOTPINK,
          fill: Cesium.Color.PINK.withAlpha(0.5),
          // strokeWidth: 3,
          markerSymbol: "3",
          markerColor: Cesium.Color.YELLOW.withAlpha(1),
        }
      );
      let that = this;
      promise.then(function (dataSource) {
        that.viewer.dataSources.add(dataSource);
        that.level3 = dataSource;
        that.level3.show = false;
      });
    },
    showLevel3() {
      if (!this.showLevel3Button) {
        this.level3.show = true;
        this.showLevel3Button = true;
      } else {
        this.level3.show = false;
        this.showLevel3Button = false;
      }
    },
    addGeoJSON_dock() {
      // 加载GeoJSON数据
      let promise = Cesium.GeoJsonDataSource.load(
        "data/dock_Project1_FeaturesToJSON.json",
        {
          stroke: Cesium.Color.HOTPINK,
          fill: Cesium.Color.PINK.withAlpha(0.5),
          // strokeWidth: 3,
        }
      );
      let that = this;
      promise.then(function (dataSource) {
        that.viewer.dataSources.add(dataSource);
        var entities = dataSource.entities.values;
        var colorHash = {};
        for (var i = 0; i < entities.length; i++) {
          var entity = entities[i];
          var colorNum = entity.properties.Color._value;
          var color = colorHash["Color_" + colorNum];
          if (!color) {
            color = Cesium.Color.fromRandom({
              alpha: 1.0,
            });
            colorHash["Color_" + colorNum] = color;
          }
          entity.polyline.material = color;
          entity.polyline.outline = false;
          entity.polyline.extrudedHeight = entity.properties.Elevation._value;
        }
        that.dock = dataSource;
        that.dock.show = false;
      });
    },
    showDock() {
      if (!this.showDockButton) {
        this.dock.show = true;
        this.showDockButton = true;
      } else {
        this.dock.show = false;
        this.showDockButton = false;
      }
    },

    addProvince() {
      //中国行政区划图
      let promise = new Cesium.GeoJsonDataSource().load(
        "data/CN_province.json",
        {
          // fill: Cesium.Color.fromAlpha(Cesium.Color.RED, 0), //设置填充透明
        }
      );
      let that = this;
      promise.then(function (dataSource) {
        that.viewer.dataSources.add(dataSource);
        var entities = dataSource.entities.values;
        var colorHash = {};
        // console.log(entities);
        for (var i = 0; i < entities.length; i++) {
          var entity = entities[i];
          var name = entity.name;
          var color = colorHash[name];
          if (!color) {
            color = Cesium.Color.fromRandom({
              alpha: 1.0,
            });
            colorHash[name] = color;
          }
          entity.polygon.material = color;
          entity.polygon.outline = false;
          entity.polygon.extrudedHeight = 5000.0;
        }
        that.province = dataSource;
        that.province.show = false;
      });
      console.log("行政区划图加载成功！");
    },
    addProvince2() {
      //中国行政区划图
      let promise = new Cesium.GeoJsonDataSource().load(
        "data/CN_province.json",
        {
          fill: Cesium.Color.fromAlpha(Cesium.Color.RED, 0.01), //设置填充透明
        }
      );
      let that = this;
      promise.then(function (dataSource) {
        that.viewer.dataSources.add(dataSource);
        that.province2 = dataSource;
        that.province2.show = false;
      });
      console.log("行政区划图2加载成功！");
    },
    showProvince() {
      if (!this.showProvinceButton) {
        this.province.show = true;
        this.showProvinceButton = true;
      } else {
        this.province.show = false;
        this.showProvinceButton = false;
      }
    },
    showProvince2() {
      if (!this.showProvinceButton2) {
        this.province2.show = true;
        this.showProvinceButton2 = true;
      } else {
        this.province2.show = false;
        this.showProvinceButton2 = false;
      }
    },
    addShpfile() {},
  },
};
</script>

<style lang="scss" scoped>
#cesiumContainer {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
  overflow: hidden;
}
.box {
  height: 100%;
  position: relative;
}
#toolbar {
  position: absolute;
  top: 0px;
  background-color: rgb(55, 55, 55);
  padding: 20px 20px;
  border-radius: 10%;
  display: flex;
  flex-direction: column;
}
.toolbarItem {
  margin-bottom: 10px;
}
</style>