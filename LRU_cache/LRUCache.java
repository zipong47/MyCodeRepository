package LRU_cache;

import java.util.HashMap;

/**
 * @Author zhipong
 * @Date 2021/8/9
 */
public class LRUCache {

    class DLinkNode {
        int key;
        int value;
        DLinkNode pre;
        DLinkNode next;

        public DLinkNode() {

        }

        public DLinkNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    private HashMap<Integer, DLinkNode> cache;
    private int size;
    private int capacity;
    private DLinkNode head, tail;

    public LRUCache(int capacity) {
        size = 0;
        this.capacity = capacity;
        cache = new HashMap<>();
        head = new DLinkNode();
        tail = new DLinkNode();
        head.next = tail;
        tail.pre = head;
    }

    public void put(int key, int value) {
        DLinkNode hit = cache.get(key);
        if(hit==null){
            DLinkNode newNode = new DLinkNode(key, value);
            cache.put(key,newNode);
            addNodeToHead(newNode);
            size++;
            if(size>capacity){
                DLinkNode tail=removeTail();
                cache.remove(tail.key);
                size--;
            }
        }else{
            hit.value=value;
            moveToHead(hit);
        }
    }

    private DLinkNode removeTail() {
        DLinkNode node = tail.pre;
        removeNode(node);
        return node;
    }

    public int get(int key) {
        DLinkNode hit = cache.get(key);
        if (hit == null) {
            return -1;
        }
        moveToHead(hit);
        return hit.value;
    }

    private void moveToHead(DLinkNode node) {
        removeNode(node);
        addNodeToHead(node);
    }

    private void addNodeToHead(DLinkNode node) {
        node.pre = head;
        node.next = head.next;
        head.next.pre = node;
        head.next = node;
    }

    private void removeNode(DLinkNode node) {
        node.pre.next = node.next;
        node.next.pre = node.pre;
    }
}
