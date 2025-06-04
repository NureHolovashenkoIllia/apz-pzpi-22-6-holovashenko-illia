import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Api } from '../../core/services/api';
import { MatCardModule } from '@angular/material/card';
import {MatButton} from '@angular/material/button';

@Component({
  selector: 'app-maintenance-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButton],
  templateUrl: './maintenances.html',
  styleUrls: ['./maintenances.scss']
})
export class Maintenances implements OnInit {
  maintenances: any[] = [];
  buildingId!: number;

  constructor(private route: ActivatedRoute, private http: HttpClient, private api: Api) {}

  ngOnInit(): void {
    this.buildingId = Number(this.route.snapshot.paramMap.get('id'));

    this.http.get<MaintenanceDto[]>(this.api.maintenances.byBuilding(this.buildingId)).subscribe({
      next: (data) => {
        this.maintenances = data;

        // Для кожного обслуговування перевіряємо, чи є оплата
        this.maintenances.forEach((m) => {
          this.http.get<PaymentDto>(this.api.payments.byMaintenanceId(m.maintenanceId)).subscribe({
            next: (payment) => {
              m.payment = payment;
              console.log(m.payment);
            },
            error: () => {
              m.payment = null;
            }
          });
        });
      },
      error: (err) => console.error(err)
    });
  }

  pay(maintenanceId: number) {
    // Спочатку знайдемо maintenance, щоб взяти суму і опис (якщо потрібно)
    const maintenance = this.maintenances.find(m => m.maintenanceId === maintenanceId);
    if (!maintenance) {
      console.error('Maintenance not found');
      return;
    }

    const total = maintenance.cost;
    const currency = 'USD';
    const description = maintenance.workDescription || 'Оплата обслуговування';

    // Створимо URL із query параметрами
    const url = `${this.api.payments.paypal.create}?total=${total}&currency=${currency}&description=${encodeURIComponent(description)}&maintenanceId=${maintenanceId}`;

    // POST запит (тіло пусте)
    this.http.post<{ approvalUrl: string }>(url, {}).subscribe({
      next: (res) => {
        // Перенаправляємо користувача на approvalUrl для оплати
        window.location.href = res.approvalUrl;
      },
      error: (err) => {
        console.error('Помилка створення платежу', err);
      }
    });
  }
}
