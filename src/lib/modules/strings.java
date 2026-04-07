package lib.modules;

import lib.Functions;
import lib.NumberValue;
import lib.StringValue;
import lib.Value;

public class strings implements Module {

    @Override
    public void init() {
        // Преобразование регистра
        Functions.set("toUpperCase", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция toUpperCase ожидает один аргумент");
            }
            return new StringValue(args[0].asString().toUpperCase());
        });

        Functions.set("toLowerCase", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция toLowerCase ожидает один аргумент");
            }
            return new StringValue(args[0].asString().toLowerCase());
        });

        // Удаление пробелов
        Functions.set("trim", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция trim ожидает один аргумент");
            }
            return new StringValue(args[0].asString().trim());
        });

        // Замена подстроки
        Functions.set("replace", args -> {
            if (args.length != 3) {
                throw new RuntimeException("Функция replace ожидает три аргумента: строка, что заменить, на что заменить");
            }
            String str = args[0].asString();
            String target = args[1].asString();
            String replacement = args[2].asString();
            return new StringValue(str.replace(target, replacement));
        });

        // Проверка начала/конца строки
        Functions.set("startsWith", args -> {
            if (args.length != 2) {
                throw new RuntimeException("Функция startsWith ожидает два аргумента: строка и префикс");
            }
            String str = args[0].asString();
            String prefix = args[1].asString();
            return new NumberValue(str.startsWith(prefix) ? 1 : 0);
        });

        Functions.set("endsWith", args -> {
            if (args.length != 2) {
                throw new RuntimeException("Функция endsWith ожидает два аргумента: строка и суффикс");
            }
            String str = args[0].asString();
            String suffix = args[1].asString();
            return new NumberValue(str.endsWith(suffix) ? 1 : 0);
        });

        // Длина строки
        Functions.set("length", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция length ожидает ровно один аргумент");
            }
            return new NumberValue(args[0].asString().length());
        });

        // Поиск подстроки
        Functions.set("indexOf", args -> {
            if (args.length < 2) {
                throw new RuntimeException("indexOf требует как минимум 2 аргумента");
            }
            String str = args[0].asString();
            String substr = args[1].asString();
            int fromIndex = args.length > 2 ? (int) args[2].asNumber() : 0;
            return new NumberValue(str.indexOf(substr, fromIndex));
        });

        // Извлечение подстроки
        Functions.set("substring", args -> {
            if (args.length != 3) {
                throw new RuntimeException("substring требует 3 аргумента");
            }
            String str = args[0].asString();
            int start = (int) args[1].asNumber();
            int end = (int) args[2].asNumber();
            return new StringValue(str.substring(start, end));
        });

        // Разворот строки
        Functions.set("reverseString", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция reverse ожидает один аргумент");
            }
            String str = args[0].asString();
            return new StringValue(new StringBuilder(str).reverse().toString());
        });
    }
}
