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
  [key: string]: string;
} & { polygon: { coordinates: number[][][]; type: string } };


export type Meteorology = {
  description: string;
  effective: string;
  headline: string;
  id: string;
  longitude: number;
  latitude: number;
  title: string;
  type: string;
}