package se.library.bookapp;

public class Book {

	private int id;
	private String title;
	private String description;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals(Object object) {
		if(!(object instanceof Book)) {
			return false;
		}
		if(((Book) object).getId() != id) {
			return false;
		}
		return true;
	}
}
