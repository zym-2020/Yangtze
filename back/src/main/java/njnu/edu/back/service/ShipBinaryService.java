package njnu.edu.back.service;

public interface ShipBinaryService {
      byte[] getShipBinary(int size,int field,int[] shipList);

      byte[] getShipBinary2(int size,int field,byte[] oldByte);
}
