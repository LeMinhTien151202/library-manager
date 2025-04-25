import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { BookService } from 'src/app/services/book.service';

@Component({
  selector: 'app-edit-book',
  templateUrl: './edit-book.component.html',
  styleUrls: ['./edit-book.component.scss']
})
export class EditBookComponent {
  bookForm: FormGroup;
  bookId: number | null = null;
  currentThumbnail: string | null = null;
  selectedFile: File | null = null;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private bookService: BookService
  ) {
    this.bookForm = this.fb.group({
      title: ['', Validators.required],
      author: ['', Validators.required],
      publicationYear: [null],
      genre: ['']
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.bookId = +id;
        this.loadBook(this.bookId);
      }
    });
  }

  loadBook(id: number): void {
    this.bookService.getBookById(id).subscribe(book => {
      this.bookForm.patchValue({
        title: book.title,
        author: book.author,
        publicationYear: book.publicationYear,
        genre: book.genre
      });
      this.currentThumbnail = book.thumbnail;
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        this.currentThumbnail = e.target?.result as string;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit(): void {
    if (!this.bookId) return;

    const formData = new FormData();
    formData.append('title', this.bookForm.get('title')?.value);
    formData.append('author', this.bookForm.get('author')?.value);
    formData.append('publicationYear', this.bookForm.get('publicationYear')?.value);
    formData.append('genre', this.bookForm.get('genre')?.value);

    if (this.selectedFile) {
      formData.append('thumbnail', this.selectedFile);
    }

    this.bookService.updateBook(this.bookId, formData).subscribe(() => {
      this.router.navigate(['/books']);
    });
  }
}
