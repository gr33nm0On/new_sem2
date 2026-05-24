package independent_works.independent_work_4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Printer {

    public static void save(Check c) {

        File dir = new File("src/independent_works/independent_work_4/json/_out/");
        dir.mkdirs();

        File file = new File(
                dir,
                "check_" + System.currentTimeMillis() + ".json"
        );

        try (FileWriter w = new FileWriter(file)) {
            w.write(toJson(c));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toJson(Check c) {

        StringBuilder sb = new StringBuilder();

        sb.append("{\n");

        sb.append("  \"account\": {\n");
        sb.append("    \"card\": \"").append(c.account.card).append("\",\n");
        sb.append("    \"name\": \"").append(c.account.name).append("\",\n");
        sb.append("    \"bank\": \"").append(c.account.bank.getClass().getSimpleName()).append("\"\n");
        sb.append("  },\n");

        sb.append("  \"products\": [\n");

        int i = 0;

        for (Map.Entry<Product, Integer> e : c.products.entrySet()) {

            Product p = e.getKey();

            sb.append("    {\n");
            sb.append("      \"id\": ").append(p.id).append(",\n");
            sb.append("      \"name\": \"").append(p.name).append("\",\n");
            sb.append("      \"cat\": \"").append(p.cat).append("\",\n");
            sb.append("      \"count\": ").append(e.getValue()).append("\n");
            sb.append("    }");

            if (++i < c.products.size()) sb.append(",");

            sb.append("\n");
        }

        sb.append("  ]\n");
        sb.append("}");

        return sb.toString();
    }
}