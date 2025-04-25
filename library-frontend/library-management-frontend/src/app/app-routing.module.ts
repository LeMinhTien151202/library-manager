import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BookListComponent } from './components/book-list/book-list.component';
import { AddBookComponent } from './components/add-book/add-book.component';
import { EditBookComponent } from './components/edit-book/edit-book.component';
import { BorrowStatusComponent } from './components/borrow-status/borrow-status.component';
import { ManageBorrowStatusComponent } from './components/manage-borrow-status/manage-borrow-status.component';
import { ManageBorrowerComponent } from './components/manage-borrower/manage-borrower.component';

const routes: Routes = [
  { path: '', redirectTo: '/books', pathMatch: 'full' },
  { path: 'books', component: BookListComponent },
  { path: 'add-book', component: AddBookComponent },
  { path: 'edit-book/:id', component: EditBookComponent },
  { path: 'borrow-status', component: BorrowStatusComponent },
  { path: 'manage-borrow-status', component: ManageBorrowStatusComponent },
  { path: 'manage-borrower', component: ManageBorrowerComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
