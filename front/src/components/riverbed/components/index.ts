import mapBoxGl from "mapbox-gl";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { useStore } from "@/store";
import { getCurrentProjectId } from '@/utils/project'
import { uuid } from '@/utils/common'
// import my_mode from './modes.js'

const store = useStore()

export class MapUtils {
    section_1 = (map: mapBoxGl.Map | undefined, draw: MapboxDraw) => {
        if (map?.loaded()) {
            map.addControl(draw, "top-right");
        } else {
            map?.on("load", () => {
                map.addControl(draw, "top-right");
            });
        }
        map?.on('draw.create', () => {
            const coordinates = (draw.getAll().features[0].geometry as any).coordinates
            console.log(coordinates)
            const layerDataList = store.state.resource.layerDataList
            const analyse = store.state.resource.analyse
            
            analyse.section.analysisResultList.push({
                id: uuid(),
                type: 'geoJson',
                show: true,
                name: '断面形态_' + analyse.section.classifyCount.toString(),
                geoJson: {
                    type: 'LineString',
                    coordinates: coordinates
                }
            })
            analyse.section.classifyCount++
            store.dispatch("setResource", {projectJsonBean: {layerDataList: layerDataList, analyse: analyse}, id: parseInt(getCurrentProjectId() as string)})
            draw.deleteAll()
            
        })
    }
}
