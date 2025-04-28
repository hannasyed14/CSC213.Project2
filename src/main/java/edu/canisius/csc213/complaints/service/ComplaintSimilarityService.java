package edu.canisius.csc213.complaints.service;

import edu.canisius.csc213.complaints.model.Complaint;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class ComplaintSimilarityService {

    private final List<Complaint> complaints;

    public ComplaintSimilarityService(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<Complaint> findTop3Similar(Complaint target) {
        // TODO: Return top 3 most similar complaints (excluding itself)
        List<ComplaintWithScore> scores = new ArrayList<>();
        for (Complaint other : complaints) {
            if (other.getComplaintId() != target.getComplaintId()) {
                double similarity = cosineSimilarity(target.getEmbedding(), other.getEmbedding());
                scores.add(new ComplaintWithScore(other, similarity));
            }
        }
        scores.sort(Comparator.comparingDouble((ComplaintWithScore cws) -> -cws.score));
        List<Complaint> top3 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, scores.size()); i++) {
            top3.add(scores.get(i).complaint);
        }
        return top3;
    }

    private double cosineSimilarity(double[] a, double[] b) {
        // TODO: Implement cosine similarity
        if (a == null || b == null || a.length != b.length) {
            return -1.0;
        }
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < a.length; i++) {
            dotProduct += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        if (normA == 0 || normB == 0) return -1.0;
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static class ComplaintWithScore {
        Complaint complaint;
        double score;

        ComplaintWithScore(Complaint c, double s) {
            this.complaint = c;
            this.score = s;
        }
    }
}
