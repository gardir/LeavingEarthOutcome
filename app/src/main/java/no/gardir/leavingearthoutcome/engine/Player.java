package no.gardir.leavingearthoutcome.engine;

import java.util.LinkedList;
import java.lang.StringBuilder;

public class Player {
    private static Player player;
    protected LinkedList<Research> researches = new LinkedList<>();

	public void addResearch(Research research ) {
		researches.add(research);
	}

	/*
	public Research pickResearch(Research research) {
		return researches.get(research);
	}
	*/

	public Research getResearch(String s_id) {
		try {
			int id = Integer.parseInt( s_id );
			for ( Research r : researches ) {
				if ( id-- == 1 ) {
					return r;
				}
			}
		} catch ( NumberFormatException e ) {
			// pass
		}
		return null;
	}

	public boolean hasResearch(Research.ResearchType type) {
		for ( Research r : researches ) {
			if ( r.isOfType( type ) ) {
				return true;
			}
		}
		return false;
	}
	
	public void printAvailableResearches() {
		StringBuilder output = new StringBuilder("--Researches--\n");
		int counter = 1;
		if ( researches.size() > 0 ) {
			for ( Research r : researches ) {
				output.append( counter++ + ": " + r + "\n" );
			}
		} else {
			output.append( "No researches\n" );
		}
		System.out.print(output);
	}

    public static Player getPlayer() {
        if ( player == null ) {
            player = new Player();
        }
        return player;
    }

    public static void reset() {
        player = new Player();
    }

    public int getResearchCount() {
        return researches.size();
    }
}
