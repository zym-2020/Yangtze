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
