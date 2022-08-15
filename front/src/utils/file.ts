import SparkMD5 from 'spark-md5'
import { getToken } from './auth'
import axios from 'axios';
import { checkMergeState } from '@/api/request'
import { useStore } from '@/store';
import { notice } from './notice';

const store = useStore()
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

export async function handlePostFiles(chunkList: string[], fileChunk: { file: Blob }[], MD5: string, id: string, fileName: string) {
    return new Promise((res, rej) => {
        const totalChunks = chunkList.length
        let successCount = 0
        const handle = () => {
            const store = useStore()
            if (chunkList.length) {
                const name = chunkList.shift()
                const token = getToken()
                const formData = new FormData()
                formData.append("file", fileChunk[parseInt(name as string)].file)
                formData.append("MD5", MD5)
                formData.append("name", name as string)
                axios.post('http://172.21.213.244:8080/Yangtze/file/uploadFile', formData, {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                }).then(response => {
                    if (response.status === 200 && response.data.code === 0) {
                        successCount++
                        store.commit("SET_UPLOAD_ITEM", {id: id, name: fileName, state: 2, progress: Math.ceil((fileChunk.length - chunkList.length) / fileChunk.length * 100)})
                        handle()
                    } else {
                        successCount++
                        chunkList.push(name as string)
                        handle()
                    }
                }).catch(err => {
                    successCount++
                    chunkList.push(name as string)
                    handle()
                })
            }
            if (successCount >= totalChunks) {
                res(undefined);
            }
        }
        for (let i = 0; i < 5; i++) {
            handle();
        }
    })
}

export async function checkStatus(key: string, id: string, callback: () => void) {
    async function handle() {
        const store = useStore()
        const response = await new Promise(async (res, rej) => {
            res(await checkMerge(key));
        });
        if (response === 0) {
            let result
            setTimeout(async () => {
                result = await handle();
            }, 2000);
            return result
        } else {
            for (let i = 0; i < store.state.other.uploadList.length; i++) {
                if (store.state.other.uploadList[i].id === id) {
                    const temp = store.state.other.uploadList[i]
                    store.commit("REMOVE_UPLOAD_ITEM", i)
                    store.commit("ADD_UPLOADED_ITEM", { id: temp.id, name: temp.name, state: response as number })
                    store.commit("SET_UPLOAD_DOT_FLAG", true)

                    callback()
                    break
                }
            }
            return response
        }
    }
    return await handle()
}

export async function checkMerge(key: string) {
    const state = await checkMergeState(key)
    return state.data
}