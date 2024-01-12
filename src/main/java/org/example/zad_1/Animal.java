package org.example.zad_1;

public abstract class Animal {
    private final String name;
    private final Integer age;

    public Animal(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public abstract void eat();
}