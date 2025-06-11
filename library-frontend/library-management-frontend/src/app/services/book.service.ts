import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Book } from '../models/book';
import { BorrowStatus } from '../models/borrow.status';
import { Borrower } from '../models/borrower';
import { ApiResponse } from '../responses/api.response';

@Injectable({
  providedIn: 'root'
})
export class BookService {

  private apiUrl = 'http://localhost:8088/api/v1';

  constructor(private http: HttpClient
  ) { }


  getBooks(): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/books`);
  }

  getBookById(id: number): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/books/${id}`);
  }

  deleteBook(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.apiUrl}/books/${id}`);
  }

  createBook(bookData: FormData): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/books`, bookData);
  }

  updateBook(id: number, bookData: FormData): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.apiUrl}/books/${id}`, bookData);
  }

  getBorrowStatus(): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/borrow_status`);
  }

  createBorrowStatus(borrowStatus: any): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/borrow_status`, borrowStatus);
  }

  updateBorrowStatus(id: number, borrowStatus: any): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.apiUrl}/borrow_status/${id}`, borrowStatus);
  }

  deleteBorrowStatus(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.apiUrl}/borrow_status/${id}`);
  }

  getBorrowers(): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/borrowers`);
  }

  getBorrowerById(id: number): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(`${this.apiUrl}/borrowers/${id}`);
  }

  createBorrower(borrower: any): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.apiUrl}/borrowers`, borrower);
  }

  updateBorrower(id: number, borrower: any): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.apiUrl}/borrowers/${id}`, borrower);
  }

  deleteBorrower(id: number): Observable<ApiResponse> {
    return this.http.delete<ApiResponse>(`${this.apiUrl}/borrowers/${id}`);
  }
}
