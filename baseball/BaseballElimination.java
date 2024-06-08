// import edu.princeton.cs.algs4.FlowEdge;
// import edu.princeton.cs.algs4.FlowNetwork;
// import edu.princeton.cs.algs4.FordFulkerson;
// import edu.princeton.cs.algs4.In;
// import edu.princeton.cs.algs4.Queue;
// import edu.princeton.cs.algs4.ST;
//
// import java.util.NoSuchElementException;
//
// public class BaseballElimination {
//     private ST<String, int[]> allData;
//     private int idPosition;
//     private int N; // the number of teams
//
//     // create a baseball division from given filename in format specified below
//     public BaseballElimination(String filename) {
//         if (filename == null) {
//             throw new IllegalArgumentException("the argument to BaseballElimination() is null\n");
//         }
//
//         allData = new ST<String, int[]>();
//         In input = new In(filename);
//         N = input.readInt();
//         idPosition = N + 3;
//         for (int i = 0; i < N; i++) {
//             String team = input.readString();
//             int[] data = new int[idPosition + 1];
//             for (int j = 0; j < idPosition; j++)
//                 data[j] = input.readInt();
//             data[idPosition] = i;
//             allData.put(team, data);
//         }
//     }
//
//     // number of teams
//     public int numberOfTeams() {
//         return N;
//     }
//
//     // all teams
//     public Iterable<String> teams() {
//         return allData.keys();
//     }
//
//     // number of wins for given team
//     public int wins(String team) {
//         validateTeam(team);
//
//         int[] data = allData.get(team);
//         if (data != null) {
//             return data[0];
//         }
//         else {
//             throw new NoSuchElementException("wins(): no such team\n");
//         }
//     }
//
//     // number of losses for given team
//     public int losses(String team) {
//         validateTeam(team);
//
//         int[] data = allData.get(team);
//         if (data != null) {
//             return data[1];
//         }
//         else {
//             throw new NoSuchElementException("losses(): no such team\n");
//         }
//     }
//
//     // number of remaining games for given team
//     public int remaining(String team) {
//         validateTeam(team);
//
//         int[] data = allData.get(team);
//         if (data != null) {
//             return data[2];
//         }
//         else {
//             throw new NoSuchElementException("remaining(): no such team\n");
//         }
//     }
//
//     // number of remaining games between team1 and team2
//     public int against(String team1, String team2) {
//         validateTeam(team1);
//         validateTeam(team2);
//
//         int[] data1 = allData.get(team1);
//         int[] data2 = allData.get(team2);
//         return data1[data2[idPosition] + 3];
//     }
//
//     // subset R of teams that eliminates given team; null if not eliminated
//     public Iterable<String> certificateOfElimination(String team) {
//         validateTeam(team);
//
//         Queue<String> R = new Queue<String>();
//
//         // is trivial elimination ?
//         int[] data = allData.get(team);
//         int max = data[0] + data[2];
//         for (String t : allData.keys()) {
//             if (allData.get(t)[0] > max) {
//                 R.enqueue(t);
//             }
//         }
//
//         // nontrivial elimination
//         if (R.isEmpty()) {
//             FlowNetwork baseball = buildFlowNetwork(team);
//             int v = baseball.V();
//             FordFulkerson ff = new FordFulkerson(baseball, v - 2, v - 1);
//             for (String t : allData.keys()) {
//                 int teamID = allData.get(t)[idPosition];
//                 if (ff.inCut(teamID)) {
//                     R.enqueue(t);
//                 }
//             }
//         }
//
//         if (R.isEmpty()) return null;
//         else return R;
//     }
//
//     // is given team eliminated?
//     public boolean isEliminated(String team) {
//         validateTeam(team);
//         return certificateOfElimination(team) != null;
//     }
//
//     private FlowNetwork buildFlowNetwork(String teamX) {
//         String[] teams = new String[N];
//         for (String t : allData.keys()) {
//             teams[allData.get(t)[idPosition]] = t;
//         }
//
//         int[] dataX = allData.get(teamX);
//         int idX = dataX[idPosition];
//
//         int count = 0;
//         Queue<int[]> games = new Queue<int[]>();
//         for (int i = 0; i < N; i++) {
//             if (!teams[i].equals(teamX)) {
//                 int[] data = allData.get(teams[i]);
//                 for (int j = i + 3; j < idPosition; j++) {
//                     if (data[j] != 0 && (j - 3) != idX) {
//                         count++;
//                         int[] game = new int[3];
//                         game[0] = data[idPosition];
//                         game[1] = j - 3;
//                         game[2] = data[j];
//                         games.enqueue(game);
//                     }
//                 }
//             }
//         }
//
//         int v = N + count + 2;
//         FlowNetwork baseball = new FlowNetwork(v);
//         for (int i = 0; i < N; i++) {
//             if (!teams[i].equals(teamX)) {
//                 int win = allData.get(teams[i])[0];
//                 int id = allData.get(teams[i])[idPosition];
//                 baseball.addEdge(new FlowEdge(id, v - 1, dataX[0] + dataX[2] - win));
//             }
//         }
//
//         for (int i = N; i < v - 2; i++) {
//             int[] game = games.dequeue();
//             baseball.addEdge(new FlowEdge(v - 2, i, game[2]));
//             baseball.addEdge(new FlowEdge(i, game[0], Double.POSITIVE_INFINITY));
//             baseball.addEdge(new FlowEdge(i, game[1], Double.POSITIVE_INFINITY));
//         }
//
//         return baseball;
//     }
//
//     private void validateTeam(String team) {
//         if (team == null || !allData.contains(team)) {
//             throw new IllegalArgumentException("invalid argument\n");
//         }
//     }
    /*
    // test
    public void testData() {
        StdOut.println(N);
        for (String team : teams()) {
            // 宽度14，左对齐
            StdOut.printf("%-14s", team);
            StdOut.printf("%2d", wins(team));
            StdOut.printf(" %2d", losses(team));
            StdOut.printf(" %2d  ", remaining(team));

            int[] data = allData.get(team);
            for (int i = 3; i < N + 3; i++) {
                StdOut.print(" " + data[i]);
            }
            StdOut.println();
        }
    }

    public void testFlowNetwork(String teamX) {
        FlowNetwork baseball = buildFlowNetwork(teamX);
        StdOut.println(baseball.toString());
    }

    public void testFF(String teamX) {
        FlowNetwork baseball = buildFlowNetwork(teamX);
        int v = baseball.V();
        FordFulkerson ff = new FordFulkerson(baseball, v - 2, v - 1);
        int remaining = 0;
        for (FlowEdge e : baseball.adj(v - 2)) {
            remaining += e.capacity();
        }
        StdOut.println(remaining + " " + ff.value());
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        // division.testData();
        // division.testFlowNetwork(args[1]);
        // division.testFF(args[1]);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
    */
