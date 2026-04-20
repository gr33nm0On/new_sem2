package homeworks.homework6_IOTests.tests;

import java.io.BufferedReader;
import java.io.FileReader;

public class StudentFileTests {

    @FileTest(filename = "src/homeworks/homework6_IOTests/students.json")
    public void checkIvanovIvanIvanovichInJson(String filename) throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean found = false;

            while ((line = reader.readLine()) != null) {
                if (line.contains("Иванов Иван Иванович")) {
                    found = true;
                    break;
                }
            }

            if (found) {
                System.out.println("Ivanov Ivan Ivanovich found");
            }
            else {
                System.out.println("Ivanov Ivan Ivanovich not found");
            }
        }
        catch (Exception e) {
            throw new RuntimeException("No such file with filename");
        }
    }

    @FileTest(filename = "src/homeworks/homework6_IOTests/students.json")
    public void checkJsonFormatIsValid(String filename) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line.trim());
            }

            String json = content.toString();

            boolean isValid = (json.startsWith("{") && json.endsWith("}")) ||
                            (json.startsWith("[") && json.endsWith("]"));

            if (isValid) {
                System.out.println("JSON format is valid");
            } else {
                System.out.println("JSON format is invalid");
            }

        } catch (Exception e) {
            throw new RuntimeException("No such file with filename");
        }
    }

    @FileTest(filename = "src/homeworks/homework6_IOTests/students.json")
    public void checkXMLMatchesJSON(String filename) throws Exception {
        try {
            String xmlFile = filename.replace(".json", ".xml");
            String jsonFromXML = convertXMLToJSON(xmlFile);

            String originalJSON = readJSONFile(filename);

            String normalizedOriginal = originalJSON.replaceAll("\\s+", "");
            String normalizedXML = jsonFromXML.replaceAll("\\s+", "");

            if (normalizedOriginal.equals(normalizedXML)) {
                System.out.println("XML matches JSON");
            } else {
                System.out.println("XML doesn't matches JSON");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error comparing XML and JSON: " + e.getMessage());
        }
    }

    private String convertXMLToJSON(String xmlFile) throws Exception {
        StringBuilder json = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(xmlFile))) {
            String line;
            boolean isFirstStudent = true;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("<students>")) {
                    json.append("[\n");
                }
                else if (line.equals("</students>")) {
                    json.append("\n]\n");
                }
                else if (line.equals("<student>")) {
                    if (!isFirstStudent) {
                        json.append(",\n");
                    }
                    json.append("\t{\n");
                    isFirstStudent = false;
                }
                else if (line.equals("</student>")) {
                    json.append("\n\t}");
                }
                else if (line.startsWith("<") && line.contains("</")) {
                    String[] tagsOnLine = line.replace("><", ">#<").split("#");

                    for (int i = 0; i < tagsOnLine.length; i++) {
                        String singleTag = tagsOnLine[i];
                        int startTagEnd = singleTag.indexOf(">");
                        int endTagStart = singleTag.indexOf("</");

                        if (startTagEnd != -1 && endTagStart != -1) {
                            String key = singleTag.substring(1, startTagEnd);
                            String value = singleTag.substring(startTagEnd + 1, endTagStart);

                            boolean isNumber = value.matches("\\d+(\\.\\d+)?");
                            String jsonValue = isNumber ? value : "\"" + value + "\"";

                            if (i > 0) {
                                json.append(",\n");
                            }

                            json.append("\t\t\"" + key + "\": " + jsonValue);
                        }
                    }
                }
            }
        }

        return json.toString();
    }

    private String readJSONFile(String filename) throws Exception {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }

        return content.toString();
    }
}