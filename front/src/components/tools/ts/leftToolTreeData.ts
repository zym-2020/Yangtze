

export const treeData = [
    {
        id: "1",
        label: "河床演变",
        icon: "/river_bed_img/3DAnalystCreateSteepestPath32.png",
        children: [
            {
                id: "1-1",
                label: "断面形态",
                icon: "/river_bed_img/TinEditingTinLineAdd32.png",
                children: [
                    {
                        id: "1-1-1",
                        label: "任意断面",
                        icon: "/river_bed_img/ElementLine16.png",
                    },
                    {
                        id: "1-1-2",
                        label: "输入断面",
                        icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
                    },
                ],
            },
            {
                id: "1-2",
                label: "断面比较",
                icon: "/river_bed_img/3DAnalystInterpolateLine32.png",
                children: [
                    {
                        id: "1-2-1",
                        label: "任意断面",
                        icon: "/river_bed_img/ElementLine16.png",
                    },
                    {
                        id: "1-2-2",
                        label: "输入断面",
                        icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
                    },
                ],
            },
            {
                id: "1-3",
                label: "汊道断面对比",
                icon: "/river_bed_img/MapSendBehind32.png",
                children: [
                    {
                        id: "1-3-1",
                        label: "选择固定断面",
                        icon: "/river_bed_img/CadastralPRImage3_16.png",
                    },
                    {
                        id: "1-3-2",
                        label: "查看对比图",
                        icon: "/river_bed_img/3DAnalystInterpolateProfileGraphCreate16.png",
                    },
                ],
            },
            {
                id: "1-4",
                label: "断面面积冲淤",
                icon: "/river_bed_img/LayerServiceMap32.png",
                children: [
                    {
                        id: "1-4-1",
                        label: "任意断面",
                        icon: "/river_bed_img/ElementLine16.png",
                    },
                    {
                        id: "1-4-2",
                        label: "输入断面",
                        icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
                    },
                ],
            },
            {
                id: "1-5",
                label: "任意区域冲淤",
                icon: "/river_bed_img/3DAnalystInterpolatePolygon32.png",
                children: [
                    {
                        id: "1-5-1",
                        label: "任意矩形",
                        icon: "/river_bed_img/EditingRectangleTool16.png",
                    },
                    {
                        id: "1-5-2",
                        label: "任意圆形",
                        icon: "/river_bed_img/EditingCircleTool16.png",
                    },
                    {
                        id: "1-5-3",
                        label: "任意多边形",
                        icon: "/river_bed_img/EditingPolygonTool16.png",
                    },
                    {
                        id: "1-5-6",
                        label: "来自文本",
                        icon: "/river_bed_img/EditingSketchPropertiesWindowShow16.png",
                    },
                ],
            },
            {
                id: "1-6",
                label: "特定高程冲淤",
                icon: "/river_bed_img/RasterImageAnalysisPanSharpen32.png",
                children: [],
            },
            {
                id: "1-7",
                label: "冲淤等深线",
                icon: "/river_bed_img/RasterImageAnalysisOrthoRectify32.png",
                children: [],
            },
            {
                id: "1-8",
                label: "河道容积计算",
                icon: "/river_bed_img/MapPackageMPKFile32.png",
                children: [
                    {
                        id: "1-8-1",
                        label: "任意矩形",
                        icon: "/river_bed_img/EditingRectangleTool16.png",
                    },
                    {
                        id: "1-8-2",
                        label: "任意圆形",
                        icon: "/river_bed_img/EditingCircleTool16.png",
                    },
                    {
                        id: "1-8-3",
                        label: "任意多边形",
                        icon: "/river_bed_img/EditingPolygonTool16.png",
                    },
                    {
                        id: "1-8-4",
                        label: "来自文本",
                        icon: "/river_bed_img/EditingSketchPropertiesWindowShow16.png",
                    },
                ],
            },
            {
                id: "1-9",
                label: "河床坡度提取",
                icon: "/river_bed_img/Surface32.png",
                children: [],
            },
            {
                id: "1-10",
                label: "深泓线比较",
                icon: "/river_bed_img/SpatialAnalystContourTool32.png",
                children: [],
            },
            {
                id: "1-11",
                label: "等深线对比",
                icon: "/river_bed_img/CadastralCreateLineString32.png",
                children: [
                    {
                        id: "1-11-1",
                        label: "等深线对比",
                        icon: "/river_bed_img/ElementLine16.png",
                    },
                    {
                        id: "1-11-2",
                        label: "等高线对比",
                        icon: "/river_bed_img/EditingCreateCOGOAttributes16.png",
                    },
                ],
            },
            {
                id: "1-12",
                label: "边界分析",
                icon: "/river_bed_img/3DAnalystCreateSteepestPath32.png",
                children: [],
            },
        ],
    },
    {
        id: "2",
        label: "水文分析",
        icon: "/river_bed_img/3DAnalystCreateSteepestPath32.png",
        children: [],
    },
]

export const computedDataView = (data: any[]) => {
    const getIcon = (type: string) => {
        if (type === "riverBed") {
            return "#icon-raster";
        } else if (type === "hydrology") {
            return "#icon-vector";
        } else if (type === 'satellite') {
            return "#icon-tiff"
        }
    };
    const temp: any[] = [
        {
            name: "基础数据",
            children: []
        },
        {
            name: "分析结果",
            children: [
                {
                    name: "断面形态",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "断面比较",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "汊道断面对比",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "断面面积冲淤",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "任意区域冲淤",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "特定高程冲淤",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "冲淤等深线",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "河道容积计算",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "河床坡度提取",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "深泓线比较",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "等深线对比",
                    children: [],
                    type: 'classify'
                },
                {
                    name: "边界分析",
                    children: [],
                    type: 'classify'
                }
            ]
        }

    ]
    data.forEach(item => {
        if (item.type === 'riverBed' || item.type === 'hydrology' || item.type === 'satellite') {
            temp[0].children.push({
                id: item.id,
                name: item.name,
                type: item.type,
                icon: getIcon(item.type)
            })
        } else {
            if (item.type === 'section') {
                temp[1].children[0].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/TinEditingTinLineAdd32.png'
                })
            } else if(item.type === 'sectionContrast') {
                temp[1].children[1].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/3DAnalystInterpolateLine32.png'
                })
            } else if(item.type === 'branch') {
                temp[1].children[2].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/MapSendBehind32.png'
                })
            } else if(item.type === 'area') {
                temp[1].children[3].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/LayerServiceMap32.png'
                })
            } else if(item.type === 'anyArea') {
                temp[1].children[4].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/3DAnalystInterpolatePolygon32.png'
                })
            } else if(item.type === 'elev') {
                temp[1].children[5].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/RasterImageAnalysisPanSharpen32.png'
                })
            } else if(item.type === 'deep') {
                temp[1].children[6].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/RasterImageAnalysisOrthoRectify32.png'
                })
            } else if(item.type === 'volume') {
                temp[1].children[7].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/MapPackageMPKFile32.png'
                })
            } else if(item.type === 'slope') {
                temp[1].children[8].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/Surface32.png'
                })
            } else if(item.type === 'line') {
                temp[1].children[9].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/SpatialAnalystContourTool32.png'
                })
            } else if(item.type === 'deepContrast') {
                temp[1].children[10].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/CadastralCreateLineString32.png'
                })
            } else if(item.type === 'boundary') {
                temp[1].children[11].children.push({
                    id: item.id,
                    name: item.name,
                    type: item.type,
                    img: '/river_bed_img/3DAnalystCreateSteepestPath32.png'
                })
            }
        }
    })
    return temp

}