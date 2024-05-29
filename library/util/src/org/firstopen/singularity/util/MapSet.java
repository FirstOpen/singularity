/* 
 * Copyright (c) 2005 J. Thomas Rose. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 	http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.firstopen.singularity.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 
 * Essentially a HashMap that impelements the Set Interface.
 * Issues: 
 * 
 * <li> Cannot fully implement the Map interface because of
 * <code>remove(object)</code> Method clash between Map and Collection.
 * </li>
 * <li>
 * Also  keySet() per the Map interface should return a Set<K>, however,
 * to implement the Set Inteface the backing HashMap<Object, V> had to be created
 * to support the creation of keys on the add(V object) method.
 * </li>
 * <li> <code>add(V object)</code> for objects that implement the 
 * org.firstopen.singularity.util.Named interface will use the object name. 
 * otherwise the object's hashcode is used for the key.
 * </li>
 * 
 * @author TomRose
 * @version $Id$
 * 
 */
public class MapSet<K,V> implements Set<V>, Serializable{

    /*
     * backing Map object.
     */
    private HashMap<Object,V> map = new HashMap<Object,V>();
    

    /**
     * 
     */
    public MapSet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /* (non-Javadoc)
     * @see java.util.Set#size()
     */
    public int size() {
        return map.size();
    }

    /* (non-Javadoc)
     * @see java.util.Set#isEmpty()
     */
    public boolean isEmpty() {
         return map.isEmpty();
    }

    /* (non-Javadoc)
     * @see java.util.Set#contains(java.lang.Object)
     */
    public boolean contains(Object o) {
        return map.containsValue(o);
    }

    /* (non-Javadoc)
     * @see java.util.Set#iterator()
     */
    public Iterator<V> iterator() {
        return map.values().iterator();
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray()
     */
    public Object[] toArray() {
        return map.values().toArray();
    }

    /* (non-Javadoc)
     * @see java.util.Set#toArray(T[])
     */
    public <T> T[] toArray(T[] a) {
       return map.values().toArray(a);
    }

    /**
     * if an instance of Named use the Name 
     * as as the key, otherwise use the Hashcode as 
     * the key.
     * (non-Javadoc)
     * @see java.util.Set#add(E)
     */
    public boolean add(V o) {
        if (o == null) {
            throw new IllegalArgumentException("value object cannot be null");
        }
        boolean contains = map.containsValue(o);
        map.put(getKey(o), o);
        return !contains;
    }

    /* (non-Javadoc)
     * @see java.util.Set#remove(java.lang.Object)
     */
    public boolean remove(Object o) {
        boolean contains = map.containsValue(o);
        map.remove(getKey(o));
        return  contains;
    }

    public void delete(K key) {
        map.remove(key);
    }
    
    /* (non-Javadoc)
     * @see java.util.Set#containsAll(java.util.Collection)
     */
    public boolean containsAll(Collection<?> c) {
        Collection<V> values = map.values();
        return values.containsAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#addAll(java.util.Collection)
     */
    public boolean addAll(Collection<? extends V> c) {
        boolean changed = false;
       for (V value : c) {
           if (add(value)) changed = true;
       }
        return changed;
    }

    /* (non-Javadoc)
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    public boolean retainAll(Collection<?> c) {
        Collection<V> values = map.values();
        return values.retainAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#removeAll(java.util.Collection)
     */
    public boolean removeAll(Collection<?> c) {
        Collection<V> values = map.values();
        return values.removeAll(c);
    }

    /* (non-Javadoc)
     * @see java.util.Set#clear()
     */
    public void clear() {
       map.clear();
        
    }

    private Object getKey(Object o) {
        Object key = o.hashCode();
        if (o instanceof Named) {
               key = ((Named)o).getName();
        }
        
        return key;
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        
        return map.containsKey(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#containsValue(java.lang.Object)
     */
    public boolean containsValue(Object value) {
      
        return map.containsValue(value);
    }

    /* (non-Javadoc)
     * @see java.util.Map#get(java.lang.Object)
     */
    public V get(Object key) {
       
        return map.get(key);
    }

    /* (non-Javadoc)
     * @see java.util.Map#put(K, V)
     */
    public V put(K key, V value) {
        
        return map.put(key, value);
    }

    /* (non-Javadoc)
     * @see java.util.Map#putAll(java.util.Map)
     */
    public void putAll(Map<? extends K, ? extends V> t) {
        map.putAll(t);
        
    }

    /* (non-Javadoc)
     * @see java.util.Map#keySet()
     */
    public Set<Object> keySet() {
        
        return map.keySet();
    }

    /* (non-Javadoc)
     * @see java.util.Map#values()
     */
    public Collection<V> values() {
       
        return map.values();
    }

    /* (non-Javadoc)
     * @see java.util.Map#entrySet()
     */
    public Set<Entry<Object, V>> entrySet() {
        
        return map.entrySet();
    }
    
 
}
