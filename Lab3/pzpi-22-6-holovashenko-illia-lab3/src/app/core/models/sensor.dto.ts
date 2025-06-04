export interface SensorDto {
  sensorId: number;
  sensorName: string;
  sensorType: string;
  sensorStatus: 'enabled' | 'disabled';
  buildingId: number;
  dateAdded: string;         // ISO формат, наприклад: '2025-06-04T12:00:00.000Z'
  lastDataReceived: string;  // ISO формат
}
