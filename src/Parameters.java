import java.io.File;
import java.util.ArrayList;

/*
Класс для обработки аргументов, полученных при запуске jar
Идея для данного вида обработки была взята из лабы по java за третий семак
*/
public record Parameters(String path, String prefix, boolean rewrite, boolean info, ArrayList<File> inputFiles) {
    public static class BuildParameters {
        private String path = ""; // По умолчанию кидаем в ту же папку
        private String prefix = ""; // По умолчанию префикса нема
        private boolean rewrite = true; // По умолчанию файлы перезаписываем
        private boolean info = true; // -s (short=false) / -f (fool=true)
        private final ArrayList<File> inputFiles = new ArrayList<>(); // Файлы для чтения

        public void setPath(String path) {
            this.path = path;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public void setRewrite(boolean rewrite) {
            this.rewrite = rewrite;
        }

        public void setInfo(boolean info) {
            this.info = info;
        }

        public void addInputFile(File inputFile) {
            inputFiles.add(inputFile);
        }

        public Parameters build() {
            if (inputFiles.size() > 0) {
                return new Parameters(path, prefix, rewrite, info, inputFiles);
            }
            else throw new NullPointerException("Пустой список файлов для чтения! ᕦ(ò_óˇ)ᕤ");
        }
    }
}
