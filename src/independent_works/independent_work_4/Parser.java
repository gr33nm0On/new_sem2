package independent_works.independent_work_4;

import java.io.*;
import java.util.ArrayList;

public class Parser {

    public static ArrayList<Product> parse(String fileName) {

        ArrayList<Product> products = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("src/independent_works/independent_work_4/json/_in/" + fileName))) {
            String line, name = "", cat = "";
            int id = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("\"id\""))
                    id = Integer.parseInt(
                            line.split(":")[1]
                                    .replace(",", "")
                                    .trim()
                    );

                else if (line.startsWith("\"name\""))
                    name = line.split(":")[1]
                            .replace("\"", "")
                            .replace(",", "")
                            .trim();

                else if (line.startsWith("\"cat\""))
                    cat = line.split(":")[1]
                            .replace("\"", "")
                            .replace(",", "")
                            .trim();

                else if (line.startsWith("}"))
                    products.add(new Product(id, name, cat));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return products;
    }
}