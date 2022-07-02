package no.gardir.leavingearthoutcome;

import java.util.List;
import java.util.Scanner;
import java.util.LinkedList;

import no.gardir.leavingearthoutcome.engine.Player;
import no.gardir.leavingearthoutcome.engine.Research;


public class MainTest {
	private static LinkedList<String> commandQueue = new LinkedList<>();
	private static Scanner stdin = new Scanner(System.in);
	private static Player player = new Player();
	
	public static void main(String[] args) {
		commandQueue.add("Research");
		mainLoop();
	}

	private static String getQueue() {
		String out = "";
		for ( String s : commandQueue ) {
			out += s + ".";
		}
		return out;
	}
	
	private static String stdin( String out ) {
		System.out.format("%s%s ~ ", getQueue(), out);
		return stdin.nextLine();
	}

	private static void mainLoop() {
		String usage = "'q' to quit\n'new' for new research\n'activate' to activate a research\n";
		while (true) {
			String command = stdin("main");
			switch (command) {
			case "q":
				return;

			case "new":
				newResearchLoop();
				break;
				
			case "activate":
				activateResearchLoop();
				break;

			case "h":
				System.out.print(usage);
				break;
			default:
				System.out.println("No such command: " + command);
			}
		}
	}

	private static void newResearchLoop() {
		commandQueue.add("new");
		String usage = "'q' to quit\n'#' to get new research represented by number";
		boolean dontStop = true;
		Research.printAvailableResearches(player);
		while( dontStop ) {
			String id = stdin("id");
			switch ( id ) {
			case "q":
				dontStop = false;
				break;
			case "h":
				System.out.println(usage);
			case "l":
				Research.printAvailableResearches(player);
				break;
			default:
				System.out.println(Research.giveResearch(player, id));
			}
		}
		commandQueue.removeLast();
	}

	private static void activateResearchLoop() {
        commandQueue.add("activate");
        String usage = "'q' to quit\n'h' for help\n'l' for id's";
        boolean dontStop = true;
        player.printAvailableResearches();
        while (dontStop) {
            String id = stdin("id");
            switch (id) {
                case "q":
                    dontStop = false;
                    break;
                case "h":
                    System.out.print(usage);
                case "l":
                    player.printAvailableResearches();
                    break;
                default:
                    Research r = player.getResearch(id);
                    if (r != null) {
                        List<Research.Outcome> results = r.drawOutcome();
						for (Research.Outcome result: results){
							System.out.println(result);
							String input = stdin(String.format("remove (%d$)? ",
									result == Research.Outcome.SUCCESS ? 10 : 5));
							if (input.equals("yes") || input.equals("y")) {
								System.out.println(r.removeLastOutcome());
							}
						}
                    } else {
                        System.out.println("No such research id: " + id);
                    }
            }
        }
    }
	
}
