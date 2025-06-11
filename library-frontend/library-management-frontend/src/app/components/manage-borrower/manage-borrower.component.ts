import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Borrower } from 'src/app/models/borrower';
import { ApiResponse } from 'src/app/responses/api.response';
import { BookService } from 'src/app/services/book.service';

@Component({
  selector: 'app-manage-borrower',
  templateUrl: './manage-borrower.component.html',
  styleUrls: ['./manage-borrower.component.scss']
})
export class ManageBorrowerComponent {
  borrowerForm: FormGroup;
  borrowers: Borrower[] = [];
  isEditMode = false;
  currentBorrowerId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private bookService: BookService
  ) {
    this.borrowerForm = this.fb.group({
      name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phone: ['']
    });
  }

  ngOnInit(): void {
    this.loadBorrowers();
  }

  loadBorrowers(): void {
    this.bookService.getBorrowers().subscribe((apiResponse : ApiResponse) => {
      this.borrowers = apiResponse.data;
    });
  }

  onSubmit(): void {
    const borrowerData = {
      name: this.borrowerForm.get('name')?.value,
      email: this.borrowerForm.get('email')?.value,
      phone: this.borrowerForm.get('phone')?.value || null
    };

    if (this.isEditMode && this.currentBorrowerId) {
      this.bookService.updateBorrower(this.currentBorrowerId, borrowerData).subscribe({
        next: () => {
          this.loadBorrowers();
          this.resetForm();
        },
        error: () => {
          console.error('Cập nhật thất bại');
        }
      });
    } else {
      this.bookService.createBorrower(borrowerData).subscribe({
        next: () => {
          this.loadBorrowers();
          this.resetForm();
        },
        error: () => {
          console.error('Thêm thất bại');
        }
      });
    }
  }

  editBorrower(borrower: Borrower): void {
    this.isEditMode = true;
    this.currentBorrowerId = borrower.id;
    this.borrowerForm.patchValue({
      name: borrower.name,
      email: borrower.email,
      phone: borrower.phone
    });
  }

  deleteBorrower(id: number): void {
    if (confirm('Bạn có chắc muốn xóa người mượn này?')) {
      this.bookService.deleteBorrower(id).subscribe({
        next: () => {
          this.loadBorrowers();
          this.resetForm();
        },
        error: (err:any) => {
          if (err.status === 400) {
            console.error('Không thể xóa: Người mượn đang có trạng thái mượn.');
          } else {
            console.error('Xóa thất bại');
          }
        }
      });
    }
  }

  resetForm(): void {
    this.isEditMode = false;
    this.currentBorrowerId = null;
    this.borrowerForm.reset({
      name: '',
      email: '',
      phone: ''
    });
  }

}
