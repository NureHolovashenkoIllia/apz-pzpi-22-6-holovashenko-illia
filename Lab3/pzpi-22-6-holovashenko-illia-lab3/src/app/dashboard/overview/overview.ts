import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import {Router, RouterLink} from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Api } from '../../core/services/api';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-overview',
  standalone: true,
  templateUrl: './overview.html',
  styleUrls: ['./overview.scss'],
  imports: [CommonModule, MatCardModule, MatButtonModule, RouterLink]
})
export class Overview implements OnInit {
  buildingsCount = 0;
  sensorsCount = 0;

  constructor(private http: HttpClient, private api: Api, private router: Router) {}

  ngOnInit(): void {
    this.loadOverviewStats();
  }

  private loadOverviewStats(): void {
    this.http.get<any[]>(this.api.buildings.byUser).subscribe(buildings => {
      this.buildingsCount = buildings.length;

      const sensorRequests = buildings.map(b =>
        this.http.get<any[]>(this.api.sensors.byBuilding(b.buildingId))
      );

      forkJoin(sensorRequests).subscribe(results => {
        this.sensorsCount = results.reduce((sum, arr) => sum + arr.length, 0);
      });
    });
  }

  addBuilding(): void {
    this.router.navigate(['/dashboard/buildings'], {
      queryParams: { action: 'add' }
    });
  }
}
