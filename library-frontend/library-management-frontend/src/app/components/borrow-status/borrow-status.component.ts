import { Component, OnInit } from '@angular/core';
import { BorrowStatus } from 'src/app/models/borrow.status';
import { BookService } from 'src/app/services/book.service';

@Component({
  selector: 'app-borrow-status',
  templateUrl: './borrow-status.component.html',
  styleUrls: ['./borrow-status.component.scss']
})
export class BorrowStatusComponent implements OnInit{
  borrowStatuses: BorrowStatus[] = [];

  constructor(private bookService: BookService) {}

  ngOnInit(): void {
    this.loadBorrowStatuses();
  }

  loadBorrowStatuses(): void {
    this.bookService.getBorrowStatus().subscribe((statuses) => {
      this.borrowStatuses = statuses;
    });
  }

}
