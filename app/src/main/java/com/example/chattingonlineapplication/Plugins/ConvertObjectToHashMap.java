package com.example.chattingonlineapplication.Plugins;

import java.util.HashMap;

interface ConvertObjectToHashMap<T> {
    HashMap<String, Object> convert(T object);
}
