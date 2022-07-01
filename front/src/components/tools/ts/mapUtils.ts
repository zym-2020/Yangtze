import mapBoxGl from "mapbox-gl";
import MapboxDraw from "@mapbox/mapbox-gl-draw";
import { useStore } from "@/store";
import { uuid } from '@/utils/common'
import { addSection, saveSectionContrastValue } from '@/api/request'

export const sectionUtil = (map: mapBoxGl.Map | undefined, draw: MapboxDraw, projectId: string, demId: string, demName: string, callback: (layer: any) => void) => {

    if (map?.loaded()) {
        map.addControl(draw, "top-left");
    } else {
        map?.on("load", () => {
            map.addControl(draw, "top-left");
        });
    }
    map?.on('draw.create', async () => {
        const coordinates = (draw.getAll().features[0].geometry as any).coordinates
        const uid = uuid()
        const layer = {
            id: uid,
            type: 'section',
            selectDemId: demId,
            selectDemName: demName,
            geoJson: {
                coordinates: coordinates as any[],
                type: 'LineString'
            }
        }

        callback(layer)
        draw.deleteAll()
        await addSection(layer, projectId)
    })
}

export const sectionContrastUtil = (map: mapBoxGl.Map | undefined, draw: MapboxDraw, projectId: string) => {
    const store = useStore()
    const json = JSON.parse(store.state.other.editState.state)
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
        const selectDemIds: string[] = json.selectDemIds
        const selectDemNames: string[] = json.selectDemNames
        const uid = uuid()
        analyse.sectionContrast.analysisResultList.push({
            id: uid,
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
        await store.dispatch("setResource", { projectJsonBean: { layerDataList: layerDataList, analyse: analyse }, id: projectId })
        draw.deleteAll()
        await saveSectionContrastValue({
            id: uid,
            lat1: coordinates[0][1] as number,
            lon1: coordinates[0][0] as number,
            lat2: coordinates[1][1] as number,
            lon2: coordinates[1][0] as number,
            demIds: selectDemIds,
            projectId: projectId
        })
    })
}