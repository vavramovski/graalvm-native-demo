package com.example.demo.payload.resources;

public interface ResourceAssemblerSupport<T, D> {

    T fromResource(D resource);

    T fromResource(T oldEntity, D resource);
}
