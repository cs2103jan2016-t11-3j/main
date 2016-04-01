package common;

public class Triple <S, T, U> {

	private S first;
	private T second;
	private U third;
	
	public Triple(S first, T second, U third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}
	
	public Triple() {
		
	}
	
	public S getFirst() {
		return first;
	}
	
	public T getSecond() {
		return second;
	}
	
	public U getThird() {
		return third;
	}
}
