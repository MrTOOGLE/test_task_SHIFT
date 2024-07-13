public class Analyzer {

    // Метод для анализа полученных аргументов и составления класса Parameters
    protected static Parameters parametersAnalyzer(String[] args) {
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

    // Метод для определения типа данных строки (что в ней находится -> string/float/integer)
    protected static String lineAnalyzer(String line) {
        char[] chars = line.toCharArray();
        int eCounter = 0;
        int signCounter = 0;
        boolean hasPoint = false;
        boolean hasLetters = false;
        for (char c : chars) {
            if (c == 'E' || c == 'e' || c == 'Е' || c == 'е') {
                eCounter++;
            }
            if (c == '.') {
                hasPoint = true;
            }
            if (c == '-') {
                signCounter++;
            }
            if (Character.isLetter(c) && c != 'E' && c != 'e' && c != 'Е' && c != 'е') {
                hasLetters = true;
            }
        }

        if (hasLetters || eCounter > 1 || (eCounter == 1 && signCounter == 0)) {
            return "str";
        } else if (hasPoint) {
            return "float";
        } else return "int";
    }
}