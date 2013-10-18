package org.free.bacnet4j.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import org.apache.commons.lang3.StringUtils;

public class StreamUtils{

	public StreamUtils(){}

	public static void transfer(InputStream in, OutputStream out)throws IOException{
		transfer(in, out, -1L);
	}

	public static void transfer(InputStream in, OutputStream out, long limit)
			throws IOException {
		byte buf[] = new byte[1024];
		long total = 0L;
		do{
			int readcount;
			if((readcount = in.read(buf)) == -1)
				break;
			if(limit != -1L && total + (long)readcount > limit)
				readcount = (int)(limit - total);
			if(readcount > 0)
				out.write(buf, 0, readcount);
			total += readcount;
		} while(limit == -1L || total < limit);
		out.flush();
	}

	public static void transfer(InputStream in, SocketChannel out) throws IOException{
		byte buf[] = new byte[1024];
		ByteBuffer bbuf = ByteBuffer.allocate(1024);
		int len;
		while((len = in.read(buf)) != -1) {
			bbuf.put(buf, 0, len);
			bbuf.flip();
			for(; bbuf.remaining() > 0; out.write(bbuf));
			bbuf.clear();
		}
	}

	public static void transfer(Reader reader, Writer writer) throws IOException{
		transfer(reader, writer, -1L);
	}

	public static void transfer(Reader reader, Writer writer, long limit) throws IOException {
		char buf[] = new char[1024];
		long total = 0L;
		do {
			int readcount;
			if((readcount = reader.read(buf)) == -1)
				break;
			if(limit != -1L && total + (long)readcount > limit)
				readcount = (int)(limit - total);
			if(readcount > 0)
				writer.write(buf, 0, readcount);
			total += readcount;
		} while(limit == -1L || total < limit);
		writer.flush();
	}

	public static byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
		transfer(in, out);
		return out.toByteArray();
	}

	public static char[] read(Reader reader) throws IOException {
		CharArrayWriter writer = new CharArrayWriter();
		transfer(reader, writer);
		return writer.toCharArray();
	}

	public static char readChar(InputStream in) throws IOException {
		return (char)in.read();
	}

	public static String readString(InputStream in, int length) throws IOException {
		StringBuilder sb = new StringBuilder(length);
		for(int i = 0; i < length; i++)
			sb.append(readChar(in));

		return sb.toString();
	}

	public static byte readByte(InputStream in) throws IOException {
		return (byte)in.read();
	}

	public static int read4ByteSigned(InputStream in) throws IOException {
		return in.read() | in.read() << 8 | in.read() << 16 | in.read() << 24;
	}

	public static long read4ByteUnsigned(InputStream in) throws IOException{
		return (long)(in.read() | in.read() << 8 | in.read() << 16 | in.read() << 24);
	}

	public static int read2ByteUnsigned(InputStream in) throws IOException {
		return in.read() | in.read() << 8;
	}

	public static short read2ByteSigned(InputStream in) throws IOException{
		return (short)(in.read() | in.read() << 8);
	}

	public static void writeByte(OutputStream out, byte b) throws IOException {
		out.write(b);
	}

	public static void writeChar(OutputStream out, char c) throws IOException{
		out.write((byte)c);
	}

	public static void writeString(OutputStream out, String s) throws IOException{
		for(int i = 0; i < s.length(); i++)
			writeChar(out, s.charAt(i));
	}

	public static void write4ByteSigned(OutputStream out, int i) throws IOException{
		out.write((byte)(i & 0xff));
		out.write((byte)(i >> 8 & 0xff));
		out.write((byte)(i >> 16 & 0xff));
		out.write((byte)(i >> 24 & 0xff));
	}

	public static void write4ByteUnsigned(OutputStream out, long l) throws IOException{
		out.write((byte)(int)(l & 255L));
		out.write((byte)(int)(l >> 8 & 255L));
		out.write((byte)(int)(l >> 16 & 255L));
		out.write((byte)(int)(l >> 24 & 255L));
	}

	public static void write2ByteUnsigned(OutputStream out, int i) throws IOException{
		out.write((byte)(i & 0xff));
		out.write((byte)(i >> 8 & 0xff));
	}

	public static void write2ByteSigned(OutputStream out, short s) throws IOException{
		out.write((byte)(s & 0xff));
		out.write((byte)(s >> 8 & 0xff));
	}

	public static String dumpArray(byte b[]){
		return dumpArray(b, 0, b.length);
	}

	public static String dumpArray(byte b[], int pos, int len){
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int i = pos; i < len; i++){
			if(i > 0)
				sb.append(",");
			sb.append(b[i]);
		}
		sb.append(']');
		return sb.toString();
	}

	public static String dumpMessage(byte b[]){
		return dumpMessage(b, 0, b.length);
	}

	public static String dumpMessage(byte b[], int pos, int len){
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int i = pos; i < len; i++)
			switch(b[i]){
			case 2: // '\002'
				sb.append("&STX;");
				break;
			case 3: // '\003'
				sb.append("&ETX;");
				break;
			case 27: // '\033'
				sb.append("&ESC;");
				break;
			default:
				sb.append((char)b[i]);
				break;
			}
		sb.append(']');
		return sb.toString();
	}

	public static String dumpArrayHex(byte b[]){
		return dumpArrayHex(b, 0, b.length);
	}

	public static String dumpArrayHex(byte b[], int pos, int len){
		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for(int i = pos; i < len; i++){
			if(i > 0)
				sb.append(",");
			sb.append(Integer.toHexString(b[i] & 0xff));
		}
		sb.append(']');
		return sb.toString();
	}

	public static String dumpHex(byte b[]){
		return dumpHex(b, 0, b.length);
	}

	public static String dumpHex(byte b[], int pos, int len){
		StringBuilder sb = new StringBuilder();
		for(int i = pos; i < len; i++)
			sb.append(StringUtils.leftPad(Integer.toHexString(b[i] & 0xff), 2, '0'));
		return sb.toString();
	}
	//
	// public static String readFile(String filename) throws IOException {
	//     return readFile(new File(filename));
	// }
	//
	// public static String readFile(File file) throws IOException {
	//     FileReader in = null;
	//     String s;
	//     in = new FileReader(file);
	//     StringWriter out = new StringWriter();
	//     transfer(in, out);
	//     s = out.toString();
	//     if(in != null)
	//         in.close();
	//     return s;
	//     Exception exception;
	//     exception;
	//     if(in != null)
	//         in.close();
	//     throw exception;
	// }

