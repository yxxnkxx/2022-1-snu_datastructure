import javax.print.attribute.standard.PrinterMakeAndModel;
import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[]) throws IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue, 0, newvalue.length-1); //change for merge sort
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue, 0, newvalue.length-1);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
                    System.out.println((System.currentTimeMillis() - t) + " ms");
				}
				else
				{
					StringBuilder sb = new StringBuilder();
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						sb.append(newvalue[i]);
						sb.append("\n");
					}
					System.out.print(sb);
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	private static void swap(int[] value, int i, int j) {
		int temp= value[i];
		value[i] = value[j];
		value[j] = temp;

	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoBubbleSort(int[] value)
	{
		//book p.277 / p.318
		int n = value.length;
		boolean swapped; //flag
		for (int last=n-1; last>=1; last--) {
			swapped = false;
			for (int i=0; i<last; i++) {
				if (value[i] > value[i+1]) {
					swap(value, i, i+1);
					swapped = true;
				}
			}
			if (!swapped) {
				//all sorted
				break;
			}
		}
		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{

		int n = value.length;

		// book p.281
		for (int i=1; i<n; i++) {
			int j=i-1;
			int newItem=value[i]; ///value[0...i-1] is already sorted
			while (j>=0 && newItem < value[j]) {
				value[j+1] = value[j];
				j--;
			}
			value[j+1] = newItem;
		}


		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void percolateDown(int[] value, int i, int numItems){
		//book chapter 8 p.266
		int child = 2*i+1;
		int rightChild = 2*i+2;
		if (child <= numItems-1) {
			if (rightChild<=numItems-1 && value[child] < value[rightChild]) {
				child = rightChild;
			}
			if (value[i] < value[child]) {
				swap(value, i, child);
				percolateDown(value, child, numItems);
			}
		}
	}
	private static void buildHeap(int[] value) {
		//book chapter 8 p.266, p.321
		int numItems = value.length;
		if (numItems >=2) {
			for (int i = (numItems - 2) / 2; i >= 0; i--) {
				percolateDown(value, i, numItems);
			}
		}
	}

	private static int deleteMax(int[] value, int numItems) {
		//book chapter 8 p.266
		//not handle if array is empty
		int max = value[0];
		int num = numItems;
		value[0] = value[num - 1];
		num--;
		percolateDown(value, 0, num);
		return max;
	}

	private static int[] DoHeapSort(int[] value)
	{
		//book p.300
		buildHeap(value);
		int result[] = new int[value.length];
		int n = value.length;
		for (int i=n-1; i>=1; i--) {
			result[i] = deleteMax(value, i+1);
		}
		result[0] = value[0];
		return (result);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static void merge(int[] value, int p, int q, int r, int[] B) {
		//book p.285, p.319
		int i=p;
		int j=q+1;
		int t=0;
		while (i <= q && j <= r) {
			if (value[i] <= value[j]) {
				B[t++] = value[i++];
			} else {
				B[t++] = value[j++];
			}
		}
		while (i <= q) {
			B[t++] = value[i++];
		}
		while (j <= r) {
			B[t++] = value[j++];
		}
		i = p;
		t = 0;
		while (i <= r) {
			value[i++] = B[t++];
		}
	}

	private static void mSort(int[] value, int[] B, int p, int r) {
		if (p < r) {
			int q = (p+r)/2;
			mSort(value, B, p, q);
			mSort(value, B,q+1, r);
			merge(value, p, q, r, B);
		}
	}

	private static int[] DoMergeSort(int[] value, int p, int r)
	{
		//book p.284, p.319

		int[] B = new int[value.length];
		mSort(value, B, p, r);
		return value;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int partition(int[] value, int p, int r) {
		//book p.291
		int x = value[r];
		int i= p-1;
		for (int j=p; j<=r-1; j++) {
			if (value[j] <= x || (value[j]==x && Math.random()<0.5)) { //for same inputs
				i++;
				swap(value, i, j);
			}
		}
		swap(value, i+1, r);
		return i+1;
	}


	private static int[] DoQuickSort(int[] value, int p, int r)
	{
		//book p.290
		if (p < r) {
			int q = partition(value, p, r);
			value = DoQuickSort(value, p, q-1);
			value = DoQuickSort(value, q+1, r);
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] rSort(int[] value) {
		//book p.322
		int[] cnt = new int[10];
		int[] start = new int[10];
		int[] B = new int[value.length];
		int max = -1;
		for (int i = 0 ; i < value.length; i++) {
			if (value[i] > max) {
				max = value[i];
			}
		}
		int numDigits = (int)Math.log10(max)+1; //최대 자릿수
		for (int digit = 1; digit<=numDigits; digit++) { //1의 자릿수부터 최대자릿수까지
			for (int d=0; d<=9; d++) {
				cnt[d]=0;  						//cnt배열 초기화
			}
			for (int i=0; i<value.length; i++) {
				cnt[(int)(value[i]/Math.pow(10, digit-1))%10]++; //각 자릿수에 해당하는 값이 몇개인지 업데이트
			}
			start[0]=0;
			for (int d=1; d<=9; d++) {
				start[d] = start[d-1] + cnt[d-1];		//1부터 9까지 존재하는 데이터 누적합 counting sort 활용
				//start배열의 값이 그 값의 index, 그 값의 cnt는 다음 배열에 반영
			}
			for (int i=0; i<value.length; i++) {
				B[start[(int)(value[i]/Math.pow(10,digit-1)%10)]++] = value[i];

				//value[i]값을 start 배열의 값=index에 넣어 정렬시킴
				//배열에 넣은 후에 +1을 해주어 중복 원소 처리
			}
			for (int i=0; i<value.length; i++) {
				value[i] = B[i]; //value 배열에 업데이트
			}
		}

		return (value);
	}



	private static int[] DoRadixSort(int[] value)
	{
		int cnt=0;
		for (int i=0; i<value.length; i++) {
			if (value[i]<0) {
				cnt++;
			}
		}

		int[] positive = new int[value.length-cnt];
		int[] negative = new int[cnt];
		int p_idx = 0;
		int n_idx = 0;
		for (int i=0; i<value.length; i++) {
			if (value[i] >= 0) {
				positive[p_idx] = value[i];
				p_idx++;
			} else {
				negative[n_idx] = -value[i];
				n_idx++;
			}
		}


		positive = rSort(positive);
		negative = rSort(negative);

		for (int i=0; i<value.length; i++) {
			if (i <= negative.length-1) {
				value[i] = -negative[negative.length-1-i];
			} else {
				value[i] = positive[i-negative.length];
			}
		}



		return value;
	}
}
