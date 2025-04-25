import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Book } from 'src/app/models/book';
import { BorrowStatus } from 'src/app/models/borrow.status';
import { Borrower } from 'src/app/models/borrower';
import { BookService } from 'src/app/services/book.service';

@Component({
  selector: 'app-manage-borrow-status',
  templateUrl: './manage-borrow-status.component.html',
  styleUrls: ['./manage-borrow-status.component.scss']
})
export class ManageBorrowStatusComponent {
  borrowStatusForm: FormGroup;
  books: Book[] = [];
  borrowers: Borrower[] = [];
  borrowStatuses: BorrowStatus[] = [];
  isEditMode = false;
  currentBorrowId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private bookService: BookService
  ) {
    this.borrowStatusForm = this.fb.group({
      book_id: ['', Validators.required],
      borrower_id: ['', Validators.required],
      borrow_date: ['', Validators.required],
      return_date: [''],
      status: ['borrowed', Validators.required]
    });
  }

  ngOnInit(): void {
    this.loadBooks();
    this.loadBorrowers();
    this.loadBorrowStatuses();
  }

  loadBooks(): void {
    this.bookService.getBooks().subscribe(books => {
      this.books = books;
    });
  }

  loadBorrowers(): void {
    this.bookService.getBorrowers().subscribe(borrowers => {
      this.borrowers = borrowers;
    });
  }

  loadBorrowStatuses(): void {
    this.bookService.getBorrowStatus().subscribe(statuses => {
      this.borrowStatuses = statuses;
    });
  }

  onSubmit(): void {
    debugger
    const borrowStatusData = {
      book_id: this.borrowStatusForm.get('book_id')?.value,
      borrower_id: this.borrowStatusForm.get('borrower_id')?.value,
      borrow_date: this.borrowStatusForm.get('borrow_date')?.value,
      return_date: this.borrowStatusForm.get('return_date')?.value || null,
      status: this.borrowStatusForm.get('status')?.value
    };

    if (this.isEditMode && this.currentBorrowId) {
      this.bookService.updateBorrowStatus(this.currentBorrowId, borrowStatusData).subscribe(() => {
        debugger
        this.loadBorrowStatuses();
        this.resetForm();
      });
    } else {
      this.bookService.createBorrowStatus(borrowStatusData).subscribe(() => {
        debugger
        this.loadBorrowStatuses();
        this.resetForm();
      });
    }
  }

  editBorrowStatus(status: BorrowStatus): void {
    this.isEditMode = true;
    this.currentBorrowId = status.borrowId;
    this.borrowStatusForm.patchValue({
      book_id: status.book.id,
      borrower_id: status.borrower.id,
      borrow_date: status.borrowDate,
      retur_date: status.returnDate,
      status: status.status
    });
  }

  deleteBorrowStatus(id: number): void {
    if (confirm('Bạn có chắc muốn xóa trạng thái mượn này?')) {
      this.bookService.deleteBorrowStatus(id).subscribe(() => {
        this.loadBorrowStatuses();
        this.resetForm();
      });
    }
  }

  resetForm(): void {
    this.isEditMode = false;
    this.currentBorrowId = null;
    this.borrowStatusForm.reset({
      bookId: '',
      borrowerId: '',
      borrowDate: '',
      returnDate: '',
      status: 'borrowed'
    });
  }

}
