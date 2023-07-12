# 环境依赖

**数据库：**
postgresql *13.6*
redis *5.0.14*
postgis *3.2.0*

**java**
java8

**python**
python *3.9.0*

**python依赖包**
gdal *3.2.3*
numpy *1.24.3*
opencv *4.5.1*
pandas *2.0.1*
statsmodels *0.14.0*


# 项目所用技术栈
前端：vue3、typescript、mapbox、openlayers、element-plus、axios、echarts
后端：springboot、mybatis

需要了解：java多线程，java调用开启进程，postgis矢量切片、矢量入库，python相关包的使用（gdal、numpy、cv），sql语法


# 数据库字段解释
## dataset数据库
**Description：pg数据库，主数据库**

***user***
*用户表*
|    字段    |            描述             |
| :--------: | :-------------------------: |
|     id     |             id              |
|    name    |           用户名            |
|   email    |            邮箱             |
|  password  |            密码             |
|   avatar   |          用户头像           |
| occupation |            职业             |
| department |            部门             |
|    role    | 用户角色（member or admin） |

***analytic_dataset***
*分析结果表*
|    字段     |            描述            |
| :---------: | :------------------------: |
|     id      |             id             |
|  file_name  | 结果文件名（用于前端显示） |
|   address   |          文件地址          |
| create_time |          创建时间          |
|   creator   |           创建人           |
| visual_type |      文件的可视化类型      |
|  visual_id  |     对应的可视化文件id     |
| project_id  |      对应的project id      |

***analytic_parameter***
*一些做分析要用到的参数*
|     字段     |              描述               |
| :----------: | :-----------------------------: |
|      id      |               id                |
|     type     |              类型               |
|   file_id    |         对应files表的id         |
| data_list_id |       对应data_list表的id       |
|   address    |            文件地址             |
|   content    |         忘了是干嘛的了          |
| benchmark_id | 基准dem id（针对特定的dem参数） |
|   refer_id   | 参考dem id（针对特定的dem参数） |

***browse_history***
*data_list的浏览记录*
|    字段     |       描述        |
| :---------: | :---------------: |
|     id      |        id         |
|   user_id   |   浏览用户的id    |
| browse_time |    浏览的时间     |
|     ip      |   浏览用户的ip    |
|   data_id   | 对应data_list的id |

***data_list***
*数据集列表*
|       字段       |                       描述                       |
| :--------------: | :----------------------------------------------: |
|        id        |                        id                        |
|       name       |                     数据集名                     |
|     location     |                 数据集的地理范围                 |
|   description    |                    数据集描述                    |
|       tags       |                    数据集标签                    |
|     creator      |                  数据集谁创建的                  |
|   create_time    |                  数据集创建时间                  |
|   update_time    |                  数据集更新时间                  |
|     download     |                  数据集的下载量                  |
|      watch       |                  数据集的浏览量                  |
|      avatar      |                   数据集的图标                   |
|      status      |        数据集的状态（online or offline）         |
|     provider     |                   数据的提供方                   |
|      range       |               数据的范围，文件描述               |
|       type       |                    数据集类型                    |
|  provider_phone  |                    提供方电话                    |
|  provider_email  |                    提供方邮箱                    |
| provider_address |                    提供方地址                    |
|    get_online    | 获取方式（直接下载 or 订单方式目前都是直接下载） |
|      detail      |            markdown形式的数据详情补充            |
|    time_stamp    |                      时间戳                      |


***data_relational***
files与data_list映射关系
|     字段     |       描述        |
| :----------: | :---------------: |
|      id      |        id         |
| data_list_id | 对应data_list的id |
|   file_id    |   对应files的id   |

***download_history***
*data_list的下载记录*
|     字段      |           描述            |
| :-----------: | :-----------------------: |
|      id       |            id             |
|    user_id    |       对应用户的id        |
| download_time |         下载时间          |
|      ip       |       下载用户的ip        |
| data_list_id  |     对应data_list的id     |
|    file_id    | 对应data_list下的files id |

***files***
*存储的文件*
|    字段     |          描述          |
| :---------: | :--------------------: |
|     id      |           id           |
|  file_name  | 文件名（用于前端显示） |
|    size     |        文件大小        |
|   address   |        文件地址        |
|  uploader   |         上传者         |
| visual_type |     文件可视化类型     |
|  visual_id  |    相应可视化文件id    |
|  folder_id  |   文件所在文件夹的id   |

***folder***
*文件夹*
|    字段     |           描述           |
| :---------: | :----------------------: |
|     id      |            id            |
| folder_name | 文件夹名（用于前端显示） |
|  parent_id  |      上层文件夹 id       |
|  uploader   |         上传的人         |
|   address   |        文件夹地址        |

***project***
*项目*
|     字段     |     描述     |
| :----------: | :----------: |
|      id      |      id      |
| project_name |    项目名    |
|   creator    |  项目创建人  |
| create_time  | 项目创建时间 |
|    avatar    |   项目图标   |
| layer_manage | 项目图层管理 |
|   basemap    |     底图     |
|  is_public   |   是否公开   |

***project_file***
*项目里添加的data_list中的数据*
|     字段     |       描述        |
| :----------: | :---------------: |
|      id      |        id         |
|  project_id  |   对应的项目id    |
|   file_id    |   对应files的id   |
| data_list_id | 对应data_list的id |

***special_data***
*特色数据*
| 字段  |              描述               |
| :---: | :-----------------------------: |
|  id   | id（此处的id对应data_list的id） |

***upload_record***
*上传记录*
|    字段     |   描述   |
| :---------: | :------: |
|     id      |    id    |
|  file_name  |  文件名  |
|  uploader   |  上传人  |
| upload_time | 上传时间 |
|    size     | 文件大小 |

***visual_file***
*可视化文件list*
|   字段    |            描述            |
| :-------: | :------------------------: |
|    id     |             id             |
| file_name |           文件名           |
|   type    |            类型            |
|  content  |   根据这个字段进行可视化   |
|   view    | 针对地图数据的视角进行偏移 |




## shp_dataset数据库
**Description：pg数据库，存储矢量文件**

## 2020-10-planet-14.mbtiles数据库
**Description：sqlite数据库，osm瓦片切片**

## static_database.db数据库
**Description：sqlite数据库，网络上获取的航道相关数据（如浮标）**

## ais.db数据库
**Description：sqlite数据库，爬取的长江航道在线的两天的AIS数据**


