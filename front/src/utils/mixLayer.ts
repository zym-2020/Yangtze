import { IconLayer, PolygonLayer } from "@deck.gl/layers/typed";
import { CompositeLayer, Layer, LayersList, CompositeLayerProps, UpdateParameters } from "@deck.gl/core/typed";

const ICON_MAPPING = {
    shipMarker: {x: 0, y: 0, width: 512, height: 512, mask: false}, 
    parkMarker: {x: 0, y: 0, width: 256, height: 256, mask: false}, 
};

const layerDefaultProps = {
    ...PolygonLayer.defaultProps, 
    billboard: false, 
    pickable: true, 
    iconAtlas: 'http://172.21.212.10:8080/ship-park.png', // icon image url
    iconMapping: ICON_MAPPING, 
    sizeUnits: 'pixels', 
    sizeScale: 1, 
    getSize: 20, 
}

class MixLayer extends CompositeLayer {
    updateState(params: UpdateParameters<Layer<Required<CompositeLayerProps<any>>>>): void {
        const iconData = [];
        const pgData = [];
        for(const aRow of (params.props.data as Array<Record<string, any>>)) {
            if(aRow.qyfw["type"] == 'polygon') {
                pgData.push(aRow);
            }
            else {
                iconData.push(aRow);
            }
        }
        this.setState({iconData, pgData})
    }
    renderLayers(): LayersList | null {
        const {iconData, pgData} = this.state;
        const { updateTriggers, visible } = this.props;

        return [
            new IconLayer(this.getSubLayerProps({
                id: 'icon', 
                data: this.state.iconData, 
                getPosition: (d: any) => {
                    // console.log(d.qyfw.point);
                    return d.qyfw.point;
                },
                pickable: true,
                iconAtlas: 'http://172.21.212.10:8080/ship-park.png',
                iconMapping: ICON_MAPPING,
                getIcon: (d: any) => 'parkMarker', 
                billboard: true, 
                sizeUnits: 'meters', 
                sizeScale: 1, 
                getSize: (d: any) => 280,
                updateTriggers, 
                visible
            })), 
            new PolygonLayer(this.getSubLayerProps({
                id: 'polygon', 
                data: this.state.pgData, 
                getPolygon: (d: any) => {
                    const pgPts = d.qyfw.points;
                    if(pgPts[pgPts.length-1] !== pgPts[0]) {
                        pgPts.push(pgPts[0]);
                    }
                    return pgPts;
                }, 
                pickable: true,
                stroked: true,
                filled: true,
                getFillColor: [34, 34, 255, 190], 
                getLineWidth: 5,
            }))
        ];
    }
}

MixLayer.layerName = 'MixLayer';
// MixLayer.defaultProps = layerDefaultProps;

export {
    MixLayer, 
}
