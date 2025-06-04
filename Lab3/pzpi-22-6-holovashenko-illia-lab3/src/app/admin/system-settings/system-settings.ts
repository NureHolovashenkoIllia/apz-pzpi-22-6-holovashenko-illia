import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import {Api} from '../../core/services/api';
import {MatButton, MatIconButton} from '@angular/material/button';
import {NgForOf, NgIf} from '@angular/common';
import {MatProgressBar} from '@angular/material/progress-bar';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';
import {FormsModule} from '@angular/forms';
import {MatIcon} from '@angular/material/icon';

@Component({
  selector: 'app-system-settings',
  standalone: true,
  imports: [
    MatFormField,
    MatIconButton,
    NgIf,
    MatProgressBar,
    MatCardTitle,
    MatCardContent,
    MatLabel,
    MatButton,
    FormsModule,
    MatInput,
    MatIcon,
    MatCard,
    NgForOf
  ],
  templateUrl: './system-settings.html',
  styleUrls: ['./system-settings.scss']
})
export class SystemSettings implements OnInit {
  settings: { key: string, value: string }[] = [];
  newKey = '';
  newValue = '';
  isLoading = false;

  constructor(
    private http: HttpClient,
    private api: Api,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadSettings();
  }

  loadSettings(): void {
    this.isLoading = true;
    this.http.get<any[]>(this.api.admin.systemSettings.all).subscribe({
      next: (data) => {
        this.settings = data.map(item => ({
          key: item.settingKey,
          value: item.settingValue
        }));
        this.isLoading = false;
      },
      error: () => {
        this.snackBar.open('Не вдалося завантажити налаштування', 'OK', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  saveSetting(): void {
    if (!this.newKey || !this.newValue) return;

    this.http.post(this.api.admin.systemSettings.create, null, {
      params: { settingKey: this.newKey, settingValue: this.newValue }
    }).subscribe({
      next: () => {
        this.snackBar.open('Налаштування збережено', 'OK', { duration: 3000 });
        this.newKey = '';
        this.newValue = '';
        this.loadSettings();
      },
      error: () => {
        this.snackBar.open('Помилка при збереженні налаштування', 'OK', { duration: 3000 });
      }
    });
  }

  updateSetting(setting: { key: string, value: string }): void {
    const newValue = prompt(`Нове значення для "${setting.key}":`, setting.value);
    if (!newValue) return;

    this.http.patch(this.api.admin.systemSettings.update(setting.key), null, {
      params: { value: newValue }
    }).subscribe({
      next: () => {
        this.snackBar.open('Налаштування оновлено', 'OK', { duration: 3000 });
        this.loadSettings();
      },
      error: () => {
        this.snackBar.open('Помилка при оновленні налаштування', 'OK', { duration: 3000 });
      }
    });
  }
}
