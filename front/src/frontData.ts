/* eslint-disable prefer-const */
import * as echarts from "echarts";
import mapboxgl from "mapbox-gl";



export { frontData, commponentOpt, tideStat }

type StrKeyObject = {
    [key: string]: object | (()=>void) | echarts.EChartsOption | mapboxgl.MapboxOptions
};

type StrKeyObjects = {
    [key: string]: StrKeyObject[]
};


const hourInDay = [
    0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 
    15, 16, 17, 18, 19, 20, 21, 22, 23
]; 

const tideData = [
    [2.35, 2.19, 2.02, 1.88, 1.74, 1.59, 1.46, 1.39, 1.45, 1.70, 1.89, 1.89, 
    1.81, 1.70, 1.59, 1.51, 1.45, 1.38, 1.32, 1.32, 1.54, 2.05, 2.46, 2.65 ], 
    [1.54, 1.33, 1.12, 0.92, 0.77, 0.69, 0.80, 1.10, 1.36, 1.40, 1.31, 1.15, 
    1.00, 0.89, 0.78, 0.71, 0.62, 0.65, 0.91, 1.44, 1.89, 2.18, 2.26, 2.19 ], 
    [0.48, 0.26, 0.18, 0.37, 0.75, 1.03, 1.18, 1.18, 0.99, 0.73, 0.52, 0.35, 
    0.23, 0.17, 0.19, 0.42, 0.90, 1.37, 1.74, 1.94, 1.95, 1.80, 1.60, 1.37 ], 
    [0.83, 2.48, 3.59, 4.00, 3.75, 3.18, 2.63, 2.26, 1.90, 1.55, 1.20, 0.94, 
    0.80, 1.05, 2.95, 3.35, 3.30, 2.75, 2.30, 1.94, 1.60, 1.30, 1.00, 0.71 ]
];

const barMaxData = 5;
const pieMaxData = 60;
const drop_url = 'image://https://s1.ax1x.com/2022/05/19/OqSwp6.png';

function random(maxData: number, fixed: number) {
    return + (Math.random() * (maxData)).toFixed(fixed);
}

const barDynamicChange = (barChartFst: echarts.ECharts): void => {
    const dynamicData = [random(barMaxData, 2), random(barMaxData, 2), random(barMaxData, 2), random(barMaxData, 2)];
        barChartFst.setOption({
            series: [
                {
                    data: dynamicData.slice()
                },
                {
                    data: dynamicData.slice()
                }
            ]
        });
}

const pieDynamicChange = (pieChartFst: echarts.ECharts): void => {
    const dynamicData = [random(pieMaxData, 0), random(pieMaxData, 0), random(pieMaxData, 0), random(pieMaxData, 0)];
    pieChartFst.setOption({
        series: [
            {data: [{ value: 10 + dynamicData[3], name: 'A' },
                    { value: 10 + dynamicData[1], name: 'B' },
                    { value: 10 + dynamicData[0], name: 'C' },
                    { value: 10 + dynamicData[2], name: 'D' },
                ].sort(function (a, b) {
                    return b.value - a.value;
                })
            }, 
        ], 
    });
}

function dynamicMap1(map: mapboxgl.Map): void {
    let tick = 1.0;
    setInterval(()=> {
        map.setPaintProperty('monitors_station_symbol', 'circle-blur', 0.6 + tick/20);
        tick = (tick+1.0) % 10;
    }, 100);
}

function getVirtulData(year: string) {
    year = year || '2022';
    const date = +echarts.number.parseDate(year + '-01-01');
    const end = +echarts.number.parseDate(+year + 1 + '-01-01');
    const dayTime = 3600 * 24 * 1000;
    const data = [];
    for (let time = date; time < end; time += dayTime) {
        data.push([
            echarts.format.formatTime('yyyy-MM-dd', time),
            Math.floor(Math.random() * 1000)
        ]);
    }
    return data;
}

