import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import {Api} from '../../core/services/api';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatButton} from '@angular/material/button';
import {MatProgressBar} from '@angular/material/progress-bar';
import {NgIf} from '@angular/common';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-backup',
  templateUrl: './backup.html',
  styleUrls: ['./backup.scss'],
  standalone: true,
  imports: [
    MatCard,
    MatCardTitle,
    MatCardContent,
    MatIcon,
    MatButton,
    MatProgressBar,
    NgIf
  ],
})
export class Backup {
  isLoading = false;

  constructor(
    private http: HttpClient,
    private api: Api,
    private snackBar: MatSnackBar
  ) {}

  downloadBackup(): void {
    const defaultName = `FlameGuard-backup-${new Date().toISOString().slice(0, 10)}.sql`;
    const backupPath = prompt(
      'Введіть шлях до збереження файлу на сервері (наприклад: /backups/FlameGuard.sql):',
      `/backups/${defaultName}`
    );

    if (!backupPath) return;

    this.isLoading = true;

    this.http.post(this.api.admin.systemSettings.backup, {
      backupPath: backupPath
    }, {
      responseType: 'blob'
    }).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = defaultName;
        a.click();
        window.URL.revokeObjectURL(url);
        this.snackBar.open('Бекап збережено локально', 'OK', { duration: 3000 });
        this.isLoading = false;
      },
      error: () => {
        this.snackBar.open('Не вдалося створити або завантажити бекап', 'OK', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }
}
