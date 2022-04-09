import SparkMD5 from 'spark-md5'

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
    while(count < file.size) {
        fileChunkList.push({
            file: file.slice(count, count + size),
        })
        count += size
    }
    return fileChunkList
}