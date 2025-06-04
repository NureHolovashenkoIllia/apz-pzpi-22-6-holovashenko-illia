import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { Api } from '../../core/services/api';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';

@Component({
  selector: 'app-measurements',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule
  ],
  templateUrl: './measurements.html',
  styleUrls: ['./measurements.scss']
})
export class Measurements implements OnInit {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private api = inject(Api);

  sensorId!: number;
  measurements: any[] = [];
  displayedColumns: string[] = ['dateTimeReceived', 'measurementValue', 'measurementUnit'];

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.sensorId = Number(params['sensorId']);
      if (this.sensorId) {
        this.loadMeasurements();
      }
    });
  }

  loadMeasurements(): void {
    this.http.get<any[]>(this.api.measurements.bySensor(this.sensorId)).subscribe(data => {
      this.measurements = data;
    });
  }

  getRowStyle(measurement: any): string {
    const unit = measurement.measurementUnit?.toLowerCase();
    const value = Number(measurement.measurementValue);

    const criticalSettings: { [unit: string]: { safe: number, warnRange: number, critical: number } } = {
      '°c': { safe: 23, warnRange: 5, critical: 65 },      // температура
      'ppm': { safe: 5, warnRange: 5, critical: 30 },      // газ
      '%': {                                               // може бути або вологість, або дим
        // об'єднаємо, але краще розрізняти за типом сенсора
        safe: 3, warnRange: 5, critical: 20
      }
    };

    const settings = criticalSettings[unit];
    console.log(settings);
    if (!settings) return 'normal';
    console.log(value);
    console.log(settings.safe);
    if (value <= settings.safe) return 'normal';

    const over = value - settings.safe;
    const dangerLevel = over / (Number(settings.critical) - settings.safe);
    console.log(dangerLevel);
    if (dangerLevel < 0.3) return 'warn-light';
    else if (dangerLevel < 0.7) return 'warn-medium';
    else return 'warn-strong';
  }
}
