
public class DecisionTreePredictor {

    public static final String EXCELLENT = "Excellent";
    public static final String AVERAGE   = "Average";
    public static final String POOR      = "Poor";

    /**
     * Predicts performance level from 5 subject marks (out of 100).
     *
     * @param maths    Mark for Maths
     * @param science  Mark for Science
     * @param english  Mark for English
     * @param social   Mark for Social Science
     * @param language Mark for Language
     * @return Performance level string
     */
    public static String predict(int maths, int science, int english, int social, int language) {
        double avg = (maths + science + english + social + language) / 5.0;
        return predictFromAverage(avg, maths, science, english, social, language);
    }

    /**
     * Extended decision tree using per-subject analysis.
     */
    private static String predictFromAverage(double avg,
            int maths, int science, int english, int social, int language) {

        // Root node: Is average >= 75?
        if (avg >= 75) {
            // Check for any failing subject (< 35) even in high average
            if (hasFailingSubject(maths, science, english, social, language)) {
                return AVERAGE; // High avg but at least one subject failed
            }
            return EXCELLENT;
        }

        // Second level: Is average >= 50?
        if (avg >= 50) {
            // Check if majority of subjects are passing
            int passingCount = countPassing(maths, science, english, social, language);
            if (passingCount >= 3) {
                return AVERAGE;
            } else {
                return POOR; // Too many failures despite average >= 50
            }
        }

        // Default: Poor
        return POOR;
    }

    private static boolean hasFailingSubject(int... marks) {
        for (int m : marks) {
            if (m < 35) return true;
        }
        return false;
    }

    private static int countPassing(int... marks) {
        int count = 0;
        for (int m : marks) {
            if (m >= 35) count++;
        }
        return count;
    }

    /**
     * Returns a detailed explanation of the prediction decision.
     */
    public static String getExplanation(int maths, int science, int english, int social, int language) {
        double avg = (maths + science + english + social + language) / 5.0;
        int passing = countPassing(maths, science, english, social, language);
        boolean anyFail = hasFailingSubject(maths, science, english, social, language);
        String result = predict(maths, science, english, social, language);

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Average Score : %.1f%%\n", avg));
        sb.append(String.format("Passing Subjects: %d / 5\n", passing));
        sb.append(String.format("Any Failing Subject (<35): %s\n\n", anyFail ? "Yes" : "No"));
        sb.append("Decision Tree Path:\n");

        if (avg >= 75) {
            sb.append("  avg >= 75 → YES\n");
            if (anyFail) {
                sb.append("  Has failing subject → YES → Average\n");
            } else {
                sb.append("  Has failing subject → NO → ✅ Excellent\n");
            }
        } else if (avg >= 50) {
            sb.append("  avg >= 75 → NO\n");
            sb.append("  avg >= 50 → YES\n");
            if (passing >= 3) {
                sb.append("  Passing subjects >= 3 → YES → ✅ Average\n");
            } else {
                sb.append("  Passing subjects >= 3 → NO → ⚠ Poor\n");
            }
        } else {
            sb.append("  avg >= 75 → NO\n");
            sb.append("  avg >= 50 → NO → ⚠ Poor\n");
        }

        sb.append("\nFinal Prediction: ").append(result);
        return sb.toString();
    }
}