//	public static List<String> readLines(String filename) throws IOException{
//		return readLines(new File(filename));
//	}
//
//	public static List<String> readLines(File file) throws IOException {
//		List<String> lines = new ArrayList<>();
//		BufferedReader in = null;
//		List<String> list;
//		in = new BufferedReader(new FileReader(file));
//		String line;
//		while((line = in.readLine()) != null) 
//			lines.add(line);
//		list = lines;
//		if(in != null)
//			in.close();
//		return list;
//		Exception exception;
//		exception;
//		if(in != null)
//			in.close();
//		throw exception;
//	}

//	public static void writeFile(String filename, String content)throws IOException{
//		writeFile(new File(filename), content);
//	}
//
//	public static void writeFile(File file, String content) throws IOException {
//		FileWriter out = null;
//		out = new FileWriter(file);
//		out.write(content);
//		if(out != null)
//			out.close();
//		break MISSING_BLOCK_LABEL_38;
//		Exception exception;
//		exception;
//		if(out != null)
//			out.close();
//		throw exception;
//	}

//	public static void readLines(String filename, LineHandler lineHandler) 
//															 throws IOException{
//		BufferedReader in = null;
//		in = new BufferedReader(new FileReader(filename));
//		String line;
//		while((line = in.readLine()) != null) 
//			lineHandler.handleLine(line);
//		lineHandler.done();
//		if(in != null)
//			in.close();
//		break MISSING_BLOCK_LABEL_67;
//		Exception exception;
//		exception;
//		if(in != null)
//			in.close();
//		throw exception;
//	}

	public static String toHex(byte bs[]){
		StringBuilder sb = new StringBuilder(bs.length * 2);
		byte arr$[] = bs;
		int len$ = arr$.length;
		for(int i$ = 0; i$ < len$; i$++)
		{
			byte b = arr$[i$];
			sb.append(StringUtils.leftPad(Integer.toHexString(b & 0xff), 2, '0'));
		}

		return sb.toString();
	}

	public static String toHex(byte b){
		return StringUtils.leftPad(Integer.toHexString(b & 0xff), 2, '0');
	}

	public static String toHex(short s){
		return StringUtils.leftPad(Integer.toHexString(s & 0xffff), 4, '0');
	}

	public static String toHex(int i){
		return StringUtils.leftPad(Integer.toHexString(i), 8, '0');
	}

	public static String toHex(long l){
		return StringUtils.leftPad(Long.toHexString(l), 16, '0');
	}

	public static byte[] fromHex(String s){
		byte bs[] = new byte[s.length() / 2];
		for(int i = 0; i < bs.length; i++)
			bs[i] = (byte)Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
		return bs;
	}
}