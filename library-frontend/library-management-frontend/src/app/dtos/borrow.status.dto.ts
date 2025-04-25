export interface BorrowStatusDTO {
  book_id: number;
  borrower_id: number;
  borrow_date: string;
  return_date: string | null;
  status: 'borrowed' | 'returned';
  createdAt: string;
}
