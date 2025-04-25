import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Book } from '../models/book';
import { BorrowStatus } from '../models/borrow.status';
import { Borrower } from '../models/borrower';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private apiUrl = 'http://localhost:8088/api/v1';

  constructor(private http: HttpClient
  ) { }


  getBooks(): Observable<Book[]> {
    return this.http.get<Book[]>(`${this.apiUrl}/books`);
  }

  getBookById(id: number): Observable<Book> {
    return this.http.get<Book>(`${this.apiUrl}/books/${id}`);
  }

  deleteBook(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/books/${id}`);
  }

  createBook(bookData: FormData): Observable<Book> {
    return this.http.post<Book>(`${this.apiUrl}/books`, bookData);
  }

  updateBook(id: number, bookData: FormData): Observable<Book> {
    return this.http.put<Book>(`${this.apiUrl}/books/${id}`, bookData);
  }

  getBorrowStatus(): Observable<BorrowStatus[]> {
    return this.http.get<BorrowStatus[]>(`${this.apiUrl}/borrow_status`);
  }

  createBorrowStatus(borrowStatus: any): Observable<BorrowStatus> {
    return this.http.post<BorrowStatus>(`${this.apiUrl}/borrow_status`, borrowStatus);
  }

  updateBorrowStatus(id: number, borrowStatus: any): Observable<BorrowStatus> {
    return this.http.put<BorrowStatus>(`${this.apiUrl}/borrow_status/${id}`, borrowStatus);
  }

  deleteBorrowStatus(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/borrow_status/${id}`);
  }

  getBorrowers(): Observable<Borrower[]> {
    return this.http.get<Borrower[]>(`${this.apiUrl}/borrowers`);
  }

  getBorrowerById(id: number): Observable<Borrower> {
    return this.http.get<Borrower>(`${this.apiUrl}/borrowers/${id}`);
  }

  createBorrower(borrower: any): Observable<Borrower> {
    return this.http.post<Borrower>(`${this.apiUrl}/borrowers`, borrower);
  }

  updateBorrower(id: number, borrower: any): Observable<Borrower> {
    return this.http.put<Borrower>(`${this.apiUrl}/borrowers/${id}`, borrower);
  }

  deleteBorrower(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/borrowers/${id}`);
  }
}
