import { Component, OnInit } from '@angular/core';
import { Book } from 'src/app/models/book';
import { ApiResponse } from 'src/app/responses/api.response';
import { BookService } from 'src/app/services/book.service';

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrls: ['./book-list.component.scss']
})
export class BookListComponent implements OnInit{
  books: Book[] = [];

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.loadBooks();
  }

  loadBooks(): void {
    this.bookService.getBooks().subscribe((apiResponse : ApiResponse) => {
      debugger
      this.books = apiResponse.data;
    });
  }

  deleteBook(id: number): void {
    if (confirm('Bạn có chắc muốn xóa sách này?')) {
      this.bookService.deleteBook(id).subscribe(() => {
        this.loadBooks();
      });
    }
  }

}
