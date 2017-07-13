package com.dfkj.fcp.core.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author songfei
 *
 */
public final class ByteArray {

	private List<Byte> listOfByte = null;

	public ByteArray() { }

	public ByteArray(byte[] bytes) {
		do {
			if (bytes == null)
				break;
			listOfByte = new ArrayList<Byte>();
			for (int idx = 0; idx < bytes.length; idx++) {
				listOfByte.add(bytes[idx]);
			}
		} while (false);
	}

	public ByteArray(byte[] bytes, int length) {
		do {
			if (bytes == null)
				break;
			listOfByte = new ArrayList<Byte>(length);
			for (int idx = 0; idx < bytes.length && length > 0; idx++, length--) {
				listOfByte.add(bytes[idx]);
			}
			while (listOfByte.size() <= length) {
				listOfByte.add((byte)0);
			}
		} while (false);
	}

	public ByteArray(int length, byte initValue) {
		listOfByte = new ArrayList<Byte>();
		while (length >= 0) {
			listOfByte.add(initValue);
			length -= 1;
		}
	}
	
	public byte[] toBytes() {
		byte[] bytes = null;

		do {
			if (listOfByte == null || listOfByte.size() == 0)
				break;
			bytes = new byte[listOfByte.size()];
			for (int idx = 0; idx < bytes.length; idx++) {
				bytes[idx] = listOfByte.get(idx);
			}
		} while (false);
		return bytes;
	}

	public void set(int position, byte b) {
		if (listOfByte == null || position >= listOfByte.size()) {
			return;
		}
		listOfByte.set(position, b);
	}
	
	public byte getAt(int position) {
		if (listOfByte == null)
			return 0;
		return listOfByte.get(makePosition(position, listOfByte.size()));
	}
	
	public short getShortAt(int position) {
		short ret = 0;
		ret = (short) ((ret << 8) | ((short)getAt(position) & 0xFF));
		ret = (short) ((ret << 8) | ((short)getAt(position + 1) & 0xFF));
		return ret;
	}
	
	public int getIntAt(int position) {
		int ret = 0;
		ret = (int) ((ret << 8) | ((int)getAt(position) & 0xFF));
		ret = (int) ((ret << 8) | ((int)getAt(position + 1) & 0xFF));
		ret = (int) ((ret << 8) | ((int)getAt(position + 2) & 0xFF));
		ret = (int) ((ret << 8) | ((int)getAt(position + 3) & 0xFF));
		return ret;
	}
	
	public long getLongAt(int position) {
		long ret = 0;
		ret = (long) ((ret << 8) | ((long)getAt(position) & 0xFF));
		ret = (long) ((ret << 8) | ((long)getAt(position + 1) & 0xFF));
		ret = (long) ((ret << 8) | ((long)getAt(position + 2) & 0xFF));
		ret = (long) ((ret << 8) | ((long)getAt(position + 3) & 0xFF));
		ret = (long) ((ret << 8) | ((long)getAt(position + 4) & 0xFF));
		ret = (long) ((ret << 8) | ((long)getAt(position + 5) & 0xFF));
		ret = (long) ((ret << 8) | ((long)getAt(position + 6) & 0xFF));
		ret = (long) ((ret << 8) | ((long)getAt(position + 7) & 0xFF));
		return ret;
	}
	public  float byteToFloat(int index) {    
		byte[] b = new byte[4];
		b[0]=getAt(index);
		b[1]=getAt(index+1);
		b[2]=getAt(index+2);
		b[3]=getAt(index+3);
	    int l;                                             
	    l = b[index + 3];                                  
	    l &= 0xff;  
	    l |= ((long) b[index + 2] << 8);     
	    l &= 0xffff;                                       
	    l |= ((long) b[index + 1] << 16);                  
	    l &= 0xffffff;                                     
	    l |= ((long) b[index + 0] << 24);                
	    return Float.intBitsToFloat(l);                    
	}
	public  float byteToFloat2(int index) {    
	    int l;                                             
	    l = getAt(index+3);                                  
	    l &= 0xff;  
	    l |= ((long) getAt(index+2) << 8);     
	    l &= 0xffff;                                       
	    l |= ((long) getAt(index+1) << 16);                  
	    l &= 0xffffff;                                     
	    l |= ((long) getAt(index) << 24);                
	    return Float.intBitsToFloat(l);                    
	}
	
	public byte getBeginByte() {
		return (listOfByte == null || listOfByte.size() == 0) ? 0 : listOfByte.get(0);
	}

	public byte getLastByte() {
		return (listOfByte == null) ? 0 : getAt(listOfByte.size() - 1);
	}

	public ByteArray removeAt(int position) {
		do {
			if (listOfByte == null || position < 0 || position >= listOfByte.size())
				break;
			listOfByte.remove(position);
		} while (false);
		return this;
	}

	public ByteArray removeBeginByte() {
		return removeAt(0);
	}
	
	public ByteArray removeAt(int position, int length) {
		do {
			if (listOfByte == null || position < 0 || position >= listOfByte.size())
				break;
			length = (position + length) <= listOfByte.size() ? length : listOfByte.size() - position;
			for (int i = 0; i < length; i++) {
				listOfByte.remove(position);
			}
		} while (false);

		return this;
	}

