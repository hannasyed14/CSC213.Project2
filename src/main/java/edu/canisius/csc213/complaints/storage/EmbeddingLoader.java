package edu.canisius.csc213.complaints.storage;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.*;

public class EmbeddingLoader {

    /**
     * Loads complaint embeddings from a JSONL (newline-delimited JSON) file.
     * Each line must be a JSON object with:
     * {
     *   "complaintId": <long>,
     *   "embedding": [<double>, <double>, ...]
     * }
     *
     * @param jsonlStream InputStream to the JSONL file
     * @return A map from complaint ID to its embedding vector
     * @throws IOException if the file cannot be read or parsed
     */
    public static Map<Long, double[]> loadEmbeddings(InputStream jsonlStream) throws IOException {
        // TODO: Implement parsing of JSONL to extract complaintId and embedding
        Map<Long, double[]> embeddings = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(jsonlStream));
        ObjectMapper mapper = new ObjectMapper();
        String line;
        while ((line = reader.readLine()) != null) {
            Map<String, Object> map = mapper.readValue(line, Map.class);

            Object idField = map.get("complaintId");
            if (idField == null) {
                idField = map.get("id");
            }

            Long id;
            if (idField instanceof Number) {
                id = ((Number) idField).longValue();
            } else if (idField instanceof String) {
                id = Long.parseLong((String) idField);
            } else {
                throw new IllegalArgumentException("Unexpected id field type: " + idField);
            }

            List<Double> embeddingList = (List<Double>) map.get("embedding");
            double[] embeddingArray = embeddingList.stream().mapToDouble(Double::doubleValue).toArray();
            embeddings.put(id, embeddingArray);
        }
        return embeddings;
    }


}
