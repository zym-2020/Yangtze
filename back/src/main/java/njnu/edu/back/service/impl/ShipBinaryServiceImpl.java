package njnu.edu.back.service.impl;

import njnu.edu.back.dao.ShipMapper;
import njnu.edu.back.service.ShipBinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;

@Service
public class ShipBinaryServiceImpl implements ShipBinaryService {


    @Autowired
    ShipMapper shipMapper;

    @Override
    public byte[] getShipBinary(int size, int field, int[] shipList) {
        int Size = field * size;
        ByteBuffer buffer = ByteBuffer.allocate(4 * Size);
        for (int i = 0; i < field; i++) {
            for (int j = i; j < Size; j = j + field) {
                buffer.putInt(shipList[j]);
            }
        }
        byte[] b = buffer.array();

        return b;
    }

    public  byte[] getShipBinary2(int size,int field,byte[] oldByte)
    {
        int Size = field * size;
        int bufferSize = 4 * Size;
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        byte[] b = buffer.array();
        int c=0;
        for (int i = 0; i < field; i++) {
            for (int j = i*4; j < bufferSize; j = j + field*4) {
                for (int k=0 ;k<4;k++)
                    b[c++] = oldByte[j+k];
            }
        }

        return b;

    }
}
