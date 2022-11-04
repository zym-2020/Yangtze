import SparkMD5 from 'spark-md5'
import { getToken } from './auth'
import axios from 'axios';
import { checkMergeStateTemp, uploadFile } from '@/api/request'
import { useStore } from '@/store';
import { notice } from './notice';
import { prefix } from '@/prefix'


export function getFileMd5(file: File, callback: (f: string) => void) {

    const chunkSize = 5242880
    //为了兼容各种浏览器
    const blobSlice = File.prototype.slice || (File.prototype as any).mozSlice || (File.prototype as any).webkitSlice
    const fileReader = new FileReader()

    //计算分片数
    const totalChunks = Math.ceil(file.size / chunkSize)
    const spark = new SparkMD5.ArrayBuffer()
    let currentChunk = 0
    fileReader.readAsArrayBuffer(blobSlice.call(file, currentChunk * chunkSize, (currentChunk + 1) * chunkSize >= file.size ? file.size : (currentChunk + 1) * chunkSize))
    fileReader.onload = function (e) {
        try {
            spark.append(e.target?.result as ArrayBuffer)
        } catch (error) {
            console.log('获取Md5错误：' + currentChunk)
        }
        if (currentChunk < totalChunks) {
            currentChunk++
            fileReader.readAsArrayBuffer(blobSlice.call(file, currentChunk * chunkSize, (currentChunk + 1) * chunkSize >= file.size ? file.size : (currentChunk + 1) * chunkSize))
        } else {
            callback(spark.end())
        }
    }
    fileReader.onerror = function () {
        console.warn('读取Md5失败，文件读取错误')
    }
}

export function createFileChunk(file: File) {
    const size = 5242880
    const fileChunkList = []
    let count = 0
    while (count < file.size) {
        fileChunkList.push({
            file: file.slice(count, count + size),
        })
        count += size
    }
    return fileChunkList
}

export async function handlePostFiles(fileChunk: { file: Blob }[], key: string, id: string) {
    const store = useStore()
    const names: string[] = []
    fileChunk.forEach((item, index) => {
        names.push(index.toString())
    })
    return new Promise((res, rej) => {
        const totalChunks = fileChunk.length
        let successCount = 0
        console.log(fileChunk)
        const handle = async () => {
            if (store.state.other.uploading[id] === undefined || store.state.other.uploading[id].state === 1) {
                if (names.length > 0) {
                    const name = names.shift()
                    const formData = new FormData()
                    formData.append("file", fileChunk[parseInt(name as string)].file)
                    formData.append("key", key)
                    formData.append("name", name as string)
                    const data = await uploadFile(formData)
                    if (data != null && (data as any).code === 0) {
                        const temp = store.state.other.uploading[id]
                        if (temp != undefined) {
                            store.commit("UPDATE_UPLOADING", {
                                id: id, value: {
                                    name: temp.name,
                                    size: temp.size,
                                    state: temp.state,
                                    progress: parseFloat(((totalChunks - names.length) * 100 / totalChunks).toFixed(2))
                                }
                            })
                        }
                    } else {
                        names.push(name as string)
                    }
                    successCount++
                    await handle()

                }
                if (successCount >= totalChunks) {
                    if (names.length === 0) {
                        for (let i = 0; i < totalChunks; i++) {
                            fileChunk.shift()
                        }
                    }
                    res(undefined);
                }
            } else {
                store.commit("REMOVE_UPLOADING", id)
                res(undefined);
            }

        }
        for (let i = 0; i < 5; i++) {
            handle();
        }
    })
}

export async function checkStatus(key: string, id: string) {
    async function handle() {
        const store = useStore()
        const response = await checkMerge(key)
        if (response === 0) {
            setTimeout(async () => {
                await handle();
            }, 2000);
        } else {
            const temp = store.state.other.uploading[id]
            store.commit("REMOVE_UPLOADING", id)

            if (response === 1) {
                store.commit("ADD_UPLOADED_ITEM", { id: id, name: temp.name, size: temp.size, time: new Date() })
                notice("success", "成功", temp.name + "上传成功")
            } else {
                notice("error", "失败", temp.name + "上传失败")
            }

        }
    }
    await handle()
}

export async function checkMerge(key: string) {
    const state = await checkMergeStateTemp(key)
    if (state != null && (state as any).code === 0) {
        return state.data
    } else {
        return -1
    }
}