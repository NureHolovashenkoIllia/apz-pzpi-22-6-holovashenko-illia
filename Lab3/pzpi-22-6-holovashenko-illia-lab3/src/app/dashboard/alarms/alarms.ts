import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatIconModule } from '@angular/material/icon';
import { Api } from '../../core/services/api';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-alarms',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatTableModule,
    MatIconModule,
    MatButton
  ],
  templateUrl: './alarms.html',
  styleUrls: ['./alarms.scss']
})
export class Alarms implements OnInit {
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private api = inject(Api);

  sensorId!: number;
  alarms: any[] = [];
  displayedColumns: string[] = ['alarmType', 'timeOccurred', 'timeResolved', 'resolved', 'actions'];

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.sensorId = Number(params['sensorId']);
      if (this.sensorId) {
        this.loadAlarms();
      }
    });
  }

  loadAlarms(): void {
    this.http.get<any[]>(this.api.alarms.bySensor(this.sensorId)).subscribe(data => {
      this.alarms = data;
    });
  }

  resolveAlarm(alarm: any): void {
    const updatedAlarm = {
      ...alarm,
      resolved: true,
      timeResolved: new Date().toISOString()
    };

    this.http.put(this.api.alarms.update(alarm.alarmId), updatedAlarm).subscribe(() => {
      this.loadAlarms(); // Перезавантажити список після оновлення
    });
  }
  getRowClass(alarm: any): string {
    return alarm.resolved ? 'resolved-row' : 'unresolved-row';
  }
}
