package lib.modules;


import lib.Functions;
import lib.NumberValue;
import lib.Value;

public class math implements Module {
    @Override
    public void init() {
        Functions.set("sin", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Ожидался только один аргумент");
            }
            return new NumberValue(Math.sin(args[0].asNumber()));
        });

        Functions.set("cos", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Ожидался только один аргумент");
            }
            return new NumberValue(Math.cos(args[0].asNumber()));
        });

        Functions.set("tan", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция tan ожидает один аргумент");
            }
            return new NumberValue(Math.tan(args[0].asNumber()));
        });

        Functions.set("atan", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция atan ожидает один аргумент");
            }
            return new NumberValue(Math.atan(args[0].asNumber()));
        });

        Functions.set("asin", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция asin ожидает один аргумент");
            }
            return new NumberValue(Math.asin(args[0].asNumber()));
        });

        Functions.set("acos", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция acos ожидает один аргумент");
            }
            return new NumberValue(Math.acos(args[0].asNumber()));
        });



        // Степенные и логарифмические функции

        Functions.set("pow", args -> {
            if (args.length != 2) {
                throw new RuntimeException("Функция pow ожидает два аргумента: основание и степень");
            }
            double base = args[0].asNumber();
            double exponent = args[1].asNumber();
            return new NumberValue(Math.pow(base, exponent));
        });

        Functions.set("sqrt", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция sqrt ожидает один аргумент");
            }
            double num = args[0].asNumber();
            if (num < 0) {
                throw new RuntimeException("Квадратный корень из отрицательного числа");
            }
            return new NumberValue(Math.sqrt(num));
        });

        Functions.set("cbrt", args -> {    // кубический корень
            if (args.length != 1) {
                throw new RuntimeException("Функция cbrt ожидает один аргумент");
            }
            return new NumberValue(Math.cbrt(args[0].asNumber()));
        });

        Functions.set("exp", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция exp ожидает один аргумент");
            }
            return new NumberValue(Math.exp(args[0].asNumber()));
        });

        Functions.set("log", args -> {
            if (args.length == 1) {
                return new NumberValue(Math.log(args[0].asNumber())); // натуральный логарифм
            } else if (args.length == 2) {
                double num = args[0].asNumber();
                double base = args[1].asNumber();
                return new NumberValue(Math.log(num) / Math.log(base)); // логарифм по основанию
            }
            throw new RuntimeException("Функция log ожидает 1 или 2 аргумента");
        });

        Functions.set("log10", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция log10 ожидает один аргумент");
            }
            return new NumberValue(Math.log10(args[0].asNumber()));
        });

        // Округление
        Functions.set("round", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция round ожидает один аргумент");
            }
            return new NumberValue(Math.round(args[0].asNumber()));
        });

        Functions.set("floor", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция floor ожидает один аргумент");
            }
            return new NumberValue(Math.floor(args[0].asNumber()));
        });

        Functions.set("ceil", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция ceil ожидает один аргумент");
            }
            return new NumberValue(Math.ceil(args[0].asNumber()));
        });



        // минимум и максимум
        Functions.set("max", args -> {
            if (args.length == 0) {
                throw new RuntimeException("Функция max ожидает хотя бы один аргумент");
            }
            double max = args[0].asNumber();
            for (Value arg : args) {
                max = Math.max(max, arg.asNumber());
            }
            return new NumberValue(max);
        });

        Functions.set("min", args -> {
            if (args.length == 0) {
                throw new RuntimeException("Функция min ожидает хотя бы один аргумент");
            }
            double min = args[0].asNumber();
            for (Value arg : args) {
                min = Math.min(min, arg.asNumber());
            }
            return new NumberValue(min);
        });



        // Гиперболические функции
        Functions.set("sinh", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция sinh ожидает один аргумент");
            }
            return new NumberValue(Math.sinh(args[0].asNumber()));
        });

        Functions.set("cosh", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция cosh ожидает один аргумент");
            }
            return new NumberValue(Math.cosh(args[0].asNumber()));
        });

        Functions.set("tanh", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция tanh ожидает один аргумент");
            }
            return new NumberValue(Math.tanh(args[0].asNumber()));
        });



        // Константы

        Functions.set("toDegrees", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция toDegrees ожидает один аргумент");
            }
            return new NumberValue(Math.toDegrees(args[0].asNumber()));
        });

        Functions.set("toRadians", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция toRadians ожидает один аргумент");
            }
            return new NumberValue(Math.toRadians(args[0].asNumber()));
        });

        // прочие

        Functions.set("abs", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция abs ожидает один аргумент");
            }
            return new NumberValue(Math.abs(args[0].asNumber()));
        });

        Functions.set("sign", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция sign ожидает один аргумент");
            }
            double num = args[0].asNumber();
            return new NumberValue(Math.signum(num));
        });

        Functions.set("random", args -> {
            if (args.length == 0) {
                return new NumberValue(Math.random());
            } else if (args.length == 1) {
                double max = args[0].asNumber();
                return new NumberValue(Math.random() * max);
            } else if (args.length == 2) {
                double min = args[0].asNumber();
                double max = args[1].asNumber();
                return new NumberValue(min + Math.random() * (max - min));
            }
            throw new RuntimeException("Функция random ожидает 0, 1 или 2 аргумента");
        });
    }


}