// }

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BaseballElimination {
    private final Map<String, TeamInfo> teams;
    private final int numberOfTeams;

    private class TeamInfo {
        int wins;
        int losses;
        int remaining;
        int[] gamesAgainst;
        int id;

        TeamInfo(int w, int l, int r, int[] ga, int id) {
            this.wins = w;
            this.losses = l;
            this.remaining = r;
            this.gamesAgainst = ga;
            this.id = id;
        }
    }

    public BaseballElimination(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        teams = new HashMap<>();
        In in = new In(filename);
        numberOfTeams = in.readInt();

        for (int i = 0; i < numberOfTeams; i++) {
            String teamName = in.readString();
            int wins = in.readInt();
            int losses = in.readInt();
            int remaining = in.readInt();
            int[] gamesAgainst = new int[numberOfTeams];
            for (int j = 0; j < numberOfTeams; j++) {
                gamesAgainst[j] = in.readInt();
            }
            teams.put(teamName, new TeamInfo(wins, losses, remaining, gamesAgainst, i));
        }
    }

    public int numberOfTeams() {
        return numberOfTeams;
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    public int wins(String team) {
        validateTeam(team);
        return teams.get(team).wins;
    }

    public int losses(String team) {
        validateTeam(team);
        return teams.get(team).losses;
    }

    public int remaining(String team) {
        validateTeam(team);
        return teams.get(team).remaining;
    }

    public int against(String team1, String team2) {
        validateTeam(team1);
        validateTeam(team2);
        return teams.get(team1).gamesAgainst[teams.get(team2).id];
    }

    public Iterable<String> certificateOfElimination(String team) {
        validateTeam(team);
        Set<String> eliminationSet = new HashSet<>();

        int maxWins = teams.get(team).wins + teams.get(team).remaining;

        for (String t : teams.keySet()) {
            if (teams.get(t).wins > maxWins) {
                eliminationSet.add(t);
            }
        }

        if (!eliminationSet.isEmpty()) {
            return eliminationSet;
        }

        FlowNetwork flowNetwork = buildFlowNetwork(team);
        FordFulkerson ff = new FordFulkerson(flowNetwork, flowNetwork.V() - 2, flowNetwork.V() - 1);

        for (String t : teams.keySet()) {
            if (ff.inCut(teams.get(t).id)) {
                eliminationSet.add(t);
            }
        }

        return eliminationSet.isEmpty() ? null : eliminationSet;
    }

    public boolean isEliminated(String team) {
        validateTeam(team);
        return certificateOfElimination(team) != null;
    }

    private FlowNetwork buildFlowNetwork(String team) {
        int teamIndex = teams.get(team).id;
        int maxWins = teams.get(team).wins + teams.get(team).remaining;

        int gameVertices = numberOfTeams * (numberOfTeams - 1) / 2;
        int totalVertices = numberOfTeams + gameVertices + 2;
        FlowNetwork flowNetwork = new FlowNetwork(totalVertices);

        int source = totalVertices - 2;
        int sink = totalVertices - 1;

        int gameVertex = numberOfTeams;

        for (int i = 0; i < numberOfTeams; i++) {
            if (i == teamIndex) continue;

            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == teamIndex) continue;

                int games = teams.get(teams.keySet().toArray()[i]).gamesAgainst[j];
                if (games > 0) {
                    flowNetwork.addEdge(new FlowEdge(source, gameVertex, games));
                    flowNetwork.addEdge(new FlowEdge(gameVertex, i, Double.POSITIVE_INFINITY));
                    flowNetwork.addEdge(new FlowEdge(gameVertex, j, Double.POSITIVE_INFINITY));
                    gameVertex++;
                }
            }

            int capacity = maxWins - teams.get(teams.keySet().toArray()[i]).wins;
            if (capacity < 0) {
                return new FlowNetwork(0); // shortcut: team is trivially eliminated
            }

            flowNetwork.addEdge(new FlowEdge(i, sink, capacity));
        }

        return flowNetwork;
    }

    private void validateTeam(String team) {
        if (!teams.containsKey(team)) {
            throw new IllegalArgumentException("Invalid team: " + team);
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}