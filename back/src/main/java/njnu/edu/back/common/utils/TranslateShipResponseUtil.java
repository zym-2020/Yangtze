package njnu.edu.back.common.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2022/06/22/17:31
 * @Description:
 */
public class TranslateShipResponseUtil {

    public static List<Map<String, Object>> translate(String[] bbox, String zoom, String mmsi, String ref, String showName) {
        List<Map<String, Object>> result = new ArrayList<>();
        byte[] response = RemoteRequest.getShips(bbox, zoom, mmsi, ref, showName);
        int payloadSize = response.length;
        int headerLength = payloadSize >= 12 ? response[2] : 0;
        boolean zoomLevelOver13 = Integer.parseInt(zoom) > 13;
        int mmsiInHeader = 0;
        if (headerLength >= 8) {
            mmsiInHeader = byte2Int(getByteArray32(response, 8));
        }

        for(int offset = 4 + headerLength; offset < payloadSize;) {
            short flags = byte2Int16(getByteArray16(response, offset));
            offset += 2;
            int rotationFlag = ((flags & 0x3F00) >> 8);
            int currentMMSI = byte2Int(getByteArray32(response, offset));
            offset += 4;
            double lon = byte2Int(getByteArray32(response, offset)) / 6e5;
            offset += 4;
            double lat = byte2Int(getByteArray32(response, offset)) / 6e5;
            offset += 4;

            if (currentMMSI == mmsiInHeader) {
                offset += 6;
            }

            offset += 1;
            int nameStringLength = response[offset];
            offset += 1;
            if (offset + nameStringLength > payloadSize) {
                break;
            }
            String name = new String(getStringByteArray(response, offset, nameStringLength + offset));
            offset += nameStringLength;
            if (name.equals("")) {
                name = Integer.toString(currentMMSI);
            }
            if (currentMMSI == mmsiInHeader)
                offset += 4;

            Map<String, Object> temp = new HashMap<>();
            temp.put("mmsi", currentMMSI);
            temp.put("rotation", rotationFlag * 11.25);
            temp.put("latitude", lat);
            temp.put("longitude", lon);
            temp.put("name", name);
            result.add(temp);

            if (zoomLevelOver13) {
                offset += 10;
            }

            if ((flags & 2 ) > 0 && !zoomLevelOver13) {
                offset += 2;
            }
        }
        return result;
    }

    private static int byte2Int(byte[] b) {
        int intValue = 0;
        for (int i = 0; i < b.length; i++) {
            intValue += (b[i] & 0xFF) << (8 * (3 - i));
        }
        return intValue;
    }

    private static short byte2Int16(byte[] b) {
        short l = 0;
        for (int i = 0; i < 2; i++) {
            l<<=8;
            l |= (b[i] & 0xff);
        }
        return l;

    }

    private static byte[] getByteArray32(byte[] b, int count) {
        byte[] result = new byte[4];
        for(int i = 0; i < 4; i++) {
            result[i] = b[count + 1];
        }
        return result;
    }

    private static byte[] getByteArray16(byte[] b, int count) {
        byte[] result = new byte[2];
        for(int i = 0; i < 2; i++) {
            result[i] = b[count + 1];
        }
        return result;
    }

    private static byte[] getStringByteArray(byte[] b, int start, int end) {
        return Arrays.copyOfRange(b, start, end);
    }
}
