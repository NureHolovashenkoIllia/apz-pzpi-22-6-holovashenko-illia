import { Component, effect, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { CommonModule, NgIf } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import {AuthService} from '../../../core/services/auth';


@Component({
  selector: 'app-header',
  standalone: true,
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
  imports: [MatToolbarModule, MatButtonModule, RouterLink, NgIf, CommonModule]
})
export class HeaderComponent {
  isLoggedIn = signal(false);
  userRole = signal('GUEST');

  constructor(private auth: AuthService, private router: Router) {
    effect(() => {
      this.auth.isLoggedIn$.subscribe(value => this.isLoggedIn.set(value));
      this.auth.userRole$.subscribe(value => this.userRole.set(value));
    });
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
