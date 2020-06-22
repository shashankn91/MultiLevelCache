package com.phonepe;

import com.phonepe.models.CacheResponse;
import com.phonepe.models.Element;
import com.phonepe.models.OperationType;
import com.phonepe.models.State;
import lombok.Getter;
import lombok.extern.java.Log;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Log
public class Cache implements ICache{

    private final String id = UUID.randomUUID().toString();
    private final Integer capacity;
    private final Integer readTime;
    private final Integer writeTime;
    private final Integer level;
    private final Queue<Element> elements= new LinkedList<>();
    private final Map<String,Element> elementsMap= new HashMap<>();


    public Cache(Integer capacity, Integer readTime, Integer writeTime, Integer level) {
        this.capacity = capacity;
        this.readTime = readTime;
        this.writeTime = writeTime;
        this.level = level;
    }


    @Override
     public CacheResponse read(String key){

        List<Element> elementWithKey = elements.stream().filter(i -> i.getKey().equals(key)).collect(Collectors.toList());
        if(elementWithKey.size() == 0){
            return new CacheResponse(State.NOT_FOUND, readTime, OperationType.READ, this, key);
        }
        else if (elementWithKey.size() == 1){
            return new CacheResponse(State.FOUND, readTime, OperationType.READ, this, key, elementWithKey.get(0).getValue());

        }
        else{
            log.severe("Multiple elements with Same key");
           throw new RuntimeException("Multiple elements with Same key");
        }

    }


    @Override
     public synchronized CacheResponse write(String key, String value){

        List<Element> elementWithKey = elements.stream().filter(i -> i.getKey().equals(key)).collect(Collectors.toList());
         Element currentElement = new Element(key,value);
        if(elementWithKey.size() == 0){
            if(this.capacity == elements.size()){
                elements.poll();
            }
            elements.add(currentElement);
            return new CacheResponse(State.NOT_FOUND, writeTime, OperationType.WRITE, this, currentElement.getKey(),currentElement.getValue());
        }
        else if (elementWithKey.size() == 1){
            if(elementWithKey.get(0).getValue().equals(value)){
                return new CacheResponse(State.FOUND, 0, OperationType.WRITE, this, key, value);
            }
            else {
                elements.remove(elementWithKey.get(0));
                elements.add(currentElement);
                return new CacheResponse(State.FOUND,writeTime, OperationType.WRITE, this, key, value);

            }

        }
        else{
            log.severe("Multiple elements with Same key");
            throw new RuntimeException("Multiple elements with Same key");
        }

    }
}
