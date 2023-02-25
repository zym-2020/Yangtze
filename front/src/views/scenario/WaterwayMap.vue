<template>
  <div class="waterway">
    <head-notice :content="meteorologyList" />
    <div id="layer-control" :class="{ active: controlActive }">
      <el-row id="layer-panel">
        <el-col id="layers-drop" :span="19">
          <el-collapse v-model="activeList">
            <el-collapse-item title="数据图层" name="layers">
              <el-checkbox-group v-model="showLayerList" @change="ToggleLayer">
                <el-checkbox label="AIS船舶" />
                <el-checkbox label="航标" />
                <el-checkbox label="停泊区" />
                <el-checkbox label="锚地" />
                <el-checkbox label="桥梁" />
                <el-checkbox label="气象预警" />
                <el-checkbox label="水位站点" />
                <!-- <el-checkbox label="其他设施" /> -->
              </el-checkbox-group>
            </el-collapse-item>
          </el-collapse>
        </el-col>
        <el-col id="control-toggle" :span="5" @click="ToggleControl">
          <el-image id="toggle-image" :src="toggleURL" fit="scale-down" />
        </el-col>
      </el-row>
    </div>

    <div class="container" ref="container"></div>
    <ship-info :shipInfo="shipInfo" ref="shipWindow" />
    <bridge-info :bridgeInfo="bridgeInfo" ref="bridgeWindow" />
    <div ref="buoyWindow" class="buoyWindow">
      <buoy-info :buoyInfo="buoyInfo" />
    </div>
    <div ref="anchorWindow" class="anchorWindow">
      <anchor-info :anchorInfo="anchorInfo" />
    </div>
    <div ref="parkWindow" class="parkWindow">
      <park-info :parkInfo="parkInfo" />
    </div>
    <div ref="meteorologyWindow" class="MeteorologyInfo">
      <meteorology-info :meteorologyInfo="meteorologyInfo" />
    </div>
    <el-dialog v-model="stationDialog" :width="1250" :show-close="false">
      <station-info :stationInfo="stationInfo" v-if="stationDialog"/>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  onActivated,
  onDeactivated,
  onMounted,
  ref,
} from "vue";
import { Feature, Map, View } from "ol";
import TileGrid from "ol/tilegrid/TileGrid";
import { Tile as TileLayer, Vector as VectorLayer } from "ol/layer";
import VectorSource from "ol/source/Vector";
import XYZ from "ol/source/XYZ";
import { prefix } from "@/prefix";
import { defaults as defaultControls } from "ol/control";
import { Point, Polygon } from "ol/geom";
import {
  getBuoyByBox,
  getShipInfoByBoxAndTime,
  getAnchorInfoByBox,
  getParkInfoByBox,
  getBridgeInfo,
  getMeteorology,
  getStationByBox,
} from "@/api/request";
import { Style, Icon, Fill, Stroke } from "ol/style";
import { Ship, Buoy, Anchor, Park, Bridge, Meteorology, Station } from "@/type";
import Overlay from "ol/Overlay.js";
import ShipInfo from "@/components/scenePart/ShipInfo.vue";
import BuoyInfo from "@/components/scenePart/BuoyInfo.vue";
import AnchorInfo from "@/components/scenePart/AnchorInfo.vue";
import ParkInfo from "@/components/scenePart/ParkInfo.vue";
import BridgeInfo from "@/components/scenePart/BridgeInfo.vue";
import MeteorologyInfo from "@/components/scenePart/MeteorologyInfo.vue";
import HeadNotice from "@/components/scenePart/HeadNotice.vue";
import StationInfo from "@/components/scenePart/StationInfo.vue";

