package de.unimannheim.dws.wikilist.models;

/**
 * The Class Pair.
 *
 * @param <T1> the generic type
 * @param <T2> the generic type
 */
public class Pair<T1,T2> {
	
	/** The first. */
	private T1 first;
	
	/** The second. */
	private T2 second;

	/**
	 * Instantiates a new pair.
	 *
	 * @param first the first
	 * @param second the second
	 */
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first.
	 *
	 * @return the first
	 */
	public T1 getFirst() {
		return first;
	}

	/**
	 * Sets the first.
	 *
	 * @param first the new first
	 */
	public void setFirst(T1 first) {
		this.first = first;
	}

	/**
	 * Gets the second.
	 *
	 * @return the second
	 */
	public T2 getSecond() {
		return second;
	}

	/**
	 * Sets the second.
	 *
	 * @param second the new second
	 */
	public void setSecond(T2 second) {
		this.second = second;
	}
	
	
}
