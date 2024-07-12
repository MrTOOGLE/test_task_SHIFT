import java.io.*;

public class Main {
    public static void main(String[] args) {
        Parameters parameters = parametersAnalyzer(args);
        BufferedReader reader;
        String resInt = "";
        String resFloat = "";
        String resString = "";
        for (File file : parameters.inputFiles()) {
            try {
                reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String analysis = lineAnalyzer(line);
                    switch (analysis) {
                        case "str" -> resString += line + "\n";
                        case "float" -> resFloat += line + "\n";
                        case "int" -> resInt += line + "\n";
                    }
                }
                //TODO: добавить статистику + "-a"
            } catch (FileNotFoundException e) {
                System.out.printf("Файла %s не существует", file);
            }
            catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
            }
        }
        write(resString, "strings", parameters);
        write(resFloat, "floats", parameters);
        write(resInt, "integers", parameters);
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
                    case "-s" -> buildParameters.setInfo(false);
                }
            }
            else {
                if (isPath) {
                    buildParameters.setPath(arg);
                    isPath = false;
                }
                else if (isPrefix) {
                    buildParameters.setPrefix(arg);
                    isPrefix = false;
                }
                else {
                    buildParameters.addInputFile(new File(arg));
                }
            }
        }
        return buildParameters.build();
    }

    private static String lineAnalyzer(String line) {
        char[] chars = line.toCharArray();
        boolean isStr = false, isFloat = false;
        int eCounter = 0;
        for (char c : chars) {
            // на всякий случай проверка на русскую е
            if (Character.isLetter(c) && c != 'E' && c != 'Е') {
                isStr = true;
                return "str";
            }
            else if (c == 'E' || c == 'Е') {
                if (eCounter > 0) {
                    return "str";
                }
                else {
                    eCounter++;
                }
            }
            else if (c == '.') {
                return "float";
            }
        }
        return "int";
    }

    private static void write(String result, String name, Parameters parameters) {
        try {
            if (!result.equals("")) {
                String path = parameters.path() + "\\" + parameters.prefix() + name + ".txt";
                System.out.println(path);
                BufferedWriter writer = new BufferedWriter(new FileWriter(path));
                for (String s : result.split("\n")) {
                    writer.write(s);
                    System.out.println(s);
                }
            }
            System.out.println("------------------------------------------------");
        }
        catch (IOException e) {
            System.out.println("Ошибка ввода/вывода");
        }
    }
}