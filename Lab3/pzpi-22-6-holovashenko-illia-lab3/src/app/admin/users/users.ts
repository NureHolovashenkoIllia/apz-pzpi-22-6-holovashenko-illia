import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatDialog } from '@angular/material/dialog';
import {MatSnackBar, MatSnackBarModule} from '@angular/material/snack-bar';
import {Api} from '../../core/services/api';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow, MatHeaderRowDef, MatRow, MatRowDef,
  MatTable, MatTableModule
} from '@angular/material/table';
import {MatProgressBar, MatProgressBarModule} from '@angular/material/progress-bar';
import {MatCard, MatCardModule, MatCardTitle} from '@angular/material/card';
import {NgIf} from '@angular/common';
import {MatButtonModule, MatIconButton} from '@angular/material/button';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {AdminLayoutComponent} from '../../shared/layout/admin-layout/admin-layout.component';

interface UserAccount {
  userAccountId: number;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  email: string;
  userPassword: string;
  userRole: string;
}

@Component({
  selector: 'app-manage-users',
  templateUrl: './users.html',
  standalone: true,
  imports: [MatCardModule,
    MatTableModule,
    MatIconModule,
    MatButtonModule,
    MatProgressBarModule,
    MatSnackBarModule,
    MatHeaderCell,
    MatTable,
    MatProgressBar,
    MatCardTitle,
    MatCard,
    MatColumnDef,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    NgIf,
    MatIconButton,
    MatIcon,
    MatHeaderRow,
    MatRow,
    MatRowDef,
    MatHeaderRowDef],
  styleUrls: ['./users.scss']
})
export class ManageUsersComponent implements OnInit {
  users: UserAccount[] = [];
  displayedColumns = ['id', 'name', 'email', 'role', 'actions'];
  isLoading = false;

  constructor(
    private http: HttpClient,
    private api: Api,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.isLoading = true;
    this.http.get<UserAccount[]>(this.api.users.all).subscribe({
      next: users => {
        this.users = users;
        this.isLoading = false;
      },
      error: () => {
        this.snackBar.open('Не вдалося завантажити користувачів', 'OK', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  deleteUser(id: number): void {
    if (!confirm('Ви дійсно хочете видалити користувача?')) return;
    this.http.delete(this.api.users.delete(id)).subscribe({
      next: () => {
        this.snackBar.open('Користувача видалено', 'OK', { duration: 3000 });
        this.loadUsers();
      },
      error: () => {
        this.snackBar.open('Помилка при видаленні користувача', 'OK', { duration: 3000 });
      }
    });
  }

  changeRole(user: UserAccount): void {
    const newRole = prompt(
      'Нова роль (customer, system_administrator, database_admin, global_administrator):',
      user.userRole
    );

    const validRoles = ['customer', 'system_administrator', 'database_admin', 'global_administrator'];
    if (!newRole || !validRoles.includes(newRole)) return;

    this.http.patch(`${this.api.users.updateRole(user.userAccountId)}?role=${newRole}`, null).subscribe({
      next: () => {
        this.snackBar.open('Роль оновлено', 'OK', { duration: 3000 });
        this.loadUsers();
      },
      error: () => {
        this.snackBar.open('Помилка при оновленні ролі', 'OK', { duration: 3000 });
      }
    });
  }
}
