package com.example.chattingonlineapplication.Plugins.Interface;

import java.util.HashMap;

public interface ConvertObjectToHashMap<T> {
    HashMap<String, Object> convert(T object);
}
