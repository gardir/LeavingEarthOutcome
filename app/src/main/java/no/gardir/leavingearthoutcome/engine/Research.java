package no.gardir.leavingearthoutcome.engine;

import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.util.LinkedList;

public class Research {
	public static boolean OUTER_PLANETS_ENABLED = true;
	public static boolean STATIONS_ENABLED = true;
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
		int NUMB_OUTCOMES = 100;
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
			if ( !player.hasResearch(type) && validResearch(type)
			) {
				list.add(type);
			}
		}
		return list;
	}

	private static boolean validResearch(ResearchType research) {
		if (contains(coreResearches, research)) {
			return true;
		} else if (OUTER_PLANETS_ENABLED && contains(outerPlanetsResearches, research)) {
			return true;
		} else if (STATIONS_ENABLED && contains(stationsResearches, research)) {
			return true;
		}
		return false;
	}

	private static boolean contains(ResearchType[] researches, ResearchType research) {
		for (ResearchType contender : researches) {
			if (contender == research) {
				return true;
			}
		}
		return false;
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
		PROTON, AEROBREAKING,
		SPACE_SHUTTLE, SYNTHESIS, ROVER
	}

	public static final ResearchType[] coreResearches = new ResearchType[]{
			ResearchType.JUNO, ResearchType.ATLAS, ResearchType.SOYUZ, ResearchType.SATURN,
			ResearchType.ION_THRUSTERS, ResearchType.SURVEYING, ResearchType.LANDING,
			ResearchType.REENTRY, ResearchType.RENDEZVOUS, ResearchType.LIFE_SUPPORT,
	};

	public static final ResearchType[] outerPlanetsResearches = new ResearchType[]{
			ResearchType.PROTON, ResearchType.AEROBREAKING
	};

	public static final ResearchType[] stationsResearches = new ResearchType[]{
			ResearchType.SPACE_SHUTTLE, ResearchType.SYNTHESIS, ResearchType.ROVER
	};
	
	public final ResearchType research;
	public final int reveal;
	private ArrayList<Outcome> stackedOutcomes = new ArrayList<>(3);
	
	public Research(ResearchType research) {
		this.research = research;
		int localOutcomes = getLocalOutcomes(research);
		for( int i=0; i<localOutcomes; i++) {
			stackedOutcomes.add( allOutcomes.pop() );
		}
		reveal = research == ResearchType.ROVER ? 2 : 1;
	}

	private int getLocalOutcomes(ResearchType research) {
		switch (research) {
			case SURVEYING:
				return 1;
			case SYNTHESIS:
			case ROVER:
				return 5;
			default:
				return 3;
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
		}
		return super.equals(o);
	}

	public List<Outcome> drawOutcome() {
		if (this.research == ResearchType.ROVER) {
			return drawOutcomes(2);
		}
		return drawOutcomes(1);

	}

	public List<Outcome> drawOutcomes(int nOutcomes) {
		List<Outcome> drawnOutcomes = new LinkedList<>();
		if (stackedOutcomes.size() > 0) {
			Collections.shuffle(stackedOutcomes);
			for (int i=0; i<Math.min(nOutcomes, stackedOutcomes.size()); i++) {
				drawnOutcomes.add(stackedOutcomes.get(i));
			}
		}
		else  drawnOutcomes.add(Outcome.SUCCESS);
		return drawnOutcomes;
	}

	public boolean isOfType(ResearchType researchType) {
		return this.research == researchType;
	}

    public int outcomes() {
        return stackedOutcomes.size();
    }

	public String removeLastOutcome() {
		if ( stackedOutcomes.size() > 0 ) {
			return String.format("%s removed, %d outcomes left.", stackedOutcomes.remove(0), stackedOutcomes.size() );
		} else {
			return "No more outcomes to remove!";
		}
	}

	public String removeOutcome(Outcome result) {
		if (stackedOutcomes.contains(result)) {
			return String.format("%s removed, %d outcomes left.", stackedOutcomes.remove(result), stackedOutcomes.size() );
		} else {
			return String.format("%s has no %s to remove!", this.research, result);
		}
	}
	
	@Override
	public String toString() {
		return String.format("%s -- %d outcomes", research, stackedOutcomes.size());
	}
		
	
}

