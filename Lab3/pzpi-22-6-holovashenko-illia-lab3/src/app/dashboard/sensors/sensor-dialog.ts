import { Component, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import {SensorDto} from '../../core/models/sensor.dto';

@Component({
  selector: 'app-sensor-dialog',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  template: `
    <div>
      <h2>{{ isEdit ? 'Редагувати сенсор' : 'Додати сенсор' }}</h2>
      <form [formGroup]="form" (ngSubmit)="submit()">
        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Назва сенсора</mat-label>
          <input matInput formControlName="sensorName" required />
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Тип сенсора</mat-label>
          <input matInput formControlName="sensorType" required />
        </mat-form-field>

        <mat-form-field appearance="outline" class="full-width">
          <mat-label>Статус</mat-label>
          <mat-select formControlName="sensorStatus" required>
            <mat-option value="enabled">Активний</mat-option>
            <mat-option value="disabled">Вимкнений</mat-option>
          </mat-select>
        </mat-form-field>

        <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
          {{ isEdit ? 'Зберегти' : 'Створити' }}
        </button>
        <button mat-button type="button" (click)="dialogRef.close()">Скасувати</button>
      </form>
    </div>
  `,
  styles: [`
    div {
      background-color: #fefefe;
    }

    h2, form {
      display: flex;
      flex-direction: column;
      gap: 1.5rem;
      padding: 2rem;
      background-color: #fefefe;
      border-radius: 12px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
      width: 100%;
      max-width: 440px;
      margin: 0 auto;
    }

    .full-width {
      width: 100%;
    }

    h2 {
      margin: 0;
      padding: 1rem 2rem 0 2rem;
      font-size: 1.5rem;
      font-weight: 600;
      color: #212121;
    }

    button {
      align-self: flex-start;
    }
  `]
})
export class SensorDialogComponent {
  form: FormGroup;
  isEdit = false;

  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<SensorDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Partial<SensorDto>,
  ) {
    this.isEdit = !!data.sensorId;

    console.log(data)
    this.form = this.fb.group({
      sensorName: [data.sensorName || '', Validators.required],
      sensorType: [data.sensorType || '', Validators.required],
      sensorStatus: [data.sensorStatus || 'enabled', Validators.required],
      buildingId: [data.buildingId, Validators.required]
    });
  }

  submit(): void {
    if (this.form.valid) {
      const now = new Date().toISOString();
      const sensor = {
        ...this.form.value,
        dateAdded: this.isEdit ? this.data.dateAdded : now,
        lastDataReceived: this.isEdit ? this.data.lastDataReceived : now,
      };
      this.dialogRef.close(sensor);
    }
  }
}
