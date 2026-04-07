package lib.modules;

import lib.ArrayValue;
import lib.Functions;
import lib.NumberValue;
import lib.StringValue;
import lib.Value;

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
    }
}
