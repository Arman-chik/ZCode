package lib;


import java.util.HashMap;
import java.util.Map;

public class Functions {


    private static Map<String, Function> functions;


    static {
        // Математические функции.
        functions = new HashMap<>();
        functions.put("sin", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Ожидался только один аргумент");
                }
                return new NumberValue(Math.sin(args[0].asNumber()));
            }
        });

        functions.put("cos", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Ожидался только один аргумент");
                }
                return new NumberValue(Math.cos(args[0].asNumber()));
            }
        });

        functions.put("round", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Ожидался только один аргумент");
                }
                double number = args[0].asNumber();

                return new NumberValue(Math.round(number));
            }
        });

        functions.put("sqrt", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция sqrt ожидает один аргумент");
                }
                double num = args[0].asNumber();
                if (num < 0) {
                    throw new RuntimeException("Квадратный корень из отрицательного числа");
                }
                return new NumberValue(Math.sqrt(num));
            }
        });

        functions.put("pow", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 2) {
                    throw new RuntimeException("Функция pow ожидает два аргумента: основание и степень");
                }
                double base = args[0].asNumber();
                double exponent = args[1].asNumber();
                return new NumberValue(Math.pow(base, exponent));
            }
        });

        functions.put("abs", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция abs ожидает один аргумент");
                }
                return new NumberValue(Math.abs(args[0].asNumber()));
            }
        });

        functions.put("log", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length == 1) {
                    return new NumberValue(Math.log(args[0].asNumber()));
                } else if (args.length == 2) {
                    double num = args[0].asNumber();
                    double base = args[1].asNumber();
                    return new NumberValue(Math.log(num) / Math.log(base));
                }
                throw new RuntimeException("Функция log ожидает 1 или 2 аргумента");
            }
        });

        functions.put("exp", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция exp ожидает один аргумент");
                }
                return new NumberValue(Math.exp(args[0].asNumber()));
            }
        });




        functions.put("echo", new Function() {
            @Override
            public Value execute(Value... args) {
                for (Value arg : args) {
                    System.out.println(arg.asString());
                }
                return NumberValue.ZERO;
            }
        });


        functions.put("len", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция len ожидает ровно один аргумент");
                }
                if (args[0] instanceof ArrayValue) {
                    return new NumberValue(((ArrayValue)args[0]).size());
                }
                throw new RuntimeException("Аргумент функции len должен быть массивом");
            }
        });

        functions.put("length", new Function() {  // для строк
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция length ожидает ровно один аргумент");
                }
                return new NumberValue(args[0].asString().length());
            }
        });

        functions.put("newArray", new Function() {

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

        functions.put("indexOf", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length < 2) throw new RuntimeException("indexOf требует как минимум 2 аргумента");
                String str = args[0].asString();
                String substr = args[1].asString();
                int fromIndex = args.length > 2 ? (int)args[2].asNumber() : 0;
                return new NumberValue(str.indexOf(substr, fromIndex));
            }
        });

        functions.put("substring", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 3) throw new RuntimeException("substring требует 3 аргумента");
                String str = args[0].asString();
                int start = (int)args[1].asNumber();
                int end = (int)args[2].asNumber();
                return new StringValue(str.substring(start, end));
            }
        });


    }

    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }

    public static Function get(String key) {
        if (!isExists(key)) {
            throw new RuntimeException("Неизвестная функция " + key);
        }
        return functions.get(key);
    }

    public static void set(String key, Function function) {
        functions.put(key, function);
    }
}
