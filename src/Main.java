import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Main {
	// array for all competing squads:
	public static Squad[] squads = new Squad[32];

	public static void main(String[] args) {
		// read in files:
		File managerFile = new File("./Managers.csv");
		File playerFile = new File("./Players.csv");
		// create scanner to read lines of each file:
		Scanner managerIn = null;
		Scanner playerIn = null;
		try {
			managerIn = new Scanner(managerFile);
			managerIn.nextLine(); // clear headings line of file
			for (int i = 0; i < squads.length; i++) {
				// split data up at commas
				String[] tempManagerData = managerIn.nextLine().split("\\,");
				// create manager object:
				Manager tempManager = new Manager(tempManagerData[0], tempManagerData[1], tempManagerData[2],
						tempManagerData[3], Double.parseDouble(tempManagerData[4]),
						Double.parseDouble(tempManagerData[5]), Double.parseDouble(tempManagerData[6]),
						Double.parseDouble(tempManagerData[7]));
				String tempTeam = tempManagerData[2]; // get temp value for team name
				squads[i] = new Squad(tempTeam, tempManager); // create squad
				playerIn = new Scanner(playerFile);
				playerIn.nextLine(); // clear headings line of file
				while (playerIn.hasNextLine()) { // loop over all players
					// split data up at commas
					String[] tempPlayerData = playerIn.nextLine().split("\\,");
					// add player to the manager's squad if they refer to the same team
					if (tempPlayerData[2].equals(tempTeam)) {
						Player tempPlayer = new Player(tempPlayerData[0], tempPlayerData[1], tempPlayerData[2],
								tempPlayerData[3], Double.parseDouble(tempPlayerData[4]),
								Double.parseDouble(tempPlayerData[5]), Double.parseDouble(tempPlayerData[6]),
								Double.parseDouble(tempPlayerData[7]), Double.parseDouble(tempPlayerData[8]),
								Double.parseDouble(tempPlayerData[9]), Double.parseDouble(tempPlayerData[10]),
								Double.parseDouble(tempPlayerData[11]), Double.parseDouble(tempPlayerData[12]),
								Double.parseDouble(tempPlayerData[13]));
						squads[i].addPlayer(tempPlayer); // add player
					}
				}
			}
		} catch (FileNotFoundException e) { // error handling if the file can't be found
			e.printStackTrace();
		}
		Team[] teams = new Team[32]; // create teams array for all teams
		for (int i = 0; i < squads.length; i++) {
			teams[i] = getTeam(squads[i]); // reduce squad to teams
		}
		// start Java World Cup tournament
		runTournament(teams);
	}

	public static Team getTeam(Squad s) { // reduce squad to team
		Team t = new Team(s.getTeamName(), s.getManager()); // create team object
		// create lists to catagorise all players
		ArrayList<Player> goalkeepers = new ArrayList<Player>();
		ArrayList<Player> forwards = new ArrayList<Player>();
		ArrayList<Player> defenders = new ArrayList<Player>();
		ArrayList<Player> midfielders = new ArrayList<Player>();
		// add players to corresponding lists
		for (int i = 0; i < 26; i++) { // loop over all players
			switch (s.getPlayer(i).getPosition()) {
			case " Goal Keeper":
				goalkeepers.add(s.getPlayer(i));
				break;
			case " Forward":
				forwards.add(s.getPlayer(i));
				break;
			case " Defender":
				defenders.add(s.getPlayer(i));
				break;
			case " Midfielder":
				midfielders.add(s.getPlayer(i));
				break;
			}
		}
		// get formation as [defenders, midfielders, forwards]
		String[] favouredFormation = t.getManager().getFavouredFormation().split("-");
		// get best goalkeeper
		ArrayList<Player> goalkeeperShortlist = getPlayers(goalkeepers, 1);
		for (int i = 0; i < goalkeeperShortlist.size(); i++) {
			t.addPlayer(goalkeeperShortlist.get(i));
		}
		// get best defenders
		ArrayList<Player> defenderShortlist = getPlayers(defenders, Integer.parseInt(favouredFormation[0]));
		for (int i = 0; i < defenderShortlist.size(); i++) {
			t.addPlayer(defenderShortlist.get(i));
		}
		// get best midfielders
		ArrayList<Player> midfielderShortlist = getPlayers(midfielders, Integer.parseInt(favouredFormation[1]));
		for (int i = 0; i < midfielderShortlist.size(); i++) {
			t.addPlayer(midfielderShortlist.get(i));
		}
		// get best forwards
		ArrayList<Player> forwardShortlist = getPlayers(forwards, Integer.parseInt(favouredFormation[2]));
		for (int i = 0; i < forwardShortlist.size(); i++) {
			t.addPlayer(forwardShortlist.get(i));
		}
		return t;
	}

	// method to get best players:
	public static ArrayList<Player> getPlayers(ArrayList<Player> players, int n) {
		ArrayList<Player> shortlist = new ArrayList<Player>();
		// iterate over players - get average of all statistics
		Comparator<Player> averages = Comparator.comparingDouble(Player::getAverage);
		// sort the players based on the gathered averages - best players at the start
		Collections.sort(players, averages);
		// loop over sorted ArrayList - add the best players
		for (int i = 0; i < n; i++) {
			shortlist.add(players.get(players.size() - 1 - i));
		}
		return shortlist;
	}

	// run the Java World Cup tournament
	public static void runTournament(Team[] teams) {
		System.out.println("\n=================\nTOURNAMENT START!\n=================\n");
		Collections.shuffle(Arrays.asList(teams)); // randomise teams
		// put teams into random groups of 4.
		Team[][] groups = { { teams[0], teams[1], teams[2], teams[3] }, { teams[4], teams[5], teams[6], teams[7] },
				{ teams[8], teams[9], teams[10], teams[11] }, { teams[12], teams[13], teams[14], teams[15] },
				{ teams[16], teams[17], teams[18], teams[19] }, { teams[20], teams[21], teams[22], teams[23] },
				{ teams[24], teams[25], teams[26], teams[27] }, { teams[28], teams[29], teams[30], teams[31] } };
		Collections.shuffle(Arrays.asList(groups)); // randomise group order

		// group stage of the tournament:
		HashMap<Team, Integer> leaderboard = new HashMap<>(); // group stage standings
		// list groups:
		System.out.println("\nGROUP STAGE:");
		// add each group to the standings:
		for (int i = 0; i < groups.length; i++) {
			System.out.println("\nGROUP " + (i + 1) + ":");
			Collections.shuffle(Arrays.asList(groups[i]));
			for (int j = 0; j < groups[i].length; j++) {
				System.out.println(groups[i][j].getTeamName());
				leaderboard.put(groups[i][j], 0);
			}
		}
		// run each group game - 48 games total
		int gameCount = 0; // used to display which group game we are on to the viewer
		for (int k = 0; k < groups.length; k++) {
			for (int i = 0; i < groups[k].length; i++) {
				for (int j = i + 1; j < groups[k].length; j++) {
					// run the group game; and alter the standings according to the result:
					gameCount++;
					System.out.println("\nGROUP GAME " + gameCount);
					String outcome = runGame(groups[k][i], groups[k][j]);
					switch (outcome) {
					case "team1win":
						leaderboard.put(groups[k][i], leaderboard.get(groups[k][i]) + 3);
						break;
					case "team2win":
						leaderboard.put(groups[k][j], leaderboard.get(groups[k][j]) + 3);
						break;
					case "draw":
						leaderboard.put(groups[k][i], leaderboard.get(groups[k][i]) + 1);
						leaderboard.put(groups[k][j], leaderboard.get(groups[k][j]) + 1);
						break;
					}
				}
			}
		}
		// array to store winners of the group stage:
		Team[] qualifiers = new Team[16];
		int qualifierIndex = 0;
		System.out.println("\nGROUP STAGE CONCLUDED!");
		System.out.println("Group leaderboard:");
		// develop group stage leaderboard
		for (int i = 0; i < groups.length; i++) {
			System.out.println("GROUP " + (i + 1));
			HashMap<String, Integer> scoreShortlist = new HashMap<String, Integer>();
			for (int j = 0; j < groups[i].length; j++) {
				System.out.println(groups[i][j].getTeamName() + ": " + leaderboard.get(groups[i][j]));
				scoreShortlist.put(groups[i][j].getTeamName(), leaderboard.get(groups[i][j]));
			}
			for (int j = qualifierIndex; j < qualifiers.length; j++) {
				if (qualifiers[j] == null) {
					qualifierIndex = j;
					break;
				}
			}
			// get the top team from the group and assign to qualifiers[qualifierIndex]:
			ArrayList<HashMap.Entry<String, Integer>> shortListConverted = new ArrayList<>(scoreShortlist.entrySet());
			Collections.sort(shortListConverted,
					Comparator.comparing(HashMap.Entry::getValue, Comparator.reverseOrder()));
			for (int k = 0; k < 2; k++) {
				for (int l = 0; l < groups[i].length; l++) {
					if (groups[i][l].getTeamName().equals(shortListConverted.get(k).getKey())) {
						qualifiers[qualifierIndex] = groups[i][l];
						qualifierIndex++;
					}
				}
			}
		}

		// knockout stage:
		Random random = new Random();
		System.out.println("\n\nKnockout stage qualifiers:");
		for (Team team : qualifiers) {
			System.out.print(" -" + team.getTeamName() + " - \n");
		}
		// recursively reduce winners list until only one remains - that's the winning
		// team:
		String winner = runKnockoutStage(qualifiers, 0);
		System.out.println("\nThe world cup winner is " + winner + "!");
		System.out.println("\n============\n" + winner.toUpperCase() + " WINS THE JAVA WORLD CUP!\n============");
	}

	// recursive function to reduce winners list to one team that ends up winning
	// the cup:
	public static String runKnockoutStage(Team[] winnerPool, int roundCount) {
		Random random = new Random();
		// list of round headings for display purposes:
		String[] knockoutStages = { "\nKNOCKOUT ROUND 1!\n", "\nQUARTER FINALS!\n", "\nSEMI FINALS!\n",
				"\nWORLD CUP FINAL!\n" };
		String roundMessage = knockoutStages[roundCount]; // display the round
		System.out.println(roundMessage);
		Collections.shuffle(Arrays.asList(winnerPool)); // randomise pool of teams
		Team[] roundWinners = getKnockoutResult(winnerPool, random);
		String resultMessage = roundMessage.toLowerCase();
		System.out.println(resultMessage + "winners:");
		for (Team team : roundWinners) {
			System.out.print(" -" + team.getTeamName() + " - \n");
		}
		// if there are more than 1 teams remaining in the tournament:
		if (roundWinners.length > 1) {
			// run another stage with the winners of the current stage:
			return runKnockoutStage(roundWinners, roundCount + 1);
		} else {
			// return overall cup winner
			return roundWinners[0].getTeamName();
		}
	}

	public static Team[] getKnockoutResult(Team[] teamPool, Random random) {
		Team[] winners = new Team[teamPool.length / 2];
		// quarter finals:
		for (int i = 0; i < teamPool.length; i += 2) { // loop over every two teams:
			System.out.println("\nGAME " + (i / 2 + 1));
			String winner = runGame(teamPool[i], teamPool[i + 1]);
			switch (winner) {
			case "team1win":
				winners[i / 2] = teamPool[i];
				break;
			case "team2win":
				winners[i / 2] = teamPool[i + 1];
				break;
			case "draw":
				// penalty shootout:
				System.out.println("\nMatch ended in a draw; penalty shootout to decide!");
				boolean penaltyWinner = random.nextBoolean();
				// get winner based on random 50/50 chance:
				if (penaltyWinner) {
					System.out.println(teamPool[i].getTeamName() + " won the shootout!");
					System.out.println(teamPool[i].getTeamName() + " WINS!");
					winners[i / 2] = teamPool[i];
				} else {
					System.out.println(teamPool[i + 1].getTeamName() + " won the shootout!");
					System.out.println(teamPool[i + 1].getTeamName() + " WINS!");
					winners[i / 2] = teamPool[i + 1];
				}
				break;
			}
		}
		return winners;
	}

	// run a world cup game to determine winner - simulate game:
	public static String runGame(Team team1, Team team2) {
		// display teams that are competing:
		System.out.println(team1.getTeamName() + " VS " + team2.getTeamName());
		int[] score = { 0, 0 }; // init score
		int possession = -1;
		Random random = new Random();
		possession = random.nextInt((2 - 1) + 1) + 1; // randomly generate team that starts
		switch (possession) {
		case 1:
			System.out.println(team1.getTeamName() + " Has first touch of the ball!");
			break;
		case 2:
			System.out.println(team2.getTeamName() + " Has first touch of the ball!");
			break;
		}
		// simulate game; one iteration for each minute
		for (int i = 0; i < 90; i++) {
			int occurence = 1;
			// get occurence that happens - chance is as follows:
			// shot at goal < tackle < pass
			occurence = random.nextInt((10 - 1) + 1) + 1;
			if (occurence < 9) {
				if (occurence <= 4) {
					occurence = 3;
				} else {
					occurence = 2;
				}
			} else {
				occurence = 1;
			}
			switch (occurence) {
			// shot on goal:
			case 1:
				ArrayList<Player> forwards = null;
				String strikingTeam = "";
				// get which team currently has possession and is therefore taking the shot
				if (possession == 1) {
					// get forward - calculate goal chance based on skill
					forwards = team1.getPlayerByPosition(" Forward");
					strikingTeam = team1.getTeamName();
				} else {
					// get forward - calculate goal chance based on skill
					forwards = team2.getPlayerByPosition(" Forward");
					strikingTeam = team2.getTeamName();
				}
				if (forwards.size() > 0) {
					// get a random forward to determine who the striker is:
					int strikerIndex = random.nextInt((forwards.size()));
					Player currentForward = forwards.get(strikerIndex);
					// calculate the selected forward's chance of scoring: accuracy * frequency
					double shotAtGoal = random.nextDouble(((10 * currentForward.getShotFrequency()) - 1) + 1) + 1;
					shotAtGoal *= currentForward.getShotAccuracy();
					if (shotAtGoal >= 4) { // the chance has to be greater than 4 to go in the goal
						score[possession - 1] = score[possession - 1] + 1;
						System.out.println(strikingTeam + " scored! Striker: " + currentForward.getSurname() + "!");
						System.out.println("Score: " + team1.getTeamName() + ": " + score[0] + " - "
								+ team2.getTeamName() + ": " + score[1]);
					} else {
						System.out.println(strikingTeam + "'s striker " + currentForward.getSurname()
								+ " attempted to score but missed!");
					}
				} else {
					System.out.println("Cant find forwards");
				}
				break;
			// player challenged
			case 2:
				// get current possession:
				switch (possession) {
				case 1:
					possession = challengePlayer(team1, team2, random, possession);
					break;
				case 2:
					possession = challengePlayer(team2, team1, random, possession);
					break;
				}
				break;
			// pass made
			case 3:
				switch (possession) {
				case 1:
					possession = makePass(team1, team2, random, possession);
					break;
				case 2:
					possession = makePass(team2, team1, random, possession);
					break;
				}
				break;
			}
		}
		System.out.println("\nFINAL SCORE:\n" + team1.getTeamName() + ": " + score[0] + "\n" + team2.getTeamName()
				+ ": " + score[1] + "\n");
		// determine winner of the game:
		if (score[0] > score[1]) {
			System.out.println(team1.getTeamName() + " WINS!");
			return "team1win";
		} else if (score[1] > score[0]) {
			System.out.println(team2.getTeamName() + " WINS!");
			return "team2win";
		} else {
			System.out.println("IT'S A DRAW!");
			return "draw";
		}
	}

	public static int challengePlayer(Team controllers, Team challengers, Random random, int possession) {
		// get player with current position
		Player currentPlayer = controllers.getPlayer(random.nextInt(random.nextInt((9 - 1) + 1) + 1));
		// get random player on the other team to challenge the current player:
		Player challenger = challengers.getPlayer(random.nextInt(random.nextInt((9 - 1) + 1) + 1));
		// tackle succeeds if the aggression is greater than the current player's
		// defensiveness:
		if (challenger.getAggression() > currentPlayer.getDefensiveness()) {
			System.out
					.println(challenger.getSurname() + " has successfully tackled " + currentPlayer.getSurname() + "!");
			// swap possession to the other team:
			possession--;
			if (possession < 1) {
				possession = 2;
			}
		} else {
			System.out.println(challenger.getSurname() + " failed to tackle " + currentPlayer.getSurname() + "!");
		}
		// if the aggression is higher; there is a greater chance to foul the player and
		// give a free kick:
		double foulChance = random.nextDouble(2) * challenger.getAggression();
		if (foulChance > 1.3) { // give a free kick if the player is fouled:
			System.out.println(challenger.getSurname() + " fouled " + currentPlayer.getSurname() + "!");
			System.out.println("Free kick given to " + controllers.getTeamName() + "!");
			// swap possession to the other team:
			possession--;
			if (possession < 1) {
				possession = 2;
			}
		}
		return possession; // return who has the ball after the tackle attempt
	}

	public static int makePass(Team controllers, Team interceptors, Random random, int possession) {
		// get player with current position
		Player currentPlayer = controllers.getPlayer(random.nextInt(random.nextInt((9 - 1) + 1) + 1));
		// get random player on the other team to attempt to intercept:
		Player interceptor = interceptors.getPlayer(random.nextInt(random.nextInt((9 - 1) + 1) + 1));
		// get random player on the current team to receive the pass attempt:
		Player receiver = controllers.getPlayer(random.nextInt(random.nextInt((9 - 1) + 1) + 1));
		// if the chance creation of the interceptor is greater than the accuracy of the
		// passer then the pass gets intercepted:
		if (interceptor.getChanceCreation() > currentPlayer.getPassingAccuracy()) {
			System.out.println(interceptor.getSurname() + " has successfully intercepted a pass made by "
					+ currentPlayer.getSurname() + "!");
			// swap possession to the other team:
			possession--;
			if (possession < 1) {
				possession = 2;
			}
		} else {
			// calculate whether the receiver successfully handles the pass
			if (currentPlayer.getPassingAccuracy() > random.nextDouble()) {
				if (receiver.getPositioning() > random.nextDouble()) {
					System.out.println(
							receiver.getSurname() + " caught a great pass from " + currentPlayer.getSurname() + "!");
				} else {
					System.out.println(currentPlayer.getSurname() + " made a great pass, but " + receiver.getSurname()
							+ " fumbled the ball!");
					// swap possession to the other team:
					possession--;
					if (possession < 1) {
						possession = 2;
					}
				}
			} else {
				System.out.println(currentPlayer.getSurname() + " missed a pass! The ball has gone to the other team! ("
						+ interceptors.getTeamName() + ")");
				// swap possession to the other team:
				possession--;
				if (possession < 1) {
					possession = 2;
				}
			}
		}
		return possession; // return who has the ball after the pass attempt
	}
}