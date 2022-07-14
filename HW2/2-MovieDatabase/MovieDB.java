import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Genre, Title 을 관리하는 영화 데이터베이스.
 * 
 * MyLinkedList 를 사용해 각각 Genre와 Title에 따라 내부적으로 정렬된 상태를  
 * 유지하는 데이터베이스이다. 
 */
public class MovieDB  {

	MyLinkedList movieList;

    public MovieDB() {
        // FIXME implement this
		movieList = new MyLinkedList<Genre>();
    	
    	// HINT: MovieDBGenre 클래스를 정렬된 상태로 유지하기 위한 
    	// MyLinkedList 타입의 멤버 변수를 초기화 한다.
    }




    public void insert(MovieDBItem item) {
        // FIXME implement this
        // Insert the given item to the MovieDB.
		//1. 장르
		String genre = item.getGenre();
		String title = item.getTitle();
		Genre newGenre = new Genre(genre);


		if (movieList.isEmpty()) {
			newGenre.genreList.add(title);
			movieList.add(newGenre);
			return;
		}


		if (movieList.find(newGenre)!=null) {
			Genre genreFound = (Genre) movieList.find(newGenre);
			if (genreFound.genreList.find(title)!=null) {
				return;
			} else {
				genreFound.genreList.add(title);
			}
		} else {
			newGenre.genreList.add(title);
			movieList.add(newGenre);
		}



    }

    public void delete(MovieDBItem item) {


		String genre = item.getGenre();
		String title = item.getTitle();
		Genre newGenre = new Genre(genre);
		if (movieList.isEmpty()) {
			return;
		}
		if (movieList.find(newGenre)==null) {
			return;
		} else{
			Genre genreFound = (Genre) movieList.find(newGenre);
			if (genreFound.genreList.find(title)!=null) {
				genreFound.genreList.removeItem(title);
				if (genreFound.genreList.numItems==1) {
					movieList.removeItem(genreFound);
				}
			}

		}



    }

    public MyLinkedList<MovieDBItem> search(String term) {
        // FIXME implement this
        // Search the given term from the MovieDB.
        // You should return a linked list of MovieDBItem.
        // The search command is handled at SearchCmd class.


		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		if (movieList.isEmpty()) {
			return results;
		}



		Iterator searchIter = new MyLinkedListIterator(movieList);
		while (searchIter.hasNext()) {
			Genre item = (Genre) searchIter.next();
			Iterator movieIter = new MyLinkedListIterator(item.genreList);
			while (movieIter.hasNext()) {
				String movieTitle = (String) movieIter.next();
				if (movieTitle.contains(term)) {
					MovieDBItem movieDBItem = new MovieDBItem(item.genreName, movieTitle);
					results.add(movieDBItem);
				}
			}
		}


    	
        // This tracing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.



        return results;
    }
    
    public MyLinkedList<MovieDBItem> items() {
        // FIXME implement this
        // Search the given term from the MovieDatabase.
        // You should return a linked list of QueryResult.
        // The print command is handled at PrintCmd class.

		MyLinkedList<MovieDBItem> results = new MyLinkedList<MovieDBItem>();
		Iterator genreIter = new MyLinkedListIterator(movieList);
		while (genreIter.hasNext()) {
			Genre item = (Genre) genreIter.next();
			Iterator movieIter = new MyLinkedListIterator(item.genreList);
			while (movieIter.hasNext()) {
				String movieTitle = (String) movieIter.next();
				MovieDBItem movieDBItem = new MovieDBItem(item.genreName, movieTitle);
				results.add(movieDBItem);
				}
			}

    	// Printing movie items is the responsibility of PrintCmd class. 
    	// So you must not use System.out in this method to achieve specs of the assignment.

    	// Printing functionality is provided for the sake of debugging.
        // This code should be removed before submitting your work.

    	// FIXME remove this code and return an appropriate MyLinkedList<MovieDBItem> instance.
    	// This code is supplied for avoiding compilation error.   


    	return results;
    }
}

class Genre extends Node<String> implements Comparable<Genre> {

	String genreName;
	MyLinkedList<String> genreList;

	public Genre(String name) {
		super(name);
		this.genreName = name;
		genreList = new MyLinkedList<>(this.genreName);
//		throw new UnsupportedOperationException("not implemented yet");
	}
	

	@Override
	public int compareTo(Genre o) {

		return this.genreName.compareTo(o.genreName);

//		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.genreName == null) ? 0 : this.genreName.hashCode());
		return result;

//		throw new UnsupportedOperationException("not implemented yet");
	}


	public boolean equals(Genre obj) {

		return this.genreName==obj.genreName;
//		throw new UnsupportedOperationException("not implemented yet");
	}
}

class MovieList implements ListInterface<String> {	
	public MovieList() {
	}

	@Override
	public Iterator<String> iterator() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void add(String item) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public String first() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void removeAll() {
		throw new UnsupportedOperationException("not implemented yet");
	}
}