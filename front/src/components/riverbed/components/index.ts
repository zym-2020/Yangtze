import mapBoxGl from "mapbox-gl";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { useStore } from "@/store";
import { getCurrentProjectId } from '@/utils/project'
import { uuid } from '@/utils/common'
import { section } from '@/api/request'
import { getCurrentProjectName } from "@/utils/project";

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
        map?.on('draw.create', async () => {
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
                },
                selectDemId: store.state.other.dataSelect.id,
                selectDemName: store.state.other.dataSelect.name
            })
            analyse.section.classifyCount++
            await store.dispatch("setResource", {projectJsonBean: {layerDataList: layerDataList, analyse: analyse}, id: parseInt(getCurrentProjectId() as string)})
            draw.deleteAll()
            await section({
                DEMId: parseInt(store.state.other.dataSelect.id),
                lat1: coordinates[0][1] as number,
                lon1: coordinates[0][0] as number,
                lat2: coordinates[1][1] as number,
                lon2: coordinates[1][0] as number,
                sectionName: '断面形态_' + (analyse.section.classifyCount - 1).toString(),
                projectName: getCurrentProjectName() as string
            })
        })
    }
}