	public ByteArray removeLastByte() {
		return (listOfByte == null) ? this : removeAt(listOfByte.size() - 1);
	}

	public ByteArray insertAt(int position, byte b) {
		if (listOfByte == null) {
			listOfByte = new ArrayList<Byte>();
		}

		listOfByte.add(makePosition(position, listOfByte.size()), b);
		return this;
	}

	public ByteArray insertAt(int position, byte[] bytes) {
		do {
			if (bytes == null)
				break;
			for (byte b : bytes) {
				insertAt(position, b);
				position += 1;
			}
		} while (false);
		return this;
	}
	
	public ByteArray insertAt(int position, ByteArray arr) {
		position = makePosition(position, listOfByte == null ? 0 : listOfByte.size());
		listOfByte.addAll(position, arr.listOfByte);
		return this;
	}
	
	public ByteArray append(byte b) {
		return insertAt(listOfByte == null ? 0 : listOfByte.size(), b);
	}
	
	public ByteArray append(byte[] bytes) {
		return insertAt(listOfByte == null ? 0 : listOfByte.size(), bytes);
	}
	
	public ByteArray appendShort(short sValue) {
		this.append((byte)(sValue >> 8 & 0xFF));
		this.append((byte)(sValue >> 0 & 0xFF));
		return this;
	}
	
	public ByteArray appendInt(int iValue) {
		this.append((byte)(iValue >> 24 & 0xFF));
		this.append((byte)(iValue >> 16 & 0xFF));
		this.append((byte)(iValue >> 8 & 0xFF));
		this.append((byte)(iValue >> 0 & 0xFF));
		return this;
	}
	
	public ByteArray appendLong(long lValue) {
		this.append((byte)(lValue >> 56 & 0xFF));
		this.append((byte)(lValue >> 48 & 0xFF));
		this.append((byte)(lValue >> 40 & 0xFF));
		this.append((byte)(lValue >> 32 & 0xFF));
		this.append((byte)(lValue >> 24 & 0xFF));
		this.append((byte)(lValue >> 16 & 0xFF));
		this.append((byte)(lValue >> 8 & 0xFF));
		this.append((byte)(lValue >> 0 & 0xFF));
		return this;
	}
	
	public ByteArray append(ByteArray arr) {
		if (listOfByte == null) {
			listOfByte = new ArrayList<Byte>();
		}
		
		int lastPosition = listOfByte == null ? 0 : listOfByte.size();
		listOfByte.addAll(lastPosition, arr.listOfByte);
		
		return this;
	}
	
	public int size() {
		return listOfByte == null ? 0 : listOfByte.size();
	}
	
	public ByteArray replace(byte[] oldBytes, byte[] newBytes) {
		do {
			if (listOfByte == null || oldBytes == null || oldBytes.length < 1)
				break;

			//int size = listOfByte.size();
			int sizeOfOldBytes = oldBytes.length;
			int sizeOfNewBytes = newBytes.length;
			
			int position = -1;
			int startPosition = 0;
			while ((position = find(oldBytes, startPosition)) != -1) {
				removeAt(position, sizeOfOldBytes).insertAt(position, newBytes);
				startPosition += sizeOfNewBytes;
			}

		} while (false);
		return this;
	}

	public ByteArray replace(ByteArray oldBytes, ByteArray newBytes) {
		return replace(oldBytes.toBytes(), newBytes.toBytes());
	}

	/**
	 * position = 0
	 * 
	 * @param targetBytes
	 * @return
	 */
	public int find(byte[] targetBytes) {
		return find(targetBytes, 0);
	}

	public int find(byte[] targetBytes, int startPosition) {
		int position = -1;

		if (listOfByte == null) {
			return position;
		}

		int size = listOfByte.size();
		startPosition = makePosition(startPosition, size);

		int tagIndex = 0; // index of target byte array
		int sizeOfTarget = targetBytes.length;
		boolean isMatch = false;

		for (position = startPosition; position < size; position++) {
			if (targetBytes[tagIndex] == listOfByte.get(position)) {
				tagIndex += 1;
				isMatch = true;
			} else {
				tagIndex = 0;
				isMatch = false;
			}

			if (tagIndex >= sizeOfTarget) {
				break;
			}
		}

		if (isMatch && tagIndex == sizeOfTarget) { // find
			position = position - sizeOfTarget + 1;
		} else {
			position = -1;
		}

		return position;
	}

	public static final int makePosition(int position, int listSize) {
		position = (position < 0) ? 0 : position;
		position = (position > listSize) ? listSize : position;
		return position;
	}

	public static final void swapByte(byte[] bytes, int index1, int index2) {
		bytes[index1] = (byte) (bytes[index1] ^ bytes[index2]);
		bytes[index2] = (byte) (bytes[index1] ^ bytes[index2]);
		bytes[index1] = (byte) (bytes[index1] ^ bytes[index2]);
	}
	
	public final byte[] subByteArray(ByteArray bytes,int index1,int index2){
		byte[] b = new byte[9];
		int i = 0;
		while(index1 <= index2){
			b[i] = bytes.getAt(index1);	
			index1 += 1;
			i += 1;			
		}
		return b;
	}
}
