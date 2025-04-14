import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        // Создаем объекты GameProgress
        GameProgress progress1 = new GameProgress(100, 3, 1, 0.0);
        GameProgress progress2 = new GameProgress(85, 5, 3, 125.5);
        GameProgress progress3 = new GameProgress(45, 10, 10, 578.2);

        // Сохраняем объекты
        String saveDir = "C:/Games/savegames/"; // Укажите ваш путь
        List<String> savedFiles = new ArrayList<>();

        savedFiles.add(saveGame(saveDir + "save1.dat", progress1));
        savedFiles.add(saveGame(saveDir + "save2.dat", progress2));
        savedFiles.add(saveGame(saveDir + "save3.dat", progress3));

        // Архивируем файлы
        String zipFile = saveDir + "saves.zip";
        zipFiles(zipFile, savedFiles);

        // Удаляем исходные файлы
        deleteFiles(savedFiles);
    }

    public static String saveGame(String filePath, GameProgress progress) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(progress);
            return filePath;
        } catch (IOException e) {
            System.err.println("Ошибка сохранения файла: " + e.getMessage());
            return null;
        }
    }

    public static void zipFiles(String zipPath, List<String> filesToZip) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filePath : filesToZip) {
                if (filePath == null) continue;

                File file = new File(filePath);
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                } catch (IOException e) {
                    System.err.println("Ошибка при добавлении файла в архив: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка создания архива: " + e.getMessage());
        }
    }

    public static void deleteFiles(List<String> filesToDelete) {
        for (String filePath : filesToDelete) {
            if (filePath == null) continue;

            File file = new File(filePath);
            if (file.delete()) {
                System.out.println("Файл удален: " + filePath);
            } else {
                System.err.println("Не удалось удалить файл: " + filePath);
            }
        }
    }
}