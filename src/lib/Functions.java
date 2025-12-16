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

        functions.put("tan", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция tan ожидает один аргумент");
                }
                return new NumberValue(Math.tan(args[0].asNumber()));
            }
        });

        functions.put("atan", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция atan ожидает один аргумент");
                }
                return new NumberValue(Math.atan(args[0].asNumber()));
            }
        });


        // Строковые функции

        functions.put("toUpperCase", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция toUpperCase ожидает один аргумент");
                }
                return new StringValue(args[0].asString().toUpperCase());
            }

        });


        functions.put("toLowerCase", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция toLowerCase ожидает один аргумент");
                }
                return new StringValue(args[0].asString().toLowerCase());
            }
        });



        functions.put("trim", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция trim ожидает один аргумент");
                }

                return new StringValue(args[0].asString().trim());
            }
        });


        functions.put("replace", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 3) {
                    throw new RuntimeException("Функция replace ожидает три аргумента: строка, что заменить, на что заменить");
                }
                String str = args[0].asString();

                String target = args[1].asString();
                String replacement = args[2].asString();

                return new StringValue(str.replace(target, replacement));
            }
        });


        functions.put("startsWith", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 2) {
                    throw new RuntimeException("Функция startsWith ожидает два аргумента: строка и префикс");
                }
                String str = args[0].asString();
                String prefix = args[1].asString();

                return new NumberValue(str.startsWith(prefix) ? 1 : 0);
            }
        });


        functions.put("endsWith", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 2) {
                    throw new RuntimeException("Функция endsWith ожидает два аргумента: строка и суффикс");
                }

                String str = args[0].asString();
                String suffix = args[1].asString();

                return new NumberValue(str.endsWith(suffix) ? 1 : 0);
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

        functions.put("echo", new Function() {
            @Override
            public Value execute(Value... args) {
                for (Value arg : args) {
                    System.out.println(arg.asString());
                }
                return NumberValue.ZERO;
            }
        });

        // Массивы

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



        functions.put("push", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length < 2) {
                    throw new RuntimeException("Функция push ожидает как минимум 2 аргумента: массив и элементы");
                }
                if (!(args[0] instanceof ArrayValue)) {
                    throw new RuntimeException("Первый аргумент функции push должен быть массивом");
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
            }
        });


        functions.put("pop", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция pop ожидает один аргумент: массив");
                }
                if (!(args[0] instanceof ArrayValue)) {
                    throw new RuntimeException("Аргумент функции pop должен быть массивом");
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
            }
        });


        functions.put("reverse", new Function() {
            @Override
            public Value execute(Value... args) {
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
                    // Для строк
                    String str = args[0].asString();
                    return new StringValue(new StringBuilder(str).reverse().toString());
                }
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



        // Прочие функции.

        functions.put("random", new Function() {
            @Override
            public Value execute(Value... args) {
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
            }
        });


        functions.put("floor", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция floor ожидает один аргумент");
                }

                return new NumberValue(Math.floor(args[0].asNumber()));
            }
        });


        functions.put("ceil", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция ceil ожидает один аргумент");
                }

                return new NumberValue(Math.ceil(args[0].asNumber()));
            }
        });


        functions.put("max", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length == 0) {
                    throw new RuntimeException("Функция max ожидает хотя бы один аргумент");
                }
                double max = args[0].asNumber();

                for (Value arg : args) {
                    max = Math.max(max, arg.asNumber());
                }

                return new NumberValue(max);
            }
        });


        functions.put("min", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length == 0) {
                    throw new RuntimeException("Функция min ожидает хотя бы один аргумент");
                }
                double min = args[0].asNumber();

                for (Value arg : args) {
                    min = Math.min(min, arg.asNumber());
                }

                return new NumberValue(min);
            }
        });



        // Типы элементов и проверки
        functions.put("isNumber", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция isNumber ожидает один аргумент");
                }
                try {
                    Double.parseDouble(args[0].asString());
                    return new NumberValue(1);
                } catch (NumberFormatException e) {
                    return new NumberValue(0);
                }
            }
        });

        functions.put("isArray", new Function() {
            @Override
            public Value execute(Value... args) {
                if (args.length != 1) {
                    throw new RuntimeException("Функция isArray ожидает один аргумент");
                }
                return new NumberValue(args[0] instanceof ArrayValue ? 1 : 0);
            }
        });



        functions.put("typeOf", new Function() {
            @Override
            public Value execute(Value... args) {
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
