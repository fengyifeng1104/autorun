package com.ymatou.autorun.datadriver.base.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ymt.core.tool.Logger;

public class MapUtil {
	public static Map<String,Object> pojoToMap(Object object){
		Map<String,Object> ret = new HashMap<String,Object>();
		try{
			Field[] fs = object.getClass().getDeclaredFields();
			for(Field field : fs){
				field.setAccessible(true);
				ret.put(field.getName(), field.get(object));
			}
		}catch(Exception e){
			Logger.fail(e);
		}
		return ret;
		
	}	
	
	
	
	public static <K,V> Map<K,V> hashMap(){
		return new HashMap<K, V>();
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1){
		return praseMap(Arrays.asList(k1),Arrays.asList(v1));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2){
		return praseMap(Arrays.asList(k1,k2),Arrays.asList(v1,v2));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2,K k3,V v3){
		return praseMap(Arrays.asList(k1,k2,k3),Arrays.asList(v1,v2,v3));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4){
		return praseMap(Arrays.asList(k1,k2,k3,k4),Arrays.asList(v1,v2,v3,v4));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4,K k5,V v5){
		return praseMap(Arrays.asList(k1,k2,k3,k4,k5),Arrays.asList(v1,v2,v3,v4,v5));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4,K k5,V v5,K k6,V v6){
		return praseMap(Arrays.asList(k1,k2,k3,k4,k5,k6),Arrays.asList(v1,v2,v3,v4,v5,v6));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4,K k5,V v5,K k6,V v6,K k7,V v7){
		return praseMap(Arrays.asList(k1,k2,k3,k4,k5,k6,k7),Arrays.asList(v1,v2,v3,v4,v5,v6,v7));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4,K k5,V v5,K k6,V v6,K k7,V v7,K k8,V v8){
		return praseMap(Arrays.asList(k1,k2,k3,k4,k5,k6,k7,k8),Arrays.asList(v1,v2,v3,v4,v5,v6,v7,v8));
	}
	
	public static <K,V> Map<K,V> hashMap(K k1,V v1,K k2,V v2,K k3,V v3,K k4,V v4,K k5,V v5,K k6,V v6,K k7,V v7,K k8,V v8,K k9,V v9){
		return praseMap(Arrays.asList(k1,k2,k3,k4,k5,k6,k7,k8,k9),Arrays.asList(v1,v2,v3,v4,v5,v6,v7,v8,v9));
	}
	
	private  static <K,V> Map<K,V> praseMap(List<K> keys,List<V> values){
		Map<K,V> map = new HashMap<K,V>();
		for (int i=0;i<keys.size();i++ ){map.put(keys.get(i),values.get(i));}
		return map;
	}
	
	
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<K,V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
    
    
	
	public static void main(String[] args) {
		hashMap("a","b","c",1);

	}
	
}
