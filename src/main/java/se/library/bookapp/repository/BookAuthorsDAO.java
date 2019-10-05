package se.library.bookapp.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import se.library.bookapp.entities.BookWithAuthors;

import java.util.List;

@Transactional
@Repository
public class BookAuthorsDAO implements CRUDEntity<BookWithAuthors> {

    @Override
    public int create(BookWithAuthors bookWithAuthors) {
        return 0;
    }

    @Override
    public int update(BookWithAuthors bookWithAuthors) {
        return 0;
    }

    @Override
    public int delete(int id) {
        return 0;
    }

    @Override
    public BookWithAuthors find(int id) {
        return null;
    }

    @Override
    public List<BookWithAuthors> findAll() {
        return null;
    }
}
