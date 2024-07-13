import java.io.*;

public class Main {
    public static void main(String[] args) {
        Parameters parameters = Analyzer.parametersAnalyzer(args);
        BufferedReader reader;
        String resInt = "";
        String resFloat = "";
        String resString = "";
        for (String filePath : parameters.inputFiles()) {
            try {
                reader = new BufferedReader(new FileReader(filePath));
                String line;
                while ((line = reader.readLine()) != null) {
                    String analysis = Analyzer.lineAnalyzer(line);
                    switch (analysis) {
                        case "str" -> resString += line + "\n";
                        case "float" -> resFloat += line + "\n";
                        case "int" -> resInt += line + "\n";
                    }
                }
                reader.close();
            } catch (FileNotFoundException e) {
                System.out.printf("Файла %s не существует", filePath);
            } catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
            }
        }
        write(resString, "strings", parameters, "String");
        write(resFloat, "floats", parameters, "Float");
        write(resInt, "integers", parameters, "Integer");
    }

    // Метод для получения статистики по уже отсортированным типам данных
    private static String getStatistics(String str, String type, String typeStat) {
        int length = str.split("\n").length;
        String result = String.format("Кол-во элементов у типа %s - %d\n", type, length);
        switch (typeStat) {
            case "integer" -> {
                long max = 0;
                // TODO: сделать отдельный класс для анализа и статистики
                long min = 999999999999999999L;
                long sum = 0;
                for (String s : str.split("\n")) {
                    long num = 0;
                    try {
                        num = Long.parseLong(s);
                    } catch (NumberFormatException ignored) {
                    }
                    sum += num;
                    max = Math.max(max, num);
                    min = Math.min(min, num);
                }
                result += String.format("Максимальное число = %d\nМинимальное число = %d\nСумма элементов = %d\nСреднее значение = %.2f\n", max, min, sum, (float) sum / length);
            }
            case "float" -> {
                double max = 0;
                double min = 999999999;
                double sum = 0;
                for (String s : str.split("\n")) {
                    double num = 0;
                    try {
                        num = Double.parseDouble(s);
                    } catch (NumberFormatException ignored) {
                    }
                    sum += num;
                    max = Math.max(max, num);
                    min = Math.min(min, num);
                }
                // если в минимальное число очень маленькое (содержит е), то выводим немного по другому
                result += (String.valueOf(min)).toLowerCase().contains("e") ? String.format("Максимальное число = %f\nМинимальное число = %e\nСумма элементов = %f\nСреднее значение = %f\n", max, min, sum, sum / length) : String.format("Максимальное число = %f\nМинимальное число = %f\nСумма элементов = %f\nСреднее значение = %f\n", max, min, sum, sum / length);
                ;
            }
            case "string" -> {
                int max = 0;
                int min = 999999999;
                for (String s : str.split("\n")) {
                    max = Math.max(max, s.length());
                    min = Math.min(min, s.length());
                }
                result += String.format("Максимальная длина строки = %d\nМинимальная длина строки = %d\n", max, min);
            }
        }
        return result;
    }

    // Метод для записи результата работы программы в текстовые файлы
    private static void write(String result, String name, Parameters parameters, String type) {
        if (!result.equals("")) {
            try {
                // Получаем путь куда сохранять, если в параметрах его нет, то получаем папку, где находимся и сохраняем
                String path = parameters.path().equals("") ? System.getProperty("user.dir") : parameters.path();
                path += "\\" + parameters.prefix() + name + ".txt";
                // !parameters.rewrite() - для добавления/перезаписывания файла
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, !parameters.rewrite()));
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
            }
            // Если опция для отображеня статистики была указана (short/full info), то выводим её в консоль
            if (!parameters.info().equals("")) {
                String message = getStatistics(result, type, parameters.info().equals("full") ? type.toLowerCase() : "");
                System.out.println(message);
            }
        }
    }
}