import mapboxgl from "mapbox-gl";

export default class mapUtils {
    map: mapboxgl.Map;
    constructor(mapboxMap: mapboxgl.Map) {
        this.map = mapboxMap;
    }

    ChangePaintProperty(layerId:string, propertyName: string, propertyVal: number) {
        this.map.setPaintProperty(layerId, propertyName, propertyVal);
    }

    ChangeCircleBlur(layerId: string, blur: number) {
        const layer = this.map.getLayer(layerId);
        if(layer.type === 'circle') {
            this.map.setPaintProperty(layerId, 'circle-blur', blur);
        }
        else {
            console.log("This is not a circle type layer!");
        }
    }
}