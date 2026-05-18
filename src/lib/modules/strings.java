package lib.modules;

import lib.*;

import java.util.regex.Pattern;

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

        Functions.set("split", args -> {
            if(args.length!=2) throw new RuntimeException("split ожидает 2 аргумента");
            String[] parts = args[0].asString().split(Pattern.quote(args[1].asString()), -1);
            ArrayValue arr = new ArrayValue(parts.length);
            for(int i=0;i<parts.length;i++) arr.set(i, new StringValue(parts[i]));
            return arr;
        });

        Functions.set("join", args -> {
            if(args.length!=2 || !(args[0] instanceof ArrayValue)) throw new RuntimeException("join ожидает массив, разделитель");
            ArrayValue arr=(ArrayValue)args[0]; String sep=args[1].asString(); StringBuilder sb=new StringBuilder();
            for(int i=0;i<arr.size();i++){ if(i>0) sb.append(sep); sb.append(arr.get(i).asString()); }
            return new StringValue(sb.toString());
        });

        Functions.set("includes", args -> { if(args.length!=2) throw new RuntimeException("includes ожидает 2 аргумента"); return new NumberValue(args[0].asString().contains(args[1].asString()) ? 1 : 0); });
        Functions.set("charAt", args -> {
            if(args.length!=2) throw new RuntimeException("charAt ожидает 2 аргумента");
            String s=args[0].asString(); int idx=(int)args[1].asNumber();
            if(idx<0||idx>=s.length()) throw new RuntimeException("charAt: индекс вне диапазона");
            return new StringValue(String.valueOf(s.charAt(idx)));
        });

        Functions.set("repeat", args -> {
            if(args.length!=2) throw new RuntimeException("repeat ожидает 2 аргумента");
            int n=(int)args[1].asNumber(); if(n<0) n=0;
            StringBuilder sb=new StringBuilder(); for(int i=0;i<n;i++) sb.append(args[0].asString());
            return new StringValue(sb.toString());
        });

        Functions.set("padLeft", args -> {
            if(args.length<2||args.length>3) throw new RuntimeException("padLeft ожидает 2-3 аргумента");
            String s=args[0].asString(); int len=(int)args[1].asNumber(); char pad=args.length==3?args[2].asString().charAt(0):' ';
            if(s.length()>=len) return new StringValue(s);
            StringBuilder sb=new StringBuilder(); for(int i=0;i<len-s.length();i++) sb.append(pad); sb.append(s);
            return new StringValue(sb.toString());
        });

        Functions.set("padRight", args -> {
            if(args.length<2||args.length>3) throw new RuntimeException("padRight ожидает 2-3 аргумента");
            String s=args[0].asString(); int len=(int)args[1].asNumber(); char pad=args.length==3?args[2].asString().charAt(0):' ';
            if(s.length()>=len) return new StringValue(s);
            StringBuilder sb=new StringBuilder(s); for(int i=0;i<len-s.length();i++) sb.append(pad);
            return new StringValue(sb.toString());
        });

        Functions.set("count", args -> {
            if(args.length!=2) throw new RuntimeException("count ожидает 2 аргумента");
            String s=args[0].asString(), sub=args[1].asString();
            if(sub.isEmpty()) return NumberValue.ZERO;
            int count=0, idx=0; while((idx=s.indexOf(sub, idx))!=-1){ count++; idx+=sub.length(); }
            return new NumberValue(count);
        });

        Functions.set("slice", args -> {
            if(args.length<2||args.length>3) throw new RuntimeException("slice ожидает 2-3 аргумента");
            String s=args[0].asString(); int start=(int)args[1].asNumber(), end=args.length==3?(int)args[2].asNumber():s.length();
            if(start<0) start=Math.max(0, s.length()+start); if(end<0) end=Math.max(0, s.length()+end);
            return new StringValue(s.substring(Math.min(start,s.length()), Math.min(end,s.length())));
        });

        Functions.set("remove", args -> {
            if(args.length!=2) {
                throw new RuntimeException("remove ожидает 2 аргумента");
            }
            return new StringValue(args[0].asString().replace(args[1].asString(), ""));
        });
    }
}