const frontData: StrKeyObjects = {
    'maps': [
        {
            'initStyle': {
                container: 'map', 
                style: 'mapbox://styles/johnnyt/cl39v9pcv009614qp3gnmcpos', 
                center: [121.180, 31.723], 
                zoom: 10.5,
            },
            'dynamicFunc': dynamicMap1, 
        },
        {
            'initStyle': {},
            'dynamicFunc': {}, 
        },
        {
            'initStyle': {},
            'dynamicFunc': {}, 
        },
    ],
    'charts': [
        {
            'chartOpt': {
                title: {
                    text: '潮位站潮位',
                    left: 15, 
                    top: 10, 
                    textStyle: {
                        color: 'rgba(255, 255, 255, 0.8)', 
                        fontWeight: 'bolder', 
                        fontFamily: 'Microsoft YaHei', 
                        fontSize: 32, 
                    }
                }, 
                tooltip: {
                    show: true, 
                },
                xAxis: {
                    max: barMaxData,
                    splitLine: { show: false },
                    offset: -10,
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    },
                    axisLabel: {
                        margin: 4, 
                        fontSize: 14
                    }, 
                    z: 25
                },
                yAxis: {
                    data: ['A', 'B', 'C', 'D'],
                    inverse: true,
                    axisTick: { show: false },
                    axisLine: { show: false },
                    axisLabel: {
                        margin: 10,
                        color: '#fff',
                        fontSize: 16, 
                        fontFamily: 'Microsoft YaHei'
                    }, 
                },
                grid: {
                    top: 65,
                    height: 150,
                    left: 50,
                    right: 100
                },
                series: [
                    {
                        z: 10, 
                        // current data
                        type: 'pictorialBar',
                        symbol: drop_url,
                        symbolRepeat: true,
                        symbolMargin: '5%',
                        symbolClip: true,
                        symbolSize: 24,
                        symbolBoundingData: barMaxData,
                        data: [2.35, 2.17, 1.56, 1.03],
                        markLine: {
                            z: 15, 
                            symbol: 'none',
                            label: {
                                formatter: 'max: {c}',
                                position: 'end', 
                                shadowBlur: 0, 
                                color: 'white', 
                                textBorderColor: 'white', 
                                fontSize: 12, 
                                distance: 6, 
                            },
                            lineStyle: {
                                color: 'white',
                                type: 'dotted',
                                opacity: 1,
                                width: 2
                            },
                            data: [
                                {
                                    type: 'max'
                                }
                            ]
                        },
                    },
                    {
                        // full data
                        type: 'pictorialBar',
                        itemStyle: {
                            opacity: 0.3
                        },
                        label: {
                            show: true,
                            formatter: function (params: any) {
                                return params.value;
                            },
                            position: 'right',
                            offset: [10, 0],
                            color: 'white',
                            fontSize: 16
                        },
                        animationDuration: 0,
                        symbolRepeat: 'fixed',
                        symbolMargin: '5%',
                        symbol: drop_url,
                        symbolSize: 24,
                        symbolBoundingData: barMaxData,
                        data: [2.35, 2.17, 1.56, 1.03],
                        z: 5
                    }
                ]
            }, 
            'dynamicFunc': barDynamicChange
        }, 
        {
            'chartOpt': {
                title: {
                    text: '测站统计',
                    left: 10, 
                    top: 10, 
                    textStyle: {
                        color: 'rgba(255, 255, 255, 0.8)', 
                        fontWeight: 'bolder', 
                        fontFamily: 'Microsoft YaHei', 
                        fontSize: 32, 
                    }
                }, 
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    orient: 'vertical', 
                    bottom: 16, 
                    right: 10, 
                    backgroundColor: 'rgba(255, 255, 255, 0.2)',
                    borderRadius: 10, 
                    show: true, 
                    padding: 10, 
                    textStyle: {
                        color: 'white', 
                        fontWeight: 'bold'
                    }
                },
                visualMap: {
                    show: false,
                    min: 5,
                    max: 90,
                    inRange: {
                        colorLightness: [0, 1]
                    }
                },
                series: [
                    {
                        name: '潮位站统计',
                        type: 'pie',
                        radius: [25, 150],
                        center: ['45%', '52%'],
                        roseType: 'radius',
                        avoidLabelOverlap: false,
                        itemStyle: {
                            borderRadius: 8, 
                            color: '#415CC2',
                            shadowBlur: 200,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        },
                        label: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            label: {
                                show: true,
                                fontSize: '24',
                                fontWeight: 'bold', 
                                borderColor: 'black', 
                                color: 'white'
                            }
                        },
                        data: [
                            { value: 60, name: 'A' },
                            { value: 36, name: 'B' },
                            { value: 47, name: 'C' },
                            { value: 29, name: 'D' },
                        ].sort(function (a, b) {
                            return b.value - a.value;
                        }),
                    }
                ]
            }, 
            'dynamicFunc': pieDynamicChange
        }, 
        {
            'chartOpt': {
                title: {
                    text: '2022年4月数据',
                    left: 10, 
                    top: 10, 
                    textStyle: {
                        color: 'rgba(255, 255, 255, 0.8)', 
                        fontWeight: 'bolder', 
                        fontFamily: 'Microsoft YaHei', 
                        fontSize: 32, 
                    }
                }, 
                tooltip: {
                    position: 'top', 
                }, 
                visualMap: {
                    min: 0, 
                    max: 1000, 
                    calculable: true,
                    orient: 'vertical', 
                    inverse: true, 
                    seriesIndex: [0], 
                    inRange: {
                        color: ['#2146AD', '#89AAE0'], 
                        symbolSize: [3, 30]
                    }, 
                    textStyle: {
                        color: 'rgba(255, 255, 255, 0.9)'
                    }, 
                    textGap: 0,
                    right: 2, 
                    bottom: 20, 
                    align: 'top', 
                    itemHeight: 180, 
                    itemWidth: 30
                }, 
                calendar: [{
                    orient: 'vertical', 
                    yearLabel: {
                        show: false, 
                    }, 
                    monthLabel: {
                        margin: 6,
                        fontSize: 18, 
                        color: "rgba(255, 255, 255, 0.9)", 
                    }, 
                    dayLabel: {
                        firstDay: 1, 
                        margin: 4, 
                        nameMap: 'cn', 
                        color: "rgba(255, 255, 255, 0.9)", 
                    }, 
                    itemStyle: {
                        color: 'rgba(6, 31, 71, 0.75)', 
                    }, 
                    cellSize: 45, 
                    range: '2022-4', 
                    right: 84,
                    bottom: 20
                }], 
                series: {
                    type: 'effectScatter',
                    coordinateSystem: 'calendar',
                    calendarIndex: 0,
                    symbolSize: function (val: any) {
                        return val[1] / 40;
                    },
                    itemStyle: {
                        color: '#415CC2', 
                    }, 
                    data: getVirtulData('2022')
                }
            }
        }, 
        {
            'chartOpt': {
                title: {
                    text: '2022-4-20测站潮位值',
                    left: 10, 
                    top: 10, 
                    textStyle: {
                        color: 'rgba(255, 255, 255, 0.8)', 
                        fontWeight: 'bolder', 
                        fontFamily: 'Microsoft YaHei', 
                        fontSize: 32, 
                    }
                }, 
                legend: {
                    orient: 'horizontal', 
                    top: 8, 
                    right: 28, 
                    backgroundColor: 'rgba(255, 255, 255, 0.2)',
                    borderRadius: 10, 
                    show: true, 
                    padding: 10, 
                    textStyle: {
                        color: 'white', 
                        fontSize: 14, 
                        fontWeight: 'bold'
                    }, 
                    data: [
                        {name: 'A', lineStyle: {color: '#C72212'}, itemStyle: {color: '#C72212'}}, 
                        {name: 'B', lineStyle: {color: '#08C719'}, itemStyle: {color: '#08C719'}}, 
                        {name: 'C', lineStyle: {color: '#D6C100'}, itemStyle: {color: '#D6C100'}}, 
                        {name: 'D', lineStyle: {color: '#123CC7'}, itemStyle: {color: '#123CC7'}}, 
                    ]
                },
                visualMap: [
                    {
                        show: false,
                        type: 'continuous',
                        seriesIndex: 0,
                        min: Math.min.apply(null, tideData[0]),
                        max: Math.max.apply(null, tideData[0]), 
                        inRange: {
                            color: ['#FFE1DE', '#C72212']
                        }
                    }, 
                    {
                        show: false,
                        type: 'continuous',
                        seriesIndex: 1,
                        min: Math.min.apply(null, tideData[1]),
                        max: Math.max.apply(null, tideData[1]), 
                        inRange: {
                            color: ['#D4FFD0', '#08C719']
                        }
                    }, 
                    {
                        show: false,
                        type: 'continuous',
                        seriesIndex: 2,
                        min: Math.min.apply(null, tideData[2]),
                        max: Math.max.apply(null, tideData[2]), 
                        inRange: {
                            color: ['#FFF9C3', '#D6C100']
                        }
                    }, 
                    {
                        show: false,
                        type: 'continuous',
                        seriesIndex: 3,
                        min: Math.min.apply(null, tideData[3]),
                        max: Math.max.apply(null, tideData[3]), 
                        inRange: {
                            color: ['#49A6FF', '#123CC7']
                        }
                    }, 
                ],
                tooltip: {  
                    show: true, 
                },
                xAxis: {
                    data: hourInDay, 
                    axisLine: {
                        lineStyle: {
                            color: 'rgba(255, 255, 255, 0.6)'
                        }
                    },
                    axisLabel: {
                        margin: 8, 
                        fontSize: 14, 
                        color: '#fff'
                    }, 
                }, 
                yAxis: {
                    axisLine: {
                        lineStyle: {
                            color: '#fff'
                        }
                    },
                    splitLine: {
                        lineStyle: {
                            color: 'rgba(255, 255, 255, 0.2)'
                        }
                    }, 
                    axisLabel: {
                        margin: 4, 
                        fontSize: 16
                    }, 
                }, 
                grid: {
                    bottom: 30, 
                    left: 45, 
                    right: 30
                }, 
                series: [
                    {
                        name: 'A', 
                        type: 'line', 
                        symbol: 'circle',
                        symbolSize: 6, 
                        data: tideData[0]
                    }, 
                    {
                        name: 'B', 
                        type: 'line', 
                        symbol: 'triangle', 
                        symbolSize: 6, 
                        data: tideData[1]
                    }, 
                    {
                        name: 'C', 
                        type: 'line', 
                        symbol: 'diamond',
                        symbolSize: 6, 
                        data: tideData[2]
                    }, 
                    {
                        name: 'D', 
                        type: 'line', 
                        symbol: 'rect',
                        symbolSize: 6, 
                        data: tideData[3]
                    },
                ]
            }
        }, 
    ], 
    'tables': [
        {
            'tHead': ['Time', 'A', 'B', 'C', 'D'], 
            'tBody': [
                {datA:'2.35', datB:'0.48', datC:'2.16', datD:'1.72', index:'0'}, 
                {datA:'2.19', datB:'0.26', datC:'1.93', datD:'1.51', index:'1'}, 
                {datA:'2.02', datB:'0.18', datC:'1.72', datD:'1.28', index:'2'}, 
                {datA:'1.88', datB:'0.37', datC:'1.56', datD:'1.08', index:'3'}, 
                {datA:'1.74', datB:'0.75', datC:'1.41', datD:'0.92', index:'4'}, 
                {datA:'1.59', datB:'1.03', datC:'1.30', datD:'0.81', index:'5'}, 
                {datA:'1.46', datB:'1.18', datC:'1.22', datD:'0.73', index:'6'}, 
                {datA:'1.39', datB:'1.18', datC:'1.25', datD:'0.96', index:'7'}, 
                {datA:'1.45', datB:'0.99', datC:'1.46', datD:'1.21', index:'8'}, 
                {datA:'1.70', datB:'0.73', datC:'1.72', datD:'1.44', index:'9'}, 
                {datA:'1.89', datB:'0.52', datC:'1.78', datD:'1.42', index:'10'}, 
                {datA:'1.89', datB:'0.35', datC:'1.70', datD:'1.33', index:'11'}, 
            ]
        }, 

    ], 
};

