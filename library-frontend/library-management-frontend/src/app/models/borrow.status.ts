import { Book } from "./book";
import { Borrower } from "./borrower";

export interface BorrowStatus {
  borrowId: number;
  book: Book;
  borrower: Borrower;
  borrowDate: string;
  returnDate: string | null;
  status: 'borrowed' | 'returned';
  createdAt: string;
}
