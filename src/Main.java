import java.io.*;

public class Main {
    public static void main(String[] args) {
        Parameters parameters = analyzeParameters(args);
        BufferedReader reader;
        for (File file : parameters.inputFiles()) {
            try {
                reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (FileNotFoundException e) {
                System.out.printf("Файла %s не существует", file);
            }
            catch (IOException e) {
                System.out.println("Ошибка ввода/вывода");
            }
        }
    }

    private static Parameters analyzeParameters(String[] args) {
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
}