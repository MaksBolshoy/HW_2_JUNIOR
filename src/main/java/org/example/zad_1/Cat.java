package org.example.zad_1;

public class Cat extends Animal implements iAnimal {

    private final Integer weight;

    public Cat(String name, Integer age, Integer weight) {
        super(name, age);
        this.weight = weight;
    }

    public void hunt() {
        System.out.println("Кот охотится...");
    }

    @Override
    public void eat() {
        System.out.println("Кот ест...");
    }

    @Override
    public void makeSound() {
        System.out.println("Мяу!");
    }

}