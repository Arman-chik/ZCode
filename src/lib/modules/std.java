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
    }

}
