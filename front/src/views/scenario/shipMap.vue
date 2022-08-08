<template>
<div id="map"></div>
<div id="stats"></div>
</template>

<script lang="ts">

import axios from 'axios';
import mapboxgl, { Map} from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';
import { getMap } from '../../utils/visualTools/mapSetting';
import Stats from 'three/examples/jsm/libs/stats.module';

import {tbvsSymbol, tbvsSymbols} from "../../utils/geoscratch/function/tbvs";
import { onMounted } from '@vue/runtime-dom';
import { TLayer } from "../../utils/visualTools/customLayer"
import { encodeFloatToDouble } from "../../utils/geoscratch/core/webglUtil/utils";

let map: Map;
let container: HTMLElement | null;
let stats: Stats;

// let zoomText: HTMLElement;

const sidewalk = [0, 27];
const bus = [27, 23];
const car = [50, 21];
const airport = [71, 4];
const train = [75, 13];
const ship = [88, 20];

// const symbolStyels = [sidewalk, bus, car, airport, train, ship];

// let clickPos = new THREE.Vector2();


export default {
    name: 'ShipMap',

    setup() {
        onMounted(async () => {
            container = document.getElementById( 'stats' ); 
            stats = new (Stats as any)();
            container?.appendChild( stats.dom );

            // zoomText = document.getElementById('zoom-level') as HTMLElement;

            map = getMap('pk.eyJ1IjoieWNzb2t1IiwiYSI6ImNsMWVsdnpxNDBzcDgzYnA0MDJrcW1hOXQifQ.-5KUoc4jAJbAcBEWgbMGSA', 
            {
                container: 'map', // container ID
                style:  'mapbox://styles/mapbox/streets-v11', // style URL
                center: [120.950697, 31.864162], // starting position [lng, lat]
                // center: [148.9819, -35.3981], // starting position [lng, lat]
                zoom: 8, // starting zoom,
                // pitch: 60,
                antialias: true
            });

            const shipData = await axios.get('http://172.21.212.254:8002/ship/QueryCode', { responseType: 'arraybuffer' })
                            .then((res) => {
                            const dataView = new DataView(res.data)
                            // console.log(dataView.byteLength / 25);
                            return dataView;
                            })
            // console.log(shipData.getInt32(12));
            const shipNum = shipData.byteLength / 25;

            const lonStart = shipNum * 4;
            const latStart = shipNum * 8;

            let aisPoints: Array<tbvsSymbol> = [];
            for (let i = 0; i < shipNum; i++) {
                const lon = shipData.getInt32(lonStart + i * 4) / 100000;
                const lat = shipData.getInt32(latStart + i * 4) / 100000;
                // console.log(lon, lat);
                let style = [88, 20];

                const position = mapboxgl.MercatorCoordinate.fromLngLat({lng:lat, lat:lon});
                const positionX = encodeFloatToDouble(position.x);
                const positionY = encodeFloatToDouble(position.y);
                aisPoints.push(new tbvsSymbol(0, style[0], style[1], [positionX[0], positionY[0], positionX[1], positionY[1]]));
            }
            // console.log(aisPoints);
            const aisMarkers = new tbvsSymbols(aisPoints);

            let cusLayer = new TLayer('tbvs', 'custom', '3d', aisMarkers);
 
            map.addLayer(cusLayer as mapboxgl.CustomLayerInterface);

            map.on('render', ()=> {
                stats.update();
            });
        });
    }
}
</script>

<style language='scss' scoped>

body { 
  margin: 0;
  padding: 0; 
}

#map { 
  position: absolute; 
  top: 0; bottom: 0; 
  width: 100%; 
}

</style>