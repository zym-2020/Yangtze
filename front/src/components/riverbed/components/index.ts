import mapBoxGl from "mapbox-gl";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { useStore } from "@/store";
import { getCurrentProjectId } from '@/utils/project'
import { uuid } from '@/utils/common'
import { section, saveSectionContrastValue } from '@/api/request'
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
            console.log(store.state.other.dataSelect.id)
            const layerDataList = JSON.parse(JSON.stringify(store.state.resource.layerDataList))
            const analyse = JSON.parse(JSON.stringify(store.state.resource.analyse))
            
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
            await store.dispatch("setResource", {projectJsonBean: {layerDataList: layerDataList, analyse: analyse}, id: getCurrentProjectId() as string})
            draw.deleteAll()
            await section({
                DEMId: store.state.other.dataSelect.id,
                lat1: coordinates[0][1] as number,
                lon1: coordinates[0][0] as number,
                lat2: coordinates[1][1] as number,
                lon2: coordinates[1][0] as number,
                sectionName: '断面形态_' + (analyse.section.classifyCount - 1).toString(),
                projectName: getCurrentProjectName() as string
            })
        })
    }

    section_2 = (map: mapBoxGl.Map | undefined, draw: MapboxDraw) => {
        if (map?.loaded()) {
            map.addControl(draw, "top-right");
        } else {
            map?.on("load", () => {
                map.addControl(draw, "top-right");
            });
        }

        map?.on('draw.create', async () => {
            const coordinates = (draw.getAll().features[0].geometry as any).coordinates
            const layerDataList = JSON.parse(JSON.stringify(store.state.resource.layerDataList))
            const analyse = JSON.parse(JSON.stringify(store.state.resource.analyse))
            const selectDemIds: string[] = []
            const selectDemNames: string[] = []
            store.state.other.dataSelects.forEach(item => {
                selectDemIds.push(item.id)
                selectDemNames.push(item.name)
            })
            analyse.sectionContrast.analysisResultList.push({
                id: uuid(),
                type: 'geoJson',
                show: true,
                name: '断面比较_' + analyse.sectionContrast.classifyCount.toString(),
                geoJson: {
                    type: 'LineString',
                    coordinates: coordinates
                },
                selectDemIds: selectDemIds,
                selectDemNames: selectDemNames,
            })
            analyse.sectionContrast.classifyCount++
            await store.dispatch("setResource", {projectJsonBean: {layerDataList: layerDataList, analyse: analyse}, id: getCurrentProjectId() as string})
            draw.deleteAll()
            await saveSectionContrastValue({
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
