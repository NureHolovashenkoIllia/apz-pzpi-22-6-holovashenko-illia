import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Api} from '../../core/services/api';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {CommonModule, DatePipe, NgForOf, NgIf} from '@angular/common';
import {MatCard, MatCardModule} from '@angular/material/card';
import {MatButton, MatButtonModule} from '@angular/material/button';
import {MatFormField, MatInput, MatLabel} from '@angular/material/input';

@Component({
  selector: 'app-buildings',
  standalone: true,
  templateUrl: './buildings.html',
  styleUrls: ['./buildings.scss'],
  imports: [ReactiveFormsModule, MatFormField, MatLabel, DatePipe, MatCard, MatInput, MatButton, NgForOf, NgIf]
})
export class Buildings implements OnInit {
  buildings: any[] = [];
  user: any = null;
  showAddForm = false;
  buildingForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private api: Api,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.buildingForm = this.fb.group({
      buildingName: ['', [Validators.required, Validators.minLength(2)]],
      buildingDescription: ['', [Validators.required, Validators.minLength(5)]],
      buildingType: ['', Validators.required],
      buildingCondition: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.buildingForm = this.fb.group({
      buildingName: ['', [Validators.required, Validators.minLength(2)]],
      buildingDescription: ['', [Validators.required, Validators.minLength(5)]],
      buildingType: ['', Validators.required],
      buildingCondition: ['', Validators.required]
    });

    this.http.get(this.api.auth.profile).subscribe({
      next: (userData: any) => {
        this.user = userData;
        this.loadBuildings(); // Завантажуй будівлі лише після отримання user
      },
      error: err => console.error('Failed to load user profile', err)
    });

    this.route.queryParams.subscribe(params => {
      if (params['action'] === 'add') {
        this.showAddForm = true;
      }
    });
  }

  loadBuildings(): void {
    this.http.get<any[]>(this.api.buildings.byUser).subscribe({
      next: data => this.buildings = data,
      error: err => console.error('Failed to load buildings', err)
    });
  }

  viewBuilding(buildingId: number): void {
    this.router.navigate(['/dashboard/buildings', buildingId]);
  }

  submitBuilding(): void {
    if (this.buildingForm.invalid || !this.user.userAccountId) return;

    const newBuilding = {
      ...this.buildingForm.value,
      creationDate: new Date().toISOString().split('T')[0],
      userAccountId: this.user.userAccountId
    };

    this.http.post(this.api.buildings.add, newBuilding).subscribe({
      next: () => {
        this.buildingForm.reset();
        this.showAddForm = false;
        this.loadBuildings();
        this.router.navigate([], { queryParams: {} });
      },
      error: err => console.error('Failed to add building', err)
    });
  }

  cancel(): void {
    this.buildingForm.reset();
    this.showAddForm = false;
    this.router.navigate([], { queryParams: {} });
  }
}
