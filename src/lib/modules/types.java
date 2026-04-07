package lib.modules;

import lib.ArrayValue;
import lib.Functions;
import lib.NumberValue;
import lib.StringValue;
import lib.Value;

public class types implements Module {

    @Override
    public void init() {
        // Проверка, является ли значение числом
        Functions.set("isNumber", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция isNumber ожидает один аргумент");
            }
            try {
                Double.parseDouble(args[0].asString());
                return new NumberValue(1);
            } catch (NumberFormatException e) {
                return new NumberValue(0);
            }
        });

        // Проверка, является ли значение массивом
        Functions.set("isArray", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция isArray ожидает один аргумент");
            }
            return new NumberValue(args[0] instanceof ArrayValue ? 1 : 0);
        });

        // Проверка, является ли значение строкой
        Functions.set("isString", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция isString ожидает один аргумент");
            }
            return new NumberValue(args[0] instanceof StringValue ? 1 : 0);
        });

        // Получение типа значения
        Functions.set("typeOf", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция typeOf ожидает один аргумент");
            }

            Value value = args[0];

            if (value instanceof NumberValue) {
                return new StringValue("number");
            }
            if (value instanceof StringValue) {
                return new StringValue("string");
            }
            if (value instanceof ArrayValue) {
                return new StringValue("array");
            }

            return new StringValue("unknown");
        });
    }
}
