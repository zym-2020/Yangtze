import mapboxgl from "mapbox-gl";

function MakeHSLStr(hslArray: [number, number, number]) {
    return 'hsl(' + String(hslArray[0]) + ',' + String(hslArray[1]) + '%' + ',' + String(hslArray[2]) + '%' + ')';
}

export default class MapUtil {
    map: mapboxgl.Map;

    constructor(curMap: mapboxgl.Map) {
        this.map = curMap;
    }

    GetLayer(lyrName: string) {
        let layer = this.map.getLayer(lyrName);
        // console.log(layer);
        if(layer === undefined) {
            throw console.error("Wrong layer ID or layer does't exist !!");
        }
        return layer;
    }

    async LineBlink(lyrName: string, repeat: number = 3, oneDur: number = 500, delay: number = 0) {
        let lyr = this.GetLayer(lyrName);
        if(lyr.type != 'line') {
            throw console.error("Wrong type layer, a line layer is required !!");
        }

        let width = this.map.getPaintProperty(lyrName, 'line-width');
        let color = this.map.getPaintProperty(lyrName, 'line-color');
        let hslArray = color.match(/\d+/g).map(Number);
        // console.log(hslArray);
        
        let newL = 90;
        if(hslArray[2] > 50) { newL = 15; }

        this.map.setPaintProperty(lyrName, 'line-width-transition', {'duration': oneDur, 'delay': delay});
        this.map.setPaintProperty(lyrName, 'line-blur-transition', {'duration': oneDur, 'delay': delay});
        let i = 0;
        while(i < repeat) {
            // this.map.setPaintProperty(lyrName, 'line-width', 2);
            this.map.setPaintProperty(lyrName, 'line-color', MakeHSLStr([hslArray[0], hslArray[1], newL]));
            await new Promise(r => setTimeout(r, 500));
            // this.map.setPaintProperty(lyrName, 'line-width', width);
            this.map.setPaintProperty(lyrName, 'line-color', color);
            await new Promise(r => setTimeout(r, 500));
            i++;
            console.log(i);
        }
    }
    
    async Fly2Pos(zoomLevel: number, centerPos: [number, number], flySpeed: number = 0.5, zoomCurve: number = 1) {
        if(centerPos[0] > 180 || centerPos[0] < -180) {
            throw console.error('Lon exceeded limits!!!');
        }
        else if(centerPos[1] > 90 || centerPos[1] < -90) {
            throw console.error('Lat exceeded limits!!!');
        }

        this.map.flyTo({zoom: zoomLevel, center: centerPos, speed: flySpeed, curve: zoomCurve});
    }

}