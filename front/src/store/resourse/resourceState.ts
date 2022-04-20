export interface Resource {
    name: string
    id?: string
    type: string
    show?: boolean
    tableName?: string
    vectorType?: string
    geoJson?: { type: string, coordinates: [] },
}

export interface AnalyseResource {
    classifyCount: number;
    classify: string;
    analysisResultList: Resource[]
}

export interface Analyse {
    section: AnalyseResource;
    sectionContrast: AnalyseResource;
    area: AnalyseResource;
    boundary: AnalyseResource;
    branch: AnalyseResource;
    deep: AnalyseResource;
    deepContrast: AnalyseResource;
    elev: AnalyseResource;
    line: AnalyseResource;
    slope: AnalyseResource;
    volume: AnalyseResource;
    anyArea: AnalyseResource;
}

export interface ResourceState {
    layerDataList: Resource[]
    analyse: Analyse
}


export const state: ResourceState = {
    layerDataList: [],
    analyse: {
        section: { classifyCount: 0, classify: '断面形态', analysisResultList: [] },
        sectionContrast: { classifyCount: 0, classify: '断面比较', analysisResultList: [] },
        area: { classifyCount: 0, classify: '断面面积冲淤', analysisResultList: [] },
        boundary: { classifyCount: 0, classify: '边界分析', analysisResultList: [] },
        branch: { classifyCount: 0, classify: '汊道断面比较', analysisResultList: [] },
        deep: { classifyCount: 0, classify: '冲淤等深线', analysisResultList: [] },
        deepContrast: { classifyCount: 0, classify: '等深线比较', analysisResultList: [] },
        elev: { classifyCount: 0, classify: '特定高程冲淤', analysisResultList: [] },
        line: { classifyCount: 0, classify: '深泓线比较', analysisResultList: [] },
        slope: { classifyCount: 0, classify: '河床坡度提取', analysisResultList: [] },
        volume: { classifyCount: 0, classify: '河道容积计算', analysisResultList: [] },
        anyArea: { classifyCount: 0, classify: '任意区域冲淤', analysisResultList: [] }
    }
}