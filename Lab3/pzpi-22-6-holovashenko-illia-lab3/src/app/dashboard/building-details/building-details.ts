// src/app/dashboard/building-details/building-details.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Api } from '../../core/services/api';
import { MatCardModule } from '@angular/material/card';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-building-details',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatListModule, MatButtonModule],
  templateUrl: './building-details.html',
  styleUrls: ['./building-details.scss']
})
export class BuildingDetailsComponent implements OnInit {
  buildingId!: number;
  building: any;
  sensors: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private api: Api
  ) {}

  ngOnInit(): void {
    this.buildingId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.buildingId) {
      this.loadBuilding();
      this.loadSensors();
    }
  }

  loadBuilding(): void {
    this.http.get(this.api.buildings.byId(this.buildingId)).subscribe({
      next: data => (this.building = data),
      error: err => console.error('Failed to load building', err)
    });
  }

  loadSensors(): void {
    this.http.get<any[]>(this.api.sensors.byBuilding(this.buildingId)).subscribe({
      next: data => (this.sensors = data),
      error: err => console.error('Failed to load sensors', err)
    });
  }

  goToMaintenances(): void {
    this.router.navigate([`/dashboard/buildings/${this.buildingId}/maintenances`]);
  }

  goToSensors(): void {
    this.router.navigate([`/dashboard/buildings/${this.buildingId}/sensors`]);
  }
}
