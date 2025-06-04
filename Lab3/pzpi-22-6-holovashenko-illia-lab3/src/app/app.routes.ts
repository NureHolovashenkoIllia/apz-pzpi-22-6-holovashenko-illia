import { Routes } from '@angular/router';
import {AuthGuard} from './core/guards/auth-guard';
import {RoleGuard} from './core/guards/role-guard';
import {PageNotFound} from './error/page-not-found/page-not-found';
import {DashboardRoutingModule} from './dashboard/dashboard-routing-module';
import {AdminRoutingModule} from './admin/admin-routing-module';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },

  {
    path: 'login',
    loadComponent: () => import('./auth/login/login').then(m => m.Login)
  },
  {
    path: 'register',
    loadComponent: () => import('./auth/register/register').then(m => m.Register)
  },

  {
    path: 'dashboard',
    canActivate: [AuthGuard],
    loadChildren: () => import('./dashboard/dashboard-routing-module').then(m => m.DashboardRoutingModule)
  },

  {
    path: 'admin',
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['global_administrator', 'database_admin', 'system_administrator'] },
    loadChildren: () => import('./admin/admin-routing-module').then(m => m.AdminRoutingModule)
  },

  {
    path: 'unauthorized',
    loadComponent: () => import('./error/unauthorized/unauthorized').then(m => m.Unauthorized)
  },

  {
    path: '**',
    loadComponent: () => import('./error/page-not-found/page-not-found').then(m => m.PageNotFound)
  }
];
