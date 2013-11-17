package de.unimannheim.dws.wikilist.models;


/**
 * The Class Triple.
 *
 * @param <T1> the generic type
 * @param <T2> the generic type
 * @param <T3> the generic type
 */
public class Triple<T1, T2, T3> {

	/** The first. */
	private T1 first;
	
	/** The second. */
	private T2 second;
	
	/** The third. */
	private T3 third;

	/**
	 * Instantiates a new triple.
	 */
	public Triple() {
		
	}
	
	/**
	 * Instantiates a new triple.
	 *
	 * @param first the first
	 * @param second the second
	 * @param third the third
	 */
	public Triple(T1 first, T2 second, T3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
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

	/**
	 * Gets the third.
	 *
	 * @return the third
	 */
	public T3 getThird() {
		return third;
	}

	/**
	 * Sets the third.
	 *
	 * @param third the new third
	 */
	public void setThird(T3 third) {
		this.third = third;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return first + "::" + second + "::" + third;
	}

}
 