export default defineComponent({
  components: {
    ShipInfo,
    BuoyInfo,
    AnchorInfo,
    ParkInfo,
    BridgeInfo,
    MeteorologyInfo,
    HeadNotice,
    StationInfo,
  },
  setup() {
    let map: Map;
    let overlay: Overlay;
    let anchorOverlay: Overlay;
    let parkOverlay: Overlay;
    let meteorologyOverlay: Overlay;
    const CJresolutions = [
      0.023794610058302794, 0.0095178440233211186, 0.0047589220116605593,
      0.0023794610058302797, 0.0011897305029151398, 0.00059486525145756991,
      0.00029743262572878496, 0.00014871631286439248, 7.4358156432196239e-5,
      3.717907821609812e-5, 1.8590728838551974e-5, 9.2941746887730713e-6,
      4.6470873443865356e-6, 2.3794610058302796e-6,
    ];

    const timeArr: string[] = [];
    let count = 0;
    let t: any;

    const container = ref<HTMLElement>();
    const shipWindow = ref<HTMLElement>();
    const bridgeWindow = ref<HTMLElement>();
    const buoyWindow = ref<HTMLElement>();
    const anchorWindow = ref<HTMLElement>();
    const parkWindow = ref<HTMLElement>();
    const meteorologyWindow = ref<HTMLElement>();

    const stationDialog = ref(false);

    const controlActive = ref(false);
    const activeList = ref(["layers"]);
    const showLayerList = ref([
      "航标",
      "AIS船舶",
      "停泊区",
      "锚地", // "其他设施"
      "桥梁",
      "气象预警",
      "水位站点",
    ]);
    const shipInfo = ref<Ship>();
    const buoyInfo = ref<Buoy>();
    const anchorInfo = ref<Anchor>();
    const parkInfo = ref<Park>();
    const bridgeInfo = ref<Bridge>();
    const meteorologyInfo = ref<Meteorology>();
    const stationInfo = ref<Station>();

    const meteorologyList = ref<Meteorology[]>([]);
    let toggleURLList = ["./layer.png", "./angle-double-left.png"];
    let toggleUrlIndex = 0;
    let toggleURL = ref(toggleURLList[toggleUrlIndex]);

    const init = () => {
      for (let i = 0; i < 24; i++) {
        let hour = "";
        if (i < 10) {
          hour = "0" + i;
        } else {
          hour = "" + i;
        }
        timeArr.push(hour + ":00");
        timeArr.push(hour + ":20");
        timeArr.push(hour + ":40");
      }

      overlay = new Overlay({
        element: buoyWindow.value,
        offset: [
          document.body.clientWidth * -0.12,
          document.body.clientWidth * -0.11 - 20,
        ],
        autoPan: {
          animation: {
            duration: 250,
          },
        },
      });
      anchorOverlay = new Overlay({
        element: anchorWindow.value,
        offset: [
          document.body.clientWidth * -0.12,
          document.body.clientWidth * -0.08 - 20,
        ],
        autoPan: {
          animation: {
            duration: 250,
          },
        },
      });
      parkOverlay = new Overlay({
        element: parkWindow.value,
        offset: [
          document.body.clientWidth * -0.12,
          document.body.clientWidth * -0.08 - 20,
        ],
        autoPan: {
          animation: {
            duration: 250,
          },
        },
      });
      meteorologyOverlay = new Overlay({
        element: meteorologyWindow.value,
        offset: [-200, -240],
        autoPan: {
          animation: {
            duration: 250,
          },
        },
      });

      map = new Map({
        target: container.value,
        view: new View({
          center: [120.851, 31.864],
          projection: "EPSG:4326",
          zoom: 10,
          maxZoom: 18,
          minZoom: 5,
          multiWorld: true,
        }),
        overlays: [overlay, anchorOverlay, parkOverlay, meteorologyOverlay],
        layers: [
          new TileLayer({
            visible: true,
            source: new XYZ({
              // url: "http://t0.tianditu.com/DataServer?T=ter_c&x={x}&y={y}&l={z}&tk=2e23e92f7c9790018ab06498f1f55c1e",
              url: "http://t0.tianditu.com/DataServer?T=vec_c&x={x}&y={y}&l={z}&tk=35a94ab5985969d0b93229c30db6abd6",
              projection: "EPSG:4326",
            }),
          }),
          new TileLayer({
            visible: true,
            source: new XYZ({
              url: "http://t0.tianditu.com/DataServer?T=cva_c&x={x}&y={y}&l={z}&tk=35a94ab5985969d0b93229c30db6abd6",
              projection: "EPSG:4326",
            }),
          }),
          //海图图层
          new TileLayer({
            source: new XYZ({
              url: prefix + "multiSource/seaChart/map/{x}/{y}/{z}",
              tileGrid: new TileGrid({
                resolutions: CJresolutions,
                origin: [-400, 400],
                tileSize: 256,
              }),
              projection: "EPSG:4326",
            }),
          }),
          //   海图标注图层
          new TileLayer({
            source: new XYZ({
              url: prefix + "multiSource/seaChart/mark/{x}/{y}/{z}",
              tileGrid: new TileGrid({
                resolutions: CJresolutions,
                origin: [-400, 400],
                tileSize: 256,
              }),
              projection: "EPSG:4326",
            }),
          }),
          //   锚地图层（4）
          new VectorLayer({
            source: new VectorSource({
              features: [],
            }),
          }),
          //   停泊区图层（5）
          new VectorLayer({
            source: new VectorSource({
              features: [],
            }),
          }),
          //   桥梁图层(6)
          new VectorLayer({
            source: new VectorSource({
              features: [],
            }),
          }),
          //   浮标图层（7）
          new VectorLayer({
            source: new VectorSource({
              features: [],
            }),
          }),
          //   AIS船舶图层（8）
          new VectorLayer({
            source: new VectorSource({
              features: [],
            }),
          }),
          //   气象预警（9）
          new VectorLayer({
            source: new VectorSource({
              features: [],
            }),
          }),
          //水位站图层（10）
          new VectorLayer({
            source: new VectorSource({
              features: [],
            }),
          }),
        ],
        controls: defaultControls({
          zoom: false,
          rotate: false,
          attribution: false,
        }),
      });

      map.on("moveend", async () => {
        updateBuoy();
        updateShip();
        updateAnchor();
        updatePart();
        updateStation();
      });

      map.on("singleclick", (e) => {
        let flag = true;
        let click = map.forEachFeatureAtPixel(
          e.pixel,
          (param1, param2) => {
            if (flag) {
              flag = false;
              showInfo(param1.getProperties()["info"]);
            }
            return true;
          },
          {
            layerFilter: (layer) => {
              return layer.getVisible();
            },
            hitTolerance: 1,
          }
        );
        if (!click) {
          overlay.setPosition(undefined);
          anchorOverlay.setPosition(undefined);
          parkOverlay.setPosition(undefined);
          meteorologyOverlay.setPosition(undefined);
        }
      });

      map.on("pointermove", (e) => {
        const feature = map.forEachFeatureAtPixel(
          e.pixel,
          (val) => {
            return val;
          },
          {
            layerFilter: (layer) => {
              return layer.getVisible();
            },
            hitTolerance: 1,
          }
        );
        if (feature != undefined) {
          map.getTargetElement().style.cursor = "pointer";
        } else {
          map.getTargetElement().style.cursor = "auto";
        }
      });
    };

    const updateBuoy = async () => {
      if ((map.getView().getZoom() as number) > 12) {
        const coor = map.getView().calculateExtent(map.getSize());
        const data = await getBuoyByBox(coor[3], coor[2], coor[1], coor[0]);
        const features: Feature[] = [];
        (data as any).forEach((item: Buoy) => {
          const f = new Feature({
            geometry: new Point([item["jdwz_84jd"], item["jdwz_84wd"]]),
          });
          f.setStyle(
            new Style({
              image: new Icon({
                src: prefix + "multiSource/img/" + item["hbtlpz"],
              }),
            })
          );
          f.setProperties({
            info: item,
          });
          f.setId(item["id"]);
          features.push(f);
        });

        map.getAllLayers()[7].setSource(
          new VectorSource({
            features: features,
          })
        );
      } else {
        map.getAllLayers()[7].setSource(
          new VectorSource({
            features: [],
          })
        );
      }
    };

    const updateShip = async () => {
      function getSize(length: string, width: string) {
        const l = parseInt(length);
        const w = parseInt(width);
        if (l < 20 || w < 15) {
          return [20, 20];
        } else if (l > 100 || w > 50) {
          return [100, 50];
        } else {
          return [l, w];
        }
      }
      function filter() {
        if ((map.getView().getZoom() as number) < 13) {
          return 200;
        }
        if ((map.getView().getZoom() as number) < 14) {
          return 120;
        }
        if ((map.getView().getZoom() as number) < 15) {
          return 60;
        } else {
          return 0;
        }
      }
      if ((map.getView().getZoom() as number) > 12) {
        const coor = map.getView().calculateExtent(map.getSize());
        const data = await getShipInfoByBoxAndTime(
          coor[3],
          coor[2],
          coor[1],
          coor[0],
          timeArr[count],
          timeArr[(count + 1) % timeArr.length]
        );
        const features: Feature[] = [];

        if (data != null) {
          (data as any).forEach((item: Ship) => {
            if (parseInt(item["length"]) > filter()) {
              const f = new Feature({
                geometry: new Point([item["lon"], item["lat"]]),
              });
              f.setStyle(
                new Style({
                  image: new Icon({
                    src: "/ship-from-above-cut.png",
                    width: getSize(item["length"], item["width"])[1],
                    height: getSize(item["length"], item["width"])[0],
                    rotation: item["direction"],
                  }),
                })
              );
              f.setProperties({
                info: item,
              });
              f.setId(item["mmsi"]);
              features.push(f);
            }
          });
          map.getAllLayers()[8].setSource(
            new VectorSource({
              features: features,
            })
          );
        }
      } else {
        map.getAllLayers()[8].setSource(
          new VectorSource({
            features: [],
          })
        );
      }
    };

    const updateAnchor = async () => {
      if ((map.getView().getZoom() as number) > 12) {
        const coor = map.getView().calculateExtent(map.getSize());
        const data = await getAnchorInfoByBox(
          coor[3],
          coor[2],
          coor[1],
          coor[0]
        );
        const features: Feature[] = [];
        (data as any).forEach((item: Anchor) => {
          const f = new Feature({
            geometry: new Polygon(polygonUtil(item.qyfw.points)),
          });
          f.setStyle(
            new Style({
              fill: new Fill({
                color: [233, 13, 13, 0.4],
              }),
            })
          );
          f.setProperties({
            info: item,
          });
          f.setId(item["id"]);
          features.push(f);
        });

        map.getAllLayers()[4].setSource(
          new VectorSource({
            features: features,
          })
        );
      } else {
        map.getAllLayers()[4].setSource(
          new VectorSource({
            features: [],
          })
        );
      }
    };

    const updatePart = async () => {
      if ((map.getView().getZoom() as number) > 12) {
        const coor = map.getView().calculateExtent(map.getSize());
        const data = await getParkInfoByBox(coor[3], coor[2], coor[1], coor[0]);
        const features: Feature[] = [];
        (data as any).forEach((item: Park) => {
          if (item.qyfw.type === "polygon" && item.qyfw.points) {
            const f = new Feature({
              geometry: new Polygon(polygonUtil(item.qyfw.points)),
            });
            f.setStyle(
              new Style({
                fill: new Fill({
                  color: [0, 0, 0],
                }),
              })
            );
            f.setProperties({
              info: item,
            });
            f.setId(item["id"]);
            features.push(f);
          }
        });

        map.getAllLayers()[4].setSource(
          new VectorSource({
            features: features,
          })
        );
      } else {
        map.getAllLayers()[4].setSource(
          new VectorSource({
            features: [],
          })
        );
      }
    };

    const updateBridge = async () => {
      const data = await getBridgeInfo();

      if (data != null && (data as any).code === 0) {
        const features: Feature[] = [];
        data.data.forEach((item: Bridge) => {
          const f = new Feature({
            geometry: new Polygon(item.polygon.coordinates),
          });
          f.setStyle(
            new Style({
              fill: new Fill({
                color: [78, 14, 199, 0.7],
              }),
              stroke: new Stroke({
                width: 5,
              }),
            })
          );
          f.setProperties({
            info: item,
          });
          features.push(f);
        });

        map.getAllLayers()[6].setSource(
          new VectorSource({
            features: features,
          })
        );
      }
    };

    const updateMeteorology = async () => {
      const data = await getMeteorology();
      if (data != null && (data as any).code === 0) {
        meteorologyList.value = data.data;
        const features: Feature[] = [];
        data.data.forEach((item: Meteorology) => {
          const f = new Feature({
            geometry: new Point([item.longitude, item.latitude]),
          });
          f.setStyle(
            new Style({
              image: new Icon({
                src:
                  prefix +
                  "multiSource/getMeteorologyPng/" +
                  item.type +
                  ".png",
                width: 40,
                height: 30,
              }),
            })
          );
          f.setProperties({ info: item });
          features.push(f);
        });

        map
          .getAllLayers()[9]
          .setSource(new VectorSource({ features: features }));
      }
    };

    const updateStation = async () => {
      if ((map.getView().getZoom() as number) > 11) {
        const coor = map.getView().calculateExtent(map.getSize());
        const data = await getStationByBox(coor[3], coor[2], coor[1], coor[0]);
        const features: Feature[] = [];
        (data.data as any).forEach((item: Station) => {
          const f = new Feature({
            geometry: new Point([item["lon"], item["lat"]]),
          });
          f.setStyle(
            new Style({
              image: new Icon({
                src: "/水文站 (1).png",
                width: 30,
                height: 30,
              }),
            })
          );
          f.setProperties({
            info: item,
          });
          f.setId(item["name_en"]);
          features.push(f);
        });

        map.getAllLayers()[10].setSource(
          new VectorSource({
            features: features,
          })
        );
      } else {
        map.getAllLayers()[10].setSource(
          new VectorSource({
            features: [],
          })
        );
      }
    };

    const polygonUtil = (arr: number[][]) => {
      const result = [];
      if (arr.length === 2) {
        const left = arr[0][0];
        const right = arr[1][0];
        const top = arr[1][1];
        const bottom = arr[0][1];
        arr.splice(1, 0, [right, bottom]);
        arr.push([left, top]);
        arr.push(arr[0]);
      } else {
        if (
          arr[0][0] != arr[arr.length - 1][0] ||
          arr[0][1] != arr[arr.length - 1][1]
        ) {
          arr.push(arr[0]);
        }
      }
      result.push(arr);
      return result;
    };

    const setTime = () => {
      t = setTimeout(async () => {
        if ((map.getView().getZoom() as number) > 12) {
          await updateShip();
          count = (count + 1) % timeArr.length;
        }
        setTime();
      }, 3000);
    };

    const ToggleLayer = (val: string[]) => {
      const oj: { [key: string]: boolean } = {
        锚地: false,
        停泊区: false,
        桥梁: false,
        航标: false,
        AIS船舶: false,
        气象预警: false,
        水位站点: false,
      };
      val.forEach((item) => {
        if (oj[item] != undefined) {
          oj[item] = true;
        }
      });
      let temp = 4;
      for (let key in oj) {
        if (oj[key]) {
          map.getAllLayers()[temp].setVisible(true);
        } else {
          map.getAllLayers()[temp].setVisible(false);
        }
        temp++;
      }
    };
    const ToggleControl = () => {
      toggleUrlIndex = 1 - toggleUrlIndex;
      toggleURL.value = toggleURLList[toggleUrlIndex];
      controlActive.value = !controlActive.value;
    };

    const showInfo = (
      info: Buoy | Ship | Anchor | Park | Meteorology | Station
    ) => {
      if ("mmsi" in info) {
        shipInfo.value = info;
        (shipWindow.value as any).extend();
      } else if ("zxaqjl" in info) {
        buoyInfo.value = info;
        overlay.setPosition([info.jdwz_84jd, info.jdwz_84wd]);
      } else if ("mdmc" in info) {
        anchorInfo.value = info;
        anchorOverlay.setPosition([info.zbjd, info.zbwd]);
      } else if ("mc" in info) {
        parkInfo.value = info;
        parkOverlay.setPosition([info.zbjd, info.zbwd]);
      } else if ("polygon" in info) {
        bridgeInfo.value = info;
        (bridgeWindow.value as any).popupClick();
      } else if ("effective" in info) {
        meteorologyInfo.value = info;
        meteorologyOverlay.setPosition([info.longitude, info.latitude]);
      } else if ("keys_cn" in info) {
        stationInfo.value = info;
        stationDialog.value = true;
      }
    };

    onMounted(() => {
      init();
      updateBridge();
      updateMeteorology();
    });

    onDeactivated(() => {
      clearTimeout(t);
    });

    onActivated(() => {
      setTime();
    });

    return {
      container,
      controlActive,
      ToggleLayer,
      activeList,
      showLayerList,
      toggleURL,
      ToggleControl,
      shipInfo,
      shipWindow,
      buoyWindow,
      anchorWindow,
      parkWindow,
      bridgeWindow,
      meteorologyWindow,
      buoyInfo,
      anchorInfo,
      parkInfo,
      bridgeInfo,
      meteorologyInfo,
      stationInfo,
      meteorologyList,
      stationDialog,
    };
  },
});
</script>


