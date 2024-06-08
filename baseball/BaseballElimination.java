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
