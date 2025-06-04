import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Api } from '../../core/services/api';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import {SensorDialogComponent} from './sensor-dialog';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-sensors',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './sensors.html',
  styleUrls: ['./sensors.scss']
})
export class Sensors implements OnInit {
  sensors: any[] = [];
  buildingId!: number;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private api: Api,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.buildingId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadSensors();
  }

  loadSensors(): void {
    this.http.get(this.api.sensors.byBuilding(this.buildingId)).subscribe({
      next: data => (this.sensors = data as any[]),
      error: err => console.error(err)
    });
  }

  addSensor(): void {
    const dialogRef = this.dialog.open(SensorDialogComponent, {
      width: '510px',
      data: { buildingId: this.buildingId }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log(result);
        this.http.post(this.api.sensors.add, result).subscribe({
          next: () => this.loadSensors(),
          error: err => console.error('Не вдалося створити сенсор', err)
        });
      }
    });
  }

  editSensor(sensor: any): void {
    const dialogRef = this.dialog.open(SensorDialogComponent, {
      width: '510px',
      data: { ...sensor }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.put(this.api.sensors.update(sensor.sensorId), result).subscribe({
          next: () => this.loadSensors(),
          error: err => console.error('Не вдалося оновити сенсор', err)
        });
      }
    });
  }

  goToAlarms(sensorId: number): void {
    this.router.navigate([`/dashboard/buildings/${this.buildingId}/sensors/alarms`], {
      queryParams: { sensorId }
    });
  }

  goToMeasurements(sensorId: number): void {
    this.router.navigate([`/dashboard/buildings/${this.buildingId}/sensors/measurements`], {
      queryParams: { sensorId }
    });
  }
}
