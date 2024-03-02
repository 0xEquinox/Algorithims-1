/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


public class Outcast {
    private final WordNet wordNet;

    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        int n = nouns.length;
        int[] distances = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int dist = wordNet.distance(nouns[i], nouns[j]);
                distances[i] += dist;
                distances[j] += dist;
            }
        }

        int maxDistance = 0;
        String result = "";
        for (int i = 0; i < n; i++) {
            if (distances[i] > maxDistance) {
                maxDistance = distances[i];
                result = nouns[i];
            }
        }

        return result;
    }
}