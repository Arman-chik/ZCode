package lib.modules;

import lib.*;

import java.util.Scanner;

public class std implements Module {


    @Override
    public void init() {
        Functions.set("echo", args -> {
            for (Value arg : args) {
                System.out.println(arg.asString());
            }
            return NumberValue.ZERO;
        });

        Functions.set("print", args -> {
            for (Value arg : args) {
                System.out.print(arg.asString());
            }
            return NumberValue.ZERO;
        });

        Functions.set("input", args -> {
            Scanner scanner = new Scanner(System.in);
            if (args.length > 0) {
                System.out.print(args[0].asString());
            }
            return new StringValue(scanner.nextLine());
        });

        Functions.set("newArray", new Function() {
            @Override
            public Value execute(Value... args) {
                return createArray(args, 0);
            }

            private ArrayValue createArray(Value[] args, int index) {
                final int size = (int) args[index].asNumber();
                final int last = args.length - 1;
                ArrayValue array = new ArrayValue(size);
                if (index == last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, NumberValue.ZERO);
                    }
                } else if (index < last) {
                    for (int i = 0; i < size; i++) {
                        array.set(i, createArray(args, index + 1));
                    }
                }
                return array;
            }
        });

        Functions.set("push", args -> {
            if (args.length < 2) {
                throw new RuntimeException("push ожидает минимум 2 аргумента");
            }
            if (!(args[0] instanceof ArrayValue)) {
                throw new RuntimeException("Первый аргумент push должен быть массивом");
            }


            ArrayValue array = (ArrayValue) args[0];

            ArrayValue newArray = new ArrayValue(array.size() + args.length - 1);
            for (int i = 0; i < array.size(); i++) {
                newArray.set(i, array.get(i));
            }

            for (int i = 1; i < args.length; i++) {
                newArray.set(array.size() + i - 1, args[i]);
            }

            return newArray;
        });

        Functions.set("pop", args -> {
            if (args.length != 1) {
                throw new RuntimeException("pop ожидает 1 аргумент");
            }
            if (!(args[0] instanceof ArrayValue)) {
                throw new RuntimeException("Аргумент pop должен быть массивом");
            }
            ArrayValue array = (ArrayValue) args[0];
            if (array.size() == 0) {
                throw new RuntimeException("Нельзя выполнить pop на пустом массиве");
            }


            ArrayValue newArray = new ArrayValue(array.size() - 1);
            for (int i = 0; i < newArray.size(); i++) {
                newArray.set(i, array.get(i));
            }
            return newArray;
        });


        // Длина массива
        Functions.set("len", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция len ожидает ровно один аргумент");
            }
            if (args[0] instanceof ArrayValue) {
                return new NumberValue(((ArrayValue) args[0]).size());
            }
            if (args[0] instanceof StringValue) {
                return new NumberValue(args[0].asString().length());
            }
            throw new RuntimeException("Аргумент функции len должен быть массивом или строкой");
        });

        // Разворот массива
        Functions.set("reverseArray", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция reverse ожидает один аргумент");
            }

            if (args[0] instanceof ArrayValue) {
                ArrayValue array = (ArrayValue) args[0];
                ArrayValue reversed = new ArrayValue(array.size());
                for (int i = 0; i < array.size(); i++) {
                    reversed.set(i, array.get(array.size() - 1 - i));
                }
                return reversed;
            } else {
                // Для строк разворот уже есть в strings модуле, но оставим для обратной совместимости
                String str = args[0].asString();
                return new StringValue(new StringBuilder(str).reverse().toString());
            }
        });
    }
}
