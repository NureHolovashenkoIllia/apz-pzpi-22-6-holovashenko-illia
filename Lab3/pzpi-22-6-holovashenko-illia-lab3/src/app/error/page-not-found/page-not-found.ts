import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-page-not-found',
  standalone: true,
  imports: [CommonModule, RouterModule, MatButtonModule],
  templateUrl: './page-not-found.html',
  styleUrls: ['./page-not-found.scss']
})
export class PageNotFound {}
