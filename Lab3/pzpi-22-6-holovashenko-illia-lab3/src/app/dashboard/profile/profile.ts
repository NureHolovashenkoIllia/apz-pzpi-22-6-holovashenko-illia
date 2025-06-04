import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Api } from '../../core/services/api';
import { MatCardModule } from '@angular/material/card';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.html',
  styleUrls: ['./profile.scss'],
  imports: [CommonModule, MatCardModule, ReactiveFormsModule, MatInputModule, MatButtonModule]
})
export class Profile implements OnInit {
  user: any = null;
  form!: FormGroup;
  isEditing = false;

  constructor(private http: HttpClient, private api: Api, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      firstName: [''],
      lastName: [''],
      phoneNumber: ['', Validators.required],
      email: [{ value: '', disabled: true }, Validators.required],
      userPassword: ['', Validators.required],
      userRole: [{ value: '', disabled: true }]
    });

    this.http.get(this.api.auth.profile).subscribe({
      next: (data: any) => {
        this.user = data;
        this.form.patchValue(data);
      },
      error: (err) => console.error('Failed to load profile', err)
    });
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
    if (this.isEditing) {
      this.form.patchValue(this.user);
    }
  }

  saveChanges() {
    if (!this.user?.userAccountId || this.form.invalid) {
      console.log(this.user);
      console.warn('Форма невалідна або userId відсутній');
      return;
    }

    const updatedData = {
      ...this.user,
      ...this.form.value
    };

    console.log('Відправка даних на оновлення:', updatedData);

    this.http.put(this.api.users.update(this.user.userAccountId), updatedData).subscribe({
      next: () => {
        this.user = updatedData;
        this.isEditing = false;
        console.log('Профіль оновлено успішно');
      },
      error: (err) => console.error('Не вдалося оновити профіль', err)
    });
  }
}
