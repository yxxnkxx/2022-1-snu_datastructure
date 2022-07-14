

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;

public class Matching
{
	static MyHashTable<String, MyPair<Integer>> hashTable = null;

	public static int getHashCode(String input) {
		int hash = 0;
		for (int i=0; i<input.length(); i++) {
			hash += input.charAt(i);
		}
		return hash%100;
	}


	public static void main(String args[])
	{

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("QUIT") == 0)
					break;

				command(input);
			}
			catch (IOException e)
			{
				System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
			}
		}
	}

	private static void command(String input) throws IOException
	{

		char order = input.charAt(0);
        String obj = input.substring(2);


        switch (order) {
			case '<':
				int cnt = 0;
				hashTable = new MyHashTable<String, MyPair<Integer>>();
				BufferedReader br = new BufferedReader(new FileReader(obj));
				while (true) {
					String str = br.readLine();

					if (str == null) {
						break;
					}
					cnt++;

					for (int i = 1; i <= str.length() - 5; i++) {
						String substring = str.substring(i - 1, i + 5);
						int hash = getHashCode(substring);
						MyPair<Integer> myPair = new MyPair<>(cnt, i);
						hashTable.insert(substring, myPair, hash);
					}

				}


				break;
			case '@':

				int hashNum = Integer.parseInt(obj);
				hashTable.printData(hashNum);
				break;

			case '?':
				//search
				ArrayList<MyPair<Integer>> pairs = new ArrayList<>();
				String search = obj;
				int length = search.length();

				for (int i = length-5; i >=1; i --) {
					Boolean[] isUpdate = new Boolean[pairs.size()];
					if (i!=length-5) {
						Arrays.fill(isUpdate, false);
					}


					String substring = search.substring(i - 1, i + 5);
					int hash = getHashCode(substring);
					AVLTree<String, MyPair<Integer>> avlTree = hashTable.search(hash);
					if (avlTree == null) {
						System.out.println("(0, 0)"); //find hash
						return;
					} else {
						if (avlTree.search(substring) == AVLTree.NIL) {
							System.out.println("(0, 0)"); //find key = substring
							return;
						} else {
							MyLinkedList<MyPair<Integer>> searchList = avlTree.search(substring).myLinkedList;
							MyLinkedList<MyPair<Integer>>.Node<MyPair<Integer>> curr = searchList.head;
							while (curr != null) {
								if (i==length-5)	{
									pairs.add(curr.data); } //for first, just add
								else {
									for (int j=0; j<pairs.size(); j++) {
										if (Objects.equals(curr.data.getX(), pairs.get(j).getX()) && curr.data.getY() + 1 == pairs.get(j).getY()) {
											pairs.set(j, curr.data);
											isUpdate[j] = true;
										}
									} // compare one by one, by adding index
								}
								curr = curr.next;
							}

							for (int l=isUpdate.length-1; l>=0;l--) {
								if (isUpdate[l]==false) {
									pairs.remove(l);
								}
							} //not update -> doesn't exist pattern

							if (pairs.isEmpty()) {
								break;
							}

						}

					}
				}
				if (!pairs.isEmpty()) {
					StringBuilder sb = new StringBuilder();
					for (int a=0;a<pairs.size();a++) {
						sb.append(pairs.get(a).getString());
						sb.append(" ");
					}
					System.out.println(sb.toString().trim());
					break;
				}


        }
	}
}





