import { PickingInfo } from '@deck.gl/core/typed'
import { Map } from 'mapbox-gl';
import { MjolnirGestureEvent } from "mjolnir.js"
import { Ref } from 'vue';

type ViewState = {
    latitude: number,
    longitude: number,
    zoom: number,
    pitch: number,
    bearing: number
};

type InfoStyle = {
    left: string, 
    bottom: string, 
    zIndex: number, 
    width: string, 
    height: string, 
    [key: string]: string | number
}

class PopupHelper {
    private _oldDxDic:{[key:string]: number};
    private _oldDyDic:{[key:string]: number};
    private _activeLayerId: string;
    private _curGeoPos: [number, number];
    constructor(
        private layerCardStyles: {[key:string]: Ref<InfoStyle>}, 
        private _map: Map
    ) {
        this._oldDxDic = {};
        this._oldDyDic = {};
        this._activeLayerId = '';
        this._curGeoPos = [0, 0];
        for(const key in layerCardStyles) {
            this._oldDxDic[key] = 0;
            this._oldDyDic[key] = 0;
        }
        
    }

    public handleClick(info: PickingInfo, event: MjolnirGestureEvent) {
        if(info.layer === null) {
            if(this._activeLayerId !== '') {
                this.layerCardStyles[this._activeLayerId].value.zIndex = -1;
                this._oldDxDic[this._activeLayerId] = 0;
                this._oldDyDic[this._activeLayerId] = 0;
                this._activeLayerId = '';
            }
            console.log('Clicked empty.');
            return;
        }
        else {
            const currentLayerID = info.layer.id;
            if(!(currentLayerID in this.layerCardStyles)) {
                return;
            }
            if(this._activeLayerId !== currentLayerID) {
                if(this._activeLayerId !== '') {
                    this.layerCardStyles[this._activeLayerId].value.zIndex = -1;
                }
                this.layerCardStyles[currentLayerID].value.zIndex = 5;
                this._activeLayerId = currentLayerID;
            }
            // if(info.pixel) {
            //     this.layerCardStyles[this._activeLayerId].value.left = 
            //         info.pixel[0] - 
            //         parseFloat(this.layerCardStyles[this._activeLayerId].value.width.split('px')[0]) / 2.0 
            //         + 15.0 + 'px';
            //     this.layerCardStyles[this._activeLayerId].value.bottom = info.pixel[1] + 18 + 'px';
            //     console.log(info);
            // }
            if(info.coordinate) {
                this._curGeoPos = [info.coordinate[0], info.coordinate[1]];
                const newPos = this._map.project(this._curGeoPos);
                this.layerCardStyles[this._activeLayerId].value.left = 
                    newPos.x - 
                    parseFloat(this.layerCardStyles[this._activeLayerId].value.width.split('px')[0]) / 2.0 
                    + 'px';
                this.layerCardStyles[this._activeLayerId].value.bottom = (this._map.getCanvas().clientHeight-newPos.y) + 18 + 'px';
            }
            
        }

        
    }

    public handleViewChange(viewState: ViewState) {
        if(this._activeLayerId == '') return;
        // console.log(viewState);
        if(viewState.zoom > 12) {
            const newPos = this._map.project(this._curGeoPos);
            this.layerCardStyles[this._activeLayerId].value.left = 
                newPos.x - 
                parseFloat(this.layerCardStyles[this._activeLayerId].value.width.split('px')[0]) / 2.0 
                + 'px';
            this.layerCardStyles[this._activeLayerId].value.bottom = (this._map.getCanvas().clientHeight-newPos.y) + 18 + 'px';
            this.layerCardStyles[this._activeLayerId].value.zIndex = 5;
           
        }
        else if(this.layerCardStyles[this._activeLayerId].value.zIndex !== -1) {
            this.layerCardStyles[this._activeLayerId].value.zIndex = -1;
        }
    }
}

export {
    InfoStyle, 
    ViewState, 
    PopupHelper
}
