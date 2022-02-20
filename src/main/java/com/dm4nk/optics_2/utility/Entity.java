package com.dm4nk.optics_2.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Entity<T, E> {
    private T t;
    private E e;
}
