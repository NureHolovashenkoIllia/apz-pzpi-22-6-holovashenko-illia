import { Component } from '@angular/core';
import {MatSidenav, MatSidenavContainer, MatSidenavContent} from '@angular/material/sidenav';
import {RouterLink, RouterLinkActive, RouterOutlet} from '@angular/router';
import {MatListItem, MatNavList} from '@angular/material/list';

@Component({
  selector: 'app-admin-layout',
  templateUrl: './admin-layout.component.html',
  standalone: true,
  imports: [
    MatSidenavContent,
    RouterOutlet,
    MatNavList,
    MatSidenav,
    MatSidenavContainer,
    MatListItem,
    RouterLink,
    RouterLinkActive
  ],
  styleUrls: ['./admin-layout.component.scss']
})
export class AdminLayoutComponent {}
