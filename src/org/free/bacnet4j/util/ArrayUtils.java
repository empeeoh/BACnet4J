package org.free.bacnet4j.util;
//Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
//Jad home page: http://www.kpdus.com/jad.html
//Decompiler options: packimports(3) 
//Source File Name:   ArrayUtils.java

import java.util.List;

public class ArrayUtils{

	public ArrayUtils(){}

	public static String toHexString(byte bytes[]){
		return toHexString(bytes, 0, bytes.length);
	}

	public static String toHexString(byte bytes[], int start, int len){
		if(len == 0)
			return "[]";
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		sb.append(Integer.toHexString(bytes[start] & 0xff));
		for(int i = 1; i < len; i++)
			sb.append(',').append(Integer.toHexString(bytes[start + i] & 0xFF));

		sb.append("]");
		return sb.toString();
	}

	public static String toPlainHexString(byte bytes[]){
		return toPlainHexString(bytes, 0, bytes.length);
	}

	public static String toPlainHexString(byte bytes[], int start, int len){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < len; i++){
			String s = Integer.toHexString(bytes[start + i] & 0xFF);
			if(s.length() < 2)
				sb.append('0');
			sb.append(s);
		}
		return sb.toString();
	}

	public static String toString(byte bytes[]){
		return toString(bytes, 0, bytes.length);
	}

	public static String toString(byte bytes[], int start, int len){
		if(len == 0)
			return "[]";
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		sb.append(Integer.toString(bytes[start] & 0xff));
		for(int i = 1; i < len; i++)
			sb.append(',').append(Integer.toString(bytes[start + i] & 0xFF));

		sb.append("]");
		return sb.toString();
	}

	public static boolean isEmpty(int value[]){
		return value == null || value.length == 0;
	}

	public static int indexOf(String values[], String value){
		if(values == null)
			return -1;
		for(int i = 0; i < values.length; i++)
			if(values[i].equals(value))
				return i;
		return -1;
	}

	public static boolean containsIgnoreCase(String values[], String value){
		if(values == null)
			return false;
		for(int i = 0; i < values.length; i++)
			if(values[i].equalsIgnoreCase(value))
				return true;
		return false;
	}

	public static int indexOf(byte src[], byte target[]){
		return indexOf(src, 0, src.length, target);
	}

	public static int indexOf(byte src[], int len, byte target[]){
		return indexOf(src, 0, len, target);
	}

	public static int indexOf(byte src[], int start, int len, byte target[]){
		for(int pos = start; pos + target.length <= len; pos++){
			if(src[pos] != target[0])
				continue;
			boolean matched = true;
			int i = 1;
			do{
				if(i >= target.length)
					break;
				if(src[pos + i] != target[i])
				{
					matched = false;
					break;
				}
				i++;
			} while(true);
			if(matched)
				return pos;
		}
		return -1;
	}

	public static int sum(int a[]){
		int sum = 0;
		for(int i = 0; i < a.length; i++)
			sum += a[i];
		return sum;
	}

	public static int[] toIntArray(List<Integer> list){
		int result[] = new int[list.size()];
		for(int i = 0; i < result.length; i++)
			result[i] = ((Integer)list.get(i)).intValue();
		return result;
	}

	public static double[] toDoubleArray(List<Double> list){
		double result[] = new double[list.size()];
		for(int i = 0; i < result.length; i++)
			result[i] = ((Double)list.get(i)).doubleValue();
		return result;
	}

	public static String concatenate(Object a[], String delimiter){
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		Object arr[] = a;
		int len = arr.length;
		for(int i = 0; i < len; i++){
			Object o = arr[i];
			if(first)
				first = false;
			else
				sb.append(delimiter);
			sb.append(o);
		}
		return sb.toString();
	}

	public static void shift(Object a[], int count){
		if(count > 0)
			System.arraycopy(((Object) (a)), 0, ((Object) (a)), count, a.length - count);
		else
			System.arraycopy(((Object) (a)), -count, ((Object) (a)), 0, a.length + count);
	}
}