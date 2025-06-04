import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Api } from './api';
import { Observable, BehaviorSubject } from 'rxjs';

export interface RegisterRequest {
  firstName: string;
  lastName: string;
  phoneNumber: string;
  email: string;
  userPassword: string;
  userRole: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private isLoggedInSubject = new BehaviorSubject<boolean>(!!localStorage.getItem('authToken'));
  private userRoleSubject = new BehaviorSubject<string>(localStorage.getItem('userRole') || 'GUEST');

  isLoggedIn$ = this.isLoggedInSubject.asObservable();
  userRole$ = this.userRoleSubject.asObservable();

  constructor(private http: HttpClient, private api: Api) {}

  register(data: RegisterRequest): Observable<any> {
    return this.http.post(this.api.auth.register, data);
  }

  login(email: string, password: string): Observable<{ token: string; role: string }> {
    return new Observable(observer => {
      this.http.post<{ token: string; role: string }>(this.api.auth.login, null, {
        params: { email, password }
      }).subscribe({
        next: (response) => {
          this.isLoggedInSubject.next(true);
          this.userRoleSubject.next(response.role);
          observer.next(response);
          observer.complete();
        },
        error: (err) => observer.error(err)
      });
    });
  }

  logout(): void {
    localStorage.clear();
    this.isLoggedInSubject.next(false);
    this.userRoleSubject.next('GUEST');
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getRole(): string {
    return localStorage.getItem('userRole') || 'GUEST';
  }
}