<style lang="scss" scoped>
.waterway {
  height: 100%;
  position: relative;
  overflow: hidden;
  .head-notice {
    position: absolute;
    top: 0;
  }
  #layer-control {
    position: absolute;
    width: 15vw;
    height: 5vh;
    left: -12vw;
    top: 10vh;
    z-index: 8;
    background-color: rgba(242, 247, 255, 0.6);
    backdrop-filter: blur(2px);
    border-top-right-radius: 6px;
    border-bottom-right-radius: 6px;

    transition: 0.3s ease-in-out;

    &.active {
      left: 0vw;
    }

    .el-row {
      height: 5vh;

      .el-col {
        &#layers-drop {
          height: 5vh;
          /deep/ .el-collapse {
            height: 5vh;
            border-width: 0px;

            .el-collapse-item {
              height: 5vh;

              &.is-active {
                .el-collapse-item__wrap {
                  height: 20vh;
                  padding-left: 12px;
                  transition: 0.3s ease-in-out;
                  background-color: rgba(246, 251, 255, 0.95);
                  backdrop-filter: blur(1em);
                }
              }

              div {
                height: 5vh;
                border-width: 0px;
                transition: 0.3s ease-in-out;

                &.el-collapse-item__header {
                  font-size: 1vw;
                  font-weight: 600;
                  padding-left: 12px;
                }

                &.el-collapse-item__content {
                  height: 20vh;

                  .el-checkbox-group {
                    height: 20vh;
                    display: flex;
                    flex-flow: column;
                    justify-content: space-around;
                    .el-checkbox {
                      .el-checkbox__label {
                        font-size: 1vw;
                        color: rgb(136, 136, 136);
                      }

                      &.is-checked {
                        .el-checkbox__label {
                          color: rgb(4, 49, 88);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        &#control-toggle {
          // background-color:antiquewhite;
          height: 5vh;
          padding: 12px 18px;
          &:hover {
            cursor: pointer;
          }
        }
      }
    }
  }
  .container {
    height: 100%;
  }
}
/deep/.el-dialog {
  .el-dialog__header {
    padding: 0px;
  }
  .el-dialog__body {
    padding: 0;
  }
}
</style>