package njnu.edu.back.common.utils;

import java.nio.ByteBuffer;

public class Byte2btye {

    public static byte[] getShipBinary(byte[] oldByte)
    {
        //int Size = field * size;
        int field =7;
        int bufferSize = oldByte.length;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        byte[] b = buffer.array();
        int c=0;
        for (int i = 0; i < field; i++) {
            for (int j = i*4; j < bufferSize; j = j + 25) {
                if(i==6){
                    for (int k = 0; k < 1; k++)
                        b[c++] = oldByte[j + k];
                }
                else {
                    for (int k = 0; k < 4; k++)
                        b[c++] = oldByte[j + k];
                }
            }
//            if(i == 6) {
//
//            } else {
//
//            }
        }

        return b;

    }

}
