package org.example.zad_1;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class Program {

    /*
        Создайте абстрактный класс "Animal" с полями "name" и "age".
        Реализуйте два класса-наследника от "Animal" (например, "Dog" и "Cat")
        с уникальными полями и методами.
        Создайте массив объектов типа "Animal" и с использованием Reflection API
        выполните следующие действия:
        Выведите на экран информацию о каждом объекте.
        Вызовите метод "makeSound()" у каждого объекта,
        если такой метод присутствует.
     */
    public static void main(String[] args)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {

        Class<Cat> catClass = Cat.class;
        Class<Dog> dogClass = Dog.class;
        Animal[] animals = new Animal[4];

        Constructor<?> catConstructor =
                catClass.getConstructor(String.class,
                        Integer.class, Integer.class);
        Constructor<?> dogConstructor =
                dogClass.getConstructor(String.class,
                        Integer.class, Integer.class);

        Object cat1 = catConstructor.newInstance("Барсик", 2, 5);
        Object cat2 = catConstructor.newInstance("Мурзик", 3, 3);
        animals[0] = (Animal) cat1;
        animals[1] = (Animal) cat2;

        Object dog1 = dogConstructor.newInstance("Бобик", 7, 4);
        Object dog2 = dogConstructor.newInstance("Шарик", 6, 3);
        animals[2] = (Animal) dog1;
        animals[3] = (Animal) dog2;

        for (Animal animal : animals) {
            System.out.println(animal.getClass().getSimpleName());
            printObjectInfo(animal);
            actionMethodForName(animal, "makeSound");
            System.out.println("=======================");
        }
    }

    private static void printObjectInfo(Object obj) {
        Field[] fields = obj.getClass().getSuperclass().getDeclaredFields();
        printFields(fields, obj);
        fields = obj.getClass().getDeclaredFields();
        printFields(fields, obj);
    }

    private static void printFields(Field[] fields, Object obj) {
        Arrays.stream(fields).forEach(field -> {
            try {
                field.setAccessible(true);
                System.out.printf("%s = %s\n", field.getName(), field.get(obj));
                field.setAccessible(false);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                System.out.println("Ошибка доступа к полям объекта: "
                        + e.getMessage());
            }
        });
    }

    private static void actionMethodForName(Object obj, String nameMethod) {
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals(nameMethod)) {
                try {
                    method.invoke(obj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.out.printf(
                            "При вызове метода %s, произошла ошибка %s\n",
                            method.getName(), e.getMessage());
                }
            }
        }
    }
}