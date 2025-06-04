interface MaintenanceDto {
  maintenanceId: number;
  datePerformed: string;
  workDescription: string;
  cost: number;
  buildingId: number;
  payment?: PaymentDto | null;
}
