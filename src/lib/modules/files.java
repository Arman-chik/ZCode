package lib.modules;

import lib.ArrayValue;
import lib.Functions;
import lib.NumberValue;
import lib.StringValue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class files implements Module {

    @Override
    public void init() {
        // Чтение файла
        Functions.set("readFile", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция readFile ожидает один аргумент: путь к файлу");
            }
            String path = args[0].asString();

            try {
                Path filePath = Paths.get(path);
                byte[] bytes = Files.readAllBytes(filePath);
                return new StringValue(new String(bytes, StandardCharsets.UTF_8));
            } catch (NoSuchFileException e) {
                throw new RuntimeException("Файл не найден: " + path);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка чтения файла: " + e.getMessage());
            }
        });

        // Запись в файл
        Functions.set("writeFile", args -> {
            if (args.length != 2) {
                throw new RuntimeException("Функция writeFile ожидает 2 аргумента: путь и содержимое");
            }

            String path = args[0].asString();
            String content = args[1].asString();

            try {
                Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8));
                return NumberValue.ZERO;
            } catch (IOException e) {
                throw new RuntimeException("Ошибка записи в файл: " + e.getMessage());
            }
        });

        // Добавление в файл
        Functions.set("appendFile", args -> {
            if (args.length != 2) {
                throw new RuntimeException("Функция appendFile ожидает 2 аргумента: путь и содержимое");
            }

            String path = args[0].asString();
            String content = args[1].asString();

            try {
                Files.write(Paths.get(path), content.getBytes(StandardCharsets.UTF_8),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                return NumberValue.ZERO;
            } catch (IOException e) {
                throw new RuntimeException("Ошибка добавления в файл: " + e.getMessage());
            }
        });

        // Удаление файла
        Functions.set("deleteFile", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция deleteFile ожидает один аргумент: путь к файлу");
            }

            String path = args[0].asString();

            try {
                boolean deleted = Files.deleteIfExists(Paths.get(path));
                return new NumberValue(deleted ? 1 : 0);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка удаления файла: " + e.getMessage());
            }
        });

        // Чтение строк файла в массив
        Functions.set("readLines", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция readLines ожидает один аргумент: путь к файлу");
            }
            String path = args[0].asString();

            try {
                java.util.List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);

                ArrayValue array = new ArrayValue(lines.size());
                for (int i = 0; i < lines.size(); i++) {
                    array.set(i, new StringValue(lines.get(i)));
                }
                return array;
            } catch (NoSuchFileException e) {
                throw new RuntimeException("Файл не найден: " + path);
            } catch (IOException e) {
                throw new RuntimeException("Ошибка чтения файла: " + e.getMessage());
            }
        });

        // Проверка существования файла
        Functions.set("fileExists", args -> {
            if (args.length != 1) {
                throw new RuntimeException("Функция fileExists ожидает один аргумент: путь к файлу");
            }
            String path = args[0].asString();
            boolean exists = Files.exists(Paths.get(path));
            return new NumberValue(exists ? 1 : 0);
        });

        Functions.set("listFiles", args -> {
            if(args.length!=1) {
                throw new RuntimeException("listFiles ожидает путь");
            }
            File dir = new File(args[0].asString());
            if(!dir.exists()||!dir.isDirectory()) {
                throw new RuntimeException("Путь не является директорией");
            }
            String[] files = dir.list();
            if(files==null) {
                return new ArrayValue(0);
            }
            ArrayValue arr = new ArrayValue(files.length);
            for(int i=0;i<files.length;i++) {
                arr.set(i, new StringValue(files[i]));
            }
            return arr;
        });

        Functions.set("mkdir", args -> {
            if(args.length!=1) {
                throw new RuntimeException("mkdir ожидает путь");
            }
            return new NumberValue(new File(args[0].asString()).mkdirs()?1:0);
        });

        Functions.set("rmdir", args -> {
            if(args.length!=1) {
                throw new RuntimeException("rmdir ожидает путь");
            }
            return new NumberValue(new File(args[0].asString()).delete()?1:0);
        });

        Functions.set("rename", args -> {
            if(args.length!=2) {
                throw new RuntimeException("rename ожидает два названия: старое, новое");
            }
            return new NumberValue(new File(args[0].asString()).renameTo(new File(args[1].asString()))?1:0);
        });

        Functions.set("copyFile", args -> {
            if(args.length!=2) throw new RuntimeException("copyFile ожидает src, dest");
            try { java.nio.file.Files.copy(java.nio.file.Paths.get(args[0].asString()), java.nio.file.Paths.get(args[1].asString()), StandardCopyOption.REPLACE_EXISTING); return NumberValue.ONE; }
            catch(Exception e) { return NumberValue.ZERO; }
        });

        Functions.set("fileSize", args -> {
            if(args.length!=1) {
                throw new RuntimeException("fileSize ожидает путь");
            }
            return new NumberValue(new File(args[0].asString()).length());
        });

        Functions.set("isDir", args -> {
            if(args.length!=1) {
                throw new RuntimeException("isDir ожидает путь");
            }
            return new NumberValue(new File(args[0].asString()).isDirectory()?1:0);
        });

        Functions.set("basename", args -> {
            if(args.length!=1) {
                throw new RuntimeException("basename ожидает путь");
            }
            return new StringValue(new File(args[0].asString()).getName());
        });

        Functions.set("dirname", args -> {
            if(args.length!=1) {
                throw new RuntimeException("dirname ожидает путь");
            }
            return new StringValue(new File(args[0].asString()).getParent());
        });

        Functions.set("getWorkingDir", args -> new StringValue(System.getProperty("user.dir")));
    }
}
