export type Buoy = {
  bsys: string;
  dbbz: string;
  dbxz: string;
  dbys: string;
  dzxx: string;
  hbmc: string;
  hbphoto: string;
  hbtlpz: string;
  hbxz: string;
  hdyyjl: string;
  id: string;
  jdwz_84jd: number;
  jdwz_84wd: number;
  sbzt: string;
  sshd: string;
  xpy: string;
  ypy: string;
  zxaqjl: string;
};

export type Ship = {
  direction: number;
  lat: number;
  length: string;
  lon: number;
  mmsi: string;
  name: string;
  name_cn: string;
  register_time: string;
  update_time: string;
  velocity: number;
  width: string;
};

export type RealShip = {
  acblx: string;
  cb_aisdwlx: string;
  cb_aisdwlx_name: string;
  cb_aislx: string;
  cbcs: string;
  cbhh: string;
  cbhx: number;
  cbmc: string;
  cd: string;
  cjsj: string;
  dqhs: number;
  gxsj: string;
  id: string;
  imo: string;
  jssj: string;
  kd: string;
  mdg: string;
  mmsi: string;
  sjly: string;
  type: string;
  yjsj: string;
  zbjd: number;
  zbwd: number;
  zwmc: string;
};

export type Anchor = {
  id: string;
  mdmc: string;
  sdcd: string;
  type: string;
  zbjd: number;
  zbwd: number;
  tpdz: string;
  gldw: string;
  hd_hdly_name: string;
  ty_anb_name: string;
  yt: string;
  jcsj: string;
  hdlc: number;
  qyfw: {
    type: string;
    points: number[][];
  };
  hd_hdly: string;
  ty_anb: string;
  sd_name: string;
};

export type Park = {
  id: string;
  mc: string;
  hd_hdly: string;
  hd_hdly_name: string;
  hdlc: number;
  ty_anb: string;
  ty_anb_name: string;
  type: string;
  kd: string;
  yt: string;
  zbjd: number;
  zbwd: number;
  tpdz: string;
  sd_id: string;
  sd_name: string;
  gldw: string;
  qyfw: {
    type: string;
    point?: number[];
    points?: number[][];
  };
  sdcd: string;
};

export type Bridge = {
  id: string;
  syfw: string;
  qhb: string;
  hlhf: string;
  hppfwj: string;
  hsgljg: string;
  qlsx: string;
  sjthdbcx: string;
  qzd: string;
  qdthdbcx: string;
  qlzdfzss: string;
  qlyxgldw: string;
  tcsj: string;
  qlbdfzss: string;
  rjdmll: string;
  qqspss: string;
  glzd: string;
  lhss: string;
  qt: string;
  zthkbh: string;
  tbq: string;
  khss: string;
  hbbz: string;
  xlthkbh: string;
  polygon: { coordinates: number[][][]; type: string };
  yhdw: string;
  qlmc: string;
  name: string;
  fznl: string;
};

export type Meteorology = {
  description: string;
  effective: string;
  headline: string;
  id: string;
  longitude: number;
  latitude: number;
  title: string;
  type: string;
};

export type Station = {
  name: string;
  name_en: string;
  keys: string[];
  keys_cn: string[];
  lon: number;
  lat: number;
  type: string;
  start_time: number[];
};

export type WaterLevelChartType = {
  timeList: string[];
  legend: string[];
  yAxis: {
    alignTicks: boolean;
    type: string;
    offset?: number;
    axisLine: {
      show: boolean;
      lineStyle: {
        color: string;
      };
    };
  }[];
  series: {
    name: string;
    data: number[];
    type: string;
    smooth: boolean;
    yAxisIndex: number;
    itemStyle: {
      normal: {
        color: string; //改变折线点的颜色
        lineStyle: {
          color: string; //改变折线颜色
        };
      };
    };
  }[];
};

export type WaterLevel =
  | {
      time: string;
      waterLevel: number;
      flow: number;
    }
  | {
      upstreamWaterLevel: number;
      downstreamWaterLevel: number;
      flow: number;
      time: string;
    }
  | {
      rainfall: number;
      waterLevel: number;
      input: number;
      output: number;
      time: string;
    };

export type SearchTable =
  | {
      mmsi: string;
      name: string;
    }
  | { sshd: string; hbmc: string }
  | { mc: string; yt: string }
  | { mdmc: string; yt: string }
  | { 桥梁属性: string; 桥梁名称: string }
  | { name: string };

export type Section = {
  id: string;
  name: string;
};
export type AnalyticDataset = {
  id: string;
  fileName: string;
  visualId: string;
  visualType: string;
};
export type AnalyticParameter = {
  fileId: string;
  fileName: string;
  dataListId: string;
  dataListName: string;
  visualId: string;
  visualType: string;
};
export type TreeData = {
  label: string;
  flag: boolean;
  children: TreeData[];
  id: string;
  checkFlag?: boolean;
  visualId?: string;
  visualType?: string;
  parentId?: string;
  parentName?: string;
};

export type Folder = {
  id: string;
  folderName: string;
  parentId: string;
  flag: boolean;
};
export type File = {
  id: string;
  fileName: string;
  visualType: string;
  size: string;
  uploader: string;
  folderId: string;
  visualId: string;
  flag: boolean;
  view?: string;
};

export type AuditFile = {
  id: string;
  fileName: string;
  size: string;
  uploader: string;
  visualType: string;
  visualId: string;
  name: string;
};

export type ModelInfo = {
  id: string;
  name: string;
  description: string;
};

export type ModelConfig = {
  id: string;
  name: string;
  description: string;
  parameters: {
    parameterList: {
      optional: boolean;
      name: string;
      description: string | null;
      type: string;
    }[];
  };
};

export type GenerateConfigJsonData = {
  projectId: string;
  config: {
    model: string;
    parameters: {
      parameter: string;
      key: string;
      value: string;
    }[];
  };
};
