import java.util.HashMap;

/**
 * @Author zhipong
 * @Date 2021/8/6
 */
public class MyHashMap<K, V> {
    private int CAPACITY = 16;
    private int size = 0;
    private float loadFactory = 0.75f;
    private MyEntry<K, V>[] table;

    public MyHashMap(int CAPACITY) {
        this.CAPACITY = CAPACITY;
        this.table = new MyEntry[this.CAPACITY];
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("can not put a null key");
        }
        int hash = myHash(key);
        int index = getIndexForTable(hash);

        for (MyEntry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        addEntry(hash, key, value, index);
        return;
    }

    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("can not put a null key");
        }
        int hash = myHash(key);
        int index = getIndexForTable(hash);
        for (MyEntry<K, V> e = table[index]; e != null; e = e.next) {
            if (e.hash == hash && e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    private void addEntry(int hash, K key, V value, int index) {
        MyEntry<K, V> entry = table[index];
        table[index] = new MyEntry<>(hash, key, value, entry);
        if (size >= CAPACITY * loadFactory) {
            resize();
        }
        size++;
    }

    private void resize() {
        CAPACITY = CAPACITY * 2;
        MyEntry<K, V>[] newTable = new MyEntry[CAPACITY];
        for (MyEntry<K, V> entry : table) {
            MyEntry<K, V> e = entry;
            if (e != null) {
                entry = null; // 释放旧Entry数组的对象引用
                do{
                    MyEntry<K, V> next = e.next;
                    int i=getIndexForTable(e.hash);
                    e.next=newTable[i];
                    newTable[i]=e;
                    e=next;
                }while(e!=null);
            }
        }
        table=newTable;
    }

    private int getIndexForTable(int hash) {
        return hash & (CAPACITY - 1);
    }

    private int myHash(K key) {
        if(key==null){
            throw new IllegalArgumentException("can not put a null key");
        }
        int hash =key.hashCode();
        return hash;
    }
    public static void main(String[] args) {
        MyHashMap mhm = new MyHashMap(16);

        HashMap<Object, Object> hm = new HashMap<>();
        long s3 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            hm.put(i + " ", i);
        }
        for (int i = 0; i < 1000000; i++) {
            hm.get(i);
        }
        long s4 = System.currentTimeMillis();
        System.out.println("jdkHashMap usetime:" + (s4 - s3));
        // 测试
        long s1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            mhm.put(i + " ", i);
        }
        for (int i = 0; i < 1000000; i++) {
            mhm.get(i);
        }
        long s2 = System.currentTimeMillis();
        System.out.println("MyHashMap usetime:" + (s2 - s1));

    }
}

class MyEntry<K, V> {
    public int hash;
    public K key;
    public V value;
    public MyEntry<K, V> next;

    public MyEntry(int hash, K key, V value, MyEntry<K, V> next) {
        this.hash = hash;
        this.key = key;
        this.value = value;
        this.next = next;
    }
}