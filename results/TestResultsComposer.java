import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class TestResultsComposer {

    private static class Test {
        String language;
        String implementation = "";
        String compiler = "";
        String options = "";

        public Test (String language) {
            this.language = language;
        }
    }

    private static class TestResult {
        int sequentialWrite = 0;
        int sequentialRead = 0;
        int randomWrite = 0;
        int randomRead = 0;
        long result = 0;
    }

    private static final Map<Test, List<TestResult>> testResults = new HashMap<>();
    private static final Map<String, List<Test>> tests = new TreeMap<>();

    public static void main(String[] args) throws IOException {
        String dirPath;
        if (args.length == 0) {
            dirPath = System.getProperty("user.dir");
        } else {
            dirPath = args[0];
        }
        File directory = new File(dirPath);
        System.out.println("## " + directory.getName());
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                readResults(file);
            }
        }
        for (Test test : testResults.keySet()) {
            if (tests.containsKey(test.language)) {
                tests.get(test.language).add(test);
            } else {
                List<Test> t = new ArrayList<>();
                t.add(test);
                tests.put(test.language, t);
            }
        }

        System.out.println(generateMarkdown());
    }

    private static String generateMarkdown() {
        StringBuilder out = new StringBuilder();
        //out.append("## Test results\n");
        out.append("| Language | Setup | Sequential write | Sequential read | Random write | Random read |\n");
        out.append("| :--- | :---: | :---: | :---: | :---: | :---: |\n");
        for (String lang : tests.keySet()) {
            for (Test test : tests.get(lang)) {
                String setup = (test.implementation.trim() + (test.implementation.trim().isBlank() ? "" : ", ")
                        + test.compiler.trim() + (test.compiler.isBlank() ? "" : ", ") + test.options.trim()).trim();
                if (setup.endsWith(",")) {
                    setup = setup.substring(0, setup.length() - 1);
                }
                TestResult average = new TestResult();
                int cnt = 0;
                for (TestResult tr : testResults.get(test)) {
                    cnt++;
                    average.sequentialWrite += tr.sequentialWrite;
                    average.sequentialRead += tr.sequentialRead;
                    average.randomWrite += tr.randomWrite;
                    average.randomRead += tr.randomRead;
                    average.result += tr.result;
                }
                average.sequentialWrite /= cnt;
                average.sequentialRead /= cnt;
                average.randomWrite /= cnt;
                average.randomRead /= cnt;
                average.result /= cnt;

                if (average.result != 273800243080L) {
                    //skip setup
                    System.out.println("Wrong test results for " + lang + " " + setup);
                    break;
                }
                out.append("| ").append(lang).append(" | <small>").append(setup).append("</small> |");
                out.append(average.sequentialWrite).append(" | ");
                out.append(average.sequentialRead).append(" | ");
                out.append(average.randomWrite).append(" | ");
                out.append(average.randomRead).append(" | \n");
            }
        }
        return out.toString();
    }

    private static void readResults(File file) throws IOException {
        Test test = null;
        List<TestResult> results = null;
        TestResult result = new TestResult();

        List<String> lines = Files.readAllLines(file.toPath());
        for (String line : lines) {
            if (line.contains(": ")) {
                String param = line.split(":")[0].trim().toLowerCase();
                String value = line.substring(param.length() + 2).trim();

                switch (param) {
                    case "language" :
                        if (test != null) {
                            testResults.put(test, results);
                        }
                        test = new Test(value);
                        results = new ArrayList<>();
                        break;
                    case "implementation" :
                        assert test != null;
                        test.implementation = value;
                        break;
                    case "compiler" :
                        assert test != null;
                        test.compiler = value;
                        break;
                    case "options" :
                        assert test != null;
                        test.options = value;
                        break;
                    case "sequential write" :
                        result.sequentialWrite = Integer.parseInt(value);
                        break;
                    case "random read" :
                        result.randomRead = Integer.parseInt(value);
                        break;
                    case "random write" :
                        result.randomWrite = Integer.parseInt(value);
                        break;
                    case "sequential read" :
                        result.sequentialRead = Integer.parseInt(value);
                        break;
                    case "result" :
                        result.result = Long.parseLong(value);
                        assert results != null;
                        results.add(result);
                        result = new TestResult();
                        break;
                    default:
                }
            }
        }
        if (test != null) {
            testResults.put(test, results);
        }
    }
}