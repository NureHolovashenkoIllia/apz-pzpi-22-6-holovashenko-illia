import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ManageUsersComponent} from './users/users';
import {AdminLayoutComponent} from '../shared/layout/admin-layout/admin-layout.component';
import {SystemSettings} from './system-settings/system-settings';
import {Backup} from './backup/backup';
import {AdminBuildings} from './buildings/buildings';
import {RoleGuard} from '../core/guards/role-guard';

const routes: Routes = [
  {
    path: '',
    component: AdminLayoutComponent,
    children: [
      {
        path: 'users',
        component: ManageUsersComponent,
        canActivate: [RoleGuard],
        data: { roles: ['global_administrator'] }
      },
      {
        path: 'buildings',
        component: AdminBuildings,
        canActivate: [RoleGuard],
        data: { roles: ['global_administrator'] }
      },
      {
        path: 'settings',
        component: SystemSettings,
        canActivate: [RoleGuard],
        data: { roles: ['database_admin'] }
      },
      {
        path: 'backup',
        component: Backup,
        canActivate: [RoleGuard],
        data: { roles: ['database_admin'] }
      },
      { path: '', redirectTo: 'users', pathMatch: 'full' }
    ]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
