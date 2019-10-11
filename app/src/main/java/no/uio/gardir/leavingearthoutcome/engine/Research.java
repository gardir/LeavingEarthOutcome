package no.uio.gardir.leavingearthoutcome.engine;

import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;

public class Research {
	private static long SEED = getRandomSeed();
	private static long getRandomSeed() {
		Random r = new Random();
		return r.nextLong();
	}
    private static double PERCENT_MINOR_FAILURE = .2;
    private static double PERCENT_MAJOR_FAILURE = .1;
    private static double PERCENT_SUCCESS = 1 - PERCENT_MINOR_FAILURE - PERCENT_MAJOR_FAILURE;


	protected static LinkedList<Outcome> allOutcomes = createOutcomes();
	private static LinkedList<Outcome> createOutcomes() {
		Random rand = new Random( SEED );
		int NUMB_OUTCOMES = 50;
		LinkedList<Outcome> outcomes = new LinkedList<>();
		
		int i=0;
		for ( int p=0; p<Double.valueOf( NUMB_OUTCOMES * PERCENT_SUCCESS ).intValue(); p++ ) {
			outcomes.add( Outcome.SUCCESS );
		}
		for ( int p=0; p<Double.valueOf( NUMB_OUTCOMES * PERCENT_MINOR_FAILURE ).intValue(); p++ ) {
			outcomes.add( Outcome.MINOR_FAILURE );
		}
		for ( int p=0; p<Double.valueOf( NUMB_OUTCOMES * PERCENT_MAJOR_FAILURE ).intValue(); p++ ) {
			outcomes.add( Outcome.MAJOR_FAILURE );
		}
		Collections.shuffle( outcomes, rand );
        /* // DEBUG PRINTING
        for (i=0; i<outcomes.size(); i++ ) {
            System.err.format("outcome[%d] = %s\n", i, outcomes.get(i).toString());
        }
        */
		return outcomes;
	}

	public static String giveResearch( Player player, String s_id ) {
		try {
			int id = Integer.parseInt( s_id );
			for ( ResearchType rt : getAvailableResearches(player)) {
				if ( id-- == 1 ) {
					player.addResearch( new Research(rt) );
					return String.format("%s added to player.", rt);
				}
			}
		} catch (NumberFormatException e) {
			// do nothing
		}
		return "No such research " + s_id;
	}
	
	public static void printAvailableResearches(Player player) {
		int counter = 1;
		LinkedList<ResearchType> list = getAvailableResearches(player);
		if ( list.size() > 0 ) {
			for ( ResearchType rt : list ) {
				System.out.println(counter++ + ": " + rt);
			}
		} else {
			System.out.println("No more researches available");
		}
	}

	public static LinkedList<ResearchType> getAvailableResearches(Player player) {
		LinkedList<ResearchType> list = new LinkedList<>();
		for ( ResearchType type : ResearchType.values() ) {
			if ( !player.hasResearch(type) ) {
				list.add(type);
			}
		}
		return list;
	}

	public static void removeOutcomes(int numb) {
		for (int i=0; i<numb; i++) {
			System.out.println( "Removed " + allOutcomes.remove(0) );
		}
	}

    public static String getSEED() {
        return SEED + "";
    }

    public static void setSEED(String seed) {
        try {
            SEED = Long.parseLong( seed );
        } catch ( NumberFormatException e ) {
            System.err.println("NumberFormatException: What an ugly seed - " + seed);
        }
    }

    public static void reset() {
        allOutcomes = createOutcomes();
    }
	
	public enum ResearchType {
		JUNO, ATLAS, SOYUZ, SATURN, ION_THRUSTERS,
		SURVEYING, LANDING, REENTRY, RENDEZVOUS, LIFE_SUPPORT,
		PROTON, AEROBREAKING
	}
	
	public final ResearchType research;
	private ArrayList<Outcome> outcomes = new ArrayList<>(3);
	
	public Research(ResearchType research) {
		this.research = research;
		int localOutcomes = (research == ResearchType.SURVEYING) ? 1 : 3;
		for( int i=0; i<localOutcomes; i++) {
			outcomes.add( allOutcomes.pop() );
		}
	}

	public enum Outcome{
		SUCCESS,
		MINOR_FAILURE,
		MAJOR_FAILURE,
        UNKNOWN
	}

	@Override
	public boolean equals(Object o) {
		if ( o instanceof Research ) {
			Research r = ( Research ) o;
			return this.research == r.research;
		} else if ( o instanceof ResearchType ) {
			ResearchType rt = ( ResearchType ) o;
			return this.research == rt;
		}
		return super.equals(o);
	}

	public Outcome drawOutcome() {
		if ( outcomes.size() > 0 ) {
			Collections.shuffle(outcomes);
			return outcomes.get(0);
		}
		return Outcome.SUCCESS;
	}

    public int outcomes() {
        return outcomes.size();
    }

	public String removeLastOutcome() {
		if ( outcomes.size() > 0 ) {
			return String.format("%s removed, %d outcomes left.", outcomes.remove(0), outcomes.size() );
		} else {
			return "No more outcomes to remove!";
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s -- %d outcomes", research, outcomes.size());
	}
		
	
}