type StrObject = {
    [key: string]: string
};

const commponentOpt: StrObject[][] = [
    [{name:'断面形态', funcId:'0', index:'0'}, {name:'断面冲淤', funcId:'1', index:'0'}, 
    {name:'深泓线', funcId:'2', index:'0'}, {name:'等深线', funcId:'3', index:'0'}, {name:'河道边界', funcId:'4', index:'0'}], 
];

type TideData = {
    [key: string]: {name: string, val: string, time: string, index: string}[]
}

const tideStat: TideData[] = [
    {
        'A': [
            {name:'高潮', val:'2.65', time:'23:00', index:'0'}, 
            {name:'低潮', val:'1.32', time:'19:00', index:'1'}, 
            {name:'高低潮', val:'1.39', time:'7:00', index:'2'}, 
            {name:'低高潮', val:'1.89', time:'10:00', index:'3'}
        ], 
        'B':[
            {name:'高潮', val:'2.26', time:'22:00', index:'0'}, 
            {name:'低潮', val:'0.62', time:'16:00', index:'1'}, 
            {name:'高低潮', val:'0.69', time:'5:00', index:'2'}, 
            {name:'低高潮', val:'1.40', time:'9:00', index:'3'}
        ], 
        'C':[
            {name:'高潮', val:'1.95', time:'20:00', index:'0'}, 
            {name:'低潮', val:'0.17', time:'13:00', index:'1'}, 
            {name:'高低潮', val:'0.18', time:'2:00', index:'2'}, 
            {name:'低高潮', val:'1.18', time:'6:00', index:'3'}
        ], 
        'D':[
            {name:'高潮', val:'4.00', time:'3:00', index:'0'}, 
            {name:'低潮', val:'0.71', time:'23:00', index:'1'}, 
            {name:'高低潮', val:'0.80', time:'12:00', index:'2'}, 
            {name:'低高潮', val:'3.35', time:'15:00', index:'3'}
        ]
    }, 

]

