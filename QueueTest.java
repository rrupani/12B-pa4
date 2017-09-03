// Rhea Rupani
// rrupani
// 12B PA4
// QueueTest.java

public class QueueTest {
	public static void main (String[] args) {
		Queue A = new Queue();

		System.out.println(A.isEmpty());
		A.enqueue((int)2);
		A.enqueue((int)14);
		A.enqueue((int)6);
		A.enqueue((int)17);

		System.out.println(A.isEmpty());
		System.out.println("Length: " + A.length());
		System.out.println(A); 

		A.dequeue();
		A.dequeue();

		System.out.println(A.isEmpty());
		System.out.println("Length: " + A.length());
		

		A.dequeueAll();
		System.out.println(A.peek());
		System.out.println(A.isEmpty());
		
		
	}
}
