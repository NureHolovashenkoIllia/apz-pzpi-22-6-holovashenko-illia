import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'overview',
    pathMatch: 'full'
  },
  {
    path: 'overview',
    loadComponent: () => import('./overview/overview').then(m => m.Overview)
  },
  {
    path: 'buildings',
    loadComponent: () => import('./buildings/buildings').then(m => m.Buildings)
  },
  {
    path: 'buildings/:id',
    loadComponent: () => import('./building-details/building-details').then(m => m.BuildingDetailsComponent)
  },
  {
    path: 'buildings/:id/sensors',
    loadComponent: () => import('./sensors/sensors').then(m => m.Sensors)
  },
  {
    path: 'buildings/:id/sensors/alarms',
    loadComponent: () => import('./alarms/alarms').then(m => m.Alarms)
  },
  {
    path: 'buildings/:id/sensors/measurements',
    loadComponent: () => import('./measurements/measurements').then(m => m.Measurements)
  },
  {
    path: 'buildings/:id/maintenances',
    loadComponent: () => import('./maintenances/maintenances').then(m => m.Maintenances)
  },
  {
    path: 'profile',
    loadComponent: () => import('./profile/profile').then(m => m.Profile)
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DashboardRoutingModule {}
