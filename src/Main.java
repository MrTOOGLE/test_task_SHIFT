import java.io.*;

public class Main {
    public static void main(String[] args) {
        Parameters parameters = parametersAnalyzer(args);
        BufferedReader reader;
        String resInt = "";
        String resFloat = "";
        String resString = "";
        for (String filePath : parameters.inputFiles()) {
            try {
                reader = new BufferedReader(new FileReader(filePath));
                String line;
                while ((line = reader.readLine()) != null) {
                    String analysis = lineAnalyzer(line);
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

    private static Parameters parametersAnalyzer(String[] args) {
        Parameters.BuildParameters buildParameters = new Parameters.BuildParameters();
        boolean isPath = false;
        boolean isPrefix = false;
        for (String arg : args) {
            if (arg.startsWith("-")) {
                switch (arg) {
                    case "-o" -> isPath = true;
                    case "-p" -> isPrefix = true;
                    case "-a" -> buildParameters.setRewrite(false);
                    case "-s" -> buildParameters.setInfo("short");
                    case "-f" -> buildParameters.setInfo("full");
                }
            } else {
                if (isPath) {
                    buildParameters.setPath(arg);
                    isPath = false;
                } else if (isPrefix) {
                    buildParameters.setPrefix(arg);
                    isPrefix = false;
                } else {
                    buildParameters.addInputFile(arg);
                }
            }
        }
        return buildParameters.build();
    }

    private static String lineAnalyzer(String line) {
        char[] chars = line.toCharArray();
        int eCounter = 0;
        for (char c : chars) {
            // на всякий случай проверка на русскую е
            if (Character.isLetter(c) && c != 'E' && c != 'Е') {
                return "str";
            } else if (c == 'E' || c == 'Е') {
                if (eCounter > 0) {
                    return "str";
                } else {
                    eCounter++;
                }
            } else if (c == '.') {
                return "float";
            }
        }
        return "int";
    }

    private static String getStatistics(String str, String type, String typeStat) {
        int length = str.split("\n").length;
        String result = String.format("Кол-во элементов у типа %s - %d\n", type, length);
        switch (typeStat) {
            case "integer" -> {
                long max = 0;
                // TODO: понять почему тут ошибка, сделать отдельный класс для анализа и статистики
                long min = 9999999999999;
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
                result += String.format("Максимальное число = %d\nМинимальное число = %d\nСумма элементов = %d\nСреднее значение = %f\n", max, min, sum, (float) sum / length);
            }
            case "float" -> {
                float max = 0;
                float min = 999999999;
                float sum = 0;
                for (String s : str.split("\n")) {
                    float num = 0;
                    try {
                        num = Float.parseFloat(s);
                    } catch (NumberFormatException ignored) {
                    }
                    sum += num;
                    max = Math.max(max, num);
                    min = Math.min(min, num);
                }
                result += String.format("Максимальное число = %f\nМинимальное число = %f\nСумма элементов = %f\nСреднее значение = %f\n", max, min, sum, sum / length);
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

    private static void write(String result, String name, Parameters parameters, String type) {
        if (!result.equals("")) {
            try {
                String path = parameters.path() + "\\" + parameters.prefix() + name + ".txt";
                // !parameters.rewrite() - для добавления/перезаписывания файла
                BufferedWriter writer = new BufferedWriter(new FileWriter(path, !parameters.rewrite()));
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
            }
            if (!parameters.info().equals("")) {
                String message = getStatistics(result, type, parameters.info().equals("full") ? type.toLowerCase() : "");
                System.out.println(message);
            }
        }
    }
}