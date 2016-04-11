# A0124636Hunused
###### \src\common\Quadruple.java
``` java

package common;

public class Quadruple <S, T, U, V> {

	private S first;
	private T second;
	private U third;
	private V fourth;
	
	public Quadruple(S first, T second, U third, V fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
	}
	
	public Quadruple() {
		
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
	
	public V getFourth() {
		return fourth;
	}
}
```
###### \src\logic\Main.java
``` java

package logic;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		Logic logic = new Logic();
		ArrayList<String> output = new ArrayList<String>();
		
		while (true) {
			System.out.print("command: ");
			String input = sc.nextLine();
			logic.run(input);
			
			output = logic.getOutput();
			for (int i = 0; i < output.size(); i++) {
				System.out.println(output.get(i));
			}
			
			System.out.println();
		}
	}
	
}
```
