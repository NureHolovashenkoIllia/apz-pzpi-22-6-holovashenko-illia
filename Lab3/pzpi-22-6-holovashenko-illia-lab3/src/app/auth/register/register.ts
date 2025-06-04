import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../core/services/auth';
import { Router } from '@angular/router';
import { Field } from '../login/login';

@Component({
  selector: 'app-register',
  standalone: true,
  templateUrl: './register.html',
  styleUrls: ['./register.scss'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ]
})
export class Register implements OnInit {
  title = 'Реєстрація';
  submitLabel = 'Зареєструватися';
  errorMessage = '';
  form!: FormGroup;

  fields: Field[] = [
    { name: 'firstName', label: "Ім’я", type: 'text', required: true },
    { name: 'lastName', label: 'Прізвище', type: 'text', required: true },
    { name: 'phoneNumber', label: 'Телефон', type: 'text', required: true },
    { name: 'email', label: 'Email', type: 'email', required: true },
    { name: 'userPassword', label: 'Пароль', type: 'password', required: true },
    {
      name: 'userRole',
      label: 'Роль',
      type: 'select',
      required: true,
      options: [
        { value: 'customer', label: 'Customer' },
        { value: 'SystemAdmin', label: 'System Admin' },
        { value: 'GlobalAdmin', label: 'Global Admin' }
      ]
    }
  ];

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      userPassword: ['', [Validators.required, Validators.minLength(6)]],
      userRole: ['customer', Validators.required]
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.auth.register(this.form.value).subscribe({
        next: () => this.router.navigate(['/login']),
        error: (err) => (this.errorMessage = err.error?.message || 'Помилка реєстрації')
      });
    }
  }
}
