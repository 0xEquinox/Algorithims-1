/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WordNet {
    private final HashMap<String, ArrayList<Integer>> nounIds = new HashMap<>();
    private final HashMap<Integer, String> nouns = new HashMap<>();
    private int size = 0;
    private SAP sap;

    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        readSynsets(synsets);
        readHypernyms(hypernyms);
    }

    private void readSynsets(String synsets) {
        Arrays.stream(new In(synsets).readAllLines())
              .map(line -> line.split(","))
              .filter(words -> words.length >= 2)
              .forEach(words -> {
                  size += 1;
                  int id = Integer.parseInt(words[0]);
                  this.nouns.put(id, words[1]);
                  String[] nouns = words[1].split(" ");
                  Arrays.stream(nouns)
                        .forEach(noun -> nounIds
                                .computeIfAbsent(noun, k -> new ArrayList<>())
                                .add(id));
              });
    }

    private void readHypernyms(String hypernyms) {
        Digraph graph = new Digraph(size);

        Arrays.stream(new In(hypernyms).readAllLines())
              .map(line -> line.split(","))
              .filter(word -> word.length >= 2)
              .forEach(word -> {
                  int start = Integer.parseInt(word[0]);
                  for (int i = 1; i < word.length; i++) {
                      graph.addEdge(start, Integer.parseInt(word[i]));
                  }
              });

        if (hasCycle(graph)) throw new IllegalArgumentException("Cycle detected");
        if (countRoots(graph) > 1) throw new IllegalArgumentException("More than 1 root");

        sap = new SAP(graph);
    }

    private boolean hasCycle(Digraph graph) {
        return new DirectedCycle(graph).hasCycle();
    }

    private int countRoots(Digraph graph) {
        int numRoots = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0) numRoots++;
        }
        return numRoots;
    }

    public Iterable<String> nouns() {
        return nounIds.keySet();
    }

    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("word is null");

        return nounIds.containsKey(word);
    }

    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not WordNet noun.");

        return sap.length(nounIds.get(nounA), nounIds.get(nounB));
    }

    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException("Not WordNet noun.");

        return nouns.get(sap.ancestor(nounIds.get(nounA), nounIds.get(nounB)));
    }
}
