package org.example.zad_1;

public class Dog extends Animal implements iAnimal {

    private final Integer countPaws;

    public Dog(String name, Integer age, Integer countPaws) {
        super(name, age);
        this.countPaws = countPaws;
    }

    public void guard() {
        System.out.println("Собака сторожит...");
    }

    @Override
    public void eat() {
        System.out.println("Кот ест..");
    }

    @Override
    public void makeSound() {
        System.out.println("Гав!");
    }
}