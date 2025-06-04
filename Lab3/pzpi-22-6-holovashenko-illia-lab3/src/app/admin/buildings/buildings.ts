import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { MatSnackBar } from '@angular/material/snack-bar';
import {Api} from '../../core/services/api';
import {MatCard, MatCardContent, MatCardTitle} from '@angular/material/card';
import {MatProgressBar} from '@angular/material/progress-bar';
import {DatePipe, NgForOf, NgIf} from '@angular/common';
import {MatIconButton} from '@angular/material/button';
import {MatIcon} from '@angular/material/icon';
import {MatDialog} from '@angular/material/dialog';
import {BuildingEditDialogComponent} from '../building-edit-dialog/building-edit-dialog.component';

@Component({
  selector: 'app-admin-buildings',
  standalone: true,
  templateUrl: './buildings.html',
  imports: [
    MatCard,
    MatCardTitle,
    MatProgressBar,
    NgIf,
    MatCardContent,
    MatIconButton,
    MatIcon,
    NgForOf,
    DatePipe
  ],
  styleUrls: ['./buildings.scss']
})
export class AdminBuildings implements OnInit {
  buildings: any[] = [];
  isLoading = false;

  constructor(
    private dialog: MatDialog,
    private http: HttpClient,
    private api: Api,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.loadBuildings();
  }

  loadBuildings(): void {
    this.isLoading = true;
    this.http.get<any[]>(this.api.buildings.all).subscribe({
      next: (data) => {
        this.buildings = data;
        this.isLoading = false;
      },
      error: () => {
        this.snackBar.open('Помилка завантаження будівель', 'OK', { duration: 3000 });
        this.isLoading = false;
      }
    });
  }

  updateBuilding(building: any): void {
    const dialogRef = this.dialog.open(BuildingEditDialogComponent, {
      width: '500px',
      data: building
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.http.put(this.api.buildings.update(building.buildingId), { ...building, ...result }).subscribe({
          next: () => {
            this.snackBar.open('Будівлю оновлено', 'OK', { duration: 3000 });
            this.loadBuildings();
          },
          error: () => {
            this.snackBar.open('Помилка оновлення будівлі', 'OK', { duration: 3000 });
          }
        });
      }
    });
  }

  deleteBuilding(id: number): void {
    if (!confirm('Ви дійсно хочете видалити цю будівлю?')) return;

    this.http.delete(this.api.buildings.delete(id)).subscribe({
      next: () => {
        this.snackBar.open('Будівлю видалено', 'OK', { duration: 3000 });
        this.loadBuildings();
      },
      error: () => {
        this.snackBar.open('Помилка видалення', 'OK', { duration: 3000 });
      }
    });
  }

  addMaintenance(buildingId: number): void {
    const workDescription = prompt('Опис роботи обслуговування:');
    if (!workDescription) return;

    const maintenance = {
      buildingId,
      datePerformed: new Date().toISOString(),
      workDescription,
      cost: null
    };

    this.http.post(this.api.maintenances.add, maintenance).subscribe({
      next: () => {
        this.snackBar.open('Обслуговування додано', 'OK', { duration: 3000 });
      },
      error: () => {
        this.snackBar.open('Помилка додавання обслуговування', 'OK', { duration: 3000 });
      }
    });
  }
}
